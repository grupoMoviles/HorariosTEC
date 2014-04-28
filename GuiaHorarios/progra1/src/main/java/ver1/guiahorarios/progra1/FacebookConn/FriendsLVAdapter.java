package ver1.guiahorarios.progra1.FacebookConn;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.util.List;

import ver1.guiahorarios.progra1.R;

/**
 * Created by sanchosv on 19/04/14.
 */
public class FriendsLVAdapter  extends ArrayAdapter<RowItem> {

        Context context;

        public FriendsLVAdapter(Context context, int resourceId,
        List<RowItem> items) {
            super(context, resourceId, items);
            this.context = context;
        }

    /*private view holder class*/
        private class ViewHolder {
            ProfilePictureView imageView;
            TextView tv_name;
        }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.friends_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.FI_Name);
            holder.imageView = (ProfilePictureView) convertView.findViewById(R.id.FI_Profile);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tv_name.setText(rowItem.getName());
        holder.imageView.setProfileId(rowItem.getFacebook_id());

        return convertView;
    }
}

