package net.gh.ghoshMyRmc.pdfGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import com.itextpdf.text.Chunk;
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

	@Autowired
	RiskCalculation riskCalculation;

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
		System.out.println("categories are [" + categories + "]");
		System.out.println("categories size is [" + categories.size() + "]");
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

		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document,
				byteArrayOutputStream);
		document.open();
		Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 20);
		Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
		Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 10);

		PdfPTable table;
		PdfPCell cell;

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		float[] columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk("Account Details", new Font(
				Font.FontFamily.TIMES_ROMAN, 20))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setBorderWidthBottom(.5f);
		cell.setBorderColor(new BaseColor(146, 144, 144));
		cell.setPadding(0);
		cell.setPaddingBottom(8);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk(" ", new Font(
				Font.FontFamily.TIMES_ROMAN, 8))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setFixedHeight(2f);
		cell.setPadding(0);
		table.addCell(cell);
		document.add(table);

		PdfPTable pdfPTable = new PdfPTable(2);
		pdfPTable.setWidthPercentage(100);
		columnWidths = new float[] { 1f, 1f };
		pdfPTable.setWidths(columnWidths);

		// --------------------

		PdfPTable table1 = new PdfPTable(2);
		table1.setWidthPercentage(100);
		columnWidths = new float[] { 1f, 1f };

		cell = new PdfPCell(new Phrase("Account Details", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setPaddingBottom(8f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Client/Department", font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(5f);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getDepartment()
				.getName(), font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Location", font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getLocation()
				.getName(), font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("LOB", font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getLob()
				.getName(), font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Business Stage", font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount().getPhase(),
				font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPaddingBottom(7f);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);

		cell = new PdfPCell(table1);
		cell.setBorderWidth(0f);
		cell.setPadding(2f);
		pdfPTable.addCell(cell);

		// ----------------
		table1 = new PdfPTable(2);
		table1.setWidthPercentage(100);
		columnWidths = new float[] { 0.8f, 0.2f };
		table1.setWidths(columnWidths);

		cell = new PdfPCell(new Phrase("Risk value and rating",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL,
						BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setPaddingBottom(8f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(
				"Total - Risk Factor (Critical Controls)", font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(5f);
		cell.setPaddingBottom(7f);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(String.valueOf(riskCalculation
				.getCriticalRiskvalueforAssessment(assessment)), font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setPaddingRight(5f);
		cell.setPaddingBottom(7f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Total - Risk Factor (All Controls)",
				font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(
				String.valueOf(assessment.getRiskValue()), font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setPaddingRight(5f);
		cell.setPaddingBottom(7f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Rating (Current)", font2));
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getRiskLevel(), font2));
		cell.setPaddingRight(5f);
		cell.setPaddingBottom(7f);
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase("Rating (Initial)", font2));
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setPaddingBottom(7f);
		cell.setPaddingLeft(5f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.addCell(cell);
		cell = new PdfPCell(new Phrase(assessment.getAccount()
				.getInitialRating(), font2));
		cell.setPaddingRight(5f);
		cell.setPaddingBottom(7f);
		cell.setBackgroundColor(new BaseColor(245, 243, 243));
		cell.setBorderWidth(0f);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);

		cell = new PdfPCell(table1);
		cell.setBorderWidth(0f);
		cell.setPadding(2f);
		pdfPTable.addCell(cell);
		document.add(pdfPTable);

		// ------------------------

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk(" ", new Font(
				Font.FontFamily.TIMES_ROMAN, 8))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setBorderWidthBottom(.5f);
		cell.setBorderColor(new BaseColor(146, 144, 144));
		cell.setFixedHeight(4f);
		cell.setPadding(0);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk(" ", new Font(
				Font.FontFamily.TIMES_ROMAN, 8))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setFixedHeight(5f);
		cell.setPadding(0);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);
		cell = new PdfPCell(new Phrase("Category wise risk",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL,
						BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(8f);
		cell.setBorderWidth(0.5f);
		cell.setBorderColor(new BaseColor(146, 144, 144));
		table.addCell(cell);
		System.out.println("assessment is [" + assessment + "]");
		System.out.println("assessment id is [" + assessment.getId() + "]");
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

		plot.setOutlineVisible(true);
		plot.setBackgroundPaint(new Color(237, 247, 255));
		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		Color color1 = new Color(131, 188, 249);
		barRenderer.setBaseItemLabelsVisible(true);
		barRenderer.setShadowVisible(false);
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
		barRenderer.setDrawBarOutline(true);
		barRenderer.setSeriesOutlinePaint(1, new Color(83, 138, 185));
		barRenderer.setMaximumBarWidth(0.05);
		barChart.setBackgroundPaint(Color.white);
		barChart.setBorderPaint(Color.DARK_GRAY);
		BufferedImage outputImage = barChart.createBufferedImage(750, 350);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) outputImage, "png", baos);
		byte[] imageInByte = baos.toByteArray();
		Image image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image, true);
		cell.setBorderColor(new BaseColor(125, 126, 128));
		cell.setPadding(12);

		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk(" ", new Font(
				Font.FontFamily.TIMES_ROMAN, 8))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setFixedHeight(5f);
		cell.setPadding(0);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,
				BaseColor.BLACK);
		cell = new PdfPCell(new Phrase("Internal/External Risk",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL,
						BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(8f);
		cell.setBorderWidth(0.5f);
		cell.setBorderColor(new BaseColor(146, 144, 144));
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);

		JFreeChart pieChart = ChartFactory.createPieChart("",
				createPieDataset(assessment), true, false, true);
		pieChart.setBackgroundPaint(Color.white);
		PiePlot piePlot = (PiePlot) pieChart.getPlot();
		piePlot.setLabelFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 17));
		piePlot.setSectionPaint("Internal", new Color(213, 91, 103));
		piePlot.setSectionPaint("External", new Color(242, 186, 20));
		piePlot.setSectionPaint("Both", new Color(64, 132, 205));
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
		LegendTitle legendTitle = pieChart.getLegend();
		legendTitle.setPosition(RectangleEdge.RIGHT);
		legendTitle.setItemFont(new java.awt.Font(java.awt.Font.SANS_SERIF,
				java.awt.Font.PLAIN, 17));
		legendTitle.setBorder(0, 0, 0, 0);
		outputImage = pieChart.createBufferedImage(750, 350);
		baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) outputImage, "png", baos);
		imageInByte = baos.toByteArray();
		image = Image.getInstance(imageInByte);

		cell = new PdfPCell(image, true);
		cell.setPadding(12);
		cell.setBorderColor(new BaseColor(125, 126, 128));
		table.addCell(cell);
		document.add(table);
		document.newPage();

		List<AssessmentCategories> assessmentCategories = new ArrayList<AssessmentCategories>();
		assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		List<Answer> domainAnswer = new ArrayList<Answer>();
		if (assessmentCategories == null) {
			assessmentCategories = new ArrayList<AssessmentCategories>();
		}
		int j = 1;
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
			table.setWidthPercentage(100);
			columnWidths = new float[] { 1f };
			table.setWidths(columnWidths);
			font1 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLACK);

			cell = new PdfPCell(new Phrase(assessmentCategory
					.getAssignedCategories().getName(), FontFactory.getFont(
					FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.WHITE)));
			cell.setBackgroundColor(new BaseColor(125, 126, 128));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPaddingBottom(8f);
			cell.setBorderWidth(0.5f);
			cell.setBorderColor(new BaseColor(146, 144, 144));
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

			plot.setOutlineVisible(true);
			plot.setBackgroundPaint(new Color(237, 247, 255));
			barRenderer = (BarRenderer) plot.getRenderer();
			color1 = new Color(131, 188, 249);
			barRenderer.setBaseItemLabelsVisible(true);
			barRenderer.setShadowVisible(false);
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
			barRenderer.setDrawBarOutline(true);
			barRenderer.setSeriesOutlinePaint(1, new Color(83, 138, 185));
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
				cell.setFixedHeight(200);
				cell.setBorderColor(new BaseColor(125, 126, 128));
				table.addCell(cell);
			} else {
				cell = new PdfPCell(image, true);
				cell.setBorderColor(new BaseColor(125, 126, 128));
				cell.setPadding(12);
				table.addCell(cell);
			}
			cell = new PdfPCell(new Phrase(new Chunk(" ", new Font(
					Font.FontFamily.TIMES_ROMAN, 8))));
			cell.setBorderWidth(0f);
			cell.setFixedHeight(5f);
			cell.setPadding(0);
			table.addCell(cell);
			document.add(table);

			if (j % 3 == 0) {
				document.newPage();
			}
			j++;

		}

		if (j % 3 == 1 || j % 3 == 2) {
			document.newPage();
		}

		/*-table = new PdfPTable(1);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(" ", font));
		cell.setPaddingTop(8);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);*/

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk(
				"Controls and Their Responses", new Font(
						Font.FontFamily.TIMES_ROMAN, 17))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setBorderWidthBottom(.5f);
		cell.setBorderColor(new BaseColor(146, 144, 144));
		cell.setPadding(0);
		cell.setPaddingBottom(8);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(100);
		columnWidths = new float[] { 1f };
		table.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase(new Chunk(" ", new Font(
				Font.FontFamily.TIMES_ROMAN, 8))));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorderWidth(0f);
		cell.setFixedHeight(2f);
		cell.setPadding(0);
		table.addCell(cell);
		document.add(table);

		table1 = new PdfPTable(6);
		table1.setWidthPercentage(100);
		columnWidths = new float[] { 0.5f, 3f, 0.8f, 1f, 2f, 1f };
		table1.setWidths(columnWidths);
		cell = new PdfPCell(new Phrase("#", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(7f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Control", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Ans", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Risk", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Comment", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);

		cell = new PdfPCell(new Phrase("Mitigation", FontFactory.getFont(
				FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.WHITE)));
		cell.setBackgroundColor(new BaseColor(125, 126, 128));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(7f);
		cell.setBorderWidth(0f);
		table1.addCell(cell);

		int i = 1;
		for (AssessmentCategories assessmentCategory : assessmentCategories) {
			cell = new PdfPCell(new Phrase("", FontFactory.getFont(
					FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
			cell.setBackgroundColor(BaseColor.WHITE);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setColspan(6);
			cell.setFixedHeight(4f);
			cell.setBorderWidth(0f);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(assessmentCategory
					.getAssignedCategories().getName(), FontFactory.getFont(
					FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
			cell.setBackgroundColor(new BaseColor(235, 241, 243));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPaddingLeft(12f);
			cell.setPaddingBottom(7f);
			cell.setColspan(6);
			cell.setBorderWidth(0f);
			cell.setBorderColor(new BaseColor(125, 126, 128));
			cell.setBorderWidthBottom(.5f);
			cell.setBorderWidthTop(0.5f);
			table1.addCell(cell);

			domainAnswer = controlDao
					.allAnswerbyAssessmentCategory(assessmentCategory);
			if (domainAnswer == null) {
				domainAnswer = new ArrayList<Answer>();
			}
			for (Answer ans : domainAnswer) {

				// ------------------------index ----------------
				cell = new PdfPCell(new Phrase(String.valueOf(i),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 10,
								Font.NORMAL, BaseColor.BLACK)));
				cell.setBorderWidth(0f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(228, 228, 228));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setPaddingBottom(7f);
				table1.addCell(cell);
				// --------- index finished -----------------------

				// ---------------------Control--------------------------
				cell = new PdfPCell(new Phrase(ans.getControl().getControl()
						.getControl(), FontFactory.getFont(
						FontFactory.TIMES_ROMAN, 10, Font.NORMAL,
						BaseColor.BLACK)));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(228, 228, 228));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setPaddingBottom(7f);
				cell.setBorderWidth(0f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(cell);
				// ---------------------Control finished --------------------

				// --------------------- Answer ------------------------------
				cell = new PdfPCell(new Phrase(ans.getAnswer(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 10,
								Font.NORMAL, BaseColor.BLACK)));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(228, 228, 228));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorderWidth(0f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingBottom(7f);
				table1.addCell(cell);
				// ----------------------answer finished -------------------

				// ------------------------Risk ----------------------
				cell = new PdfPCell(new Phrase(String.valueOf(riskCalculation
						.getRiskForAnswer(ans)), FontFactory.getFont(
						FontFactory.TIMES_ROMAN, 10, Font.NORMAL,
						BaseColor.BLACK)));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(228, 228, 228));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorderWidth(0f);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(cell);
				// ----------------------Risk finished ---------------------

				// -----------------------Comment -----------------------------
				cell = new PdfPCell(new Phrase(ans.getComment(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 10,
								Font.NORMAL, BaseColor.BLACK)));
				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(228, 228, 228));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorderWidth(0f);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(cell);
				// -----------------------Comment finished ---------------------

				// ------------------mitigationDate --------------------
				if (ans.getMitigationDate() != null) {
					SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
					cell = new PdfPCell(new Phrase(dt1.format(ans
							.getMitigationDate()), FontFactory.getFont(
							FontFactory.TIMES_ROMAN, 10, Font.NORMAL,
							BaseColor.BLACK)));
				} else {
					cell = new PdfPCell(new Phrase("", FontFactory.getFont(
							FontFactory.TIMES_ROMAN, 10, Font.NORMAL,
							BaseColor.BLACK)));
				}

				if (i % 2 == 0) {
					cell.setBackgroundColor(new BaseColor(228, 228, 228));
				} else {
					cell.setBackgroundColor(BaseColor.WHITE);
				}
				cell.setBorderWidth(0f);
				cell.setPaddingBottom(7f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table1.addCell(cell);
				i++;
			}

			cell = new PdfPCell(new Phrase("", FontFactory.getFont(
					FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
			cell.setBackgroundColor(new BaseColor(235, 241, 243));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(0f);
			cell.setFixedHeight(0f);
			cell.setColspan(6);
			cell.setBorderWidth(0f);
			cell.setBorderWidthBottom(1f);
			cell.setBorderColor(new BaseColor(125, 126, 128));
			table1.addCell(cell);
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