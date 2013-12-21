/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT;
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
            + "tags set<text>," + "data blob" + ");");
    session.execute("CREATE TABLE simplex.playlists (" + "id uuid,"
            + "title text," + "album text, " + "artist text,"
            + "song_id uuid," + "PRIMARY KEY (id, title, album, artist)"
            + ");");
*/
   // session.execute("CREATE TABLE Sesame (subject text PRIMARY KEY);");
            
}
public void close() {
    cluster.shutdown();
}
public static void main(String[] args) {
    Schema client = new Schema();
    client.connecti();

   // client.createSchema();
    client.close();

}
}
