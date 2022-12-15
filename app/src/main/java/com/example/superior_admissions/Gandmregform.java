package com.example.superior_admissions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.superior_admissions.databinding.ActivityGandmregformBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Gandmregform extends AppCompatActivity {

    ActivityGandmregformBinding binding;
    String Name,Fname, email, Mobile_num,cnic1,fcnic,mat,inte,d1,d2, pass, register;

    long subfee = 139400;
    long collfee =1;
    float markfee=1;
    float semfee;
    float fee;
    int adfee = 20000, mischarge = 7500;
    float per, percent, d25= 0.75F, d30 = 0.65F, d40 = 0.60F, d50= 0.50F;
    RadioButton degree1, degree2, sup, other;
    String cldisc, markdisc, disc;

    FirebaseDatabase db;
    DatabaseReference reference;
    RadioGroup radioClass, programclass, college;

    RadioButton selectedBtn, selectedprog, selectclg;

    TextInputEditText name,fname, eml, pswd, mob, cnic, cnic2, matmarks, intmarks, regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seregform);

        binding = ActivityGandmregformBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        degree1 = findViewById(R.id.intdegree);
        degree2 = findViewById(R.id.intdegree2);
        sup = findViewById (R.id.sup);
        other = findViewById (R.id.other);
        radioClass = findViewById(R.id.radioClass);
        college = findViewById (R.id.college);
        programclass = findViewById(R.id.programclass);
        name = findViewById(R.id.userName);
        fname = findViewById (R.id.fname);
        eml = findViewById(R.id.Email);
        pswd = findViewById(R.id.password);
        mob = findViewById(R.id.Mobileno);
        cnic = findViewById(R.id.cnic);
        cnic2 = findViewById(R.id.fcnc);
        matmarks = findViewById(R.id.matric1);
        intmarks = findViewById(R.id.inter1);
        regist = findViewById (R.id.registeration);

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = binding.userName.getText().toString();
                Fname = binding.fname.getText ().toString ();
                email = binding.Email.getText().toString();
                Mobile_num = binding.Mobileno.getText().toString();
                cnic1 = binding.cnic.getText().toString();
                fcnic = binding.fcnc.getText().toString();
                mat = binding.matric1.getText().toString();
                inte = binding.inter1.getText().toString();
                pass = binding.password.getText().toString();
                register = binding.registeration.getText ().toString ();

                int selectedId = radioClass.getCheckedRadioButtonId();
                int selectedid = programclass.getCheckedRadioButtonId();
                int select = college.getCheckedRadioButtonId ();
                // find the radiobutton by returned id
                selectedBtn = (RadioButton) findViewById(selectedId);
                selectedprog = findViewById(selectedid);
                selectclg = findViewById (select);


                if(sup.isChecked ())
                {
                    collfee = subfee/2;
                    cldisc = "50%";
                }
                else if(other.isChecked()){
                    collfee = subfee;
                    cldisc = "0%";
                }

                if(Integer.parseInt (inte)>=770 && Integer.parseInt (inte)<825){
                    markfee = (subfee * d25);
                    markdisc = "25%";

                }
                if(Integer.parseInt (inte)>=825 && Integer.parseInt (inte)<880)
                {
                    markfee =  (subfee * d30);
                    markdisc = "30%";
                }
                if(Integer.parseInt (inte)>=880 && Integer.parseInt (inte)<935)
                {
                    markfee =  (subfee * d40);
                    markdisc = "40%";
                }
                if(Integer.parseInt (inte)>=935 && Integer.parseInt (inte)<=1100)
                {
                    System.out.println (d50);
                    markfee =  (subfee * d50);
                    markdisc = "50%";
                }

                if(collfee>=markfee){
                    semfee = markfee;
                    disc = markdisc;

                }
                else if(markfee>collfee){
                    semfee = collfee;
                    disc = cldisc;
                }

                fee = semfee + adfee + mischarge;

                Users user = new Users(Name,Fname,email,Mobile_num,cnic1,fcnic,mat,inte,selectedBtn.getText ().toString (),pass,selectedprog.getText().toString(),selectclg.getText().toString(),register, String.valueOf((int) fee),disc);

                if (binding.userName.getText().toString().isEmpty()){
                    binding.userName.setError("Name cannot be empty!");
                }
                else if (binding.Email.getText().toString().isEmpty()){
                    binding.Email.setError("Email cannot be empty!");
                }
                else if (binding.Mobileno.getText().toString().isEmpty()){
                    binding.Mobileno.setError("Mobile Number cannot be empty!");
                }
                else if (binding.Mobileno.getText().toString().trim().length() != 11){
                    binding.Mobileno.setError("Invalid mobile number!");
                }
                else if (binding.cnic.getText().toString().isEmpty()){
                    binding.cnic.setError("Your CNIC number cannot be empty!");
                }
                else if (binding.cnic.getText().toString().trim().length() != 13){
                    binding.cnic.setError("Invalid CNIC number!");
                }
                else if (binding.fcnc.getText().toString().isEmpty()){
                    binding.fcnc.setError("Your father's CNIC number cannot be empty!");
                }
                else if (binding.fcnc.getText().toString().trim().length() != 13){
                    binding.fcnc.setError("Invalid CNIC number!");
                }
                else if (binding.matric1.getText().toString().isEmpty()){
                    binding.matric1.setError("Marks cannot be left empty");
                }
                else if (Integer.parseInt(binding.matric1.getText().toString()) < 550){
                    binding.matric1.setError("You cannot apply in this program with marks less than 550");
                }
                else if (binding.inter1.getText().toString().isEmpty()){
                    binding.inter1.setError("Marks cannot be left empty");
                }
                else if (Integer.parseInt(binding.inter1.getText().toString()) < 550){
                    binding.inter1.setError("You cannot apply in this program with marks less than 550");
                }


                else {
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Student20");
                    reference.child(cnic1).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            binding.userName.setText("");
                            binding.Email.setText("");
                            binding.Mobileno.setText("");
                            binding.cnic.setText("");
                            binding.fcnc.setText("");
                            binding.matric1.setText("");
                            binding.inter1.setText("");
                            binding.password.setText("");


                            Toast.makeText(Gandmregform.this,"Successfuly Registered",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Gandmregform.this, Subjects.class);
                            intent.putExtra("cnic", cnic1);
                            intent.putExtra("degree", selectedprog.getText());
                            if (selectedprog.getText().equals("BS Gaming and Multimedia")){
                                intent.putExtra("department", "gm");
                            }
                            startActivity(intent);
                        }
                    });
                }

            }




        });
    }
}