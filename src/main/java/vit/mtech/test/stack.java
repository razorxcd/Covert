/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.util.*;
//import vit.mtech.test.AnimalQueryV1;
import com.datastax.driver.core.*;


/**
 *
 * @author SanjayV
 */
class stackOps extends QueryV4{

    
    
    static int x = 0;
    static int obcount = 0;
    static int cbcount = 0;
    static String ex[];
    String expr=new String();
    Stack st=new Stack();
    
    
    Cluster clust;
    Session sess;

    @Override
   
    public void connecti() {
        clust = Cluster.builder().addContactPoint("localhost").build();
        Metadata metadata = clust.getMetadata();
        //System.out.println("Cassandra connection established");
        System.out.printf("Connected to clust: %s\n",
                metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s \n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
            sess = clust.connect();

        }
    }

   

    @Override
    public void init() {
        sess.execute("USE rdf;");

    }

    @Override
    public void close() {
        clust.shutdown();
    }


    public stackOps(String e)
    {
       super(e);
       expr=e;
        
        
    }
    
    


    public void Strip(Stack st, int i, int resindex,int currindex, String pred, String object, String subject) {
        //To change body of generated methods, choose Tools | Templates.
        String basic = "";
        int obindex = 0;
        int tempo = 0;
        int tempc = 0;
        int y = 0;
        x++;
        String temp = "?temp" + x;
        //System.out.println("In Strip Section Stack Print"+st);
        //System.out.println("In Strip Printing Size" + st.size());
        for (int j = 0; j < st.size(); j++) {
            if (st.get(j).equals("(")) {
                tempo++;
            } else if (st.get(j).equals(")")) {
                tempc++;
            }
        }
        if ((tempo == tempc)&&st.size()>5) {
            String val;
           // x++;
            val = "?temp" + x;
            for (int j = st.size() - 2; j >= ((st.size() - 2) - 2); j--) {

                basic = basic + st.get(j);
                //System.out.println("Basic Finally" + basic);
            }
            if(basic.contains(")"))
                {
                    basic=basic.substring(0,basic.length()-1);
                }
            if (basic.contains(">")) {
                String b[] = basic.split(">");
                //System.out.println("DO: "+b[0] + ">" + b[1]);
            }
            else if(basic.contains("<"))
            {
                String b[]=basic.split("<");
                //System.out.println("DO: "+b[0] + "<" + b[1]);
            }
            else if(basic.contains("+"))
            {
                 String b[]=basic.split("\\+");
                //System.out.println("DO: "+b[0] + "+" + b[1]);
            }
            else if(basic.contains("-"))
            {
                String b[]=basic.split("\\-");
                //System.out.println("DO: "+b[0] + "-" + b[1]);
            }
            else if(basic.contains("&&"))
            {
                String b[]=basic.split("&&");
                //System.out.println("DO: "+b[0] + "&&" + b[1]);
            }
            int size = ((st.size() - 2) - 2);
            for (int j = st.size() - 2; j >= size; j--) {
                st.pop();
            }
            st.push(val);
            st.push(")");
            if (st.size() == 5) {
                Strip(st, i, resindex, currindex, pred, object, subject);
            }

        } else {
            for (int j = st.size() - 1; j >= 1; j--) {
                if (st.get(j).equals("(")) {

                    //System.out.println("Got opening at" + j);
                    obindex = j;
                    break;
                }
            }
            for (int j = obindex + 1; j < st.size(); j++) {
                ////System.out.println(st.get(obindex+1));

                basic = basic + st.get(j);
                //System.out.println("Printing " + basic);
            }
            if(basic.contains(")"))
            {
                basic=basic.substring(0, basic.length()-1);
            }
            if (basic.contains(">")) {
                String b[] = basic.split(">");
                //System.out.println("DO: "+b[0] + ">" + b[1]);
                lessthanops(b[0],b[1],resindex,currindex, pred, object, subject, "greater");
            }
            else if(basic.contains("<"))
            {
                String b[]=basic.split("<");
                //System.out.println("DO"+b[0] + "<" + b[1]);
                lessthanops(b[0],b[1],resindex,currindex, pred, object, subject, "lesser");
            }
            else if(basic.contains("+"))
            {
                 String b[]=basic.split("\\+");
                //System.out.println("DO: "+b[0] + "+" + b[1]);
            }
            else if(basic.contains("-"))
            {
                String b[]=basic.split("\\-");
                //System.out.println("DO: "+b[0] + "-" + b[1]);
            }
            else if(basic.contains("&&"))
            {
                String b[]=basic.split("&&");
                //System.out.println("DO: "+b[0] + "&&" + b[1]);
            }

            //System.out.println("Before Strip" + st);
            int size = st.size() - 1;
            for (int j = size; j >= obindex; j--) {
                st.pop();
            }
            cbcount--;
            obcount--;
            st.push(temp);
            //st.push(")");
            //System.out.println("In strip" + st);


        }
    }
    public void  filterOut(int resindex,int currindex, String pred, String object, String subject)
    {
        //String expr = "FILTER ( ?simProperty2 < ( ?origProperty2 + ( ?y + ( ?xy - 50 ) ) && ?simProperty2 > ( ?origProperty2 – 220 ) )";
        
        //System.out.println(expr);
        ex = expr.split(" ");
        
        
        
        //System.out.println("Im going to replace the results in"+ (resindex+1));

        for (int i = 0; i < ex.length; i++) {
            if (ex[i].equals("(")) {
                //obcount++;
            } else if (ex[i].equals(")")) {
                //cbcount++;
            }
        }


        here:
        for (int i = 0; i < ex.length; i++) {



            if (ex[i].equalsIgnoreCase("FILTER")) {
                continue;
            }
            if (!ex[i].equals(")")) {
                if (ex[i].equals("(")) {
                    obcount++;
                }

                st.push(ex[i]);
                //System.out.println(st);
            } else {
                st.push(ex[i]);
                cbcount++;
                //System.out.println(st);
                //System.out.println(" ) has reached");

                //st.push(ex[i]);
                String basic = "";
                if (st.size() == 5) {
                    Strip(st, i, resindex, currindex, pred, object, subject);
                }
                while (st.size() > 4 && (obcount != cbcount)) {
                    //System.out.println("Stripping it" + ex[i]);

                    //System.out.println("Obcount" + obcount);

                    if (i == ex.length - 2) {

                        if (obcount >= 1) {
                            //System.out.println("Si it working");
                            Strip(st, i, resindex, currindex, pred, object, subject);
                        }
                        //System.out.println("Hey");
                        continue here;
                    } else {
                        Strip(st, i, resindex,currindex,pred, object, subject);
                    }


                }

//                while (st.size() > 5 && (obcount == cbcount)) {
//                    String val;
//                    x++;
//                    val = "?temp" + x;
//                    for (int j = st.size() - 2; j >= ((st.size() - 2) - 2); j--) {
//
//                        basic = basic + st.get(j);
//                        //System.out.println("Basic Finally" + basic);
//                    }
//                    if (basic.contains(">")) {
//                        String b[] = basic.split(">");
//                        //System.out.println("DO: "+b[0] + ">" + b[1]);
//                    }
//                    int size = ((st.size() - 2) - 2);
//                    for (int j = st.size() - 2; j >= size; j--) {
//                        st.pop();
//                    }
//                    st.push(val);
//                    st.push(")");
//                    if (st.size() == 5) {
//                        Strip(st, i);
//                    }
//
//                }
                //System.out.println(st);
                if (st.size() == 1) {
                    break;
                }
//                else if(st.size()==3)
//                {
//                    continue;
//                } 
                for (int j = 1; j < st.size(); j++) {
                    //System.out.println("In for" + st.get(j));
                    basic = basic + st.get(j);
                    //System.out.println(basic);
                }
                if(basic.contains(")"))
                {
                    basic=basic.substring(0,basic.length()-1);
                }
                if (basic.contains(">")) {
                String b[] = basic.split(">");
                //System.out.println("DO: "+b[0] + ">" + b[1]);
            }
            else if(basic.contains("<"))
            {
                String b[]=basic.split("<");
                //System.out.println("DO: "+b[0] + "<" + b[1]);
            }
            else if(basic.contains("+"))
            {
                 String b[]=basic.split("\\+");
                //System.out.println("DO: "+b[0] + "+" + b[1]);
            }
            else if(basic.contains("-"))
            {
                String b[]=basic.split("\\-");
                //System.out.println("DO: "+b[0] + "-" + b[1]);
            }
            else if(basic.contains("&&"))
            {
                String b[]=basic.split("&&");
                //System.out.println("DO: "+b[0] + "&&" + b[1]);
            }
                //System.out.println(st);
                int size = st.size();
                for (int j = 1; j < size; j++) {
                    // //System.out.println("Popping "+st.get(j));
                    //System.out.println(st.pop());
                }
                st.push("?temp" + ++x);
                //System.out.println(st);



            }

        }
    }

