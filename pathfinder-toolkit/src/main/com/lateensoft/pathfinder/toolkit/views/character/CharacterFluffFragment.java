package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.FluffListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterNameDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.FluffDAO;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import com.lateensoft.pathfinder.toolkit.views.SimpleValueEditorDialog;

import java.util.List;

public class CharacterFluffFragment extends AbstractCharacterSheetFragment {
    @SuppressWarnings("unused")
    private static final String TAG = CharacterFluffFragment.class.getSimpleName();
    private ListView fluffList;
    private int fluffIndexSelectedForEdit;

    private CharacterNameDAO characterNameDao;
    private FluffDAO fluffDao;

    private String[] fluffNames;
    private List<String> fluffValues;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterNameDao = new CharacterNameDAO(getContext());
        fluffDao = new FluffDAO(getContext());
        fluffNames = getResources().getStringArray(R.array.fluff_fields);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRootView(inflater.inflate(R.layout.character_fluff_fragment, 
                container, false));
        
        fluffList = (ListView) getRootView().findViewById(R.id.fluff_list);
        fluffList.setOnItemClickListener(listItemClickListener);
        
        return getRootView();        
    }

    private OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            fluffIndexSelectedForEdit = position;
            showItemDialog(position);
        }
    };
    
    private void showItemDialog(int fluffIndex) {
        SimpleValueEditorDialog.builder(getContext())
                .forType(SimpleValueEditorDialog.ValueType.TEXT_MULTILINE)
                .withHint(R.string.fluff_hint)
                .withTitle(fluffNames[fluffIndex])
                .withInitialValue(fluffValues.get(fluffIndex))
                .withOnFinishedListener(new EditDialogListener())
                .build()
                .show();
    }

    private class EditDialogListener implements SimpleValueEditorDialog.OnEditingFinishedListener {

        @Override public void onEditingFinished(boolean okWasPressed, Editable editable) {
            if (okWasPressed) {
                fluffValues.set(fluffIndexSelectedForEdit, editable.toString());
                updateDatabase();
                refreshFluffListView();
            }
            hideKeyboardDelayed(0);
        }
    }

    private void refreshFluffListView() {
        if (fluffValues != null) {
            FluffListAdapter adapter = new FluffListAdapter(getContext(),
                    R.layout.character_fluff_row, fluffNames,
                    fluffValues);
            fluffList.setAdapter(adapter);
            setTitle(getCharacterName());
        }
    }

    private String getCharacterName() {
        return fluffValues.get(0);
    }

    @Override
    public void updateFragmentUI() {
        refreshFluffListView();
    }
    
    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_fluff);
    }

    @Override
    public void updateDatabase() {
        if (fluffValues != null) {
            long currentCharacterId = getCurrentCharacterID();
            try {
                characterNameDao.update(new IdNamePair(currentCharacterId, getCharacterName()));
                FluffInfo fluff = buildModelFromFluffValues();
                fluffDao.update(currentCharacterId, fluff);
            } catch (DataAccessException e) {
                Log.e(TAG, "Failed to update character " + currentCharacterId);
            }
        }
    }

    private FluffInfo buildModelFromFluffValues() {
        FluffInfo fluff = new FluffInfo();
        fluff.setAlignment(fluffValues.get(1));
        fluff.setXP(fluffValues.get(2));
        fluff.setNextLevelXP(fluffValues.get(3));
        fluff.setPlayerClass(fluffValues.get(4));
        fluff.setRace(fluffValues.get(5));
        fluff.setDeity(fluffValues.get(6));
        fluff.setLevel(fluffValues.get(7));
        fluff.setSize(fluffValues.get(8));
        fluff.setGender(fluffValues.get(9));
        fluff.setHeight(fluffValues.get(10));
        fluff.setWeight(fluffValues.get(11));
        fluff.setEyes(fluffValues.get(12));
        fluff.setHair(fluffValues.get(13));
        fluff.setLanguages(fluffValues.get(14));
        fluff.setDescription(fluffValues.get(15));
        return fluff;
    }

    @Override
    public void loadFromDatabase() {
        FluffInfo fluff = fluffDao.find(getCurrentCharacterID());
        String name = characterNameDao.find(getCurrentCharacterID()).getName();
        setFluffFields(name, fluff);
    }

    private void setFluffFields(String name, FluffInfo fluff) {
        if (fluffValues == null) {
            fluffValues = Lists.newArrayListWithCapacity(fluffNames.length);
            for (String fluffName : fluffNames) {
                fluffValues.add(null);
            }
        }

        fluffValues.set(0, name);
        fluffValues.set(1, fluff.getAlignment());
        fluffValues.set(2, fluff.getXP());
        fluffValues.set(3, fluff.getNextLevelXP());
        fluffValues.set(4, fluff.getPlayerClass());
        fluffValues.set(5, fluff.getRace());
        fluffValues.set(6, fluff.getDeity());
        fluffValues.set(7, fluff.getLevel());
        fluffValues.set(8, fluff.getSize());
        fluffValues.set(9, fluff.getGender());
        fluffValues.set(10, fluff.getHeight());
        fluffValues.set(11, fluff.getWeight());
        fluffValues.set(12, fluff.getEyes());
        fluffValues.set(13, fluff.getHair());
        fluffValues.set(14, fluff.getLanguages());
        fluffValues.set(15, fluff.getDescription());
    }
}
