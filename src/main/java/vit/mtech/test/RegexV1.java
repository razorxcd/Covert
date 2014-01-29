/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

 



import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;


 

/**
 *
 * @author SanjayV
 */
class Pair
{
   public String key;
   public String value;

   public Pair(String key, String value)
   {
      this.key = key;
      this.value = value;
   }
   public void print()
   {
       System.out.println(key+value);
   }

}
public class RegexV1 {
    public static void main(String args[])
    {
        Pair p[]=new Pair[10];
        for(int i=0;i<p.length;i++)
        {
            
            p[i]=new Pair("String","String");
            //System.out.println(p[i]);
            p[i].print();
        }
      
}
}
