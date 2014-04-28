package ver1.guiahorarios.progra1.TeacherOrganization;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ver1.guiahorarios.progra1.R;

/**
 * Created by sanchosv on 27/04/14.
 */
public class TeacherInfoAdapter extends BaseAdapter {
    private Context context;
    private List<Teacher> teacher_list = new ArrayList<Teacher>();
    List<Teacher> arraylist = new ArrayList<Teacher>();

    public TeacherInfoAdapter(Context pContext, ArrayList<Teacher> menu){
        context = pContext;
        teacher_list = menu;
        arraylist.addAll(menu);
    }

    public int getCount() {
        return teacher_list.size();
    }

    public Object getItem(int position) {
        return teacher_list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View myView = convertView;
        Teacher teacher = (Teacher) getItem(position);
        if (convertView == null){
            LayoutInflater inf = LayoutInflater.from(context);
            myView = inf.inflate(R.layout.teacher_list_item,null);
        }

        TextView name = (TextView) myView.findViewById(R.id.LI_TV_teacher_name);
        RatingBar rating = (RatingBar) myView.findViewById(R.id.LI_ratingBar);

        name.setText(teacher.getName());
        float f = (float)teacher.getAverageRating();
        rating.setRating(f);

        myView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context,TeacherInfo_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TeacherSelected.getInstance().setTeacher(teacher_list.get(position));
                context.startActivity(i);
            }
        });

        return myView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("LLega al filtro",charText);
        teacher_list.clear();
        if (charText.length() == 0) {
            teacher_list.addAll(arraylist);
        }
        else {
            Log.e("Largo arrayList",""+arraylist.size());
            for (Teacher tp : arraylist) {
                Log.e("NOMBRE PROFE NO ENCUENTRA",tp.getName());
                if (tp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Log.e("NOMBRE PROFE",tp.getName());
                    teacher_list.add(tp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
