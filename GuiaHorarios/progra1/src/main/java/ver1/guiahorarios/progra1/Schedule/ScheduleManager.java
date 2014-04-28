package ver1.guiahorarios.progra1.Schedule;

import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;

/**
 * Created by sanchosv on 27/04/14.
 */
public class ScheduleManager extends Observable implements Observer {


    private static ScheduleManager _instance;

    private ScheduleManager()
    {
        obtainedData = false;
        GroupConn.getInstance().addObserver(this);
    }

    public static ScheduleManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new ScheduleManager();
        }
        return _instance;
    }

    public void getScheduleArray()
    {
        ArrayList<Group> grupos = new ArrayList<Group>(GroupsSelected.getInstance().getHash_groups().values());
        for(int i=0;i< grupos.size();i++)
        {
            Schedule1.getInstance().fillRange(grupos.get(i).getSchedule());
        }
        obtainedData = true;
        setChanged();
        notifyObservers();
    }

    public boolean obtainedData;

    @Override
    public void update(Observable observable, Object o) {
        Log.e("Se dio cuenta", "SchedManager");
        getScheduleArray();
    }
}
