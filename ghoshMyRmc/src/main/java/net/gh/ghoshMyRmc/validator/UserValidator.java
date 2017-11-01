package net.gh.ghoshMyRmc.validator;

import net.gh.ghoshMyRmcBackend.dto.User;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

	private User user;

	public UserValidator(User user) {
		this.user = user;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		System.out.println("******** 1");
		User user = (User) target;
		System.out.println("******** 2");
		String email = user.getEmail().toLowerCase();
		System.out.println("******** 3 ");
		User existUser = this.user;
		System.out.println("******** 4");
		if (existUser != null) {
			System.out.println("******** 1");
			errors.rejectValue("email", null,
					"This email id is already registered");
			return;
		}
		System.out.println("******** 5");
	}

}
