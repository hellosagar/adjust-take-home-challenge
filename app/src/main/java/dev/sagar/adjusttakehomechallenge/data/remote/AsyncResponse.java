package dev.sagar.adjusttakehomechallenge.data.remote;

/**
 * Interface used to pass data from [{@link PostSecond}] to [{@link dev.sagar.adjusttakehomechallenge.ui.MainActivity}]
 */
public interface AsyncResponse {
    void processFinish(String output);
}