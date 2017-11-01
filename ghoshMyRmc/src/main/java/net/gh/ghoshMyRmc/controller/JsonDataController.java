package net.gh.ghoshMyRmc.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.Control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/data")
public class JsonDataController {

	/**
	 * @author anurag ghosh
	 */

	@Autowired
	private AssessmentDao assessmentDao;

	@Autowired
	private ControlDao controlDao;

	@RequestMapping("/all/controls")
	@ResponseBody
	public List<Control> allControls() {
		System.out.println("hello there [" + controlDao.controlLists().size()
				+ "]");
		return controlDao.controlLists();
	}

	@RequestMapping("/categories/{catId}")
	@ResponseBody
	public List<AssessmentCategories> allCategories(
			@PathVariable("assessmentId") Long assessmentId,
			HttpServletResponse response) {
		System.out.println("hello there [" + controlDao.controlLists().size()
				+ "]");

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessmentDao
						.getAssessmentById(assessmentId));
		return assessmentCategories;
	}

	@RequestMapping("/control/{controlId}")
	@ResponseBody
	public Control controlById(@PathVariable("controlId") Long controlId,
			HttpServletResponse response) {

		Control control = controlDao.getControl(controlId);
		return control;
	}
}
