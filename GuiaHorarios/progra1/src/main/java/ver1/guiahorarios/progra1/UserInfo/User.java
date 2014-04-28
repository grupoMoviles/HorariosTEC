package ver1.guiahorarios.progra1.UserInfo;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.CourseOrganization.Course;
import ver1.guiahorarios.progra1.CourseOrganization.Group;

/**
 * Created by sanchosv on 18/04/14.
 */
public class User {

    public User(){}

    public User(String pEmail,String pPassword)
    {
        course_list = new ArrayList<Course>();
        group_list = new ArrayList<Group>();
    }

    private String email;
    private String facebook;
    private String password;
    private ArrayList<Course> course_list;
    private ArrayList<Group> group_list;
    private boolean hasFacebook;

    public boolean isHasFacebook() {
        return hasFacebook;
    }

    public void setHasFacebook(boolean hasFacebook) {
        this.hasFacebook = hasFacebook;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Course> getCourse_list() {
        return course_list;
    }

    public void setCourse_list(ArrayList<Course> course_list) {
        this.course_list = course_list;
    }

    public ArrayList<Group> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(ArrayList<Group> group_list) {
        this.group_list = group_list;
    }
}
