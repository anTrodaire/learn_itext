package learn_itext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class BasicPDF {
	
	private static final String DESTINATION = "D:/Users/Alex/Desktop/results.pdf";

	public static void main(String[] args) {
		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(DESTINATION));
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		doc.open();
		try {
			doc.add(new Paragraph("hello world"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		doc.close();
	}
}
