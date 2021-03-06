/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

/**
 *
 * @author SanjayV
 */
import org.apache.cassandra.thrift.AuthenticationException;
   import org.apache.cassandra.thrift.AuthorizationException;
   import org.apache.cassandra.thrift.Cassandra;
   import org.apache.cassandra.thrift.Column;
   import org.apache.cassandra.thrift.ColumnParent;
   import org.apache.cassandra.thrift.ConsistencyLevel;
   import org.apache.cassandra.thrift.InvalidRequestException;
   import org.apache.cassandra.thrift.NotFoundException;
   import org.apache.cassandra.thrift.TimedOutException;
   import org.apache.cassandra.thrift.UnavailableException;
   import org.apache.thrift.transport.TTransport;
   import org.apache.thrift.transport.TFramedTransport;
   import org.apache.thrift.transport.TSocket;
   import org.apache.thrift.protocol.TProtocol;
   import org.apache.thrift.protocol.TBinaryProtocol;
   import org.apache.thrift.TException;
   import org.apache.thrift.transport.TTransportException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collection;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
public class objecttest {
    public static void main(String[] args) throws TTransportException, 
           UnsupportedEncodingException, InvalidRequestException, NotFoundException,
           UnavailableException, TimedOutException, TException, AuthenticationException,
           AuthorizationException, MalformedURLException, IOException, RDFParseException, RDFHandlerException 
    {
         String buffer;
        TTransport tt=new TFramedTransport(new TSocket("localhost",9160));
        TProtocol pr = new TBinaryProtocol(tt);
        Cassandra.Client client=new Cassandra.Client(pr);
        tt.open();
        String keyspace = "rdf";
        client.set_keyspace(keyspace);
                //record id
        //String key_user_id = "1";
        String columnFamily = "foaf";
        long timestamp= System.currentTimeMillis();
        //Random r=new Random(timestamp);
        
        //Setting the RDF parser to work
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\finalrdf.rdf");
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

        rdfParser.parse(inputStream,"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#");
        
         ColumnParent parent=new ColumnParent(columnFamily);
         Collection<Statement> it=collector.getStatements();
         
        for(Statement st:it)
        {
       String s= st.getObject().toString();
       if(s.contains("http://www.w3.org/2002/07/owl#"))
       {
           continue;
       }
       else
       {
       System.out.println(s);
       }
            
         }
                
         
    }
}
