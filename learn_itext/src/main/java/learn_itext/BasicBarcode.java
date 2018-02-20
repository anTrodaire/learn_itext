package learn_itext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class BasicBarcode {

	private static final String DEST = "C:/Users/axp338/Desktop/basic_barcode.pdf";

	public static void main(String[] args) {
		Document doc = new Document();
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST));
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
			return;
		}

		doc.open();

		PdfContentByte contentBytes = writer.getDirectContent();

		Barcode39 code39 = new Barcode39();
		code39.setCode("8211-17-02-0001");
		try {
			doc.add(code39.createImageWithBarcode(contentBytes, null, null));
		} catch (DocumentException e) {
			e.printStackTrace();
			return;
		}

		doc.close();
	}
}
