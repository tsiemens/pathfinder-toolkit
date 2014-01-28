package com.lateensoft.pathfinder.toolkit;

import android.view.MenuInflater;
import com.lateensoft.pathfinder.toolkit.views.settings.PTSettingsActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class PTSharedMenu {

	public static void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.shared_menu, menu);
	}

	public static boolean onOptionsItemSelected(MenuItem item, Activity activity) {
		switch (item.getItemId()) {
		case R.id.mi_app_settings:
			showSettingsActivity(activity);
			return true;
		default:
			return false;
		}
	}

	private static void showSettingsActivity(Activity caller) {

		caller.startActivity(new Intent(caller, PTSettingsActivity.class));
	}

	
}
