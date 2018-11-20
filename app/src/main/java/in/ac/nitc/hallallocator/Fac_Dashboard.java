package in.ac.nitc.hallallocator;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Fac_Dashboard extends Fragment {
    private static final String TAG = "Fac_Dashboard";

    private Button navDash;
    private Button navNotif;
    private Button navform;
    private Button logout;
    private ListView bookings;
    private DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("/booking");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener listener;
    private String UID = mAuth.getUid();
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("/").child("faculty").child(UID).child("fac_email");
    private DatabaseReference db3 = FirebaseDatabase.getInstance().getReference().child("request");
    private String name ;
    private ArrayList<Booking> halls = new ArrayList<Booking>();;
    private String hname ;
    private String hallName,date,fid,sname,status;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fac_dashboard,container,false);
        Log.d(TAG,"uididjdiji"+UID);

        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  name = dataSnapshot.getValue(String.class);
                Log.d(TAG,"erwer   "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navDash=(Button) view.findViewById(R.id.dash);
        navNotif=(Button) view.findViewById(R.id.notif);
        navform=(Button) view.findViewById(R.id.navForm);
        bookings= (ListView)view.findViewById(R.id.bookings);
        logout = (Button) view.findViewById(R.id.fac_logout);
        setupFirebaseListener();
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
            }
        });


//        halls.add("h1");
//        halls.add("h2");

        halls.clear();

        db1.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Booking booking ;
                for(DataSnapshot hallShot : dataSnapshot.getChildren()){

                    hname = hallShot.child("fid").getValue(String.class);
                    sname = hallShot.child("sname").getValue(String.class);
                    Log.d(TAG, "hname   " + hname);
                    if(hname.equals(name)) {
                        hallName= hallShot.child("hid").getValue(String.class);
                        date = hallShot.child("date").getValue(String.class);
                        fid = hallShot.child("fid").getValue(String.class);
                        sname = hallShot.child("sname").getValue(String.class);
                        status = hallShot.child("status").getValue(String.class);
                        booking = new Booking(hallName,date,fid,sname,status);

                        halls.add(booking);
                        Log.d(TAG, "hallnames   " + date);
                        Log.d(TAG,"booking hall list size is"+ halls.size());
                    }
                }
                HallsAdapter hallsAdapter = new HallsAdapter(getContext(),halls);
                bookings.setAdapter(hallsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        db3.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Booking booking ;
                for(DataSnapshot hallShot : dataSnapshot.getChildren()){

                    hname = hallShot.child("fid").getValue(String.class);
                    status = hallShot.child("status").getValue(String.class);
                    Log.d(TAG, "hname   " + hname);
                    if(hname.equals(name) && !status.equals("Waiting List") && !status.equals("NO")) {
                        hallName= hallShot.child("hid").getValue(String.class);
                        date = hallShot.child("date").getValue(String.class);
                        fid = hallShot.child("fid").getValue(String.class);
                        sname = hallShot.child("sname").getValue(String.class);

                        status = hallShot.child("status").getValue(String.class);
                        booking = new Booking(hallName,date,fid,sname,status);

                        halls.add(booking);
                        Log.d(TAG, "hallnames   " + date);
                        Log.d(TAG," request hall list size is"+ halls.size());
                    }
                }
                HallsAdapter hallsAdapter = new HallsAdapter(getContext(),halls);
                bookings.setAdapter(hallsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        String[] vals = new String[]{"h1","h2"};



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

        navform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),faculty_form.class);
                startActivity(intent);

            }
        });


        return view;

    }
    private void setupFirebaseListener(){
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){

                }
                else{
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(listener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(listener);
        }
    }
}
