/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SanjayV
 */
public class NewClass {
    public static void main(String args[]){
        String str="http://www.w3.org/2000/01/rdf-schema#subClassOf";
        /*Pattern p=Pattern.compile("[^/]");
        Matcher m=p.matcher(str);
        if(m.matches()) {
    System.out.println("The quantity is " + m.group(1));
    System.out.println("The time is " + m.group(2));
}
        else{
            System.out.println("No match");
        }*/
        String p[]=str.split("#");
        for(int i=0;i<p.length;i++)
        {
            System.out.println(p[i]);
        }
    }
    
}
