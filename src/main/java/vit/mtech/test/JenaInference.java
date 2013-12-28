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
        com.hp.hpl.jena.rdf.model.Resource A=infmodel.getResource("http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#BirdTypeA");
         com.hp.hpl.jena.rdf.model.Resource config = ModelFactory.createDefaultModel()
                      .createResource()
                      .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, "simple");
        Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);
         System.out.println("BirdTypeA :");
         printStatements(infmodel, A, null, null);
        /*InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
         com.hp.hpl.jena.rdf.model.Resource a = inf.getResource(NS+"a");
            System.out.println("Statement: " + a.getProperty(q));*/
                
    }

    private static void printStatements(Model m, Resource s, Property p, Resource o) {
         //To change body of generated methods, choose Tools | Templates.
       
         for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) 
         {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }
    
}
