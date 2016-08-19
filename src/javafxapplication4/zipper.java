/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author hp
 */
public class zipper
{
	int BUFFER = 2048;
	public void zip(String name)
	{
	      try {
	          BufferedInputStream origin = null;
	          FileOutputStream dest = new 
	            FileOutputStream("c:\\neuralnet\\"+name+".zip");
	          ZipOutputStream out = new ZipOutputStream(new 
	            BufferedOutputStream(dest));
	          //out.setMethod(ZipOutputStream.DEFLATED);
	          byte data[] = new byte[BUFFER];
	          // get a list of files from current directory
	          File f = new File("C:\\neural");
	          String files[] = f.list();
	          for(int i=0;i<files.length;i++)
	         	 files[i]="C:\\neural\\"+files[i];
	          System.out.println(files[1]);
	          for (int i=0; i<files.length; i++) {
	             System.out.println("Adding: "+files[i]);
	             
	             FileInputStream fi = new 
	               FileInputStream(files[i]);
	         
	             origin = new 
	               BufferedInputStream(fi, BUFFER);
	             ZipEntry entry = new ZipEntry(files[i]);
	             out.putNextEntry(entry);
	             int count;
	             while((count = origin.read(data, 0, 
	               BUFFER)) != -1) {
	                out.write(data, 0, count);
	             }
	             origin.close();
	          }
	          out.close();
	       } catch(Exception e) {
	          e.printStackTrace();
	       }
	}
	public void unzip(String name)
	{
	      try {
	          BufferedOutputStream dest = null;
	          FileInputStream fis = new 
	 	   FileInputStream(name);
	          ZipInputStream zis = new 
	 	   ZipInputStream(new BufferedInputStream(fis));
	          ZipEntry entry;
	          while((entry = zis.getNextEntry()) != null) {
	             System.out.println("Extracting: " +entry);
	             int count;
	             byte data[] = new byte[BUFFER];
	             // write the files to the disk
	             FileOutputStream fos = new 
	 	      FileOutputStream(entry.getName());
	             dest = new 
	               BufferedOutputStream(fos, BUFFER);
	             while ((count = zis.read(data, 0, BUFFER)) 
	               != -1) {
	                dest.write(data, 0, count);
	             }
	             dest.flush();
	             dest.close();
	          }
	          zis.close();
	       } catch(Exception e) {
	          e.printStackTrace();
	       }
	}
}
