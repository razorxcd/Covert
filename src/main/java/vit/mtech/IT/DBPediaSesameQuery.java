/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT;

/**
 *
 * @author SanjayV
 */
//import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

//import org.openrdf.query.BindingSet;
//import org.openrdf.query.QueryLanguage;
//import org.openrdf.query.TupleQuery;
//import org.openrdf.query.TupleQueryResult;
//import org.openrdf.repository.RepositoryConnection;
//import org.openrdf.repository.http.HTTPRepository;


public class DBPediaSesameQuery {

        public static void main(String args[]) {

        {

        String endpointURL = "http://dbpedia.org/sparql";
        // http://www.w3.org/wiki/SparqlEndpoints
        // http://www.semantic-systems-biology.org/biogateway/endpoint


        try {
            HTTPRepository dbpediaEndpoint = new HTTPRepository(endpointURL, "");
            dbpediaEndpoint.initialize();

            RepositoryConnection conn = dbpediaEndpoint.getConnection();
            String sparqlQuery =  " SELECT * WHERE {<http://dbpedia.org/resource/Diego_Maradona> ?y ?z}";
              TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
              TupleQueryResult result = query.evaluate();

              while(result.hasNext()) {
                  BindingSet bindingSet = result.next();
                 // Value valueOfY = bindingSet.getValue("y");
                  Value valueOfZ= bindingSet.getValue("z");
                  String url=valueOfZ.toString();
                  String[] parts=url.split("/");
                   System.out.println(parts[parts.length-1]);
                   /*List sList = new ArrayList();
                    
                     
                    

    // Add elements.
                   for(int i=0;i<parts.length-1;i++)
                   {
                       sList.add(parts[i]);
                   }

                   Joiner joiner = Joiner.on("/");
                   String f=joiner.join(sList);
                   System.out.println(f);
                   */
                 //  String final=concatStringsWSep(sList,sep);
                 // System.out.println(valueOfY.stringValue());//+" "+valueOfZ.stringValue());
              }

        } catch(RepositoryException e) {
            e.printStackTrace(System.out);
        } catch (MalformedQueryException e) {
            e.printStackTrace(System.out);
        } catch (QueryEvaluationException e) {
            e.printStackTrace(System.out);
        }
    }
       
  }
        
}
