package net.gh.ghoshMyRmc.pdfGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
//import sandbox.WrapToTest;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gh.ghoshMyRmc.riskAnalysis.RiskCalculation;
import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelPosition;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelRenderer;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.BarChartProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.properties.util.ChartStroke;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@Service("pdfSections")
public class PdfSections {

	@Autowired
	ControlDao controlDao;

	@Autowired
	AssessmentDao assessmentDao;

	public static final String DEST = "C:/Users/IBM_ADMIN/Desktop/chapter_title.pdf";
	public static final String IMAGE = "/medinTact/src/sa/resources/Activated1.png";

	private BarChartProperties barChartProperties;

	// ---all of my charts serviced by this Servlet will have the same
	// properties.
	protected LegendProperties legendProperties;
	protected AxisProperties axisProperties;
	protected ChartProperties chartProperties;

	protected int width = 450;
	protected int height = 300;

	private int maxVal = 0;
	private boolean riskAvailibility = false;

	private void setProperties() {
		this.legendProperties = new LegendProperties();
		this.chartProperties = new ChartProperties();
		this.axisProperties = new AxisProperties(false);

		legendProperties.setNumColumns(0);

		legendProperties.setBorderStroke(ChartStroke.DEFAULT_CHART_OUTLINE);

		ChartFont axisScaleFont = new ChartFont(new java.awt.Font(
				java.awt.Font.DIALOG, java.awt.Font.LAYOUT_RIGHT_TO_LEFT, 10),
				Color.black);
		axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
		axisProperties.getYAxisProperties().setScaleChartFont(axisScaleFont);

		ChartFont axisTitleFont = new ChartFont(new java.awt.Font(
				java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 10), Color.black);
		axisProperties.getXAxisProperties().setTitleChartFont(axisTitleFont);
		axisProperties.getYAxisProperties().setTitleChartFont(axisTitleFont);

		ChartFont axisTitleFont1 = new ChartFont(new java.awt.Font(
				java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 12), Color.black);
		axisProperties.getXAxisProperties().setAxisTitleChartFont(
				axisTitleFont1);
		axisProperties.getYAxisProperties().setAxisTitleChartFont(
				axisTitleFont1);
		axisProperties.getXAxisProperties().setShowEndBorder(false);
		axisProperties.getYAxisProperties().setShowEndBorder(false);

		DataAxisProperties dataAxisProperties = (DataAxisProperties) axisProperties
				.getYAxisProperties();

		try {
			dataAxisProperties.setUserDefinedScale(0, 200);
		} catch (PropertyException propertyException) {
			propertyException.printStackTrace();
		}

		dataAxisProperties.setRoundToNearest(2);

		ChartFont titleFont = new ChartFont(new java.awt.Font(
				java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 10), Color.black);
		this.chartProperties.setTitleFont(titleFont);

		this.barChartProperties = new BarChartProperties();
		barChartProperties.setWidthPercentage(0.4f);
		barChartProperties.setShowOutlinesFlag(false);

		ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer(false,
				false, true, -1);
		ChartFont labelFont = new ChartFont(new java.awt.Font(
				java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 9), Color.black);
		valueLabelRenderer.setValueLabelPosition(ValueLabelPosition.ON_TOP);
		valueLabelRenderer.setValueChartFont(labelFont);
		valueLabelRenderer.useVerticalLabels(false);
		barChartProperties.addPostRenderEventListener(valueLabelRenderer);
	}

