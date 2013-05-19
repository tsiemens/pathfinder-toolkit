package com.lateensoft.pathfinder.toolkit;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTUserPrefsManager;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

public class PTMainMenu extends ListActivity implements OnClickListener {

	private final String TAG = "PTMainMenu";
	String mListLabels[];
	private PTUserPrefsManager mUserPrefsManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUserPrefsManager = new PTUserPrefsManager(this);
		PTDatabaseManager SQLManager = new PTDatabaseManager(this.getApplicationContext());
		
		//Needs to update the database after upgrading
		if(mUserPrefsManager.checkLastUsedVersion(this)){
			SQLManager.performUpdates(this);
			mUserPrefsManager.setLastUsedVersion(this);
		}
		
		mListLabels = getResources().getStringArray(R.array.main_menu_array);

		setContentView(R.layout.activity_main_menu);

		setListAdapter(new PTMainMenuAdapter(this,
				R.layout.main_menu_row, mListLabels));

		showRateDialogIfRequired();
	}

    private void showRateDialogIfRequired(){
    	if(mUserPrefsManager.checkLastRateTime() && mUserPrefsManager.checkLastRatedVersion(this)){
    		showRateAppPromptDialog();
    	}
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		switch (position) {
		case 0:
			startActivity(new Intent(this, PTDiceRollerActivity.class));
			break;

		case 1:
			startActivity(new Intent(this, PTCharacterSheetActivity.class));
			break;
		case 2:
			startActivity(new Intent(this, PTInitiativeTrackerActivity.class));
			break;
		case 3:
			startActivity(new Intent(this, PTPartySkillCheckerActivity.class));
			break;
		case 4:
			startActivity(new Intent(this, PTPartyManagerActivity.class));
			break;
		case 5:
			startActivity(new Intent(this, PTPointbuyCalculator.class));
			break;
		}
	}
    
	public void showRateAppPromptDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//Set up dialog layout

		ImageView dialogMemeImage = new ImageView(this);
		dialogMemeImage.setImageResource(R.drawable.rate_meme);
		dialogMemeImage.setScaleType(ScaleType.FIT_CENTER);
		
		builder.setTitle(getString(R.string.rate_dialog_header));
		
		builder.setView(dialogMemeImage)
				.setPositiveButton(getString(R.string.rate_dialog_positive_button), this)
				.setNegativeButton(getString(R.string.rate_dialog_negative_button), this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Initialize the global menu items
		PTSharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		
		//Add aditional menu items
		return true;
	}



	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    if(PTSharedMenu.onOptionsItemSelected(item, this) == false) {
	      // handle local menu items here or leave blank
	    }
	    
	    return super.onOptionsItemSelected(item);
	  }


	//For dialog
	public void onClick(DialogInterface dialogInterface, int selection) {
		switch(selection){
		case DialogInterface.BUTTON_POSITIVE:
			mUserPrefsManager.setLastRatedVersion(this);
			mUserPrefsManager.setRatePromptTime();
			Log.v(TAG, "Attempting to open market page");
			String appPackageName = this.getPackageName();
			try {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName)));
			} catch (ActivityNotFoundException e) {
				Log.v(TAG, "Failed to open market. Attempting in browser.");
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appPackageName)));
			}
			break;
		default:
			Log.v(TAG, "Delaying rate dialog.");
			mUserPrefsManager.setRatePromptTime();
		}
		
	}
	
    
}
