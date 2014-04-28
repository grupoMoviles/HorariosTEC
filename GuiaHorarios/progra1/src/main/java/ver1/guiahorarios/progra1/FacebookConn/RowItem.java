package ver1.guiahorarios.progra1.FacebookConn;

/**
 * Created by sanchosv on 19/04/14.
 */
public class RowItem {

    public RowItem(String pId,String pName)
    {
        facebook_id = pId;
        name = pName;
    }

    private String facebook_id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
