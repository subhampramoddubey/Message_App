package com.example.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText inputEmail, inputPass;
    TextView forgetPass, createNewAcc;
    Button loginBtn;


    ProgressDialog progressdialog;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView textView = findViewById(R.id.textView1);
        textView.setText("LOG IN");

        inputEmail = findViewById(R.id.loginEmail);
        inputPass = findViewById(R.id.loginPass);
        forgetPass = findViewById(R.id.forgetPassLogintxt);
        createNewAcc = findViewById(R.id.createAccTxt);
        loginBtn = findViewById(R.id.loginButton);

        progressdialog = new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();;


        if (mUser != null) {

            Intent i = new Intent(LoginActivity.this, ChatSection.class);
            startActivity(i);
        }


        createNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   performLogin();
            }
        });


    }

    private void performLogin() {

        String email = inputEmail.getText().toString();
        String pass = inputPass.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Input Valid Email");
        } else if (pass.isEmpty()) {
            inputPass.setError("Enter Password");
        } else {
            progressdialog.setTitle("Login");
            progressdialog.setMessage("Please wait...");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.show();


            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressdialog.dismiss();

                        startActivity(new Intent(getApplicationContext(), ChatSection.class));
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    }else {
                        progressdialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed.Please Retry", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }
}
