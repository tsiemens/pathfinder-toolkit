package com.lateensoft.pathfinder.toolkit.character;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterArmorEditActivity;

public class PTCharacterArmorFragment extends PTCharacterSheetFragment implements
	OnItemClickListener, OnClickListener {
	private static final String TAG = PTCharacterArmorFragment.class.getSimpleName();
	
	private ListView mListView;
	int mArmorSelectedForEdit;
	private Button mAddButton;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mParentView = inflater.inflate(R.layout.character_armor_fragment,
				container, false);
		
		mAddButton = (Button) mParentView.findViewById(R.id.buttonAddArmor);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		mListView = (ListView) mParentView.findViewById(R.id.lvArmor);
		refreshArmorListView();

		mListView.setOnItemClickListener(this);
		
		return mParentView;
	}
	
	private void refreshArmorListView() {
		PTArmorAdapter adapter = new PTArmorAdapter(getActivity(),
				R.layout.character_armor_row,
				mCharacter.getInventory().getArmorArray());
		for (PTArmor a : mCharacter.getInventory().getArmorArray())
		Log.d(TAG, ""+a);
		
		Log.d(TAG, "Called refreshArmorListView");
		
		mListView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mArmorSelectedForEdit = position;
		showArmorEditor(mCharacter.getInventory().getArmor(position));
	}

	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mArmorSelectedForEdit = -1;
		showArmorEditor(null);
	}

	private void showArmorEditor(PTArmor armor) {
		Intent armorEditIntent = new Intent(getActivity(),
				PTCharacterArmorEditActivity.class);
		armorEditIntent.putExtra(
				PTCharacterArmorEditActivity.INTENT_EXTRAS_KEY_ARMOR, armor);
		startActivityForResult(armorEditIntent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTArmor armor = data.getExtras().getParcelable(
					PTCharacterArmorEditActivity.INTENT_EXTRAS_KEY_ARMOR);
			Log.v(TAG, "Add.edit armor OK: " + armor.getName());
			if(mArmorSelectedForEdit < 0) {
				Log.v(TAG, "Adding an armor");
				if(armor != null) {
					mCharacter.getInventory().addArmor(armor);
					refreshArmorListView(); 
				}
			} else {
				Log.v(TAG, "Editing an armor");
				mCharacter.getInventory().setArmor(armor, mArmorSelectedForEdit);
				refreshArmorListView();
			}
			
			break;
		
		case PTCharacterArmorEditActivity.RESULT_CUSTOM_DELETE:
			Log.v(TAG, "Deleting an armor");
			mCharacter.getInventory().deleteArmor(mArmorSelectedForEdit);
			refreshArmorListView();
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
		refreshArmorListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_armor);
	}
}
