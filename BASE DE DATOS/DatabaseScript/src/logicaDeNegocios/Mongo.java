 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicaDeNegocios;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author BRUNO
 */
public class Mongo {
    
    String dataBaseName;
    ArrayList<String> collections;
    DB dataBase;
    
    public Mongo(String pDataBase, ArrayList<String> pCollections) throws UnknownHostException
    {
        dataBaseName = pDataBase;
        collections = pCollections;
        dataBase = new MongoClient().getDB(dataBaseName);
    }
    
    private DBCollection getCollection(int collectionIndex)
    {
        DBCollection collection = dataBase.getCollection(collections.get(collectionIndex));
        return collection;
    }
    
    public void saveDocument(BasicDBObject doc, int collectionIndex)
    {
        if(find(doc,collectionIndex).size() == 0)
            getCollection(collectionIndex).insert(doc);
    }
    
    public void saveDocument(BasicDBObject doc, BasicDBObject validationDoc, int collectionIndex)
    {
        if(find(validationDoc,collectionIndex).size() == 0)
            getCollection(collectionIndex).insert(doc);
    }
    
    public DBCursor find(BasicDBObject doc,int collectionIndex) 
    {
        DBCursor collectionsFinded = getCollection(collectionIndex).find(doc);
        return collectionsFinded;
    }
    
    /*public static void main(String[] args) throws UnknownHostException  {
    
        ArrayList<String> collections = new ArrayList<String>();
        collections.add("usuarios"); //0 
        collections.add("cursos"); //1
        Mongo miBase = new Mongo("guiaDeHorariosPrueba",collections);
        
       
        
        BasicDBObject doc = new BasicDBObject("nombre","bsuno");
        ArrayList<BasicDBObject> arreglo = new ArrayList<>();
        arreglo.add(doc);
        
        
        miBase.find(doc, 0);
        
        System.out.println("HOLA");
        
        //MongoClient mongoClient = new MongoClient();
        //DB db = mongoClient.getDB("guiaDeHorariosPrueba");
        //DBCollection coll = db.getCollection("usuarios");
        
        //BasicDBObject doc = new BasicDBObject("nombre","Bruno Sarmiento Carballo").append("password","r9829827h98jdrewk"); 
        
        //coll.insert(doc);
        
    }*/
    
}
