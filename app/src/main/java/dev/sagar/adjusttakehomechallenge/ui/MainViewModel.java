package dev.sagar.adjusttakehomechallenge.ui;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import dev.sagar.adjusttakehomechallenge.data.local.TimeEntity;
import dev.sagar.adjusttakehomechallenge.data.local.dao.DatabaseHelper;
import dev.sagar.adjusttakehomechallenge.data.remote.AsyncResponse;
import dev.sagar.adjusttakehomechallenge.data.remote.PostSecond;
import dev.sagar.adjusttakehomechallenge.util.InternetChecker;
import dev.sagar.adjusttakehomechallenge.util.TimeUtil;

/**
 * This class contains the business logic of operations in local DB and posting data to server
 * Here its not extended to [{@link androidx.lifecycle.ViewModel}] because we don't have a usecase of it according to our task right now.
 */
public class MainViewModel implements AsyncResponse {

    private DatabaseHelper databaseHelper;
    private InternetChecker internetChecker;

    public MainViewModel(Context context) {
        databaseHelper = new DatabaseHelper(context);
        internetChecker = new InternetChecker(context);

        syncTime();
    }

    void insertSecond() {
        new Thread(() -> {
            int second = Integer.parseInt(TimeUtil.getCurrentSecond());
            boolean insertStatus = databaseHelper.insertTime(second);

            if (insertStatus) {
                if (internetChecker.hasInternetConnection()) {
                    databaseHelper.markInProgress(String.valueOf(second));
                    PostSecond req = new PostSecond(this, String.valueOf(second));
                    req.execute();
                }
            } else {
                if (internetChecker.hasInternetConnection())
                    if (databaseHelper.isTimeUnSynced(String.valueOf(second))) {
                        databaseHelper.markInProgress(String.valueOf(second));
                        PostSecond req = new PostSecond(this, String.valueOf(second));
                        req.execute();
                    }
                Log.w("Second exist already: ", String.valueOf(second));
            }

        }).start();
    }

    private void syncTime() {
        new Thread(() -> {
            if (internetChecker.hasInternetConnection()) {
                List<TimeEntity> seconds = databaseHelper.getAllUnSyncedTime();
                for (int i = 0; i < seconds.size(); i++) {
                    PostSecond postSecond = new PostSecond(this, String.valueOf(seconds.get(i).getSecond()));
                    postSecond.execute();
                }
            }
        }).start();
    }

    @Override
    public void onPostResponse(String output) {
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
                Log.d("MainActivity", "Seconds: " + second + " and id: " + id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    void clear() {
        databaseHelper = null;
        internetChecker = null;
    }

}
