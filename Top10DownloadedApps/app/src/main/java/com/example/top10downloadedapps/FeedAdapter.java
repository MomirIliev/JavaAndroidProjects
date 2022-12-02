package com.example.top10downloadedapps;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

//import org.w3c.dom.Text;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;                                                               // Will save the resource that will be inflated by the layoutInflator. Resource is the xml document that will be displaying the view on the screen.
    private final LayoutInflater layoutInflater;                                                    //We will get layoutInflator from a context (which will be given as a parameter in the constructor)
    private List<FeedEntry> applications;

    public FeedAdapter(@NonNull Context context, int resource, List<FeedEntry> applications) {      // Konstruktor za klasata FeedAdapter. resource is the xml file that will be inflated.
        super(context, resource);
        this.applications = applications;                                                           // this vo java se koristi za pokazuvanje kon segasnata instanca od klasata, this.applications pokazuva na promenlivata deklarirana vo red 15.
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    /*
    getView metodot se povikuva od ListView sekogas koga treba da se prikaze nov element. ( Every time it wants another item to display).
    Setiranjeto na argumentot parent vo layoutInflater so parametarot parent od getView dava pristap do trite TextView elementi.
    Parametarot position pretstavuva pozicija vo koja treba da se prikaze elementot.
    Android Framework avtomatski ke go isprati vo parametarot parent dokumentot list_record xml.
    Creating a view and inflating the layoutResource with the layoutInflator (created in the constructor). layoutResource is going to be set by list_record xml file, by the ConstrainedLayout in which are created the three TextViews.
    Nevoobicaeno ovde se koristi view, pred findViewById, bidejki na linijata se kreira view za pristapuvanje kon ovie elementi.
    Problemot so ovoj metod e toa sto za sekoj nov element se kreira nov view, so ova aplikacijata ima se pogolema pobaruvacka za memorija sto vo nikoj slucaj ne e prifatlivo.

    If the ListView has a view that can be reused, it passes a reference to it to a convertView. If not then convertView is null. So a better option than to create a new view for every item is to check if convertView is null ant if so then reuse
    the same and create a new view.
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            Log.d(TAG, "getView: Called with null convertView. ");

/* Dokolku ova e nov view koj seuste ne e kreiran togas se kreira viewHolder vo koj ke se zacuva noviot view. Kako parametar za noviot view se ispraka convertView koj go sodrzi momentalniot view.
    Vsusnost convertView koga e iskoristen kako argument vo izrazot "viewHolder = new ViewHolder(convertView)" gi sodrzi vrednostite od site elementi od momentalniot view. Toa znaci deka ako momentalniot view ima teoretski i 100 elementi,
    convertView ke gi isprati site kako refenca na kontruktorot od klasata ViewHolder.
    Isto taka se setira objektot setTag (koj moze da prima sekakov vid na parametar) deka vo istiot ke bide zacuvan objekt od klasata viewHolder.

*/

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: Called with existing convertView. ");
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        View view = layoutInflater.inflate(layoutResource, parent, false);                        Kreira nov view za sekoj element so aplikacijat ke alocira se poveke memorija, what is definately not good. ;)
//        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
//        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvArtist);
//        TextView tvSummarty = (TextView) convertView.findViewById(R.id.tvSummary);


        FeedEntry currentApp = applications.get(position);

        viewHolder.tvName.setText(currentApp.getName());
        viewHolder.tvArtist.setText(currentApp.getArtist());
        viewHolder.tvSummary.setText(currentApp.getSummary());
        viewHolder.tvImage.setText(currentApp.getImageURL());
        return convertView;

    }

    private class ViewHolder {
        final TextView tvName;
        final TextView tvArtist;
        final TextView tvSummary;
        final TextView tvImage;


        ViewHolder(View v) {
            this.tvName = v.findViewById(R.id.tvName);
            this.tvArtist = v.findViewById(R.id.tvArtist);
            this.tvSummary = v.findViewById(R.id.tvSummary);
            this.tvImage = v.findViewById(R.id.urlImage);
        }
    }
}
