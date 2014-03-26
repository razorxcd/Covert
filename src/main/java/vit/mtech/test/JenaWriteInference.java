/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;
import com.hp.hpl.jena.rdf.model.InfModel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.Statement;
import java.io.IOException;


/**
 *
 * @author SanjayV
 */

            
        

 public class JenaWriteInference {
    
    public static void main(String args[]) throws MalformedURLException, FileNotFoundException, IOException
    {
         Model fin = ModelFactory.createDefaultModel();
       
        ArrayList<Statement> container=new ArrayList<>();
        FileOutputStream out = new FileOutputStream("F:\\Cassandra\\LUBM\\src\\TinyUniWI.rdf");
        for(int i=0;i<1;i++)
        {
            int j=0;
        while(j<15)
        {
         String univ="University"+i+"_"+j+".owl";
            System.out.println(univ);
            
       
        File uni = new File("F:\\Cassandra\\LUBM\\src\\"+univ);
        
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        
        // create an empty model
        //Model model = ModelFactory.createDefaultModel();
        Model model=ModelFactory.createDefaultModel();
        
        
 // use the FileManager to find the input file
        InputStream in = FileManager.get().open(documentUrl.toString());
        model.read(in, null);

       // InfModel infmodel = ModelFactory.createRDFSModel(model);
        
        
        fin.add(model);
        //StmtIterator stmts = model.listStatements();
       //StmtIterator stmts1 = infmodel.listStatements();
//        while(stmts1.hasNext())
//            
//        {
//            container.add((Statement) stmts1.next());
//            //System.out.println(stmts1.next().toString());
//            
//            
//        }
        
        //infmodel.write(out, "RDF/XML");
        j++;
        }
        }
       
        //fin.add((com.hp.hpl.jena.rdf.model.Statement) container);
        
        fin.write(out, "RDF/XML");
        
        out.close();
    }
        
       
       
       
       
    
}


