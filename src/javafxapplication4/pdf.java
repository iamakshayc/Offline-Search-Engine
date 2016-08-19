/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author hp
 */
public class pdf
{
	public PDDocument doc;
	PDFTextStripper stripper;
	public pdf(String document)
	{
		doc = null;
		 File file = new File(document);
		 System.out.print(file.getAbsolutePath());
       try {
           doc = PDDocument.load(file.getAbsolutePath());
       } catch (IOException ex) {
      	 System.out.println("file not found");
       }
       stripper = null;
       try {
           stripper = new PDFTextStripper();
       } catch (IOException ex) {
      	 System.out.println("Stripper : read permission error");
       }

	}
	public int pdfsize()
	{
		return doc.getNumberOfPages();
	}
	public String readpdf(int start,int end)
	 {
		String s=null;
        if(start>0)
       	 stripper.setStartPage(start);
       // else
       	// stripper.setStartPage(0);
        if(end>0)
       	 stripper.setEndPage(end);
        //else
       	// stripper.setEndPage(stripper.getEndPage());
       // System.out.println(stripper.getParagraphStart());
        try {
        
			s=stripper.getText(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("read permission error");
		}
		 return s;
	 }
}
