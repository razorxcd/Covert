/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

                //System.out.println(al);
            
            
           
            
            
            
        
        


    
        
    
                //System.out.println(al);
            
            
           
            
            
            
        
        


    
        
    
/**
 *
 * @author SanjayV
 */
 class Inference1 {
    
        String flag;
        HashMap<String,String> hash=new HashMap<String,String>();
         HashMap<String,String> result=new HashMap<String,String>();
        ArrayList<String> al=new ArrayList<String>();
        public void DO() throws MalformedURLException, IOException, RDFParseException, RDFHandlerException
        {
            
            
    
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\new.owl");
        //java.net.URL documentUrl = new URL("F:\\Cassandra\\stardog\\examples\\data\\University.owl");
        URL documentUrl = ((uni.toURI()).toURL());
        InputStream inputStream = documentUrl.openStream();
        //RDFParser rdfParser = Rio.createParser(RDFFormat.);
        RDFFormat format = Rio.getParserFormatForFileName(documentUrl.toString());
        //RDFFormat format = Rio.getParserFormatForMIMEType(contentType);
        RDFParser rdfParser = Rio.createParser(format);
        Model model = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(model);
        rdfParser.setRDFHandler(collector);

        rdfParser.parse(inputStream, documentUrl.toString());
        Collection<Statement> ite=collector.getStatements();
        
        
            for (Statement sta : ite){
                String pred=sta.getPredicate().toString();
                String sub=sta.getSubject().toString();
                String obj=sta.getObject().toString();
                
                //splitting
                String p[]=pred.split("#");
                String s[]=sub.split("#");
                String o[]=obj.split("#");
                
                if("subClassOf".equals(p[1])){
                    hash.put(s[1],o[1]);
                    
                    
                }
            }
            
                     // Printing the HashMap
                        /* Set set = hash.entrySet();
                            // Get an iterator
                           Iterator i = set.iterator();
                           // Display elements
                                while(i.hasNext()) {
                                Map.Entry me = (Map.Entry)i.next();
                                System.out.print(me.getKey() + ": ");
                                 System.out.println(me.getValue());
                                }*/
            Set set=hash.entrySet();
            Iterator i=set.iterator();
            //Set set1=result.entrySet();
            //Iterator j=set1.iterator();
            Iterator i1=set.iterator();
            
           for(Map.Entry<String, String> entry : hash.entrySet())
            {
               //Map.Entry me=(Map.Entry)i.next();
               al.add(entry.getKey().toString());
               
             // System.out.println(al);
               
            }
            
          
            //}
          //boolean f=true;
           do{
              //boolean s=check();
              //System.out.println(s);
            //ArrayList<String> AY=new ArrayList<String>();  
            
              
              
                
                //System.out.println("YES");
            
           
           while(i.hasNext())
            {
                
                
                Map.Entry me=(Map.Entry)i.next();
                String x=me.getValue().toString();
                String y=(String)hash.get(x);
                String key=me.getKey().toString();
                //System.out.println(key);
                if (isThere(x))
                        {
                            if(y!= null){
                            //hash.put(key,(String)hash.get(x));
                          result.put(key,(String)hash.get(x));
                          
                           
                           System.out.println(key+":"+y);
                            
                        }
                           
                        }
                
                   
            }
            
            /*while(j.hasNext())
            {
                Map.Entry me1=(Map.Entry)j.next();
                String x1=me1.getValue().toString();
               //System.out.println(x1);
                if(isThere(x1))
                {
                    //System.out.println("There is"+x1);
                    f=true;
                    j.remove();
                    break;
                }
                else
                {
                    f=false;
                   // continue;
                }
            }*/
           //for(String C:AY)
           //{
               
           //}
            
              
          }while(!check());
          //print();
        }
          public boolean check() {
        
        Set set1=result.entrySet();
        Iterator j=set1.iterator();
        while(j.hasNext())
            {
                Map.Entry me1=(Map.Entry)j.next();
                String x1=me1.getValue().toString();
                String key=me1.getKey().toString();
               //System.out.println(x1);
                if(isThere(x1))
                {
                    System.out.println("There is"+x1);
                    //f=true;
                  result.remove(key);
                    break; //To change body of generated methods, choose Tools | Templates.
                 }
                
            }
        return true;
      
    }
        

      
    public boolean isThere(String val)
    {
      //  System.out.println(al);
        
        for(String x:al)
        {
            if(x.equals(val))
            {
              break;
               
            }
            else
                return false;
            
        }
            return true;
            
      
        
    }
       
            
    public void print() {
       //To change body of generated methods, choose Tools | Templates.
        
           for(Map.Entry<String, String> entry : hash.entrySet())
           {
              //Map.Entry me=(Map.Entry)i.next();
              System.out.println(entry.getKey().toString()+":"+entry.getValue().toString());
    }
                          
            
 }  

    
 }         //System.out.println(al);
            
            
           
            
            
            
        
        


    
        
    
public class InferenceP{
    
public static void main(String[] args) throws 
            UnsupportedEncodingException, MalformedURLException, IOException, RDFParseException, RDFHandlerException{
    Inference1 inf=new Inference1();
    inf.DO();
   inf.print();
    //inf.isThere(x);
}
}
 
       
            
        
        
    


    
        
    
