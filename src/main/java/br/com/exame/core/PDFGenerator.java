package br.com.exame.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.exame.utils.YamlHelper;
import cucumber.api.Scenario;

public class PDFGenerator {

	private Method method;
	private Document document;
	YamlHelper yamlhelper = new YamlHelper();

	public void iniciaPDF(Scenario cenario) throws Exception {
		String featureName = "";
		Date agora = new Date();
		String rawFeatureName = cenario.getId().split(";")[0].replace("-", " ");
		featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);
		String converterDataParaString = DateFormat.getInstance().format(agora).toString();

		document = new Document(PageSize.LETTER.rotate());
		document.setPageSize(PageSize.LETTER);
		File file = new File("./src/main/report/" + featureName);
		file.mkdir();
		String pathtoFolder = file.getAbsolutePath().toString();
		PdfWriter.getInstance(document,new FileOutputStream((pathtoFolder + "/" + "  AMBIENTE[STG]  " + cenario.getName()+ " " +converterDataParaString.replace("/","-").replace(":", "h")  +" " + ".pdf")));

		document.open();
	

		String[] headers = new String[] { "ENV", "Feature", "Cenario", "Data" };
		String[][] rows = new String[][] {{ yamlhelper.getAtributo("nome-env", "uat").toString() + " - OS: " + getOperatingSystem(), featureName,
						"Cenario: " + cenario.getName(),converterDataParaString }, };

		try {

			PdfWriter.getInstance(document, new FileOutputStream(new File("./src/main/report/Table.pdf")));
			document.open();
			Font fontHeader = new Font(Font.FontFamily.HELVETICA, 17, Font.NORMAL);
			Font fontRow = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

			Font formatColor = new Font();
			formatColor.setFamily("HELVETICA");
			formatColor.setStyle(Font.BOLD);
			formatColor.setSize(15);
			formatColor.setColor(BaseColor.BLACK);
			String content = "RELATORIO DE AUTOMACAO DE TESTES";

			Image logo = Image.getInstance("./src/main/report/logo-exame.png");
			logo.scalePercent(100);
			document.add(logo);
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(content, formatColor));
			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(headers.length);

			for (String header : headers) {
				PdfPCell cell = new PdfPCell();
				cell.setGrayFill(0.9f);
				cell.setPhrase(new Phrase(header.toUpperCase(), fontHeader));
				table.addCell(cell);
			}

			table.completeRow();

			for (String[] row : rows) {
				for (String data : row) {
					int cont = headers.length;
					if (headers.length == 3) {
						if (cenario.getStatus() == "pending") {
							Font color = new Font();
							color.setFamily("Courier");
							color.setStyle(Font.BOLD);
							color.setSize(20);
							color.setColor(BaseColor.GREEN);
							Phrase phrase = new Phrase(data, color);
							table.addCell(new PdfPCell(phrase));
						} else {
							Font color = new Font();
							color.setFamily("Courier");
							color.setColor(BaseColor.RED);
							color.setStyle(Font.BOLD);
							color.setSize(20);
							Phrase phrase = new Phrase(data, color);
							table.addCell(new PdfPCell(phrase));
						}

					}

					Phrase phrase = new Phrase(data, fontRow);

					table.addCell(new PdfPCell(phrase));
				}
				table.completeRow();
			}

			document.setMargins(40, 40, 80, 40);
			document.add(table);

		} catch (DocumentException FileNotFoundException) {

		} finally {

		}

		document.add(Chunk.NEWLINE);

	}

	public void conteudoPDF(String descricaoPasso, String conteudo) throws Exception {

		document.newPage();

		Font color = new Font();
		color.setFamily("Courier");
		color.setStyle(Font.BOLD);
		color.setSize(15);
		color.setColor(BaseColor.BLACK);

		document.add(new Paragraph(descricaoPasso, color));

	
		document.newPage();

		Font color2 = new Font();
		color.setFamily("Courier");
		color.setStyle(Font.BOLD);
		color.setSize(15);
		color.setColor(BaseColor.BLUE);

		document.add(new Paragraph(conteudo, color2));
	}

	public void fechaPDF(Scenario cenario) throws DocumentException, IOException {
		if (cenario.isFailed()) {

			document.newPage();

			Font colorFailed = new Font();
			colorFailed.setFamily("Courier");
			colorFailed.setStyle(Font.BOLD);
			colorFailed.setSize(15);
			colorFailed.setColor(BaseColor.RED);

			document.add(new Paragraph("Status: " + cenario.getStatus(), colorFailed));
			
			document.close();
				
		}else {
			Font colorPassed = new Font();
			colorPassed.setFamily("Courier");
			colorPassed.setColor(BaseColor.GREEN);
			colorPassed.setStyle(Font.BOLD);
			colorPassed.setSize(20);
			document.add(new Paragraph("Status: " + cenario.getStatus(), colorPassed));

			document.close();
		}
	}

	public String scenarioName() {
		return method.getName();
	}

	public String getOperatingSystem() {
		String os = System.getProperty("os.name");
		return os;
	}

}
