package in.ac.nitc.hallallocator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class student_form extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);


        Button facFormSubmit = (Button)findViewById(R.id.stud_FormSubmit);

        final Spinner hall = (Spinner)findViewById(R.id.stud_hallName);
        final Spinner facmail = (Spinner) findViewById(R.id.stud_facName);
        List<String> halls = new ArrayList<>();
        halls.add("Kalam Hall");
        halls.add("CSED 202");
        halls.add("CSED Seminar Hall");
        halls.add("ELHC 401");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, halls);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hall.setAdapter(dataAdapter);

        List<String> emails = new ArrayList<>();
        emails.add("fac01@nitc.ac.in");
        emails.add("fac02@nitc.ac.in");
        emails.add("fac@nitc.ac.in");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, emails);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facmail.setAdapter(dataAdapter2);

        final EditText  bookingDate = (EditText) findViewById(R.id.stud_bookingDate);
        final EditText  smail = (EditText) findViewById(R.id.stud_Email);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("request");
        bookingDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(student_form.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date = day+"/"+month+"/"+year;
                bookingDate.setText(date);

            }
        };
        facFormSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = facmail.getSelectedItem().toString();
                String hall_list = hall.getSelectedItem().toString();
                String date = bookingDate.getText().toString();
                String sname = smail.getText().toString();
                DatabaseReference newBooking =  ref.push();
                newBooking.child("date").setValue(date);
                newBooking.child("fid").setValue(email);
                newBooking.child("hid").setValue(hall_list);
                newBooking.child("sname").setValue(sname);
                newBooking.child("status").setValue("Waiting List");
                Intent intent = new Intent(getApplicationContext(),Student_Main.class);
                startActivity(intent);

            }
        });




    }
}
