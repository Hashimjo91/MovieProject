package com.hadeelkhatib.movies.Entities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hashim on 25/08/2015.
 */
public class listAdapter extends BaseAdapter {
    ArrayList<MoviesModel> moviesModels;
    Context context;

    public listAdapter(ArrayList<MoviesModel> moviesModels, Context context) {
        this.moviesModels = moviesModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return moviesModels.size();
    }

    @Override
    public MoviesModel getItem(int i) {
        return moviesModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LinearLayout lo=new LinearLayout(context);
        lo.setOrientation(LinearLayout.VERTICAL);
        TextView tv1=new TextView(context);
        tv1.setText("auther:  " + getItem(i).getTitle()+".");

        TextView tv2=new TextView(context);
        tv2.setText("Content:  "+getItem(i).getPoster_path()+"\n\n");



lo.addView(tv1);
lo.addView(tv2);


        return lo;
    }
}
