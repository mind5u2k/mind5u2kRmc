package net.gh.ghoshMyRmc.mailService;

import net.gh.ghoshMyRmcBackend.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public boolean sendMailToNewUser(User user, String originalPassword) {

		String mail = "hello there <br> name=" + user.getName()
				+ "<br> password = " + originalPassword;
		return SendMail.SendMail(user.getEmail(), "", "mail for new Password",
				mail);
	}

}
