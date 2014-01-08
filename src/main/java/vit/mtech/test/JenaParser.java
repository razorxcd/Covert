/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

/**
 *
 * @author SanjayV
 */
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
public class JenaParser {
    
    public static void main(String args[]) throws MalformedURLException, IOException
    
    {
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\finalrdf.rdf");
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        
        // create an empty model
 Model model = ModelFactory.createDefaultModel();

 // use the FileManager to find the input file
 InputStream in = FileManager.get().open(documentUrl.toString());
if (in == null) {
    throw new IllegalArgumentException(
                                 "File: " + documentUrl.toString() + " not found");
}

// read the RDF/XML file
model.read(in, null);

// write it to standard out
//model.write(System.out);
        //InputStream inputStream = documentUrl.openStream();
       //model.read(inputStream, documentUrl.toString());

        StmtIterator stmts = model.listStatements();
        while(stmts.hasNext())
        {
            System.out.println(stmts.next().getSubject().toString()+"--- "+stmts.next().getPredicate().toString()+"--- "+stmts.next().getObject().toString());
        }
        
    }
}

    