package com.example.uscdoordrink_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * @Author: Yuxiang Zhang
 * @Date: 2022/3/23 01:03
 */

public class SignUpActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText userName, password, contactInformation;
    Button login, register;
    RadioGroup select;
    ImageView eye;
    String TAG = "SignUpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.et_account);
        password = findViewById(R.id.et_password);
        contactInformation = findViewById((R.id.et_ci));
        register = findViewById(R.id.btn_modify);
        login = findViewById(R.id.Login);
        select = (RadioGroup)findViewById(R.id.select);

        eye = findViewById(R.id.if_see_password);

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }else{
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService s = new UserService();

                //check if username already exists
                DocumentReference docRef = db.collection("User").document(userName.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Toast.makeText(SignUpActivity.this, "Username already exits, please log in.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No such document");
                                //sign up
                                signUp();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(SignUpActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void signUp(){
            int id = select.getCheckedRadioButtonId();
            UserService s = new UserService();
            switch (id) {
                case R.id.customer:
                    User u1 = new User(userName.getText().toString(), password.getText().toString(), contactInformation.getText().toString(), UserType.CUSTOMER);
                    s.register(u1, new OnSuccessCallBack<Void>() {
                        @Override
                        public void onSuccess(Void input) {
                            Toast.makeText(SignUpActivity.this, "Customer sign up successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, new OnFailureCallBack<Exception>() {
                        @Override
                        public void onFailure(Exception input) {
                            Toast.makeText(getApplicationContext(),
                                    "sign up failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case R.id.seller:
                    User u2 = new User(userName.getText().toString(), password.getText().toString(), contactInformation.getText().toString(), UserType.SELLER);
                    s.register(u2, new OnSuccessCallBack<Void>() {
                        @Override
                        public void onSuccess(Void input) {
                            Toast.makeText(SignUpActivity.this, "Seller sign up successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, new OnFailureCallBack<Exception>() {
                        @Override
                        public void onFailure(Exception input) {
                            Toast.makeText(getApplicationContext(),
                                    "sign up failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                default:
                    Toast.makeText(SignUpActivity.this, "Please select your user type!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
