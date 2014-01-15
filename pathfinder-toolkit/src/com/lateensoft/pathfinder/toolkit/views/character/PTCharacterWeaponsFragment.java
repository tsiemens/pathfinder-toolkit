package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTWeaponAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTWeaponRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;

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

	private ListView m_listView;
	private int m_weaponSelectedForEdit;
	private Button m_addButton;
	
	private PTWeaponRepository m_weaponRepo;
	private PTInventory m_inventory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_weaponRepo = new PTWeaponRepository();
		m_inventory = new PTInventory();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Starting onCreateView");
		
		setRootView(inflater.inflate(R.layout.character_weapon_fragment,
				container, false));
		
		m_addButton = (Button) getRootView().findViewById(R.id.buttonAddWeapon);
		m_addButton.setOnClickListener(this);
		
		m_listView = new ListView(container.getContext());
		m_listView = (ListView) getRootView().findViewById(R.id.listViewWeapons);

		m_listView.setOnItemClickListener(this);
		
		return getRootView();
	}

	private void refreshWeaponsListView() {
		PTWeaponAdapter adapter = new PTWeaponAdapter(getActivity(),
				R.layout.character_weapon_row,
				m_inventory.getWeaponArray());
		
		Log.d(TAG, "Called refreshWeaponsListView");
		
		m_listView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		m_weaponSelectedForEdit = position;
		//showWeaponDialog(mCharacter.getInventory().getWeapon(position));
		showWeaponEditor(m_inventory.getWeapon(position));
	}



	public void onClick(View v) {
		m_weaponSelectedForEdit = -1;
		//showWeaponDialog(null);
		showWeaponEditor(null);
	}
	
	private void showWeaponEditor(PTWeapon weapon) {
		Intent weaponEditIntent = new Intent(getActivity(),
				PTCharacterWeaponEditActivity.class);
		weaponEditIntent.putExtra(
				PTCharacterWeaponEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, weapon);
		startActivityForResult(weaponEditIntent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTWeapon weapon = data.getExtras().getParcelable(
					PTCharacterWeaponEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE);
			Log.i(TAG, "Add.edit weapon OK: " + weapon.getName());
			if(m_weaponSelectedForEdit < 0) {
				Log.v(TAG, "Adding a weapon");
				if(weapon != null) {
					weapon.setCharacterID(getCurrentCharacterID());
					if (m_weaponRepo.insert(weapon) != -1) {
						m_inventory.addWeapon(weapon);
						refreshWeaponsListView();
					}
				}
			} else {
				Log.v(TAG, "Editing a weapon");
				if (m_weaponRepo.update(weapon) != 0) {
					m_inventory.setWeapon(weapon, m_weaponSelectedForEdit);
					refreshWeaponsListView();
				}
			}
			
			break;
		
		case PTCharacterWeaponEditActivity.RESULT_DELETE:
			Log.i(TAG, "Deleting a weapon");
			PTWeapon weaponToDelete = m_inventory.getWeapon(m_weaponSelectedForEdit);
			if (weaponToDelete != null && m_weaponRepo.delete(weaponToDelete) != 0) {
				m_inventory.deleteWeapon(m_weaponSelectedForEdit);
				refreshWeaponsListView();
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

	@Override
	public void updateFragmentUI() {
		refreshWeaponsListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_weapons);
	}

	@Override
	public void updateDatabase() {
		// Done dynamically
	}

	@Override
	public void loadFromDatabase() {
		m_inventory.setWeapons(m_weaponRepo.querySet(getCurrentCharacterID()));
	}
}
