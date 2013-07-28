package com.lateensoft.pathfinder.toolkit.character.sheet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;

public class PTCharacterArmorFragment extends PTCharacterSheetFragment implements
	OnItemClickListener, OnClickListener, OnArmorDialogReturnListener {
	
	private static final String TAG = PTCharacterArmorFragment.class.getSimpleName();
	private static final String DIALOG_NAME = "ArmorDialog";
	private static final int DIALOG_CODE = 1;
	private ListView mListView;
	int mArmorSelectedForEdit;
	private Button mAddButton;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Starting onCreateView");
		
		mParentView = inflater.inflate(R.layout.character_armor_fragment,
				container, false);
		
		mAddButton = (Button) mParentView.findViewById(R.id.buttonAddArmor);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		mListView = (ListView) mParentView.findViewById(R.id.lvArmor);
		refreshArmorListView();

		mListView.setOnItemClickListener(this);
		
		Log.v(TAG, "Finishing onCreateView");
		return mParentView;
	}
	
	private void refreshArmorListView() {
		PTArmorAdapter adapter = new PTArmorAdapter(getActivity(),
				R.layout.character_armor_row,
				mCharacter.getInventory().getArmorArray());
		
		Log.v(TAG, "Called refreshArmorListView");
		
		mListView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mArmorSelectedForEdit = position;
		showArmorDialog(mCharacter.getInventory().getArmor(position));
	}

	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mArmorSelectedForEdit = -1;
		showArmorDialog(null);
	}

	private void showArmorDialog(PTArmor armor) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_NAME);
	    if (prev != null) {
	        ft.remove(prev);
	    }
		
		ft.addToBackStack(null);
		PTArmorDialogFragment newFragment = PTArmorDialogFragment.newInstance(armor);
		newFragment.setTargetFragment(this, DIALOG_CODE);
		newFragment.show(ft, DIALOG_NAME);
	}
	
	public void onArmorDialogReturn(PTArmorDialogFragment.ArmorReturn val) {
		PTArmor armor = val.armor;
		switch (val.action) {
		case ADD_EDIT:
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
		
		case DELETE:
			Log.v(TAG, "Deleting an armor");
			mCharacter.getInventory().deleteArmor(mArmorSelectedForEdit);
			refreshArmorListView();
			break;
		
		case CANCEL:
			break;
		
		default:
			break;
		}
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
