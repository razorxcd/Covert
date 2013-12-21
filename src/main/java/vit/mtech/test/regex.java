/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 *
 * @author SanjayV
 */
public class regex {
    public static void main(String args[])
    {
        String sub="hello";
        String pred="world";
        String obj="some";
String input = "INSERT INTO rdfstore (subject, predicate, object) VALUES"+" "+"("+"'"+sub+"'"+","+" "+"'"+pred+"'"+","+" "+"'"+obj+"'"+");";
String query="SELECT"+" "+"subject"+","+" "+"Object"+" "+"FROM rdfstore"+" "+"WHERE predicate='"+"Something"+"';";
String x="INSERT INTO rdfstore (subject, predicate, object) VALUES ("+"'"+"ont+key1"+"'"+","+" "+"'"+"pred"+"'"+","+" "+"'"+"val"+"');";
//Matcher matcher = Pattern.compile("(/?/).*").matcher(input);
//matcher.find();
System.out.println(x);
    }
}
