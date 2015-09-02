package com.hadeelkhatib.movies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hadeelkhatib.movies.Entities.MoviesModel;
import com.hadeelkhatib.movies.Entities.MoviesParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public String SORT_POPULARITY = "popularity.desc";
    public String SORT_VOTES = "vote_average.desc";
    SQLiteDatabase db;
    Context context = this;
    ArrayList<MoviesModel> moviesList = new ArrayList<MoviesModel>();
    private GridView gridView;
    private int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridView.setNumColumns(5);

        }

        db=openOrCreateDatabase("mydb",Context.MODE_PRIVATE,null);

        try {
            db.execSQL("create table myMovies (ID text Primary key ,Title text , poster text , releaseDate text , overView text , avg txt , Faved )");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {


            if (savedInstanceState == null || !savedInstanceState.containsKey("Data")) {
                invokeMethodGet(AppConstant.MOVIE_URL+SORT_POPULARITY);

            } else {
                selectedItem=savedInstanceState.getInt("selectedItem");
                moviesList.clear();
                moviesList = savedInstanceState.getParcelableArrayList("Data");


                GridViewAdapter adapter = new GridViewAdapter(context, moviesList);
                gridView.setAdapter(adapter);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("DATA", (Serializable) moviesList.get(position));
        startActivity(intent);

    }


    public void invokeMethodGet( String url) throws JSONException, UnsupportedEncodingException {

        Log.e("URL", url);


        AsyncHttpClient client = new AsyncHttpClient();


        client.setTimeout(50000);

        client.get(context, url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] data) {
                String content = new String(data);
                Log.e("onSuccess", content);

                try {

                    moviesList.clear();
                    moviesList = MoviesParser.Parse(content);
                    for(MoviesModel item:moviesList)
                    {
                        try {
                            db.execSQL("insert into myMovies values(\""+item.getId()+"\",\""+item.getTitle()+"\",\""+item.getPoster_path()+"\",\""+item.getRelease_date()+"\",\""+item.getOverview()+"\",\""+item.getVote_average()+"\",\"no\")");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    Cursor c=db.rawQuery("select * from myMovies",null);
                    ArrayList<MoviesModel> moviesModelList=new ArrayList<MoviesModel>();
                    while (c.moveToNext())
                    {
                        MoviesModel mm=new MoviesModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5));
                        mm.setFaved(c.getString(6));

                        moviesModelList.add(mm);

                    }
                    GridViewAdapter adapter = new GridViewAdapter(context, moviesModelList);
                    gridView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {


        final CharSequence[] items = {"Most Popular", "Highest-Rated"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by:");
        builder.setSingleChoiceItems(items, selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();

                String url="";
                switch (item) {
                    case 0:
                         url = AppConstant.MOVIE_URL + SORT_POPULARITY;
                        break;

                    case 1:
                        url = AppConstant.MOVIE_URL+SORT_VOTES;
                        break;


                }

                selectedItem = item;
                try {


                    invokeMethodGet(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Data",moviesList);
        outState.putInt("selectedItem", selectedItem);
        super.onSaveInstanceState(outState);
    }
}
