package com.lateensoft.pathfinder.toolkit.character;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterFeatEditActivity;

public class PTCharacterFeatsFragment extends PTCharacterSheetFragment
		implements OnClickListener, OnItemClickListener {

	private static final String TAG = PTCharacterFeatsFragment.class.getSimpleName();
	private ListView mFeatsListView;
	private Button mAddButton;

	private EditText mDialogFeatNameEditText;
	private EditText mDialogFeatDescEditText;

	private ViewGroup mContainer;

	private int mFeatSelectedForEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mContainer = container;

		setRootView(inflater.inflate(R.layout.character_feats_fragment,
				container, false));

		mAddButton = (Button) getRootView().findViewById(R.id.buttonAddFeat);
		mAddButton.setOnClickListener(this);

		mFeatsListView = (ListView) getRootView()
				.findViewById(R.id.listViewFeats);
		refreshFeatsListView();
		mFeatsListView.setOnItemClickListener(this);

		return getRootView();
	}

	private void refreshFeatsListView() {
		String[] featNames = mCharacter.getFeatList().getFeatNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				mContainer.getContext(), android.R.layout.simple_list_item_1,
				featNames);
		mFeatsListView.setAdapter(adapter);

	}

	// Add Feat button was tapped
	public void onClick(View button) {
		mFeatSelectedForEdit = -1;
		showFeatEditor(null);

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mFeatSelectedForEdit = position;
		showFeatEditor(mCharacter.getFeatList().getFeat(position));

	}
	
	private void showFeatEditor(PTFeat feat) {
		Intent featEditIntent = new Intent(getActivity(),
				PTCharacterFeatEditActivity.class);
		featEditIntent.putExtra(
				PTCharacterFeatEditActivity.INTENT_EXTRAS_KEY_FEAT, feat);
		startActivityForResult(featEditIntent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTFeat item = data.getExtras().getParcelable(
					PTCharacterFeatEditActivity.INTENT_EXTRAS_KEY_FEAT);
			Log.v(TAG, "Add/edit feat OK: " + item.getName());
			if(mFeatSelectedForEdit < 0) {
				Log.v(TAG, "Adding a feat");
				if(item != null) {
					mCharacter.getFeatList().addFeat(item);
					refreshFeatsListView();
				}
			} else {
				Log.v(TAG, "Editing a feat");
				mCharacter.getFeatList().setFeat(item, mFeatSelectedForEdit);
				refreshFeatsListView();
			}
			
			break;
		
		case PTCharacterFeatEditActivity.RESULT_CUSTOM_DELETE:
			Log.v(TAG, "Deleting an item");
			mCharacter.getFeatList().deleteFeat(mFeatSelectedForEdit);
			refreshFeatsListView();
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
		refreshFeatsListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_feats);
	}

}
