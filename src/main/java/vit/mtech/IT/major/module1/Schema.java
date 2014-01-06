/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 *
 * @author SanjayV
 */
public class Schema {
    private Session session;

private Cluster cluster;

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
public void createSchema() {
    session.execute("USE rdf;");

    /*session.execute("CREATE TABLE simplex.songs (" + "id uuid PRIMARY KEY,"
            + "title text," + "album text," + "artist text,"
            + "tags set<text>," + "data blob" + ");");*/
    session.execute("CREATE TABLE RDF_SPO (" + "subject varchar,"
            + "predicate varchar," + "object varchar, "+ "PRIMARY KEY (subject, object)"
            + ")");
    session.execute("CREATE index on RDF_SPO(predicate);");

   // session.execute("CREATE TABLE Sesame (subject text PRIMARY KEY);");
            
}
public void createSchema1() {
    session.execute("USE rdf;");

    /*session.execute("CREATE TABLE simplex.songs (" + "id uuid PRIMARY KEY,"
            + "title text," + "album text," + "artist text,"
            + "tags set<text>," + "data blob" + ");");*/
    session.execute("CREATE TABLE RDF_OSP (" + "subject varchar,"
            + "predicate varchar," + "object varchar, "+ "PRIMARY KEY (object)"
            + ")");
    //session.execute("CREATE index on RDF_OSP(predicate);");

   // session.execute("CREATE TABLE Sesame (subject text PRIMARY KEY);");
            
}
public void createSchema2() {
    session.execute("USE rdf;");

    /*session.execute("CREATE TABLE simplex.songs (" + "id uuid PRIMARY KEY,"
            + "title text," + "album text," + "artist text,"
            + "tags set<text>," + "data blob" + ");");*/
    session.execute("CREATE TABLE RDF_POS (" + "subject varchar,"
            + "predicate varchar," + "object varchar, "+ "PRIMARY KEY (predicate, object)"
            + ")");
    //session.execute("CREATE index on RDF_OSP(predicate);");

   // session.execute("CREATE TABLE Sesame (subject text PRIMARY KEY);");
            
}
public void close() {
    cluster.shutdown();
}
public static void main(String[] args) {
    Schema client = new Schema();
    client.connecti();

   // client.createSchema();
   //client.createSchema1();
    client.createSchema2();
    client.close();

}
}
