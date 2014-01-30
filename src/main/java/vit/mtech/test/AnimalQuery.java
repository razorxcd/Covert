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
class Query
{
    int flag;
    static final String db="finalrdf";
    static ArrayList<String> al=new ArrayList<>();
    static ArrayList<String> find=new ArrayList<>();
    static ArrayList<String> param=new ArrayList<>();
    static ArrayList<List<String>> SubQueries = new ArrayList<>();
    static ArrayList<List<String>> reshold=new ArrayList<>();
    //static ArrayList<List<String>> Results = new ArrayList<>();
    //static ArrayList<String> results=new ArrayList<String>();
    //static ArrayList<String> FinRes=new ArrayList<>();
    //static HashMap<String, ArrayList<String>> hash=new HashMap<>();
    static MultiMap hash = new MultiValueMap();
    static MultiMap results = new MultiValueMap();
    static HashMap<String, String> tabs=new HashMap<>();
    static Map<String, List<String>> res = new HashMap<>();
    
     String ub="http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
    //String Ontology="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    //String pred="http://www.w3.org/2000/01/rdf-schema#";
     /*String  queryString="select * where {\n" +
"\"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Tiny\" rdfs:subClassOf ?o .\n" +
"?o rdfs:subClassOf ?su .\n" +
"?su rdfs:subClassOf \"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Animals\" .\n" +
"}"; */
     String queryString="select * where {\n" +
"?s rdfs:subClassOf ?o .\n" +
"}";
    /*String  queryString="select ?s ?o where {\n" +
"?s ub:teacherOf \"http://www.Department0.University0.edu/Course15\" .\n" +
"?s ub:undergraduateDegreeFrom ?o .\n" +
"}";*/

    /*String  queryString="select * where {\n" +
"?s rdfs:subClassOf \"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Animals\" .\n" +
"}";*/
    Cluster cluster;
    Session session;

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

    public void init() {
        session.execute("USE rdf;");
    }

    public void close() {
        cluster.shutdown();
    }