	class MyFooter extends PdfPageEventHelper {
		Font ffont = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC);

		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();
			Phrase header = new Phrase("this is a header", ffont);
			Phrase footer = new Phrase("this is a footer", ffont);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, header, (10),
					document.top() + 10, 0);
			ColumnText.showTextAligned(
					cb,
					Element.ALIGN_RIGHT,
					footer,
					(document.right() - document.left()) / 2
							+ document.leftMargin(), document.bottom() - 10, 0);
		}
	}

	public static void main(String[] args) throws IOException,
			DocumentException, ChartDataException, PropertyException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new PdfSections().createPdf(DEST);
	}

	public void createPdf(String dest) throws IOException, DocumentException,
			ChartDataException, PropertyException {
		Document document = new Document(PageSize.A4, 0, 0, 20, 20);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(dest));
		MyFooter event = new MyFooter();
		writer.setPageEvent(event);
		document.open();
		Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.UNDERLINE,
				BaseColor.BLACK);
		Paragraph title = new Paragraph("ACCOUNT DETAILS\n\n", font1);
		title.setAlignment(Element.ALIGN_CENTER);
		// document.add(title);

		PdfPTable table;
		PdfPCell cell;
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);

		table = new PdfPTable(3);
		float[] columnWidths = new float[] { 1f, 0.1f, 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		// cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingTop(20);
		cell.setColspan(3);
		cell.setFixedHeight(25f);
		table.addCell(cell);

		PdfPTable table1 = new PdfPTable(2);
		columnWidths = new float[] { 1f, 1f };

		cell = new PdfPCell(new Phrase("Account Details", font));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Client/Department", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingTop(6f);
		cell.setPaddingLeft(5f);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("adsf", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Location", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("sadf", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("LOB", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("asdf", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Business Stage", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("asdfasdf", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);

		cell = new PdfPCell(table1);
		cell.setPaddingBottom(7f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingBottom(7f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		table1 = new PdfPTable(2);
		columnWidths = new float[] { 0.8f, 0.2f };
		table1.setWidths(columnWidths);

		cell = new PdfPCell(new Phrase("Risk value and rating", font));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(
				"Total - Risk Factor (Critical Controls)", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingTop(6f);
		cell.setPaddingLeft(5f);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("0", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Total - Risk Factor (All Controls)",
				font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("24", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Rating (Current)", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("B", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Rating (Initial)", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("A", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);

		cell = new PdfPCell(table1);
		cell.setPaddingBottom(7f);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(8);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);
		cell = new PdfPCell(new Phrase("Category wise risk", font1));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table.addCell(cell);
		document.add(table);

		/*-font1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.UNDERLINE,
				BaseColor.BLACK);
		title = new Paragraph("Category wise risk", font1);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);*/

		// User pe = report.getPe();
		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);

		/*-List<Domain> domains = new ArrayList<Domain>();
		domains = auditService.getDistinctPEDPEDomains(report.getPe(), report,
				role);
		
		int i = 0;
		for (Domain domain : domains) {
			xAxisLabels[i] = domain.getName();
			int risk = adminService.getAllRiskFactorByCountry(report
					.getLocprocmap().getCountry().getId(), report
					.getLocprocmap().getComptency().getId(), report
					.getLocprocmap().getProcess().getId(), report
					.getLocprocmap().getLocation().getId(), domain.getId(),
					pe.getEmpId(), pe.getAuditee().getId(), role);
			data[0][i] = risk;
			System.out.println("data stored as a risk is [" + data[0][i] + "]");
			i++;
		}
		 */
		/*-String[] xAxisLabels = {"a","b"};
		double[][] data = new double[1][domains.size()];
		setProperties();

		String xAxisTitle = "Category";
		String yAxisTitle = "Risk";
		String title1 = " ";*/
		/*-IAxisDataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle,
				yAxisTitle, title1);

		String[] legendLabels = { "Risk Values" };
		Color color = new Color(181, 108, 108);
		Paint[] paints = new Paint[] { color };
		dataSeries.addIAxisPlotDataSet(new AxisChartDataSet(data, legendLabels,
				paints, ChartType.BAR, this.barChartProperties));

		AxisChart axisChart = new AxisChart(dataSeries, this.chartProperties,
				this.axisProperties, null, this.width, this.height);

		BufferedImage bufferedImage = new BufferedImage(450, 300, 2);

		axisChart.setGraphics2D(bufferedImage.createGraphics());
		axisChart.render();

		Graphics2D g = axisChart.getGraphics2D();
		g.dispose();

		BufferedImage resizedImage = new BufferedImage(700, 300, 2);
		Graphics2D g1 = resizedImage.createGraphics();
		g1.drawImage(bufferedImage, 0, 0, 476, 150, null);
		g1.dispose();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) bufferedImage, "png", baos);
		byte[] imageInByte = baos.toByteArray();
		Image image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image);
		cell.setPadding(12);
		// cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);*/
		// document.add(table);

		JFreeChart barChart = ChartFactory.createBarChart("", "Category",
				"Risk", createDataset(), PlotOrientation.VERTICAL, false, true,
				true);
		CategoryPlot plot = barChart.getCategoryPlot();

		ValueAxis axis2 = plot.getRangeAxis();
		axis2.setTickLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 15));
		axis2.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.BOLD, 15));
		axis2.setRange(0, 10);

		CategoryAxis axis = plot.getDomainAxis();
		axis.setMaximumCategoryLabelLines(6);
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		axis.setTickLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 15));
		axis.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.BOLD, 15));

		plot.setOutlineVisible(false);
		plot.setBackgroundPaint(Color.white);
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		Color color1 = new Color(181, 108, 108);
		barRenderer.setBaseItemLabelsVisible(true);
		DecimalFormat decimalformat1 = new DecimalFormat("##,###.0");
		barRenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalformat1));
		barRenderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		barRenderer.setSeriesPaint(0, color1);
		barRenderer.setBarPainter(new StandardBarPainter());
		barRenderer.setMaximumBarWidth(0.05);
		barChart.setBackgroundPaint(Color.white);
		BufferedImage outputImage = barChart.createBufferedImage(650, 200);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) outputImage, "png", baos);
		byte[] imageInByte = baos.toByteArray();
		Image image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image, true);
		cell.setPadding(12);
		// cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(8);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);
		cell = new PdfPCell(new Phrase("Internal/External Risk", font1));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);

		JFreeChart pieChart = ChartFactory.createPieChart("",
				createPieDataset(), true, false, true);
		pieChart.setBackgroundPaint(Color.white);
		PiePlot piePlot = (PiePlot) pieChart.getPlot();
		piePlot.setSectionPaint("Internal", new Color(119, 60, 60));
		piePlot.setSectionPaint("External", new Color(181, 108, 108));
		piePlot.setBackgroundPaint(Color.white);
		piePlot.setOutlineVisible(false);
		piePlot.setShadowPaint(Color.black);
		piePlot.setShadowXOffset(0);
		piePlot.setShadowYOffset(0);
		piePlot.setLabelOutlinePaint(null);
		piePlot.setLabelShadowPaint(null);
		piePlot.setLabelBackgroundPaint(null);
		LegendTitle legendTitle = pieChart.getLegend();
		legendTitle.setPosition(RectangleEdge.RIGHT);
		outputImage = pieChart.createBufferedImage(450, 150);
		baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) outputImage, "png", baos);
		imageInByte = baos.toByteArray();
		image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image);
		cell.setPadding(12);
		// cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);
		document.add(table);

		document.close();
	}

	/*-private Image getScaledImage(Image image, float x, float y) {
		AffineTransform transform = new AffineTransform();
		transform.scale(x, y);
		transform.translate((x - 1) * image.getWidth() / 2,
				(y - 1) * image.getHeight() / 2);
	}*/

	private CategoryDataset createDataset(Assessment assessment) {
		maxVal = 0;
		User approver = assessment.getApprover();
		List<AssessmentCategories> categories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		if (categories == null) {
			categories = new ArrayList<AssessmentCategories>();
		}
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int i = 0;
		RiskCalculation riskCalculation = new RiskCalculation();
		for (AssessmentCategories assessmentCategory : categories) {
			int risk = riskCalculation
					.getRiskValueforAssessmentCategory(assessmentCategory);
			dataset.addValue(risk, "a", assessmentCategory
					.getAssignedCategories().getName());
			if (maxVal < risk) {
				maxVal = risk;
			}
		}

		return dataset;
	}

	private CategoryDataset createDataset4Domain(List<Answer> answers) {
		maxVal = 0;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Answer answer : answers) {
			dataset.addValue(answer.getControl().getControl().getRating(), "a",
					answer.getControl().getControl().getShortText());
			if (maxVal < answer.getControl().getControl().getRating()) {
				maxVal = answer.getControl().getControl().getRating();
			}
		}
		return dataset;
	}

	private CategoryDataset createDataset() {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(5.0, "a", "Awareness iwjsjdflsajsadkjfndsa");
		dataset.addValue(5.0, "a", "IT Controls");
		dataset.addValue(1.0, "a", "Data Privacy");
		dataset.addValue(3.0, "a", "Ragulatory");

		return dataset;
	}

	private PieDataset createPieDataset(Assessment assessment) {
		List<Answer> answers = new ArrayList<Answer>();

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		for (AssessmentCategories asc : assessmentCategories) {
			answers.addAll(controlDao.allAnswerbyAssessmentCategory(asc));
		}
		List<Answer> riskyAnswers = new ArrayList<Answer>();
		for (Answer ans : answers) {
			if (ans.getAnswer().equals("No")
					&& ans.getControl().getControl().getFlag() == 1) {
				riskyAnswers.add(ans);
			}
			if (ans.getAnswer().equals("Yes")
					&& ans.getControl().getControl().getFlag() == 2) {
				riskyAnswers.add(ans);
			}
		}
		int internal = 0;
		int external = 0;
		int both = 0;
		for (Answer ans : riskyAnswers) {
			if (ans.getControl().getControl().getRisk()
					.equals(Util.INTERNAL_RISK)) {
				internal += ans.getControl().getControl().getRating();
			}
			if (ans.getControl().getControl().getRisk()
					.equals(Util.EXTERNAL_RISK)) {
				external += ans.getControl().getControl().getRating();
			}
			if (ans.getControl().getControl().getRisk().equals("Both")) {
				both += ans.getControl().getControl().getRating();
			}
		}

		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Internal", new Double(internal));
		dataset.setValue("External", new Double(external));
		dataset.setValue("Both", new Double(both));
		return dataset;
	}

	private PieDataset createPieDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Internal", new Double(20));
		dataset.setValue("External", new Double(40));
		dataset.setValue("Both", new Double(40));
		return dataset;
	}

	public void generatePdf(HttpServletRequest requests,
			HttpServletResponse response, long assessmentId)
			throws IOException, DocumentException, ChartDataException,
			PropertyException {

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);

		response.setContentType("application/pdf");

		Document document = new Document(PageSize.A4, 0, 0, 40, 10);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document,
				byteArrayOutputStream);
		document.open();
		Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.UNDERLINE,
				BaseColor.BLACK);
		Paragraph title = new Paragraph("ACCOUNT DETAILS\n\n", font1);
		title.setAlignment(Element.ALIGN_CENTER);

		PdfPTable table;
		PdfPCell cell;
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);

		table = new PdfPTable(3);
		float[] columnWidths = new float[] { 1f, 0.1f, 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingTop(20);
		cell.setColspan(3);
		cell.setFixedHeight(25f);
		table.addCell(cell);

		PdfPTable table1 = new PdfPTable(2);
		columnWidths = new float[] { 1f, 1f };

		cell = new PdfPCell(new Phrase("Account Details", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Client/Department", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingTop(6f);
		cell.setPaddingLeft(5f);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getDepartment()
				.getName(), font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Location", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getLocation()
				.getName(), font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("LOB", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getLob()
				.getName(), font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Business Stage", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(
				new Phrase(assessment.getAccount().getPhase(), font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);

		cell = new PdfPCell(table1);
		cell.setPaddingBottom(7f);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingBottom(7f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		table1 = new PdfPTable(2);
		columnWidths = new float[] { 0.8f, 0.2f };
		table1.setWidths(columnWidths);

		cell = new PdfPCell(new Phrase("Risk value and rating",
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL,
						BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(
				"Total - Risk Factor (Critical Controls)", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingTop(6f);
		cell.setPaddingLeft(5f);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("0", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingRight(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Total - Risk Factor (All Controls)",
				font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("24", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingRight(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Rating (Current)", font));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("B", font));
		cell.setPaddingRight(5f);
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Rating (Initial)", font));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("A", font));
		cell.setPaddingRight(5f);
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);

		cell = new PdfPCell(table1);
		cell.setPaddingBottom(7f);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(8);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);
		cell = new PdfPCell(new Phrase("Category wise risk",
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL,
						BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table.addCell(cell);
		JFreeChart barChart = ChartFactory.createBarChart("", "Category",
				"Risk", createDataset(assessment), PlotOrientation.VERTICAL,
				false, true, true);
		CategoryPlot plot = barChart.getCategoryPlot();
		ValueAxis axis2 = plot.getRangeAxis();
		axis2.setTickLabelPaint(new Color(146, 15, 15));
		axis2.setLabelPaint(new Color(146, 15, 15));

		axis2.setTickLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 16));
		axis2.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 20));
		axis2.setRange(0, maxVal + (maxVal * 0.2));

		CategoryAxis axis = plot.getDomainAxis();
		axis.setTickLabelPaint(new Color(146, 15, 15));
		axis.setLabelPaint(new Color(146, 15, 15));
		// axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		axis.setMaximumCategoryLabelLines(6);
		axis.setTickLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 16));
		axis.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 20));

		plot.setOutlineVisible(false);
		plot.setBackgroundPaint(Color.white);
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		Color color1 = new Color(181, 108, 108);
		barRenderer.setBaseItemLabelsVisible(true);
		DecimalFormat decimalformat1 = new DecimalFormat("0.0");
		barRenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalformat1));
		barRenderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		barRenderer.setBaseItemLabelPaint(new Color(146, 15, 15));
		barRenderer.setBaseItemLabelFont(new java.awt.Font(
				java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 17));
		barRenderer.setSeriesPaint(0, color1);
		barRenderer.setBarPainter(new StandardBarPainter());
		barRenderer.setMaximumBarWidth(0.05);
		barChart.setBackgroundPaint(Color.white);
		BufferedImage outputImage = barChart.createBufferedImage(750, 400);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) outputImage, "png", baos);
		byte[] imageInByte = baos.toByteArray();
		Image image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image, true);
		cell.setPadding(12);
		// cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(8);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);
		cell = new PdfPCell(new Phrase("Internal/External Risk",
				FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL,
						BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setFixedHeight(25f);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);

		JFreeChart pieChart = ChartFactory.createPieChart("",
				createPieDataset(assessment), false, false, true);
		pieChart.setBackgroundPaint(Color.white);
		PiePlot piePlot = (PiePlot) pieChart.getPlot();
		piePlot.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 17));
		piePlot.setSectionPaint("Internal", new Color(119, 60, 60));
		piePlot.setSectionPaint("External", new Color(181, 108, 108));
		piePlot.setBackgroundPaint(Color.white);
		piePlot.setOutlineVisible(false);
		piePlot.setShadowPaint(Color.black);
		piePlot.setShadowXOffset(0);
		piePlot.setShadowYOffset(0);
		piePlot.setLabelOutlinePaint(null);
		piePlot.setLabelShadowPaint(null);
		piePlot.setLabelBackgroundPaint(null);
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
				"{0}: ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
		piePlot.setLabelGenerator(gen);
		piePlot.setLabelPaint(new Color(146, 15, 15));
		/*-LegendTitle legendTitle = pieChart.getLegend();
		legendTitle.setPosition(RectangleEdge.RIGHT);
		legendTitle.setItemFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 20));*/
		outputImage = pieChart.createBufferedImage(750, 350);
		baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) outputImage, "png", baos);
		imageInByte = baos.toByteArray();
		image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image, true);
		cell.setPadding(12);
		// cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(30);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		List<AssessmentCategories> assessmentCategories = new ArrayList<AssessmentCategories>();
		assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		List<Answer> domainAnswer = new ArrayList<Answer>();
		if (assessmentCategories == null) {
			assessmentCategories = new ArrayList<AssessmentCategories>();
		}
		for (AssessmentCategories assessmentCategory : assessmentCategories) {
			domainAnswer = controlDao
					.allAnswerbyAssessmentCategory(assessmentCategory);
			List<Answer> answer = new ArrayList<Answer>();
			for (Answer ans : domainAnswer) {
				if (ans.getAnswer().equals("No")
						&& ans.getControl().getControl().getFlag() == 1) {
					answer.add(ans);
				}
				if (ans.getAnswer().equals("Yes")
						&& ans.getControl().getControl().getFlag() == 2) {
					answer.add(ans);
				}
			}

			table = new PdfPTable(1);
			columnWidths = new float[] { 1f };
			table.setWidths(columnWidths);
			font1 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLACK);
			cell = new PdfPCell(new Phrase(assessmentCategory
					.getAssignedCategories().getName(), font1));
			cell.setBackgroundColor(new BaseColor(237, 225, 225));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			cell.setFixedHeight(25f);
			table.addCell(cell);
			barChart = ChartFactory.createBarChart("", "Short Text", "Risk",
					createDataset4Domain(answer), PlotOrientation.VERTICAL,
					false, true, true);
			plot = barChart.getCategoryPlot();
			axis2 = plot.getRangeAxis();
			axis2.setTickLabelPaint(new Color(146, 15, 15));
			axis2.setLabelPaint(new Color(146, 15, 15));

			axis2.setTickLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
					java.awt.Font.PLAIN, 16));
			axis2.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
					java.awt.Font.PLAIN, 20));
			axis2.setRange(0, maxVal + (maxVal * 0.2));

			axis = plot.getDomainAxis();
			axis.setTickLabelPaint(new Color(146, 15, 15));
			axis.setLabelPaint(new Color(146, 15, 15));
			// axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
			axis.setMaximumCategoryLabelLines(6);
			axis.setTickLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
					java.awt.Font.PLAIN, 16));
			axis.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
					java.awt.Font.PLAIN, 20));

			plot.setOutlineVisible(false);
			plot.setBackgroundPaint(Color.white);
			barRenderer = (BarRenderer) plot.getRenderer();
			color1 = new Color(181, 108, 108);
			barRenderer.setBaseItemLabelsVisible(true);
			decimalformat1 = new DecimalFormat("0.0");
			barRenderer
					.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
							"{2}", decimalformat1));
			barRenderer
					.setPositiveItemLabelPositionFallback(new ItemLabelPosition(
							ItemLabelAnchor.CENTER, TextAnchor.CENTER));
			barRenderer.setBaseItemLabelPaint(new Color(146, 15, 15));
			barRenderer.setBaseItemLabelFont(new java.awt.Font(
					java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 17));
			barRenderer.setSeriesPaint(0, color1);
			barRenderer.setBarPainter(new StandardBarPainter());
			barRenderer.setMaximumBarWidth(0.05);
			barChart.setBackgroundPaint(Color.white);
			outputImage = barChart.createBufferedImage(750, 320);
			baos = new ByteArrayOutputStream();
			ImageIO.write((RenderedImage) outputImage, "png", baos);
			imageInByte = baos.toByteArray();
			image = Image.getInstance(imageInByte);

			// cell.setBorder(Rectangle.NO_BORDER);

			if (answer.size() == 0) {
				font1 = FontFactory.getFont(FontFactory.HELVETICA, 15,
						Font.NORMAL, BaseColor.BLACK);
				cell = new PdfPCell(new Phrase("No Risk Available for "
						+ assessmentCategory.getAssignedCategories().getName(),
						font1));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setFixedHeight(215);
				table.addCell(cell);
			} else {
				cell = new PdfPCell(image, true);
				cell.setPadding(12);
				table.addCell(cell);
			}

			document.add(table);

			table = new PdfPTable(1);
			columnWidths = new float[] { 1f };
			table.setWidths(columnWidths);
			cell = new PdfPCell(new Phrase(" ", font));
			cell.setPaddingTop(8);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);
		}

		/*-table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(8);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);*/

		table1 = new PdfPTable(7);
		columnWidths = new float[] { 0.5f, 3f, 1.7f, 0.8f, 1f, 2f, 1f };
		table1.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase("No", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Control", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Opt", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Ans", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Risk", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Comment", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Mitigation", FontFactory.getFont(
				FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
		cell.setBackgroundColor(new BaseColor(237, 225, 225));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(25f);
		table1.addCell(cell);

		int i = 1;
		for (AssessmentCategories assessmentCategory : assessmentCategories) {

			cell = new PdfPCell(new Phrase(" ", font));
			cell.setPaddingTop(8);
			cell.setColspan(6);
			cell.setBorder(Rectangle.NO_BORDER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(assessmentCategory
					.getAssignedCategories().getName(), FontFactory.getFont(
					FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
			cell.setBackgroundColor(new BaseColor(235, 241, 243));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPaddingLeft(12f);
			cell.setColspan(6);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setFixedHeight(20f);
			table1.addCell(cell);
			domainAnswer = controlDao
					.allAnswerbyAssessmentCategory(assessmentCategory);
			if (domainAnswer == null) {
				domainAnswer = new ArrayList<Answer>();
			}
			for (Answer ans : domainAnswer) {
				cell = new PdfPCell(new Phrase(String.valueOf(i), font));

				cell.setBorder(Rectangle.BOTTOM);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setPaddingBottom(7f);
				/*-cell.setPaddingTop(6f);
				cell.setPaddingLeft(5f);
				 */
				table1.addCell(cell);
				cell = new PdfPCell(new Phrase(ans.getControl().getControl()
						.getControl(), font));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setPaddingBottom(7f);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell);
				cell = new PdfPCell(new Phrase(ans.getControl().getControl()
						.getAnswers(), font));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorder(Rectangle.BOTTOM);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				/*-cell.setPaddingTop(6f);
				cell.setPaddingLeft(5f);
				cell.setPaddingBottom(7f);*/cell.setPaddingBottom(7f);
				table1.addCell(cell);
				cell = new PdfPCell(new Phrase(ans.getAnswer(), font));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorder(Rectangle.BOTTOM);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell);
				cell = new PdfPCell(new Phrase(String.valueOf(ans.getControl()
						.getControl().getRating()), font));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorder(Rectangle.BOTTOM);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table1.addCell(cell);
				cell = new PdfPCell(new Phrase(ans.getComment(), font));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorder(Rectangle.BOTTOM);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell);
				cell = new PdfPCell(new Phrase(ans.getMitigationDate()
						.toString(), font));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(245, 243, 243));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorder(Rectangle.BOTTOM);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table1.addCell(cell);
				i++;
			}
		}

		document.add(table1);

		document.close();

		System.out.println("size of document is [" + document + "]");
		System.out.println("size of byte input stream is ["
				+ byteArrayOutputStream.size() + "]");

		byte[] blob = byteArrayOutputStream.toByteArray();
		response.setContentLength(blob.length);
		System.out.println("size of blob object is [" + blob.length + "]");
		ServletOutputStream out = response.getOutputStream();
		out.write(blob);
		out.close();
	}
}