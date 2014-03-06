
package vit.mtech.test;


import java.util.*;

import com.datastax.driver.core.*;
import java.io.BufferedInputStream;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;


/**
 *
 * @author SanjayV
 */
public class QueryTransform {

    int flag;
    static String queryString;
    static final String db = "BSBM50";
    static ArrayList<String> al = new ArrayList<>();
    static ArrayList<String> find = new ArrayList<>();
    static ArrayList<String> param = new ArrayList<>();
    static ArrayList<String> paramv1 = new ArrayList<>();
    static ArrayList<List<String>> SubQueries = new ArrayList<>();
    static ArrayList<List<String>> reshold = new ArrayList<>();
    static ArrayList<Integer> fil = new ArrayList<>();
    //static ArrayList<List<String>> Results = new ArrayList<>();
    //static ArrayList<String> results=new ArrayList<String>();
    //static ArrayList<String> FinRes=new ArrayList<>();
    //static HashMap<String, ArrayList<String>> hash=new HashMap<>();
    static MultiMap hash = new MultiValueMap();
    static MultiMap results = new MultiValueMap();
    static HashMap<String, String> tabs = new HashMap<>();
    static Map<String, List<String>> res = new HashMap<>();
    static HashMap<String, String> filtertabs = new HashMap<>();
    static HashMap<String, String> expression = new HashMap<>();
    static boolean filter = false;
    static boolean filterops = false;
    String ub = "http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
    //String Ontology="http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#";
    //String pred="http://www.w3.org/2000/01/rdf-schema#";
    /*String queryString = "select ?give where {\n"dddddddd
     + "\"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Tiny\" rdfs:subClassOf ?o .\n"
     + "?o rdfs:subClassOf ?su .\n"
     + "?su rdfs:subClassOf \"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Animals\" .\n"
     + "?p rdf:type \"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Dull\" .\n"
     + "?p rdf:type ?type .\n"
     + "?give rdf:type ?everything "
     + "}";*/
    /*String queryString="select * where {\n" +
     "?s ?p ?o .\n" +
     "}";*/
//    String queryString =  "select ?product ?label ?value1 ?value2 ?pro where {\n"
//            + "?product rdf:type http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType10 .\n"
//            + "?product bsbm:productFeature http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature397 .\n"
//            + "?product bsbm:productFeature http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature380 .\n"
//            + "?product bsbm:productPropertyNumeric1 ?value1 .\n"
//            + "FILTER ( ?value1 > 500 ) .\n"
//            + "?product bsbm:productPropertyNumeric1 ?value2 .\n"
//            + "FILTER ( ?value2 < 900 ) .\n"
//            + "?product rdfs:label ?label .\n"
//            + "};";
    
//    String queryString="select ?name where {\n" +
//"?person foaf:name ?name .\n" +
//"}";
//    String queryString="select ?label ?comment ?producer ?productFeature ?propertyTextual1 ?propertyTextual2 ?propertyTextual3 " +
//    "?propertyNumeric1 ?propertyNumeric2 ?propertyTextual4 ?propertyTextual5 ?propertyNumeric4 where {\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" rdfs:label ?label .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" rdfs:comment ?comment .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:producer ?p .\n" +
//    "?p rdfs:label ?producer .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" dc:publisher ?p .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:productFeature ?f .\n" +
//    "?f rdfs:label ?productFeature .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:productPropertyTextual1 ?propertyTextual1 .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:productPropertyTextual2 ?propertyTextual2 .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:productPropertyTextual3 ?propertyTextual3 .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:productPropertyNumeric1 ?propertyNumeric1 .\n" +
//    "\"http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/Product209\" bsbm:productPropertyNumeric2 ?propertyNumeric2 .\n" +   
//"}";
    /*String  queryString="select ?s ?o where {\n" +
     "?s ub:teacherOf \"http://www.Department0.University0.edu/Course15\" .\n" +
     "?s ub:undergraduateDegreeFrom ?o .\n" +
     "}";*/
//    String queryString="select ?sub ?type where {\n" +
//     "?s rdfs:subClassOf ?sub .\n" +
//     "\"http://www.semanticweb.org/sanjayv/ontologies/2013/11/untitled-ontology-11#Animals\" rdf:type ?type .\n" +
//     "}";
    Cluster cluster;
    Session session;

