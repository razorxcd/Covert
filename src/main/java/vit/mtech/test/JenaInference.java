package vit.mtech.test;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

import com.datastax.driver.core.*;


import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


class JenaInference
{
    public static void main(String args[]) throws MalformedURLException
    {
        File uni = new File("F:\\Cassandra\\LUBM\\src\\University0_0.owl");
        URL documentUrl = ((uni.toURI()).toURL());

        // create an empty model
        //Model model = ModelFactory.createDefaultModel();
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(documentUrl.toString());
        model.read(in, null);

        InfModel infmodel = ModelFactory.createRDFSModel(model);
        //StmtIterator stmts = model.listStatements();
        StmtIterator stmts1 = infmodel.listStatements();
        
         while (stmts1.hasNext()) {
           // c++;
           // System.out.println(c);
            String sa = stmts1.next().toString();
            //System.out.println(map.get("type"));

            System.out.println(sa);
            String sp[] = sa.split(",");
            
            for(int i=0;i<sp.length;i++)
                 System.out.println(sp[i]);
            

            String first = sp[0].substring(1, sp[0].length());
             System.out.println("before"+first);
            first = first.replace("'", "");
           // first = " " + first;
            System.out.println("After"+first);
           // break;
            // first=first.replace(ont,"tst:");

            String last = sp[2].substring(0, sp[2].length() - 1);
            if(last.startsWith(" "))
                last=last.replace(" ", "");
            //System.out.println(last);
            last = last.replace("'", "");
            
             System.out.println("Last"+last);

            //last=last.replace(ont,"tst:");
            String mid; 
            mid = sp[1];
            if(mid.startsWith(" "))
                mid=mid.replace(" ", "");
            
            System.out.println("Last"+mid);
            break;
//            String msplit[]=new String[2];
//            if (mid.contains("#")) {
//                msplit = mid.split("#");
//            } else {
//                if(mid.startsWith(" "))
//                {
//                    mid=mid.replace(" ", "");
//                }
//                URI uri = URI.create(mid);
//                String path = uri.getPath();
//                //System.out.println(path.substring(path.lastIndexOf('/') + 1));
//                msplit[1] = path.substring(path.lastIndexOf('/') + 1);
            }
         }
    }
