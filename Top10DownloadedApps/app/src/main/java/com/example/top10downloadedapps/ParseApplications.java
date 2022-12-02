package com.example.top10downloadedapps;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();                                                      // Inicijaliziranje na promenlivata applications, t.e inicijaliziranje na ArrayList - ta, kako bi bila spremna za vnesuvanje na elementi (entries)
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {                                                          // xmlData e argument vo koj se sodrzani podatocite procitani od URL.
        boolean status = true;                                                                      // Prom. status ke ja koristime za proveruvanje dali parsiranjeto uspesno se izvrsuva. Vo slucaj na neuspesno parsiranje status ke bide setirana da vrati false.
        FeedEntry currentRecord = null;                                                                    // Vo sekoj ciklus na parsiranje na podatocite treba da bidat zacuvuvani elementite vo FeedEntry lista, sto ke bide sprovedeno vo promenlivata currentRecord
        boolean inEntry = false;                                                                    // inEntry ke ja koristime za proveruvanje na pozicijata na podatocite vo XML dokumentot od vneseniot URL, vo slucajot sakame da citame podatoci od Entry Tag vo XML.
        String textValue = "";                                                                      // Vo textValue ke bide zacuvana vrednosta na Tag - ot vo koj se naoga citacot vo momentot = (current Tag).

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();                      // First 3 lines of code are setting the Java Xml Parser that will do all the hard work of making sense of the xml that was sent in the argument xmlData
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));                                                // StringReader e klasa koja gi tretira String podatocite kako Stream.
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {                                        // Dodeka ne dojde do krajot na dokumentot ke se izvrsuvat operaciite vo while ciklusot
                String tagName = xpp.getName();                                                     // vo tagName ke go smestime imeto na tagot od XML dokumentot do koj ke pristapime preku URL
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        //           Log.d(TAG, "parse: Starting tag for: " + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //           Log.d(TAG, "parse: Ending tag for: " + tagName);
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {                                  // Vo if statement se koristat "entry", "name", "artist" ... poradi toa sto istite po default ne mozat da vratat vrednost null bidejki samite se vrednost.
                                applications.add(currentRecord);                                    // Dokolku napravevme sporedba vo obraten redosled, metodot getName koj ke ja vraka vrednosta na imeto na tagot od XML datotekata moze da vrati vrednost null
                                inEntry = false;                                                    // i vo toj slucaj za sekoja od if, if else izrazite ke morase da kodirame i exceptions (isklucoci) za fakanje na null vrednostite.
                            }                                                                       // Vo ovoj slucaj dokolku equalsIgnoreCase(tagName) vrati null vrednost, istata ke bide fatena vo definiraniot Exception na krajot na while ciklusot.
                            else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("relseaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURL(textValue);
                            } else if ("title".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(tagName);
                            }
                            break;
                        }
                            default:
                                //nothing else to do
                        }
                        eventType = xpp.next();

                }

//            for (FeedEntry app: applications){
//                Log.d(TAG, "*****************************");
//                Log.d(TAG, app.toString());
//            }

            } catch(Exception e){
                status = false;
                e.printStackTrace();

            }
            return status;

        }


    }
