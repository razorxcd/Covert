/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;

/**
 *
 * @author SanjayV
 */
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
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
import static vit.mtech.IT.major.module1.WORK1.session;

/**
 *
 * @author SanjayV
 */
class WORK1 {

    String flag;
    static public String ont = "http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    static public String pred = "http://www.w3.org/2000/01/rdf-schema#subClassOf";
    static public Session session;
    static public Cluster cluster;
    static HashMap<String, String> hash = new HashMap<>();
    static HashMap<String, String> result = new HashMap<>();
    static HashMap<String, String> type1 = new HashMap<>();
    static Set set = hash.entrySet();
    static Set set1=result.entrySet();
    static Set set2=type1.entrySet();
    static Iterator j=set1.iterator();
    static Iterator k=set2.iterator();
    static Iterator i = set.iterator();
    static ArrayList<String> al = new ArrayList<>();
    static ArrayList<String> bl = new ArrayList<>();
   
    

    public void DO() throws MalformedURLException, IOException, RDFParseException, RDFHandlerException {



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
        Collection<Statement> ite = collector.getStatements();

        int count=0;
        for (Statement sta : ite) {
            //System.out.println(sta.getSubject()+" "+sta.getPredicate()+" "+sta.getObject());
            String pred = sta.getPredicate().toString();
            String sub = sta.getSubject().toString();
            String obj = sta.getObject().toString();
            count++;

            String p[] = pred.split("#");
            String s[] = sub.split("#");
           String o[] = obj.split("#");
           
          
            for(int i=0;i<s.length;i++)
            {
                if(i==1)
                {
                    if("type".equals(p[i]))
                    {
                        if(o[i].equals("Class"))
                        {
                            continue;
                        }
                    type1.put(s[i], o[i]);
                    }
                }
                else
                {
                    continue;
                }
                //System.out.println(s[i]);
            }
            
              
             
          
            if ("subClassOf".equals(p[1]))
            {
                //System.out.println("before"+s[1]);
               hash.put(s[1], o[1]);
            }
            
       
                         
             
            
        }
         
         for (Map.Entry<String, String> me1 : type1.entrySet())
            {
                System.out.println(me1.getKey()+" : "+me1.getValue());
            }
        System.out.println("Total statements in the ontology: "+ count);
    }
   

    public void connecti() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
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
    public void inferAndInsert()
    {
        for (Map.Entry<String, String> me : hash.entrySet()) {
            bl.add(me.getKey().toString());

        }
        for (int i = 0; i < bl.size(); i++) {
            //System.out.println(al);
            // System.out.println("Enter the key");
            // Scanner s=new Scanner(System.in);


            String key = bl.get(i);
            String key1 = key;
            int count = 0;
            while (bl.contains(key)) {

                count++;
                String col = "col" + count;
                String val = hash.get(key).toString();
                //String c=Integer.toString(count);
                //System.out.println(key1 + ":" + val);
               // System.out.println(" Inserting " + val + "into" + col + "");
                result.put(key1, val);
                //session.execute("INSERT INTO TestRDF (subject, " + col + ") VALUES ('" + key1 + "'," + " '" + val + "');");

                /* try{ 
                 //session.execute("ALTER TABLE TestRDF ADD "+col+" varchar;");
                 session.execute("INSERT INTO TestRDF (subject, "+col+") VALUES ('"+key1+"',"+" '"+val+"');");
                 }
                 catch(InvalidQueryException e)
                 {
                 System.out.println("Working");
                 session.execute("ALTER TABLE TestRDF ADD "+col+" varchar;");
                 session.execute("INSERT INTO TestRDF (subject, "+col+") VALUES ('"+key1+"',"+" '"+val+"');");
                 }
       
                 */

                key = val;


            }
        }
    }
    public void inferType()
    {
        for (Map.Entry<String, String> me : result.entrySet()) 
        {
            
            for (Map.Entry<String, String> me1 : type1.entrySet())
            {
                 
                 
                
            }
        }
            
    }

    public void close() {
        cluster.shutdown();
    }
}

public class InferSubClassOfV1 extends WORK1 {

    public static void main(String args[]) throws MalformedURLException, IOException, RDFParseException, RDFHandlerException {
        WORK1 w = new WORK1();
        w.connecti();
        session.execute("USE rdf;");
        w.DO();
        
        w.inferAndInsert();
        
        //w.inferType();



        
        w.close();

    }
}
