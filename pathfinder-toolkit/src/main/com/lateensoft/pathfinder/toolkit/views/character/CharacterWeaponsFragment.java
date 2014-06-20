package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.WeaponListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.WeaponRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Inventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

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
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterWeaponsFragment extends AbstractCharacterSheetFragment implements
OnClickListener, OnItemClickListener{
	private static final String TAG = CharacterWeaponsFragment.class.getSimpleName();

	private ListView m_listView;
	private int m_weaponSelectedForEdit;
	private Button m_addButton;
	
	private WeaponRepository m_weaponRepo;
	private Inventory m_inventory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_weaponRepo = new WeaponRepository();
		m_inventory = new Inventory();
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
        List<Weapon> weapons = m_inventory.getWeapons();
        Collections.sort(weapons);

		WeaponListAdapter adapter = new WeaponListAdapter(getContext(),
				R.layout.character_weapon_row, weapons);
		m_listView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		m_weaponSelectedForEdit = position;
		//showWeaponDialog(mCharacter.getInventory().getWeapon(position));
		showWeaponEditor(m_inventory.getWeapons().get(position));
	}



	public void onClick(View v) {
		m_weaponSelectedForEdit = -1;
		//showWeaponDialog(null);
		showWeaponEditor(null);
	}
	
	private void showWeaponEditor(Weapon weapon) {
		Intent weaponEditIntent = new Intent(getContext(),
				WeaponEditActivity.class);
		weaponEditIntent.putExtra(
				WeaponEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, weapon);
		startActivityForResult(weaponEditIntent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			Weapon weapon = ParcelableEditorActivity.getParcelableFromIntent(data);
            if (weapon != null) {
                if(m_weaponSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a weapon");
                    weapon.setCharacterID(getCurrentCharacterID());
                    if (m_weaponRepo.insert(weapon) != -1) {
                        m_inventory.getWeapons().add(weapon);
                        refreshWeaponsListView();
                    }
                } else {
                    Log.v(TAG, "Editing a weapon");
                    if (m_weaponRepo.update(weapon) != 0) {
                        m_inventory.getWeapons().set(m_weaponSelectedForEdit, weapon);
                        refreshWeaponsListView();
                    }
                }
            }

            break;
		
		case WeaponEditActivity.RESULT_DELETE:
			Log.i(TAG, "Deleting a weapon");
			Weapon weaponToDelete = m_inventory.getWeapons().get(m_weaponSelectedForEdit);
			if (weaponToDelete != null && m_weaponRepo.delete(weaponToDelete) != 0) {
				m_inventory.getWeapons().remove(m_weaponSelectedForEdit);
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
        m_inventory.getWeapons().clear();
		m_inventory.getWeapons().addAll(m_weaponRepo.querySet(getCurrentCharacterID()));
	}
}
