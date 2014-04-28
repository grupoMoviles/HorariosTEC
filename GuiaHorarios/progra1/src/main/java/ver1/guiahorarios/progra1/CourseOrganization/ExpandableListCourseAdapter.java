package ver1.guiahorarios.progra1.CourseOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import ver1.guiahorarios.progra1.R;

public class ExpandableListCourseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ExpandableListView expListView;
    private int[] groupStatus;
    private List<School> school_list;
    public static Boolean checked[] = new Boolean[1];

    public ExpandableListCourseAdapter(Context pContext, ExpandableListView pExpandableListView,
                                       List<School> pGroupCollection) {
        context = pContext;
        school_list = pGroupCollection;
        expListView = pExpandableListView;
        groupStatus = new int[school_list.size()];
        setListEvent();
    }


    private void setListEvent() {
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int arg0) {
                groupStatus[arg0] = 1;
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int arg0) {
                groupStatus[arg0] = 0;
            }
        });
    }
    @Override
    public String getChild(int arg0, int arg1) {
        return school_list.get(arg0).courseList.get(arg1).getCourse_name();
    }
    @Override
    public long getChildId(int arg0, int arg1) {
        return arg1;
    }
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean arg2, View convertView,ViewGroup parent)
    {
        final courseInfo tempCourseInfo;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.explist_item, null);
            tempCourseInfo = new courseInfo();
            tempCourseInfo.name=(TextView)convertView.findViewById(R.id.TV_ExpCourse);
            convertView.setTag(tempCourseInfo);
        } else {
            tempCourseInfo = (courseInfo) convertView.getTag();
        }
        tempCourseInfo.name.setText(school_list.get(groupPosition).courseList.get(childPosition).getCourse_name());

        /*if(CoursesSelected.getInstance().getHash_courses().containsKey(school_list.get(groupPosition).courseList.get(childPosition).getId())) {
            tempCourseInfo.name.setChecked(true);
        } else {
            tempCourseInfo.name.setChecked(false);
        }*/
        return convertView;
    }
    @Override
    public int getChildrenCount(int arg0) {
        return school_list.get(arg0).courseList.size();
    }
    @Override
    public Object getGroup(int arg0) {
        return school_list.get(arg0);
    }
    @Override
    public int getGroupCount() {
        return school_list.size();
    }
    @Override
    public long getGroupId(int arg0) {
        return arg0;
    }
    @Override
    public View getGroupView(int groupPosition, boolean arg1, View view, ViewGroup parent) {
        schoolInfo tempSchoolInfo;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.explist_group,null);
            tempSchoolInfo = new schoolInfo();
            tempSchoolInfo.title = (TextView) view.findViewById(R.id.TV_SchoolName);
            view.setTag(tempSchoolInfo);
        } else {
            tempSchoolInfo = (schoolInfo) view.getTag();
        }
        tempSchoolInfo.title.setText(school_list.get(groupPosition).school_name);
        return view;
    }
    class schoolInfo {
        TextView title;
    }
    class courseInfo {
        TextView name;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}

