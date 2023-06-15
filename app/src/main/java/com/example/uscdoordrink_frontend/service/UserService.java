package com.example.uscdoordrink_frontend.service;

import android.util.Log;

import androidx.annotation.NonNull;


import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "UserService";

    public void register(User u, final OnSuccessCallBack<Void> successCallBack,
                         final OnFailureCallBack<Exception> failureCallBack) {
        DocumentReference documentReference = db.collection("User").document(u.getUserName());
        documentReference.set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot added");
                successCallBack.onSuccess(unused);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
                failureCallBack.onFailure(e);
            }
        });

    }


    public void updateStoreUID(String name, String UID){
        DocumentReference docRef = db.collection("User").document(name);
        docRef.update("storeUID", UID);
    }

    public void addUserRequest(String name, Request r){
        DocumentReference docRef = db.collection("User").document(name);
        docRef.update("orderHistory", FieldValue.arrayUnion(r));
    }

    public void changeUserRequest(String name, ArrayList<Request> request){
        DocumentReference docRef = db.collection("User").document(name);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                u.setOrderHistory(request);
                docRef.set(u);
            }
        });
    }

    public void deleteUserByName(String name,
                                 final OnSuccessCallBack<Void> successCallBack,
                                 final OnFailureCallBack<Exception> failureCallBack){
        DocumentReference docRef = db.collection("User").document(name);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Successfully deleted User with name: " + name);
                successCallBack.onSuccess(unused);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "delete not successful", e);
                failureCallBack.onFailure(e);
            }
        });
    }

}