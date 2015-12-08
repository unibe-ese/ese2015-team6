package ch.unibe.ese.Tutorfinder.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * the upload picture process. Solves the saving and overwriting process for the
 * users profile picture.
 * 
 * @version 2.0
 *
 */
@Controller
public class PictureController {

	@Autowired
	private UserService userService;
	@Autowired
	private PrepareFormService prepareFormService;

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(Principal authUser, @RequestParam("file") MultipartFile file) {
		ModelAndView model = new ModelAndView("updateProfile");
		if (!file.isEmpty()) {
			byte[] bytes = null;
			File serverFile = null;

			try {
				bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("user.dir");
				User tmpUser = userService.getUserByPrincipal(authUser);
				File dir = new File(rootPath + File.separator + "src" + File.separator + "main" + File.separator
						+ "webapp" + File.separator + "img" + File.separator + "profPic");
				if (!dir.exists()) {
					boolean directoryCreation = dir.mkdirs();
					assert directoryCreation : "Directory is not created!";
				}
				serverFile = new File(dir.getAbsolutePath() + File.separator + tmpUser.getId() + ".png");

			} catch (IOException e) {
				model.addObject("page_error", e.getMessage());
			}
			// Try only if the first try block was successful
			if (bytes != null && serverFile != null) {
				try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));) {
					// Create the file on server
					stream.write(bytes);

				} catch (IOException e) {
					model.addObject("page_error", e.getMessage());
				}
			}
		}
		model = prepareFormService.prepareForm(authUser, model);
		model.addObject("updateProfileForm", prepareFormService.getFormWithValues(authUser));
		return model;
	}

}
