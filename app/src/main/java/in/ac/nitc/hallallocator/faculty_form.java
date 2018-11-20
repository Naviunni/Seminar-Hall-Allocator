package in.ac.nitc.hallallocator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class faculty_form extends AppCompatActivity {

    private static final String TAG = "faculty_form";

    private EditText bookDate;
    private EditText email;
    private EditText capcity;
    private DatePickerDialog.OnDateSetListener bookDateListener;
    private Spinner hallname;


    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("/request");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);
        bookDate=(EditText) findViewById(R.id.bookingDate);

        email =(EditText) findViewById(R.id.facEmail);
        capcity =(EditText) findViewById(R.id.reqCapacity);
        hallname = findViewById(R.id.hallName);


        String[] items = new String[]{"Kalam Hall", "CSED 202", "CSED Seminar Hall", "ELHC 401"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        hallname.setAdapter(adapter);

        bookDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal= Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        faculty_form.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        bookDateListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                }
            });
            bookDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month=month+1;
                    Log.d(TAG,"onDateSet:Date:"+year+"/"+month+"/"+day);

                    String date=day+"/"+month+"/"+year;
                    bookDate.setText(date);

                }
            };

    }
    public void submit(View view){
        DatabaseReference newBooking =  database.push();
        newBooking.child("hid").setValue(hallname.getSelectedItem().toString());
        newBooking.child("fid").setValue(email.getText().toString());
        newBooking.child("sname").setValue("nil");
        newBooking.child("status").setValue("Requested");
        newBooking.child("date").setValue(bookDate.getText().toString());
        Intent myIntent = new Intent(this, Faculty_Main.class);
        this.startActivity(myIntent);

    }
}
