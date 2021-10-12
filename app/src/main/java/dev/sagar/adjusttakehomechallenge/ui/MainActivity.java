package dev.sagar.adjusttakehomechallenge.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dev.sagar.adjusttakehomechallenge.R;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private MainViewModel viewModel;
    private Handler handler;
    private Runnable insertTimeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new MainViewModel(this);
        handler = new Handler(Looper.getMainLooper());
        insertTimeRunnable = () -> viewModel.insertSecond();

        initViews();
        initClickListeners();
    }

    private void initViews() {
        button = findViewById(R.id.button);
    }

    private void initClickListeners() {
        // Debouncing clicks events which is less than 100 ms
        button.setOnClickListener(view -> {
            for (int i = 0; i < 60; i++) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(insertTimeRunnable, 100);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}