    public void lessthanops(String op1, String op2, int resindex, int currindex, String pred, String object, String subject, String operation) {
        int num;
       // //System.out.println(reshold.get(resindex-1));
        if(op1.matches("-?\\\\d+(\\\\.\\\\d+)?"))
        {
            num=Integer.parseInt(op1);
            //System.out.println("Operand 1 is numeric"+ num);
        }
        else
        {
            num = Integer.parseInt(op2);
            //System.out.println("Operand 2 is numeric" + num);
            outermost:for (String c : reshold.get(resindex)) {
                if (c.startsWith(" ")) {
                                    //System.out.println("Replaced Space!");
                                    c = c.replaceFirst(" ", "");
                                }
                String cql = "SELECT subject, " + pred + " from " + db + " WHERE subject='" + c + "';";
                //System.out.println(cql);
                ResultSet result1 = sess.execute(cql);
                for (Row row : result1) {
                     // continue;
                        String ysplit[];
                        //System.out.println("Im reaching here!!");
                        String val = row.getString(pred);
                        val=val.replace("|", "*");
                        String sub= row.getString("subject");
                        //System.out.println(val);
                         ysplit = val.split("\\*");
                                            String yfin = "";
                                            for (int lis = 0; lis < ysplit.length; lis++) {
                                                if (!ysplit[lis].contains("null") && ysplit[lis].contains("|")) {
                                                    ysplit[lis] = ysplit[lis].substring(0, ysplit[lis].length() - 1);
                                                }
                                            }
                                            ArrayList<String> ysplitlist = new ArrayList<>(Arrays.asList(ysplit));
                                            //System.out.println("Here!!"+ysplitlist);
                                            HashSet set = new HashSet(ysplitlist);
                                            Iterator iterator = set.iterator();
                                                while (iterator.hasNext()) {
                                                  
                                                    String it = iterator.next().toString();
                                                      //System.out.println("Trying to extract the number!"+it);
                                                    if (it.contains("null")) {
                                                        //System.out.println("FOund");
                                                        continue;
                                                    } 
                                                    else if(it.contains("^^<http://www.w3.org/2001/XMLSchema#integer>"))
                                                    {
                                                        //char n = 0;
                                                        //System.out.println("why is it not gettin here!!");
                                                        String nu="";
                                                        char arr[]=it.toCharArray();
                                                        out: for(int i=0;i<arr.length;i++)
                                                        {
                                                            if(arr[i]=='"')
                                                            {
                                                                int j=i+1;
                                                                while(j<arr.length)
                                                                {
                                                                    if(arr[j]!='"')
                                                                    {
                                                                        nu=nu+arr[j];
                                                                        j++;
                                                                    }
                                                                    else
                                                                        break out;
                                                                }
                                                            }
                                                        }
                                                        //System.out.println("Successful!! The number part extracted:" + nu);
                                                        int number = Integer.parseInt(nu);
                                                        switch (operation) {
                                                            case "lesser":
                                                                if (number < num) {
                                                                    //System.out.println(sub + "WILL be included!");
                                                                    reshold.get(currindex).add(sub);

                                                                    //reshold.get(resindex).add(val);
                                                                    results.put(object, nu);
                                                                    //System.out.println("************putting Subjects:" + "[" + sub + "]");
                                                                    results.put(subject, sub);
                                                                    
                                                                } else {
                                                                    continue outermost;
                                                                }
                                                                break;
                                                            case "greater":
                                                                if (number > num) {
                                                                    //System.out.println(sub + "WILL be included!");
                                                                    reshold.get(currindex).add(sub);

                                                                    //reshold.get(resindex).add(val);
                                                                    results.put(object, nu);
                                                                    //System.out.println("************putting Subjects:" + "[" + sub + "]");
                                                                    results.put(subject, sub);
                                                                } else {
                                                                    continue outermost;
                                                                }
                                                                break;

                                                        }
                                                       
                                                    }
                                                }
                }
                
            }
        }
        
        
    }
}

public class stack
{
public static void main(String args[]) {
        
        stackOps s=new stackOps("FILTER ( ?value1 < 200 )");
//        s.connecti();
//        s.init();
//        //s.filterOut(3,"productPropertyNumeric1");
//        
        
}
}

