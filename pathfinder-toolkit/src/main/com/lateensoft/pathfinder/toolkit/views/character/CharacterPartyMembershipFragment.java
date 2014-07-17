package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SimpleSelectableListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.PartyDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.PartyMemberIdDAO;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.views.MultiSelectActionModeController;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtil;

import java.util.Collections;
import java.util.List;

public class CharacterPartyMembershipFragment extends AbstractCharacterSheetFragment {

    private static final String TAG = CharacterPartyMembershipFragment.class.getSimpleName();
    public static final int GET_NEW_PARTIES_CODE = 33091;

    private ListView partyListView;
    private Button addButton;

    private PartyDAO partyDao;
    private PartyMemberIdDAO partyMemberDao;
    private List<IdNamePair> parties;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyDao = new PartyDAO(getContext());
        partyMemberDao = new PartyMemberIdDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRootView(inflater.inflate(R.layout.character_membership_fragment,
                container, false));

        addButton = (Button) getRootView().findViewById(R.id.button_add);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPartyPicker();
            }
        });

        partyListView = (ListView) getRootView()
                .findViewById(R.id.lv_parties);
        partyListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        partyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!actionModeController.isActionModeStarted()) {
                    actionModeController.startActionModeWithInitialSelection(position);
                    return true;
                }
                return false;
            }
        });
        partyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionModeController.isActionModeStarted()) {
                    actionModeController.toggleListItemSelection(position);
                }
            }
        });

        return getRootView();
    }

    private MultiSelectActionModeController actionModeController = new MultiSelectActionModeController() {
        @Override public Activity getActivity() {
            return CharacterPartyMembershipFragment.this.getActivity();
        }

        @Override public int getActionMenuResourceId() {
            return R.menu.remove_action_mode_menu;
        }

        @Override public ListView getListView() {
            return partyListView;
        }

        @Override public boolean onActionItemClicked(MultiSelectActionModeController controller, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                showRemoveCharacterFromPartyDialog(getSelectedItems(parties));
                controller.finishActionMode();
                return true;
            }
            return false;
        }
    };

    private void refreshPartiesListView() {
        if (actionModeController.isActionModeStarted()) {
            actionModeController.finishActionMode();
        }

        Collections.sort(parties);

        SimpleSelectableListAdapter adapter = new SimpleSelectableListAdapter<IdNamePair>(getActivity(),
                parties, new SimpleSelectableListAdapter.DisplayStringGetter<IdNamePair>() {
                    @Override public String getDisplayString(IdNamePair object) {
                        return object.getName();
                    }
                });
        adapter.setItemSelectionGetter(new SimpleSelectableListAdapter.ItemSelectionGetter() {
            @Override public boolean isItemSelected(int position) {
                return actionModeController.isListItemSelected(position);
            }
        });

        partyListView.setAdapter(adapter);
    }

    public void showRemoveCharacterFromPartyDialog(final List<IdNamePair> parties) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.membership_remove_dialog_title)
                .setMessage(String.format(getString(R.string.membership_remove_dialog_msg), parties.size()));

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    removeCharacterFromParties(parties);
                }
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    private void removeCharacterFromParties(List<IdNamePair> parties) {
        for (IdNamePair party : parties) {
            try {
                partyMemberDao.removeById(party.getId(), getCurrentCharacterID());
                this.parties.remove(party);
            } catch (DataAccessException e) {
                Log.e(TAG, "Could not remove character " + getCurrentCharacterID() +
                        " from party " + party.getId(), e);
            }
        }
        refreshPartiesListView();
    }

    public void showPartyPicker() {
        List<IdNamePair> parties = partyDao.findAll();
        parties.removeAll(this.parties);
        PickerUtil.Builder builder = new PickerUtil.Builder(getActivity());
        builder.setTitle(R.string.membership_party_picker_title)
                .setSingleChoice(false)
                .setPickableParties(parties);
        Intent pickerIntent = builder.build();
        startActivityForResult(pickerIntent, GET_NEW_PARTIES_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_NEW_PARTIES_CODE && resultCode == Activity.RESULT_OK &&
                data != null) {
            PickerUtil.ResultData resultData = new PickerUtil.ResultData(data);
            List<IdNamePair> partiesToAdd = resultData.getParties();
            if (partiesToAdd != null) {
                addCharacterToParties(partiesToAdd);
            }
        }
    }

    private void addCharacterToParties(List<IdNamePair> partiesToAdd) {
        for (IdNamePair party : partiesToAdd) {
            try {
                partyMemberDao.add(party.getId(), getCurrentCharacterID());
                parties.add(party);
            } catch (DataAccessException e) {
                Log.e(TAG, "Failed to add character " + getCurrentCharacterID() +
                        " to party " + party, e);
            }
        }
        refreshPartiesListView();
    }

    @Override
    public void updateFragmentUI() {
        refreshPartiesListView();
    }
    
    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_membership);
    }

    @Override
    public void updateDatabase() {
        // Done dynamically
    }

    @Override
    public void loadFromDatabase() {
        parties = partyDao.findAllWithMember(getCurrentCharacterID());
    }
}
