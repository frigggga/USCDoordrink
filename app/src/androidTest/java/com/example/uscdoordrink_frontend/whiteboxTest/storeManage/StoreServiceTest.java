package com.example.uscdoordrink_frontend.whiteboxTest.storeManage;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import static org.awaitility.Awaitility.*;

import android.provider.Contacts;
import android.util.Log;

import com.example.uscdoordrink_frontend.entity.Drink;
import com.example.uscdoordrink_frontend.entity.Store;
import com.example.uscdoordrink_frontend.service.CallBack.OnFailureCallBack;
import com.example.uscdoordrink_frontend.service.CallBack.OnSuccessCallBack;
import com.example.uscdoordrink_frontend.service.StoreService;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.cucumber.java.bs.A;


/**
 * @Author: Yuxuan Liao
 * @Date: 2022/4/9 23:19
 */

@RunWith(AndroidJUnit4.class)
public class StoreServiceTest {

    private final String TAG = "StoreServiceTest";
    private StoreService storeService;
    private Store testStore;
    private Callable<Boolean> hasStore(){
        return () -> testStore != null;
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
    public void init(){
        storeService = new StoreService();
        testStore = null;
    }

    @Test
    public void getStoreTest(){
        //Try to fetch a store already in database, and examine if it is intact.
        String storeUID = "iCeVSwKCgwMVcDdQFJGv";
        storeService.getStoreByUID(storeUID,
                new OnSuccessCallBack<Store>() {
                    @Override
                    public void onSuccess(Store input) {
                        testStore = input;
                        Log.d(TAG, "fetching successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "fetching failed", input);
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(hasStore());
        assertEquals("storeUID", storeUID, testStore.getStoreUID());
        assertEquals("storeName", "boba time", testStore.getStoreName());
        assertEquals("addressString", "3617 S Vermont Ave, Los Angeles, CA 90007, USA",
                testStore.getAddressString());
        assertEquals("hashLocation", "9q5cs1w5pp", testStore.getHashLocation());
        assertEquals("storeAddress", new LatLng(34.0224656, -118.2922312),
                testStore.getStoreAddress());
        Drink drink = new Drink();
        drink.setPrice(5.5);
        drink.setStoreUID(storeUID);
        drink.setDrinkName("boba");
        drink.setDiscount(0.1);
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("milk");
        drink.setIngredients(ingredients);
        ArrayList<Drink> menu = new ArrayList<>();
        menu.add(drink);
        assertEquals("menu", menu, testStore.getMenu());

        //Try to fetch a store that does not exist, and expect to fail
        final Result failGetResult = new Result();
        storeService.getStoreByUID("Impossible Store",
                new OnSuccessCallBack<Store>() {
                    @Override
                    public void onSuccess(Store input) {
                        Log.w(TAG, "impossible success with store: " + input.getStoreUID());
                        failGetResult.complete(true, "impossible success with store: " + input.getStoreUID());
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.d(TAG, "fails as expected with msg " + input.getMessage());
                        failGetResult.complete(false, "expected failure");
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(failGetResult.hasCompleted());
        assertFalse("failGetResult", failGetResult.success);
        assertEquals("failGetResultMsg", "expected failure", failGetResult.message);
    }

    @Test
    public void addStoreRemoveStoreTest(){
        // add a store to Firestore
        Store s = new Store();
        s.setStoreName("Atelier Crenn");
        s.setStoreAddress(37.7983, -122.4360);
        s.setAddressString("3127 Fillmore St., San Francisco, 94123, United States");
        List<Drink> menu = new ArrayList<>();

        Drink drink1 = new Drink();
        drink1.setPrice(20);
        drink1.setDrinkName("Brandy");

        Drink drink2 = new Drink();
        drink2.setPrice(75);
        drink2.setDrinkName("Single Malt Whiskey");
        drink2.setDiscount(5);
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Water");
        ingredients.add("Oak");
        drink2.setIngredients(ingredients);

        menu.add(drink1);
        menu.add(drink2);
        s.setMenu(menu);
        final Result addStoreResult = new Result();
        String storeUID = storeService.addStore(s, new OnSuccessCallBack<Void>() {
            @Override
            public void onSuccess(Void input) {
                Log.d(TAG, "add store successful");
                addStoreResult.complete(true, "add store successful");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "failed to add store", input);
                addStoreResult.complete(false, input.getMessage());
            }
        });
        await().atMost(3, TimeUnit.SECONDS).until(addStoreResult.hasCompleted());
        assertNotNull("storeUID", storeUID);
        assertTrue("addStoreResult", addStoreResult.success);
        assertEquals("addStoreResult msg", "add store successful", addStoreResult.message);

        // Try to fetch the store just added and compare it to local store
        assertNull(testStore);
        storeService.getStoreByUID(storeUID,
                new OnSuccessCallBack<Store>() {
                    @Override
                    public void onSuccess(Store input) {
                        testStore = input;
                        Log.d(TAG, "fetching successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "fetching failed", input);
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(hasStore());
        assertEquals("compare s and store from the database", s, testStore);

        //Remove the store just added
        final Result removeStoreResult = new Result();
        storeService.deleteStoreByUID(storeUID,
                new OnSuccessCallBack<Void>() {
                    @Override
                    public void onSuccess(Void input) {
                        Log.d(TAG, "remove store successful");
                        removeStoreResult.complete(true, "remove store successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to remove store with ID:" + storeUID, input);
                        removeStoreResult.complete(false, input.getMessage());
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(removeStoreResult.hasCompleted());
        assertTrue("removeStoreResult", removeStoreResult.success);
        assertEquals("removeStoreResultMsg", "remove store successful", removeStoreResult.message);

        //Try to fetch the store again and expect to fail
        final Result failGetResult = new Result();
        storeService.getStoreByUID(storeUID,
                new OnSuccessCallBack<Store>() {
                    @Override
                    public void onSuccess(Store input) {
                        Log.w(TAG, "impossible success with store: " + input.getStoreUID());
                        failGetResult.complete(true, "impossible success with store: " + input.getStoreUID());
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.d(TAG, "fails as expected with msg " + input.getMessage());
                        failGetResult.complete(false, "expected failure");
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(failGetResult.hasCompleted());
        assertFalse("failGetResultMsg", failGetResult.success);
        assertEquals("failGetResultMsg", "expected failure", failGetResult.message);
    }

    @Test
    public void updateStoreTest(){
        // add a Store to database
        Store s = new Store();
        s.setStoreName("Atelier Crenn");
        s.setStoreAddress(37.7983, -122.4360);
        s.setAddressString("3127 Fillmore St., San Francisco, 94123, United States");
        List<Drink> menu = new ArrayList<>();

        Drink drink1 = new Drink();
        drink1.setPrice(20);
        drink1.setDrinkName("Brandy");
        menu.add(drink1);
        s.setMenu(menu);
        final Result addStoreResult = new Result();
        String storeUID = storeService.addStore(s, new OnSuccessCallBack<Void>() {
            @Override
            public void onSuccess(Void input) {
                Log.d(TAG, "add store successful");
                addStoreResult.complete(true, "add store successful");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "failed to add store", input);
                addStoreResult.complete(false, input.getMessage());
            }
        });
        await().atMost(3, TimeUnit.SECONDS).until(addStoreResult.hasCompleted());
        assertNotNull("storeUID", storeUID);
        assertTrue("addStoreResult", addStoreResult.success);
        assertEquals("addStoreResult msg", "add store successful", addStoreResult.message);

        //update the store
        Drink drink2 = new Drink();
        drink2.setPrice(75);
        drink2.setDrinkName("Single Malt Whiskey");
        drink2.setDiscount(5);
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Water");
        ingredients.add("Oak");
        drink2.setIngredients(ingredients);

        menu.add(drink2);
        assertEquals("The size of the menu of the local Store", 2, s.getMenu().size());
        assertNotNull("The storeUID of the store after adding", s.getStoreUID());
        final Result updateStoreResult = new Result();
        storeService.updateStore(s, new OnSuccessCallBack<Void>() {
            @Override
            public void onSuccess(Void input) {
                Log.d(TAG, "update store successful");
                updateStoreResult.complete(true, "update store successful");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "failed to update store", input);
                updateStoreResult.complete(false, input.getMessage());
            }
        });
        await().atMost(3, TimeUnit.SECONDS).until(updateStoreResult.hasCompleted());
        assertTrue("updateStoreResult", updateStoreResult.success);
        assertEquals("updateStoreResultMsg", "update store successful",updateStoreResult.message);

        //check the store has been updated
        assertNull(testStore);
        storeService.getStoreByUID(storeUID,
                new OnSuccessCallBack<Store>() {
                    @Override
                    public void onSuccess(Store input) {
                        testStore = input;
                        Log.d(TAG, "fetching successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "fetching failed", input);
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(hasStore());
        assertEquals("compare s and store from the database", s, testStore);

        //Remove the store just added
        final Result removeStoreResult = new Result();
        storeService.deleteStoreByUID(storeUID,
                new OnSuccessCallBack<Void>() {
                    @Override
                    public void onSuccess(Void input) {
                        Log.d(TAG, "remove store successful");
                        removeStoreResult.complete(true, "remove store successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to remove store with ID:" + storeUID, input);
                        removeStoreResult.complete(false, input.getMessage());
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(removeStoreResult.hasCompleted());
        assertTrue("removeStoreResult", removeStoreResult.success);
        assertEquals("removeStoreResultMsg", "remove store successful", removeStoreResult.message);
    }

    @Test
    public void getNearbyStoreTest(){
        // get stores nearby, compare the list to the expected result
        List<Store> storeList = new ArrayList<>();
        storeService.getNearbyStore(new LatLng(34.0254, -118.2870),
                new OnSuccessCallBack<List<Store>>() {
                    @Override
                    public void onSuccess(List<Store> input) {
                        storeList.addAll(input);
                        Log.d(TAG, "successfully fetched store list");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to fetch store list", input);
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(() -> !storeList.isEmpty());
        assertEquals("size of nearby list", 5, storeList.size());
        List<String> UIDList = new ArrayList<>();
        UIDList.add("09a6rdzbl1SFxQOPXkuT");
        UIDList.add("7PqA0Yca8mKTntrvIHPT");
        UIDList.add("DGcKm39rOfkd2FJ5yfbh");
        UIDList.add("VNU8salaXDOGcyMIn6o7");
        UIDList.add("iCeVSwKCgwMVcDdQFJGv");
        List<String> actualList = new ArrayList<>();
        for (Store s : storeList){
            actualList.add(s.getStoreUID());
        }
        Collections.sort(UIDList);
        Collections.sort(actualList);
        assertEquals("actual list after sorting", UIDList, actualList);

        // add a Store to database that is not around the test position.
        Store s = new Store();
        s.setStoreName("Atelier Crenn");
        s.setStoreAddress(37.7983, -122.4360);
        s.setAddressString("3127 Fillmore St., San Francisco, 94123, United States");
        List<Drink> menu = new ArrayList<>();
        Drink drink1 = new Drink();
        drink1.setPrice(20);
        drink1.setDrinkName("Brandy");
        menu.add(drink1);
        s.setMenu(menu);
        final Result addStoreResult = new Result();
        String storeUID = storeService.addStore(s, new OnSuccessCallBack<Void>() {
            @Override
            public void onSuccess(Void input) {
                Log.d(TAG, "add store successful");
                addStoreResult.complete(true, "add store successful");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "failed to add store", input);
                addStoreResult.complete(false, input.getMessage());
            }
        });
        await().atMost(3, TimeUnit.SECONDS).until(addStoreResult.hasCompleted());
        assertNotNull("storeUID", storeUID);
        assertTrue("addStoreResult", addStoreResult.success);
        assertEquals("addStoreResult msg", "add store successful", addStoreResult.message);

        //examine that the nearby store still remains the same
        List<Store> storeList2 = new ArrayList<>();
        storeService.getNearbyStore(new LatLng(34.0254, -118.2870),
                new OnSuccessCallBack<List<Store>>() {
                    @Override
                    public void onSuccess(List<Store> input) {
                        storeList2.addAll(input);
                        Log.d(TAG, "successfully fetched store list");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to fetch store list", input);
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(() -> !storeList2.isEmpty());
        assertEquals("size of nearby list", 5, storeList2.size());
        List<String> actualList2 = new ArrayList<>();
        for (Store s2 : storeList2){
            actualList2.add(s2.getStoreUID());
        }
        Collections.sort(UIDList);
        Collections.sort(actualList2);
        assertEquals("actual list after sorting", UIDList, actualList2);

        //add another store in range
        Store inRange = new Store();
        inRange.setStoreName("USC Coffee");
        inRange.setStoreAddress(34.0224, -118.2851);
        inRange.setAddressString("Los Angeles, CA 90007");
        List<Drink> menuUSC = new ArrayList<>();
        Drink USCdrink = new Drink();
        USCdrink.setPrice(4);
        USCdrink.setDrinkName("USC matcha");
        menuUSC.add(USCdrink);
        inRange.setMenu(menuUSC);
        final Result addUSCResult = new Result();
        String uscUID = storeService.addStore(inRange, new OnSuccessCallBack<Void>() {
            @Override
            public void onSuccess(Void input) {
                Log.d(TAG, "add store successful");
                addUSCResult.complete(true, "add store successful");
            }
        }, new OnFailureCallBack<Exception>() {
            @Override
            public void onFailure(Exception input) {
                Log.w(TAG, "failed to add store", input);
                addUSCResult.complete(false, input.getMessage());
            }
        });
        await().atMost(3, TimeUnit.SECONDS).until(addUSCResult.hasCompleted());
        assertNotNull("storeUID", uscUID);
        assertTrue("addUSCResult", addUSCResult.success);
        assertEquals("addUSCResult msg", "add store successful", addUSCResult.message);

        //check that the nearby store list is updated
        List<Store> storeList3 = new ArrayList<>();
        storeService.getNearbyStore(new LatLng(34.0254, -118.2870),
                new OnSuccessCallBack<List<Store>>() {
                    @Override
                    public void onSuccess(List<Store> input) {
                        storeList3.addAll(input);
                        Log.d(TAG, "successfully fetched store list");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to fetch store list", input);
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(() -> !storeList3.isEmpty());
        assertEquals("size of nearby list", 6, storeList3.size());
        List<String> actualList3 = new ArrayList<>();
        for (Store s3 : storeList3){
            actualList3.add(s3.getStoreUID());
        }
        Collections.sort(UIDList);
        Collections.sort(actualList3);
        assertNotEquals("actual list after updating", UIDList, actualList3);
        UIDList.add(uscUID);
        Collections.sort(UIDList);
        assertEquals("actual list after sorting", UIDList, actualList3);

        //remove the store out of range
        final Result removeStoreResult = new Result();
        storeService.deleteStoreByUID(storeUID,
                new OnSuccessCallBack<Void>() {
                    @Override
                    public void onSuccess(Void input) {
                        Log.d(TAG, "remove store successful");
                        removeStoreResult.complete(true, "remove store successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to remove store with ID:" + storeUID, input);
                        removeStoreResult.complete(false, input.getMessage());
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(removeStoreResult.hasCompleted());
        assertTrue("removeStoreResult", removeStoreResult.success);
        assertEquals("removeStoreResultMsg", "remove store successful", removeStoreResult.message);

        //remove the store in range
        final Result removeUSCResult = new Result();
        storeService.deleteStoreByUID(uscUID,
                new OnSuccessCallBack<Void>() {
                    @Override
                    public void onSuccess(Void input) {
                        Log.d(TAG, "remove store successful");
                        removeUSCResult.complete(true, "remove store successful");
                    }
                }, new OnFailureCallBack<Exception>() {
                    @Override
                    public void onFailure(Exception input) {
                        Log.w(TAG, "failed to remove store with ID:" + storeUID, input);
                        removeUSCResult.complete(false, input.getMessage());
                    }
                });
        await().atMost(3, TimeUnit.SECONDS).until(removeUSCResult.hasCompleted());
        assertTrue("removeUSCResult", removeUSCResult.success);
        assertEquals("removeUSCResultMsg", "remove store successful", removeUSCResult.message);
    }
}
