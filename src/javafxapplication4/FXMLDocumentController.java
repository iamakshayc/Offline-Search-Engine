/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication4;
import java.awt.Desktop;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;


/**
 *
 * @author hp
 */


public class FXMLDocumentController implements Initializable {
    network n;
    TreeMap t;
     ArrayList<String> sf;
    ArrayList <landc> lines_to_print;
    ObservableList<String> items;
    @FXML
    private Label label;
    @FXML
    private  ComboBox searchfield;
    @FXML
    private ListView<String> list;
    @FXML
    private void handleAddAction(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
       Control c = Control.getInstance();
 fileChooser.setTitle("Add Documents");
 fileChooser.getExtensionFilters().addAll(
         new ExtensionFilter("Text Files", "*.txt","*.pdf","*.PDF","*.doc","*.DOC","*.docx","*.DOCX"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(c.getStage().getScene().getWindow());
        label.setText(selectedFile.get(0).getAbsolutePath());
        
    }
   
    @FXML
    private void handleLoadAction(ActionEvent event) {
        label.setText("Load");
        
    	        /*Load network*/
        Control c = Control.getInstance();
        File dir=new File("C:\\neuralnet");
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Choose Network File");
         fileChooser.setInitialDirectory(dir);
 fileChooser.getExtensionFilters().addAll(
         new ExtensionFilter("Network Files","*.ZIP","*.zip"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(c.getStage().getScene().getWindow());
        double startTim = System.nanoTime();
        n.loadnetwork(selectedFile.get(0).getAbsolutePath());
        
        t.putAll(n.d.dictionary);
       
        sf.addAll(t.keySet());
        double endTim = System.nanoTime();
        System.out.println("Load Time "+(endTim - startTim)/1000000000 + " s");
        
    }
    @FXML
    private void handleComboAction(ActionEvent event) {
        double startTim=System.nanoTime();   
        lines_to_print = n.searcher(searchfield.getValue().toString());
        double endTim = System.nanoTime();
        System.out.println("Output Time "+(endTim - startTim)/1000000000 + " s"); 
        items.clear();
        ArrayList<String> lp=new ArrayList<String>();
        for(int ww=0;ww<lines_to_print.size();ww++){
	       // System.out.println(lines_to_print.get(ww).line+"::"+lines_to_print.get(ww).page+"::"+n.all.docname.get(lines_to_print.get(ww).doc-1));
                String f=lines_to_print.get(ww).line+"\n"+n.all.docname.get(lines_to_print.get(ww).doc-1)+"-"+"page "+lines_to_print.get(ww).page;
                lp.add(f);
	       }
        for(int i=0;i<lp.size();i++)
        {
           items.add(lp.get(i));
        }
        list.setItems(items);
        endTim = System.nanoTime();
        System.out.println("Output Time "+(endTim - startTim)/1000000000 + " s");
        n.falsify();
        /*
        startTim=System.nanoTime(); 
         String stext=searchfield.getValue().toString();
        searchfield.getItems().clear();
         searchfield.setValue(stext);
        if(items.size()==0)
        {
        suggest s=new suggest();
        String[] mmi=stext.split(" ");
        s.q=mmi[mmi.length-1];
        sf=s.sorter(sf);
       
        String sugg=mmi[0];
        for(int j=1;j<mmi.length-1;j++)
            sugg=sugg+" "+mmi[j];
        for(int i=0;i<sf.size();i++)
        {
            if(searchfield.getItems().size()<5)
            {
                Node newnode=(Node) n.d.dictionary.get(sf.get(i));
                if(newnode.getlnum().size()==0)
                    continue;
                if(mmi.length>1)
                     searchfield.getItems().add(sugg+" "+sf.get(i));
                else
                    searchfield.getItems().add(sf.get(i));
            }
            else
                break;
        }
        searchfield.show();
        }   
        endTim = System.nanoTime();
        System.out.println("Suggestion Time "+(endTim - startTim)/1000000000 + " s");*/
    }
     @FXML
    private void handleCreateAction(ActionEvent event) {
        
        Control c = Control.getInstance();
        c.loadfxml("create.fxml");;
    }
   
     @FXML
    private void handleSearchAction(ActionEvent event) {
       // label.setText(searchfield.getText());
        double startTim=System.nanoTime();   
        lines_to_print = n.searcher(searchfield.getValue().toString());
        double endTim = System.nanoTime();
        System.out.println("Output Time "+(endTim - startTim)/1000000000 + " s"); 
        items.clear();
        ArrayList<String> lp=new ArrayList<String>();
        for(int ww=0;ww<lines_to_print.size();ww++){
	       // System.out.println(lines_to_print.get(ww).line+"::"+lines_to_print.get(ww).page+"::"+n.all.docname.get(lines_to_print.get(ww).doc-1));
                String f=lines_to_print.get(ww).line+"\n"+n.all.docname.get(lines_to_print.get(ww).doc-1)+"-"+"page "+lines_to_print.get(ww).page;
                lp.add(f);
	       }
        for(int i=0;i<lp.size();i++)
        {
           items.add(lp.get(i));
        }
        list.setItems(items);
        endTim = System.nanoTime();
        System.out.println("Output Time "+(endTim - startTim)/1000000000 + " s");
        n.falsify();
        startTim=System.nanoTime(); 
         String stext=searchfield.getValue().toString();
         searchfield.getItems().clear();
         searchfield.setValue(stext);
        if(items.size()==0)
        {
        suggest s=new suggest();
        String[] mmi=stext.split(" ");
        s.q=mmi[mmi.length-1];
        sf=s.sorter(sf);
       
        String sugg=mmi[0];
        for(int j=1;j<mmi.length-1;j++)
            sugg=sugg+" "+mmi[j];
        for(int i=0;i<sf.size();i++)
        {
            if(searchfield.getItems().size()<5)
            {
                Node newnode=(Node) n.d.dictionary.get(sf.get(i));
                if(newnode.getlnum().size()==0)
                    continue;
                if(mmi.length>1)
                     searchfield.getItems().add(sugg+" "+sf.get(i));
                else
                    searchfield.getItems().add(sf.get(i));
            }
            else
                break;
        }
        searchfield.show();
        }   
        endTim = System.nanoTime();
        System.out.println("Suggestion Time "+(endTim - startTim)/1000000000 + " s");
    }
    @FXML public void handleListClick(MouseEvent arg0) {
        int index=list.getSelectionModel().getSelectedIndex();
        String filename=n.all.docname.get(lines_to_print.get(index).doc-1) ;
        int page=lines_to_print.get(index).page;
    System.out.println("clicked on " +filename);
    if(filename.endsWith(".pdf")||filename.endsWith(".PDF"))
    {
            try { 
                
            String[] command = new String[3];
            command[0] = "cmd";
            command[1] = "/c";
            command[2] = "cd C:\\Program Files (x86)\\Adobe\\Reader 11.0\\Reader && AcroRd32.exe /A page="+page+" \""+filename+"\"";
            Process p = Runtime.getRuntime().exec(command);
            } 
            catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    else
    {
      if (Desktop.isDesktopSupported()) {
    try {
        File myFile = new File(filename);
        Desktop.getDesktop().open(myFile);
    } catch (IOException ex) {
        // no application registered for PDFs
    }
}  
    }
    }
    public network copynet()
    {
        return n;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        n=network.getInstance();
        t=new TreeMap();
        sf=new ArrayList<String>();
        list.setCursor(Cursor.HAND);
        
        items=FXCollections.observableArrayList (
    "Single", "Double", "Suite", "Family App");
        
       
        
    }    
    
}
