package ver1.guiahorarios.progra1.TeacherOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */
public class TeacherSelected {

    private static TeacherSelected _instance;

    private TeacherSelected(){}

    public static TeacherSelected getInstance()
    {
        if(_instance==null)
        {
            _instance = new TeacherSelected();
        }
        return _instance;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    private Teacher teacher;

}
