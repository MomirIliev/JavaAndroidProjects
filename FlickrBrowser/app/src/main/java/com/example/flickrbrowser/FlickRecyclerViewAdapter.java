package com.example.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickRecyclerViewAdapter extends RecyclerView.Adapter<FlickRecyclerViewAdapter.FlickrImageViewHolder>  {
    private static final String TAG = "FlickRecyclerViewAdapte";
    private List<Photo> mPhotosList;
    private Context mContext;

    public FlickRecyclerViewAdapter(Context context, List<Photo> photosList) {
        mContext = context;
        mPhotosList = photosList;
    }


   // @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Called bu the layout manager when it needs a new view 
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

   // @NonNull
    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        // Called by the layout manager when it needs a new data in a existing row

        Photo photoItem = mPhotosList.get(position);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " ---> " + position);
        Picasso.get().load(photoItem.getImage()).
                error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
      /*  Picasso.get().load(photoItem.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);

       */

        holder.title.setText(photoItem.getTitle());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.size() : 0);
    }

    void loadNewData (List<Photo> newPhotos){
        mPhotosList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title_image);

        }
    }
}
