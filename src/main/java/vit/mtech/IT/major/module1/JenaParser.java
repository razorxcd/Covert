/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;
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

/**
 *
 * @author SanjayV
 */
public class JenaParser {
    
     public static  Session session;
     public static Cluster cluster;
     static int count;
     static int counts=0;
     
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
          session.execute("TRUNCATE TestRDF;");
      }
      public void close()
      {
          cluster.shutdown();
         
      }
      public static void main(String args[]) throws MalformedURLException
    {
        JenaParser j=new JenaParser();
        j.connecti();
        j.init();
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\benchmark1M.rdf");
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        
        // create an empty model
        //Model model = ModelFactory.createDefaultModel();
        Model model=ModelFactory.createDefaultModel();
        
 // use the FileManager to find the input file
        InputStream in = FileManager.get().open(documentUrl.toString());
        model.read(in, null);
        StmtIterator st = model.listStatements();
        while(st.hasNext())
        {
            counts++;
            System.out.println(counts);
            count=0;
            
            String s=st.next().getSubject().toString();
           // System.out.println(s);
            while(st.hasNext())
            {
                if(s.equals(st.next().getSubject().toString()))
                {
                    count++;
                    String col="col"+count;
                  
                    session.execute("INSERT INTO SO_RDF (subject, " + col + ") VALUES ('" +st.next().getSubject().toString()+ "'," + " '" + st.next().getObject().toString() + "');");
                    break;
                }
            }
        }
        j.close();
    }
    
}
