/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;
import java.io.*;
import java.util.*;
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

import com.datastax.driver.core.*;



/**
 *
 * @author SanjayV
 */
class Mapper
{
    ArrayList<String> al=new ArrayList<String>();
    ArrayList<String> find=new ArrayList<String>();
    ArrayList<String> param=new ArrayList<String>();
    String Ontology="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    String pred="http://www.w3.org/2000/01/rdf-schema#";
    Cluster cluster;
    Session session;
          
    public void map()
    {
          String query="SELECT ?subject ?object WHERE { ?Subject rdfs:subClassOf ?Object }";
          String q[]=query.split(" ");
          int i=0;
          
          while(!q[i].equals("WHERE"))
          {
              al.add(q[i]);
              i++;
          }
          
          for(int j=0;j<al.size();j++)
              
          {
              if(al.get(j).equals("SELECT"))
              {
                  continue;
              }
              else
              {
                  find.add(al.get(j).substring(1));
              }
          }
          for(int j=0;j<q.length;j++)
          {
              if(q[j].contains("rdfs:"))
              {
                  String x[]=q[j].split(":");
                  find.add(x[1]);
                  break;
              }
              else
              {
                  continue;
              }
          }
        //System.out.println(find);
         
    }
    public void connect()
    {
        
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
    
     public void cqlquery()
     {
        // param.add(null);
         String queryString="SELECT"+" "+find.get(0)+","+" "+find.get(1)+" "+"FROM rdf_spo"+" "+"WHERE predicate='"+pred+find.get(2)+"';";
         session.execute("USE rdf;");
         ResultSet result=session.execute(queryString);
       // System.out.println(String.format("subject", "object",
      // "-------------------------------+-----------------------+--------------------"));
for (Row row : result) {
    //System.out.println(String.format("%-30s\t%-20s\t%-20s", row.getString("subject"),
   // row.getString("object")));
    String x=row.getString("subject");
    String y=row.getString("object");
    System.out.println(x+"   "+y);
}
System.out.println();
     }
     public void close() {
    cluster.shutdown();
}
      
    
    
    
}
public class SPARQueryMapper {
    
    public static void main(String args[])
    {
        Mapper m=new Mapper();
        m.map();
        m.connect();
        m.cqlquery();
        m.close();
        
    }
    
}
