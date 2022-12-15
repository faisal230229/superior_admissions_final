package com.example.superior_admissions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superior_admissions.databinding.ActivityCsregfromBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Csregfrom extends AppCompatActivity {


    ActivityCsregfromBinding binding;
    String Name, email, Mobile_num,cnic1,fcnic,mat,inte,d1,d2, pass, register, Fname;
    long subfee = 163200;
    long collfee =1;
    float markfee=1;
    float semfee;
    float fee;
    int adfee = 20000, mischarge = 7500;
    float per, percent, d25= 0.75F, d30 = 0.65F, d40 = 0.60F, d50= 0.50F;

    RadioButton degree1, degree2, sup, other;
    Uri imgUrl;
    ImageView pdfBtn;

    FirebaseDatabase db;
    DatabaseReference reference;
    StorageReference storageReference;
    RadioGroup radioClass, programclass, college;
    String cldisc, markdisc, disc;

    RadioButton selectedBtn, selectedprog, selectclg;

    TextInputEditText name, eml, pswd, mob, cnic, cnic2, matmarks, intmarks, regist, fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csregfrom);

        binding = ActivityCsregfromBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        degree1 = findViewById(R.id.intdegree);
        degree2 = findViewById(R.id.intdegree2);
        radioClass = findViewById(R.id.radioClass);
        college = findViewById (R.id.college);
        programclass = findViewById(R.id.programclass);
        name = findViewById(R.id.userName);
        eml = findViewById(R.id.Email);
        pswd = findViewById(R.id.password);
        mob = findViewById(R.id.Mobileno);
        cnic = findViewById(R.id.cnic);
        cnic2 = findViewById(R.id.fcnc);
        sup = findViewById (R.id.sup);
        other = findViewById (R.id.other);
        matmarks = findViewById(R.id.matric1);
        intmarks = findViewById(R.id.inter1);
        regist = findViewById (R.id.registeration);
        fname = findViewById (R.id.fname);
        pdfBtn = findViewById(R.id.pdfBtn);
        storageReference = FirebaseStorage.getInstance().getReference();

        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPdfFile();
            }
        });

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
                    System.out.println(collfee);
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
                else if(Integer.parseInt (inte)>=825 && Integer.parseInt (inte)<880)
                {
                    markfee =  (subfee * d30);
                    markdisc = "30%";
                }
                else if(Integer.parseInt (inte)>=880 && Integer.parseInt (inte)<935)
                {
                    markfee =  (subfee * d40);
                    markdisc = "40%";
                }
                else if(Integer.parseInt (inte)>=935 && Integer.parseInt (inte)<=1100)
                {
                    markfee =  subfee * d50;
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

//                System.out.println(semfee);
//                System.out.println(adfee);
//                System.out.println(mischarge);

                fee = semfee + adfee + mischarge;

//                System.out.println((int) fee);

                Users user = new Users(Name, Fname,email,Mobile_num,cnic1,fcnic,mat,inte, selectedBtn.getText ().toString (), pass, selectedprog.getText().toString(),selectclg.getText().toString(),register, String.valueOf((int) fee),disc, imgUrl.toString());

                if (binding.userName.getText().toString().isEmpty()){
                    binding.userName.setError("Name cannot be empty!");
                }
                else if(binding.fname.getText ().toString ().isEmpty ()){
                    binding.fname.setError ("Father Name cannot be empty!");
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
                else if(binding.registeration.getText().toString ().isEmpty ()){
                    binding.registeration.setError ("Please enter a registeration Number!");
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



                            Toast.makeText(Csregfrom.this,"Successfuly Registered",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Csregfrom.this, Subjects.class);
                            intent.putExtra("cnic", cnic1);
                            intent.putExtra("degree", selectedprog.getText());

                            if (selectedprog.getText().equals("BS Computer Science")){
                                intent.putExtra("department", "cs");
                            }
                            else if (selectedprog.getText().equals("BS Cyber Security") ){
                                System.out.println(selectedprog.getText());
                                intent.putExtra("department", "cyber");
                            }
                            else if (selectedprog.getText().equals("BS Artificial Intelligence")){
                                intent.putExtra("department", "ai");
                            }
                            else if (selectedprog.getText().equals("BS Information Technology")){
                                intent.putExtra("department", "it");
                            }
                            startActivity(intent);
                        }
                    });
                }

            }

        });
    }
    private void selectPdfFile() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i, "Select PDF File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            uploadPDFFile(data.getData());

        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploads/"+ System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        imgUrl = uri.getResult();

                        pdfBtn.setImageResource(R.drawable.tick);

                        Toast.makeText(Csregfrom.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();

                        progressDialog.setMessage("Uploaded: "+ (int)progress+ "%");
                    }
                });
    }
}