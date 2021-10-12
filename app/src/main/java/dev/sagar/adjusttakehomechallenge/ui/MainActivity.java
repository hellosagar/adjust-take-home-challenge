package dev.sagar.adjusttakehomechallenge.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.List;

import dev.sagar.adjusttakehomechallenge.R;
import dev.sagar.adjusttakehomechallenge.data.local.TimeEntity;
import dev.sagar.adjusttakehomechallenge.data.local.dao.DatabaseHelper;
import dev.sagar.adjusttakehomechallenge.data.remote.AsyncResponse;
import dev.sagar.adjusttakehomechallenge.data.remote.PostSecond;
import dev.sagar.adjusttakehomechallenge.util.InternetChecker;
import dev.sagar.adjusttakehomechallenge.util.TimeUtil;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private Button button;
    private DatabaseHelper databaseHelper;
    private InternetChecker internetChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        internetChecker = new InternetChecker(MainActivity.this);

        initViews();
        initClickListeners();
        syncTime();
    }

    private void initViews() {
        button = findViewById(R.id.button);
    }

    private void initClickListeners() {
        button.setOnClickListener(view -> {
            new Thread(() -> {
                int second = Integer.parseInt(TimeUtil.getCurrentSecond());
                boolean insertStatus = databaseHelper.insertTime(second);
                if (insertStatus) {
                    Log.d("Second inserted: ", String.valueOf(second));
                    if (internetChecker.hasInternetConnection()) {
                        databaseHelper.markInProgress(String.valueOf(second));
                        PostSecond req = new PostSecond(MainActivity.this, String.valueOf(second));
                        req.execute();
                    }
                } else {
                    if (internetChecker.hasInternetConnection())
                        if (databaseHelper.isTimeUnSynced(String.valueOf(second))) {
                            databaseHelper.markInProgress(String.valueOf(second));
                            PostSecond req = new PostSecond(MainActivity.this, String.valueOf(second));
                            req.execute();
                        }
                    Log.w("Second exist already: ", String.valueOf(second));
                }

            }).start();
        });
    }

    private void syncTime() {
        new Thread(() -> {
            List<TimeEntity> seconds = databaseHelper.getAllUnSyncedTime();
            for (int i = 0; i < seconds.size(); i++) {
                if (internetChecker.hasInternetConnection()) {
                    PostSecond postSecond = new PostSecond(MainActivity.this, String.valueOf(seconds.get(i).getSecond()));
                    postSecond.execute();
                }
            }
        }).start();
    }


    @Override
    public void processFinish(String output) {
        new Thread(() -> {
            try {
                JSONObject object = new JSONObject(output);
                String second = (String) object.get("seconds");
                int id = (int) object.get("id");
                if (id == -1) {
                    databaseHelper.markUnSync(second);
                } else {
                    databaseHelper.markSync(second);
                }
                Log.d("Mark Synced", "Seconds: " + second + " and id: " + id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}