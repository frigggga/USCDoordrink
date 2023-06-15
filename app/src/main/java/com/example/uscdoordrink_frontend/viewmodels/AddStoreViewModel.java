package com.example.uscdoordrink_frontend.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uscdoordrink_frontend.entity.Store;

/**
 * @Author: Yuxuan Liao
 * @Date: 2022/3/26 15:40
 */
public class AddStoreViewModel extends ViewModel {
    public MutableLiveData<Store> mStoreModel = new MutableLiveData<>();
}
