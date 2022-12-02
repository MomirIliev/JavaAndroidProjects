package com.example.top10downloadedapps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private String lastUrl;
    /*
             lastUrl ke bide iskoristena za zacuvuvanje na poslednata URL adresa. Istata ke bide iskoristena za sprecuvanje na povtorno downloadiranje vo slucaj na izbiranje na ista kategorina i broj na prikazani elementi.
     */
    private ListView listView;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    // Izrazot %d posle limit vo linkot, go zamenuva brojot koj oznacuva kolku elementi ke bidat procitani od proprateniot link. %d ke bide zamenet so realen broj preku metodot String.format, vmetnat vo metodot downloadUrl vo onCreate metodoot.

    private int feedLimit = 10;
    private static final String URL_SAVED = "UrlSaving";
    private static final String LIMIT_SAVED = "LimitSaving";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.xmlListView);                                       //Povrzuvanje na promenlivata listview so kreiraniot element vo dizajnot xmlListView. Isto kako sto go pravevme toa so povrzuvanjeto na kopcinjata button.

        if (savedInstanceState != null) {                                                           // Zacuvanite vrednosti vo onSaveInstanceState ke bidat prezemeni vo onCreate metodot bidejki prvoto inicijaliziranje na listView e vo ovoj metod.
            feedLimit = savedInstanceState.getInt(LIMIT_SAVED);
            feedUrl = savedInstanceState.getString(URL_SAVED);
        }
        downloadUrl(String.format(feedUrl, feedLimit));
//        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=25/xml");    // Pristapuvanje koj linkot od koj ke se prezemaat podatocite.

    }
/*
    When we try to inflate a view in adapter we need to get an inflator from a context, but Activity and AppCompatActivity are context so we can call the getMenuInflater() method directly like in onCreateOptionsMenu method.
 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);                                         // inflate metodot koristi vo slucajot dva parametri. Prviot e xml datotekata koja ke se inflate - ra, dodeka vtoriot e parent odnosno vo ovoj slucaj
        if (feedLimit == 10) {                                                                      // parent datoteka na xml datotekata feeds_menu e datotekata menu.
            menu.findItem(R.id.mnu10).setChecked(true);
        } else {
            menu.findItem(R.id.mnu25).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnuFree:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;

            case R.id.mnuPaid:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;

            case R.id.mnuSongs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.topAlbums:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=%d/xml";
                break;
            case R.id.mnu10:
            case R.id.mnu25:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    feedLimit = 35 - feedLimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + "setting feedLimit to: " + feedLimit);
                } else {
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + "feedLimit unchanged. ");
                }
                break;
            case R.id.refreshButton:
                downloadUrl(String.format(feedUrl, feedLimit));                                     // Dokolku korisnikot klikne na kopceto Refresh podatocite ke bidat uste ednas simnati (downloaded)
                Log.d(TAG, "onOptionsItemSelected: You have refreshed the list. ");
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        if (!lastUrl.equals(String.format(feedUrl, feedLimit))) {
            downloadUrl(String.format(feedUrl, feedLimit));
        } else
            Log.d(TAG, "onOptionsItemSelected: You have selected the same menu Item and feedLimit ");
        return true;
/*
        Vo if izrazot se sporeduvaat poslednata URL adresa zacuvana vo lastUrl i URL adresata posle izbiranjeto na element od listata (menu item) i broj na prikazani elementi (feedLimit).
        Dokolku pri sporeduvanjeto dvata URL se identicni nema da bide izvrsena nikakva operacija. Vo sprotivno ke bide izvrseno simnuvanje na novata izberena lista i/ili broj na podatoci
 */
    }

    private void downloadUrl(String feedUrlParam) {
        lastUrl = feedUrlParam;
        String urlChecked = String.format(feedUrl, feedLimit);
        if (urlChecked != feedUrlParam) {
            Log.d(TAG, "downloadUrl: Starting AsyncTask");
            DownloadData downloadData = new DownloadData();                                             // Kreiranje na instanca od klasata DownloadData
            downloadData.execute(feedUrlParam);                                                                 //pristapuvanje koj linkot od koj ke se prezemaat podatocite.
            Log.d(TAG, "downloadUrl: done.");
        } else Log.d(TAG, "downloadUrl: You have selected the same menu item.");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {                             // Klasata DownloadData ja nasleduva klasata AsyncTask koja ima tri argumenti koi mozat da bidat od razlicen tip vo zavisnot kakvi podatoci sakame da obrabotuvame
        private static final String TAG = "DownloadData";                                           // Se koristi za proveruvanje na vleguvanjeto vo ovoj metod preku Logcat


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parametar is: " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);                                                             // Se povikuva metodot parse (Vo koj se ispraka argumentot s, koj e vsusnost string koj sodrzi podatoci od xml simnatata sodrzina) od klasata ParseApplications
//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(                     // Kreiranje na adapter koj zema tri parametri i ke izvrsuva prikazuvanje na vratenite podatoci po izvrsuvanjeto na parse metodot od klasata ParseApplications.
//                    MainActivity.this, R.layout.list_item, parseApplications.getApplications());
//            listView.setAdapter(arrayAdapter);                                                      // Setiranje na adapterot.
            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplications.getApplications());
            listView.setAdapter(feedAdapter);
        }


        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with" + strings[0]);
            String rssFeed = DownloadXml(strings[0]);                                              // Preku optovaruvanje na metodot doInBackgorund se povikuva metodot DownloadXml vo koj se izvrsuva vcituvanjeto na podatocite od vnesenit URL
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error Downloading");                              // Dokolku vcituvanjeto e neuspesno se ispecatuva greska.
            }
            return rssFeed;
        }
    }


    private String DownloadXml(String urlPath) {
        StringBuilder xmlResult = new StringBuilder();                                              // Kreiranje na promenliva vo koja ke se smestuvaat vcitanite podatoci

        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();                // Kreiranje na URL konekcija
            int response = connection.getResponseCode();                                            // Vraka odgovor od linkot kon koj probuvame da pristapime. Koga se pojavuva greska 404 pri neuspesno pristapuvanje do URL doaga od ovaa naredba.
            Log.d(TAG, "DownloadXml: The response code was : " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));         // Kreiranje na promenliva reader preku koja ke se citaat podatoci.


            int charsRead;
            char[] inputBuffer = new char[500];
            while (true) {
                charsRead = reader.read(inputBuffer);                                               // Inicijaliziranje na promenliva charsRead so brojot na vcitani znaci od vneseniot link.
                if (charsRead < 0) {
                    break;
                }
                if (charsRead > 0) {                                                                 // Dokolku brojot na vcitanite znaci e pogolem od 0, vo promenlivata xmlResult se dodavaat vcitanite podatoci.
                    xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                }
            }
            reader.close();

            return xmlResult.toString();                                                            // Ovaa funkcija kako rezultat gi vraka podatocite koi iscitani od vneseniot link, t.e vrednosta na promenlivata xmlResult.


        } catch (MalformedURLException e) {
            Log.e(TAG, "DownloadXml: Invalid URL " + e.getMessage());                        // Go faka isklicokot dokolku URL e nevaliden.
        } catch (IOException e) {
            Log.e(TAG, "DownloadXml: IO exception reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "DownloadXml: Security Exception, Permission needed? " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(LIMIT_SAVED, feedLimit);
        outState.putString(URL_SAVED, feedUrl);
        super.onSaveInstanceState(outState);
    }
}
