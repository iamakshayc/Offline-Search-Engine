/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *
 * @author hp
 */
class MyComparator implements Comparator<landc> {
    @Override
    public int compare(landc landc0, landc landc1) {
        int w0 = landc0.count;
        int w1 = landc1.count;
        return (w0 > w1? -1 : (w0 == w1) ? 0 : 1);
    }
}
class Node implements Serializable  {
    private String label;
    private ArrayList<Node> adj;
    private ArrayList<Integer> lnum;
    public Node()
    {
    	adj = new ArrayList<Node>();
    	lnum = new ArrayList<Integer>();
       
    }
    
    public void setAdj(Node n) {
        if(!adj.contains(n))
            adj.add(n);
    }
    public ArrayList<Node> getAdj() {
        return adj;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        label = label;
    }
    public void setlnum(int i) {
    	int sz=lnum.size();
    	if(sz!=0)
    	{
    		if(i>lnum.get(lnum.size()-1))
    			lnum.add(i);
    	}
    	else
    		lnum.add(i);
    }
    

    public ArrayList<Integer> getlnum() {
        return lnum;
    }
}

class allline implements Serializable
{
	public ArrayList<landc> aline;
	public ArrayList<Integer> lend;
	public ArrayList<String> docname;
	allline()
	{
		this.aline = new ArrayList<landc>();
		this.lend = new ArrayList<Integer>();
		this.docname = new ArrayList<String>();
		//this.globvar = new landc();
	}
	public void scatter(int pos,int rad,int inc){
		int start = pos-rad;
		int end = pos+rad;
		if(start<0)start=0;
		if(end>=aline.size())end = aline.size()-1;
		for(int i=start;i<=end;i++){
			aline.get(i).count+=inc;
		}
	}
	public void setall(String s,int p,int u) {
		landc globvar = new landc();
		globvar.add_line(s,p,u);
        this.aline.add(globvar);
    }
	 public String getline(int i) {
	        return this.aline.get(i).line;
	    }
	 public int getsize() {
	        return this.aline.size();
	    }
}



class dict implements Serializable
{
	public  Map dictionary;
	dict()
	{
		this.dictionary = new HashMap();
	}
}

class landc implements Serializable{
	public String line;
	public int count,page,doc;
	public boolean visited;
	landc(){
		count=0;
		page=0;
		doc=0;
		visited=false;
	}
	void add_line(String s,int p,int u)
	{
		line=s;
		page=p;
		doc=u;
	}
}
class network
{
    String inp;
    dict d;
    allline all;
    zipper z;
    String file,search;
    ArrayList<File> files;
    private static final network Instance = new network();
    public static network getInstance(){
        return Instance;
    }
    private network()
    {
        inp = null;
        d=new dict();
        all=new allline();
        z=new zipper();
        files=new ArrayList<File>();
        file="";
        search="seed fill algorithm";
    }
    public ArrayList<String> makeSuggestions(String input) {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.addAll(charAppended(input));
        toReturn.addAll(charMissing(input));
        toReturn.addAll(charsSwapped(input));
        return toReturn;
    }

    public ArrayList<String> charAppended(String input) { 
        ArrayList<String> toReturn = new ArrayList<>();
        char alphabet[]=new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        for (char c : alphabet) {
            String atFront = c + input;
            String atBack = input + c;
            if (d.dictionary.containsKey(atFront)) {
                toReturn.add(atFront);
            }
            if (d.dictionary.containsKey(atBack)) {
                toReturn.add(atBack);
            }
        }
        return toReturn;
    }

    public ArrayList<String> charMissing(String input) {   
        ArrayList<String> toReturn = new ArrayList<>();

        int len = input.length() - 1;
        //try removing char from the front
        if (d.dictionary.containsKey(input.substring(1))) {
            toReturn.add(input.substring(1));
        }
        for (int i = 1; i < len; i++) {
            //try removing each char between (not including) the first and last
            String working = input.substring(0, i);
            working = working.concat(input.substring((i + 1), input.length()));
            if (d.dictionary.containsKey(working)) {
                toReturn.add(working);
            }
        }
        if (d.dictionary.containsKey(input.substring(0, len))) {
            toReturn.add(input.substring(0, len));
        }
        return toReturn;
    }

