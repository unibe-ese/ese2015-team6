package ch.unibe.ese.Tutorfinder.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateProfileService;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @author Antonio
 *
 */
@Controller
public class UpdateProfileController {
	
	@Autowired	UpdateProfileService updateProfileService;
	@Autowired	ProfileDao profileDao;
	@Autowired	UserDao userDao;

	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");
		
		model.addObject("updateProfileForm", getFormWithBiography(user));
		model.addObject("User", userDao.findByEmail(user.getName()));
		model.addObject("Profile", getUsersProfile(user));
		
		return model;
	}
	

	@RequestMapping(value ="/update", method = RequestMethod.POST)
	public ModelAndView update(Principal user, @Valid UpdateProfileForm updateProfileForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model;
		if (!result.hasErrors()) {
            try {
            	updateProfileService.saveFrom(updateProfileForm, user);
            	model = new ModelAndView("updateProfile");
            } catch (InvalidUserException e) {
            	model = new ModelAndView("updateProfile");
            	model.addObject("page_error", e.getMessage());
            }
        } else {
        	model = new ModelAndView("updateProfile");
        }				
		model.addObject("updateProfileForm", getFormWithBiography(user));
		model.addObject("User", userDao.findByEmail(user.getName()));
		model.addObject("Profile", getUsersProfile(user));
		
		return model;
	}
	
	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(Principal user, @RequestParam("file") MultipartFile file) {
		ModelAndView model;
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				// Creating the directory to store file
				String rootPath = System.getProperty("user.dir");
				User tmpUser = userDao.findByEmail(user.getName());
				File dir = new File(rootPath + File.separator + "src" + File.separator + "main" 
									+ File.separator + "ProfilePictures");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + tmpUser.getId() + ".png");
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				//To get the absolute path use this
				//serverFile.getAbsolutePath()
				model = new ModelAndView("updateProfile");
				
			} catch (Exception e) {
				model = new ModelAndView("updateProfile");
			}
		} else {
			model = new ModelAndView("updateProfile");
		}
		model.addObject("updateProfileForm", getFormWithBiography(user));
		model.addObject("User", userDao.findByEmail(user.getName()));
		model.addObject("Profile", getUsersProfile(user));
		return model;
	}

	private UpdateProfileForm getFormWithBiography(Principal user) {
		UpdateProfileForm tmpForm = new UpdateProfileForm();
		tmpForm.setBiography(getUsersProfile(user).getBiography());
		
		return tmpForm;
	}

	private Profile getUsersProfile(Principal user) {
		User tmpUser = userDao.findByEmail(user.getName());
		Profile tmpProfile = profileDao.findOne(tmpUser.getId());
		
		return tmpProfile;
	}
}
