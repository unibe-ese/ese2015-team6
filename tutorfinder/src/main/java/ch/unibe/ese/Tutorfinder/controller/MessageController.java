package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidMessageException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.controller.service.MessageService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Controller
public class MessageController {

	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;

	/**
	 * Maps the /messages page to the {@code messagesOverview.html}.
	 * 
	 * @param authUser
	 *            actually logged in user, is used to get the users messages
	 *            information and shows it to the user to allow reading them
	 * @param inbox
	 *            is not null when the inbox needs to be shown
	 * @param outbox
	 *            is not null when the outbox needs to be shown
	 * @return ModelAndView for Spring framework with the users messagesOverview
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public ModelAndView messages(Principal authUser, @RequestParam(value = "view", required = true) String view,
			@RequestParam(value = "show", required = false) Long show) {
		ModelAndView model = new ModelAndView("messagesOverview");

		User tmpUser = userService.getUserByPrincipal(authUser);
		
		List<Message> tmpMessageList;
		if (view.equals(ConstantVariables.INBOX)) {
			tmpMessageList =  messageService.getMessageByBox(ConstantVariables.INBOX, tmpUser);
		} else if (view.equals(ConstantVariables.OUTBOX)) {
			tmpMessageList =  messageService.getMessageByBox(ConstantVariables.OUTBOX, tmpUser);
		} else {
			tmpMessageList =  messageService.getMessageByBox(ConstantVariables.UNREAD, tmpUser);
		}
		model.addObject("messageList", tmpMessageList);
		
		//marks message as read
		if (show != null) {
			Message tmpMessage = tmpMessageList.get(show.intValue());
			if (tmpUser != tmpMessage.getReceiver()) {
			messageService.markMessageAsRead(tmpMessage.getId(), tmpUser);
			}
		}
		model.addObject("authUser", tmpUser);

		return model;
	}
	
	@RequestMapping(value = "/message")
	public String messages() {
		return "forward:/messages?view=unread";
	}

	@RequestMapping(value = "/newMessage", params = "receiver", method = RequestMethod.GET)
	public ModelAndView newMessage(Principal authUser, final HttpServletRequest req) {
		ModelAndView model;

		User tmpUser = userService.getUserByPrincipal(authUser);
		
		final Long receiverId = Long.valueOf(req.getParameter("receiver"));
		
		if (tmpUser.getId() != receiverId) {
		MessageForm messageForm = new MessageForm();
		messageForm.setReceiver(userService.getUserById(receiverId));
		messageForm.setReceiverId(receiverId);
		model = new ModelAndView("newMessage");
		model.addObject("messageForm", messageForm);
		model.addObject("authUser", tmpUser);
		} else {
			model = new ModelAndView("findTutor");
		}

		return model;
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public ModelAndView sendMessage(Principal authUser, @Valid MessageForm messageForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView("messagesOverview");
		if (!result.hasErrors()) {
			try {
				messageService.saveFrom(messageForm, authUser);
				model = new ModelAndView("redirect:messages?view="+ ConstantVariables.OUTBOX+"&show=0");

			} catch (InvalidMessageException e) {
				model = new ModelAndView("newMessage");
				model.addObject("messageForm", messageForm);
				model.addObject("authUser", userService.getUserByPrincipal(authUser));
				model.addObject("page_error", e.getMessage());
				//TODO show error message
			}
		} else {
			User tmpUser = userService.getUserByPrincipal(authUser);
			model.addObject("authUser", tmpUser);

			model.addObject("messageForm", messageForm);
		}

		return model;
	}
	
	/**
	 * Converts empty fields to null values
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

}
