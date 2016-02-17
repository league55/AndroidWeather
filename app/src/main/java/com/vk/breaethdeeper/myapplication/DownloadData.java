package com.vk.breaethdeeper.myapplication;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mixmax on 17.02.16.
 */
public class DownloadData extends IntentService {
    private static final String TAG = DownloadData.class.getSimpleName();

    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String URL_EXTRA = "url";
    public static final String JSON_RESULT = "url";
    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    final String urlString = "http://jsonplaceholder.typicode.com/posts";

    private String data = "";

    public DownloadData() {
        super(TAG);
   }

    @Override
    protected void onHandleIntent(Intent intent) {
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);

        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        String string="";
        try{
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            urlConnection.setRequestProperty("Accept", "*/*");
            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStream in = urlConnection.getInputStream();

            String line;
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                System.out.println("--------"+line);
            }

            this.data = sb.toString();

            Intent result = new Intent();
            result.putExtra(JSON_RESULT, sb.toString());

            reply.send(this, RESULT_CODE, result);

        }catch (IOException e){
            Log.e(this.getClass().getName(), e.toString());

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        } finally {

            try {
                if(br!=null) br.close();
                if(urlConnection!=null) urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
