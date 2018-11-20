package in.ac.nitc.hallallocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Main extends AppCompatActivity {


    private DatabaseReference db3 = FirebaseDatabase.getInstance().getReference().child("request");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener listener;
    private String UID = mAuth.getUid();
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("/").child("student").child(UID).child("stud_email");
    private String name ,hname;
    private static final String TAG = "Main Student";
    private String hallName,date,fid,sname,status;
    private ArrayList<Booking> halls = new ArrayList<Booking>();
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__main);

        Button navform= (Button) findViewById(R.id.navFormStud);
        logout = (Button) findViewById(R.id.stud_logout);
        setupFirebaseListener();
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
            }
        });
        navform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"starting Intent");
                Intent intent = new Intent(getApplicationContext(),student_form.class);
                startActivity(intent);
            }
        });

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



    }

    void checkList()
    {
        final ListView bookings = (ListView) findViewById(R.id.hall_list);
        db3.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Booking booking ;
                for(DataSnapshot hallShot : dataSnapshot.getChildren()){

                    hname = hallShot.child("sname").getValue(String.class);
                    Log.d(TAG, "hname " + hname + "name "+name);
                    if( hname.equals(name)) {
                        Log.d(TAG,"In if");
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
                HallsAdapter hallsAdapter = new HallsAdapter(getApplicationContext(),halls);
                bookings.setAdapter(hallsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
    private void setupFirebaseListener(){
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){

                }
                else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(listener);
        }
    }
}
