/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicaDeNegocios;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

/**
 *
 * @author BRUNO
 */
public class HTMLParser {
    
    Document document;
    
    public HTMLParser(String pWebPage)
    {
        document = Jsoup.parse(pWebPage);
    }
    
    public ArrayList<ArrayList<String>> recoverData()
    {    
        Elements tablas = document.body().children();
        //System.out.println(tablas.size());
        //System.out.println();
 
        Element mainTable = tablas.get(0);
        Element tbody = mainTable.children().get(0); // SE OBTIENE EL 0, AUNQUE SOLO TIENE UN HIJO LA TABLA
        Elements filas = tbody.children(); // SON TRES FILAS, LAS PRIMERAS DOS DE IMAGENES Y TITULOS.
        Element contenido = filas.get(2); // FILA CON LOS CURSOS.
        Element celdaMayor = contenido.child(0); // LOS CURSOS ESTAN EN UNA SOLA CELDA
        Element tablaCursosBody = celdaMayor.child(0).child(0); // LA TABLA CON LOS CURSOS ESTA EN ESA CELDA, Y LUEGO HAY QUE IR AL TBODY
        // EL BODY DE LA TABLA ANTERIORES POSEE 5 FILAS... NOS INTERESA LA CUARTA YA QUE TIENE LOS CURSOS
        Element celdaCursos = tablaCursosBody.child(3); // ESTA FILA TIENE UNA SOLA 
        if(celdaCursos.child(0).children().size()==1)
            return(new ArrayList<ArrayList<String>>());
        Element cursosTabla = celdaCursos.child(0).child(1); // Y ESTA CELDA CONTIENE OTRAS DOS TABLAS, UNA DE CONTENIDOS Y LA OTRA DE CURSOS
        Element cursosBody = cursosTabla.child(0); // SACAMOS EL BODY
        Elements cursosFilas = cursosBody.children();
        
        ArrayList<ArrayList<String>> cursos = new ArrayList<ArrayList<String>>();
        Element cursoAuxiliar;
        Elements celdasCurso;
        
        for(int curso = 1 ; curso < cursosFilas.size() ; curso++)  // SE EMPIEZA DESDE LA 1 PORQUE LA PRIMERA FILA SON ENCABEZADOS
        {
            ArrayList<String> infoCurso = new ArrayList<String>();
            
            cursoAuxiliar = cursosFilas.get(curso); // SE OBTIENE LA FILA CORRESPONDIENTE A CADA CURSO
            celdasCurso = cursoAuxiliar.children(); // OBTENGO TODAS LAS CELDAS DEL CURSO
            Element celdaAux;
            String textoCurso; 
            Elements horarioTabla;
            Element horaYdia, clase;
            String horarios = "";
            
            for(int celda = 0 ; celda < celdasCurso.size() ; celda++)
            {
                celdaAux = celdasCurso.get(celda);
                if(celda!=7)
                {
                    textoCurso = celdaAux.text();
                    infoCurso.add(textoCurso);
                }
                else
                {
                    horarios = "";
                    
                    horarioTabla = celdaAux.child(0).child(0).child(0).child(0).children(); // ESTO DEVUELVE LOS ELEMENTOS DEL BODY DE LA TABLA CON EL HORARIO.
                    for(int horario = 0 ; horario < (horarioTabla.size()) ; horario++)
                    {
                        if(horarioTabla.get(horario).children().size()>1)
                        {
                            if(horario!=0)
                                horarios = horarios + "/";
                            horaYdia = horarioTabla.get(horario).child(0); // DA LA CELDA QUE CONTIENE LA HORA Y DIA   
                            clase = horarioTabla.get(horario).child(1); // DA LA CELDA QUE CONTIENE LA CLASE
                            horarios = horarios + horaYdia.text() + "#" + clase.text();
                        }
                    }
        
                    infoCurso.add(horarios);
                }
            }
            
            cursos.add(infoCurso);
        }
        
        
        
        //for(int grupo = 0 ; grupo < cursos.size() ; grupo++)
        //{
          //  ArrayList<String> grupoAuxiliar = cursos.get(grupo);
          //  System.out.println("El curso " + grupoAuxiliar.get(2) + " impartido por " + grupoAuxiliar.get(9) + " en la sede de " + grupoAuxiliar.get(0) + " en el horario " + grupoAuxiliar.get(7) );
        //}
        
        return cursos;
    }
    
    
}
