package ver1.guiahorarios.progra1.CourseOrganization;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanchosv on 18/04/14.
 */
public class CoursesSelected {

    private static CoursesSelected _instance;

    private CoursesSelected()
    {
        course_list = new ArrayList<Course>();
        hash_courses = new HashMap<String, Course>();
    }

    public static CoursesSelected getInstance()
    {
        if(_instance==null)
        {
            _instance = new CoursesSelected();
        }
        return _instance;
    }

    public ArrayList<Course> getCourse_list() {
        return course_list;
    }

    public void setCourse_list(ArrayList<Course> course_list) {
        this.course_list = course_list;
    }

    public HashMap<String, Course> getHash_courses() {
        return hash_courses;
    }

    public void setHash_courses(HashMap<String, Course> hash_courses) {
        this.hash_courses = hash_courses;
    }

    public void fillCourseHash()
    {
        for(int i=0;i<CoursesSelected.getInstance().getCourse_list().size();i++)
        {
            hash_courses.put(CoursesSelected.getInstance().getCourse_list().get(i).getCode(),CoursesSelected.getInstance().getCourse_list().get(i));
        }
    }

    private HashMap<String,Course> hash_courses;
    private ArrayList<Course> course_list;

}
