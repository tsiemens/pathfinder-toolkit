package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.InventoryItemListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ItemDAO;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterInventoryFragment extends IndexedParcelableListFragment<Item, ItemDAO> {
    private static final String TAG = CharacterInventoryFragment.class.getSimpleName();
    
    private ListView itemsList;
    private Button addButton;
    private EditText goldText;
    private TextView totalWeightLabel;
    
    private PathfinderCharacter character;
    private ItemDAO itemDao;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDao = new ItemDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        setRootView(inflater.inflate(R.layout.character_inventory_fragment, 
                container, false));
        
        addButton = (Button) getRootView().findViewById(R.id.buttonAddItem);
        addButton.setOnClickListener(getAddButtonClickListener());
        
        goldText = (EditText) getRootView().findViewById(R.id.editTextGold);
        goldText.setOnFocusChangeListener(goldTextFocusChangeListener);
        
        totalWeightLabel = (TextView)  getRootView().findViewById(R.id.tvWeightTotal);
        
        itemsList = (ListView) getRootView().findViewById(R.id.listViewInventory);
        itemsList.setOnItemClickListener(getListItemClickListener());
        
        return getRootView();
    }

    private OnFocusChangeListener goldTextFocusChangeListener = new OnFocusChangeListener() {
        @Override public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                updateGoldFromInput();
            }
        }
    };

    @Override
    protected List<Item> getModel() {
        return character.getInventory().getItems();
    }

    @Override
    protected Class<? extends ParcelableEditorActivity> getParcelableEditorActivity() {
        return InventoryItemEditActivity.class;
    }

    @Override
    protected ItemDAO getDAO() {
        return itemDao;
    }

    private void updateGoldFromInput() {
        try {
            character.setGold(Double.parseDouble(goldText.getText().toString()));
        } catch (NumberFormatException e) {
            // Invalid input for gold. Keep old value, to be safe.
            goldText.setText(Double.toString(character.getGold()));
        }
    }

    @Override
    public void updateFragmentUI() {
        goldText.setText(Double.toString(character.getGold()));
        updateTotalWeightLabel();
        refreshItemsListView();    
    }

    private void updateTotalWeightLabel(){
        double totalWeight = character.getInventory().getTotalWeight();

        totalWeightLabel.setText(getContext().getString(R.string.inventory_total_weight_header)
                + " " + totalWeight);
    }

    private void refreshItemsListView(){
        List<Item> items = character.getInventory().getItems();
        Collections.sort(items);

        InventoryItemListAdapter adapter = new InventoryItemListAdapter(getContext(), R.layout.character_inventory_row, items);
        itemsList.setAdapter(adapter);

    }

    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_inventory);
    }

    @Override
    public void updateDatabase() {
        if (character != null) {
            updateGoldFromInput();
            try {
                getCharacterModelDAO().update(character);
            } catch (DataAccessException e) {
                Log.e(TAG, "Failed to update gold for character " + character.getId(), e);
            }
        }
    }

    @Override
    public void loadFromDatabase() {
        character = getCharacterModelDAO().find(getCurrentCharacterID());
    }
    
}
