package in.ac.nitc.hallallocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private static View view;
    private static CheckBox show_hide_password;
    private static EditText emailid, password;

    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressBar spinner;
    private static Button login;
    private static TextView forgot_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEmailField = findViewById(R.id.emailid);
        mPasswordField = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        CheckBox mCbShowPwd;
        login = (Button) findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                //forgot_pwd.setVisibility(View.GONE);
                signIn();
            }
        });
        spinner = (ProgressBar) findViewById(R.id.loading);
        spinner.setVisibility(View.GONE);
        mCbShowPwd = (CheckBox) findViewById(R.id.show_hide_password);

        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }


    public void signIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            login.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            forgot_pwd.setVisibility(View.VISIBLE);
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference FacultyRef = database.child("faculty");
                        DatabaseReference StudentRef = database.child("student");
                        DatabaseReference InchargeRef = database.child("incharges");
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            final String uid = user.getUid();
                            Log.d(TAG, "UserID:" + uid);
                          //  Log.d(TAG,"Name is "+StudentRef.child(uid));
                            StudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(uid).exists()) {
                                        Log.d(TAG, "Student exist");
                                        StudentExist();
                                        return;
                                    } else {
                                        Log.d(TAG, "Student does not exist");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG, "In onCancelled Student");
                                }
                            });
                            FacultyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(uid).exists()) {
                                        Log.d(TAG, "Faculty exist");
                                        FacultyExist();
                                        return;
                                    } else {
                                        Log.d(TAG, "Faculty does not exist");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG, "In onCancelled Faculty");
                                }
                            });

                            InchargeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(uid).exists()) {
                                        Log.d(TAG, "In charge exist");
                                        InChargeExist();
                                        return;
                                    } else {
                                        Log.d(TAG, "In charge does not exist");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG, "In onCancelled Student");
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure" );
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            login.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            //forgot_pwd.setVisibility(View.VISIBLE);
                        }
                    }
                });
        return;
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


    private void StudentExist() {
        Log.d(TAG, "In student exist");
        Log.d(TAG, "Start Student Intent");
       Intent myIntent = new Intent(this, Student_Main.class);
        this.startActivity(myIntent);
        return;
    }

    private void InChargeExist() {
        Log.d(TAG, "In charge exist");
        Log.d(TAG, "Start Incharge Intent");
       Intent myIntent = new Intent(this, Incharge_Main.class);
        this.startActivity(myIntent);
        return;
    }
    private void FacultyExist() {
        Log.d(TAG, "In faculty exist");
        Log.d(TAG, "Start Faculty Intent");
      Intent myIntent = new Intent(this, Faculty_Main.class);
        this.startActivity(myIntent);
        return;
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
}
