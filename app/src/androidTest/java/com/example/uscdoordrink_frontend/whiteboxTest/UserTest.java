package com.example.uscdoordrink_frontend.whiteboxTest;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.uscdoordrink_frontend.entity.Order;
import com.example.uscdoordrink_frontend.entity.Request;
import com.example.uscdoordrink_frontend.entity.User;
import com.example.uscdoordrink_frontend.entity.UserType;
import com.example.uscdoordrink_frontend.service.UserService;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class UserTest {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "UserTest";
    ArrayList<Request> requests = new ArrayList<Request>();
    String start_time = Instant.now().toString();
    ArrayList<Order> orders = new ArrayList<>();
    User u = new User("test", "123456", "3333333333", UserType.CUSTOMER);
    Request r1;

    @Test
    public void addOrderToHistoryTest() {
        orders.add(new Order("lemonade", "aaaa", 1, 2.0, 0.0));
        r1 = new Request(start_time, "test1", "1234", "1234", "aaaa", 10.0, orders);
        requests.add(r1);
        u.addOrderToHistory(r1);
        assertEquals("addOrderToHistory", requests, u.getOrderHistory());

    }

    @After
    public void tearDown(){
        UserService userService = new UserService();
        userService.changeUserRequest(u.getUserName(), new ArrayList<Request>());
    }
}