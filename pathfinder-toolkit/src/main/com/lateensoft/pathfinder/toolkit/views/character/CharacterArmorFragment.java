package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.ArmorListAdapter;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterArmorFragment extends IndexedParcelableListFragment<Armor, ArmorDAO> {
    private ListView armorList;
    private Button addButton;
    
    private ArmorDAO armorDao;
    private List<Armor> armors;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        armorDao = new ArmorDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRootView(inflater.inflate(R.layout.character_armor_fragment,
                container, false));
        
        addButton = (Button) getRootView().findViewById(R.id.buttonAddArmor);
        addButton.setOnClickListener(getAddButtonClickListener());
        
        armorList = new ListView(container.getContext());
        armorList = (ListView) getRootView().findViewById(R.id.lvArmor);

        armorList.setOnItemClickListener(getListItemClickListener());
        
        return getRootView();
    }

    @Override
    protected Class<? extends ParcelableEditorActivity> getParcelableEditorActivity() {
        return ArmorEditActivity.class;
    }

    @Override
    protected ArmorDAO getDAO() {
        return armorDao;
    }

    @Override
    protected List<Armor> getModel() {
        return armors;
    }

    @Override
    public void updateFragmentUI() {
        refreshArmorListView();
    }

    private void refreshArmorListView() {
        Collections.sort(armors);

        ArmorListAdapter adapter = new ArmorListAdapter(getContext(),
                R.layout.character_armor_row, armors);
        armorList.setAdapter(adapter);
    }

    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_armor);
    }

    @Override
    public void loadFromDatabase() {
        armors = armorDao.findAllForOwner(getCurrentCharacterID());
    }
}
