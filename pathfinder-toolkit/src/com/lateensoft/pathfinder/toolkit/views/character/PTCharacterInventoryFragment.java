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
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class PTCharacterInventoryFragment extends PTCharacterSheetFragment {
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
		m_addButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                m_itemIndexSelectedForEdit = -1;
                showItemEditor(null);
            }
        });
		
		m_goldEditText = (EditText) getRootView().findViewById(R.id.editTextGold);
		m_goldEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    updateGoldFromInput();
                }
            }
        });
		
		m_totalWeightText = (TextView)  getRootView().findViewById(R.id.tvWeightTotal);
		
		m_itemsListView = (ListView) getRootView().findViewById(R.id.listViewInventory);
		m_itemsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_itemIndexSelectedForEdit = position;
                showItemEditor(m_character.getInventory().getItems().get(position));
            }
        });
		
		return getRootView();
	}

	private void refreshItemsListView(){
		List<PTItem> items = m_character.getInventory().getItems();
        Collections.sort(items);
	
		PTInventoryAdapter adapter = new PTInventoryAdapter(getContext(), R.layout.character_inventory_row, items);
		m_itemsListView.setAdapter(adapter);
	
	}
	
	private void updateTotalWeight(){
		double totalWeight = m_character.getInventory().getTotalWeight();
		
		m_totalWeightText.setText(getContext().getString(R.string.inventory_total_weight_header)
				+" "+ totalWeight);
	}
	
	private void showItemEditor(PTItem item) {
		Intent itemEditIntent = new Intent(getContext(),
				PTCharacterInventoryEditActivity.class);
		itemEditIntent.putExtra(
				PTCharacterInventoryEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, item);
		startActivityForResult(itemEditIntent, PTParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PTParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTItem item = PTParcelableEditorActivity.getParcelableFromIntent(data);
            if (item != null) {
                if(m_itemIndexSelectedForEdit < 0) {
                    Log.i(TAG, "Adding item "+item.getName());
                    item.setCharacterID(getCurrentCharacterID());
                    if (m_itemRepo.insert(item) != -1) {
                        m_character.getInventory().getItems().add(item);
                        refreshItemsListView();
                        updateTotalWeight();
                    }
                } else {
                    Log.v(TAG, "Editing an item "+item.getName());
                    if (m_itemRepo.update(item) != 0) {
                        m_character.getInventory().getItems().set(m_itemIndexSelectedForEdit, item);
                        refreshItemsListView();
                        updateTotalWeight();
                    }
                }
            }

            break;
		
		case PTCharacterInventoryEditActivity.RESULT_DELETE:
			PTItem itemToDelete = m_character.getInventory().getItems().get(m_itemIndexSelectedForEdit);
			Log.i(TAG, "Deleting item "+itemToDelete.getName());
			if(itemToDelete != null && m_itemRepo.delete(itemToDelete) != 0 ) {
				m_character.getInventory().getItems().remove(m_itemIndexSelectedForEdit);
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

    private void updateGoldFromInput() {
        try {
            m_character.setGold(Double.parseDouble(m_goldEditText.getText().toString()));
        } catch (NumberFormatException e) {
            // Invalid input for gold. Keep old value, to be safe.
            m_goldEditText.setText(Double.toString(m_character.getGold()));
        }
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
            updateGoldFromInput();
			getCharacterRepo().update(m_character);
		}
	}

	@Override
	public void loadFromDatabase() {
		m_character = getCharacterRepo().query(getCurrentCharacterID());
	}
	
}
