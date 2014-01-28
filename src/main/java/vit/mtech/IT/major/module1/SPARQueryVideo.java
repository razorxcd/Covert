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
class QueryMapper
{
    int flag;
    static ArrayList<String> al=new ArrayList<>();
    static ArrayList<String> find=new ArrayList<>();
    static ArrayList<String> param=new ArrayList<>();
    static ArrayList<List<String>> SubQueries = new ArrayList<>();
    //static ArrayList<List<String>> Results = new ArrayList<>();
    static ArrayList<String> results=new ArrayList<>();
    static HashMap<String, ArrayList> hash=new HashMap<>();
    
    
    //String Ontology="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    //String pred="http://www.w3.org/2000/01/rdf-schema#";
    String  queryString="select DISTINCT ?video ?toTime ?fromTime ?cameraID ?mimeType ?repositoryID ?url ?kindOf ?local where {\n" +
"?eventInstance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.advise-project.eu/ontology/surveillance#Running> \n" +
"?eventInstance <http://www.advise-project.eu/ontology/surveillance#containsSegment> ?segments .\n" +
"?video <http://www.advise-project.eu/ontology/surveillance#hasSegment> ?segments .\n" +
"?video <http://www.advise-project.eu/ontology/surveillance#from> ?hasFrom .\n" +
"?hasFrom <http://www.advise-project.eu/ontology/surveillance#hasTime> ?fromTime .\n" +
"?video <http://www.advise-project.eu/ontology/surveillance#to> ?hasTo .\n" +
"?hasTo <http://www.advise-project.eu/ontology/surveillance#hasTime> ?toTime .\n" +
"?video <http://www.advise-project.eu/ontology/surveillance#isRealisedBy> ?realisedBy .\n" +
"?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasCameraID> ?cameraID .\n" +
"?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasMimeType> ?mimeType .\n" +
"?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasRepositoryID> ?repositoryID .\n" +
"?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasURL> ?url .\n" +
"?realisedBy <http://www.advise-project.eu/ontology/surveillance#isKindOf> ?kindOf .\n" +
"?realisedBy <http://www.advise-project.eu/ontology/surveillance#isLocal> ?local .\n" +
"}";
    Cluster cluster;
    Session session;
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
          
      //  System.out.println(find);
        //System.out.println(param);
        
        for(int i=0;i<14;i++)
        {
            
            SubQueries.add(new ArrayList<String>());
        }
        int j=0;
        for(int i=0;i<14;i++)
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
       System.out.println(SubQueries.get(0));
         
    }
    public void executeSubQueries()
    {
        int j=0;
        for(int i=0;i<14;i++)
        {
           int x=0;
           ArrayList<String> qstrings=new ArrayList<>(0);
            
            here:while(j<param.size())
            {
                if(x!=3)
                {
                    qstrings.add(SubQueries.get(j).get(i));
                    j++;
                    x++;
                }
                else
                {
                    if(!hash.containsKey(qstrings.get(0)))
                    {
                        hash.put(qstrings.get(0),results);
                        String cql="SELECT "+qstrings.get(0)+","+qstrings.get(1)+" FROM Tim";
                        
                        ResultSet result=session.execute(cql);
                    }
                    
                    break here;
                }
            }
        }
    }
          
}
public class SPARQueryVideo {
    public static void main(String args[])
    {
        QueryMapper m=new QueryMapper();
        m.map();
        
    }
}
