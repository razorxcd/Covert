/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;
import java.util.*;

import com.datastax.driver.core.*;

/**
 *
 * @author SanjayV
 */
class ObjectMap
{
    Cluster cluster;
    Session session;
    HashMap<String, String> map=new HashMap<>();
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
    public void init()
    {
        session.execute("USE rdf;");
        
    }
    public void close()
    {
        cluster.shutdown();
    }
    public void mapping()
    {
        String q="SELECT subject, type FROM finalrdf;";
        ResultSet result=session.execute(q);
        for (Row row : result) {
            
            if(row.isNull("type"))
            {
                continue;
            }
            else
            {
               String x=row.getString("subject");
               String y=row.getString("type");
           
          
           
            if(y.contains("http://www.w3.org/2002/07/owl#NamedIndividual"))
            {
                map.put(y, x);
            }
            else
            {
                continue;
            }
            
            } 
            
   
            
           // System.out.println(x+"   "+y);
    }   
    }
    public void UseSub()
    {
         for(Map.Entry<String, String> me:map.entrySet())
       {
           System.out.println(me.getValue());
           
       }
    }
}
public class MissingSubject {
    public static void main(String args[])
    {
        ObjectMap m=new ObjectMap();
        m.connect();
        m.init();
        m.mapping();
        m.UseSub();
        m.close();
    }
    
}
