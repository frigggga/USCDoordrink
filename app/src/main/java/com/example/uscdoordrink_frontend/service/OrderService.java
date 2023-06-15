package com.example.uscdoordrink_frontend.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class OrderService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "OrderService";

    public void addRequest(@NonNull Request r, final OnSuccessCallBack<Void> successCallBack,
                           final OnFailureCallBack<Exception> failureCallBack) {
        DocumentReference customerRef = db.collection("Request").document(r.getName()).collection("Orders").document(r.getStart());
        customerRef.set(r).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        DocumentReference sellerRef = db.collection("Request").document(r.getStoreUID()).collection("Orders").document(r.getStart());
        sellerRef.set(r).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateRequest(Request r, String type, String value) {
        //update request(status) for both customers and sellers
        DocumentReference customRef = db.collection("Request").document(r.getName()).collection("Orders").document(r.getStart());
        DocumentReference sellerRef = db.collection("Request").document(r.getStoreUID()).collection("Orders").document(r.getStart());
        customRef.update(type, value);
        sellerRef.update(type, value);
    }

    public void deleteRequest(Request r, final OnSuccessCallBack<Void> successCallBack,
                              final OnFailureCallBack<Exception> failureCallBack){
        DocumentReference customRef = db.collection("Request").document(r.getName()).collection("Orders").document(r.getStart());
        DocumentReference sellerRef = db.collection("Request").document(r.getStoreUID()).collection("Orders").document(r.getStart());
        customRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Successfully deleted customer request");
                successCallBack.onSuccess(unused);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "delete not successful", e);
                failureCallBack.onFailure(e);
            }
        });

        sellerRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Successfully deleted seller request");
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
