package ch.unibe.ese.Tutorfinder.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the upload picture process.
 * Solves the saving and overwriting process for the users profile picture.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Controller
public class PictureController {

	@Autowired
	UserService userService;
	@Autowired
	PrepareFormService prepareFormService;
	
	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(Principal authUser, @RequestParam("file") MultipartFile file) {
		ModelAndView model;
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("user.dir");
				User tmpUser = userService.getUserByPrincipal(authUser);
				File dir = new File(rootPath + File.separator + "src" + File.separator + "main" + File.separator
						+ "webapp" + File.separator + "img" + File.separator + "profPic");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + tmpUser.getId() + ".png");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				model = new ModelAndView("updateProfile");
				// TODO show success massage to the user
			} catch (Exception e) {
				model = new ModelAndView("updateProfile");
				model.addObject("page_error", e.getMessage());
			}
		} else {
			model = new ModelAndView("updateProfile");
			// TODO show error massage to the user
		}
		model = prepareFormService.prepareForm(authUser, model);
		return model;
	}

}
