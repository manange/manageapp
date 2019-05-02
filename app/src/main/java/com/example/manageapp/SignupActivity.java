package com.example.manageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignupActivity extends AppCompatActivity {
    private EditText inputName,inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    DatabaseReference mdatabase;
    String Name,Email;

@Override
    protected void onCreate(Bundle saveInsatanceState) {
        super.onCreate(saveInsatanceState);
        setContentView(R.layout.signup_activity);

   //Get Firebase auth instance
    auth = FirebaseAuth.getInstance();
    mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");




    btnSignIn = (Button) findViewById(R.id.btn_label_login);
    btnSignUp = (Button) findViewById(R.id.btn_register);
    inputName = (EditText) findViewById(R.id.name);
    inputEmail = (EditText) findViewById(R.id.email);
    inputPassword = (EditText) findViewById(R.id.password);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    //Move Login

    btnSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        }
    });

    btnSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    btnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String username=inputName.getText().toString().trim();
            final String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)){
                Toast.makeText(getApplicationContext(), "Enter your username please!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            //create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

//                            if (task.isSuccessful()){
//                                User user = new User(username,email);
//                                FirebaseDatabase.getInstance().getReference("Users")
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                finish();
                            }

                        }
                    });

        }
    });
}



//
//    private void OnAuth(FirebaseUser user) {
//        createAnewUser(user.getUid());
//    }

//    private void createAnewUser(String uid) {
//        User user = BuildNewuser();
//        mdatabase.child(uid).setValue(user);
//    }


//    private User BuildNewuser(){
//        return new User(
//                getDisplayName(),
//                getUserEmail(),
//                new Date().getTime()
//        );
//    }
//
//    public String getDisplayName() {
//        return Name;
//    }
//
//    public String getUserEmail() {
//        return Email;
//    }

}
