/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicaDeNegocios;

import java.io.*;
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
import java.util.ArrayList;

/**
 *
 * @author BRUNO
 */
public class Main {
 
    public static void main(String[] args) throws Exception {

        ArrayList<String> parametros = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();
        
        
        // se preparan los arreglos con las sedes y departamentos
        ArrayList<String> sedes = new ArrayList<>();
        
        sedes.add("AL");
        sedes.add("CA");
        sedes.add("SC");
        sedes.add("SJ");
       
        String[] deptos = "CDD,CDC,DIL,AEL,EML,IAL,COL,EL,MIL,MEL,PIL,SOL,FOL,IBL,AEM,MIM,MC,DEL,ETM,ELM,MIV,FOM,SOM,PIM,MCA,PCA,PCS,AEN,ATI,CA,MRN,GPM,IMT,IDC,EM,AE,AA,BI,ME,CI,CD,CS,FI,MI,CO,E,PI,SHO,MA,QU,DI,IA,FO,GTS,AAL,AMB,IB,IM".split(",");
        
        
        
        // se preparan los arreglos con los parametros
        parametros.add("Combomodalidad");
        parametros.add("Comboperiodo");
        parametros.add("Agno");
        parametros.add("Combodepto");
        parametros.add("Combosede");
        
        valores.add("S");
        valores.add("1");
        valores.add("2014");
        valores.add("CA"); // index 3
        valores.add("SJ");
        
        // se crea la conexion con la pagina web
        HttpURLConnection webPage = new HttpURLConnection("http://www.itcr.ac.cr/MatriculaN/ghor/HorDspyIndiv.asp?Filtro=DEPARTAMENTO");
        
        // arreglo de colleciones de la base de datos
        ArrayList<String> collections = new ArrayList<String>(); 
        collections.add("cursos");      //0
        collections.add("grupos");      //1
        collections.add("profesores");  //2 

        // se crea la conexion a la base y esta lista para realizar inserciones
        Mongo miBase = new Mongo("guiaDeHorariosPrueba",collections);
        
        float porcentaje;
        
        for(int sede = 0 ; sede < sedes.size() ; sede++)
        {
            valores.set(4,sedes.get(sede));
            
            for(int depto = 0 ; depto < deptos.length ; depto++)
            {
                valores.set(3,deptos[depto]);
                System.out.println(deptos[depto]);

                // se manda el POST con determinados parametros y valores
                HTMLParser parser = new HTMLParser(webPage.sendPost(parametros, valores));
                // se parsea el HTML recibido
                ArrayList<ArrayList<String>> grupos = parser.recoverData();

                // se procesa grupo por grupo
                for(int gr = 0 ; gr < grupos.size() ; gr++)
                {
                    ArrayList<BasicDBObject> docs = procesarGrupo(grupos.get(gr),valores.get(3));
                    miBase.saveDocument(docs.get(1),docs.get(0),2);
                    miBase.saveDocument(docs.get(2),0);
                    miBase.saveDocument(docs.get(3),1);   
                }

            }
            
            porcentaje = ((sede*100)/(sedes.size()));
            System.out.println("COMPLETADO " + porcentaje + "%." );
        }
        
        
        
        //System.out.println(grupos.size());
       
    }  
    
    
    public static ArrayList<BasicDBObject> procesarGrupo(ArrayList<String> grupo, String comboDepto)
    {
        // index en cada array de grupo
        // 0: sede
        // 1: codigo
        // 2: nombre
        // 3: grupo
        // 6: creditos
        // 7: horario
        // 9: profesor
        
        // se prepara el arreglo del horario que ira dentro del documento del grupo
        ArrayList<BasicDBObject> jsonDocs = new ArrayList<>();
        String horario = grupo.get(7);
        String[] dias = horario.split("/");
        String [] diaAuxiliar, horaYfecha;
        BasicDBObject docAuxiliar;
        
        for(int dia = 0 ; dia < dias.length ; dia++ )
        {   
            if(dias[dia].length()>3)
            {
                diaAuxiliar = dias[dia].split("#");
                String horaFecha = diaAuxiliar[0];
                horaYfecha = horaFecha.split(" ",2);
                docAuxiliar = new BasicDBObject("aula",diaAuxiliar[1])
                        .append("hora",horaYfecha[1])
                        .append("dia",horaYfecha[0]);
                jsonDocs.add(docAuxiliar);
            }
        }
        
        
        
        // 0 : JSON del profesor (para validar)
        BasicDBObject profesorValidar = new BasicDBObject("nombre",grupo.get(9));
        
        // 1 : JSON del profesor
        BasicDBObject profesor = new BasicDBObject("nombre",grupo.get(9))
                .append("cantidadCalificaciones",0)
                .append("calificacionTotal",0);
        
        // 2 : JSON del curso
        BasicDBObject curso = new BasicDBObject("nombre",grupo.get(2))
                .append("codigo", grupo.get(1))
                .append("creditos",grupo.get(6))
                .append("escuela", comboDepto);
        
        // 3 : JSON del grupo
        BasicDBObject grupoDoc = new BasicDBObject("sede",grupo.get(0))
                .append("numero", grupo.get(3))
                .append("curso", grupo.get(1))
                .append("horario",jsonDocs)
                .append("profesor",grupo.get(9));
        
        
        ArrayList<BasicDBObject> jsonInfo = new ArrayList<>();
        jsonInfo.add(profesorValidar);
        jsonInfo.add(profesor);
        jsonInfo.add(curso);
        jsonInfo.add(grupoDoc);
        
        return jsonInfo;
    }
    
}
