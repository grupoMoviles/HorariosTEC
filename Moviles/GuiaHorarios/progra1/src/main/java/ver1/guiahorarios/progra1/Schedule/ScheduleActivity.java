package ver1.guiahorarios.progra1.Schedule;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;
import ver1.guiahorarios.progra1.R;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

public class ScheduleActivity extends Activity {

    // ARREGLOS PUBLICOS
    ArrayList<String> courses;
    ArrayList<Group> userGroups = new ArrayList<Group>();
    String[] horas = {"7:30 - 8:20","8:30 - 9:20", "9:30 - 10:20","10:30 - 11:20","11:30 - 12:20","13:00 - 13:50","14:00 - 14:50","15:00 - 15:50","16:00 - 16:50","17:00 - 17:50","18:00 - 18:50","19:00 - 19:50","20:00 - 20:50","21:00 - 21:50"};
    String[] dias = {"  HORA   ","  LUNES  "," MARTES  ","MIERCOLES"," JUEVES  ", "VIERNES ","  SABADO "};
    int width,height;
    Schedule schedule = new Schedule();
    ArrayList<GruposPorCurso> coursesGroups = new ArrayList<GruposPorCurso>();


    int groupPressed[] = {0,0,0};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        courses = new ArrayList<String>();


        // SE LLENAN LOS ARREGLOS NECESARIOS

            if(UserChosen.getInstance().getUser().isHasFacebook())
            {
                new getUserFacebookCourseCodes().execute();
            }
            else new getUserCourseCodes().execute();




        // SE TOMAN LAS MEDIDAS DE LA PANTALLA
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();


        // SE CREA EL TABLELAYOUT (LA MALLA DE HORARIO, CON ENCABEZADOS)
        createTable();


