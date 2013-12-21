/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vit.mtech.test;
import java.util.Arrays;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.ddl.*;

/**
 *
 * @author SanjayV
 */
public class Schema {
    public static void main(String args[])
    {
        Cluster myCluster = HFactory.getOrCreateCluster("test","localhost:9160");
        ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition("MyKeyspace",
                                                                     "ColumnFamilyName",
                                                                     ComparatorType.BYTESTYPE);

        KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition("MyKeyspace",
                                                                   ThriftKsDef.DEF_STRATEGY_CLASS,
                                                                   1,
                                                                   Arrays.asList(cfDef));
        myCluster.addKeyspace(newKeyspace, true);
        

    }
    
}
