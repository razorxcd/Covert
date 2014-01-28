/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;
import java.util.*;

import com.datastax.driver.core.*;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author SanjayV
 */
class QueryMapper
{
    int flag;
    static ArrayList<String> al=new ArrayList<>();
    static ArrayList<String> find=new ArrayList<>();
    static ArrayList<String> param=new ArrayList<>();
    static ArrayList<List<String>> SubQueries = new ArrayList<>();
    //static ArrayList<List<String>> Results = new ArrayList<>();
    static ArrayList<String> results=new ArrayList<String>();
    //static ArrayList<String> FinRes=new ArrayList<>();
    //static HashMap<String, ArrayList<String>> hash=new HashMap<>();
    static MultiMap hash = new MultiValueMap();
    static Map<String, List<String>> res = new HashMap<>();
    
    String ub="http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
    //String Ontology="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    //String pred="http://www.w3.org/2000/01/rdf-schema#";
    String  queryString="select ?s ?o where {\n" +
"?s ub:teacherOf \"http://www.Department0.University0.edu/Course15\" .\n" +
"?s ub:undergraduateDegreeFrom ?o .\n" +
"}";
    Cluster cluster;
    Session session;
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
    public void close()
    {
        cluster.shutdown();
    }
    public void map()
    {
          //String query="SELECT ?subject ?object WHERE { ?Subject rdfs:subClassOf ?Object }";
        
          //queryString=queryString.replace("\n", " ");
          String q[]=queryString.split(" ");
          
          
          for(int j=0;j<q.length;j++)
          {
              //System.out.println(q[j]);
          }
          for(int j=0;j<q.length;j++)
          {
              if(!q[j].equals("where"))
              {
                   al.add(q[j]);
              }
              else
              {
                 break;
              }
             // System.out.println(q[j]);
          }
         // System.out.println(al);
         
          
          for(int j=0;j<al.size();j++)
              
          {
              if(al.get(j).equals("select")||al.get(j).equals("DISTINCT")||al.get(j).equals(","))
              {
                  continue;
              }
              else
              {
                  find.add(al.get(j));
              }
          }
          for(String c:find)
          {
              res.put(c, new ArrayList<String>());
          }
          
          for(int j=0;j<q.length;j++)
          {
              if(q[j].contains("{"))
              {
                 flag=j;
                 break;
              }
              else
              {
                  continue;
              }
          }
          
          for(int j=flag;j<q.length-1;j++)
          {
              
              
              if(q[j].contains(".\n"))
                     {
                         //continue;
                         //System.out.println("HI");
                         q[j]=q[j].substring(2);
                         //System.out.println(q[j]);
                         param.add(q[j]);
                      }
              else if(q[j].contains("{"))
              {
                  q[j]=q[j].substring(1);
                  param.add(q[j]);
              }
              else
              {
                  
                  param.add(q[j]);
                  
              }
              
          }
          //System.out.println(param.get(8));
          
       //System.out.println(find);
        //System.out.println(param);
        
        for(int i=0;i<param.size()/3;i++)
        {
            
            SubQueries.add(new ArrayList<String>());
        }
        int j=0;
        for(int i=0;i<param.size()/3;i++)
        {
            int x=0;
            
            here:while(j<param.size())
            {
                if(x!=3)
                {
                    SubQueries.get(i).add(param.get(j));
                    j++;
                    x++;
                }
                else
                {
                    break here;
                }
            }
        }
       //System.out.println(SubQueries);
         
    }
    public void executeSubQueries()
    {
        int tabs=0;
        
        outer:for(int i=0;i<param.size()/3;i++)
        {
           int j=0;
           int x=0;
           ArrayList<String> qstrings=new ArrayList<>();
            
            here:while(j<SubQueries.size())
            {
                tabs++;
                while(x<3)
                {
                    //System.out.println("Hello");
                    qstrings.add(SubQueries.get(i).get(j));
                    j++;
                    x++;
                }
                //else
                //{
                    //System.out.println(qstrings);
                ArrayList<Integer> f=new ArrayList<>();
                for(int li=0;li<qstrings.size();li++)
                {
                    
                     if(qstrings.get(li).contains("?"))
                           {
                               //System.out.println(qstrings.get(li)+" contains ?");
                               f.add(qstrings.indexOf(qstrings.get(li)));
                           }
                     else
                     {
                         continue;
                     }
                }
                 for(int l=0;l<qstrings.size();l++)
                       {
                          
                            if(qstrings.get(l).contains("\""))
                           {
                               
                               qstrings.set(l, qstrings.get(l).replace("\"",""));
                              // System.out.println(qstrings.get(l));
                           }
                           else if(qstrings.get(l).contains("\n"))
                           {
                           
                               qstrings.set(l, qstrings.get(l).replace("\n",""));
                               //System.out.println(qstrings.get(l));
                               
                           }
                           else
                           {
                               continue;
                           }
                           
                           
                    
                        
                      
                       }
                 for(int fg=0;fg<f.size();fg++)
                 {
                    //System.out.println("FLAGS :" +fg);
                 }
                 if(f.get(0)==0)
                 {
                    if(!hash.containsKey(qstrings.get(0)))
                    {
                       // System.out.println("one");
                        
                      
                       
                      // hash.put(qstrings.get(0),new ArrayList<String>());
                       /* for (Map.Entry<String, ArrayList<String>> entry : hash.entrySet()) {

                            String key = entry.getKey();

                            List<String> values = entry.getValue();

                            //System.out.println("Key = " + key);

                            //System.out.println("Values = " + values);

                            }*/
                       
                        String pre[]=qstrings.get(1).split(":");
                        String cql="SELECT subject"+","+pre[1]+" FROM University";
                        
                        ResultSet result=session.execute(cql);
                        for (Row row : result) 
                        {
            
                                    if(row.isNull(pre[1]))
                                    {
                                            continue;
                                    }
                                    else
                                    {
                                         String xx=row.getString("subject");
                                         String y=row.getString(pre[1]);
                                         
                                         //System.out.println(xx+"----->"+y);
           
          
           
                                        if(y.contains("http://www.Department0.University0.edu/Course15"))
                                        {
                                           // System.out.println(" HURRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAYYYYY");
                                            //=hash.get(qstrings.get(0));
                                                hash.put(qstrings.get(0), xx);
                                                 results.add(xx);
                                        }
                                        else
                                        {
                                                continue;
                                        }
            
                                     } 
            
                                   
            
           //System.out.println(x+"   "+y);
                    }
                        Set<String> keys = hash.keySet();
                        for (String key : keys) {
                            System.out.println("Key = " + key);
                        }
                        //System.out.println(results);
                         
                }
                    else
                    {
                        System.out.println("Finally");
                        String pre[]=qstrings.get(1).split(":");
                        List<String> list=(List<String>) hash.get(qstrings.get(0));
                        for(String c:list)
                        {
                            
                            //System.out.println(c);
                            String cql="SELECT subject"+","+pre[1]+" FROM University WHERE subject='"+c+"';";
                            ResultSet result=session.execute(cql);
                            if(result.isExhausted())
                            {
                                continue;
                            }
                            else
                            {
                                for (Row row : result) 
                                {
                                    if(row.isNull(pre[1]))
                                    {
                                        continue;
                                    }
                                    else
                                    {
                                    String xx=row.getString("subject");
                                    String y=row.getString(pre[1]);
                                    String ysplit[]=y.split(" ");
                                    String yfin="";
                                    for(int lis=0;lis<ysplit.length;lis++)
                                    {
                                        if(!ysplit[lis].contains("null")&&ysplit[lis].contains("|"))
                                        {
                                            ysplit[lis]=ysplit[lis].substring(0, ysplit[lis].length()-1);
                                        }
                                    }
                                    ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                    System.out.println(ysplitlist);
                                    HashSet set=new HashSet(ysplitlist);
                                   
                                    Iterator iterator = set.iterator(); 
                                    while ( iterator.hasNext() )
                                    {
                                        String it=iterator.next().toString();
                                        if(it.contains("null"))
                                        {
                                            System.out.println("FOund");
                                            continue;
                                        }
                                        else
                                        {
                                            System.out.println(xx+"---------->"+it);
                                            hash.put(qstrings.get(2), it);
                                        }
            
                                    }
                                       
                                    
                                    //System.out.println("Hy");
                                    
                                    //System.out.println(xx+"------>"+y);
                                    }
                                }
                        
                            }
                        }
                        break here;
                        
                    }
                 }
                 else
                 {
                     System.out.println("In outermost else");
                        
                            String pre[]=qstrings.get(1).split(":");
                            String cql="SELECT subject"+","+pre[1]+" FROM University WHERE subject='"+qstrings.get(0)+"';";
                            ResultSet result=session.execute(cql);
                            
                              for (Row row : result) 
                             {
            
                                    if(row.isNull(pre[1]))
                                    {
                                            continue;
                                    }
                                    else
                                    {
                                         String xx=row.getString("subject");
                                         String y=row.getString(pre[1]);
                                         hash.put(qstrings.get(2), y);
                                         
                                         //System.out.println(xx+"----->"+y);
           
            
                                     } 
            
                                }
                              break here;
             
                        
                 }
                 if(tabs==SubQueries.size())
                 {
                 break outer;
                 }
              }
            
           }
       }
  }
          

public class TestQ {
    public static void main(String args[])
    {
        QueryMapper m=new QueryMapper();
        m.map();
        m.connecti();
        m.init();
        m.executeSubQueries();
        m.close();
        
    }
}
