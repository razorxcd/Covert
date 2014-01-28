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
public class NewSchema {
    
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
public void init()
{
    session.execute("USE rdf;");
}
public void createschema()
{
    session.execute("USE rdf;");
    session.execute("CREATE TABLE TestRDF( subject varchar, PRIMARY KEY(subject));");
}
public void addColumns()
{
    String c="col";
    for(int i=513;i<=512;i++)
    {
        String col=c+i;
        System.out.println("Adding "+col);
        session.execute("ALTER TABLE SO_RDF ADD "+col+" varchar;");
        
    }
}
public void close() {
    cluster.shutdown();
}
public static void main(String[] args) {
    NewSchema client = new NewSchema();
    client.connecti();
    client.init();

    //client.createschema();
    client.addColumns();
   
    client.close();

}
    
}
