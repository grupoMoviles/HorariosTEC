package ver1.guiahorarios.progra1.CourseOrganization;

import java.util.HashMap;

/**
 * Created by sanchosv on 19/04/14.
 */
public class GroupsSelected {

private static GroupsSelected _instance;

        private GroupsSelected()
        {
            hash_groups = new HashMap<String, Group>();
        }

        public static GroupsSelected getInstance()
        {
            if(_instance==null)
            {
                _instance = new GroupsSelected();
            }
            return _instance;
        }


        public HashMap<String, Group> getHash_groups() {
            return hash_groups;
        }

        public void setHash_groups(HashMap<String, Group> hash_groups) {
            this.hash_groups = hash_groups;
        }

        /*public void fillGroupHash()
        {
            for(int i=0;i<CoursesSelected.getInstance().getCourse_list().size();i++)
            {
                hash_groups.put(CoursesSelected.getInstance().getCourse_list().get(i).getCode(),CoursesSelected.getInstance().getCourse_list().get(i));
            }
        }*/

private HashMap<String,Group> hash_groups;
}
