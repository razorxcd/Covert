/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
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

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.InvalidQueryException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
//import org.openrdf.model.Resource;

/**
 *
 * @author SanjayV
 */

public class JenaInference {
    
    public static void main(String args[])
    {
        
        /*String NS = "urn:x-hp-jena:eg/";
        com.hp.hpl.jena.rdf.model.Model rdfsExample = ModelFactory.createDefaultModel();
        Property p = rdfsExample.createProperty(NS, "p");
        Property q = rdfsExample.createProperty(NS, "q");
        rdfsExample.add(p, RDFS.subPropertyOf, q);
        rdfsExample.createResource(NS+"a").addProperty(p, "foo");*/
        Model schema = FileManager.get().loadModel("C:\\Users\\SanjayV\\SkyDrive\\Documents\\final1.owl");
        Model data = FileManager.get().loadModel("C:\\Users\\SanjayV\\SkyDrive\\Documents\\finalrdf.rdf");
        InfModel infmodel = ModelFactory.createRDFSModel(schema, data);
        com.hp.hpl.jena.rdf.model.Resource A=infmodel.getResource("http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Sea");
         com.hp.hpl.jena.rdf.model.Resource config = ModelFactory.createDefaultModel()
                      .createResource()
                      .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, "simple");
        Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);
         System.out.println("Average :");
         printStatements(infmodel, A, null, null);
        
         //System.out.println(s);
         
        /*InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
         com.hp.hpl.jena.rdf.model.Resource a = inf.getResource(NS+"a");
            System.out.println("Statement: " + a.getProperty(q));*/
         //printStatements(infmodel);
    }
    
    private static void printStatements(Model m) {
        for(StmtIterator i=m.listStatements(); i.hasNext(); ) {
            Statement stmt=i.nextStatement();
            System.out.println(PrintUtil.print(stmt));
        }
    }

    private static void printStatements(Model m, Resource s, Property p, Resource o) {
         //To change body of generated methods, choose Tools | Templates.
       
         for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) 
         {
            Statement stmt = i.nextStatement();
            String sa=stmt.toString();
           
            System.out.println(sa);
            String sp[]=sa.split(" ");
            
            String st=sp[0].substring(1, sp[0].length()-1);
            String st1=sp[2].substring(0, sp[2].length()-1);
           // System.out.println(st+" : "+st1);
            //System.out.println(" - " + PrintUtil.print(stmt));
           
        }
    }
    
}
