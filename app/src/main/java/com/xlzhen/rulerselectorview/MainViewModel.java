package com.xlzhen.rulerselectorview;

import androidx.lifecycle.MutableLiveData;

public class MainViewModel {
    public MutableLiveData<Integer> weight=new MutableLiveData<>(50);
    public MainActivity activity;
    public MainViewModel(MainActivity activity) {
        this.activity =activity;
        activity.binding.weightRulerSelectorView.setListener(number -> {
            weight.setValue(number);
        });
    }
}
