package com.sherwin.shercolor.customershercolorweb.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.Test;
import org.junit.internal.runners.TestClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.DrawdownLabelService;
import com.sherwin.shercolor.common.service.FormulationService;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@WebAppConfiguration
@Transactional
public class TestSherColorLabel {
	
	@Autowired
	DrawdownLabelService drawdownLabelService;
	@Autowired
	CustomerService customerService;
	@Autowired
	ColorMastService colorMastService;
	@Autowired
	FormulationService formulationService;
	
	ShercolorLabelPrintImpl testClass = new ShercolorLabelPrintImpl(drawdownLabelService,customerService,colorMastService,formulationService);
	// Create a new empty document
	PDDocument document = new PDDocument();

	@Test
	public void testHasUnicode() {
		String text = "правда";
		boolean result = testClass.hasUnicode(text);
		assertTrue(result);
	}

	@Test
	public void testDoesNotHaveUnicode() {
		String text = "truth";
		boolean result = testClass.hasUnicode(text);
		assertFalse(result);
	}


	@Test
	public void testReplaceUnicode() {
		String text = "Theправда";
		String result = testClass.replaceUnicode(text);
		boolean check = (result.contentEquals("The######") || (result.contentEquals("The###"))) ;
		assertEquals(true,check);
	}
	@Test
	public void testGetUnicode() {

		PDFont fontBold = testClass.getUnicode("");
		
		assertNotNull(fontBold);
	}
	
	@Test 
	public void testCellSettings1() {
		int fontSize = 10;
		int rowHeight=12;
		float cellWidth=100f;
		BaseTable table = createTopTable();
		Row<PDPage> row = table.createRow(rowHeight);
		Cell<PDPage> color = row.createCell(cellWidth, "правда" + " " + "2правда", HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
		testClass.cellSettings(color,fontSize,rowHeight );
		try {
			float y = table.draw();
			document.save("testlabel1.pdf");
			document.close();
			assertTrue(y > 0 );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BaseTable createTopTable() {
		float margin = 3;
		float bottomMargin = 0;
		// Create a new blank page and add it to the document
		PDPage page = new PDPage();
		// starting y position is whole page height subtracted by top and bottom margin
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		// we want table across whole page width (subtracted by left and right margin ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		float yPosition = yStartNewPage;
		boolean drawContent = true;
		boolean drawLines = false;
		BaseTable table = null;
		try {
			table = new BaseTable(yPosition, yStartNewPage,
					bottomMargin, tableWidth, margin, document, page, drawLines, drawContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}
}
