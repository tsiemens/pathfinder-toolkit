package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTWeapon;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterWeaponEditActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class PTCharacterWeaponsFragment extends PTCharacterSheetFragment implements
OnClickListener, OnItemClickListener{
	
	private static final String TAG = PTCharacterWeaponsFragment.class.getSimpleName();

	private ListView mListView;
	int mWeaponSelectedForEdit;
	private Button mAddButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Starting onCreateView");
		
		setRootView(inflater.inflate(R.layout.character_weapon_fragment,
				container, false));
		
		mAddButton = (Button) getRootView().findViewById(R.id.buttonAddWeapon);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		mListView = (ListView) getRootView().findViewById(R.id.listViewWeapons);
		refreshWeaponsListView();

		mListView.setOnItemClickListener(this);
		
		Log.v(TAG, "Finishing onCreateView");
		return getRootView();
	}

	private void refreshWeaponsListView() {
		PTWeaponAdapter adapter = new PTWeaponAdapter(getActivity(),
				R.layout.character_weapon_row,
				mCharacter.getInventory().getWeaponArray());
		
		Log.v(TAG, "Called refreshWeaponsListView");
		
		mListView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mWeaponSelectedForEdit = position;
		//showWeaponDialog(mCharacter.getInventory().getWeapon(position));
		showWeaponEditor(mCharacter.getInventory().getWeapon(position));
	}



	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mWeaponSelectedForEdit = -1;
		//showWeaponDialog(null);
		showWeaponEditor(null);
	}
	
	private void showWeaponEditor(PTWeapon weapon) {
		Intent weaponEditIntent = new Intent(getActivity(),
				PTCharacterWeaponEditActivity.class);
		weaponEditIntent.putExtra(
				PTCharacterWeaponEditActivity.INTENT_EXTRAS_KEY_WEAPON, weapon);
		startActivityForResult(weaponEditIntent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTWeapon weapon = data.getExtras().getParcelable(
					PTCharacterWeaponEditActivity.INTENT_EXTRAS_KEY_WEAPON);
			Log.v(TAG, "Add.edit weapon OK: " + weapon.getName());
			if(mWeaponSelectedForEdit < 0) {
				Log.v(TAG, "Adding a weapon");
				if(weapon != null) {
					mCharacter.getInventory().addWeapon(weapon);
					refreshWeaponsListView(); 
				}
			} else {
				Log.v(TAG, "Editing a weapon");
				mCharacter.getInventory().setWeapon(weapon, mWeaponSelectedForEdit);
				refreshWeaponsListView();
			}
			
			break;
		
		case PTCharacterWeaponEditActivity.RESULT_CUSTOM_DELETE:
			Log.v(TAG, "Deleting a weapon");
			mCharacter.getInventory().deleteWeapon(mWeaponSelectedForEdit);
			refreshWeaponsListView();
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateCharacterDatabase();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void updateFragmentUI() {
		refreshWeaponsListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_weapons);
	}
}
