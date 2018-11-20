package in.ac.nitc.hallallocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import static java.security.AccessController.getContext;

public class Fac_Notifications extends Fragment {
    private static final String TAG = "Fac_Notifications";
    ListView bookings;

    private Button navDash;
    private Button navNotif;

    private DatabaseReference db3 = FirebaseDatabase.getInstance().getReference().child("request");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String UID = mAuth.getUid();
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("/").child("faculty").child(UID).child("fac_email");
    private String name ,hname;

    private String hallName,date,fid,sname,status;
    private ArrayList<Booking> halls = new ArrayList<Booking>();;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fac_notifications,container,false);
        navDash=(Button) view.findViewById(R.id.dash);
        navNotif=(Button) view.findViewById(R.id.notif);



        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                Log.d(TAG,"erwer   "+name);
                checkList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        navDash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(),"Navigating to Dashboard",Toast.LENGTH_SHORT).show();
                ((Faculty_Main)getActivity()).setViewPager(0);
            }
        });

        navNotif.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(),"Navigating to Notifications",Toast.LENGTH_SHORT).show();
                ((Faculty_Main)getActivity()).setViewPager(1);
            }
        });



        return view;

    }
    void checkList() {
        bookings = (ListView) view.findViewById(R.id.fac_request);
        db3.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Booking booking;
                for (DataSnapshot hallShot : dataSnapshot.getChildren()) {
                    String temp = "Waiting List";
                    hname = hallShot.child("fid").getValue(String.class);
                    sname = hallShot.child("sname").getValue(String.class);
                    status = hallShot.child("status").getValue(String.class);
                    Log.d(TAG, "hname " + hname + "name " + name);
                    if (hname.equals(name) && !sname.equals("nil") && status.equals(temp)) {
                        Log.d(TAG, "In if");
                        hallName = hallShot.child("hid").getValue(String.class);
                        date = hallShot.child("date").getValue(String.class);
                        fid = hallShot.child("fid").getValue(String.class);
                        status = hallShot.child("status").getValue(String.class);
                        booking = new Booking(hallName, date, fid, sname, status);

                        halls.add(booking);
                        Log.d(TAG, "hallnames   " + date);
                        Log.d(TAG, " request hall list size is" + halls.size());
                    }
                }
                setAdapter();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void setAdapter()
    {
        if(halls.size()>0) {
            Log.d(TAG, "Calling adapter" +halls.size() );
            StudRequestAdapter studRequestAdapter = new StudRequestAdapter(getContext(), halls);
            bookings.setAdapter(studRequestAdapter);

        }
    }

//    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
//    private long mBackPressed;
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
//        {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            return;
//        }
//        else { Toast.makeText(getActivity().getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }
//
//        mBackPressed = System.currentTimeMillis();
//    }
}
