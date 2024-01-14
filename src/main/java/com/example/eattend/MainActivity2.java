package com.example.eattend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {
    private EditText CollegeName,ProfName,Semester,Subject,Branch,Roll;
   String CLG,PRO,SEM,SUB,BRA;
   int ROL;
   private Button Start;
   private Button Developerc;
   public static final String ROLL = "ROLL";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        
        CollegeName = (EditText) findViewById(R.id.collegeName);
        ProfName = (EditText) findViewById(R.id.profName);
        Semester = (EditText) findViewById(R.id.semr);
        Subject = (EditText) findViewById(R.id.subject);
        Branch = (EditText) findViewById(R.id.branch);
        Roll = (EditText) findViewById(R.id.roll);
        Start = (Button) findViewById(R.id.start);
        Developerc = (Button) findViewById(R.id.developerc);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String st1 = prefs.getString("CLG","");
        CollegeName.setText(st1);

        String st2 = prefs.getString("PRO","");
        ProfName.setText(st2);

        String st3 = prefs.getString("SEM","");
        Semester.setText(st3);

        String st4 = prefs.getString("SUB","");
        Subject.setText(st4);

        String st5 = prefs.getString("BRA","");
        Branch.setText(st5);

        int n1 = prefs.getInt("ROL",0);
        Roll.getText();


        CollegeName.addTextChangedListener(detailsTextWatcher);
        ProfName.addTextChangedListener(detailsTextWatcher);
        Semester.addTextChangedListener(detailsTextWatcher);
        Subject.addTextChangedListener(detailsTextWatcher);
        Branch.addTextChangedListener(detailsTextWatcher);
        Roll.addTextChangedListener(detailsTextWatcher);
        Start.setEnabled(false);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    CLG = CollegeName.getText().toString();
                    PRO = ProfName.getText().toString();
                    SEM = Semester.getText().toString();
                    SUB = Subject.getText().toString();
                    BRA = Branch.getText().toString();
                    ROL = Integer.parseInt(Roll.getText().toString());
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("CLG", CLG);
                    editor.putString("PRO", PRO);
                    editor.putString("SEM", SEM);
                    editor.putString("SUB", SUB);
                    editor.putString("BRA", BRA);
                    editor.putInt("ROL", ROL);
                    editor.apply();

                    sendData();
            }
        });

        Developerc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity2.this,MainActivity4.class);
                startActivity(intent);
            }
        });
    }

    public void sendData(){
        int RLN = Integer.parseInt(Roll.getText().toString());
        String clg = CollegeName.getText().toString().trim();
        String sem = Semester.getText().toString().trim();
        String bra = Branch.getText().toString().trim();
        String prof = ProfName.getText().toString().trim();
        String sub = Subject.getText().toString().trim();
        Intent intent= new Intent(MainActivity2.this,MainActivity3.class);
        intent.putExtra(MainActivity3.ROLLL,RLN);
        intent.putExtra(MainActivity3.CLG,clg);
        intent.putExtra(MainActivity3.SEM,sem);
        intent.putExtra(MainActivity3.BRA,bra);
        intent.putExtra(MainActivity3.SUB,sub);
        intent.putExtra(MainActivity3.PROF,prof);
        startActivity(intent);
    }

    private TextWatcher detailsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String collegeNameInput = CollegeName.getText().toString();
            String ProfNameInput = ProfName.getText().toString();
            String SemesterInput = Semester.getText().toString();
            String SubjectInput = Subject.getText().toString();
            String BranchInput = Branch.getText().toString();
            String RollInput = Roll.getText().toString();

            Start.setEnabled(!collegeNameInput.isEmpty()&&!ProfNameInput.isEmpty()&&!SemesterInput.isEmpty()&&!SubjectInput.isEmpty()&&!BranchInput.isEmpty()&&!RollInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}