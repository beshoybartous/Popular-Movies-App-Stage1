package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.MoviesModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private Context mContext;
    private  List<MoviesModel.ResultsBean> mMovies;
    final private ListItemClickListiner listItemClickListiner;

    public MovieAdapter(Context context,ListItemClickListiner listener){
        mContext=context;
        listItemClickListiner=listener;
        mMovies=new ArrayList<>(  );

    }

    public void setMovies(List<MoviesModel.ResultsBean>resultsBeans){
        mMovies=resultsBeans;
    }

    @Override
    public MovieViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View viewHolder=LayoutInflater.from( mContext ).inflate( R.layout.movies_items,parent,false);
        return new MovieViewHolder( viewHolder );
    }

    @Override
    public void onBindViewHolder( MovieViewHolder holder, int position) {
            Picasso.with( mContext ).load( mMovies.get( position ).getPoster_path() ).into( holder.moviePosterIv);
            holder.movieTitleTv.setText( mMovies.get( position ).getTitle() );
    }

    @Override
    public int getItemCount() {
         return mMovies.size();
    }
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView moviePosterIv;
    private TextView movieTitleTv;
        public MovieViewHolder( View itemView) {
            super( itemView );
            moviePosterIv=(ImageView)itemView.findViewById( R.id.movies_poster );
            movieTitleTv=(TextView) itemView.findViewById( R.id.movies_title );
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
                listItemClickListiner.onListItemClick( position );
        }
    }

    public interface ListItemClickListiner{
        void onListItemClick(int clickedItemIndex);
    }

}
