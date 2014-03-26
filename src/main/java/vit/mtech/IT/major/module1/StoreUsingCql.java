/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.it.major.module1;
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
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import static vit.mtech.it.major.module1.stax.session;
/**
 *
 * @author SanjayV
 */
class stax
{

   static public Session session;
    static public Cluster cluster;
    public void connecti() {
    cluster=Cluster.builder().addContactPoint("phoenix-sumeru.in").build();
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
    public void createSchema() {
  
    session.execute("CREATE TABLE RDF_SPO (\n" +
"    subject text,\n" +
"    predicate text,\n" +
"   object text,\n" +
"PRIMARY KEY(subject, object));");
    session.execute("CREATE index on RDF_SPO(predicate);");
    }
    public void close()
    {
        cluster.shutdown();
    }
    
    
}
public class StoreUsingCql extends stax {
    public static void main(String args[])throws 
           UnsupportedEncodingException, MalformedURLException, IOException, RDFParseException, RDFHandlerException 
    {
        stax st=new stax();
        st.connecti();
          session.execute("USE rdf;");
      //  st.createSchema();
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\final1.owl");
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
        Collection<Statement> it=collector.getStatements();
         for (Statement sta : it) {
               String obj=sta.getObject().toString();
                String sub=sta.getSubject().toString();
                String pred=sta.getPredicate().toString();
               if(obj.contains("http://www.w3.org/2002/07/owl#"))
               {
                   continue;
               }
               else
               {
                session.execute("INSERT INTO rdfstore (subject, predicate, object) VALUES"+" "+"("+"'"+sub+"'"+","+" "+"'"+pred+"'"+","+" "+"'"+obj+"'"+");");
               }
         }
            /*for (Statement sta : it) {
                String sub=sta.getSubject().toString();
                String pred=sta.getPredicate().toString();
                session.execute("INSERT INTO rdfstore (subject, predicate) VALUES"+" "+"("+"'"+sub+"'"+","+" "+"'"+pred+"'"+");");
                //session.execute("INSERT INTO rdfstore (subject, predicate) VALUES ('Hello', 'HI');");
                
                
            }*/
           
            
            st.close();
    }
    
}
