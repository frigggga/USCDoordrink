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
import android.widget.TextView;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifyProfile extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView userName;
    EditText password, contactInformation;
    Button modify, returnToProfile;
    ImageView eye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        password = findViewById(R.id.et_newPassword);
        contactInformation = findViewById(R.id.et_newCi);
        modify = findViewById(R.id.btn_modify);
        returnToProfile = findViewById(R.id.btn_returnProfile);
        userName = findViewById(R.id.et_accountInModify);
        userName.setText(Constants.currentUser.getUserName());
        eye = findViewById(R.id.iv_see_password_modify);

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

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("User").document(Constants.currentUser.getUserName());
                docRef.update("password", password.getText().toString());
                docRef.update("contactInformation", contactInformation.getText().toString());
                Constants.currentUser.setPassword(password.getText().toString());
                Constants.currentUser.setContactInformation(contactInformation.getText().toString());
                Toast.makeText(ModifyProfile.this, "Modify successfully!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ModifyProfile.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
        returnToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModifyProfile.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}