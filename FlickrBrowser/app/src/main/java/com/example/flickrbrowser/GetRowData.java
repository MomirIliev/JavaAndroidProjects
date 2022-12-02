package com.example.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK};
/* Enum parametrite ke se koristat za kontroliranje na procesot na simnuvanje na podatocite od ispratenoto URL vo klasata GetRowData
IDLE oznacuva deka ne se slucuva nisto, nema simnuvanje na podatoci, PROCESSING se procesiraat podatocite, NOT_INITIALIZED oznacuva deka ne sme ispratile validen link za simnuvanje,
FALIED_OR_EMPTY oznacuva deka se pojavila greska pri simnuvanjeto ili se vratil prazen podatok, OK oznacuva deka podatocite se uspesno simnati i zacuvani.
*/
class GetRowData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRowData";

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mcallBack;
    /* using small letter m as the firts letter from variable is common naming oonvention for class fields, m stands for member variable */

    interface OnDownloadComplete {                                                                  // Interface is a binding contract that guarantees that anything that will implement the interface will implement it's methods that we specify //
        void onDownloadComplete(String data, DownloadStatus status);                                // In the interface we don't write the code for the methods, that is up to the implementer.
    }                                                                                               // In the interface we only declare the name of the methods, the parameters and the return type if there is any.
                                                                                                    // When declaring an inteface we start the name with a big letter, same like in classes
    public GetRowData(OnDownloadComplete callBack) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mcallBack = callBack;
    }

    void runInSameThread (String s) {
        Log.d(TAG, "runInSameThread: starts");

//        onPostExecute(doInBackground(s));
        if(mcallBack != null){
//            String result = doInBackground(s);
//            mcallBack.onDownloadComplete(result, mDownloadStatus);
            mcallBack.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: parameter = " + s);
        if(mcallBack != null){
            mcallBack.onDownloadComplete( s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
        //       super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);                                                          // Dokolku ne uspee citanjeto na linkot, greskata ke bide prepoznaena i fatena od catch (MalformedURLException)

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was: " + response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));


//            String line;
//            while (null != (line = reader.readLine())) {
//                result.append(line).append("\n");
//            }
            for (String line = reader.readLine(); line!=null; line = reader.readLine())           // Moze da go koristime i ovoj kod za citanje na podatocite od linkot. Prednosta na ovoj kod e sto promenlivata line e deklarirana lokalno vnatre
                result.append(line).append("\n");                                                          // vo ciklusot, sto go pravi kodot porobusten.


            mDownloadStatus = DownloadStatus.OK;
            return result.toString();



        }   catch (MalformedURLException e ){
            Log.e(TAG, "doInBackground: Invalid URL" + e.getMessage() );
        }   catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception Reading Data" + e.getMessage());
        }   catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception. Needs Permission?" + e.getMessage());
        }   finally {
            if (connection !=null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage() );
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