    public void map() {
        //String query="SELECT ?subject ?object WHERE { ?Subject rdfs:subClassOf ?Object }";

        //queryString=queryString.replace("\n", " ");
        String q[] = queryString.split(" ");


        for (int j = 0; j < q.length; j++) {
            //System.out.println(q[j]);
        }
        for (int j = 0; j < q.length; j++) {
            if (!q[j].equals("where")) {
                al.add(q[j]);
            } else {
                break;
            }
            // System.out.println(q[j]);
        }
        // System.out.println(al);


        for (int j = 0; j < al.size(); j++) {
            if (al.get(j).equals("select") || al.get(j).equals("DISTINCT") || al.get(j).equals(",")) {
                continue;
            } else {
                find.add(al.get(j));
            }
        }
        for (String c : find) {
            res.put(c, new ArrayList<String>());
        }

        for (int j = 0; j < q.length; j++) {
            if (q[j].contains("{")) {
                flag = j;
                break;
            } else {
                continue;
            }
        }

        for (int j = flag; j < q.length - 1; j++) {


            if (q[j].contains(".\n")) {
                //continue;
                //System.out.println("HI");
                q[j] = q[j].substring(2);
                //System.out.println(q[j]);
                param.add(q[j]);
            } else if (q[j].contains("{")) {
                q[j] = q[j].substring(1);
                param.add(q[j]);
            } else {

                param.add(q[j]);

            }

        }
        //System.out.println(param.get(8));

        //System.out.println(find);
        System.out.println(param);
        for (int i = 0; i < param.size(); i++) {
            if (param.get(i).contains("\n")) {
                System.out.println(param.get(i).replace("\n", ""));
                param.set(i, param.get(i).replace("\n", ""));
            } else if (param.get(i).contains("\"")) {
                param.set(i, param.get(i).replace("\"", ""));
            }
        }

        for (int i = 0; i < param.size() / 3; i++) {

            SubQueries.add(new ArrayList<String>());
        }
        int j = 0;
        for (int i = 0; i < param.size() / 3; i++) {
            int x = 0;

            here:
            while (j < param.size()) {
                if (x != 3) {
                    SubQueries.get(i).add(param.get(j));
                    j++;
                    x++;
                } else {
                    break here;
                }
            }
        }
        System.out.println(SubQueries);

    }
    
    
    public void executeSubQueries() {
        //int tabs=0;

        outer:
        for (int i = 0; i < param.size() / 3; i++) {
            int j = 0;
            int x = 0;
            reshold.add(new ArrayList<String>());
            ArrayList<String> qstrings = new ArrayList<>();
            tabs.put("subject", "false");
            tabs.put("predicate", "false");
            tabs.put("object", "false");
            
            

            here:
            while (j < SubQueries.size()) {
                //tabs++;
                while (x < 3) {
                    //System.out.println("Hello");
                    qstrings.add(SubQueries.get(i).get(j));
                    j++;
                    x++;
                }
                //else
                //{
                
                System.out.println("Look at here!" + qstrings);
                ArrayList<Integer> f = new ArrayList<>();

                if (qstrings.get(0).contains("?")&&qstrings.get(2).contains("?")) {
                    tabs.put("subject", "true");
                    tabs.put("object", "true");
                } else if (qstrings.get(2).contains("?")) {
                    tabs.put("object", "true");
                } 
                else if(qstrings.get(0).contains("?"))
                  {
                      //System.out.println("Look here goddamnit!");
                          tabs.put("subject", "true");
                          
                  }
                        else {
                    tabs.put("predicate", "true");
                }
                
                System.out.println(tabs.get("subject")+tabs.get("object"));

                for (int li = 0; li < qstrings.size(); li++) {

                    if (qstrings.get(li).contains("?")) {
                        //System.out.println(qstrings.get(li)+" contains ?");
                        f.add(qstrings.indexOf(qstrings.get(li)));


                    } else {
                        continue;
                    }
                }
                if (i != 0) {
                    if (results.containsKey(qstrings.get(0))) {
                        System.out.println("REMOVING VALUE FOR KEY:" + qstrings.get(0));
                        results.remove(qstrings.get(0));
                    }
                }
                /*
                 * 
                 for(int l=0;l<qstrings.size();l++)
                 {
                 if(qstrings.get(l).contains("\""))
                 {
                               
                 qstrings.set(l, qstrings.get(l).replace("\"",""));
                 // System.out.println(qstrings.get(l));
                 }
                 else if(qstrings.get(l).contains("\n"))
                 {
                 System.out.println(" THERE!");
                           
                 qstrings.set(l, qstrings.get(l).replace("\n",""));
                 //System.out.println(qstrings.get(l));
                               
                 }
                 else
                 {
                 continue;
                 }
   
                 }*/
                for (int fg = 0; fg < f.size(); fg++) {
                    //System.out.println("FLAGS :" +fg);
                }

                /*If the subject is missing*/

                if (tabs.get("subject").equals("true")&&tabs.get("object").equals("false")) {

                    /*Check whether the Subject is in the HashMap as key*/
                    if (!hash.containsKey(qstrings.get(0))) {
                        System.out.println("Subject is missing; everything else is available");

                        String pre[] = qstrings.get(1).split(":");

                        String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + ";";

                        System.out.println(cql);

                        ResultSet result = session.execute(cql);

                        for (Row row : result) {

                            if (row.isNull(pre[1])) {
                                continue;
                            } else {
                                String xx = row.getString("subject");
                                String y = row.getString(pre[1]);
                                String ysplit[] = y.split(" ");
                                String yfin = "";
                                if (y.contains(qstrings.get(2))) {
                                    for (int lis = 0; lis < ysplit.length; lis++) {
                                        if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                            ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                        }
                                    }
                                    ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                    // System.out.println(ysplitlist);
                                    HashSet set = new HashSet(ysplitlist);

                                    Iterator iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        String it = iterator.next().toString();
                                        if (it.contains("null")) {
                                            System.out.println("FOund");
                                            continue;
                                        } else if (it.contains(qstrings.get(2))) {
                                            if (i == 0) {
                                                reshold.get(i).add(xx);
                                                results.put(qstrings.get(0), xx);
                                            } else {
                                                System.out.println(" YOU NEED TO ACCOUNT FOR TEST CASES LIKE THIS");
                                            }
                                            System.out.println(xx + "---------->" + it);
                                            hash.put(qstrings.get(0), xx);
                                            //results.put(qstrings.get(0), xx);

                                        }

                                    }

                                } else {
                                    continue;
                                }


                            }



                            //System.out.println(x+"   "+y);
                        }

                        //System.out.println(results);

                    } else {
                        /*if Subject is available in HashMap as a result of previous query*/
                        System.out.println("Subject Available (Previous Query) Object missing");
                        System.out.println("Finally");
                        String pre[] = qstrings.get(1).split(":");
                        List<String> list = (List<String>) hash.get(qstrings.get(0));
                        if (!qstrings.get(2).contains("?")) {
                            /*If there are no variables in this Line*/

                            for (String c : reshold.get(i - 1)) {
                                //String conc=c;

                                System.out.println("No variables found. Everything is available");
                                //System.out.println(c);

                                String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + c + "';";
                                System.out.println(cql);
                                ResultSet result = session.execute(cql);
                                if (result.isExhausted()) {
                                    continue;
                                } else {
                                    for (Row row : result) {
                                        if (row.isNull(pre[1])) {
                                            continue;
                                        } else {
                                            String xx = row.getString("subject");
                                            String y = row.getString(pre[1]);
                                            String ysplit[] = y.split(" ");
                                            String yfin = "";
                                            if (y.contains(qstrings.get(2))) {
                                                for (int lis = 0; lis < ysplit.length; lis++) {
                                                    if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                                        ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                                    }
                                                }
                                                ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                                // System.out.println(ysplitlist);
                                                HashSet set = new HashSet(ysplitlist);

                                                Iterator iterator = set.iterator();
                                                while (iterator.hasNext()) {
                                                    String it = iterator.next().toString();
                                                    if (it.contains("null")) {
                                                        System.out.println("FOund");
                                                        continue;
                                                    } else if (it.contains(qstrings.get(2))) {
                                                        System.out.println(xx + "---------->" + it);
                                                        //hash.put(qstrings.get(0), conc);
                                                        reshold.get(i).add(xx);
                                                        hash.put(qstrings.get(0), it);
                                                        hash.put(qstrings.get(0), xx);

                                                        results.put(qstrings.get(0), xx);
                                                    }

                                                }

                                            } else {
                                                continue;
                                            }
                                            //System.out.println("Hy");

                                            //System.out.println(xx+"------>"+y);
                                        }
                                    }

                                }
                            }

                        } else {

                            for (String c : reshold.get(i - 1)) {
                                /*if Subject is available as a result of previous query, but object is missing*/
                                // String conc=c;
                                System.out.println("Contains a variable with Subject taken from previous query");
                                System.out.println(" Right!");
                                String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + c + "';";
                                System.out.println(cql);
                                ResultSet result = session.execute(cql);
                                if (result.isExhausted()) {
                                    continue;
                                } else {
                                    for (Row row : result) {
                                        if (row.isNull(pre[1])) {
                                            continue;
                                        } else {
                                            String xx = row.getString("subject");
                                            String y = row.getString(pre[1]);
                                            String ysplit[] = y.split(" ");
                                            String yfin = "";
                                            for (int lis = 0; lis < ysplit.length; lis++) {
                                                if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                                    ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                                }
                                            }
                                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                            System.out.println(ysplitlist);
                                            HashSet set = new HashSet(ysplitlist);

                                            Iterator iterator = set.iterator();
                                            while (iterator.hasNext()) {
                                                String it = iterator.next().toString();
                                                if (it.contains("null")) {
                                                    System.out.println("FOund");
                                                    continue;
                                                } else {
                                                    if (it.equals("")) {
                                                        continue;
                                                    } else {
                                                        System.out.println("Correct");
                                                        System.out.println(xx + "---------->" + it);
                                                        //hash.put(qstrings.get(2), conc);
                                                        hash.put(qstrings.get(2), it);

                                                        reshold.get(i).add(it);
                                                        hash.put(qstrings.get(2), xx);

                                                        results.put(qstrings.get(2), it);
                                                        results.put(qstrings.get(0), xx);
                                                    }

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
                } else if (tabs.get("object").equals("true")&&tabs.get("subject").equals("false")) {
                    /*if the object is missing*/
                    System.out.println("In outermost else");
                    for (int l = 0; l < qstrings.size(); l++) {
                        if (qstrings.get(l).contains("\"")) {

                            qstrings.set(l, qstrings.get(l).replace("\"", ""));
                            // System.out.println(qstrings.get(l));
                        } else if (qstrings.get(l).contains("\n")) {

                            qstrings.set(l, qstrings.get(l).replace("\n", ""));
                            //System.out.println(qstrings.get(l));

                        } else {
                            continue;
                        }

                    }
                    // System.out.println(qstrings.get(0));

                    String pre[] = qstrings.get(1).split(":");
                    String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + qstrings.get(0) + "';";
                    System.out.println(cql);
                    ResultSet result = session.execute(cql);

                    for (Row row : result) {

                        if (row.isNull(pre[1])) {
                            continue;
                        } else {
                            String xx = row.getString("subject");
                            String y = row.getString(pre[1]);
                            //hash.put(qstrings.get(2), y);
                            String ysplit[] = y.split(" ");
                            String yfin = "";
                            for (int lis = 0; lis < ysplit.length; lis++) {
                                if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                    ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                }
                            }
                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                            System.out.println(ysplitlist);
                            HashSet set = new HashSet(ysplitlist);

                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                String it = iterator.next().toString();
                                if (it.contains("null")) {
                                    System.out.println("FOund");
                                    continue;
                                } else {
                                    if (it.equals("")) {
                                        System.out.println("Got it");
                                        continue;
                                    } else {
                                        System.out.println(xx + "---------->" + it);
                                        //hash.put(qstrings.get(2), qstrings.get(0));
                                        if (i == 0) {
                                            reshold.get(i).add(it);
                                            results.put(qstrings.get(2), it);
                                        } else {
                                            System.out.println(" Do your due dilligence");
                                        }

                                        hash.put(qstrings.get(2), it);
                                        hash.put(qstrings.get(2), xx);

                                        //hash.put(qstrings.get(2), xx);
                                        //results.put(qstrings.get(2), it);
                                        //results.put(qstrings.get(2), xx);
                                    }
                                }

                            }

                        }

                    }
                    break here;

                } else if(tabs.get("subject").equals("true")&&tabs.get("object").equals("true")) {
                    //to be done
                     System.out.println("Subject and Object are missing!");
                    for (int l = 0; l < qstrings.size(); l++) {
                        if (qstrings.get(l).contains("\"")) {

                            qstrings.set(l, qstrings.get(l).replace("\"", ""));
                            // System.out.println(qstrings.get(l));
                        } else if (qstrings.get(l).contains("\n")) {

                            qstrings.set(l, qstrings.get(l).replace("\n", ""));
                            //System.out.println(qstrings.get(l));

                        } else {
                            continue;
                        }

                    }
                    String pre[]=qstrings.get(1).split(":");
                    String cql="SELECT subject, "+pre[1]+" from "+db+";";
                    System.out.println(cql);
                    ResultSet result = session.execute(cql);
                    for(Row row:result)
                    {
                        if (row.isNull(pre[1])) {
                            continue;
                        } else {
                            String xx = row.getString("subject");
                            String y = row.getString(pre[1]);
                            System.out.println(xx);
                            //hash.put(qstrings.get(2), y);
                            
                             
                                // S//ystem.out.println(i);
                                    System.out.println((param.size()/3)-1);
                               if((param.size()/3)-1!=0)
                               {
                                if(i<(param.size()/3)-1)
                                 {
                                     if(SubQueries.get(i+1).get(0).equals(qstrings.get(2)))
                                     {
                                         reshold.get(i).add(y);
                                         results.put(qstrings.get(2), y);
                                         System.out.println("Case 1");
                                     }
                                     else if(SubQueries.get(i+1).get(0).equals(qstrings.get(0)))
                                     {
                                         reshold.get(i).add(xx);
                                         results.put(qstrings.get(0), xx);
                                         System.out.println("Case 2");
                                     }
                                     else if(SubQueries.get(i+1).get(0).equals(qstrings.get(2))&&SubQueries.get(i+1).get(0).equals(qstrings.get(0)))
                                     {
                                         System.out.println("Case 3");
                                         continue;
                                         
                                     }
                                     else
                                     {
                                         System.out.println("Case 4");
                                         results.put(qstrings.get(0), xx);
                                         results.put(qstrings.get(2), y);
                                     }
                                       // System.out.println(" The next query's first varibale is"+SubQueries.get(i+1).get(0));
                                 }
                                    else
                                    {
                                        System.out.println("you screwed up!");
                                    }
                               }
                               else
                               {
                                   results.put(qstrings.get(0), xx);
                                   results.put(qstrings.get(2), y);
                               }
                             
                             
                              
                               // reshold.get(i).add(y);
                               // results.put(qstrings.get(2), y);
                           
                        }
                            
                    }

                    
                }

            }

        }
    }
}
          

public class AnimalQuery extends Query {
    public static void main(String args[])
    {
        Query m=new Query();
        m.map();
        m.connecti();
        m.init();
        m.executeSubQueries();
        Set<String> keys = hash.keySet();
 
        // iterate through the key set and display key and values
        for (String key : keys) {
           // System.out.println("Key = " + key);
            //System.out.println("Values = " + hash.get(key));
            }
        
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        int countk=0, countv=0;
     Set<String> keys1 = results.keySet();
      for (String key : keys1) {
            System.out.println("Key = " + key);
            countk++;
            System.out.println("Values = " + results.get(key));
            countv++;
            }
   // System.out.println(reshold.get(reshold.size()-1));
   //System.out.println(reshold.get(reshold.size()-1).size());
        
        m.close();
        
    }
}
