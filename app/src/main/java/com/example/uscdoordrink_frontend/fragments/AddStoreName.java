package com.example.uscdoordrink_frontend.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uscdoordrink_frontend.AddStoreActivity;
import com.example.uscdoordrink_frontend.R;
import com.example.uscdoordrink_frontend.entity.Store;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.nio.channels.Pipe;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStoreName#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStoreName extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String stringAddress;
    private LatLng myAddress;

    public AddStoreName() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddStoreName.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStoreName newInstance(String param1, String param2) {
        AddStoreName fragment = new AddStoreName();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mainView = inflater.inflate(R.layout.fragment_add_store_name, container, false);

        EditText textName = (EditText) mainView.findViewById(R.id.editTextStoreName);
//        EditText textLat = (EditText) mainView.findViewById(R.id.editTextStoreLat);
//        EditText textLng = (EditText) mainView.findViewById(R.id.editTextStoreLng);
        String defaultName = textName.getHint().toString();

        @NonNull Store store = Objects.requireNonNull(((AddStoreActivity) requireActivity()).theStore.mStoreModel.getValue());

        if (store.getStoreName() != null){
            textName.setText(store.getStoreName());
        }
        if (store.getStoreAddress() != null){
            myAddress = new LatLng(store.getStoreAddress().latitude, store.getStoreAddress().longitude);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        if (store.getAddressString() != null){
//            textLat.setText(store.getStoreAddress().first.toString());
//            textLng.setText(store.getStoreAddress().second.toString());
            autocompleteFragment.setText(store.getAddressString());
            stringAddress = store.getAddressString();
        }else{
            autocompleteFragment.setHint("Store Address");
        }

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                myAddress = place.getLatLng();
                stringAddress = place.getAddress();
                Log.d("OnPlaceSelected", "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                Log.d("", "An error occurred: " + status);
            }
        });

        Button confirmButton = (Button)mainView.findViewById(R.id.button_confirm_name_and_address);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(textName.getText().toString())){
                    Toast.makeText(getContext(), "Store Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
//                else if("".equals(textLat.getText().toString()) || "".equals(textLng.getText().toString())){
//                    Toast.makeText(getContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
//                }
                else if (defaultName.equals(textName.getText().toString())){
                    Toast.makeText(getContext(), "Please customize your store name", Toast.LENGTH_SHORT).show();
                }
                else if (myAddress == null || stringAddress == null){
                    Toast.makeText(getContext(), "Please specify an address", Toast.LENGTH_SHORT).show();
                }
                else{
//                    try{
//                        double lat = Double.parseDouble(textLat.getText().toString());
//                        double lng = Double.parseDouble(textLng.getText().toString());
//                        if (lat > 90 || lat < -90 || lng > 180 || lng < -180){
//                            throw new NumberFormatException();
//                        }
//                        store.setStoreAddress(new Pair<Double, Double>(lat, lng));
//                        store.setStoreName(textName.getText().toString());
//                        Navigation.findNavController(view).navigate(R.id.action_name_to_menu);
//                    }catch (NumberFormatException e){
//                        Toast.makeText(getContext(), "Please enter a valid address", Toast.LENGTH_SHORT).show();
//                    }
                    store.setStoreName(textName.getText().toString());
                    store.setStoreAddress(myAddress.latitude, myAddress.longitude);
                    store.setAddressString(stringAddress);
                    Navigation.findNavController(view).navigate(R.id.action_name_to_menu);
                }
            }
        });
        return mainView;
    }
}