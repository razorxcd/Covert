/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.IT.major.module1;

/**
 *
 * @author SanjayV
 */
import org.openrdf.OpenRDFException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.rio.RDFFormat;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.sail.config.SailImplConfig;
import org.openrdf.sail.memory.config.MemoryStoreConfig;
import org.openrdf.repository.config.RepositoryImplConfig;
import org.openrdf.repository.manager.LocalRepositoryManager;
import org.openrdf.repository.sail.config.SailRepositoryConfig;

public class RepoCreation {

    private static String repositoryId = "test-db";

    public static void main(String[] args) throws RepositoryException, RepositoryConfigException, IOException {

        // create a configuration for the SAIL stack
        SailImplConfig backendConfig = new MemoryStoreConfig();

// create a configuration for the repository implementation
        File baseDir = new File("F:\\Cassandra");
        LocalRepositoryManager manager = new LocalRepositoryManager(baseDir);
        manager.initialize();
//RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(backendConfig);
//RepositoryConfig repConfig = new RepositoryConfig(repositoryId, repositoryTypeSpec);
//manager.addRepositoryConfig(repConfig);
        Repository repo = manager.getRepository(repositoryId);

        File file = new File("F:\\Cassandra\\LUBM\\src\\TinyUni.rdf");
        String baseURI = "http://swat.cse.lehigh.edu/onto/univ-bench.owl";
        ArrayList<String> al=new ArrayList<>();

        try {
            RepositoryConnection con = repo.getConnection();
            try {
                con.add(file, baseURI, RDFFormat.RDFXML);
                String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
"PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>\n" +
"SELECT ?X\n" +
"WHERE\n" +
"{?X rdf:type ub:Person .\n" +
"  ?X ub:memberOf <http://www.Department0.University0.edu> } ";
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

                TupleQueryResult result = tupleQuery.evaluate();



                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();

                    Value valueOfX = bindingSet.getValue("X");
                    System.out.println(valueOfX);
                    al.add(valueOfX.stringValue());
                   // Value valueOfY = bindingSet.getValue("y");

                    // do something interesting with the values here...
                  //  System.out.println(valueOfX+":"+valueOfY);

             


                }
                System.out.println("Size:"+al.size());
                result.close();


            } finally {
                con.close();
            }
        } catch (OpenRDFException e) {
            // handle exception
            System.out.println("Caught" + e);
        }


    }
}
