/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.util.*;

import com.datastax.driver.core.*;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.syntax.Element;
import java.net.URI;

/**
 *
 * @author SanjayV
 */
class QueryV4 {

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
    static HashMap<Integer, String> exptabs = new HashMap<>();
    static ArrayList<String> expal=new ArrayList<>();
   static MultiMap lastfoundsub=new MultiValueMap();
   static MultiMap lastfoundpred=new MultiValueMap();
   static HashMap<String, String> subtabs = new HashMap<>();
   static HashMap<String, String> predtabs = new HashMap<>();
    
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
//    String queryString="select ?name where {\n" +
//"?person foaf:name ?name .\n" +
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

    public QueryV4(String str) {
        queryString = str;
    }

    public void connecti() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        Metadata metadata = cluster.getMetadata();
        //System.out.println("Cassandra connection established");
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s \n", host.getDatacenter(), host.getAddress(), host.getRack());
            session = cluster.connect();

        }
    }
//
//    public void setQuery() {
//        Scanner stdin = new Scanner(new BufferedInputStream(System.in));
//        //System.out.println(" Enter the Query:");
//        queryString = stdin.nextLine();
//        ////System.out.println(Math.abs(stdin.nextLong() - stdin.nextLong()));
//    }

    public void init() {
        session.execute("USE rdf;");

    }

    public void close() {
        cluster.shutdown();
    }

    public void map(Query query) {

       
        
        Element q = query.getQueryPattern();
        String qstr = q.toString();
        
        qstr = qstr.replace("\n", " n");

        //qstr = StringUtils.replaceEach(qstr, new String[]{"{", "}"}, new String[]{"", ""});
        qstr=qstr.replace("{", "");
        qstr=qstr.replace("}","");
        qstr = qstr.trim().replaceAll("\\s+", " ");
        //qstr=qstr.replace("n", " .n");
        String split[] = qstr.split(" ");
        for (String c : split) 
        {
            
            
            if(c.startsWith("<")&&c.endsWith(">"))
            {
                       // c=StringUtils.replaceEach(c, new String[]{"<", ">"}, new String[]{"", ""});
                c=c.replace("<", "");
                c=c.replace(">", "");
                        
            }
            paramv1.add(c);
            if (c.equals(".") || c.equals("n")) {
                continue;
            } else {
                
                   
               
                    param.add(c);
            }
        }
        
        
        /*
         * Add "false" to every parameter in the query so as to check out during Filter search! 
         * It is used in the tailend part. 
         */
         for (int i = 0; i < param.size(); i++) {

            //System.out.println("Putting flase for:" + param.get(i));
            //filtertabs.put(param.get(i), "false");
            expression.put(param.get(i), "false");
           // exptabs.put(param.get(i), "false");
        }
         
         /*extract subqueries of size 3 from Param ArrayList
          * While doing that accumulate Filter expression in a string (as in [FILTER ( x < y ), x, x]) and 
          * add it to SubQueries AL as a whole string rather than character by character
          * so we could avoid cases in which Filter line is not multiple of 3 in which case
          * the SubQuery AL might get garbled. 
          * 
          * Also the functionility of exMapFill() is merged into this method. 
          * We no longer need it. 
          * Filter check is also done here itself! 
          */
         for (int i = 0; i < param.size() / 3; i++) {

            SubQueries.add(new ArrayList<String>());
        }
        int j = 0;
        for (int i = 0; i < paramv1.size() / 3; i++) {
            int x = 0;
            String exp="";
            
            
            here:
            while (j < paramv1.size()) {
                
                if(paramv1.get(j).equals(".")||paramv1.get(j).equals("n"))
                {
                    //System.out.println("It does");
                    j++;
                continue here;
                }
                
                if (x != 3) {
//                    if(param.get(j).equals("n"))
//                {
//                    //System.out.println("It is going");
//                    continue here;
//                }
                    if(expression.get(paramv1.get(j)).equals("true"))
                    {
                        //System.out.println("caught here");
                         SubQueries.get(i).add("xox");
                         j++;
                         x++;
                         continue here;
                    }
                       
                   else if(paramv1.get(j).contains("FILTER"))
                    {
                        filter=true;
                        fil.add(i);
                        //System.out.println("Filter is there at"+j);
                        int y=j+1;
                       
                        exp="FILTER"+" ";
                        while(!paramv1.get(y).equals("n"))
                        {
                            //System.out.println("going in here~");
                            
                            exp=exp+paramv1.get(y)+" ";
                            //System.out.println("Here-->"+exp);
                            //System.out.println("putting"+paramv1.get(y)+"to true");
                            expression.put(paramv1.get(y), "true");
                            
                            y++;
                        }
                        expal.add(exp);
                        exptabs.put(i, exp);
                        filtertabs.put(exp, "false");
                        SubQueries.get(i).add(exp);
                       j++;
                       x++;
                        continue here;
                    }
                    else
                   {
                       //System.out.println("hey");
                    SubQueries.get(i).add(paramv1.get(j));
                    j++;
                    x++;
                   }
                } else {
                    break here;
                }
            }
        }
        
        // This operation is used to keep tabs on all subjects and objects for ..umm reasons unknown. Not yet.
        for(int x=0;x<SubQueries.size();x++)
        {
            subtabs.put(SubQueries.get(x).get(0), "true");
            //System.out.println(SubQueries.get(x).get(0)+"-->"+SubQueries.get(x).get(2));
            predtabs.put(SubQueries.get(x).get(2), "true");
            
        }
        //System.out.println(SubQueries);


        //System.out.println(param);
        //System.out.println(paramv1);


    }
    
    
    /*Alright I'll lay the strategy out for this operation.
         * -------STRATEGY------------------------------------
         * 
         * V4 uses JenaARQ to parse the query string out and lay it out for me so I could divide it properly. 
         * This is done to eliminate the errors caused by mere whitespaces and /n lines. It does a great job. 
         * There is a tradeoff though. It increases the elapsed time by 1 second. Can't help it. 
         * 
         * 
         * The idea is to divide the SPARQL meat part into several subqueries of size 3
         * I've already extracted the meat part into SubQueries AL 
         * Now, each element in SubQueries AL is an AL itself. And each element in SubQueries AL also represents a line of SPARQL inner query
         * For each line I use an AL to store the result of executing the query of that particular line. 
         * This AL is used to fetch the results of the previous lines and substitute in the next line of inner query as input data
         * I keep track of all such individual lists with AL(AL). 
         * Briefly, each line of SPARQL meat being an inner query, the query is transformed into CQL and is executed against cassandra. 
         * The results are fetched and stored in AL(AL). With each AL corresponding to a particular line
         * Now, the reults stored in AL(AL) is a temporary result, meaning that changes as the looping continues. 
         * To keep track of final results that are to be printed out, I use a HashMap to store the result of each line. 
         * The key being the variable part of that query (?x) 
         * if this variable reappears again in the subsequent queries, the the previous result(ie the value) of that variable (key) is removed from the HashMap
         * In case of reapperances like above, it is important to find the lastoccurence of that subject/predicate so we could take the correct-
         * resultset corresponding to that particular line. For this purpose, I call findthelastoccurence() method which gives me the last occurence index-
         * of that particular varibale so I can take the resultset present in that particular index.
         * In this way, by the time the loop exits, the HashMap will only contain variables with final results which can be printed out. 
         * 
         * ----IDENTIFICATION OF MISSING VARIABLES-----
         * 
         * You can decide the type of CQL only after you know what variables are missing in the SPARQL query. 
         * For eg: If subject is missing in the query, you need to generate a certain type of CQL query. 
         * If the object is missing, another type and so on. 
         * The point being, CQL query generation part is not static. 
         * 
         * For this purpose, I use another HashMap to keep tabs on what Variables are missing.
         * and based on that several strategies are formulated which is fairly straightforward. 
         * 
         * -----EVALUATING FILTER EXPRESSIONS----------
         * 
         * This was the hard part. 
         * To start off, I extract filter expressions ( if it contains one) from the SPARQL query. 
         * This is done in the map() method itself. Like I said, in V4, Filter Expressions are stored as a String ie.as one element in the SQ AL
         * Now, idea is to check the variables in each query as the loop progresses whether any of these variables exist in FILTER expression
         * For that purpose, i'm calling the method checkForFilterOperands() which takes as paramter the subject and the object of the present query
         * and if that variable does contain in the expression, that particular expression is returned and control breaks. 
         * The control is transfered to stackOps() method which is present in another class stack.java which utilises the stack to evaluate-
         * mathematical expressions. 
         * THe subsequent operations are also performed in that class itself and the results are stored in the shared result HashMap. 
         * And the temporary AL of results is also populated. 
         * To insert the results in correct AL, the indices are sent to that method.
         */

    public void executeSubQueries(Query query) {
        //int tabs=0;

//        if (filter == true) {
//            //System.out.println("I'm calling exmap but it's not doing it;s job");
//           exMapFill();
//        } else {
//            // //System.out.println("I am not even calling exmap");
//        }
        ArrayList<String> resultvars=(ArrayList<String>) query.getResultVars();
        
        
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
                //System.out.println("Check to skip!! " + SubQueries.get(i).get(j));
                if (SubQueries.get(i).get(j).contains("FILTER")) {
                    break here;
                } else if (SubQueries.get(i).get(j).equals("xox")) {
                   
                    break here;
                }
                //tabs++;
                while (x < 3) {
                    ////System.out.println("Hello");
                    qstrings.add(SubQueries.get(i).get(j));
                    j++;
                    x++;
                }
                //else
                //{
                String qsplit[];
                if(qstrings.get(1).contains("#"))
                {
                    qsplit=qstrings.get(1).split("#");
                    String mod="x:"+qsplit[1];
                    qstrings.set(1, mod);
                }
                else
                {
                    URI uri = URI.create(qstrings.get(1));
                    String path = uri.getPath();
                ////System.out.println(path.substring(path.lastIndexOf('/') + 1));
                    String mod="x:"+path.substring(path.lastIndexOf('/') + 1);
                   qstrings.set(1,mod);
                }
                    

                //System.out.println("Look at here!" + qstrings);
                lastfoundsub.put(qstrings.get(0), i);
                lastfoundpred.put(qstrings.get(2), i);
                

                if (i != 0) {
                    if (results.containsKey(qstrings.get(0))) {
                        //System.out.println("REMOVING VALUE FOR KEY:" + qstrings.get(0));
                        results.remove(qstrings.get(0));
                        //results.put(qstrings.get(0), "N");
                    }
                }

                if (filter == true) {
                    //if(qstrings.get(0))
                    String expr = checkForFilterOperands(qstrings.get(0), qstrings.get(2));


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
                        int lastfound = findTheLastOccurence(qstrings.get(0), qstrings.get(2));
                        //System.out.println("LastFound ==" + lastfound);
                        String qstring[] = qstrings.get(1).split(":");
                        //System.out.println("Expression to be fed to FILTEROPS" + expr);
                        stackOps so = new stackOps(expr);
                        so.connecti();
                        so.init();
                        //System.out.println("Going into So.Filterout--->"+lastfound+" "+i+" "+ qstring[1]+ qstrings.get(2)+ qstrings.get(0));
                        so.filterOut(lastfound, i, qstring[1], qstrings.get(2), qstrings.get(0));

                        //System.out.println("Filterops Started");
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
                    ////System.out.println("Look here goddamnit!");
                    tabs.put("subject", "true");

                }


                //System.out.println(tabs.get("subject") + tabs.get("object"));

//                for (int li = 0; li < qstrings.size(); li++) {
//
//                    if (qstrings.get(li).contains("?")) {
//                        ////System.out.println(qstrings.get(li)+" contains ?");
//                        f.add(qstrings.indexOf(qstrings.get(li)));
//
//
//                    } else {
//                        continue;
//                    }
//                }

                /* if(results.containsKey(qstrings.get(0)))
                 {
                 //System.out.println("It does contain it");
                 }
                 else
                 {
                 //System.out.println("Something is WRONG!!");
                 }*/

                /*
                 * 
                 for(int l=0;l<qstrings.size();l++)
                 {
                 if(qstrings.get(l).contains("\""))
                 {
                               
                 qstrings.set(l, qstrings.get(l).replace("\"",""));
                 // //System.out.println(qstrings.get(l));
                 }
                 else if(qstrings.get(l).contains("\n"))
                 {
                 //System.out.println(" THERE!");
                           
                 qstrings.set(l, qstrings.get(l).replace("\n",""));
                 ////System.out.println(qstrings.get(l));
                               
                 }
                 else
                 {
                 continue;
                 }
   
                 }*/

                /*If the subject is missing*/


                if (tabs.get("subject").equals("true") && tabs.get("object").equals("true") && tabs.get("predicate").equals("true")) {
                    //System.out.println("Select * case");
                    String cql = "SELECT * from " + db + ";";
                    ResultSet result = session.execute(cql);
                    ColumnDefinitions c = result.getColumnDefinitions();
                    int count = c.size();
                    for (Row row : result) {
                        //System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        for (int ix = 0; ix < count; ix++) {
                            if (row.isNull(ix)) {
                                continue;
                            } else {

                                String val = row.getString(ix);
                                //System.out.println(val);
                                //results.put(val, c);

                            }
                        }

                    }
                } else if (tabs.get("subject").equals("true")) {

                    /*Check whether the Subject is in the HashMap as key*/
                    if (tabs.get("object").equals("true") && !hash.containsKey(qstrings.get(0)) && !hash.containsKey(qstrings.get(2))) {
                        //System.out.println("Subject and Object are missing!");
//Comments added!
//                        for (int l = 0; l < qstrings.size(); l++) {
//                            if (qstrings.get(l).contains("\"")) {
//
//                                qstrings.set(l, qstrings.get(l).replace("\"", ""));
//                                // //System.out.println(qstrings.get(l));
//                            } else if (qstrings.get(l).contains("\n")) {
//
//                                qstrings.set(l, qstrings.get(l).replace("\n", ""));
//                                ////System.out.println(qstrings.get(l));
//
//                            } else {
//                                continue;
//                            }
//
//                        }
                        String ysplit[];
                        String pre[] = qstrings.get(1).split(":");
                        String cql = "SELECT subject, " + pre[1] + " from " + db + ";";
                        //System.out.println(cql);
                        ResultSet result = session.execute(cql);
                        for (Row row : result) {
                            if (row.isNull(pre[1])) {
                                continue;
                            } else {
                                String xx = row.getString("subject");
                                String y = row.getString(pre[1]);
                                // //System.out.println("V---->"+y);
                                y = y.replace("|", "*");
                                ysplit = y.split("\\*");
                                for (int lis = 0; lis < ysplit.length; lis++) {
                                    if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                        ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                    }
                                }
                                ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                //System.out.println(ysplitlist);
                                HashSet set = new HashSet(ysplitlist);

                                Iterator iterator = set.iterator();
                                while (iterator.hasNext()) {
                                    String it = iterator.next().toString();
                                    if (it.contains("null")) {
                                        //System.out.println("FOund");
                                        continue;
                                    } else {
                                        if (it.equals("")) {
                                            //System.out.println("Got it");
                                            continue;
                                        } else {
//                                            if ((param.size() / 3) - 1 != 0) {
//                                                if (i < (param.size() / 3) - 1) {
//                                                    if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2))) {
//                                                        reshold.get(i).add(it);
//                                                        results.put(qstrings.get(2), it);
//                                                        hash.put(qstrings.get(2), it);
//                                                        //System.out.println("Case 1");
//                                                    } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
//                                                        reshold.get(i).add(xx);
//                                                        reshold.get(i).add(it);
//                                                        results.put(qstrings.get(0), xx);
//                                                        results.put(qstrings.get(2), it);
//                                                        hash.put(qstrings.get(2), it);
//                                                        hash.put(qstrings.get(0), xx);
//                                                        //System.out.println("Case 2");
//                                                    } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2)) && SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
//                                                        //System.out.println("Case 3");
//                                                        continue;
//
//                                                    } else {
//                                                        //System.out.println("Case 4");
//                                                        results.put(qstrings.get(0), xx);
//                                                        results.put(qstrings.get(2), it);
//                                                        hash.put(qstrings.get(2), it);
//                                                        hash.put(qstrings.get(0), xx);
//                                                    }
//                                                    // //System.out.println(" The next query's first varibale is"+SubQueries.get(i+1).get(0));
//                                                } else {
//                                                    //System.out.println("you screwed up!");
//                                                }
//                                            } else {
//                                                results.put(qstrings.get(0), xx);
//                                                results.put(qstrings.get(2), it);
//                                                hash.put(qstrings.get(2), it);
//                                                hash.put(qstrings.get(0), xx);
//                                            }
                                            //For this reason!
                                            if(subtabs.containsKey(qstrings.get(0))&&!subtabs.containsKey(qstrings.get(2)))
                                            {
                                                if(resultvars.contains(qstrings.get(2).replace("?", "")))
                                                    results.put(qstrings.get(2), it);
                                                
                                                reshold.get(i).add(xx);
                                                results.put(qstrings.get(0), xx);
                                                hash.put(qstrings.get(0), xx);
                                                System.out.println("Case 1");
                                            }
                                            else if(subtabs.containsKey(qstrings.get(2)))
                                            {
                                                if(resultvars.contains(qstrings.get(0).replace("?", "")))
                                                    results.put(qstrings.get(0), xx);
                                                reshold.get(i).add(it);
                                                System.out.println("putting "+it+" in "+qstrings.get(2));
                                                results.put(qstrings.get(2), it);
                                                hash.put(qstrings.get(2), it);
                                                System.out.println("Case 2");
                                            }
                                            else if(!subtabs.containsKey(qstrings.get(0))&&predtabs.containsKey(qstrings.get(2)))
                                            {
                                                
                                            }

                                        }
                                    }
                                    ////System.out.println(xx);
                                    //hash.put(qstrings.get(2), y);


                                    // S//ystem.out.println(i);
                                    ////System.out.println((param.size()/3)-1);



                                    // reshold.get(i).add(y);
                                    // results.put(qstrings.get(2), y);

                                }
                            }

                        }

                    } else if (tabs.get("object").equals("true") && !hash.containsKey(qstrings.get(0)) && hash.containsKey(qstrings.get(2))) {

                        //System.out.println("Subject is missing; object is taken from previous query!");
                        String pre[] = qstrings.get(1).split(":");
                        String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + ";";
                        //System.out.println(cql);
                        ResultSet result = session.execute(cql);
                        int lastfound = findTheLastOccurenceOfPredicate(qstrings.get(0), qstrings.get(2));
                        //System.out.println("Lastfound--" + lastfound);
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
                                                //System.out.println("FISHY-->" + ysplit[lis]);
                                            }
                                        }
                                        ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                        // //System.out.println(ysplitlist);
                                        HashSet set = new HashSet(ysplitlist);

                                        Iterator iterator = set.iterator();
                                        while (iterator.hasNext()) {
                                            String it = iterator.next().toString();
                                            if (it.contains("null")) {
                                                //System.out.println("FOund");
                                                continue;
                                            } else if (it.contains(c)) {

                                                reshold.get(i).add(it);
                                                results.put(qstrings.get(2), it);
                                                results.put(qstrings.get(0), xx);
                                            }
                                            //System.out.println(xx + "---------->" + it);
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


                    } else if (!hash.containsKey(qstrings.get(0))) {
                        //System.out.println("Subject is missing; everything else is available");

                        String pre[] = qstrings.get(1).split(":");

                        String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + ";";

                        //System.out.println(cql);

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
                                            //System.out.println("FISHY-->" + ysplit[lis]);
                                        }
                                    }
                                    ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                    // //System.out.println(ysplitlist);
                                    HashSet set = new HashSet(ysplitlist);

                                    Iterator iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        String it = iterator.next().toString();
                                        if (it.contains("null")) {
                                            //System.out.println("FOund");
                                            continue;
                                        } else if (it.contains(qstrings.get(2))) {

                                            reshold.get(i).add(xx);
                                            results.put(qstrings.get(0), xx);
                                        }
                                        //System.out.println(xx + "---------->" + it);
                                        hash.put(qstrings.get(0), xx);
                                        //results.put(qstrings.get(0), xx);

                                    }




                                } else {
                                    continue;
                                }


                            }



                            ////System.out.println(x+"   "+y);
                        }

                        ////System.out.println(results);

                    } else {
                        /*if Subject is available in HashMap as a result of previous query*/
                        //System.out.println("Subject Available (Previous Query) Object missing");
                        //System.out.println("Finally");
                        String pre[] = qstrings.get(1).split(":");
                        List<String> list = (List<String>) hash.get(qstrings.get(0));
                        if (!qstrings.get(2).contains("?")) {
                            /*If there are no variables in this Line*/
                            int lastfound = findTheLastOccurence(qstrings.get(0), qstrings.get(2));
                            //System.out.println("Lastfound--"+lastfound);
                            for (String c : reshold.get(lastfound)) {
                                //String conc=c;
                                String cql = null;
                                boolean flag = false;
                                //System.out.println("No variables found. Everything is available");
                                ////System.out.println(c);
                                if (c.startsWith(" ")) {
                                    //System.out.println("Replaced Space!");
                                    flag = true;
                                    c = c.replaceFirst(" ", "");
                                    //cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + c + "';";
                                }

                                cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject='" + c + "';";



                                //System.out.println(cql);
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
                                                //System.out.println("Yes it contains---------------------");
                                                for (int lis = 0; lis < ysplit.length; lis++) {
                                                    if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                                        ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                                    }
                                                }
                                                ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                                // //System.out.println(ysplitlist);
                                                HashSet set = new HashSet(ysplitlist);

                                                Iterator iterator = set.iterator();
                                                while (iterator.hasNext()) {
                                                    String it = iterator.next().toString();
                                                    //System.out.println("What's going-->" + it);
                                                    if (it.contains("null")) {
                                                        //System.out.println("FOund");
                                                        continue;
                                                    } else if (it.contains(qstrings.get(2))) {
                                                        //System.out.println(xx + "---------->" + it);
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
                                            ////System.out.println("Hy");

                                            ////System.out.println(xx+"------>"+y);
                                        }
                                    }

                                }
                            }

                        } else {
                            int lastfound = findTheLastOccurence(qstrings.get(0), qstrings.get(2));
                            //System.out.println(" Right!"+lastfound);
                            //System.out.println("Curr Index:"+i);

                            for (String c : reshold.get(lastfound)) {
                                /*if Subject is available as a result of previous query, but object is missing*/
                                // String conc=c;
                                boolean flag = false;
                                //System.out.println("Contains a variable with Subject taken from previous query");

                                String cql = null;
                                if (c.startsWith(" ")) {
                                    flag = true;
                                    //System.out.println("Replaced Space!");
                                    c = c.replaceFirst(" ", "");
                                    //cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject=' " + c + "';";
                                }

                                cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject='" + c + "';";


                                //System.out.println(cql);
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
                                            //System.out.println(ysplitlist);
                                            HashSet set = new HashSet(ysplitlist);

                                            Iterator iterator = set.iterator();
                                            while (iterator.hasNext()) {
                                                String it = iterator.next().toString();
                                                if (it.contains("null")) {
                                                    //System.out.println("FOund");
                                                    continue;
                                                } else {
                                                    if (it.equals("")) {
                                                        continue;
                                                    } else {
                                                        //System.out.println("Correct");
                                                        //System.out.println(xx + "---------->" + it);
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


                                            ////System.out.println("Hy");

                                            ////System.out.println(xx+"------>"+y);
                                        }
                                    }

                                }
                            }


                        }

                    }
                    break here;
                } else if (tabs.get("object").equals("true") && tabs.get("subject").equals("false")) {
                    /*if the object is missing*/
                    //System.out.println("In outermost else");
                    //Comments added!
//                    for (int l = 0; l < qstrings.size(); l++) {
//                        if (qstrings.get(l).contains("\"")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\"", ""));
//                            // //System.out.println(qstrings.get(l));
//                        } else if (qstrings.get(l).contains("\n")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\n", ""));
//                            ////System.out.println(qstrings.get(l));
//
//                        } else {
//                            continue;
//                        }
//
//                    }
                    // //System.out.println(qstrings.get(0));

                    String pre[] = qstrings.get(1).split(":");

                    String cql = "SELECT subject" + "," + pre[1] + " FROM " + db + " WHERE subject='" + qstrings.get(0) + "';";
                    //System.out.println(cql);
                    ResultSet result = session.execute(cql);

                    for (Row row : result) {

                        if (row.isNull(pre[1])) {
                            continue;
                        } else {
                            String ysplit[];
                            String xx = row.getString("subject");
                            String y = row.getString(pre[1]);
                            y = y.replace("|", "*");
                            //hash.put(qstrings.get(2), y);
                            ysplit = y.split("\\*");
                            String yfin = "";
                            for (int lis = 0; lis < ysplit.length; lis++) {
                                if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                    ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                }
                            }
                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                            //System.out.println(ysplitlist);
                            HashSet set = new HashSet(ysplitlist);

                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                String it = iterator.next().toString();
                                if (it.contains("null")) {
                                    //System.out.println("FOund");
                                    continue;
                                } else {
                                    if (it.equals("")) {
                                        //System.out.println("Got it");
                                        continue;
                                    } else {
                                        //System.out.println(xx + "---------->" + it);
                                        //hash.put(qstrings.get(2), qstrings.get(0));
                                        String declustered[] = null;


                                        reshold.get(i).add(it);
                                        results.put(qstrings.get(2), it);

                                        ////System.out.println(" Do your due dilligence");


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
//                    //System.out.println("Subject and Object are missing!");
//
//                    for (int l = 0; l < qstrings.size(); l++) {
//                        if (qstrings.get(l).contains("\"")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\"", ""));
//                            // //System.out.println(qstrings.get(l));
//                        } else if (qstrings.get(l).contains("\n")) {
//
//                            qstrings.set(l, qstrings.get(l).replace("\n", ""));
//                            ////System.out.println(qstrings.get(l));
//
//                        } else {
//                            continue;
//                        }
//
//                    }
//                    String ysplit[];
//                    String pre[] = qstrings.get(1).split(":");
//                    String cql = "SELECT subject, " + pre[1] + " from " + db + ";";
//                    //System.out.println(cql);
//                    ResultSet result = session.execute(cql);
//                    for (Row row : result) {
//                        if (row.isNull(pre[1])) {
//                            continue;
//                        } else {
//                            String xx = row.getString("subject");
//                            String y = row.getString(pre[1]);
//                           // //System.out.println("V---->"+y);
//                            //y=y.replace(" ","")
//                            y = y.replace("|", "*");
//                            ysplit = y.split("\\*");
//                            for (int lis = 0; lis < ysplit.length; lis++) {
//                                if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
//                                    ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
//                                }
//                            }
//                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
//                            //System.out.println(ysplitlist);
//                            HashSet set = new HashSet(ysplitlist);
//
//                            Iterator iterator = set.iterator();
//                            while (iterator.hasNext()) {
//                                String it = iterator.next().toString();
//                                if (it.contains("null")) {
//                                    //System.out.println("FOund");
//                                    continue;
//                                } else {
//                                    if (it.equals("")) {
//                                        //System.out.println("Got it");
//                                        continue;
//                                    } else {
//                                        if ((param.size() / 3) - 1 != 0) {
//                                            if (i < (param.size() / 3) - 1) {
//                                                if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2))) {
//                                                    reshold.get(i).add(y);
//                                                    results.put(qstrings.get(2), y);
//                                                    //System.out.println("Case 1");
//                                                } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
//                                                    reshold.get(i).add(xx);
//                                                    results.put(qstrings.get(0), xx);
//                                                    //System.out.println("Case 2");
//                                                } else if (SubQueries.get(i + 1).get(0).equals(qstrings.get(2)) && SubQueries.get(i + 1).get(0).equals(qstrings.get(0))) {
//                                                    //System.out.println("Case 3");
//                                                    continue;
//
//                                                } else {
//                                                    //System.out.println("Case 4");
//                                                    results.put(qstrings.get(0), xx);
//                                                    results.put(qstrings.get(2), y);
//                                                }
//                                                // //System.out.println(" The next query's first varibale is"+SubQueries.get(i+1).get(0));
//                                            } else {
//                                                //System.out.println("you screwed up!");
//                                            }
//                                        } else {
//                                            results.put(qstrings.get(0), xx);
//                                            results.put(qstrings.get(2), it);
//                                        }
//
//                                    }
//                                }
//                            ////System.out.println(xx);
//                            //hash.put(qstrings.get(2), y);
//
//
//                            // S//ystem.out.println(i);
//                            ////System.out.println((param.size()/3)-1);
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

    public MultiMap printResults(Query query) {
        
       // //System.out.println("In print results~!!");
        boolean dis = false;
        boolean filter = false;
        ArrayList<Integer> fil = new ArrayList<>();
        MultiMap tosend = new MultiValueMap();
        //System.out.println("----------------------------------------------------------------------------------------------------------------------");
        Set<String> keys1 = results.keySet();
        find=(ArrayList<String>) query.getResultVars();
        
        
        /*
         * Resultset mapping is done here -----------------
         */
        
        out: for (Map.Entry<String, String> me : predtabs.entrySet()) {
            if(!subtabs.containsKey(me.getKey()))
            {
                String orphan=me.getKey().replace("?", "");
                for(String x:expal)
                {
                    if(x.contains(orphan))
                        continue out;
                }
                
                if(find.contains(me.getKey().replace("?", "")))
                {
                    
                    String lead="?"+find.get(0);
                    
                    ArrayList<String> temp=(ArrayList<String>) results.get(lead);
                    for(String c:temp)
                    {
                        String cql="SELECT "+orphan+ " FROM " + db + " WHERE subject='" + c + "';";
                        ResultSet result=session.execute(cql);
                        for (Row row : result) {

                        if (row.isNull(orphan)) {
                            continue;
                        } else {
                            results.remove(me.getKey());
                            String ysplit[];
                            //String xx = row.getString(me.getKey());
                            String y = row.getString(orphan);
                            y = y.replace("|", "*");
                            //hash.put(qstrings.get(2), y);
                            ysplit = y.split("\\*");
                            
                           
                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                            //System.out.println(ysplitlist);
                            HashSet set = new HashSet(ysplitlist);

                            Iterator iterator = set.iterator();
                            while (iterator.hasNext()) {
                                String it = iterator.next().toString();
                                if (it.contains("null")) {
                                    //System.out.println("FOund");
                                    continue;
                                } else {
                                    if (it.equals("")) {
                                        //System.out.println("Got it");
                                        continue;
                                    } else {
                                        //System.out.println(xx + "---------->" + it);
                                        //hash.put(qstrings.get(2), qstrings.get(0));
                                        System.out.println(me.getKey()+"---->"+it);
                                        results.put(me.getKey(), it);
                                    }
                                }
                            }
                        }
                        }
                    }
                    
                }
               
            }
             else
                    continue;
        }
        if(query.isQueryResultStar())
        {
             for (String key : keys1) {
                 key="?"+key;
                    //System.out.println("Key = " + key);

                    //System.out.println("Values = " + results.get(key));
                    tosend.put(key, results.get(key));

                }
        }
                
       
        for (String c : find) {
            c="?"+c;
            if (dis) {
                ArrayList<String> distinct = (ArrayList<String>) results.get(c);
                HashSet set = new HashSet(distinct);
                Iterator iterator = set.iterator();
                //System.out.println("Key = " + c);
                //System.out.println("Values = ");
                while (iterator.hasNext()) {
                    //System.out.println(iterator.next().toString());
                }




            } else {
                System.out.println("Key = " + c);
                System.out.println("Values = " + results.get(c));
                tosend.put(c, results.get(c));
            }

        }

        return tosend;


    }

    public void exMapFill() {
        String expr = "";
        out:
        for (int i = 0; i < fil.size(); i++) {

            int j = fil.get(i) + 1;
            //System.out.println("starting from here:"+j);
            here:
            while (j < paramv1.size()) {
                if (!paramv1.get(j).equals(".")) {
                    expr = expr + paramv1.get(j) + " ";
                    //System.out.println("you should see something"+expr);
                    //System.out.println(paramv1.get(j));

                    j++;
                } else {
                    continue out;
                }


            }

        }
        //System.out.println("Expression going to split" + expr);
        String ex[] = expr.split(" ");
        for (int k = 0; k < ex.length; k++) {
            //System.out.println("Putting" + ex[k] + " TRUE");
            expression.put(ex[k], "true");
        }
        for (Map.Entry<String, String> me : expression.entrySet()) {
            //System.out.println(me.getKey()+":"+me.getValue());
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
                    //System.out.println(paramv1.get(j));
                    if (filtertabs.get(paramv1.get(j)).equals("true")) {

                        //System.out.println("It's covered:" + paramv1.get(j));
                        break here;
                    } else if (paramv1.get(j).equals(st)) {
                        //System.out.println("Changing" + st + "to TRUE");
                        filtertabs.put(st, "true");
                        // break here;
                    } else if (paramv1.get(j).equals(st1)) {

                        //System.out.println("Changing" + st1 + "to TRUE");
                        filtertabs.put(st1, "true");
                        //break here;
                    } else if (j == paramv1.size() - 1) {
                        expr = expr + paramv1.get(j);
                        paramv1.add(".");
                    }

                    //System.out.println("Wacth here" + expr);
                    j++;
                } else {
                    finex = expr;
                    if (filtertabs.get(st).equals("true") || filtertabs.get(st1).equals("true")) {
                        //System.out.println("WE proceed further from here on!");
                        filterops = true;
                        break out;
                    } else {
                        continue out;
                    }
                }

                

            }


            // //System.out.println("Printing finex here"+finex);

            //stackOps so=new stackOps(expr);
            //so.filterOut();
        }
        return finex;

    }
    
    public String checkForFilterOperands(String st, String st1)
    {
        
        filterops=false;
        String exp=new String();
        
        for(int i=0;i<fil.size();i++)
        {
             exp=exptabs.get(fil.get(i));
             //System.out.println(exp);
            if(filtertabs.get(exp).equals("true"))
            {
                //System.out.println("it's covered");
                continue;
            }
            else if(exp.contains(st)||exp.contains(st1))
            {
                filtertabs.put(exp, "true");
                //System.out.println("WE proceed further from here on!");
                filterops=true;
                break;
                
            }
        }
        
        
        return exp;
        
    }

    private int findTheLastOccurence(String sub, String obj) {
//        int lastfound[] = new int[2];
//        int x = -1;
//        int y = -1;
//        for (int xy = 0; xy < param.size() / 3; xy++) {
//            //System.out.println(sub+"****"+obj);
//            //System.out.println(SubQueries.get(xy).get(0)+"-----"+SubQueries.get(xy).get(2));
//            if (SubQueries.get(xy).get(0).contains("FILTER")) {
//                continue;
//            } else if (SubQueries.get(xy).get(0).equals("xox")) {
//                ////System.out.println("YAYAYAY!!");
//                continue;
//
//            } else if (sub.equals(SubQueries.get(xy).get(0)) && !obj.equals(SubQueries.get(xy).get(2))) {
//                //System.out.println("watch"+sub+" and "+obj);
//                //System.out.println("watch"+SubQueries.get(xy).get(0)+" and "+SubQueries.get(xy).get(2));
//                ////System.out.println(xy);
//                lastfound[0] = xy;
//
//            } else {
//                //lastfound=xy;
//                //System.out.println("In else"+sub+" and "+obj);
//                //System.out.println(SubQueries.get(xy).get(0)+" and "+SubQueries.get(xy).get(2));
//                lastfound[1] = xy;
//                break;
//            }
//
//        }
        int lastfound = 0;
        
        ArrayList<Integer> list1=(ArrayList<Integer>) lastfoundsub.get(sub);
        ArrayList<Integer> list2=(ArrayList<Integer>) lastfoundpred.get(sub);
        
        if(list1.size()==1)
        {
            if(list2.size()==1)
            {
                lastfound=list2.get(list2.size()-1);
            }
            else
                lastfound=list2.get(list2.size()-2);
        }
        else
            lastfound=list1.get(list1.size()-2);
//        
//        if(lastfoundsub.containsKey(sub))
//        {
//            
//            ArrayList<Integer> list=(ArrayList<Integer>) lastfoundsub.get(sub);
//            //System.out.println("in sub"+list.size());
//            if(list.size()==1)
//            {
//                //System.out.println("not?");
//            if(lastfoundpred.containsKey(sub))
//                {
//                    list=(ArrayList<Integer>) lastfoundpred.get(sub);
//                    lastfound=list.get(list.size()-2);
//                    
//                }
//            else
//                    //System.out.println("soemthing went wrong!");
//            }
//            else
//                lastfound=list.get(list.size()-2);
//        }
//           else if(lastfoundpred.containsKey(sub))
//        {
//            //System.out.println("in pred");
//           ArrayList<Integer> list=(ArrayList<Integer>) lastfoundpred.get(sub);
//            lastfound=list.get(list.size()-2);
//        }
        return lastfound;
    }

    private int findTheLastOccurenceOfPredicate(String sub, String obj) {
        int lastfound = 0;
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

public class AnimalQueryV4 {

    public static void main(String args[]) {

        // start time



 long lStartTime = System.nanoTime();

        String queryString =" PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/>\n" +
"PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                "SELECT *\n"
                + "WHERE { \n"
                + "    \n"
                +"?product rdfs:label ?label .\n"
                + "  ?product rdf:type bsbm-inst:ProductType10 .\n"
                + "?product bsbm:productFeature bsbm-inst:ProductFeature397 .\n"
                + "?product bsbm:productFeature bsbm-inst:ProductFeature380 .\n"
                + "    ?product bsbm:productPropertyNumeric1 ?value1 . \n"
                + "    \n"
                + "    FILTER ( ?value1 < 200 ) .\n"
                + "    ?product bsbm:productPropertyNumeric1 ?value2 .\n"
                + "\n"
                + "FILTER ( ?value2 > 100 ) .\n"
                + "    \n"
                
                + "    ?product rdf:type ?type .\n"
                + "	}";
 
// String queryString="PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer5/>\n" +
//"PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>\n" +
//"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
//"\n" +
//"SELECT ?label ?comment ?producer ?productFeature ?propertyTextual1 ?propertyTextual2 ?propertyTextual3\n" +
//" ?propertyNumeric1 ?propertyNumeric2 ?propertyTextual4 ?propertyTextual5 ?propertyNumeric4 \n" +
//"WHERE {\n" +
//"    bsbm-inst:Product209 rdfs:label ?label .\n" +
//"	bsbm-inst:Product209 rdfs:comment ?comment .\n" +
//"	bsbm-inst:Product209 bsbm:producer ?p .\n" +
//"	?p rdfs:label ?producer .\n" +
//"    bsbm-inst:Product209 dc:publisher ?p . \n" +
//"	bsbm-inst:Product209 bsbm:productFeature ?f .\n" +
//"	?f rdfs:label ?productFeature .\n" +
//"	bsbm-inst:Product209 bsbm:productPropertyTextual1 ?propertyTextual1 .\n" +
//"	bsbm-inst:Product209 bsbm:productPropertyTextual2 ?propertyTextual2 .\n" +
//"    bsbm-inst:Product209 bsbm:productPropertyTextual3 ?propertyTextual3 .\n" +
//"	bsbm-inst:Product209 bsbm:productPropertyNumeric1 ?propertyNumeric1 .\n" +
//"	bsbm-inst:Product209 bsbm:productPropertyNumeric2 ?propertyNumeric2 .\n" +
//"}";
        Query query = QueryFactory.create(queryString);
//        String queryString="SELECT ?homepage\n" +
//"\n" +
//"WHERE {\n" +
//"    http://www.w3.org/People/Berners-Lee/card#i foaf:knows ?known .\n" +
//"    ?known foaf:homepage ?homepage .\n" +
//"}";
//            String queryString =  "select ?product ?label ?value1 ?value2 ?pro where {\n"
//            + "?product rdf:type http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType10 .\n"
//            + "?product bsbm:productFeature http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature397 .\n"
//            + "?product bsbm:productFeature http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductFeature380 .\n"
//            + "?product bsbm:productPropertyNumeric1 ?value1 .\n"
//            + "FILTER ( ?value1 > 500 ) .\n"
//            + "?product bsbm:productPropertyNumeric1 ?value2 .\n"
//            + "FILTER ( ?value2 < 900 ) .\n"
//            + "?product rdfs:label ?label .\n"
//            + "};";
        QueryV4 m = new QueryV4(queryString);

        m.connecti();
        m.init();
        // m.setQuery();
       
        m.map(query);
         

 
         m.executeSubQueries(query);
         m.printResults(query);
         
         long lEndTime = System.nanoTime();
     
         long difference = lEndTime - lStartTime;
  

       
        
        //System.out.println("Elapsed nanoseconds: " + difference);
        System.out.println("Elapsed Seconds: " + difference * 0.000000001);



        // //System.out.println(reshold.get(reshold.size()-1));
        ////System.out.println(reshold.get(reshold.size()-1).size());

        m.close();

    }
}
