package net.gh.ghoshMyRmc.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gh.ghoshMyRmc.mailService.MailService;
import net.gh.ghoshMyRmc.model.UserModel;
import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.CategoryDao;
import net.gh.ghoshMyRmcBackend.dao.UserDao;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

	/**
	 * @author anurag ghosh
	 */

	@Autowired
	private GlobalController globalController;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping(value = "/login")
	public ModelAndView login(
			@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "logout", required = false) String logout,
			@RequestParam(name = "updatedPassword", required = false) String updatedPassword,
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("login");

		System.out.println("%%%%%%%%%%%%5  ["
				+ SpringSecurityMessageSource.getAccessor() + "]");
		System.out.println("************ "
				+ getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		if (error != null) {
			mv.addObject("msg", "invalid credentials");
		}
		if (logout != null) {
			mv.addObject("logout", "!! User has Successfully Logged out !!");
		}
		if (updatedPassword != null) {
			mv.addObject("logout",
					"!! Password has been Updated Successfully !!");
		}
		mv.addObject("greetings", "welcome to my first spring mvc project" + "");
		return mv;
	}

	private String getErrorMessage(HttpServletRequest request, String key) {

		Enumeration keys = request.getSession().getAttributeNames();
		while (keys.hasMoreElements()) {
			String key1 = (String) keys.nextElement();
			System.out.println(key1 + ": "
					+ request.getSession().getAttribute(key1) + "<br>");
		}

		Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}

		return error;
	}

	@RequestMapping(value = "/forgotPassword")
	public ModelAndView forgotPassword() {
		ModelAndView mv = new ModelAndView("forgotPassword");
		return mv;
	}

	@RequestMapping(value = "/sendPassword", method = RequestMethod.GET)
	public ModelAndView sendPassword(
			@RequestParam(name = "mailId", required = false) String mailId) {

		ModelAndView mv = new ModelAndView("forgotPassword");
		User user = userDao.getUserByEmailId(mailId);
		System.out.println("user is [" + user + "]");

		if (user != null) {
			String originalPassword = Util.getSaltString();

			user.setPassword(passwordEncoder.encode(originalPassword));
			user.setEnabled(false);
			userDao.updateUser(user);

			mailService.sendNewGeneratedPassword(user, originalPassword);
			mv.addObject("msg",
					"New Password has been sent to your Registered Mail Id");
		} else {
			mv.addObject("errorMsg", "!! Mail Id is not Registered !!");
		}

		return mv;
	}

	@RequestMapping(value = "/perform-logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping(value = { "/", "/home", "/index" })
	public String index() {

		User user = userDao.getUserByEmailId(globalController.getUserModel()
				.getEmail());

		if (!user.isEnabled()) {
			return "redirect:/changePassword";
		}

		return "redirect:/pageRedirection";
	}

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

	@RequestMapping(value = { "/pageRedirection" })
	public String pageRedirection() {

		UserModel userModel = globalController.getUserModel();
		if (userModel == null) {
			return "redirect:/login";
		}
		if (userModel.getRole().equals(Util.ADMIN)
				|| userModel.getRole().equals(Util.SUPERADMIN)) {
			return "redirect:/admin/adminhome";
		} else if (userModel.getRole().equals(Util.APPROVER)) {
			return "redirect:/approver/approverHome";
		} else if (userModel.getRole().equals(Util.ASSESSOR)) {
			return "redirect:/assessor/assessorHome";
		} else if (userModel.getRole().equals(Util.REVIEWER)) {
			return "redirect:/reviewer/reviewerHome";
		} else if (userModel.getRole().equals(Util.SME)) {
			return "redirect:/sme/smeHome";
		} else {
			return "redirect:/perform-logout";
		}

	}

	@RequestMapping(value = "/access-denied")
	public ModelAndView accessDeniedPage() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errorTitle", "You are not Authorized to access this page");
		mv.addObject("errorDescription",
				"you have entered in non secured area !! Please get out of here");
		mv.addObject("title", "403 - Access Denied");
		return mv;
	}

	@RequestMapping(value = "riskMigration")
	public String riskMigration() {

		return "riskMigrationPage";

	}

	@RequestMapping("/downloadRiskCalculationPdf")
	public String download(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String REAL_PATH = request.getSession().getServletContext()
					.getRealPath("/assets/Responses/");
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ "Risk Factor Calculation" + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType("application/pdf");
			FileInputStream fileInputStream = new FileInputStream(REAL_PATH
					+ "RiskFactor.pdf");

			int i;
			while ((i = fileInputStream.read()) != -1) {
				out.write(i);
			}
			fileInputStream.close();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
