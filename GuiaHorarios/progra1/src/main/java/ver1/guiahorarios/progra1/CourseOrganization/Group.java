package ver1.guiahorarios.progra1.CourseOrganization;

import java.util.ArrayList;

/**
 * Created by sanchosv on 18/04/14.
 */
public class Group {

    private String id;
    private String location;
    private int number;
    private String course_id;
    private ArrayList<ArrayList<String>> schedule;
    private ArrayList<String> sched_string;
    private String teacher;

    public Group(){}

    public Group(String pCourseId, int pNumber, ArrayList<String> pHorarios, String pTeacher)
    {
        course_id = pCourseId;
        number = pNumber;
        teacher = pTeacher;

        schedule = new ArrayList<ArrayList<String>>();
        for(int index = 0 ; index < pHorarios.size(); index++)
        {
            ArrayList<String> aux = new ArrayList<String>();
            String horario[] = pHorarios.get(index).split("/");
            aux.add(horario[0]);
            aux.add(horario[1]);
            aux.add(horario[2]);
            schedule.add(aux);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public ArrayList<ArrayList<String>> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<ArrayList<String>> schedule) {
        this.schedule = schedule;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public ArrayList<String> getSchedString()
    {
        sched_string = new ArrayList<String>();
        for(int i = 0;i<schedule.size();i++)
        {
            sched_string.add(schedToString(i));
        }
        return sched_string;
    }

    @Override
    public String toString()
    {
        return "Numero: " + number + " Profesor: " + teacher;
    }

    public String schedToString(int index)
    {
        return "Día: " + schedule.get(index).get(2) + "\n" + "Hora: " +schedule.get(index).get(1)  + "\n" +"Aula: " + schedule.get(index).get(0);
    }


}
