package com.lateensoft.pathfinder.toolkit;

import com.lateensoft.pathfinder.toolkit.views.settings.PTSettingsActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class PTSharedMenu {

	// start at 1000 ensure that we do not interfere with the activity-specific
	// menu items
	public static final int MENU_SETTINGS = 1000;

	public static void onCreateOptionsMenu(Menu menu, Context context) {
		menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, context.getResources()
				.getString(R.string.shared_menu_settings));
	}

	public static boolean onOptionsItemSelected(MenuItem item, Activity caller) {
		switch (item.getItemId()) {
		case PTSharedMenu.MENU_SETTINGS:
			showSettingsActivity(caller);
			return true;
		default:
			return false;
		}
	}

	private static void showSettingsActivity(Activity caller) {

		caller.startActivity(new Intent(caller, PTSettingsActivity.class));
	}

	
}
