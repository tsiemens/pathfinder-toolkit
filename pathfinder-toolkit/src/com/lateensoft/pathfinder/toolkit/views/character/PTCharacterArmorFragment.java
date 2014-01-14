package com.lateensoft.pathfinder.toolkit.views.character;

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
import com.lateensoft.pathfinder.toolkit.adapters.character.PTArmorAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTArmorRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;

public class PTCharacterArmorFragment extends PTCharacterSheetFragment implements
	OnItemClickListener, OnClickListener {
	private static final String TAG = PTCharacterArmorFragment.class.getSimpleName();
	
	private ListView m_listView;
	private int m_armorSelectedForEdit;
	private Button m_addButton;
	
	private PTArmorRepository m_armorRepo;
	private PTInventory m_inventory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_armorRepo = new PTArmorRepository();
		m_inventory = new PTInventory();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_armor_fragment,
				container, false));
		
		m_addButton = (Button) getRootView().findViewById(R.id.buttonAddArmor);
		m_addButton.setOnClickListener(this);
		
		m_listView = new ListView(container.getContext());
		m_listView = (ListView) getRootView().findViewById(R.id.lvArmor);
		refreshArmorListView();

		m_listView.setOnItemClickListener(this);
		
		Log.v(TAG, "Finishing onCreateView");
		return getRootView();
	}
	
	private void refreshArmorListView() {
		PTArmorAdapter adapter = new PTArmorAdapter(getActivity(),
				R.layout.character_armor_row,
				m_inventory.getArmorArray());
		for (PTArmor a : m_inventory.getArmorArray())
		Log.d(TAG, ""+a);
		
		Log.d(TAG, "Called refreshArmorListView");
		
		m_listView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		m_armorSelectedForEdit = position;
		showArmorEditor(m_inventory.getArmor(position));
	}

	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		m_armorSelectedForEdit = -1;
		showArmorEditor(null);
	}

	private void showArmorEditor(PTArmor armor) {
		Intent armorEditIntent = new Intent(getActivity(),
				PTCharacterArmorEditActivity.class);
		armorEditIntent.putExtra(
				PTCharacterArmorEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, armor);
		startActivityForResult(armorEditIntent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTArmor armor = data.getExtras().getParcelable(
					PTCharacterArmorEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE);
			Log.v(TAG, "Add.edit armor OK: " + armor.getName());
			if(m_armorSelectedForEdit < 0) {
				Log.v(TAG, "Adding an armor");
				if(armor != null) {
					m_inventory.addArmor(armor);
					refreshArmorListView(); 
				}
			} else {
				Log.v(TAG, "Editing an armor");
				m_inventory.setArmor(armor, m_armorSelectedForEdit);
				refreshArmorListView();
			}
			
			break;
		
		case PTCharacterArmorEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting an armor");
			m_inventory.deleteArmor(m_armorSelectedForEdit);
			refreshArmorListView();
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateDatabase();
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

	@Override
	public void updateDatabase() {
		// TODO optimize to update when needed
		PTArmor[] armors = m_inventory.getArmorArray();
		for (PTArmor armor : armors) {
			m_armorRepo.update(armor);
		}
	}

	@Override
	public void loadFromDatabase() {
		m_inventory.setArmor(m_armorRepo.querySet(getCurrentCharacterID()));
	}
}
