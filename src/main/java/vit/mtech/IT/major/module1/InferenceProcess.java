/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.it.major.module1;
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

/**
 *
 * @author SanjayV
 */
 class Inference {
    
        String flag;
        HashMap<String,String> hash=new HashMap<String,String>();
        ArrayList<String> al=new ArrayList<String>();
        public void DO() throws MalformedURLException, IOException, RDFParseException, RDFHandlerException
        {
            
            
    
        File uni = new File("C:\\Users\\SanjayV\\SkyDrive\\Documents\\final.owl");
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
           
            //while(i.hasNext())
            //{
           //     Map.Entry me=(Map.Entry)i.next();
            //    al.add(me.getKey().toString());
          
            //}
          boolean f=true;
          while(f){
              while(i.hasNext())
              {
              Map.Entry me=(Map.Entry)i.next();
              String val=me.getValue().toString();
              String x=(String) hash.get(val);
              //System.out.println(val);
              //System.out.println(x);
              
              if(isThere(x))
              {
                  
                  String y=(String) hash.get(x);
                  if(y!=null)
                  {
                  System.out.println(me.getKey().toString()+"  is a SubClassOfinf  "+y);
                      
              }
                  else{
                      f=false;
                  }
                  
              }
             // System.out.println(flag);
              System.out.println(me.getKey()+"  is a SubClassOf  "+me.getValue());
              
         
              if(x!=null)
              {
                 System.out.println(me.getKey().toString()+"  is a SubClassOfinf1  "+x);
              }
              
              
          }
        }
        }
     /*  public void load()
       {
           Set set=hash.entrySet();
            Iterator i=set.iterator();
            while(i.hasNext()){
              Map.Entry me=(Map.Entry)i.next();
              al.add(me.getKey().toString());
             // System.out.println(me.getKey().toString()+":"+me.getValue().toString());
              }
       }*/
        
        public boolean isThere(String flag)
        {
            String flag1;
            flag1=flag;
            //load();
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
            //return true;
                          
            
 }          
                //System.out.println(al);
            
            
           
            
            
            
        
        


    
        
    
public class InferenceProcess{
    
public static void main(String[] args) throws 
            UnsupportedEncodingException, MalformedURLException, IOException, RDFParseException, RDFHandlerException{
    Inference inf=new Inference();
    inf.DO();
    //inf.isThere(x);
}
}
       
            
        
        
    


    
        
    
