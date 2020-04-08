package com.sherwin.shercolor.customershercolorweb.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.Test;
import org.junit.internal.runners.TestClass;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;

public class TestSherColorLabel {
	ShercolorLabelPrintImpl testClass = new ShercolorLabelPrintImpl();
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

		PDFont fontBold = testClass.getUnicode();
		String name = fontBold.getName();
		boolean check = (name.contentEquals("LucidaSansUnicode") || (name.contentEquals("DejaVuSansMono-Bold"))) ;
		assertTrue(check);
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
