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
class Mapper1
{
    static int flag=0;
    ArrayList<String> al=new ArrayList<String>();
    ArrayList<String> find=new ArrayList<String>();
    ArrayList<String> param=new ArrayList<String>();
    String Ontology="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    String pred="http://www.w3.org/2000/01/rdf-schema#";
    String queryString="";
    Cluster cluster;
    Session session;
          
    public void map()
    {
        int flag=0;
          String query="SELECT * WHERE { ?Subject ?predicate ?Object }";
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
                  find.add(al.get(j));
              }
          }
          /*for(int j=0;j<q.length;j++)
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
          }*/
          for(int j=0;j<q.length;j++)
          {
              if(q[j].equals("WHERE"))
              {
                  flag=j+2;
              }
          }
          for(int j=flag;j<q.length-1;j++)
          {
              find.add(q[j]);
          }
        System.out.println(find);
         
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
    public void init()
    {
        session.execute("USE rdf;");
    }
    
     public void cqlquery()
     {
        // param.add(null);
         for(String c:find)
         {
             if(c.equals("*"))
             {
                 queryString="SELECT"+" "+find.get(0)+" "+"FROM finalrdf;";
             }
         }
         
         
         
         ResultSet result=session.execute(queryString);
         
         //ResultSet result=session.execute("SELECT * from TestRDF WHERE subject='http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType18';");
         
         ColumnDefinitions c=result.getColumnDefinitions();
         int count=c.size();
       
       for(Row row:result)
       {
           flag++;
           System.out.println("--------------------------------------------------------------------------------------------------------- ");
            
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
        
         

System.out.println("Count is"+flag);
     }
     public void close() {
    cluster.shutdown();
}
      
    
    
    
}
public class SelectQueryMapper {
    
    public static void main(String args[])
    {
        Mapper1 m=new Mapper1();
        m.map();
        m.connect();
        m.init();
        m.cqlquery();
        m.close();
        
    }
    
}
