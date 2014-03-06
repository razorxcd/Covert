/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

/**
 *
 * @author SanjayV
 */
import com.hp.hpl.jena.query.Query ;
import com.hp.hpl.jena.query.QueryFactory ;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpVisitor;
import com.hp.hpl.jena.sparql.syntax.Element;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import org.apache.commons.lang3.StringUtils;
public class JenaARQ extends ElementVisitorBase{
    private static OpVisitor OpVisitor;
    public static String queryString;
    
    
    public JenaARQ(String str)
    {
        queryString=str;
    }
   
      

  public String extract()
  {
    Query query = QueryFactory.create(queryString);
    if(query.isSelectType())
            System.out.println("it is select");
    if(query.isQueryResultStar())
            System.out.println("it has star");
    
        System.out.println(query.getQueryPattern());
        Element q=query.getQueryPattern();
        String qstr=q.toString();
        if(qstr.contains("<"))
            System.out.println("CONTAINS");
    qstr=qstr.replace("\n", " n");
    
    qstr=StringUtils.replaceEach(qstr, new String[]{"{","}","<",">"}, new String[]{"","","",""});
    qstr=qstr.trim().replaceAll("\\s+", " ");
    //qstr=qstr.replace("n", " .n");
    String split[]=qstr.split(" ");
    PrefixMapping map=query.getPrefixMapping();
      System.out.println(map.expandPrefix("rdf:type"));
      System.out.println("--------->"+map);
      
      System.out.println(query.getResultVars());
//    for(int i=0;i<split.length;i++)
//    {
//        System.out.println(split[i]);
//        System.out.println("----------");
//    }
//        System.out.println(qstr);
        return qstr;
        

      
      
        
    }
    public static void main(String args[])
        {
                queryString="PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/>\n" +
"PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> \n" +
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
"\n" +
"SELECT DISTINCT ?product ?label\n" +
"WHERE { \n" +
"    ?product rdfs:label ?label ;" +
"     rdf:type bsbm-inst:ProductType10;" +
"     bsbm:productFeature bsbm-inst:ProductFeature380;" +
"     bsbm:productFeature bsbm-inst:ProductFeature407;" +
"     bsbm:productPropertyNumeric1 ?value1;" +
"	FILTER (?value1 > 500) . \n"+
       "?product rdf:type ?value1 ."+
                        "?product rdf:type ?value2"+
"	}\n" +
"ORDER BY ?label\n" +
"LIMIT 10";
            JenaARQ q=new JenaARQ(queryString);
            q.extract();
        }

}

