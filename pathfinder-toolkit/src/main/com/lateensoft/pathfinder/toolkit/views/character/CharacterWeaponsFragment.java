package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.WeaponListAdapter;
import com.lateensoft.pathfinder.toolkit.db.dao.table.WeaponDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterWeaponsFragment extends IndexedParcelableListFragment<Weapon, WeaponDAO> {

    private ListView weaponList;
    private Button addButton;
    
    private WeaponDAO weaponDao;
    private List<Weapon> weapons;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weaponDao = new WeaponDAO(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRootView(inflater.inflate(R.layout.character_weapon_fragment,
                container, false));
        
        addButton = (Button) getRootView().findViewById(R.id.buttonAddWeapon);
        addButton.setOnClickListener(getAddButtonClickListener());
        
        weaponList = new ListView(container.getContext());
        weaponList = (ListView) getRootView().findViewById(R.id.listViewWeapons);

        weaponList.setOnItemClickListener(getListItemClickListener());
        
        return getRootView();
    }

    @Override
    protected List<Weapon> getModel() {
        return weapons;
    }

    @Override
    protected Class<? extends ParcelableEditorActivity> getParcelableEditorActivity() {
        return WeaponEditActivity.class;
    }

    @Override
    protected WeaponDAO getDAO() {
        return weaponDao;
    }

    @Override
    public void updateFragmentUI() {
        refreshWeaponsListView();
    }

    private void refreshWeaponsListView() {
        Collections.sort(weapons);

        WeaponListAdapter adapter = new WeaponListAdapter(getContext(),
                R.layout.character_weapon_row, weapons);
        weaponList.setAdapter(adapter);
    }

    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_weapons);
    }

    @Override
    public void loadFromDatabase() {
        weapons = weaponDao.findAllForOwner(getCurrentCharacterID());
    }
}