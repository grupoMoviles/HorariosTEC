package com.proyecto1.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Victor on 4/5/14.
 */
public class Group implements Parcelable{

    public String course_name;
    public String teacher;
    public int group_number;
    public String location;
    public ArrayList<String> schedule;
    boolean selected;

    public Group(Parcel in)
    {
        this.course_name = in.readString();
        this.teacher = in.readString();
        this.group_number = in.readInt();
    }

    public Group(String pCourse_name, String pTeacher, int pGroup_number)
    {
        course_name = pCourse_name;
        teacher = pTeacher;
        group_number = pGroup_number;

    }

    @Override
    public String toString() {
        return course_name + "      " + group_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(course_name);
        parcel.writeString(teacher);
        parcel.writeInt(group_number);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Group createFromParcel(Parcel in)
        {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size)
        {
            return new Group[size];
        }
    };
}
