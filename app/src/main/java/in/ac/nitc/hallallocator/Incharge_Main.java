package in.ac.nitc.hallallocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import static java.security.AccessController.getContext;

public class Incharge_Main extends AppCompatActivity {
    private Button logout;
    private FirebaseAuth.AuthStateListener listener;
    private static final String TAG = "Main Incharge";
     ListView bookings;
    public ArrayList<Booking> halls = new ArrayList<Booking>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incharge_main);
        bookings = (ListView) findViewById(R.id.office_hall_list);
        logout = (Button) findViewById(R.id.incharge_logout);
        setupFirebaseListener();
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
            }
        });
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("request");
        Log.d(TAG, "In Incharge function   " );

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking booking ;
                Log.d(TAG, "In listener   " );
                halls.clear();
                for(DataSnapshot hallShot : dataSnapshot.getChildren()){
                    Log.d(TAG, "In listener   for loop" );
                    String temp = "YES";
                    String temp2 = "Requested";
                    String hallName,date,fid,sname;
                    String status = hallShot.child("status").getValue(String.class);

                    if(status.equals(temp) || status.equals(temp2)) {
                        Log.d(TAG, "In listener   for loop if cond" );
                        hallName= hallShot.child("hid").getValue(String.class);
                        date = hallShot.child("date").getValue(String.class);
                        fid = hallShot.child("fid").getValue(String.class);
                        sname = hallShot.child("sname").getValue(String.class);

                        booking = new Booking(hallName,date,fid,sname,status);

                        halls.add(booking);
                        Log.d(TAG, "hallnames   " + date);
                        Log.d(TAG,"booking hall list size is"+ halls.size());
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
            HallsRequestAdapter hallsRequestAdapter = new HallsRequestAdapter(this, halls);
            bookings.setAdapter(hallsRequestAdapter);

        }
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
