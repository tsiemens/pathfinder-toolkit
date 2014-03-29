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
import com.lateensoft.pathfinder.toolkit.db.repository.ArmorRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Inventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterArmorFragment extends AbstractCharacterSheetFragment implements
	OnItemClickListener, OnClickListener {
	private static final String TAG = CharacterArmorFragment.class.getSimpleName();
	
	private ListView m_listView;
	private int m_armorSelectedForEdit;
	private Button m_addButton;
	
	private ArmorRepository m_armorRepo;
	private Inventory m_inventory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_armorRepo = new ArmorRepository();
		m_inventory = new Inventory();
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

		m_listView.setOnItemClickListener(this);
		
		Log.v(TAG, "Finishing onCreateView");
		return getRootView();
	}
	
	private void refreshArmorListView() {
        List<Armor> armorList = m_inventory.getArmors();
        Collections.sort(armorList);

		ArmorListAdapter adapter = new ArmorListAdapter(getContext(),
				R.layout.character_armor_row, armorList);
		m_listView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		m_armorSelectedForEdit = position;
		showArmorEditor(m_inventory.getArmors().get(position));
	}

	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		m_armorSelectedForEdit = -1;
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
                if(m_armorSelectedForEdit < 0) {
                    Log.v(TAG, "Adding an armor");
                    armor.setCharacterID(getCurrentCharacterID());
                    if (m_armorRepo.insert(armor) != -1) {
                        m_inventory.getArmors().add(armor);
                        refreshArmorListView();
                    }
                } else {
                    Log.v(TAG, "Editing an armor");
                    if (m_armorRepo.update(armor) != 0 ){
                        m_inventory.getArmors().set(m_armorSelectedForEdit, armor);
                        refreshArmorListView();
                    }
                }
            }

            break;
		
		case ArmorEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting an armor");
			Armor armorToDelete = m_inventory.getArmors().get(m_armorSelectedForEdit);
			if (armorToDelete != null && m_armorRepo.delete(armorToDelete) != 0) {
				m_inventory.getArmors().remove(m_armorSelectedForEdit);
				refreshArmorListView();
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
		m_inventory.getArmors().clear();
        m_inventory.getArmors().addAll(m_armorRepo.querySet(getCurrentCharacterID()));
	}
}
