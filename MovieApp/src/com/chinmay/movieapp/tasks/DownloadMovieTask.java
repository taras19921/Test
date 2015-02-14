package com.chinmay.movieapp.tasks;

import android.os.AsyncTask;

import com.chinmay.movieapp.MovieItemListActivity;
import com.chinmay.movieapp.movie.MovieContent;
import com.chinmay.movieapp.widget.ArrayAdapter;

public class DownloadMovieTask extends AsyncTask<Integer, Void, Void> {
	ArrayAdapter arrayAdapter;
	MovieItemListActivity activity;
	public DownloadMovieTask(ArrayAdapter arrayAdapter, MovieItemListActivity activity) {
		this.arrayAdapter = arrayAdapter;
		this.activity = activity;
	}
	@Override
	protected Void doInBackground(Integer... params) {
		MovieContent.loadMovies(params[0]);
		return null;
	}
	@Override
	protected void onPostExecute(Void v) {
		arrayAdapter.refresh();
        arrayAdapter.notifyDataSetChanged();
        activity.finishedDownload();
    }
}
