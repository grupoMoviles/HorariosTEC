package ver1.guiahorarios.progra1.Schedule;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.CourseOrganization.Group;

/**
 * Created by sanchosv on 20/04/14.
 */
public class GruposPorCurso
{
    private ArrayList<Group> groups ;
    private String course ;

    public GruposPorCurso(String pCourse)
    {
        groups = new ArrayList<Group>();
        course = pCourse;
    }

    public GruposPorCurso(String pCourse,ArrayList<Group> pGroup)
    {
        groups = pGroup;
        course = pCourse;
    }

    public String getCourse()
    {
        return course;
    }

    public ArrayList<Group> getGroups()
    {
        return groups;
    }

    public void addGroup(Group group)
    {
        groups.add(group);
    }
}
