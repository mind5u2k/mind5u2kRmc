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

	public boolean sendNewGeneratedPassword(User user, String originalPassword) {

		String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 546px; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'>Hi "
				+ user.getName()
				+ ",<br><br><br><br>Your password has been reset!<br><br>Your Credentials are :<br><br>Email Id : "
				+ user.getEmail()
				+ "<br>password : "
				+ originalPassword
				+ "<br><br><br>If you any query or trouble please contact a site administrator.<br><br><br>Thank you <br>My Risk Management and compliance Team</div></div>";
		return SendMail.SendMail(user.getEmail(), "", "mail for new Password",
				mail);
	}

}
