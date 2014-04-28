package ver1.guiahorarios.progra1.CourseOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */
public class CourseSelected {

    private static CourseSelected _instance;

    private CourseSelected(){}

    public static CourseSelected getInstance()
    {
        if(_instance==null)
        {
            _instance = new CourseSelected();
        }
        return _instance;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    private Course course;
}
