package com.televantou.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.televantou.imagehorizontalseekbar.SeekbarLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekbarLayout.OnBottleLayoutValueChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekbarLayout seekbarLayout = findViewById(R.id.seekbar);
        ArrayList<SeekbarLayout.Pointer> pointers = new ArrayList<>();

        SeekbarLayout.Pointer pointer = new SeekbarLayout.Pointer(Gravity.LEFT, 20, "STEP 2" , false);
        pointers.add(pointer);

        pointer = new SeekbarLayout.Pointer(Gravity.LEFT, 40, "STEP 3", false);
        pointers.add(pointer);
        pointer = new SeekbarLayout.Pointer(Gravity.LEFT, 90, "STEP 5", false);
        pointers.add(pointer);

        pointer = new SeekbarLayout.Pointer(Gravity.RIGHT, 10, "STEP 1", false);
        pointers.add(pointer);
        pointer = new SeekbarLayout.Pointer(Gravity.RIGHT, 80, "STEP 5", false);
        pointers.add(pointer);

        seekbarLayout.setPointers(pointers);
        seekbarLayout.setOnSeekbarLayoutValueChangedListener(this);
        seekbarLayout.setProgress(40);

    }

    @Override
    public void onValueChanged(int value) {

    }
}
