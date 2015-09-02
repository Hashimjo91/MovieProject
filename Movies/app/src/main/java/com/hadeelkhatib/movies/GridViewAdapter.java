package com.hadeelkhatib.movies;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hadeelkhatib.movies.Entities.MoviesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hadeel on 7/10/2015.
 */
public class GridViewAdapter extends BaseAdapter {
    private final ArrayList<MoviesModel> moviesList;
    private Context mContext;
    private ViewHolder holder;
    SQLiteDatabase db;

    public GridViewAdapter(Context c, ArrayList<MoviesModel> moviesList) {
        mContext = c;
        this.moviesList = moviesList;
        db = c.openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);
    }

    public int getCount() {
        return moviesList.size();
    }

    public MoviesModel getItem(int position) {
        return moviesList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        try {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.grid_item, parent, false);

            holder.posterImageView = (ImageView) convertView.findViewById(R.id.poster_item_imageview);
            holder.favbtn = (ImageView) convertView.findViewById(R.id.fav_btn);

            convertView.setTag(holder);

        Glide.with(mContext)
                .load(AppConstant.IMAGE_PATH + getItem(position).getPoster_path().toString())
                .into(holder.posterImageView);



      if(getItem(position).getFaved().contains("yes")){
               holder.favbtn.setImageResource(android.R.drawable.star_on);
                holder.favbtn.setTag("yes");
       }else
      {
          holder.favbtn.setTag("no");
      }

        final View finalConvertView = convertView;

        holder.favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if(!view.getTag().toString().contains( "yes")) {
                        db.execSQL("update myMovies set Faved =\"yes\" where id=\""+getItem(position).getId()+"\" and poster =\""+getItem(position).getPoster_path()+"\"");
                        view.setTag("yes");

                    }else
                    {

                        db.execSQL("update myMovies set Faved =\"no\" where id=\""+getItem(position).getId()+"\"and poster =\""+getItem(position).getPoster_path()+"\"");
                        view.setTag("no");
                    }

                Cursor c=db.rawQuery("select * from myMovies",null);
                ArrayList<MoviesModel> moviesModelList=new ArrayList<MoviesModel>();
                while (c.moveToNext())
                {
                    MoviesModel mm=new MoviesModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5));
                    mm.setFaved(c.getString(6));

                    moviesModelList.add(mm);

                }
                moviesList.clear();
                       moviesList.addAll( moviesModelList);
                notifyDataSetChanged();
            }
        });
        }catch (Exception e)
        {

        }
        return convertView;
    }

    private static class ViewHolder {
        public ImageView posterImageView;
        public ImageView favbtn;
    }
}