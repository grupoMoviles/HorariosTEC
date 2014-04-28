package ver1.guiahorarios.progra1.CourseOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */
import java.util.ArrayList;
/**
 * Created by Victor on 4/5/14.
 */
public class School {

    public String school_name;
    public ArrayList<Course> courseList = new ArrayList<Course>();

    public School(String pName, ArrayList<Course> pCourses)
    {
        school_name = pName;
        courseList = pCourses;
    }
}