    public ArrayList<String> charsSwapped(String input) {   
        ArrayList<String> toReturn = new ArrayList<>();

        for (int i = 0; i < input.length() - 1; i++) {
            String working = input.substring(0, i);// System.out.println("    0:" + working);
            working = working + input.charAt(i + 1);  //System.out.println("    1:" + working);
            working = working + input.charAt(i); //System.out.println("    2:" + working);
            working = working.concat(input.substring((i + 2)));//System.out.println("    FIN:" + working); 
            if (d.dictionary.containsKey(working)) {
                toReturn.add(working);
            }
        }
        return toReturn;
    }
    public Vector<String> hyper(String str){
		System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		String wordForm = str;
		//  Get the synsets containing the wrod form
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(wordForm);
		//  Display the word forms and definitions for synsets retrieved
		 Vector<String> hypernyms = new Vector<String>();
	       
	       for(Synset synset : synsets) {
	           if(synset.getType()==SynsetType.NOUN) {
	                NounSynset nounSynset = (NounSynset) synset;
	                
	                NounSynset[] nounSynsets = nounSynset.getHypernyms();
	                
	                for(NounSynset noun : nounSynsets) {
	                    String[] wordForms = noun.getWordForms();
	                    
	                    for(String wordFor : wordForms) {
	                        hypernyms.add(wordFor);
	                    }
	                }
	           }
	       }
	       return hypernyms;
	}
	 public String getStructure(String word) {
	       
		 WordNetDatabase database = WordNetDatabase.getFileInstance();
			Synset[] synsets = database.getSynsets(word);
	        String structure="";
	            for(Synset synset : synsets) {
	                if(synset.getType()==SynsetType.NOUN) {
	                    structure += "N";
	                } else if(synset.getType()==SynsetType.VERB) {
	                    structure += "V ";
	                } else if(synset.getType()==SynsetType.ADVERB) {
	                    structure += "ADV ";
	                } else if(synset.getType()==SynsetType.ADJECTIVE) {
	                    structure += "ADJ ";
	                } else {
	                    structure += "U ";
	                }
	            }
	        return structure;
	    }
	 public boolean isAlpha(String name) {
		    char[] chars = name.toCharArray();

		    for (char c : chars) {
		        if(!Character.isLetter(c)) {
		            return false;
		        }
		    }

		    return true;
		}
	 public boolean isUpper(String s)
	 {
	     for(char c : s.toCharArray())
	     {
	         if(! Character.isUpperCase(c))
	             return false;
	     }

	     return true;
	 }
	 public boolean ContainsDigit(String name) {
		    char[] chars = name.toCharArray();

		    for (char c : chars) {
		        if(Character.isDigit(c)) {
		            return true;
		        }
		    }
		    return false;
		}
	 public String readpdf(String document,int start,int end)
	 {
		 String s=null;
		 PDDocument doc = null;
		 File file = new File(document);
		 System.out.print(file.getAbsolutePath());
         try {
             doc = PDDocument.load(file.getAbsolutePath());
         } catch (IOException ex) {
        	 System.out.println("file not found");
         }

         PDFTextStripper stripper = null;
         try {
             stripper = new PDFTextStripper();
         } catch (IOException ex) {
        	 System.out.println("Stripper : read permission error");
         }
         
         if(start>0)
        	 stripper.setStartPage(start);
        // else
        	// stripper.setStartPage(0);
         if(end>0)
        	 stripper.setEndPage(end);
         //else
        	// stripper.setEndPage(stripper.getEndPage());
         System.out.println(stripper.getParagraphStart());
         try {
			s=stripper.getText(doc); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("read permission error");
		}
		 return s;
	 }
	 
