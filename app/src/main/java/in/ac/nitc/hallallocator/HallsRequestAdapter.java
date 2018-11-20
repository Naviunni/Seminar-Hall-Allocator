package in.ac.nitc.hallallocator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class HallsRequestAdapter extends ArrayAdapter<Booking> {
    public HallsRequestAdapter(@NonNull Context context, @NonNull ArrayList<Booking> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Booking b = getItem(position);
        Log.d(TAG, "In hall request Adapter");
        final String date = b.getDate();
        final String sname = b.getSname();
        final String hname = b.getHallName();
        final String status = b.getStatus();
        final String mail = b.getFid();
        View ListItem = convertView;
        if (ListItem == null) {
            ListItem = LayoutInflater.from(getContext()).inflate(R.layout.booking_list_item2, parent, false);
        }
        TextView hallView = (TextView) ListItem.findViewById(R.id.hname);
        TextView dateView = (TextView) ListItem.findViewById(R.id.hdate);
        TextView studView = (TextView) ListItem.findViewById(R.id.studname);
        TextView statusView = (TextView) ListItem.findViewById(R.id.status);
        statusView.setText(mail);
        hallView.setText(hname);
        dateView.setText(date);
        studView.setText(sname);
        Button accept = (Button) ListItem.findViewById(R.id.office_accept);
        Button reject = (Button) ListItem.findViewById(R.id.office_reject);


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot hallShot : dataSnapshot.getChildren()) {
                            if (hallShot.child("date").getValue(String.class).equals(date) &&
                                    hallShot.child("sname").getValue(String.class).equals(sname) &&
                                    hallShot.child("hid").getValue(String.class).equals(hname) &&
                                    hallShot.child("fid").getValue(String.class).equals(mail)) {
                                String id = hallShot.getKey();
                                Log.d(TAG, "id to be changed" + id);
                                ref.child(id).child("status").setValue("REJECTED");
                                Toast.makeText(getContext(),"Request Rejected",Toast.LENGTH_SHORT).show();
                                clearlist();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot hallShot : dataSnapshot.getChildren()) {
                            if (hallShot.child("date").getValue(String.class).equals(date) &&
                                    hallShot.child("sname").getValue(String.class).equals(sname) &&
                                    hallShot.child("hid").getValue(String.class).equals(hname) &&
                                    hallShot.child("fid").getValue(String.class).equals(mail)) {
                                String id = hallShot.getKey();
                                Log.d(TAG, "id to be changed" + id);
                                ref.child(id).child("status").setValue("ACCEPTED");
                                Toast.makeText(getContext(),"Request Accepted",Toast.LENGTH_SHORT).show();
                                clearlist();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        return ListItem;
    }

    public void clearlist() {
        Intent myIntent = new Intent(getContext(), Incharge_Main.class);
        getContext().startActivity(myIntent);
    }
}

