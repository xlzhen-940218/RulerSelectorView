package com.xlzhen.rulerselectorview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xlzhen.rulerselectorview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setMain(new MainViewModel(this));
    }
}