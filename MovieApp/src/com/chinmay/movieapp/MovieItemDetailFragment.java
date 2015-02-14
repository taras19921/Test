package com.chinmay.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinmay.movieapp.movie.MovieContent;
import com.chinmay.movieapp.tasks.DownloadDetailsTask;
import com.chinmay.movieapp.tasks.DownloadImageTask;

/**
 * A fragment representing a single MovieItem detail screen. This fragment is
 * either contained in a {@link MovieItemListActivity} in two-pane mode (on
 * tablets) or a {@link MovieItemDetailActivity} on handsets.
 */
public class MovieItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private MovieContent.MovieItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MovieItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = MovieContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_movieitem_detail,
				container, false);
		
		
		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.movieitem_detail);
			((TextView)layout.findViewById(R.id.movieName)).setText(mItem.title);
			((TextView)layout.findViewById(R.id.movieYear)).setText(mItem.releaseDate);
			((TextView)layout.findViewById(R.id.movieRating)).setText(mItem.rating);
			ImageView imageView = (ImageView)layout.findViewById(R.id.moviePoster);
			if(mItem.hugePosterURL != null)
				new DownloadImageTask(imageView).execute(mItem.hugePosterURL);
			new DownloadDetailsTask(layout, inflater, container).execute(mItem);
		}

		return rootView;
	}	
	
}
