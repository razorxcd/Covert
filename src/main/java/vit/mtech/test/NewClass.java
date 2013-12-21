/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SanjayV
 */

class find
{
    ArrayList<String> al=new ArrayList<String>();
   
    public void isThere(String x)
    {
        for(String cur:al)
        {
            if(cur.equals(x.toString()))
            {
                break;
            }
        }
        System.out.println("found");
    }
}

public class NewClass {
    public static void main(String args[])
    {
        find f=new find();
        f.al.add("String");
        f.al.add("Animal");
        f.al.add("Sleep");
        f.isThere("String");
    }
    
    
   
}

    
    