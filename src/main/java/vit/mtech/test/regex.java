/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;

import java.net.URI;
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
        String col="col";
        String key1="key1";
        String val="val";
        String c="[hello], [hi], [How are you? Sanjay?]";
        String s[]=c.split(",");
        String y="null| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing| http://xmlns.com/foaf/0.1/OnlineAccount| http://xmlns.com/foaf/0.1/OnlineGamingAccount| http://www.w3.org/2000/01/rdf-schema#Resource| http://www.w3.org/2002/07/owl#Thing";
        
        
        String h="select DISTINCT ?video ?toTime ?fromTime ?cameraID ?mimeType ?repositoryID ?url ?kindOf ?local where {\n" +
"        ?eventInstance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.advise-project.eu/ontology/surveillance#Running>\n" +
"        ?eventInstance <http://www.advise-project.eu/ontology/surveillance#containsSegment> ?segments .\n" +
"        ?video <http://www.advise-project.eu/ontology/surveillance#hasSegment> ?segments .\n" +
"        ?video <http://www.advise-project.eu/ontology/surveillance#from> ?hasFrom .\n" +
"        ?hasFrom <http://www.advise-project.eu/ontology/surveillance#hasTime> ?fromTime .\n" +
"        ?video <http://www.advise-project.eu/ontology/surveillance#to> ?hasTo .\n" +
"        ?hasTo <http://www.advise-project.eu/ontology/surveillance#hasTime> ?toTime .\n" +
"        ?video <http://www.advise-project.eu/ontology/surveillance#isRealisedBy> ?realisedBy .\n" +
"        ?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasCameraID> ?cameraID .\n" +
"        ?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasMimeType> ?mimeType .\n" +
"        ?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasRepositoryID> ?repositoryID .\n" +
"        ?realisedBy <http://www.advise-project.eu/ontology/surveillance#hasURL> ?url .\n" +
"        ?realisedBy <http://www.advise-project.eu/ontology/surveillance#isKindOf> ?kindOf .\n" +
"        ?realisedBy <http://www.advise-project.eu/ontology/surveillance#isLocal> ?local .\n" +
"}";
        
        
        
String input = "INSERT INTO rdfstore (subject, predicate, object) VALUES"+" "+"("+"'"+sub+"'"+","+" "+"'"+pred+"'"+","+" "+"'"+obj+"'"+");";
String query="INSERT INTO SO_RDF (subject, "+col+") VALUES ('"+key1+"',"+" '"+val+"');";
String x="http://xmlns.com/foaf/0.1/OnlineGamingAccount";
final URI uri = URI.create(x);
final String path = uri.getPath();
System.out.println(path.substring(path.lastIndexOf('/') + 1));
//Matcher matcher = Pattern.compile("(/?/).*").matcher(input);
//matcher.find();
    }
}
