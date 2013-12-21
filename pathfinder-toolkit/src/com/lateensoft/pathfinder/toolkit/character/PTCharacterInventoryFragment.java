package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PTCharacterInventoryFragment extends PTCharacterSheetFragment implements 
	OnItemClickListener, OnClickListener, 
	android.content.DialogInterface.OnClickListener, OnFocusChangeListener{
	
	private static final String TAG = PTCharacterInventoryFragment.class.getSimpleName();
	private ListView mItemsListView;
	private Button mAddButton;
	private EditText mGoldEditText;
	private TextView mTotalWeightText;
	
	private EditText mDialogItemNameEditText;
	private EditText mDialogItemQuantityEditText;
	private EditText mDialogItemWeightEditText;
	private CheckBox mDialogItemContainedCheckbox;
	
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
		showItemDialog(mCharacter.getInventory().getItem(position));
	}

	//The add button was clicked
	public void onClick(View view) {
		mItemSelectedForEdit = -1;
		showItemDialog(null);
	}

	
	/**
	 * Shows a dialog to add or edit an item.
	 * @param item
	 */
	private void showItemDialog(PTItem item) {
		
		mDummyView.requestFocus();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		//Set up dialog layout
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.character_inventory_dialog, null);
		mDialogItemNameEditText = (EditText) dialogView.findViewById(R.id.etDialogItemName);
		mDialogItemQuantityEditText = (EditText) dialogView.findViewById(R.id.etDialogItemQuantity);
		mDialogItemWeightEditText = (EditText) dialogView.findViewById(R.id.etDialogItemWeight);
		mDialogItemContainedCheckbox = (CheckBox) dialogView.findViewById(R.id.checkboxDialogItemContained);
		
		//Determine if add or edit
		if(mItemSelectedForEdit < 0){
			builder.setTitle("Add Item");
		}
		else{
			builder.setTitle("Edit Item")
					.setNeutralButton(R.string.delete_button_text, this);
			mDialogItemNameEditText.setText(item.getName());
			mDialogItemQuantityEditText.setText(Integer.toString(item.getQuantity()));
			mDialogItemWeightEditText.setText(Double.toString(item.getWeight()));
			mDialogItemContainedCheckbox.setChecked(item.isContained());
		}
		
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
		
		

		AlertDialog alert = builder.create();
		alert.show();
	}

	// Click method for the character selection dialog
	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		//OK button tapped
		case DialogInterface.BUTTON_POSITIVE:

			if(mItemSelectedForEdit < 0){
				PTItem newItem = getItemFromDialog();
				if(newItem != null){
					mCharacter.getInventory().addItem(newItem);
					refreshItemsListView();
					updateTotalWeight();
				}
			}
			else{
				PTItem editedItem = getItemFromDialog();
				mCharacter.getInventory().setItem(editedItem, mItemSelectedForEdit);
				refreshItemsListView();
				updateTotalWeight();
			}
			
			break;
		//Cancel Button	tapped
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		//Delete Button tapped
		case DialogInterface.BUTTON_NEUTRAL:
			mCharacter.getInventory().deleteItem(mItemSelectedForEdit);
			refreshItemsListView();
			updateTotalWeight();
		default:
			break;
			
		}
		
		//Close keyboard
		InputMethodManager iMM = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(mDialogItemNameEditText.hasFocus())
			iMM.hideSoftInputFromInputMethod(mDialogItemNameEditText.getWindowToken(), 0);
		else if(mDialogItemWeightEditText.hasFocus())
			iMM.hideSoftInputFromInputMethod(mDialogItemWeightEditText.getWindowToken(), 0);
		else if(mDialogItemQuantityEditText.hasFocus())
			iMM.hideSoftInputFromInputMethod(mDialogItemQuantityEditText.getWindowToken(), 0);
	}
	
	
	//Retrieves a PTItem from the open dialog
	private PTItem getItemFromDialog(){
		String name = new String(mDialogItemNameEditText.getText().toString());
		if(name == null || name.contentEquals("")){
			return null;
		}
		
		int quantity;
		try{
			quantity = (int)Double.parseDouble(mDialogItemQuantityEditText.getText().toString());
		}catch (NumberFormatException e){
			quantity = 1;
		}
		
		double weight;
		try{
			weight = Double.parseDouble(mDialogItemWeightEditText.getText().toString());
		}catch (NumberFormatException e){
			weight = 1.0;
		}
		boolean contained = mDialogItemContainedCheckbox.isChecked();
		
		return new PTItem(name, weight, quantity, contained);
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
