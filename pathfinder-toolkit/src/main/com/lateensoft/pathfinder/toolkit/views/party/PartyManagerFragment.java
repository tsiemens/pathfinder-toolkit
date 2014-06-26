package com.lateensoft.pathfinder.toolkit.views.party;

import java.util.Collections;
import java.util.List;

import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SimpleSelectableListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.*;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.MultiSelectActionModeCallback;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterCombatStatsFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtils;
import roboguice.RoboGuice;

public class PartyManagerFragment extends BasePageFragment {
	private static final String TAG = PartyManagerFragment.class.getSimpleName();
    private static final int GET_NEW_MEMBERS_CODE = 18880;
    private static final int GET_PARTY_CODE = 54716;

	public NamedList<IdStringPair> m_party;

	private EditText m_partyNameEditText;
	private ListView m_partyMemberList;
	
	private LitePartyRepository m_partyRepo = new LitePartyRepository();

    private ActionMode m_actionMode;
    private ActionModeCallback m_actionModeCallback;

    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_party_manager,
				container, false));

		m_partyNameEditText = (EditText) getRootView()
				.findViewById(R.id.editTextPartyName);

		m_partyMemberList = (ListView) getRootView()
				.findViewById(R.id.listViewPartyMembers);
		m_partyMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_actionModeCallback == null) {
                    showMemberDetails(m_party.get(position).getId());
                } else {
                    m_actionModeCallback.toggleListItemSelection(position);
                }
            }
        });
        m_partyMemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_actionMode != null) {
                    return false;
                }

                m_actionModeCallback = new ActionModeCallback();
                m_actionMode = getActivity().startActionMode(m_actionModeCallback);
                m_actionModeCallback.selectListItem(position, true);
                return true;
            }
        });

		loadCurrentParty();
		return getRootView();
	}
	
	@Override
	public void onPause() {
		updateDatabase();
		super.onPause();
	}

    @Override
    public void updateTitle() {
        setTitle(R.string.title_activity_party_manager);
        setSubtitle(null);
    }

    private void showMemberDetails(long characterId) {
        preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, characterId);
        switchToPage(CharacterCombatStatsFragment.class);
    }

	/**
	 * Load the currently set party in shared prefs If there is no party set in
	 * user prefs, it automatically generates a new one.
	 */
	private void loadCurrentParty() {
		long currentPartyID = getSelectedPartyId();

		if (currentPartyID == -1) {
			// There was no current party set in shared prefs
			addNewPartyAndSetSelected();
		} else {
			m_party = m_partyRepo.query(currentPartyID);
			if (m_party == null) {
				handleInvalidSelectedParty();
			}
			refreshPartyView();

		}
	}

    private void setSelectedParty(long partyId) {
        preferences.put(GlobalPrefs.SELECTED_PARTY_ID, partyId);
    }

    private long getSelectedPartyId() {
        return preferences.get(GlobalPrefs.SELECTED_PARTY_ID, -1L);
    }

    private void handleInvalidSelectedParty() {
        List<IdStringPair> ids = m_partyRepo.queryIdNameList();
        for (IdStringPair id : ids) {
            m_party = m_partyRepo.query(id.getId());
            if (m_party != null) {
                setSelectedParty(m_party.getId());
                break;
            }
        }
        if (m_party == null) {
            addNewPartyAndSetSelected();
        }
    }

    private class ActionModeCallback extends MultiSelectActionModeCallback {

        public ActionModeCallback() {
            super(R.menu.membership_action_mode_menu);
        }

        @Override
        public ListView getListView() {
            return m_partyMemberList;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                showRemoveCharactersFromPartyDialog(getSelectedItems(m_party));
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            super.onDestroyActionMode(mode);
            m_actionMode = null;
            m_actionModeCallback = null;
        }
    }

    private void showRemoveCharactersFromPartyDialog(final List<IdStringPair> membersToRemove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.membership_remove_dialog_title)
                .setMessage(String.format(getString(R.string.party_remove_members_dialog_msg),
                        m_party.size()));

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    removeMembersFromParty(membersToRemove);
                }
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    private void removeMembersFromParty(List<IdStringPair> membersToRemove) {
        List<Long> memberIds = Lists.newArrayList();
        for (IdStringPair pair : membersToRemove) {
            memberIds.add(pair.getId());
        }

        int numberOfDeletions = m_partyRepo.removeCharactersFromParty(m_party.getId(), memberIds);
        if (numberOfDeletions > 0) {
            m_party.removeAll(membersToRemove);
        }
        refreshPartyView();
    }

	private void addNewPartyAndSetSelected() {
		m_party = new NamedList<IdStringPair>("New Party");
		m_partyRepo.insert(m_party);
		setSelectedParty(m_party.getId());
		refreshPartyView();
	}

	private void deleteCurrentPartyAndSelectAnother() {
        m_partyRepo.delete(m_party);
		List<IdStringPair> partyIDs = m_partyRepo.queryIdNameList();

		if (partyIDs.isEmpty()) {
			addNewPartyAndSetSelected();
		} else {
			setSelectedParty(partyIDs.get(0).getId());
			loadCurrentParty();
		}
	}

	private void updateDatabase() {
        Editable text = m_partyNameEditText.getText();
		m_party.setName(text != null ? text.toString() : "");
		m_partyRepo.update(m_party);
	}

	private void refreshPartyView() {
        if (m_actionMode != null) {
            m_actionMode.finish();
            m_actionModeCallback = null;
            m_actionMode = null;
        }

        Collections.sort(m_party);
		m_partyNameEditText.setText(m_party.getName());
        SimpleSelectableListAdapter<IdStringPair> adapter = new SimpleSelectableListAdapter<IdStringPair>(
                getContext(), m_party,
                new SimpleSelectableListAdapter.DisplayStringGetter<IdStringPair>() {
                    @Override public String getDisplayString(IdStringPair object) {
                        return object.getValue();
                    }
                });
        adapter.setItemSelectionGetter(new SimpleSelectableListAdapter.ItemSelectionGetter() {
            @Override public boolean isItemSelected(int position) {
                if (m_actionModeCallback != null) {
                    return m_actionModeCallback.isListItemSelected(position);
                } else {
                    return false;
                }
            }
        });
		m_partyMemberList.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mi_party_list:
			showPartyPicker();
			break;
		case R.id.mi_new_member:
			showMemberPicker();
			break;
		case R.id.mi_new_party:
			showCreateNewPartyDialog();
			break;
		case R.id.mi_delete_party:
            showDeletePartyDialog();
			break;
		}

		super.onOptionsItemSelected(item);
		return true;

	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.party_manager_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showPartyPicker() {
        List<IdStringPair> parties = m_partyRepo.queryIdNameList();
        parties.remove(new IdStringPair(m_party.getId(), m_party.getName()));
        PickerUtils.Builder builder = new PickerUtils.Builder(getActivity());
        builder.setTitle(R.string.single_party_picker_title)
                .setSingleChoice(true)
                .setPickableParties(parties);
        Intent pickerIntent = builder.build();
        startActivityForResult(pickerIntent, GET_PARTY_CODE);
    }

    private void showMemberPicker() {
        CharacterRepository charRepo = new CharacterRepository();
        List<IdStringPair> characters = charRepo.queryIdNameList();;
        characters.removeAll(m_party);
        PickerUtils.Builder builder = new PickerUtils.Builder(getActivity());
        builder.setTitle(R.string.select_members_picker_title)
                .setSingleChoice(false)
                .setPickableCharacters(characters);
        Intent pickerIntent = builder.build();
        startActivityForResult(pickerIntent, GET_NEW_MEMBERS_CODE);
    }

    private void showCreateNewPartyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.new_party_dialog_text))
                .setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        updateDatabase();
                        addNewPartyAndSetSelected();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.create().show();
    }

    private void showDeletePartyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(String.format(getString(R.string.confirm_delete_item_message),
                m_party.getName()))
                .setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentPartyAndSelectAnother();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.create().show();
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PARTY_CODE || requestCode == GET_NEW_MEMBERS_CODE) {
            PickerUtils.ResultData pickerData = new PickerUtils.ResultData(data);
            if (requestCode == GET_PARTY_CODE) {
                IdStringPair party = pickerData.getParty();
                if (party != null) {
                    setSelectedParty(party.getId());
                    loadCurrentParty();
                }
            } else {
                List<IdStringPair> membersToAdd = pickerData.getCharacters();
                if (membersToAdd != null) {
                    PartyMembershipRepository membersRepo = m_partyRepo.getMembersRepo();
                    for (IdStringPair member : membersToAdd) {
                        long id = membersRepo.insert(new PartyMembershipRepository.Membership(m_party.getId(), member.getId()));
                        if (id >= 0) {
                            m_party.add(member);
                        } else {
                            Log.e(TAG, "Database returned " + id + " while adding character "
                                    + member + " to " + m_party.getId());
                        }
                    }
                    refreshPartyView();
                }
            }
        }

		super.onActivityResult(requestCode, resultCode, data);
	}
}
