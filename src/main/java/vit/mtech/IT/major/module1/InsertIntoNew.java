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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SanjayV
 */
class Jena
{
    static public String ont="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
     public static  Session session;
     public static Cluster cluster;
     public static ArrayList result=new ArrayList();
     public static HashMap<String, String> map=new HashMap();
     public static HashMap<String, String> res=new HashMap<>();
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
          //session.execute("TRUNCATE SO_RDF;");
      }
      public void close()
      {
          cluster.shutdown();
         
      }
      public void insertStatements(Model m, Resource s, Property p, Resource o)
      {
         for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) 
         {
             count++;
             String col="col"+count;
             String clust=" ";
            Statement stmt = i.next();
            String sa=stmt.toString();
            //System.out.println(map.get("type"));
           
           System.out.println(sa);
            String sp[]=sa.split(",");
            
            String first=sp[0].substring(1, sp[0].length());
           // System.out.println(first);
            first=first.replace("'", "");
            first=" "+first;
            
           // first=first.replace(ont,"tst:");
            
            String last=sp[2].substring(0, sp[2].length()-1);
           //System.out.println(last);
            last=last.replace("'", "");
            
            //last=last.replace(ont,"tst:");
            String mid=" ";
            mid=sp[1];
            String msplit[]=mid.split("#");
            //System.out.println(first+"------->"+last);
            //if(first.equals(last))
            //{
                
                //System.out.println("True");
               // continue;
           // }
            //else
            //{
      
                for(int j=0;j<msplit.length;j++)
                {
                    if(j==1)
                    {
                        //System.out.println(msplit[j]);
                        if(msplit[j].contains("seeAlso"))
                        {
                           // continue;
                        }
                        else
                        {
                        if(map.get(msplit[j]).equals("false"))
                        {
                            //s
                            //clust=last;
                            //System.out.println("inserting"+first+" AND "+cluster+"-->"+msplit[j]);
                           //System.out.println(last);
                            
                            session.execute("INSERT INTO University (subject, " +msplit[j]+ ") VALUES ('" +first+ "'," + " '" +last+ "');");
                            map.put(msplit[j], "true");
                            continue;
                        }
                           else
                           {
                            String x="";
                            ResultSet rs=session.execute("SELECT * FROM University WHERE subject='"+first+"';");
                            for(Row row:rs)
                            {
                               x=row.getString(msplit[j])+"|"+last;
                                
                            }
                            
                            
                            session.execute("INSERT INTO University (subject, " +msplit[j]+ ") VALUES ('" +first+ "'," + " '" +x+ "');");
                            continue;
                            
                               
                           }
 
                        }
                    //}
                 }
                }
               }
            }
         }
            
        

public class InsertIntoNew extends Jena{
    
    public static void main(String args[]) throws MalformedURLException
    {
        Jena j=new Jena();
        j.connecti();
        j.init();
        File uni = new File("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
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
        StmtIterator stmts1 = model.listStatements();
        
       
        while(stmts.hasNext())
        {
            count=0;
            counts++;
            System.out.println(counts);
            String p[]=null;
            
            String pred=stmts.next().getPredicate().toString();
             /*if(!pred.contains("#"))
             {
                 URI uri = URI.create(pred);
                 String path = uri.getPath();
                //System.out.println(path.substring(path.lastIndexOf('/') + 1));
                result.add(path.substring(path.lastIndexOf('/') + 1));
             }*/
            
                p=pred.split("#");
                for(int i=0;i<p.length;i++)
                {
                if(i==1)
                {
                    result.add(p[i]);
                    break;
                }
                }
             
            
            for(int i=0;i<result.size();i++)
            {
                map.put(result.get(i).toString(), "false");
            }
            
            
        }
          
        for(Map.Entry<String, String> me:map.entrySet())
       {
           
           System.out.println(me.getKey()+"---->"+me.getValue());
       }
        while(stmts1.hasNext())
        {
            //System.out.println(stmts1.next().getSubject().toString());
        
        com.hp.hpl.jena.rdf.model.Resource A=infmodel.getResource(stmts1.next().getSubject().toString());
         com.hp.hpl.jena.rdf.model.Resource config = ModelFactory.createDefaultModel()
                      .createResource()
                      .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, "simple");
        Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config);
        // System.out.println("Average :");
        
         j.insertStatements(infmodel, A, null, null);
        
         //System.out.println(s);
         
        /*InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
         com.hp.hpl.jena.rdf.model.Resource a = inf.getResource(NS+"a");
            System.out.println("Statement: " + a.getProperty(q));*/
         //printStatements(infmodel);
        }
       
       
    j.close();
}
}

