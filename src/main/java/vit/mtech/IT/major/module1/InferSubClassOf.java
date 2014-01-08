/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;

/**
 *
 * @author SanjayV
 */


import java.util.Scanner;
import com.datastax.driver.core.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
import static vit.mtech.IT.major.module1.WORK.al;
import static vit.mtech.IT.major.module1.WORK.hash;
import static vit.mtech.IT.major.module1.WORK.session;

/**
 *
 * @author SanjayV
 */
 class WORK {
    
        String flag;
        static public String ont="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
        static public String pred="http://www.w3.org/2000/01/rdf-schema#subClassOf";
        
        static public Session session;
        static public Cluster cluster;
        static HashMap<String,String> hash=new HashMap<String,String>();
        static Set set=hash.entrySet();
        static Iterator i=set.iterator();
       static ArrayList<String> al=new ArrayList<String>();
        static ArrayList<String> bl=new ArrayList<String>();
        
        public void DO() throws MalformedURLException, IOException, RDFParseException, RDFHandlerException
        {
            
            
    
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\final1.owl");
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        InputStream inputStream = documentUrl.openStream();
        //RDFParser rdfParser = Rio.createParser(RDFFormat.);
        RDFFormat format = Rio.getParserFormatForFileName(documentUrl.toString());
        //RDFFormat format = Rio.getParserFormatForMIMEType(contentType);
        RDFParser rdfParser = Rio.createParser(format);
        Model model = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(model);
        rdfParser.setRDFHandler(collector);

        rdfParser.parse(inputStream, documentUrl.toString());
        Collection<Statement> ite=collector.getStatements();
        
        
            for (Statement sta : ite){
                String pred=sta.getPredicate().toString();
                String sub=sta.getSubject().toString();
                String obj=sta.getObject().toString();
                
                
                
                //splitting
                String p[]=pred.split("#");
                String s[]=sub.split("#");
                String o[]=obj.split("#");
                
                if("subClassOf".equals(p[1])){
                    hash.put(s[1],o[1]);
                    
                    
                    
                }
            }
        }

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
        public void close()
        {
            cluster.shutdown();
        }

    
 }
public class InferSubClassOf extends WORK {
    public static void main(String args[]) throws MalformedURLException, IOException, RDFParseException, RDFHandlerException{
        WORK w=new WORK();
        
        
        w.connecti();
        session.execute("USE rdf;");
        w.DO();
        
        for(Map.Entry<String, String> me:hash.entrySet())
       {
           bl.add(me.getKey().toString());
           
       }
       for(int i=0;i<bl.size();i++)
    {
         //System.out.println(al);
      // System.out.println("Enter the key");
       // Scanner s=new Scanner(System.in);
        //int count=0;
           
       String key=bl.get(i);
       String key1=key;       
       while(bl.contains(key))
       {
       String val=hash.get(key).toString();
       //String c=Integer.toString(count);
       System.out.println(key1+":"+val);
      // session.execute(" INSERT INTO RDF_SPO (subject, object, predicate) VALUES ("+"'"+ont+key1+"'"+","+" "+"'"+ont+val+"'"+","+" "+"'"+pred+"');");
      // session.execute(" INSERT INTO RDF_OSP (subject, object, predicate) VALUES ("+"'"+ont+key1+"'"+","+" "+"'"+ont+val+"'"+","+" "+"'"+pred+"');");
       //session.execute(" INSERT INTO RDF_POS (subject, object, predicate) VALUES ("+"'"+ont+key1+"'"+","+" "+"'"+ont+val+"'"+","+" "+"'"+pred+"');");
       key=val;   
      // count++;
    
       }
    }
      w.close();
       
    }
}
    
    

