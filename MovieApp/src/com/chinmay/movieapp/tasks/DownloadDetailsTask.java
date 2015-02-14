package com.chinmay.movieapp.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.chinmay.movieapp.R;
import com.chinmay.movieapp.movie.MovieContent;
import com.chinmay.movieapp.movie.MovieContent.Cast;

public class DownloadDetailsTask extends AsyncTask<MovieContent.MovieItem, Void, MovieContent.MovieDetails> {
	RelativeLayout relativeLayout;
	LayoutInflater inflater;
	ViewGroup container;

	public DownloadDetailsTask(RelativeLayout relativeLayout,
			LayoutInflater inflater, ViewGroup container) {
		this.relativeLayout = relativeLayout;
		this.inflater = inflater;
		this.container = container;
	}
	
	@Override
	public void onPreExecute() {
		relativeLayout.findViewById(R.id.movieDetailRelativeLayout).setVisibility(View.GONE);
	}

	protected MovieContent.MovieDetails doInBackground(MovieContent.MovieItem... movies) {
		MovieContent.MovieItem movie = movies[0];
		JSONObject movieDetails = null;
		try {
			URLConnection urlConn = new URL(movie.movieURL).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			JSONParser parser = new JSONParser();
			movieDetails = (JSONObject) parser.parse(in);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		try {
			URLConnection urlConn = new URL(movie.castURL).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(in);
			JSONArray array = (JSONArray) obj.get("cast");
			movieDetails.put("cast", array);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return new MovieContent.MovieDetails(movieDetails);
	}
	@Override
	protected void onPostExecute(MovieContent.MovieDetails movieDetails) {
		((TextView) relativeLayout.findViewById(R.id.movieGenre)).setText(movieDetails.getGenres());
		((TextView) relativeLayout.findViewById(R.id.movieOverview)).setText(movieDetails.overview);
		((TextView) relativeLayout.findViewById(R.id.movieProduction)).setText(movieDetails.getProductions());
		((TextView) relativeLayout.findViewById(R.id.movieLanguage)).setText(movieDetails.getLanguages());
		((TextView) relativeLayout.findViewById(R.id.movieLocation)).setText(movieDetails.getContries());
		TableLayout tableLayout = (TableLayout) relativeLayout.findViewById(R.id.movieCast);
		for(Cast cast : movieDetails.castList) {
			TableRow rowLayout = (TableRow) inflater.inflate(R.layout.table_row_layout, container, false);
			((TextView) rowLayout.findViewById(R.id.castName)).setText(cast.name);
			((TextView) rowLayout.findViewById(R.id.castCharacter)).setText(cast.character);
			tableLayout.addView(rowLayout);
			if(cast.profileURL != null ) {
				ImageView pic = ((ImageView) rowLayout.findViewById(R.id.castImage));
				new DownloadImageTask(pic).execute(cast.profileURL);
			}
		}
		relativeLayout.findViewById(R.id.movieLoading).setVisibility(View.GONE);
		relativeLayout.findViewById(R.id.movieDetailRelativeLayout).setVisibility(View.VISIBLE);
	}
}