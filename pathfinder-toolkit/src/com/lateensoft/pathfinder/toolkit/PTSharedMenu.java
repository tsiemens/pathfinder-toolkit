package com.lateensoft.pathfinder.toolkit;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PTSharedMenu {

	// start at 1000 ensure that we do not interfere with the activity-specific
	// menu items
	public static final int MENU_ABOUT = 1000;

	public static void onCreateOptionsMenu(Menu menu, Context context) {
		menu.add(Menu.NONE, MENU_ABOUT, Menu.NONE, context.getResources()
				.getString(R.string.shared_menu_about));

	}

	public static boolean onOptionsItemSelected(MenuItem item, Activity caller) {
		switch (item.getItemId()) {
		case PTSharedMenu.MENU_ABOUT:
			showAboutDialog(caller);
			return true;
		default:
			return false;
		}
	}

	private static void showAboutDialog(Activity caller) {

		AlertDialog.Builder builder = new AlertDialog.Builder(caller);
		builder.setTitle(caller.getResources().getString(R.string.shared_menu_about));

		LayoutInflater inflater = caller.getLayoutInflater();
		
		View dialogView = (View)inflater.inflate(caller.getResources().getLayout(R.layout.about_screen), null);
		TextView versionText = (TextView)dialogView.findViewById(R.id.textAboutVersion);
		
		PackageInfo pInfo;
		try{
		pInfo = caller.getPackageManager().getPackageInfo(caller.getPackageName(), 0);
		versionText.setText(caller.getString(R.string.app_version_name) + " " + pInfo.versionName);
		}catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		
		builder.setView(dialogView);
		builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// Closes automatically
				
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	
}
