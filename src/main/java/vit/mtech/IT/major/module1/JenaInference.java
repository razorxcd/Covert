/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;

/**
 *
 * @author SanjayV
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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
import com.hp.hpl.jena.util.FileManager;

import com.datastax.driver.core.*;


import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
//import org.openrdf.model.Resource;

/**
 *
 * @author SanjayV
 */

public class JenaInference {
    
     public static  Session session;
     public static Cluster cluster;
     static int count;
     
      public void connecti() {
    cluster=Cluster.builder().addContactPoint("localhost").build();
    Metadata metadata = cluster.getMetadata();
    System.out.println("Cassandra connection established");
    System.out.printf("Connected to cluster: %s\n",
            metadata.getClusterName());
    for (Host host : metadata.getAllHosts()) {
        System.out.printf("Datatacenter: %s; Host: %s; Rack: %s \n",
                host.getDatacenter(), host.getAddress(), host.getRack());
        session = cluster.connect();

    }
      }
      public void init()
      {
          session.execute("USE rdf;");
      }
      public void close()
      {
          cluster.shutdown();
         
      }
    
    
    public static void main(String args[]) throws MalformedURLException
    {
        JenaInference j=new JenaInference();
        j.connecti();
        j.init();
        /*String NS = "urn:x-hp-jena:eg/";
        com.hp.hpl.jena.rdf.model.Model rdfsExample = ModelFactory.createDefaultModel();
        Property p = rdfsExample.createProperty(NS, "p");
        Property q = rdfsExample.createProperty(NS, "q");
        rdfsExample.add(p, RDFS.subPropertyOf, q);
        rdfsExample.createResource(NS+"a").addProperty(p, "foo");*/
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\finalrdf.rdf");
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        
 // use the FileManager to find the input file
        InputStream in = FileManager.get().open(documentUrl.toString());
        model.read(in, null);

        InfModel infmodel = ModelFactory.createRDFSModel(model);
        StmtIterator stmts = model.listStatements();
       
        while(stmts.hasNext())
        {
            count=0;
            System.out.println(stmts.next().getSubject().toString());
             
        com.hp.hpl.jena.rdf.model.Resource A=infmodel.getResource(stmts.next().getSubject().toString());
         com.hp.hpl.jena.rdf.model.Resource config = ModelFactory.createDefaultModel()
                      .createResource()
                      .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, "simple");
        Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);
        // System.out.println("Average :");
         insertStatements(infmodel, A, null, null);
        
         //System.out.println(s);
         
        /*InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
         com.hp.hpl.jena.rdf.model.Resource a = inf.getResource(NS+"a");
            System.out.println("Statement: " + a.getProperty(q));*/
         //printStatements(infmodel);
        }
        j.close();
    }
    
    private static void printStatements(Model m) {
       
    }

    private static void insertStatements(Model m, Resource s, Property p, Resource o) {
         //To change body of generated methods, choose Tools | Templates.
       
         for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) 
         {
             count++;
             String col="col"+count;
            Statement stmt = i.next();
            String sa=stmt.toString();
           
            //System.out.println(sa);
            String sp[]=sa.split(" ");
            
            String st=sp[0].substring(1, sp[0].length()-1);
            String st1=sp[2].substring(1, sp[2].length()-1);
            System.out.println(" Inserting "+st+" AND "+st1+" INTO "+col);
            //System.out.println(st+" ---- "+st1);
            //System.out.println(" - " + PrintUtil.print(stmt));
            session.execute("INSERT INTO TestRDF (subject, " + col + ") VALUES ('" + st + "'," + " '" + st1 + "');");
           
        }
    }
    
}
