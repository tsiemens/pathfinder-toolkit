package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTItem;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterInventoryEditActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PTCharacterInventoryFragment extends PTCharacterSheetFragment implements 
	OnItemClickListener, OnClickListener, OnFocusChangeListener{
	
	private static final String TAG = PTCharacterInventoryFragment.class.getSimpleName();
	private ListView mItemsListView;
	private Button mAddButton;
	private EditText mGoldEditText;
	private TextView mTotalWeightText;
	
	private int mItemSelectedForEdit;
	private View mDummyView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_inventory_fragment, 
				container, false));
		
		mDummyView = (View) getRootView().findViewById(R.id.dummyView);
		
		mAddButton = (Button) getRootView().findViewById(R.id.buttonAddItem);
		mAddButton.setOnClickListener(this);
		
		mGoldEditText = (EditText) getRootView().findViewById(R.id.editTextGold);
		mGoldEditText.setOnFocusChangeListener(this);
		
		mTotalWeightText = (TextView)  getRootView().findViewById(R.id.tvWeightTotal);
		
		mItemsListView = (ListView) getRootView().findViewById(R.id.listViewInventory);
		mItemsListView.setOnItemClickListener(this);
		
		updateFragmentUI();
		
		return getRootView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mDummyView.requestFocus();
	}

	@Override
	public void onPause() {
		mCharacter.mGold = Double.parseDouble(mGoldEditText.getText().toString());
		/*InputMethodManager iMM = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		iMM.hideSoftInputFromInputMethod(mGoldEditText.getWindowToken(), 0);*/
		super.onPause();
	}

	private void refreshItemsListView(){
		PTItem[] items = mCharacter.getInventory().getItems();	
	
		PTInventoryAdapter adapter = new PTInventoryAdapter(getActivity(), R.layout.character_inventory_row, items);
		mItemsListView.setAdapter(adapter);
	
	}
	
	private void updateTotalWeight(){
		double totalWeight = mCharacter.getInventory().getTotalWeight();
		
		mTotalWeightText.setText(getActivity().getString(R.string.inventory_total_weight_header)
				+" "+ totalWeight);
	}

	//An items has been clicked in the list
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mItemSelectedForEdit = position;
		showItemEditor(mCharacter.getInventory().getItem(position));
	}

	//The add button was clicked
	public void onClick(View view) {
		mItemSelectedForEdit = -1;
		showItemEditor(null);
	}
	
	private void showItemEditor(PTItem item) {
		Intent itemEditIntent = new Intent(getActivity(),
				PTCharacterInventoryEditActivity.class);
		itemEditIntent.putExtra(
				PTCharacterInventoryEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, item);
		startActivityForResult(itemEditIntent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTItem item = data.getExtras().getParcelable(
					PTCharacterInventoryEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE);
			Log.v(TAG, "Add/edit item OK: " + item.getName());
			if(mItemSelectedForEdit < 0) {
				Log.v(TAG, "Adding an item");
				if(item != null) {
					mCharacter.getInventory().addItem(item);
					refreshItemsListView();
					updateTotalWeight();
				}
			} else {
				Log.v(TAG, "Editing an item");
				mCharacter.getInventory().setItem(item, mItemSelectedForEdit);
				refreshItemsListView();
				updateTotalWeight();
			}
			
			break;
		
		case PTCharacterInventoryEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting an item");
			mCharacter.getInventory().deleteItem(mItemSelectedForEdit);
			refreshItemsListView();
			updateTotalWeight();
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateCharacterDatabase();
		super.onActivityResult(requestCode, resultCode, data);
	}

	//Gold Edit text
	public void onFocusChange(View view, boolean isInFocus) {
		if(!isInFocus)
			mCharacter.mGold = Double.parseDouble(mGoldEditText.getText().toString());
	}

	@Override
	public void updateFragmentUI() {
		mGoldEditText.setText(Double.toString(mCharacter.mGold));
		updateTotalWeight();
		refreshItemsListView();	
	}

	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_inventory);
	}
	
}
