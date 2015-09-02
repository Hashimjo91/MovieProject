package com.hadeelkhatib.movies.Entities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hadeel on 7/14/2015.
 */
public class MoviesParser {

public static ArrayList<MoviesModel> Parse(String json) throws JSONException {
    JSONObject jsonObject = new JSONObject(json);
    JSONArray jsonArray = new JSONArray();
    jsonArray=jsonObject.getJSONArray("results");
    ArrayList<MoviesModel> moviesList = new ArrayList<MoviesModel>();

    for (int i=0 ;i<jsonArray.length();i++){
        MoviesModel moviesModel = new MoviesModel();
        moviesModel.setId(jsonArray.getJSONObject(i).getString("id"));
        moviesModel.setTitle(jsonArray.getJSONObject(i).getString("title"));
        moviesModel.setPoster_path(jsonArray.getJSONObject(i).getString("poster_path"));
        moviesModel.setOverview(jsonArray.getJSONObject(i).getString("overview"));
        moviesModel.setRelease_date(jsonArray.getJSONObject(i).getString("release_date"));
        moviesModel.setVote_average(jsonArray.getJSONObject(i).getString("vote_average"));


        moviesList.add(moviesModel);
    }


    return moviesList;


}

}
