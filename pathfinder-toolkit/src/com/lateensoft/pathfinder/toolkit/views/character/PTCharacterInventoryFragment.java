package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTInventoryAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTItemRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;

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
	
	private ListView m_itemsListView;
	private Button m_addButton;
	private EditText m_goldEditText;
	private TextView m_totalWeightText;
	
	private int m_itemIndexSelectedForEdit;
	
	private PTCharacter m_character;
	private PTItemRepository m_itemRepo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_itemRepo = new PTItemRepository();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_inventory_fragment, 
				container, false));
		
		m_addButton = (Button) getRootView().findViewById(R.id.buttonAddItem);
		m_addButton.setOnClickListener(this);
		
		m_goldEditText = (EditText) getRootView().findViewById(R.id.editTextGold);
		m_goldEditText.setOnFocusChangeListener(this);
		
		m_totalWeightText = (TextView)  getRootView().findViewById(R.id.tvWeightTotal);
		
		m_itemsListView = (ListView) getRootView().findViewById(R.id.listViewInventory);
		m_itemsListView.setOnItemClickListener(this);
		
		return getRootView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		m_character.setGold(Double.parseDouble(m_goldEditText.getText().toString()));
		super.onPause();
	}

	private void refreshItemsListView(){
		PTItem[] items = m_character.getInventory().getItems();	
	
		PTInventoryAdapter adapter = new PTInventoryAdapter(getActivity(), R.layout.character_inventory_row, items);
		m_itemsListView.setAdapter(adapter);
	
	}
	
	private void updateTotalWeight(){
		double totalWeight = m_character.getInventory().getTotalWeight();
		
		m_totalWeightText.setText(getActivity().getString(R.string.inventory_total_weight_header)
				+" "+ totalWeight);
	}

	//An items has been clicked in the list
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		m_itemIndexSelectedForEdit = position;
		showItemEditor(m_character.getInventory().getItem(position));
	}

	//The add button was clicked
	public void onClick(View view) {
		m_itemIndexSelectedForEdit = -1;
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
			if(m_itemIndexSelectedForEdit < 0) {
				Log.i(TAG, "Adding item "+item.getName());
				if(item != null) {
					item.setCharacterID(getCurrentCharacterID());
					if (m_itemRepo.insert(item) != -1) {
						m_character.getInventory().addItem(item);
						refreshItemsListView();
						updateTotalWeight();
					}
				}
			} else {
				Log.v(TAG, "Editing an item "+item.getName());
				if (m_itemRepo.update(item) != 0) {
					m_character.getInventory().setItem(item, m_itemIndexSelectedForEdit);
					refreshItemsListView();
					updateTotalWeight();
				}
			}
			
			break;
		
		case PTCharacterInventoryEditActivity.RESULT_DELETE:
			PTItem itemToDelete = m_character.getInventory().getItem(m_itemIndexSelectedForEdit);
			Log.i(TAG, "Deleting item "+itemToDelete.getName());
			if(itemToDelete != null && m_itemRepo.delete(itemToDelete) != 0 ) {
				m_character.getInventory().deleteItem(m_itemIndexSelectedForEdit);
				refreshItemsListView();
				updateTotalWeight();
			}
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateDatabase();
		super.onActivityResult(requestCode, resultCode, data);
	}

	//Gold Edit text
	public void onFocusChange(View view, boolean isInFocus) {
		if(!isInFocus)
			m_character.setGold(Double.parseDouble(m_goldEditText.getText().toString()));
	}

	@Override
	public void updateFragmentUI() {
		m_goldEditText.setText(Double.toString(m_character.getGold()));
		updateTotalWeight();
		refreshItemsListView();	
	}

	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_inventory);
	}

	@Override
	public void updateDatabase() {
		if (m_character != null) {
			m_character.setGold(Double.parseDouble(m_goldEditText.getText().toString()));
			getCharacterRepo().update(m_character);
		}
	}

	@Override
	public void loadFromDatabase() {
		m_character = getCharacterRepo().query(getCurrentCharacterID());
	}
	
}
