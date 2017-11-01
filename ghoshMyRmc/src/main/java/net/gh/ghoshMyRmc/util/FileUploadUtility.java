package net.gh.ghoshMyRmc.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtility {

	/**
	 * @author anurag ghosh
	 */

	private static final String ABC_PATH = "C:\\Projects\\Spring\\ghosh-projects\\ghoshOnlineShopping\\src\\main\\webapp\\assets\\images\\";
	private static String REAL_PATH = "";

	public static void uploadFile(HttpServletRequest request,
			MultipartFile multipartFile, long id) {
		REAL_PATH = request.getSession().getServletContext()
				.getRealPath("/assets/images/");
		if (!new File(ABC_PATH).exists()) {
			new File(ABC_PATH).mkdirs();
		}
		if (!new File(REAL_PATH).exists()) {
			new File(REAL_PATH).mkdirs();
		}
		try {
			multipartFile.transferTo(new File(REAL_PATH + id + ".jpg"));
			multipartFile.transferTo(new File(ABC_PATH + id + ".jpg"));
			System.out.println("transfer successful");
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
}
