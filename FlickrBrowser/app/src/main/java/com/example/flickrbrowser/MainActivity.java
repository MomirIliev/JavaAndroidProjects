package com.example.flickrbrowser;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable {
    private static final String TAG = "MainActivity";
    private FlickRecyclerViewAdapter mFlickRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
      //  try{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
     //   }
     //   catch(NullPointerException e){
      //      System.out.println("Null Pointer Exception caught Recycler View");
     //   }

        mFlickRecyclerViewAdapter = new FlickRecyclerViewAdapter(this, new ArrayList<Photo>());
    //    try{
            recyclerView.setAdapter(mFlickRecyclerViewAdapter);
//       }
//        catch (NullPointerException e){
//            System.out.println("Null Pointer Exception caught Setting Adapter");
//        }

//        GetRowData getRowData = new GetRowData(this);
//        getRowData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android,nougat,sdk&tagmode=any&format=json&nojsoncallback=1");
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(this, "https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true);
      //  getFlickrJsonData.executeOnSameThread("android, nougat");
        getFlickrJsonData.execute("android, nougat");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable (List<Photo> s, DownloadStatus status){
        Log.d(TAG, "onDataAvailable: starts");

        if(status == DownloadStatus.OK){
        mFlickRecyclerViewAdapter.loadNewData(s);
        } else {
            Log.e(TAG, "onDataAvailable: status is" + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

}