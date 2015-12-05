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

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.controller.service.MessageService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;

	/**
	 * Constructor for testing purposes
	 * 
	 * @param messageService
	 * @param userService
	 */
	@Autowired
	public MessageController(MessageService messageService, UserService userService) {
		this.messageService = messageService;
		this.userService = userService;
	}

	/**
	 * Maps the /messages page to the {@code messagesOverview.html}.
	 * 
	 * @param authUser
	 *            actually logged in user, is used to get the users messages
	 *            information and shows it to the user to allow reading them
	 * @param view
	 *            when null the unread messages are shown, else the
	 *            corresponding messages (possibilities: {@code inbox},
	 *            {@code outbox}, {@code unread}, and everything else is as
	 *            default to unread)
	 * @param show
	 *            the index of the message which should be shown has in the
	 *            messageList object
	 * @return ModelAndView for Spring framework with the users messagesOverview
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public ModelAndView messages(Principal authUser, @RequestParam(value = "view", required = false) String view,
			@RequestParam(value = "show", required = false) Long show) {
		ModelAndView model = new ModelAndView("messagesOverview");

		User tmpUser = userService.getUserByPrincipal(authUser);

		if (view == null) {
			return new ModelAndView("redirect:messages?view=" + ConstantVariables.UNREAD);
		}
		
		List<Message> tmpMessageList;
		if (view.equals(ConstantVariables.INBOX)) {
			tmpMessageList = messageService.getMessageByBox(ConstantVariables.INBOX, tmpUser);
		} else if (view.equals(ConstantVariables.OUTBOX)) {
			tmpMessageList = messageService.getMessageByBox(ConstantVariables.OUTBOX, tmpUser);
		} else {
			tmpMessageList = messageService.getMessageByBox(ConstantVariables.UNREAD, tmpUser);
		}

		// marks message as read
		if (show != null) {
			if (show < tmpMessageList.size()) {
				Message tmpMessage = tmpMessageList.get(show.intValue());
				if (tmpUser.equals(tmpMessage.getReceiver())) {
					tmpMessage = messageService.markMessageAsRead(tmpMessage.getId(), tmpUser);
					tmpMessageList.set(show.intValue(), tmpMessage);
				}
			} else if ( show > 0) {
				show--;
				return new ModelAndView("redirect:messages?view=" + view + "&show=" + show);
			} else {
				return new ModelAndView("redirect:messages?view=" + view);
			}
		}
		model.addObject("messageList", tmpMessageList);
		model.addObject("authUser", tmpUser);

		return model;
	}

	/**
	 * Maps the /newMessage page to the {@code newMessage.html}.
	 * 
	 * @param authUser
	 *            is necessary for controlling that a user does not send a
	 *            message to himself
	 * @param req
	 *            holds the {@code receiverId}, is necessary to know which user
	 *            is the receiver of the message
	 * @return ModelAndView for Spring framework with a newMessage and a new
	 *         messageForm
	 */
	@RequestMapping(value = "/newMessage", params = "receiver", method = RequestMethod.GET)
	public ModelAndView newMessage(Principal authUser, final HttpServletRequest req) {
		ModelAndView model;
		//FIXME when the receiverId does not exist an error is caused userService.getUserById (assertionError)
		User tmpUser = userService.getUserByPrincipal(authUser);

		final Long receiverId = Long.valueOf(req.getParameter("receiver"));

		if (tmpUser.getId() != receiverId) {
			MessageForm messageForm = new MessageForm();
			messageForm.setReceiver(userService.getUserById(receiverId));
			messageForm.setReceiverId(receiverId);
			model = new ModelAndView("newMessage");
			model.addObject("messageForm", messageForm);
		} else {
			model = new ModelAndView("redirect:findTutor");
		}

		return model;
	}

	/**
	 * Maps the /sendMessage page and handles it redirection depending on the
	 * users input
	 * 
	 * @param authUser
	 *            is necessary to set the sender of the message
	 * @param messageForm
	 *            holds the users input and must be valid
	 * @param result
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public ModelAndView sendMessage(Principal authUser, @Valid MessageForm messageForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model;
		if (!result.hasErrors()) {
			messageService.saveFrom(messageForm, authUser);
			model = new ModelAndView("redirect:messages?view=" + ConstantVariables.OUTBOX + "&show=0");
		} else {
			model = new ModelAndView("newMessage");

			User tmpUser = userService.getUserByPrincipal(authUser);
			model.addObject("authUser", tmpUser);
			messageForm.setReceiver(userService.getUserById(messageForm.getReceiverId()));
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
	private void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

}