package com.example.myapplication.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.MoviesModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MoviesInfoViewHolder>  {
    private Context mContext;
    private MoviesModel.ResultsBean selectedMovie;
    public DetailAdapter(Context context)
    {
        mContext=context;
    }
    public void setOnlineMovie(MoviesModel.ResultsBean resultsBean){
        selectedMovie=resultsBean;
    }
    @Override
    public MoviesInfoViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from( mContext ).inflate( R.layout.detail_items,parent,false);
        return new MoviesInfoViewHolder( viewHolder );
    }
    @Override
    public void onBindViewHolder( MoviesInfoViewHolder holder, int position) {
        String[] releaseYear = selectedMovie.getRelease_date().split("-", 3);
        Picasso.with( mContext ).load( selectedMovie.getPoster_path() ).into( holder.moviePosterIv );
        holder.movieTitleTv.setText( selectedMovie.getTitle() );
        holder.movieReleaseDateTv.setText( releaseYear[0] );
        holder.movieRateTv.setText( String.valueOf( selectedMovie.getVote_average() )+"/10" );
        holder.movieOverviewTv.setText( selectedMovie.getOverview() );
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class MoviesInfoViewHolder extends RecyclerView.ViewHolder  {
        private LinearLayout backgroundLayout;
        private ImageView moviePosterIv;
        private TextView movieTitleTv;
        private TextView movieReleaseDateTv;
        private TextView movieRateTv;
        private TextView movieOverviewTv;
        public MoviesInfoViewHolder(final View itemView) {
            super( itemView );
            backgroundLayout=itemView.findViewById( R.id.background_layout );
            moviePosterIv=(ImageView)itemView.findViewById( R.id.selected_movie_image );
            movieTitleTv=(TextView)itemView.findViewById( R.id.movie_title );
            movieReleaseDateTv=(TextView)itemView.findViewById( R.id.movie_release_date );
            movieRateTv=(TextView)itemView.findViewById( R.id.movie_rate );
            movieOverviewTv=(TextView)itemView.findViewById( R.id.movie_overview );

            Picasso.with( mContext).load(selectedMovie.getPoster_path())
                    .into( new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                backgroundLayout.setBackground( new BitmapDrawable( bitmap ) );
                            }
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            backgroundLayout.getBackground().setAlpha( 66 );


        }

    }
}
