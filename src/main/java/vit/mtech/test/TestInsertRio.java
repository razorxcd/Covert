/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import java.net.URI;

/**
 *
 * @author SanjayV
 */
public class TestInsertRio extends setup {

    public static Session sess;
    public static Cluster clust;
    static int count;
    static int flag = 0;
    
    //static String s = "";

    @Override
    public void connecti() {
        clust = Cluster.builder().addContactPoint("localhost").build();
        Metadata metadata = clust.getMetadata();
        System.out.println("Cassandra connection established");
        System.out.printf("Connected to clust: %s\n",
                metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s \n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
            sess = clust.connect();

        }
    }

    @Override
    public void init() {
        sess.execute("USE rdf;");
        //sess.execute("tracing on;");
    }

    public void insert(File uni, String db) throws MalformedURLException, IOException, RDFParseException, RDFHandlerException {
        //File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\benchmark1M.rdf");
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
            //count = 0;
            flag++;
            String p[]=new String[2];
            String sub =sta.getSubject().toString();
            String pred=sta.getPredicate().toString();
            String obj=sta.getObject().toString();
            obj=obj.replace("\"","");
            obj=obj.replace("'","");
            System.out.println(flag);
            //System.out.println(sub+" "+pred+" "+obj);
            if (pred.contains("#")) {
                 p = pred.split("#");
            } else {
                if(pred.startsWith(" "))
                {
                    pred=pred.replace(" ", "");
                }
                URI uri = URI.create(pred);
                String path = uri.getPath();
                //System.out.println(path.substring(path.lastIndexOf('/') + 1));
                p[1] = path.substring(path.lastIndexOf('/') + 1);
            }
            
                 for (int y = 0; y < p.length; y++) {
                if (y == 1) {
                    //System.out.println(p[j]);
                    if (p[y].contains("seeAlso")) {
                        // continue;
                    } else {
                        //System.out.println(p[y]);
                        if(p[y].equals("domain")||p[y].equals("range")||p[y].equals("subPropertyOf"))
                        {
                            continue;
                        }
                         if(map.get(p[y])==null)
                        {
                        
                            map.put(p[y], "false");
                            ResultSet rs=session.execute("SELECT * FROM "+db+";");
                            ColumnDefinitions cf=rs.getColumnDefinitions();
                            if(cf.contains(p[y]))
                            {
                            }
                            else
                            {
                            System.out.println("Adding in TestInsert: Raised Null Pointer"+p[y]);
                            session.execute("ALTER TABLE "+db+" ADD "+p[y]+" text;");
                            }
                            
                        }
                        if (map.get(p[y]).equals("false")) {
                            //s
                            //clust=last;
                            //System.out.println("inserting"+first+" AND "+clust+"-->"+p[j]);
                            //System.out.println(last);

                             sess.execute("INSERT INTO "+db+" (subject, " +p[y]+ ") VALUES ('" +sub+ "'," + " '" +obj+ "');");
                            map.put(p[y], "true");
                            continue;
                        } else {
                            String x = "";
                            ResultSet rs = sess.execute("SELECT * FROM "+db+" WHERE subject='" + sub + "';");
                            for (Row row : rs) {
                                x = row.getString(p[y]) + "|" + obj;

                            }


                            sess.execute("INSERT INTO "+db+" (subject, " +p[y]+ ") VALUES ('" +sub+ "'," + " '" +x+ "');");
                            continue;


                        }

                    }
                    //}
                }
            }

            //p.insert(s, ite);
            //System.out.println(sta.getSubject()+" AND "+sta.getObject());

        }
    }
//public static void main(String[] args) throws MalformedURLException, IOException, RDFParseException, RDFHandlerException {
//        
//        TestInsertRio rio=new TestInsertRio();
//        rio.connecti();
//        rio.init();
//        File uni = new File("F:\\Cassandra\\LUBM\\src\\MassiveUni.rdf");
//        String db="LUBM1";
//        rio.insert(uni, db);
//        rio.close();
        
    }    


