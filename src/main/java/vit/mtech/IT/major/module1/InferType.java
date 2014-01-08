/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;

import com.datastax.driver.core.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

/**
 *
 * @author SanjayV
 */
public class InferType {

    static public Session session;
    static public Cluster cluster;
    static HashMap<String, String> hash = new HashMap<String, String>();
    static Set set = hash.entrySet();
    static Iterator i = set.iterator();

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

    public void close() {
        cluster.shutdown();
    }

    public void init() {
        session.execute("USE rdf;");
    }

    public static void main(String args[]) throws MalformedURLException, IOException, RDFParseException, RDFHandlerException {
        InferType t = new InferType();
        t.connecti();
        t.init();

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


        for (Statement sta : ite) {
            String pred = sta.getPredicate().toString();
            String sub = sta.getSubject().toString();
            String obj = sta.getObject().toString();
            if (obj.contains("http://www.w3.org/2002/07/owl#")) {
                continue;
            } else {
                String p[] = pred.split("#");
                String s[] = sub.split("#");
                String o[] = obj.split("#");

                if ("type".equals(p[1])) {
                    hash.put(s[1], o[1]);
                }
                for (Map.Entry<String, String> me : hash.entrySet()) {
                    String queryString = "SELECT * from TestRDF WHERE subject='AIR';";
                    ResultSet result = session.execute(queryString);
                    ColumnDefinitions c = result.getColumnDefinitions();
                    int size = c.size();
                    String col = "col";
                    int num = 0;

                    for (Row row : result) {
                        for (int i = 0; i < size; i++) {
                            if (row.isNull(i)) {
                                num = i;
                                //session.execute("INSERT INTO TestRDF (subject, " + col + num + ") VALUES ('" + me.getKey().toString() + "'," + " '" + me.getValue().toString() + "');");
                                break;

                            } else {
                                continue;
                            }
                        }
                        break;
                    }

                }



            }
        }

        for (Map.Entry<String, String> me : hash.entrySet()) {
            System.out.println(me.getKey() + " " + me.getValue());

        }
        t.close();
    }
}
