package com.hadeelkhatib.movies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hadeelkhatib.movies.Entities.MoviesModel;
import com.hadeelkhatib.movies.Entities.MoviesParser;
import com.hadeelkhatib.movies.Entities.listAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    TextView titleTv, synopsisTv, ratingTv, releaseDateTv;
    ImageView posterImgView;
    WebView mview;
    private Activity context = this;
    ListView lv;
    listAdapter la;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
       final MoviesModel model = (MoviesModel) bundle.getSerializable("DATA");


        titleTv = (TextView) findViewById(R.id.title);
        synopsisTv = (TextView) findViewById(R.id.synopsis);
        ratingTv = (TextView) findViewById(R.id.rating);
        releaseDateTv = (TextView) findViewById(R.id.release_date);
        posterImgView = (ImageView) findViewById(R.id.poster);

        lv=(ListView) findViewById(R.id.list);
        WebView mwWebView = ((WebView) findViewById(R.id.webView));
        mview=mwWebView;
        mwWebView.getSettings().setJavaScriptEnabled(true);
        // web.getSettings().setDomStorageEnabled(true);

        mwWebView.getSettings().setAllowContentAccess(true);
        WebSettings webSettings = mwWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true);
        mwWebView.setWebChromeClient(new WebChromeClient());
        webSettings.setLoadWithOverviewMode(true);

                ((TextView) findViewById(R.id.tvTrailer)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            invokeMethodGet(AppConstant.TRAILER_URL_PART_ONE + model.getId() + AppConstant.TRAILER_URL_PART_TWO, 1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
        try {
            invokeMethodGet(AppConstant.REVIEW_URL_PART_ONE + model.getId() + AppConstant.REVIEW_URL_PART_TWO, 2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mwWebView.setVisibility(View.INVISIBLE);




        titleTv.setText("Title: " + model.getTitle());
    synopsisTv.setText("Plot Synopsis: "+model.getOverview());
    ratingTv.setText("User Rating: "+model.getVote_average());
    releaseDateTv.setText("Release Date: " + model.getRelease_date());


    Glide.with(context).
    load(AppConstant.IMAGE_PATH + model.getPoster_path()).
    into(posterImgView);


}
    public void invokeMethodGet(String url,final int type) throws JSONException, UnsupportedEncodingException {

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
                    if(type==1) {
                        JSONObject jsonObject = new JSONObject(content);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray = jsonObject.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String key = (jsonArray.getJSONObject(i).getString("key"));
                            mview.loadUrl("https://www.youtube.com/watch?v=" + key);
                        }
                    }else
                    {
                        JSONObject jsonObject = new JSONObject(content);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray = jsonObject.getJSONArray("results");
                        ArrayList<MoviesModel> moviesList = new ArrayList<MoviesModel>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            MoviesModel moviesModel = new MoviesModel();
                            moviesModel.setTitle(jsonArray.getJSONObject(i).getString("author"));
                            moviesModel.setPoster_path(jsonArray.getJSONObject(i).getString("content"));
                            moviesList.add(moviesModel);
                        }
                        la=new listAdapter(moviesList,context);
                        lv.setAdapter(la);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

            }


        });

    }


}
