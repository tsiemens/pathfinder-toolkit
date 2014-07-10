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
import com.lateensoft.pathfinder.toolkit.adapters.character.ArmorListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
import com.lateensoft.pathfinder.toolkit.model.character.Inventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterArmorFragment extends AbstractCharacterSheetFragment implements
    OnItemClickListener, OnClickListener {
    private static final String TAG = CharacterArmorFragment.class.getSimpleName();
    
    private ListView armorList;
    private int armorIndexSelectedForEdit;
    private Button addButton;
    
    private ArmorDAO armorDao;
    private Inventory inventory;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        armorDao = new ArmorDAO(getContext());
        inventory = new Inventory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRootView(inflater.inflate(R.layout.character_armor_fragment,
                container, false));
        
        addButton = (Button) getRootView().findViewById(R.id.buttonAddArmor);
        addButton.setOnClickListener(this);
        
        armorList = new ListView(container.getContext());
        armorList = (ListView) getRootView().findViewById(R.id.lvArmor);

        armorList.setOnItemClickListener(this);
        
        Log.v(TAG, "Finishing onCreateView");
        return getRootView();
    }
    
    private void refreshArmorListView() {
        List<Armor> armorList = inventory.getArmors();
        Collections.sort(armorList);

        ArmorListAdapter adapter = new ArmorListAdapter(getContext(),
                R.layout.character_armor_row, armorList);
        this.armorList.setAdapter(adapter);
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "Item clicked: " + position);
        armorIndexSelectedForEdit = position;
        showArmorEditor(inventory.getArmors().get(position));
    }

    public void onClick(View v) {
        Log.v(TAG, "Add button clicked");
        armorIndexSelectedForEdit = -1;
        showArmorEditor(null);
    }

    private void showArmorEditor(Armor armor) {
        Intent armorEditIntent = new Intent(getContext(),
                ArmorEditActivity.class);
        armorEditIntent.putExtra(
                ArmorEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, armor);
        startActivityForResult(armorEditIntent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }

        switch (resultCode) {
        case Activity.RESULT_OK:
            Armor armor = ParcelableEditorActivity.getParcelableFromIntent(data);
            if (armor != null) {
                if(armorIndexSelectedForEdit < 0) {
                    Log.v(TAG, "Adding an armor");
                    armor.setCharacterID(getCurrentCharacterID());
                    try {
                        armorDao.add(getCurrentCharacterID(), armor);
                        inventory.getArmors().add(armor);
                        refreshArmorListView();
                    } catch (DataAccessException e) {
                        Log.e(TAG, "Failed to add armor", e);
                    }

                } else {
                    Log.v(TAG, "Editing an armor");
                    try {
                        armorDao.update(getCurrentCharacterID(), armor);
                        inventory.getArmors().set(armorIndexSelectedForEdit, armor);
                        refreshArmorListView();
                    } catch (DataAccessException e) {
                        Log.e(TAG, "Failed to update armor " + armor.getId(), e);
                    }
                }
            }

            break;
        
        case ArmorEditActivity.RESULT_DELETE:
            Log.v(TAG, "Deleting an armor");
            Armor armorToDelete = inventory.getArmors().get(armorIndexSelectedForEdit);
            if (armorToDelete != null) {
                try {
                    armorDao.remove(armorToDelete);
                    inventory.getArmors().remove(armorIndexSelectedForEdit);
                    refreshArmorListView();
                } catch (DataAccessException e) {
                    Log.e(TAG, "Failed to delete armor " + armorToDelete.getId(), e);
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
        refreshArmorListView();
    }
    
    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_armor);
    }

    @Override
    public void updateDatabase() {
        // Done dynamically
    }

    @Override
    public void loadFromDatabase() {
        inventory.getArmors().clear();
        inventory.getArmors().addAll(armorDao.findAllForOwner(getCurrentCharacterID()));
    }
}
