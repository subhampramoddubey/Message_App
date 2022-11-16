package com.example.message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText inputName, inputEmail, inputPass, inputConfirmPass;
    TextView loginTextview;
    Button createAcc;
    ProgressDialog progressdialog;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView textView = findViewById(R.id.textView1);
        textView.setText("Create Account");

        inputName = findViewById(R.id.signupName);
        inputEmail = findViewById(R.id.signUpEmail);
        inputPass = findViewById(R.id.signUpPass);
        inputConfirmPass = findViewById(R.id.confirmPass);
        loginTextview = findViewById(R.id.goto_LoginText);
        createAcc = findViewById(R.id.createAccButton);

        progressdialog = new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perfornAuth();
            }
        });


    }

    private void perfornAuth() {

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String pass = inputPass.getText().toString();
        String confirmPass = inputConfirmPass.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Input Valid Email");
        } else if (pass.isEmpty()) {
            inputPass.setError("Enter Password");
        } else if (confirmPass.isEmpty() || confirmPass.equals(email)) {
            inputConfirmPass.setError("Does not match");
        } else {
            progressdialog.setTitle("Registration");
            progressdialog.setMessage("Please wait...");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.show();


            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        UserId = mAuth.getUid();
                        DocumentReference documentReference = firestore.collection("users").document(UserId);
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("email", email);
                        user.put("pass", pass);
                        documentReference.set(user);
                        progressdialog.dismiss();
                        //Log Tag is missimg ? or add onSuccessfulListner()

                        startActivity(new Intent(getApplicationContext(), ChatSection.class));
                        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }
}