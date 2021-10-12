package dev.sagar.adjusttakehomechallenge.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dev.sagar.adjusttakehomechallenge.R;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new MainViewModel(this);

        initViews();
        initClickListeners();
    }

    private void initViews() {
        button = findViewById(R.id.button);
    }

    private void initClickListeners() {
        button.setOnClickListener(view -> {
            viewModel.insertSecond();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}