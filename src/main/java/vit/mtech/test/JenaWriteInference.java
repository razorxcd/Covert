/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.util.FileManager;

import com.datastax.driver.core.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SanjayV
 */

            
        

 public class JenaWriteInference {
    
    public static void main(String args[]) throws MalformedURLException, FileNotFoundException
    {
       
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\benchmark250k.rdf");
         FileOutputStream out = new FileOutputStream("C:\\Users\\SanjayV\\SkyDrive\\Documents\\Catalog.rdf");
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        
        // create an empty model
        //Model model = ModelFactory.createDefaultModel();
        Model model=ModelFactory.createDefaultModel();
        
        
 // use the FileManager to find the input file
        InputStream in = FileManager.get().open(documentUrl.toString());
        model.read(in, null);

        InfModel infmodel = ModelFactory.createRDFSModel(model);
        
        StmtIterator stmts = model.listStatements();
        StmtIterator stmts1 = infmodel.listStatements();
        while(stmts1.hasNext())
            
        {
            System.out.println(stmts1.next().toString());
        }
        
        //infmodel.write(out, "RDF/XML");
        
       
       
       
       
    
}
}

