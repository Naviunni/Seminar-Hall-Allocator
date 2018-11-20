package in.ac.nitc.hallallocator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HallsAdapter extends ArrayAdapter<Booking> {
    public HallsAdapter(@NonNull Context context, @NonNull ArrayList<Booking> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Booking b = getItem(position);

        String date = b.getDate();
        String sname = b.getSname();
        String hname = b.getHallName();
        String status = b.getStatus();
        View ListItem = convertView;
        if(ListItem == null){
            ListItem = LayoutInflater.from(getContext()).inflate(R.layout.booking_list_item,parent,false);
        }
        TextView hallView = (TextView)ListItem.findViewById(R.id.hname);
        TextView dateView = (TextView)ListItem.findViewById(R.id.hdate);
        TextView studView = (TextView)ListItem.findViewById(R.id.studname);
        TextView statusView = (TextView)ListItem.findViewById(R.id.status);
        statusView.setText(status);
        hallView.setText(hname);
        dateView.setText(date);
        studView.setText(sname);
        return ListItem;
    }
}
