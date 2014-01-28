/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;
import com.datastax.driver.core.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.cassandra.thrift.AuthenticationException;
import org.apache.cassandra.thrift.AuthorizationException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

/**
 *
 * @author SanjayV
 */

class setup{
    public static ArrayList al=new ArrayList();
    public static ArrayList result=new ArrayList();
    public static HashMap<String, Integer> imp=new HashMap();
    public static Session session;
    public static Cluster cluster;
    
    
    
    static public String ont="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    
     public void Parse() throws TTransportException, 
            UnsupportedEncodingException, InvalidRequestException, NotFoundException,
            UnavailableException, TimedOutException, TException, AuthenticationException,
            AuthorizationException, MalformedURLException, IOException, RDFParseException, RDFHandlerException 
    {
        
        File uni = new File("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
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
        for(Statement sta:ite)
        {
            String p[] = null;
            String pred=sta.getPredicate().toString();
            System.out.println(pred);
            if(!pred.contains("#"))
            {
                 URI uri = URI.create(pred);
                 String path = uri.getPath();
                //System.out.println(path.substring(path.lastIndexOf('/') + 1));
               // al.add(path.substring(path.lastIndexOf('/') + 1));
            }
            else
            {
             p=pred.split("#");
            
           
            for(int i=0;i<p.length;i++)
            {
                if(i==1)
                {
                    al.add(p[i]);
                    break;
                }
            }
            }
            
        }
        HashSet set = new HashSet(al);
        Iterator iterator = set.iterator(); 
        while ( iterator.hasNext() )
        {
            result.add(iterator.next().toString());
            //imp.put(iterator.next().toString(), 0);
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
    public void init()
    {
         session.execute("USE rdf;");
         session.execute("CREATE TABLE University( subject text, PRIMARY KEY(subject));");
    }
    public void schema()
    {
       
         
        
        for(int i=0;i<result.size();i++)
        {
         System.out.println("Adding Column: "+ result.get(i) + " " );
         session.execute("ALTER TABLE University ADD "+result.get(i)+" text;");
        }
        
    }
    public void createIndex()
    {
        
        for(int i=0;i<result.size();i++)
        {
        System.out.println("Creating Index on: "+ result.get(i) + " " );
        session.execute("CREATE INDEX ON University ("+result.get(i)+");");
        }
        
    }
    public void close()
    {
          cluster.shutdown();
         
    }
   
    
 
 
 }
public class NewSchemaV1 {
    
     public static void main(String args[])
    {
        setup i=new setup();
        i.connecti();
        i.init();
        try
        {
              i.Parse();
        }
        catch(Exception e)
        {
            System.err.print(e);
        }
        i.schema();
        i.createIndex();
        i.close();
      
    }
    
}
