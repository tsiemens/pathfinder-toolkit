package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.FluffListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.FluffInfoRepository;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;

import java.util.List;

public class CharacterFluffFragment extends AbstractCharacterSheetFragment {
    @SuppressWarnings("unused")
    private static final String TAG = CharacterFluffFragment.class.getSimpleName();
    private ListView fluffList;
    private int fluffIndexSelectedForEdit;
    
    private FluffInfoRepository fluffRepo;
    private CharacterRepository characterRepo;
    private FluffInfo fluffModel;

    private String[] fluffNames;
    private List<String> fluffValues;
    
    private EditText editDialogField;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterRepo = new CharacterRepository();
        fluffRepo = new FluffInfoRepository();
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
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //Set up dialog layout
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View dialogView = inflater.inflate(R.layout.character_fluff_dialog, null);
        editDialogField = (EditText) dialogView.findViewById(R.id.dialogFluffText);

        builder.setTitle(fluffNames[fluffIndex]);
        editDialogField.append(fluffValues.get(fluffIndex));

        DialogInterface.OnClickListener listener = new EditDialogClickListener();
        builder.setView(dialogView)
                .setPositiveButton(R.string.ok_button_text, listener)
                .setNegativeButton(R.string.cancel_button_text, listener);
        
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class EditDialogClickListener implements DialogInterface.OnClickListener {

        @Override public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                fluffValues.set(fluffIndexSelectedForEdit, getFluffValueFromDialog());
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

    private String getFluffValueFromDialog() {
        return editDialogField.getText().toString();
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
            FluffInfo fluff = buildModelFromFluffValues();
            characterRepo.updateName(getCurrentCharacterID(), getCharacterName());
            fluffRepo.update(fluff);
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
        FluffInfo fluff = fluffRepo.query(getCurrentCharacterID());
        String name = characterRepo.queryName(getCurrentCharacterID());
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
