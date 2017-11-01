package net.gh.ghoshMyRmc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gh.ghoshMyRmcBackend.dao.CategoryDao;
import net.gh.ghoshMyRmcBackend.dao.UserDao;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private GlobalController globalController;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping(value = { "/changePassword" })
	public ModelAndView changePassword() {

		ModelAndView mv = new ModelAndView("ChangePassword");
		mv.addObject("title", "Update Password");
		return mv;

	}

	@RequestMapping(value = { "/updatePassword" }, method = RequestMethod.POST)
	public String updatePassword(
			@RequestParam(name = "password", required = false) String password,
			@RequestParam(name = "cpassword", required = false) String cpassword,
			HttpServletRequest request, HttpServletResponse response) {

		System.out
				.println("hello there ! i am here in update password and password is ["
						+ password + "]");

		User user = userDao.getUserByEmailId(globalController.getUserModel()
				.getEmail());

		user.setPassword(passwordEncoder.encode(password));
		user.setEnabled(true);

		userDao.updateUser(user);

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?updatedPassword";
	}

}
