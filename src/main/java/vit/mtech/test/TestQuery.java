/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;
import com.datastax.driver.core.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author SanjayV
 */
public class TestQuery {
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
public void cqlquery()
     {
        // param.add(null);
         String queryString="SELECT * from TestRDF WHERE subject='AIR';";
         session.execute("USE rdf;");
         ResultSet result=session.execute(queryString);
         
         ColumnDefinitions c=result.getColumnDefinitions();
         int count=c.size();
       
       for(Row row:result)
       {
           for(int i=0;i<count;i++)
           {
               if(row.isNull(i))
               {
                   continue;
               }
               else
               {
                   String val=row.getString(i);
                   System.out.println(val);
               }
           }
       }
        
        

}

public void close() {
    cluster.shutdown();
}
public static void main(String args[])
{
    TestQuery t=new TestQuery();
    t.connecti();
    t.cqlquery();
    t.close();
}
}