	 public void loadnetwork(String name)
	 {      double startTim = System.nanoTime();
    	        /*Load network*/
        z.unzip(name);
        double endTim = System.nanoTime();
        System.out.println("unzip Time "+(endTim - startTim)/1000000000 + " s");
	       // z.unzip();
                startTim = System.nanoTime();
	        if (new File("c:/neural/neural.db").exists()) {
                    endTim = System.nanoTime();
        System.out.println("neural first call Time "+(endTim - startTim)/1000000000 + " s");
	            try {
	                ObjectInputStream objectIn = null;
	                try {
                            startTim = System.nanoTime();
	                    objectIn = new ObjectInputStream(new FileInputStream("c:/neural/neural.db"));
                            endTim = System.nanoTime();
        System.out.println("neural second call Time "+(endTim - startTim)/1000000000 + " s");
	                } catch (IOException ex) {
	                    //Logger.getLogger(BigDataSPTheoryMain.class.getName()).log(Level.SEVERE, null, ex);
	                }
	                startTim = System.nanoTime();
	                d = (dict) objectIn.readObject();
                        endTim = System.nanoTime();
        System.out.println("dict Time "+(endTim - startTim)/1000000000 + " s");
	            }catch (IOException ex) {} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
	        if (new File("c:/neural/line.db").exists()) {
	            try {
	                ObjectInputStream objectIn = null;
	                try {
	                    objectIn = new ObjectInputStream(new FileInputStream("c:/neural/line.db"));
	                } catch (IOException ex) {
	                    //Logger.getLogger(BigDataSPTheoryMain.class.getName()).log(Level.SEVERE, null, ex);
	                }
	                
	                all= (allline) objectIn.readObject();
	            }catch (IOException ex) {} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
             
	        
	 }
	 public void createnetwork()
	 {
             
             for(File f:files)
             {
                 boolean contloop=false;
                 file=f.getAbsolutePath();
                 for(String existed:all.docname)
                     if(existed.equalsIgnoreCase(file))
                     {
                         contloop=true;
                         break;
                     }
                 if(contloop)
                 {
                     System.out.println(file+" already exists");
                     continue;
                 }
                 System.out.println(file);
		int doc_num=all.docname.size()+1;
	        
	        if(file.endsWith(".pdf"))
	        {
	        	pdf p=new pdf(file);
	        	int size=p.pdfsize();
	        	//String totalcont=readpdf(file,50,50);
                        
                        
                                
	        	for(int y=1;y<=size;y++)
	        	{
                                
                                
	        		String totalcont=p.readpdf(y,y);
	        		//System.out.println(y);
	        		String[] lines=totalcont.split("\n");
	        		for(String line:lines)
	        		{
	        		String[] arr=line.split("\\.");
	        		for(String ss:arr)
	        		{
	        			all.setall(ss,y,doc_num);
	           		 ss=ss.replaceAll(","," ").replaceAll("\\?"," ").replaceAll("!"," ");
	           	       String[] words = ss.split(" ");  
                               
	           	    	   for ( String word : words)
	           	    	   {
	           	    	   word=word.toLowerCase();
	           	    	  if(ContainsDigit(word)){continue;}
	           	    	   Node val=(Node) d.dictionary.get(word);
	           	    	   if(val!=null)
	           	    	   {
	           	    		   val.setlnum(all.getsize()-1);
                                          
                                          
	           	    	   }
	           	    	   else
	           	    	   {
	           	    		   Vector hp=hyper(word);
	           	    		   Node nnode=new Node();
	           	    		   nnode.setLabel(word);
	           	    		   nnode.setlnum(all.getsize()-1);
	           	    		   
	           	    		   for(int x=0;x<hp.size();x++)
	           	    		   {
	           	    			   Node valhp=(Node) d.dictionary.get(hp.elementAt(x).toString());
	                   	    	   if(valhp!=null)
	                   	    	   {
	                   	    		   nnode.setAdj(valhp);
	           
	                   	    	   }else
	                   	    	   {
	                   	    		   Node nhpnode=new Node();
	                   	    		   nhpnode.setLabel(hp.elementAt(x).toString());
	                   	    		   d.dictionary.put(hp.elementAt(x).toString(), nhpnode);
	                   	    		   nnode.setAdj(nhpnode);
	                   	    	   }
	           	    		   }
	           	    		   d.dictionary.put(word,nnode);
	           	    	   
	           	    	   }
                                   
                                   
	           	    	   }
	        		}
	        		}
	        		all.aline.size();
	        	}
                     try {
                         p.doc.close();
                     } catch (IOException ex) {
                         Logger.getLogger(network.class.getName()).log(Level.SEVERE, null, ex);
                     }
	        }
                else if(file.endsWith(".doc"))
                {
                    docreader doc=new docreader();
                    String [] paragraphs=doc.readDocFile(file);
                    
                    int i=0;
                    for(String para:paragraphs)
                    {
                        String[] lines=para.split("\n");
	        		for(String line:lines)
	        		{
	        		String[] arr=line.split("\\.");
	        		for(String ss:arr)
	        		{
	        			all.setall(ss,i,doc_num);
	           		 ss=ss.replaceAll(","," ").replaceAll("\\?"," ").replaceAll("!"," ");
	           	       String[] words = ss.split(" "); 
                               ArrayList<Node> sentnodes=new ArrayList<Node>();
	           	    	   for ( String word : words)
	           	    	   {
	           	    	   word=word.toLowerCase();
	           	    	  if(ContainsDigit(word)){continue;}
	           	    	   Node val=(Node) d.dictionary.get(word);
	           	    	   if(val!=null)
	           	    	   {
	           	    		   val.setlnum(all.getsize()-1);
                                           
	           	    	   }
	           	    	   else
	           	    	   {
	           	    		   Vector hp=hyper(word);
	           	    		   Node nnode=new Node();
	           	    		   nnode.setLabel(word);
	           	    		   nnode.setlnum(all.getsize()-1);
	           	    		   
	           	    		   
	           	    		   for(int x=0;x<hp.size();x++)
	           	    		   {
	           	    			   Node valhp=(Node) d.dictionary.get(hp.elementAt(x).toString());
	                   	    	   if(valhp!=null)
	                   	    	   {
	                   	    		   nnode.setAdj(valhp);
	           
	                   	    	   }else
	                   	    	   {
	                   	    		   Node nhpnode=new Node();
	                   	    		   nhpnode.setLabel(hp.elementAt(x).toString());
	                   	    		   d.dictionary.put(hp.elementAt(x).toString(), nhpnode);
	                   	    		   nnode.setAdj(nhpnode);
	                   	    	   }
	           	    		   }
	           	    		   d.dictionary.put(word,nnode);
                                          
	           	    	   }
                        
                    }
                                    
                                }
                                }
                                i++;
                    }
                    
                }        
                else if(file.endsWith(".docx"))
                {
                    docreader doc=new docreader();
                     List<XWPFParagraph> paragraphs=doc.readDocxFile(file);
                    
                    int i=0;
                    for(XWPFParagraph para:paragraphs)
                    {
                        String[] lines=para.getText().split("\n");
	        		for(String line:lines)
	        		{
	        		String[] arr=line.split("\\.");
	        		for(String ss:arr)
	        		{
	        			all.setall(ss,i,doc_num);
	           		 ss=ss.replaceAll(","," ").replaceAll("\\?"," ").replaceAll("!"," ");
	           	       String[] words = ss.split(" ");   
                               
	           	    	   for ( String word : words)
	           	    	   {
	           	    	   word=word.toLowerCase();
	           	    	  if(ContainsDigit(word)){continue;}
	           	    	   Node val=(Node) d.dictionary.get(word);
	           	    	   if(val!=null)
	           	    	   {
	           	    		   val.setlnum(all.getsize()-1);
                                          
	           	    	   }
	           	    	   else
	           	    	   {
	           	    		   Vector hp=hyper(word);
	           	    		   Node nnode=new Node();
	           	    		   nnode.setLabel(word);
	           	    		   nnode.setlnum(all.getsize()-1);
	           	    		  
	           	    		   
	           	    		   for(int x=0;x<hp.size();x++)
	           	    		   {
	           	    			   Node valhp=(Node) d.dictionary.get(hp.elementAt(x).toString());
	                   	    	   if(valhp!=null)
	                   	    	   {
	                   	    		   nnode.setAdj(valhp);
	           
	                   	    	   }else
	                   	    	   {
	                   	    		   Node nhpnode=new Node();
	                   	    		   nhpnode.setLabel(hp.elementAt(x).toString());
	                   	    		   d.dictionary.put(hp.elementAt(x).toString(), nhpnode);
	                   	    		   nnode.setAdj(nhpnode);
	                   	    	   }
	           	    		   }
	           	    		   d.dictionary.put(word,nnode);
                                           
	           	    	   
	           	    	   }
                        
                    }
                                  
                                }
                                }
                                i++;
                    }
                    
                }        
                else
	        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
	            for(String line; (line = br.readLine()) != null; ) {
	             
	            	String[] arr = line.split("\\.");    

	            	 for ( String ss : arr) {
	            		 all.setall(ss,0,doc_num);
	            		 ss=ss.replaceAll(","," ").replaceAll("\\?"," ").replaceAll("!"," ").replaceAll("-"," ");
	            	       String[] words = ss.split(" ");  
                              
	            	    	   for ( String word : words)
	            	    	   {
	            	    	   word=word.toLowerCase();
	            	    	  if(ContainsDigit(word)){continue;}
	            	    	   Node val=(Node) d.dictionary.get(word);
	            	    	   if(val!=null)
	            	    	   {
	            	    		   val.setlnum(all.getsize()-1);
                                          
	            	    	   }
	            	    	   else
	            	    	   {
	            	    		   Vector hp=hyper(word);
	            	    		   Node nnode=new Node();
	            	    		   nnode.setLabel(word);
	            	    		   nnode.setlnum(all.getsize()-1);
	            	    		  
	            	    		   
	            	    		   for(int x=0;x<hp.size();x++)
	            	    		   {
	            	    			   Node valhp=(Node) d.dictionary.get(hp.elementAt(x).toString());
	                    	    	   if(valhp!=null)
	                    	    	   {
	                    	    		   nnode.setAdj(valhp);
	            
	                    	    	   }else
	                    	    	   {
	                    	    		   Node nhpnode=new Node();
	                    	    		   nhpnode.setLabel(hp.elementAt(x).toString());
	                    	    		   d.dictionary.put(hp.elementAt(x).toString(), nhpnode);
	                    	    		   nnode.setAdj(nhpnode);
	                    	    	   }
	            	    		   }
	            	    		   d.dictionary.put(word,nnode);
                                           
	            	    	   
	            	    	   }
	            	           
	            	        }
                                   
	            	  }
	            }
	            
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        all.lend.add(all.getsize());
	        all.docname.add(file);
                
             }

	 }
         
	 public void storenetwork(String name)
	 {
             
		 if (!new File("c:\\neural").exists()) {
             new File("c:\\neural").mkdir();
         }
                 FileOutputStream fileOut=null;
		 try {
			 fileOut = new FileOutputStream(new File("c:\\neural\\neural.db"));
		 } catch (FileNotFoundException e1) {
	// TODO Auto-generated catch block
		e1.printStackTrace();
		 }

		 	ObjectOutputStream objectOut = null;

		 	try {
     	objectOut = new ObjectOutputStream(fileOut);
		 	} catch (IOException ex) {
    
		 	}

		 	try {
     	objectOut.writeObject(d);
     	objectOut.flush();
		 	} catch (IOException ex) {
    
 	}

		 	try {
     	fileOut.close();
		 	} catch (IOException ex) {
    // Logger.getLogger(BigDataSPTheoryMain.class.getName()).log(Level.SEVERE, null, ex);
		 	}
		 	FileOutputStream lineOut=null;
		 	try {
		 		lineOut = new FileOutputStream(new File("c:\\neural\\line.db"));
		 	} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
		 		e.printStackTrace();
	}

 	ObjectOutputStream objectOu = null;
        

 	try {
 		objectOu = new ObjectOutputStream(lineOut);
 	} catch (IOException ex) {
    // Logger.getLogger(BigDataSPTheoryMain.class.getName()).log(Level.SEVERE, null, ex);
 	}

 	try {
     		objectOu.writeObject(all);
    	 objectOu.flush();
 	} catch (IOException ex) {
    // Logger.getLogger(BigDataSPTheoryMain.class.getName()).log(Level.SEVERE, null, ex);
 		}

 	try {
    	 lineOut.close();
 	} catch (IOException ex) {
    // Logger.getLogger(BigDataSPTheoryMain.class.getName()).log(Level.SEVERE, null, ex);
 	}
 	z.zip(name);
 	File f1=new File("C:\\neural\\line.db");
 	File f2=new File("C:\\neural\\neural.db");
 	f1.delete();
 	f2.delete();
             
	 }
         public void falsify()
         {
             for(int i=0;i<all.aline.size();i++)
             {
                 all.aline.get(i).visited=false;
                 all.aline.get(i).count=0;
             }
         }
	 public ArrayList <landc>  searcher(String s)
	 {
                System.out.println(s);
	        String[] keys=s.split(" "); 
	        ArrayList <landc> lines_to_print = new ArrayList<landc>();
	        Node l = new Node();
	        for (String key : keys){
	        	int inc=5,inh=1;
	        	if(isUpper(key))
	        	{
	        		inc=10;
	        		inh=2;
	        	}
	        	key=key.toLowerCase();
	        	l = (Node)d.dictionary.get(key);
	        	if(l==null)
	        		continue;
                       
	        	ArrayList <Integer> g=l.getlnum();
	        	for(int qq=0;qq<g.size();qq++){
	        		int pos = g.get(qq);
	        		all.scatter(pos,5,1);
	        		(all.aline.get(pos).count)+=inc;
	        		if(!all.aline.get(pos).visited){
	        			all.aline.get(pos).visited=true;
	        			lines_to_print.add(all.aline.get(pos));
	        		}
	        	}
	        	ArrayList a=l.getAdj();
	        	int j=0;
	        	while(j<a.size())
	        	{
	        		Node mn=(Node) a.get(j);
	        		for(int dd=0;dd<mn.getlnum().size();dd++)
	        		{
	        			int br=mn.getlnum().get(dd);
	        			all.aline.get(br).count+=inh;
	        			if(!all.aline.get(br).visited)
	        			{
	        				lines_to_print.add(all.aline.get(br));
	        				all.aline.get(br).visited=true;	
	        			}
					
	        		}
					//System.out.println(all.getline(mn.getlnum().get(dd)));
	        		j++;
	        	}
	        }
	        Collections.sort(lines_to_print, new MyComparator());
              
	        int fh=lines_to_print.size();
                ArrayList<String> lp=new ArrayList<String>();
               
	        
                return lines_to_print;
	 }
         public void listf(String directoryName) {
            File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File fil: fList) {
            String h=fil.getAbsolutePath();
            if (fil.isFile()&&(h.endsWith(".txt")||h.endsWith(".pdf")||h.endsWith(".doc")||h.endsWith(".PDF")||h.endsWith(".DOC"))) {
                files.add(fil);
            } else if (fil.isDirectory()) {
                listf(fil.getAbsolutePath());
            }
        }
}
         
}
public class CreateController implements Initializable {
    network n;
    ObservableList<String> items;
    @FXML
    ProgressBar pbar;
    @FXML
    private TextField nname;
    @FXML
    private ListView<String> list;
    @FXML
    private void handleAddAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
       Control c = Control.getInstance();
 fileChooser.setTitle("Add Documents");
 fileChooser.getExtensionFilters().addAll(
         new FileChooser.ExtensionFilter("Text Files", "*.txt","*.pdf","*.PDF","*.doc","*.DOC","*.docx","*.DOCX"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(c.getStage().getScene().getWindow());
        for(int i=0;i<selectedFile.size();i++)
        {
            n.files.add(selectedFile.get(i));
        }
        items.clear();  
        for(int i=0;i<n.files.size();i++)
        {
            items.add(n.files.get(i).toString());
        }
        list.setItems(items);
    }
    @FXML
    private void handleCreateAction(ActionEvent event) {
        
       double startTim=System.nanoTime();
        
        n.createnetwork();
        pbar.setProgress(0.75);
        double endTim = System.nanoTime();
        System.out.println("Create Time "+(endTim - startTim)/1000000000 + " s");
        startTim=System.nanoTime(); 
        n.storenetwork(nname.getText().toString());
        pbar.setProgress(1);
        endTim = System.nanoTime();
        System.out.println("write Time "+(endTim - startTim)/1000000000 + " s");

       // Control c = Control.getInstance();
        //c.loadfxml("test.fxml");   
    }
    @FXML
    private void handleExitAction(ActionEvent event) {
        Control c = Control.getInstance();
        c.loadfxml("main.fxml");   
    }
    @FXML
    private void handleRemoveAction(ActionEvent event) {
        n.files.remove(list.getSelectionModel().getSelectedIndex());
        items.remove(list.getSelectionModel().getSelectedIndex());
        list.setItems(items);
    }
    @FXML public void handleListClick(MouseEvent arg0) {
    System.out.println("clicked on " + list.getSelectionModel().getSelectedIndex());
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pbar.setProgress(0);
        n=network.getInstance();
        items=FXCollections.observableArrayList (
    "Single", "Double", "Suite", "Family App");
        
    }   
    public void setp(double d)
    {
        pbar.setProgress(d);
        
    }
    
}