    public QueryTransform(String str)
    {
        queryString=str;
    }
    public void connecti() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        Metadata metadata = cluster.getMetadata();
        System.out.println("Cassandra connection established");
        System.out.printf("Connected to cluster: %s\n",metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s \n",host.getDatacenter(), host.getAddress(), host.getRack());
            session = cluster.connect();

        }
    }
//
//    public void setQuery() {
//        Scanner stdin = new Scanner(new BufferedInputStream(System.in));
//        System.out.println(" Enter the Query:");
//        queryString = stdin.nextLine();
//        //System.out.println(Math.abs(stdin.nextLong() - stdin.nextLong()));
//    }

    public void init() {
        session.execute("USE rdf;");

    }

    public void close() {
        cluster.shutdown();
    }

    public void map() {
        //String query="SELECT ?subject ?object WHERE { ?Subject rdfs:subClassOf ?Object }";

        //queryString=queryString.replace("\n", " ");
//        StringBuilder result = new StringBuilder();
//        
//        System.out.println(result);
        //queryString=queryString.replace(".", "*");
        queryString=queryString.replace("\n", " n ");
        //queryString=queryString.replace(" .", "\\*");
        queryString=queryString.trim().replaceAll("\\s+", " ");
        String q[] = queryString.split(" ");


       
        for (int j = 0; j < q.length; j++) {
            if (!q[j].equalsIgnoreCase("WHERE")) {
                al.add(q[j]);
            } else {
                break;
            }
            // System.out.println(q[j]);
        }
        // System.out.println(al);


        for (int j = 0; j < al.size(); j++) {
            if (al.get(j).equalsIgnoreCase("SELECT") || al.get(j).equalsIgnoreCase("DISTINCT") || al.get(j).equals(",")) {
                continue;
            } else {
                find.add(al.get(j));
            }
        }
//        for (String c : find) {
//            res.put(c, new ArrayList<String>());
//        }

        for (int j = 0; j < q.length; j++) {
            if (q[j].contains("{")) {
                flag = j;
                break;
            } else {
                continue;
            }
        }

        for (int j = flag; j < q.length - 1; j++) {
            if (q[j].contains("{")) {
//                q[j] = q[j].substring(1);
//                paramv1.add(q[j]);
                continue;
            } else {
                paramv1.add(q[j]);
            }

        }
        for (int j = flag; j < q.length - 1; j++) {

            if(q[j].equals("n"))
            {
                continue;
            }
            else if (q[j].equals(".")||q[j].equals("{")) {
               if(q[j].contains("http")||q[j].contains(":"))
               {
                   param.add(q[j]);
               }
               else
                   continue;
                //System.out.println("HI");
                //q[j] = q[j].substring(2);
                //System.out.println(q[j]);
                //param.add(q[j]);
            } 
             else {

                param.add(q[j]);

            }

        }

        System.out.println(param);
        for (int i = 0; i < paramv1.size(); i++) {
            if (paramv1.get(i).equalsIgnoreCase("FILTER")) {
                filter = true;
                fil.add(i);
                System.out.println("Yes FILTER is there at" + i);
            }
        }




        for (int i = 0; i < param.size(); i++) {

            System.out.println("Putting flase for:" + param.get(i));
            filtertabs.put(param.get(i), "false");


        }

        //System.out.println(find);
        System.out.println(param);
        System.out.println(paramv1);


        for (int i = 0; i < param.size() / 3; i++) {

            SubQueries.add(new ArrayList<String>());
        }
        int j = 0;
        for (int i = 0; i < param.size() / 3; i++) {
            int x = 0;

            here:
            while (j < param.size()) {
                
                if (x != 3) {
//                    if(param.get(j).equals("n"))
//                {
//                    System.out.println("It is going");
//                    continue here;
//                }
                    SubQueries.get(i).add(param.get(j));
                    j++;
                    x++;
                } else {
                    break here;
                }
            }
        }
        System.out.println(SubQueries);
        System.out.println("Size of SubQueries"+SubQueries.size());
        System.out.println("Size of Param"+param.size()/3);
        

    }

    public void executeSubQueries() {
        //int tabs=0;

        if (filter == true) {
            System.out.println("I'm calling exmap but it's not doing it;s job");
            exMapFill();
        }
        else
        {
               // System.out.println("I am not even calling exmap");
        }
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
                System.out.println("Cheack to skip!! " + SubQueries.get(i).get(j));
                if (SubQueries.get(i).get(j).equalsIgnoreCase("FILTER")) {
                    break here;
                } else if (expression.containsKey(SubQueries.get(i).get(j))) {
                    System.out.println("YAYAYAY!!");
                    break here;
                }
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

                if (i != 0) {
                    if (results.containsKey(qstrings.get(0))) {
                        System.out.println("REMOVING VALUE FOR KEY:" + qstrings.get(0));
                        results.remove(qstrings.get(0));
                        //results.put(qstrings.get(0), "N");
                    }
                }

                if (filter == true) {
                    //if(qstrings.get(0))
                    String expr = evalAndPrint(qstrings.get(0), qstrings.get(2));
                    

//                    for (int xy = 0; xy < SubQueries.size(); xy++) {
//
//
//                        if (qstrings.get(0).equals(SubQueries.get(xy).get(0)) && !qstrings.get(2).equals(SubQueries.get(xy).get(2))) {
//                            lastfound = xy;
//
//                        } else {
//                            //lastfound=xy;
//                            break;
//                        }
//
//                    }

                    if (filterops == true) {
                        int lastfound[] = findTheLastOccurence(qstrings.get(0), qstrings.get(2));
                        System.out.println("LastFound ==" + lastfound[0]);
                        String qstring[] = qstrings.get(1).split(":");
                        System.out.println("Expression to be fed to FILTEROPS" + expr);
                        stackOps so = new stackOps(expr);
                        so.connecti();
                        so.init();
                        System.out.println("Going into So.Filterout--->"+lastfound+qstring[1]+ qstrings.get(2)+ qstrings.get(0));
                        so.filterOut(lastfound[0], lastfound [1], qstring[1], qstrings.get(2), qstrings.get(0));

                        System.out.println("Filterops Started");
                        so.close();
                        break here;

                    }

                }

                if (qstrings.get(0).contains("?") && qstrings.get(2).contains("?") && qstrings.get(1).contains("?")) {
                    tabs.put("subject", "true");
                    tabs.put("object", "true");
                    tabs.put("predicate", "true");
                } else if (qstrings.get(0).contains("?") && qstrings.get(2).contains("?")) {
                    tabs.put("subject", "true");
                    tabs.put("object", "true");
                } else if (qstrings.get(2).contains("?")) {
                    tabs.put("object", "true");
                } else if (qstrings.get(0).contains("?")) {
                    //System.out.println("Look here goddamnit!");
                    tabs.put("subject", "true");

                }


                System.out.println(tabs.get("subject") + tabs.get("object"));

//                for (int li = 0; li < qstrings.size(); li++) {
//
//                    if (qstrings.get(li).contains("?")) {
//                        //System.out.println(qstrings.get(li)+" contains ?");
//                        f.add(qstrings.indexOf(qstrings.get(li)));
//
//
//                    } else {
//                        continue;
//                    }
//                }

                /* if(results.containsKey(qstrings.get(0)))
                 {
                 System.out.println("It does contain it");
                 }
                 else
                 {
                 System.out.println("Something is WRONG!!");
                 }*/

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

                /*If the subject is missing*/


                if (tabs.get("subject").equals("true") && tabs.get("object").equals("true") && tabs.get("predicate").equals("true")) {
                    System.out.println("Select * case");
                    String cql = "SELECT * from " + db + ";";
                    ResultSet result = session.execute(cql);
                    ColumnDefinitions c = result.getColumnDefinitions();
                    int count = c.size();
                    for (Row row : result) {
                        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        for (int ix = 0; ix < count; ix++) {
                            if (row.isNull(ix)) {
                                continue;
                            } else {

                                String val = row.getString(ix);
                                System.out.println(val);
                                //results.put(val, c);

                            }
                        }

                    }
                } else if (tabs.get("subject").equals("true")) {

                    /*Check whether the Subject is in the HashMap as key*/
                    if (tabs.get("object").equals("true") && !hash.containsKey(qstrings.get(0)) && !hash.containsKey(qstrings.get(2))) {
                        System.out.println("Subject and Object are missing!");
//Comments added!
//                        for (int l = 0; l < qstrings.size(); l++) {
//                            if (qstrings.get(l).contains("\"")) {
//
//                                qstrings.set(l, qstrings.get(l).replace("\"", ""));
//                                // System.out.println(qstrings.get(l));
//                            } else if (qstrings.get(l).contains("\n")) {
//
//                                qstrings.set(l, qstrings.get(l).replace("\n", ""));
//                                //System.out.println(qstrings.get(l));
//
//                            } else {
//                                continue;
//                            }
//
//                        }
                        String ysplit[];
                        String pre[] = qstrings.get(1).split(":");
                        String cql = "SELECT subject, " + pre[1] + " from " + db + ";";
                        System.out.println(cql);
                        ResultSet result = session.execute(cql);
                        for (Row row : result) {
                            if (row.isNull(pre[1])) {
                                continue;
                            } else {
                                String xx = row.getString("subject");
                                String y = row.getString(pre[1]);
                                // System.out.println("V---->"+y);
                                y = y.replace("|", "*");
                                ysplit = y.split("\\*");
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
                                            if ((param.size() / 3) - 1 != 0) {
                                                if (i < (param.size() / 3) - 1) {
                                                    if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2))) {
                                                        reshold.get(i).add(it);
                                                        results.put(qstrings.get(2), it);
                                                        hash.put(qstrings.get(2), it);
                                                        System.out.println("Case 1");
                                                    } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
                                                        reshold.get(i).add(xx);
                                                        reshold.get(i).add(it);
                                                        results.put(qstrings.get(0), xx);
                                                        results.put(qstrings.get(2), it);
                                                        hash.put(qstrings.get(2), it);
                                                        hash.put(qstrings.get(0), xx);
                                                        System.out.println("Case 2");
                                                    } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2)) && SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
                                                        System.out.println("Case 3");
                                                        continue;

                                                    } else {
                                                        System.out.println("Case 4");
                                                        results.put(qstrings.get(0), xx);
                                                        results.put(qstrings.get(2), it);
                                                        hash.put(qstrings.get(2), it);
                                                        hash.put(qstrings.get(0), xx);
                                                    }
                                                    // System.out.println(" The next query's first varibale is"+SubQueries.get(i+1).get(0));
                                                } else {
                                                    System.out.println("you screwed up!");
                                                }
                                            } else {
                                                results.put(qstrings.get(0), xx);
                                                results.put(qstrings.get(2), it);
                                                hash.put(qstrings.get(2), it);
                                                hash.put(qstrings.get(0), xx);
                                            }

                                        }
                                    }
                                    //System.out.println(xx);
                                    //hash.put(qstrings.get(2), y);


                                    // S//ystem.out.println(i);
                                    //System.out.println((param.size()/3)-1);



                                    // reshold.get(i).add(y);
                                    // results.put(qstrings.get(2), y);

                                }
                            }

                        }

                    } 
                    else if(tabs.get("object").equals("true") && !hash.containsKey(qstrings.get(0)) && hash.containsKey(qstrings.get(2)))
                    {
                    
                        System.out.println("Subject is missing; object is taken from previous query!");
                        String pre[] = qstrings.get(1).split(":");
                        String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + ";";
                        System.out.println(cql);
                        ResultSet result = session.execute(cql);
                        int lastfound = findTheLastOccurenceOfPredicate(qstrings.get(0), qstrings.get(2));
                        System.out.println("Lastfound--" + lastfound);
                        for (Row row : result) {
                            for (String c : reshold.get(lastfound)) {

                                if (row.isNull(pre[1])) {
                                    continue;
                                } else {
                                    String ysplit[];
                                    String xx = row.getString("subject");
                                    String y = row.getString(pre[1]);
                                    y = y.replace("|", "*");
                                    ysplit = y.split("\\*");
                                    String yfin = "";
                                    if (y.contains(c)) {
                                        for (int lis = 0; lis < ysplit.length; lis++) {
                                            if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                                ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                                System.out.println("FISHY-->" + ysplit[lis]);
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
                                            } else if (it.contains(c)) {

                                                reshold.get(i).add(it);
                                                results.put(qstrings.get(2), it);
                                                results.put(qstrings.get(0), xx);
                                            }
                                            System.out.println(xx + "---------->" + it);
                                            hash.put(qstrings.get(2), it);
                                            hash.put(qstrings.get(0), xx);
                                            //results.put(qstrings.get(0), xx);

                                        }




                                    } else {
                                        continue;
                                    }

                                }

                            }
                        }


                    }
                    
                    else if (!hash.containsKey(qstrings.get(0))) {
                        System.out.println("Subject is missing; everything else is available");

                        String pre[] = qstrings.get(1).split(":");

                        String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + ";";

                        System.out.println(cql);

                        ResultSet result = session.execute(cql);

                        for (Row row : result) {

                            if (row.isNull(pre[1])) {
                                continue;
                            } else {
                                String ysplit[];
                                String xx = row.getString("subject");
                                String y = row.getString(pre[1]);
                                y = y.replace("|", "*");
                                ysplit = y.split("\\*");
                                String yfin = "";
                                if (y.contains(qstrings.get(2))) {
                                    for (int lis = 0; lis < ysplit.length; lis++) {
                                        if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                            ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                            System.out.println("FISHY-->" + ysplit[lis]);
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

                                            reshold.get(i).add(xx);
                                            results.put(qstrings.get(0), xx);
                                        }
                                        System.out.println(xx + "---------->" + it);
                                        hash.put(qstrings.get(0), xx);
                                        //results.put(qstrings.get(0), xx);

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
                            int lastfound[]=findTheLastOccurence(qstrings.get(0), qstrings.get(2));
                            System.out.println("Lastfound--"+lastfound);
                            for (String c : reshold.get(lastfound[0])) {
                                //String conc=c;
                                String cql = null;
                                boolean flag = false;
                                System.out.println("No variables found. Everything is available");
                                //System.out.println(c);
                                if (c.startsWith(" ")) {
                                    System.out.println("Replaced Space!");
                                    flag = true;
                                    c = c.replaceFirst(" ", "");
                                    //cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + c + "';";
                                }

                                cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject='" + c + "';";



                                System.out.println(cql);
                                ResultSet result = session.execute(cql);
                                if (result.isExhausted()) {
                                    continue;
                                } else {
                                    for (Row row : result) {
                                        if (row.isNull(pre[1])) {
                                            continue;
                                        } else {
                                            String ysplit[];
                                            String xx = row.getString("subject");
                                            String y = row.getString(pre[1]);
                                            y = y.replace("|", "*");

                                            ysplit = y.split("\\*");



                                            String yfin = "";
                                            if (y.contains(qstrings.get(2))) {
                                                System.out.println("Yes it contains---------------------");
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
                                                    System.out.println("What's going-->" + it);
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
                            int lastfound[]=findTheLastOccurence(qstrings.get(0), qstrings.get(2));
                            System.out.println(" Right!"+lastfound[0]);
                            System.out.println("Curr Index:"+lastfound[1]);

                            for (String c : reshold.get(lastfound[0])) {
                                /*if Subject is available as a result of previous query, but object is missing*/
                                // String conc=c;
                                boolean flag = false;
                                System.out.println("Contains a variable with Subject taken from previous query");
                                
                                String cql = null;
                                if (c.startsWith(" ")) {
                                    flag = true;
                                    System.out.println("Replaced Space!");
                                    c = c.replaceFirst(" ", "");
                                    //cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + c + "';";
                                }

                                cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject='" + c + "';";


                                System.out.println(cql);
                                ResultSet result = session.execute(cql);
                                if (result.isExhausted()) {
                                    continue;
                                } else {
                                    for (Row row : result) {
                                        if (row.isNull(pre[1])) {
                                            continue;
                                        } else {
                                            String ysplit[];
                                            String xx = row.getString("subject");
                                            String y = row.getString(pre[1]);
                                            y = y.replace("|", "*");

                                            ysplit = y.split("\\*");


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
                                                        reshold.get(i).add(xx);
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


                        }

                    }
                    break here;
                } else if (tabs.get("object").equals("true") && tabs.get("subject").equals("false")) {
                    /*if the object is missing*/
                    System.out.println("In outermost else");
                    //Comments added!
//                    for (int l = 0; l < qstrings.size(); l++) {
//                        if (qstrings.get(l).contains("\"")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\"", ""));
//                            // System.out.println(qstrings.get(l));
//                        } else if (qstrings.get(l).contains("\n")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\n", ""));
//                            //System.out.println(qstrings.get(l));
//
//                        } else {
//                            continue;
//                        }
//
//                    }
                    // System.out.println(qstrings.get(0));

                    String pre[] = qstrings.get(1).split(":");

                    String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject='" + qstrings.get(0) + "';";
                    System.out.println(cql);
                    ResultSet result = session.execute(cql);

                    for (Row row : result) {

                        if (row.isNull(pre[1])) {
                            continue;
                        } else {
                            String ysplit[];
                            String xx = row.getString("subject");
                            String y = row.getString(pre[1]);
                            //hash.put(qstrings.get(2), y);
                            ysplit = y.split("\\*");
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

                                        reshold.get(i).add(it);
                                        results.put(qstrings.get(2), it);

                                        //System.out.println(" Do your due dilligence");


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

                } else if (tabs.get("subject").equals("true") && tabs.get("object").equals("true")) {
//                    //to be done
//                    System.out.println("Subject and Object are missing!");
//
//                    for (int l = 0; l < qstrings.size(); l++) {
//                        if (qstrings.get(l).contains("\"")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\"", ""));
//                            // System.out.println(qstrings.get(l));
//                        } else if (qstrings.get(l).contains("\n")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\n", ""));
//                            //System.out.println(qstrings.get(l));
//
//                        } else {
//                            continue;
//                        }
//
//                    }
//                    String ysplit[];
//                    String pre[] = qstrings.get(1).split(":");
//                    String cql = "SELECT subject, " + pre[1] + " from " + db + ";";
//                    System.out.println(cql);
//                    ResultSet result = session.execute(cql);
//                    for (Row row : result) {
//                        if (row.isNull(pre[1])) {
//                            continue;
//                        } else {
//                            String xx = row.getString("subject");
//                            String y = row.getString(pre[1]);
//                           // System.out.println("V---->"+y);
//                            //y=y.replace(" ","")
//                            y = y.replace("|", "*");
//                            ysplit = y.split("\\*");
//                            for (int lis = 0; lis < ysplit.length; lis++) {
//                                if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
//                                    ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
//                                }
//                            }
//                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
//                            System.out.println(ysplitlist);
//                            HashSet set = new HashSet(ysplitlist);
//
//                            Iterator iterator = set.iterator();
//                            while (iterator.hasNext()) {
//                                String it = iterator.next().toString();
//                                if (it.contains("null")) {
//                                    System.out.println("FOund");
//                                    continue;
//                                } else {
//                                    if (it.equals("")) {
//                                        System.out.println("Got it");
//                                        continue;
//                                    } else {
//                                        if ((param.size() / 3) - 1 != 0) {
//                                            if (i < (param.size() / 3) - 1) {
//                                                if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2))) {
//                                                    reshold.get(i).add(y);
//                                                    results.put(qstrings.get(2), y);
//                                                    System.out.println("Case 1");
//                                                } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
//                                                    reshold.get(i).add(xx);
//                                                    results.put(qstrings.get(0), xx);
//                                                    System.out.println("Case 2");
//                                                } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2)) && SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
//                                                    System.out.println("Case 3");
//                                                    continue;
//
//                                                } else {
//                                                    System.out.println("Case 4");
//                                                    results.put(qstrings.get(0), xx);
//                                                    results.put(qstrings.get(2), y);
//                                                }
//                                                // System.out.println(" The next query's first varibale is"+SubQueries.get(i+1).get(0));
//                                            } else {
//                                                System.out.println("you screwed up!");
//                                            }
//                                        } else {
//                                            results.put(qstrings.get(0), xx);
//                                            results.put(qstrings.get(2), it);
//                                        }
//
//                                    }
//                                }
//                            //System.out.println(xx);
//                            //hash.put(qstrings.get(2), y);
//
//
//                            // S//ystem.out.println(i);
//                            //System.out.println((param.size()/3)-1);
//                           
//
//
//                            // reshold.get(i).add(y);
//                            // results.put(qstrings.get(2), y);
//
//                        }
//                        }
//
//                    }
                }

            }

        }
    }

    public void printResults() {
        boolean dis = false;
        boolean filter = false;
        ArrayList<Integer> fil = new ArrayList<>();
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        Set<String> keys1 = results.keySet();
        for (String c : find) {
            if (c.equals("*")) {
                for (String key : keys1) {
                    System.out.println("Key = " + key);

                    System.out.println("Values = " + results.get(key));

                }
            } else if (c.equalsIgnoreCase("distinct")) {
                dis = true;
            } else {
                break;
            }
        }

        if (filter == true) {
            System.out.println("Iam going to call evalPrint");
            //evalAndPrint(fil);
        }



        for (String c : find) {
            if (dis) {
                ArrayList<String> distinct = (ArrayList<String>) results.get(c);
                HashSet set = new HashSet(distinct);
                Iterator iterator = set.iterator();
                System.out.println("Key = " + c);
                System.out.println("Values = ");
                while (iterator.hasNext()) {
                    System.out.println(iterator.next().toString());
                }




            } else {
                System.out.println("Key = " + c);
                System.out.println("Values = " + results.get(c));
            }

        }


    }

    public void exMapFill() {
        String expr = "";
        out:
        for (int i = 0; i < fil.size(); i++) {

            int j = fil.get(i) + 1;
            System.out.println("starting from here:"+j);
            here:
            while (j < paramv1.size()) {
                if (!paramv1.get(j).equals(".")) {
                    expr = expr + paramv1.get(j) + " ";
                    System.out.println("you should see something"+expr);
                    System.out.println(paramv1.get(j));

                    j++;
                } else {
                    continue out;
                }


            }
            
        }
        System.out.println("Expression going to split" + expr);
            String ex[] = expr.split(" ");
            for (int k = 0; k < ex.length; k++) {
                System.out.println("Putting" + ex[k] + " TRUE");
                expression.put(ex[k], "true");
            }
        for(Map.Entry<String, String> me:expression.entrySet())
        {
            System.out.println(me.getKey()+":"+me.getValue());
        }

    }

    public String evalAndPrint(String st, String st1) {
        filterops = false;
        String finex = new String();

        out:
        for (int i = 0; i < fil.size(); i++) {

            int j = fil.get(i) + 1;
            String expr = "";
            here:
            while (j < paramv1.size()) {

                if (!paramv1.get(j).equals(".")) {
                    expr = expr + paramv1.get(j) + " ";
                    //finex=expr;
                    System.out.println(paramv1.get(j));
                    if (filtertabs.get(paramv1.get(j)).equals("true")) {

                        System.out.println("It's covered:" + paramv1.get(j));
                        break here;
                    } else if (paramv1.get(j).equals(st)) {
                        System.out.println("Changing" + st + "to TRUE");
                        filtertabs.put(st, "true");
                        // break here;
                    } else if (paramv1.get(j).equals(st1)) {

                        System.out.println("Changing" + st1 + "to TRUE");
                        filtertabs.put(st1, "true");
                        //break here;
                    } else if (j == paramv1.size() - 1) {
                        expr = expr + paramv1.get(j);
                        paramv1.add(".");
                    }

                    System.out.println("Wacth here" + expr);
                    j++;
                } else {
                    finex = expr;
                    if (filtertabs.get(st).equals("true") || filtertabs.get(st1).equals("true")) {
                        System.out.println("WE proceed further from here on!");
                        filterops = true;
                        break out;
                    } else {
                        continue out;
                    }
                }


            }


            // System.out.println("Printing finex here"+finex);

            //stackOps so=new stackOps(expr);
            //so.filterOut();
        }
        return finex;

    }

    private String findFilterVariables(String get, String get0) {
        String expr = new String();


        here:
        for (int i = 0; i < paramv1.size(); i++) {
            if (paramv1.get(i).equalsIgnoreCase("FILTER")) {
                int j = i + 1;
                System.out.println("Found filter at" + i + " And j is" + j);
                out:
                while (j < paramv1.size()) {
                    if (filtertabs.get(paramv1.get(j)).equals("true")) {
                        System.out.println("It's covered:" + filtertabs.get(paramv1.get(j)));
                        break out;
                    } else if (!paramv1.get(j).equals(".")) {
                        System.out.println("Generating Expression");
                        expr = expr + paramv1.get(j) + " ";
                        System.out.println(expr);

                        if (get.equals(paramv1.get(j))) {
                            filtertabs.put(paramv1.get(j), "true");
                        } else if (get0.equals(paramv1.get(j))) {
                            filtertabs.put(paramv1.get(j), "true");
                        }
                        j++;
                    } else {
                        break here;
                    }

                }

            }

        }
        return expr;

    }

    private int[] findTheLastOccurence(String sub, String obj) {
        int lastfound[] =new int[2];
        int x=-1;
        int y=-1;
         for (int xy = 0; xy < param.size()/3; xy++) {

                  if (SubQueries.get(xy).get(0).equalsIgnoreCase("FILTER")) {
                    continue;
                } else if (expression.containsKey(SubQueries.get(xy).get(0))) {
                    //System.out.println("YAYAYAY!!");
                    continue;
                   
                }
                       else if (sub.equals(SubQueries.get(xy).get(0)) && !obj.equals(SubQueries.get(xy).get(2))) {
                            System.out.println("watch"+sub+" and "+obj);
                        System.out.println("watch"+SubQueries.get(xy).get(0)+" and "+SubQueries.get(xy).get(2));
                        //System.out.println(xy);
                            lastfound[0]=xy;

                        } 
                        else {
                            //lastfound=xy;
                            System.out.println("In else"+sub+" and "+obj);
                        System.out.println(SubQueries.get(xy).get(0)+" and "+SubQueries.get(xy).get(2));
                        lastfound[1]=xy;
                            break;
                        }

                    }
         return lastfound;
    }
    private int findTheLastOccurenceOfPredicate(String sub, String obj) {
        int lastfound=0;
         for (int xy = 0; xy < SubQueries.size(); xy++) {


                        if (obj.equals(SubQueries.get(xy).get(2)) && !sub.equals(SubQueries.get(xy).get(0))) {
                            lastfound = xy;

                        } else {
                            //lastfound=xy;
                            break;
                        }

                    }
         return lastfound;
    }
}