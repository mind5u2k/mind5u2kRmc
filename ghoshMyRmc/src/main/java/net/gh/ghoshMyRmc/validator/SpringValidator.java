package net.gh.ghoshMyRmc.validator;

import net.gh.ghoshMyRmcBackend.dto.Category;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SpringValidator implements Validator {

	/**
	 * @author anurag ghosh
	 */

	@Override
	public boolean supports(Class<?> clazz) {
		return Category.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Category category = (Category) target;
		System.out.println(category);
		System.out.println(category.getMultipartFile());
		System.out.println(category.getMultipartFile().getOriginalFilename());
		System.out.println(category.getMultipartFile().getContentType());
		if (category.getMultipartFile() == null
				|| category.getMultipartFile().getOriginalFilename().equals("")) {
			errors.rejectValue("file", null,
					"Please select an image file to upload");
			return;
		}
		if (!(category.getMultipartFile().getContentType().equals("image/jpeg")
				|| category.getMultipartFile().getContentType()
						.equals("image/jpg")
				|| category.getMultipartFile().getContentType()
						.equals("image/png") || category.getMultipartFile()
				.getContentType().equals("image/gif"))) {
			errors.rejectValue("file", null,
					"Please use only image file for upload !!");
			return;
		}
	}

}
