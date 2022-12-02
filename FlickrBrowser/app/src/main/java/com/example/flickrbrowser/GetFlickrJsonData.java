package com.example.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask <String, Void, List<Photo>> implements GetRowData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable{                                                                      // Interface is a bonding contract that guarantees that the callback method will ve implemented.
        void onDataAvailable(List <Photo> data, DownloadStatus status);                              // Declaring an interface is like declaring a class, inside we specify the methods that must be implemented by anything that implements the inteface.
    }

    public GetFlickrJsonData( OnDataAvailable callBack, String baseUrl, String language, boolean matchAll ) {
        Log.d(TAG, "GetFlickrJsonData: called");
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallBack = callBack;
    }

    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;                                                                 // The variable runningOnSameThread we use to control where will be called the command "mcallback.OnDataAvailable".
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);                    // If the method executeOnSameThread is called, the variable runningOnSameThread will be set to true and mcallback will be called in method onDownloadXOmplete
        GetRowData getRowData = new GetRowData(this);                                      // in the other case the class GetFlickrJsonData will be called as asynchronous and in that case will be called the methods doInBackground and onPostExecute
        getRowData.execute(destinationUri);                                                         // and the variable mcallback.onDataAvailable will be called from onPostExecute method.
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");

        if(mCallBack != null){
            mCallBack.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);

        GetRowData getRowData = new GetRowData(this);
        getRowData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll){
        Log.d(TAG, "createUri: starts");

        return  Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts" + status);

        if(status == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();


            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i<itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String author_id = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, author_id, link , tags, photoUrl);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());

                }
            }
            catch (JSONException jsonException) {
                jsonException.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing json data" + jsonException.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if(runningOnSameThread && mCallBack != null){
            // Inform the caller that the processing is done and it may return null value if there was an error.
            mCallBack.onDataAvailable(mPhotoList, status);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