        // LLENAR EL HORARIO CON LOS GRUPOS YA SELECCIONADOS




    }




    public void fillGroupsInSchedule()
    {
        Log.e("IMPRIMIENDO GRUPOS =================================================","========================================");
        int grupos = userGroups.size();
        for(int group = 0 ; group < grupos ; group++)
        {
            Group auxiliarGroup = userGroups.get(group);
            int number = auxiliarGroup.getNumber();
            String course = auxiliarGroup.getCourse_id();
            ArrayList<ArrayList<String>> days = auxiliarGroup.getSchedule();
            for(int day = 0 ; day < days.size() ; day++)
            {
                ArrayList<String> info = days.get(day);
                int rowsColumn[] = calcularFilaCol(info.get(2), info.get(1));
                fillCells(rowsColumn,course,number);
                Log.e("IMPRIMIENDO HORA ====================================",course+number+rowsColumn[0]+"/"+rowsColumn[1]);
            }
        }

    }


    public void highlightCells(int rowsColumn[], int number)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.drawable_back3);
        Drawable drawable1 = getResources().getDrawable(R.drawable.drawable_back4);
        Drawable drawable2 = getResources().getDrawable(R.drawable.drawable_back5);
        Drawable drawable3 = getResources().getDrawable(R.drawable.drawable_back6);
        Drawable drawable4 = getResources().getDrawable(R.drawable.drawable_back7);
        Drawable drawable5 = getResources().getDrawable(R.drawable.drawable_back8);
        Drawable drawable6 = getResources().getDrawable(R.drawable.drawable_back9);
        Drawable drawables[] = {drawable,drawable1,drawable2,drawable3,drawable4,drawable5,drawable6};
        int begin = rowsColumn[0];
        int end = rowsColumn[1];
        int column = rowsColumn[2];

        if(column!=0 && begin!=0 && end!=0)
            for(int auxiliar = begin; auxiliar <= end ; auxiliar++)
            {
                TextView tv = (TextView) findViewById(auxiliar*7+column);
                tv.setBackgroundColor(Color.parseColor("#3EB3EE"));
                tv.setText("Grupo: " + number);
            }

    }



    public void fillCells(int rowsColumn[],String course , int number)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.drawable_back3);
        Drawable drawables[] = {drawable,drawable,drawable,drawable,drawable};
        int begin = rowsColumn[0];
        int end = rowsColumn[1];
        int column = rowsColumn[2];

        if(column!=0 && begin!=0 && end!=0)
            for(int auxiliar = begin; auxiliar <= end ; auxiliar++)
            {
                TextView tv = (TextView) findViewById(auxiliar*7+column);
                tv.setBackground(drawables[indexOfCourse(course)]);
                tv.setText(course + "\nGrupo: " + number);

                schedule.ocuppy(auxiliar,column,course,number);

            }

    }


    class getUserCourseCodes extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().getUserCourses(UserChosen.getInstance().getUser().getEmail(),UserChosen.getInstance().getUser().getPassword());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            courses = CourseConn.getInstance().getUser_courses_code();
            try {
                beginArrays();
                fillGroupsInSchedule();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getUserFacebookCourseCodes extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().getFacebookUserCourses(UserChosen.getInstance().getUser().getFacebook());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            courses = CourseConn.getInstance().getUser_courses_code();
            try {
                beginArrays();
                fillGroupsInSchedule();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void beginArrays() throws IOException, JSONException {

        //courses.add("IC1401");
        //courses.add("MA1202");
        //courses.add("CI7300");

        ArrayList<Group> groups = new ArrayList<Group>(GroupsSelected.getInstance().getHash_groups().values());
        userGroups = groups;

        for(int i=0;i<courses.size();i++)
        {
            Log.e("MIS CURSOS",""+courses.get(i) +" GRUPO " +userGroups.get(i).getSchedString());

        }

        //String pCourseId, int pNumber, ArrayList<String> pHorarios, String pTeacher
        ArrayList<String> horario = new ArrayList<String>();
        horario.add("F5 - 06/13:00 - 14:50/J");
        horario.add("F5 - 08/13:00 - 15:50/K");
        Group IC1401 = new Group("IC1401",1,horario,"ANDREI FUENTES");

        ArrayList<String> horario1 = new ArrayList<String>();
        horario1.add("A1 - 01/7:30 - 10:20/L");
        horario1.add("B3 - 02/17:00 - 18:50/M");
        Group MA1202 = new Group("MA1202",3,horario1,"JEFF SCHMIDT");

        ArrayList<String> horario2 = new ArrayList<String>();
        horario2.add("D3 - 03/7:30 - 9:20/M");
        horario2.add("B2 - 10/7:30 - 9:20/V");
        Group CI7300 = new Group("CI7300",10,horario2,"JORGE PRENDAS");

       // userGroups.add(IC1401);
        //userGroups.add(MA1202);
        //userGroups.add(CI7300);



        // grupos de IC1401
        ArrayList<String> horario3 = new ArrayList<String>();
        horario3.add("F5 - 06/15:00 - 17:50/L");
        horario3.add("F5 - 08/15:00 - 18:50/V");
        Group IC14012 = new Group("IC1401",2,horario3,"JOSE CASTRO");

        ArrayList<String> horario4 = new ArrayList<String>();
        horario4.add("F5 - 06/7:30 - 10:20/K");
        horario4.add("F5 - 08/8:30 - 11:20/V");
        Group IC14013 = new Group("IC1401",3,horario4,"KISTEIN GATJENS");

        GruposPorCurso groupsIC1401 = new GruposPorCurso("IC1401");
        groupsIC1401.addGroup(IC1401);
        groupsIC1401.addGroup(IC14012);
        groupsIC1401.addGroup(IC14013);

        saveNewCourseGroups();
        //coursesGroups.add(groupsIC1401);

    }

    int course_index = 0;
    ArrayList<Group> group_per_course;

    public void saveNewCourseGroups()
    {
        if(course_index<courses.size())
        new getCourseGroup().execute();
        else course_index=0;
    }


    class getCourseGroup extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                GroupConn.getInstance().postData(courses.get(course_index));
                group_per_course = GroupConn.getInstance().getGroup_list();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            coursesGroups.add(new GruposPorCurso(courses.get(course_index),group_per_course));
            course_index +=1;
            saveNewCourseGroups();
        }
    }



    public int findGroupsByCourse(String course)
    {
        for(int index = 0 ; index < coursesGroups.size() ; index++)
        {
            if(coursesGroups.get(index).getCourse().equals(course))
                return index;
        }
        return -1;
    }



    public void createTable()
    {
        TableLayout table = new TableLayout(this);
        FrameLayout.LayoutParams frameparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,FrameLayout.LayoutParams.FILL_PARENT);
        table.setLayoutParams(frameparams);
        table.setStretchAllColumns(true);

        for(int fila = 0 ; fila < 15 ; fila++)
        {
            TableRow tr = new TableRow(this);
            TableLayout.LayoutParams paramAux = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.FILL_PARENT);
            paramAux.weight = 1;
            tr.setLayoutParams(paramAux);

            for(int col = 0 ; col < 7 ; col++)
            {
                TextView tv = new TextView(this);
                if(fila==0)
                {
                    tv.setText(dias[col]);
                    tv.setBackground(getResources().getDrawable(R.drawable.drawable_back));
                }
                else
                {
                    if(col==0)
                    {
                        tv.setText(horas[fila-1]);
                        tv.setBackground(getResources().getDrawable(R.drawable.drawable_back));
                    }
                    else
                        tv.setBackground(getResources().getDrawable(R.drawable.drawable_back));
                }

                tv.setTextColor(Color.parseColor("#529EB1"));
                tv.setTextSize(6);
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams( new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.FILL_PARENT));
                tv.setId(fila*7+col);
                tr.addView(tv);
            }

            table.addView(tr);
        }

        FrameLayout mainContainer = (FrameLayout) findViewById(R.id.container);
        mainContainer.addView(table);
    }


    public int findUsersGroupByCourse(String course)
    {
        for(int group = 0 ; group < userGroups.size() ; group++)
        {
            if(userGroups.get(group).getCourse_id().equals(course))
                return group;
        }

        return -1;
    }


    public class Schedule
    {
        int cells[][];
        ArrayList<String> cellsInformation[][];
        boolean pressing = false;
        int cellPressed[] = {0,0,0};

        public Schedule()
        {
            cells = new int[15][7];
            for(int fila = 0 ; fila < cells.length ; fila++)
                for(int col = 0 ; col < cells[fila].length ; col++)
                {
                    if(col==0 || fila==0)
                        cells[fila][col] = 2 ;
                    else
                        cells[fila][col] = 0 ;
                }

            cellsInformation = new ArrayList[15][7];
            for(int fila = 0 ; fila < cellsInformation.length ; fila++)
                for(int col = 0 ; col < cellsInformation[fila].length ; col++)
                {
                    ArrayList<String> auxiliar = new ArrayList<String>();
                    auxiliar.add("");
                    auxiliar.add("");
                    cellsInformation[fila][col] = auxiliar ;
                }

        }

        public void printCellsState()
        {
            for(int i = 0 ; i < cells.length ; i++)
            {
                int row[] = cells[i];
                Log.d("-> ",""+row[0]+row[1]+row[2]+row[3]+row[4]+row[5]+row[6]);
            }

        }

        public boolean isPressing()
        {
            return pressing;
        }

        public void pressed(int row, int col)
        {
            cellPressed[0] = row;
            cellPressed[1] = row;
            cellPressed[2] = col;
            pressing = true;
        }

        public void dropped()
        {
            pressing = false;
        }

        public boolean isOcuppy(int row, int col)
        {
            if(cells[row][col] == 1)
                return true;
            else
                return false;
        }


        public void ocuppy(int row, int column,String course,int number)
        {
            cells[row][column] = 1 ;
            cellsInformation[row][column].set(0,course);
            cellsInformation[row][column].set(1,""+number);
        }


        // QUITA UN GRUPO DE LA MALLA LLENANDO CON CEROS LAS CELDAS
        public void evacuateGroup(int group)
        {
            Group oldGroup = userGroups.get(group);
            ArrayList<ArrayList<String>> days = oldGroup.getSchedule();

            for(int day = 0 ; day < days.size() ; day++)
            {
                ArrayList<String> info = days.get(day);
                int rowsColumn[] = calcularFilaCol(info.get(2), info.get(1));
                evacuateRange(rowsColumn);
            }

        }


        // LLENA CON CEROS UN RANGE
        public void evacuateRange(int range[])
        {
            int begin = range[0];
            int end = range[1];
            int col = range[2];

            for(int aux = begin ; aux <= end ; aux++)
            {
                evacuate(aux,col);
            }
        }


        public void evacuate(int row, int column)
        {
            cells[row][column] = 0;
        }


        // RESALTA LOS GRUPOS DISPONIBLES DEL CURSO SELECCIONADO
        public void highlightGroups(int row,int col)
        {
            String course = cellsInformation[row][col].get(0);
            String number = cellsInformation[row][col].get(1);

            ArrayList<Group> groups = coursesGroups.get(findGroupsByCourse(course)).getGroups();

            for(int group = 0 ; group < groups.size() ; group++)
            {
                Group auxiliar = groups.get(group);
                String groupNumber = "" + auxiliar.getNumber();
                if(!groupNumber.equals(number))
                {
                    ArrayList<ArrayList<String>> days = auxiliar.getSchedule();
                    for(int day = 0 ; day < days.size() ; day++)
                    {
                        ArrayList<String> info = days.get(day);
                        int rowsColumn[] = calcularFilaCol(info.get(2), info.get(1));
                        highlightCells(rowsColumn,auxiliar.getNumber());
                    }

                }
            }

        }


        // QUITA LOS GRUPOS DISPONIBLES
        public void unHighlightGroups()
        {
            for(int row = 1 ; row < cells.length ; row++)
                for(int col = 1 ; col < cells[row].length ; col++)
                {
                    if(cells[row][col] == 0)
                    {
                        TextView tv = (TextView) findViewById(row*7+col);
                        tv.setBackground(getResources().getDrawable(R.drawable.drawable_back));
                        tv.setText("");
                    }
                }
        }


        // VE SI UNA CELDA PERTENCE A UN RANGO
        public boolean isCellInRange(int cell[] , int range[])
        {
            int begin = range[0];
            int end = range[1];
            int col = range[2];

            int cellColumn = cell[1];
            int cellRow = cell[0];

            if(cellColumn==col)
            {
                for(int aux = begin ; aux <= end ; aux++)
                {
                    if(aux == cellRow)
                        return true;
                }

                return false;
            }
            else
                return false;

        }



        // CALCULA SI UN RANGO DE CELDAS ESTA VACIO
        public boolean areEmpty(int rowsColumn[])
        {
            int begin = rowsColumn[0];
            int end = rowsColumn[1];
            int col = rowsColumn[2];

            for(int aux = begin ; aux <= end ; aux++)
            {
                if(isOcuppy(aux,col))
                    return false;
            }

            return true;
        }


        // CAMBIA EN EL USUARIO EL GRUPO SELECCIONADO DE CIERTO CURSO
        public void changeGroup(int row, int col)
        {
            // CALCULAR GRUPO TOCADO
            int rowPressed = cellPressed[0];
            int colPressed = cellPressed[2];
            String course = cellsInformation[rowPressed][colPressed].get(0);
            ArrayList<Group> groups = coursesGroups.get(findGroupsByCourse(course)).getGroups();

            int newGroup[] = {row,col};

            for(int group = 0 ; group < groups.size() ; group++)
            {
                Group auxiliar = groups.get(group);
                String groupNumber = "" + auxiliar.getNumber();

                boolean allEmpty = true;
                boolean belongs = false;

                ArrayList<ArrayList<String>> days = auxiliar.getSchedule();
                for(int day = 0 ; day < days.size() ; day++)
                {
                    ArrayList<String> info = days.get(day);
                    int rowsColumn[] = calcularFilaCol(info.get(2), info.get(1));



                    if(isCellInRange(newGroup,rowsColumn))
                        belongs = true;

                    if(!areEmpty(rowsColumn))
                        allEmpty = false;
                }

                if(allEmpty && belongs)
                {
                    int oldGroup = findUsersGroupByCourse(course);
                    evacuateGroup(oldGroup);
                    userGroups.set(oldGroup,auxiliar);
                }


            }


        }


    }




    public int[] calcularFilaCol(String letraDia,String horarioClase)
    {
        int arreglo[] = new int[3];

        int diaCol = 0;

        if(letraDia.equals("L"))
            diaCol = 1;
        if(letraDia.equals("K"))
            diaCol = 2;
        if(letraDia.equals("M"))
            diaCol = 3;
        if(letraDia.equals("J"))
            diaCol = 4;
        if(letraDia.equals("V"))
            diaCol = 5;
        if(letraDia.equals("S"))
            diaCol = 6;

        arreglo[2] = diaCol;

        String[] horas = {"7:30 - 8:20","8:30 - 9:20", "9:30 - 10:20","10:30 - 11:20","11:30 - 12:20","13:00 - 13:50","14:00 - 14:50","15:00 - 15:50","16:00 - 16:50","17:00 - 17:50","18:00 - 18:50","19:00 - 19:50","20:00 - 20:50","21:00 - 21:50"};
        String inicioFinal[] = horarioClase.split(" ");

        for(int h = 0 ; h < horas.length ; h++)
        {
            String[] hs = horas[h].split(" ");
            if(inicioFinal[0].equals(hs[0]))
                arreglo[0] = h;
            if(inicioFinal[2].equals(hs[2]))
                arreglo[1] = h;

        }

        arreglo[0]++;
        arreglo[1]++;

        return arreglo;
    }




    public int[] calculateCellTouched(float x,float y)
    {

        // CALCULAR LA FILA =======================================================================

        float altoMalla = 0;
        for(int row = 0 ; row < 15 ; row++)
        {
            TextView tv = (TextView) findViewById(row*7+0);
            altoMalla += tv.getHeight();
        }

        float barSize = height - altoMalla;
        y = y - barSize;

        float altoMallaTemporal = 0;
        int touchedRow = 0;
        for(int row = 0 ; row < 15 ; row++)
        {
            TextView tv = (TextView) findViewById(row*7+0);
            altoMallaTemporal += tv.getHeight();
            if(altoMallaTemporal > y)
            {
                touchedRow = row;
                break;
            }
        }


        // CALCULAR LA COLUMNA ====================================================================

        float largoMallaTemporal = 0;
        int touchedCol = 0;
        for(int col = 0 ; col < 7 ; col++)
        {
            TextView tv = (TextView) findViewById(7+col);
            largoMallaTemporal += tv.getWidth();
            if(largoMallaTemporal > x)
            {
                touchedCol = col;
                break;
            }
        }

        int rowColumn[] = {touchedRow,touchedCol};

        return rowColumn;

    }



    public int indexOfCourse(String course)
    {
        for(int index = 0 ; index < courses.size() ; index++)
        {
            if(courses.get(index).equals(course))
                return index;
        }

        return -1;
    }




    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float x = e.getX();
        float y = e.getY();

        int rowColumn[] = calculateCellTouched(x,y);
        int row = rowColumn[0];
        int col = rowColumn[1];

        int event = e.getAction();

        if(row < 15 && row > 0 && col < 7 && col > 0 )
        {
            if(event ==  MotionEvent.ACTION_DOWN)
            {
                if(schedule.isOcuppy(row,col))
                {
                    schedule.pressed(row,col);
                    schedule.highlightGroups(row,col);
                }
            }

            if(event == MotionEvent.ACTION_UP)
            {
                if(schedule.isPressing())
                {
                    schedule.changeGroup(row,col);
                    schedule.unHighlightGroups();
                    fillGroupsInSchedule();
                    schedule.dropped();
                }
            }
        }

        else
        {
            if(event == MotionEvent.ACTION_UP)
            {
                if(schedule.isPressing())
                {
                    schedule.unHighlightGroups();
                    fillGroupsInSchedule();
                    schedule.dropped();
                }
            }
        }

        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

