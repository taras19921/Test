package com.chinmay.movieapp;

import com.chinmay.movieapp.movie.MovieContent;
import com.chinmay.movieapp.tasks.DownloadMovieTask;
import com.chinmay.movieapp.widget.ArrayAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of MovieItems. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MovieItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieItemListFragment} and the item details (if present) is a
 * {@link MovieItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MovieItemListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class MovieItemListActivity extends FragmentActivity implements
		MovieItemListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private ArrayAdapter adapter;
	private boolean loading;
	private boolean filterActive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_movieitem_list);
		adapter = (ArrayAdapter)((MovieItemListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.movieitem_list)).getListAdapter();
		loadMoreMovies();
		String title = getIntent().getStringExtra(Intent.EXTRA_TITLE);
		setTitle(title);
		final EditText searchBar = ((EditText)findViewById(R.id.searchMovie));
		
		searchBar.addTextChangedListener(new TextWatcher() {
		     
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changed the Text
		    	filterActive = (searchBar.getText().toString().length() > 0 );
		    	MovieItemListActivity.this.adapter.getFilter().filter(searchBar.getText());  
		    }
		     
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {
		        // TODO Auto-generated method stub
		         
		    }
		     
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub                         
		    }
		});

		if (findViewById(R.id.movieitem_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MovieItemListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.movieitem_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	public void loadMoreMovies() {
		if(!loading && !filterActive) {
			loading = true;
			int type = getIntent().getIntExtra("movie_type", 0);
			new DownloadMovieTask(adapter, this).execute(type);
		}
	}

	/**
	 * Callback method from {@link MovieItemListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(MovieItemDetailFragment.ARG_ITEM_ID, id);
			MovieItemDetailFragment fragment = new MovieItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.movieitem_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					MovieItemDetailActivity.class);
			detailIntent.putExtra(MovieItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		MovieContent.reset();
	}

	public void finishedDownload() {
		adapter.notifyDataSetChanged();
		loading = false;
	}
}
