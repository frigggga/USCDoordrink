package com.example.uscdoordrink_frontend.whiteboxTest;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

import android.util.Log;

import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.Store;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.OrderService;
import com.example.uscdoordrink_frontend.whiteboxTest.storeManage.StoreServiceTest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderServiceTest {

    private final String TAG = "OrderServiceTest";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OrderService orderService;
    private Request testRequest;
    private String UID = "VNU8salaXDOGcyMIn6o7";
    ArrayList<Request> requests = new ArrayList<Request>();
    ArrayList<Order> orders = new ArrayList<>();
    private Callable<Boolean> hasStore(){
        return () -> testRequest != null;
    }

    private static class Result{
        boolean completed = false;
        boolean success;
        String message;
        void complete(boolean result, String inputMsg) {
            completed = true;
            success = result;
            message = inputMsg;
        }
        Callable<Boolean> hasCompleted() {return () -> completed;}
    }

    @Before
    public void setUp() throws Exception {
        orders.add(new Order("lemonade", "aaaa", 1, 2.0, 0.0));
        orderService = new OrderService();
        testRequest = new Request("2022-04-13T10:46:57.500Z", "userTest", "33333333", "44444", UID, 10.0, orders);
    }

    @Test
    public void addRequestTest() {
        Request r = testRequest;
        testRequest = r;
        final Result addRequestResult = new Result();
        orderService.addRequest(r, new OnSuccessCallBack<Void>() {
            @Override
            public void onSuccess(Void input) {
                Log.d(TAG, "add request successful");
                addRequestResult.complete(true, "add request successful");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "failed to add request", input);
                addRequestResult.complete(false, input.getMessage());
            }
        });

        await().atMost(10, TimeUnit.SECONDS).until(addRequestResult.hasCompleted());
        assertTrue("addRequestResult", addRequestResult.success);
        assertEquals("addRequestResult msg", "add request successful", addRequestResult.message);
    }

    @Test
    public void updateRequestTest() {
        orderService.updateRequest(testRequest, "status", "2");
        final Result updateRequestResult = new Result();
        DocumentReference docRef = db.collection("Request").document(testRequest.getName()).collection("Orders").document(testRequest.getStart());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Request r = documentSnapshot.toObject(Request.class);
                assertEquals("updateRequestResult ", "2", r.getStatus());
                updateRequestResult.complete(true, "update request successful");

            }
        });

        await().atMost(10, TimeUnit.SECONDS).until(updateRequestResult.hasCompleted());
        assertTrue("addRequestResult", updateRequestResult.success);
        assertEquals("addRequestResult msg", "update request successful", updateRequestResult.message);

    }

    @Test
    public void z_deleteRequestTest(){
        final Result deleteRequestResult = new Result();
        orderService.deleteRequest(testRequest,
                new OnSuccessCallBack<Void>() {
                    @Override
                    public void onSuccess(Void input) {
                        Log.d(TAG, "delete request successful");
                        deleteRequestResult.complete(true, "delete request successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to delete request");
                        deleteRequestResult.complete(false, input.getMessage());
                    }
                });
        await().atMost(5, TimeUnit.SECONDS).until(deleteRequestResult.hasCompleted());
        assertTrue("deleteRequestResult", deleteRequestResult.success);
        assertEquals("deleteRequestResultMsg", "delete request successful", deleteRequestResult.message);

    }
}