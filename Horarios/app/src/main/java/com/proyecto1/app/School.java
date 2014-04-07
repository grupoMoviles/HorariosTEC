package com.proyecto1.app;

import java.util.ArrayList;
/**
 * Created by Victor on 4/5/14.
 */
public class School {

    public String school_name;
    public ArrayList<Group> groupList = new ArrayList<Group>();

    public School(String pName, ArrayList<Group> pGroups)
    {
        school_name = pName;
        groupList = pGroups;
    }
}
