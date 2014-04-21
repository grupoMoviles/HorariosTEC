package ver1.guiahorarios.progra1.CourseOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */
public class GroupSelected {

    private static GroupSelected _instance;

    private GroupSelected(){}

    public static GroupSelected getInstance()
    {
        if(_instance==null)
        {
            _instance = new GroupSelected();
        }
        return _instance;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private Group group;
}