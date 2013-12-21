/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT;
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


/**
 *
 * @author SanjayV
 */
public class RDFWrite {
    public static void main(String[] args) throws TTransportException, 
            UnsupportedEncodingException, InvalidRequestException, NotFoundException,
            UnavailableException, TimedOutException, TException, AuthenticationException,
            AuthorizationException, MalformedURLException, IOException, RDFParseException, RDFHandlerException 
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
        FileOutputStream out = new FileOutputStream("C:\\Users\\SanjayV\\SkyDrive\\Documents\\finalrdf.rdf");
        RDFWriter writer = Rio.createWriter(RDFFormat.RDFXML, out);

        writer.startRDF();
       for (Statement st: model) {
            writer.handleStatement(st);
         }
        writer.endRDF();

    
}
}