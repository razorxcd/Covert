/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author SanjayV
 */
public class StringParser {
    
    public static void main(String args[]) throws UnsupportedEncodingException
    {
        String q="SELECT DISTINCT ?product ?label ?value1 ?value2\n" +
"WHERE { \n" +
"    \n" +
"    ?product rdf:type bsbm-inst:ProductType10 .\n" +
"    ?product bsbm:productFeature bsbm-inst:ProductFeature397 . \n" +
"    ?product bsbm:productFeature bsbm-inst:ProductFeature380 . \n" +
"    ?product bsbm:productPropertyNumeric1 ?value1 . \n" +
"\n" +
"    ?product bsbm:productPropertyNumeric1 ?value2 .\n" +
"    \n" +
"    ?product rdfs:label ?label .\n" +
"	}";
        q=q.replace("\n", " ");
        q=q.trim().replaceAll("\\s+", " ");
        System.out.println(q);
        String qsplit[]=q.split(" ");
         //qsplit=StringUtils.stripAll(qsplit);
        String x=qsplit[11];
        
            byte[] bytes = x.getBytes("US-ASCII");
            int i=0;
            for(String c:qsplit)
            {
                i++;
                if(c.equals("."))
                        {
                            System.out.println("At "+i);
                            break;
                        }
                    
            }
            
  
//        for(byte b:bytes)
//        {
//            System.out.print(b);
//            char c=(char) b;
//            System.out.println(c);
//        }
        
        
    }
    
}
