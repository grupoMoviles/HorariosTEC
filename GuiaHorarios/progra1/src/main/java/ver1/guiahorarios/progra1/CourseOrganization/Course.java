package ver1.guiahorarios.progra1.CourseOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor on 4/5/14.
 */
public class Course implements Parcelable{


    public Course(Parcel in)
    {
        this.course_name = in.readString();
        //this.teacher = in.readString();
        //this.group_number = in.readInt();
    }

    public Course(String pCourse_name,String pCode,int pCredits,String pSchool)
    {
        course_name = pCourse_name;
        code=pCode;
        credits=pCredits;
        school=pSchool;

    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return course_name ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(course_name);
       // parcel.writeString(teacher);
       // parcel.writeInt(group_number);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Course createFromParcel(Parcel in)
        {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size)
        {
            return new Course[size];
        }
    };

    private int credits;
    private String school;
    boolean selected;
    private String course_name;
    private int id;
    private String code;
}

