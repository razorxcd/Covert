/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

/**
 *
 * @author SanjayV
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author batch1
 */
import java.io.*;
import java.util.*;

public class NewClass1 {
   static HashMap<String, String> hash=new HashMap<String, String>();
    static HashMap<String, String> result=new HashMap<String, String>();
    static ArrayList<String> al=new ArrayList();
    public static void main(String args[])
    {
        
        hash.put("Animal", "LivingBeings");
        hash.put("Desert", "Plants");
        hash.put("HumanBeings", "LivingBeings");
        hash.put("birds", "AIR");
        hash.put("AIR", "Animal");
        hash.put("Water", "Animal");
        hash.put("Sea", "Plants");
        hash.put("Plants", "LivingBeings");
        hash.put("Dull", "LivingBeings");
        hash.put("Average", "HumanBeings");
        hash.put("Land", "Animals");
        hash.put("Tropical", "Plants");
        boolean flag=true;
        
        Set set1=hash.entrySet();
        Iterator j=set1.iterator();
        while(flag)
        {
            Set set=hash.entrySet();
            Iterator i=set.iterator();
            while(i.hasNext())
            {
                Map.Entry me=(Map.Entry)i.next();
                String x=me.getValue().toString();
                if (isThere(x))
                        {
                            hash.put(me.getKey().toString(),(String)hash.get(me.getValue().toString()));
                            //result.put(me.getValue().toString(),(String)hash.get(me.getValue().toString()));
                           
                        }
                
                   
            }
           
            while(i.hasNext())
            {
                Map.Entry me=(Map.Entry)i.next();
                System.out.println("PING");
                if (isThere(me.getValue().toString()))
                {
                    System.out.println("PING");
                    flag=true;
                    break;
                }
                else
                {
                    flag=false;
                    continue;
                }
                
            }
        }
       print();
        
    }

    
   static public void load()
       {
           Set set=hash.entrySet();
            Iterator i=set.iterator();
            while(i.hasNext()){
              Map.Entry me=(Map.Entry)i.next();
              al.add(me.getKey().toString());
              }
       }
    private static boolean isThere(String value) {
       
            String flag1;
            flag1=value;
            load();
            //System.out.println(al);
            //int flag;
           // ArrayList<String> resList = new ArrayList<String>();
            //while(i.hasNext()){
              //Map.Entry me=(Map.Entry)i.next();
             // String val=me.getValue().toString();
              //String x=(String) hash.get(val);
             for (String curVal : al){
               if (curVal.equals(flag1)){
                   //String y=(String) hash.get(flag);
                   //System.out.println();
                     break;
                }     
                   // }
                else
                {
                   continue;
                 }
               
                }
              //break;
              return true;
              }

    private static void print() {
         //To change body of generated methods, choose Tools | Templates.
        Set set=hash.entrySet();
            Iterator i=set.iterator();
            while(i.hasNext()){
              Map.Entry me=(Map.Entry)i.next();
              System.out.println(me.getKey().toString()+":"+me.getValue().toString());
    }
    
    
}
}
              
       
   

    
