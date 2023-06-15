package com.example.uscdoordrink_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.OrderNotificationService;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.OrderBy;

/**
 * @Author: Yuxiang Zhang
 * @Date: 2022/3/23 01:03
 */

public class LoginActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "UserService";
    EditText userName, password;
    Button login, register;
    ImageView eye;
    CheckBox remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.et_account);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.Register);
        remember = findViewById(R.id.checkBox);
        eye = findViewById(R.id.iv_see_password);

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


        //get username and password from shared preferences
        SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
        if(preferences.getBoolean("remember_me", false)){
            userName.setText(preferences.getString("full_name", "name"));
            password.setText(preferences.getString("password", "password"));
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get value from fields
                String name = userName.getText().toString();
                String p = password.getText().toString();

                //remember me functionality
                if(remember.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("remember_me", true);
                    editor.putString("full_name",name);
                    editor.putString("password", p);
                    editor.commit();

                }else{
                    SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("remember_me", false);
                }

                //validate data from firebase
                DocumentReference docRef = db.collection("User").document(name);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                DocumentReference docRef = db.collection("User").document(name);
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User u = documentSnapshot.toObject(User.class);
                                        if(u.getPassword().equals(p)){
                                            Log.d(TAG, "auth correct");
                                            Constants.currentUser = u;
                                            String toast = getString(R.string.login_toast);
                                            Toast.makeText(LoginActivity.this, toast, Toast.LENGTH_SHORT).show();
                                            Intent i;
                                            if (u.getUserType() == UserType.SELLER && (u.getStoreUID() == null || "toBeAssigned".equals(u.getStoreUID()))){
                                                i = new Intent(LoginActivity.this, AddStoreActivity.class);
                                            }else{
                                                i = new Intent(LoginActivity.this, ProfileActivity.class);
                                            }
                                            startActivity(i);
                                            finish();
                                        } else{
                                            Toast.makeText(LoginActivity.this, "Wrong password, please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                                Toast.makeText(LoginActivity.this, "cannot find this username, please register first.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(LoginActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}