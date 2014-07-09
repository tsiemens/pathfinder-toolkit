package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.WeaponListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.WeaponDAO;
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

    private ListView weaponList;
    private int weaponIndexSelectedForEdit;
    private Button addButton;
    
    private WeaponDAO weaponDao;
    private Inventory inventory;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weaponDao = new WeaponDAO(getContext());
        inventory = new Inventory();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting onCreateView");
        
        setRootView(inflater.inflate(R.layout.character_weapon_fragment,
                container, false));
        
        addButton = (Button) getRootView().findViewById(R.id.buttonAddWeapon);
        addButton.setOnClickListener(this);
        
        weaponList = new ListView(container.getContext());
        weaponList = (ListView) getRootView().findViewById(R.id.listViewWeapons);

        weaponList.setOnItemClickListener(this);
        
        return getRootView();
    }

    private void refreshWeaponsListView() {
        List<Weapon> weapons = inventory.getWeapons();
        Collections.sort(weapons);

        WeaponListAdapter adapter = new WeaponListAdapter(getContext(),
                R.layout.character_weapon_row, weapons);
        weaponList.setAdapter(adapter);
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "Item clicked: " + position);
        weaponIndexSelectedForEdit = position;
        //showWeaponDialog(mCharacter.getInventory().getWeapon(position));
        showWeaponEditor(inventory.getWeapons().get(position));
    }



    public void onClick(View v) {
        weaponIndexSelectedForEdit = -1;
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
                if(weaponIndexSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a weapon");
                    weapon.setCharacterID(getCurrentCharacterID());
                    try {
                        weaponDao.add(getCurrentCharacterID(), weapon);
                        inventory.getWeapons().add(weapon);
                        refreshWeaponsListView();
                    } catch (DataAccessException e) {
                        Log.e(TAG, "Failed to add weapon", e);
                    }
                } else {
                    Log.v(TAG, "Editing a weapon");
                    try {
                        weaponDao.update(getCurrentCharacterID(), weapon);
                        inventory.getWeapons().set(weaponIndexSelectedForEdit, weapon);
                        refreshWeaponsListView();
                    } catch (DataAccessException e) {
                        Log.e(TAG, "Failed to update weapon id=" + weapon.getId(), e);
                    }

                }
            }

            break;
        
        case WeaponEditActivity.RESULT_DELETE:
            Log.i(TAG, "Deleting a weapon");
            Weapon weaponToDelete = inventory.getWeapons().get(weaponIndexSelectedForEdit);
            if (weaponToDelete != null) {
                try {
                    weaponDao.remove(weaponToDelete);
                    inventory.getWeapons().remove(weaponIndexSelectedForEdit);
                    refreshWeaponsListView();
                } catch (DataAccessException e) {
                    Log.e(TAG, "Failed to delete weapon id=" + weaponToDelete.getId(), e);
                }
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
        inventory.getWeapons().clear();
        inventory.getWeapons().addAll(weaponDao.findAllForOwner(getCurrentCharacterID()));
    }
}
