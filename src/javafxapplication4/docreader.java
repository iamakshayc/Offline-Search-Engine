/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *
 * @author hp
 */
public class docreader {

	public String[] readDocFile(String fileName) {
String[] paragraphs=null;
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			HWPFDocument doc = new HWPFDocument(fis);

			WordExtractor we = new WordExtractor(doc);

			paragraphs = we.getParagraphText();
			
			
			fis.close();
			we.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paragraphs;

}
	public List<XWPFParagraph> readDocxFile(String fileName) {
		
		List<XWPFParagraph> paragraphs=null;
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			XWPFDocument document = new XWPFDocument(fis);

			paragraphs = document.getParagraphs();
			
			
			
			
			fis.close();
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paragraphs;
	}

}
