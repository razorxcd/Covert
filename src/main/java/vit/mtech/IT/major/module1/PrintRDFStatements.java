/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collection;
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
import com.datastax.driver.core.*;

/**
 *
 * @author SanjayV
 */
public class PrintRDFStatements {
    
    public static  Session session;
     public static Cluster cluster;
     static int count;
     static int flag=0;
     static String s="";

   

    public void insert(String s, Collection<Statement> ite) {
       // System.out.println(s);
        
        for(Statement sta:ite)
        {
            
            while(s.equals(sta.getSubject().toString()))
            {
            count++;
            
                String col="col"+count;
                //System.out.println(" Inserting with key= "+sta.getSubject().toString()+" AND "+sta.getObject().toString()+" INTO " +col);
                //System.out.println("Subject:"+sta.getSubject().toString()+" "+"Predicate:"+sta.getPredicate().toString()+" "+"Object:"+sta.getObject().toString());
               
                session.execute("INSERT INTO SO_RDF (subject, " + col + ") VALUES ('" + sta.getSubject().toString() + "'," + " '" + sta.getObject().toString() + "');");
                
                break;
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
     public void init()
      {
          session.execute("USE rdf;");
      }
      public void close()
      {
          cluster.shutdown();
         
      }
    
    
    
     public static void main(String[] args) throws TTransportException, 
            UnsupportedEncodingException, InvalidRequestException, NotFoundException,
            UnavailableException, TimedOutException, TException, AuthenticationException,
            AuthorizationException, MalformedURLException, IOException, RDFParseException, RDFHandlerException 
    {
        PrintRDFStatements p= new  PrintRDFStatements();
        p.connecti();
        p.init();
    File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\benchmark1M.rdf");
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
            count=0;
            flag++;
            s=sta.getSubject().toString();
             System.out.println(flag);
            
            p.insert(s,ite);
            //System.out.println(sta.getSubject()+" AND "+sta.getObject());
            
        }
        
            
    
       p.close();
    
}
}
 