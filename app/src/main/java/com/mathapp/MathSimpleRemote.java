package com.mathapp;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ahmha02 on 15-11-15.
 */
public class MathSimpleRemote extends AsyncTask<Object, Void, JSONObject> {
    TextView resultTextView = null;
    Button button = null;

    protected JSONObject doInBackground (Object... operationRest){

        JSONObject resultJSON = null;
        //returnJSON = operationRest[1].toString();

        // get result TextView pointer
        resultTextView = (TextView) operationRest[1];
        button = (Button) operationRest[2];


        // Remote URL call
        HttpURLConnection httpConnection = null;

        try {
            // Get the url into a URL object
            URL url = new URL((String)operationRest[0]);

            // Open the URL connection
            httpConnection = (HttpURLConnection) url.openConnection();

            // Test the HTTP response code
            int rc = httpConnection.getResponseCode();
            if (rc == HttpURLConnection.HTTP_OK) {
                // If we actually got something...
                InputStream responseData = new BufferedInputStream(httpConnection.getInputStream());
                resultJSON = new JSONObject(new Scanner(responseData).useDelimiter("\\A").next());
            } else {
                // Return the HTTP error details
                try {
                    resultJSON = new JSONObject("{\"result\":0,\"content\":\"" + "HTTP Error: " + String.format("%d", rc) + "\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (MalformedURLException ex) {
            // Malformed URL
            try {
                resultJSON = new JSONObject("{\"result\":0,\"content\":\"" + ex.getLocalizedMessage() + "\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (java.io.IOException ex) {
            // TODO: 16-07-18 getting this exception to investigate: java.net.ConnectException: failed to connect to localhost/127.0.0.1 (port 8080): connect failed: ECONNREFUSED (Connection refused)
            // IO Exception on the HTTP request
            try {
                resultJSON = new JSONObject("{\"result\":0,\"content\":\"" + ex.getLocalizedMessage() + "\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (JSONException ex) {
            // JSON parsing error
            try {
                resultJSON = new JSONObject("{\"result\":0,\"content\":\"" + ex.getLocalizedMessage() + "\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                httpConnection.disconnect();
            }
        }


        return resultJSON;
    }

    protected void onPostExecute(JSONObject jsonResult) {

        // Process JSON result
        try {
            String result = jsonResult.getString("result");
            resultTextView.setText(result);
        } catch (JSONException ex) {
            resultTextView.setText("ERROR: " + ex.getLocalizedMessage());
        }

        //enable button on response
        button.setEnabled(true);

    }
}
