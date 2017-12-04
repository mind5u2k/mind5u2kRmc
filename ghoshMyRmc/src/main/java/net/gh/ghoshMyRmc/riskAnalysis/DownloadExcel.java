package net.gh.ghoshMyRmc.riskAnalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("downloadExcel")
public class DownloadExcel {

	@Autowired
	ControlDao controlDao;

	@Autowired
	AssessmentDao assessmentDao;

	private static String REAL_PATH = "";

	public void updateXlsx(HttpServletRequest request,
			HttpServletResponse response, Long key) {
		File folder = getFileFromURL(request);
		File[] listOfFiles = folder.listFiles();
		response.setContentType("application/ms-excel");
		response.setContentLength((int) listOfFiles[0].length());
		response.setHeader("Expires:", "0"); // eliminates browser caching
		response.setHeader("Content-Disposition",
				"attachment; filename=testxls.xls");

		System.out.println("total files are [" + listOfFiles.length + "]");

		try {
			FileInputStream inputStream = new FileInputStream(listOfFiles[0]);
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			Object[][] bookData = {
					{ "The Passionate Programmer", "Chad Fowler", 16 },
					{ "Software Craftmanship", "Pete McBreen", 26 },
					{ "The Art of Agile Development", "James Shore", 32 },
					{ "Continuous Delivery", "Jez Humble", 41 }, };

			int rowCount = 7;

			for (Object[] aBook : bookData) {
				Row row = sheet.createRow(++rowCount);

				int columnCount = 0;

				Cell cell = row.createCell(columnCount);
				cell.setCellValue(rowCount);

				for (Object field : aBook) {
					cell = row.createCell(++columnCount);
					if (field instanceof String) {
						cell.setCellValue((String) field);
					} else if (field instanceof Integer) {
						cell.setCellValue((Integer) field);
					}
				}

			}
			inputStream.close();

			FileOutputStream fileOutputStream = new FileOutputStream(
					listOfFiles[0]);
			workbook.write(fileOutputStream);
			((FileOutputStream) workbook).close();

			fileOutputStream.close();

		} catch (IOException | EncryptedDocumentException
				| InvalidFormatException ex) {
			ex.printStackTrace();
		}
	}

	public void getAssessmentExcel(HttpServletRequest request,
			HttpServletResponse response, Assessment assessment) {
		try {

			REAL_PATH = request.getSession().getServletContext()
					.getRealPath("/assets/excelTemplates/responses/");
			if (!new File(REAL_PATH).exists()) {
				new File(REAL_PATH).mkdirs();
			}
			File folder = null;
			try {
				folder = new File(REAL_PATH);
			} catch (Exception e) {
				folder = new File(REAL_PATH);
			} finally {
			}

			// File folder = getFileFromURL(request);
			File[] listOfFiles = folder.listFiles();

			XSSFWorkbook workbook = new XSSFWorkbook(listOfFiles[0]);

			XSSFSheet sheet = workbook.getSheetAt(0);
			int rowCount = 1;

			List<Answer> answers = new ArrayList<Answer>();

			List<AssessmentCategories> assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessment(assessment);
			for (AssessmentCategories asc : assessmentCategories) {
				answers.addAll(controlDao.allAnswerbyAssessmentCategory(asc));
			}

			int rowNo = 0;
			for (Answer answer : answers) {

				Row row = sheet.createRow(++rowCount);
				int columnCount = 0;

				Cell cell = row.createCell(columnCount);
				cell.setCellValue(answer.getControl().getAssessmentCategories()
						.getAssessment().getAccount().getCountry().getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getAssessmentCategories()
						.getAssessment().getAccount().getDepartment().getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getAssessmentCategories()
						.getAssessment().getAccount().getLocation().getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getAssessmentCategories()
						.getAssessment().getAccount().getLob().getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getAssessmentCategories()
						.getAssignedCategories().getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(rowNo++);

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getControl().getControl());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getControl()
						.getShortText());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getControl().getRisk());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getControl()
						.getCritical());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getControl().getAnswers());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getAnswer());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getArtifaceName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getComment());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getMitigationDate());

				cell = row.createCell(++columnCount);
				cell.setCellValue(answer.getControl().getControl().getRating());
			}

			response.reset();

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition",
					"attachment; filename=\"Response(s).xlsx");

			workbook.write(response.getOutputStream());
			workbook.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void getAllAssessmentExcel(HttpServletRequest request,
			HttpServletResponse response, List<Assessment> assessments) {
		try {

			REAL_PATH = request.getSession().getServletContext()
					.getRealPath("/assets/excelTemplates/assessmentTemplate/");
			if (!new File(REAL_PATH).exists()) {
				new File(REAL_PATH).mkdirs();
			}
			File folder = null;
			try {
				folder = new File(REAL_PATH);
			} catch (Exception e) {
				folder = new File(REAL_PATH);
			} finally {
			}

			// File folder = getFileFromURL(request);
			File[] listOfFiles = folder.listFiles();

			XSSFWorkbook workbook = new XSSFWorkbook(listOfFiles[0]);

			XSSFSheet sheet = workbook.getSheetAt(0);
			int rowCount = 1;

			for (Assessment assessment : assessments) {

				Row row = sheet.createRow(++rowCount);
				int columnCount = 0;

				Cell cell = row.createCell(columnCount);
				System.out.println("cell is [" + cell + "]");
				cell.setCellValue(assessment.getAccount().getCountry()
						.getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getDepartment()
						.getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getLob().getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getLocation()
						.getName());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getSector());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getPhase());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getInitialRating());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAccount().getState());

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getApprover().getName() + " ["
						+ assessment.getApprover().getEmail() + "]");

				cell = row.createCell(++columnCount);
				cell.setCellValue(assessment.getAssessor().getName() + " ["
						+ assessment.getAssessor().getEmail() + "]");
			}

			response.reset();

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition",
					"attachment; filename=\"Assessment(s).xlsx");

			workbook.write(response.getOutputStream());
			workbook.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	@SuppressWarnings("finally")
	private File getFileFromURL(HttpServletRequest request) {

		REAL_PATH = request.getSession().getServletContext()
				.getRealPath("/assets/Responses/");
		if (!new File(REAL_PATH).exists()) {
			new File(REAL_PATH).mkdirs();
		}
		File file = null;
		try {
			file = new File(REAL_PATH);
		} catch (Exception e) {
			file = new File(REAL_PATH);
		} finally {
			return file;
		}
	}

	public static void main(String[] args) {

	}

}
