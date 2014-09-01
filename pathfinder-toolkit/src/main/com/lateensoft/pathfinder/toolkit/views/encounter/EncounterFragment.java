package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SelectableItemAdapter;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.MultiSelectActionModeController;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtil;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicArrayAdapter;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicListView;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.views.encounter.EncounterPresenter.EncounterStartMode;
import static com.lateensoft.pathfinder.toolkit.views.encounter.EncounterRoller.SkillCheckType;

public class EncounterFragment extends BasePageFragment {
    private static final int ADD_PARTICIPANTS_REQUEST_CODE = 2338;
    private static final int SELECT_ENCOUNTER_REQUEST_CODE = 8526;

    private EditText encounterNameEditor;
    private TextView lastSkillCheckName;
    private Button nextTurnButton;
    private DynamicListView participantList;

    private EncounterPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new EncounterPresenter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.fragment_encounters, container, false));
        initializeComponents();
        presenter.bind(this);
        return getRootView();
    }

    private void initializeComponents() {
        encounterNameEditor = (EditText) getRootView().findViewById(R.id.et_encounter_name);
        lastSkillCheckName = (TextView) getRootView().findViewById(R.id.tv_last_skill_check);
        nextTurnButton = (Button) getRootView().findViewById(R.id.button_next);
        participantList = (DynamicListView) getRootView().findViewById(R.id.listview);

        participantList.setOnItemClickListener(listItemClickListener);
        participantList.setOnItemLongClickListener(listItemLongClickListener);
        encounterNameEditor.addTextChangedListener(encounterNameEditListener);
        nextTurnButton.setOnClickListener(nextTurnClickListener);
    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (actionModeController.isActionModeStarted()) {
                actionModeController.toggleListItemSelection(position);
            } else {
                presenter.onParticipantSelected(presenter.getModel().get(position));
            }
        }
    };

    private AdapterView.OnItemLongClickListener listItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!actionModeController.isActionModeStarted()) {
                actionModeController.startActionModeWithInitialSelection(position);
                return true;
            }
            return false;
        }
    };

    private TextWatcher encounterNameEditListener = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override public void afterTextChanged(Editable s) {
            presenter.onEncounterNameEdited(s);
        }
    };

    private View.OnClickListener nextTurnClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            presenter.onNextTurnSelected();
        }
    };

    private MultiSelectActionModeController actionModeController = new MultiSelectActionModeController() {
        @Override public Activity getActivity() {
            return EncounterFragment.this.getActivity();
        }

        @Override public int getActionMenuResourceId() {
            return R.menu.remove_action_mode_menu;
        }

        @Override public ListView getListView() {
            return participantList;
        }

        @Override public boolean onActionItemClicked(MultiSelectActionModeController controller, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                showRemoveParticipantsFromEncounterDialog(getSelectedItems(presenter.getModel()));
                controller.finishActionMode();
                return true;
            }
            return false;
        }
    };

    private void showRemoveParticipantsFromEncounterDialog(final List<EncounterParticipantRowModel> participantsToRemove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.membership_remove_dialog_title)
                .setMessage(String.format(getString(R.string.remove_participants_from_encounter_msg),
                        participantsToRemove.size()));

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                presenter.onParticipantsSelectedForDeletion(participantsToRemove);
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    @Override
    public void onPause() {
        presenter.onViewLostFocus();
        super.onPause();
    }

    public void notifyModelChanged() {
        rebuildParticipantListContent();
        updateNonListUI();
    }

    private void rebuildParticipantListContent() {
        if (actionModeController.isActionModeStarted()) {
            actionModeController.finishActionMode();
        }

        EncounterParticipantListAdapter adapter = new EncounterParticipantListAdapter(getActivity(), presenter.getModel());
        participantList.setDynamicAdapter(adapter);
        adapter.setDragIconTouchListener(dragIconTouchListener);
        adapter.setRollsClickListener(rollsClickedListener);
        adapter.setOnItemsSwappedListener(itemsSwappedListener);
        adapter.setItemSelectionGetter(new SelectableItemAdapter.ItemSelectionGetter() {
            @Override public boolean isItemSelected(int position) {
                return actionModeController.isListItemSelected(position);
            }
        });
    }

    public void notifyModelAttributesChanged() {
        ((ArrayAdapter) participantList.getAdapter()).notifyDataSetChanged();
        updateNonListUI();
    }

    private void updateNonListUI() {
        updateTitle();

        EncounterViewModel model = presenter.getModel();
        encounterNameEditor.setText(model != null ? model.getName() : "");

        SkillCheckType lastSkillCheck = presenter.getLastSkillCheck();
        lastSkillCheckName.setText(lastSkillCheck != null ?
                getString(lastSkillCheck.getDisplayNameResId()) : "-");

        nextTurnButton.setEnabled(presenter.isEncounterOngoing());
        getActivity().invalidateOptionsMenu();
    }

    private EncounterParticipantListAdapter.RowTouchListener dragIconTouchListener = new EncounterParticipantListAdapter.RowTouchListener() {
        @Override public void onTouch(View v, MotionEvent event, int position) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && participantList.canHoverRows()
                    && !actionModeController.isActionModeStarted()) {
                participantList.hoverRow(position);
            }
        }
    };

    private EncounterParticipantListAdapter.RowComponentClickListener rollsClickedListener =
            new EncounterParticipantListAdapter.RowComponentClickListener() {
        @Override public void onClick(View v, int position) {
            new InitiativeEditorDialog(presenter.getModel().get(position)).show();
        }
    };

    private class InitiativeEditorDialog {
        private AlertDialog dialog;
        private EditText initiativeEditor;
        private EncounterParticipantRowModel row;

        public InitiativeEditorDialog(EncounterParticipantRowModel row) {
            this.row = row;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());

            View dialogView = inflater.inflate(R.layout.edit_value_dialog, null);
            initiativeEditor = (EditText) dialogView.findViewById(R.id.editor);
            initiativeEditor.setHint(R.string.initiative_editor_hint);

            builder.setTitle(R.string.initiative_editor_title);
            initiativeEditor.append(Integer.toString(row.getParticipant().getInitiativeScore()));

            builder.setView(dialogView)
                    .setPositiveButton(R.string.ok_button_text, okListener)
                    .setNegativeButton(R.string.cancel_button_text, null);
            dialog = builder.create();
        }

        private DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                presenter.onInitiativeEdited(initiativeEditor.getText(), row);
                hideKeyboardDelayed(0);
            }
        };

        private void show() {
            dialog.show();
        }
    }

    private DynamicArrayAdapter.ItemSwapListener itemsSwappedListener = new DynamicArrayAdapter.ItemSwapListener() {
        @Override
        public void onHoverEventFinished(DynamicListView.HoverEvent event) {
            presenter.onParticipantOrderChangeConfirmed(event.getStart(), event.getEnd());
        }

        @Override public void onItemsSwapped(int pos1, int pos2) {
            presenter.onParticipantOrdersChanged();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.encounters_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void onPreparePageOptionsMenu(Menu menu) {
        boolean isEncounterOngoing = presenter.isEncounterOngoing();
        menu.findItem(R.id.mi_reset).setVisible(isEncounterOngoing);
        menu.findItem(R.id.mi_start).setVisible(!isEncounterOngoing);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_start:
                showMenuPopup(R.menu.encounters_start_menu,
                        getActivity().findViewById(R.id.mi_start), startEncounterPopupListener);
                break;
            case R.id.mi_reset:
                presenter.onEncounterResetSelected();
                break;
            case R.id.mi_check_skill:
                showMenuPopup(R.menu.encounters_check_menu,
                        getActivity().findViewById(R.id.mi_check_skill), checkSkillPopupListener);
                break;
            case R.id.mi_select_encounter:
                showChooseEncounterPicker();
                break;
            case R.id.mi_add_participant:
                showAddCharactersPicker();
                break;
            case R.id.mi_new_encounter:
                showNewEncounterDialog();
                break;
            case R.id.mi_delete_encounter:
                showConfirmDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMenuPopup(int menuResource, View anchor, PopupMenu.OnMenuItemClickListener listener) {
        if (anchor == null) {
            anchor = getActivity().findViewById(R.id.overflow_popup_anchor);
        }
        PopupMenu popup = new PopupMenu(getActivity(), anchor);
        popup.setOnMenuItemClickListener(listener);
        popup.inflate(menuResource);

        popup.show();
    }

    private PopupMenu.OnMenuItemClickListener startEncounterPopupListener = new PopupMenu.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            EncounterStartMode mode = null;
            switch (item.getItemId()) {
                case R.id.mi_roll_init:     mode = EncounterStartMode.AUTO_ROLL;    break;
                case R.id.mi_manual_start:  mode = EncounterStartMode.MANUAL;       break;
            }

            presenter.onEncounterStarted(mode);
            return true;
        }
    };

    private PopupMenu.OnMenuItemClickListener checkSkillPopupListener = new PopupMenu.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            SkillCheckType checkType = null;
            switch (item.getItemId()) {
                case R.id.mi_fort_check:        checkType = SkillCheckType.FORT;        break;
                case R.id.mi_reflex_check:      checkType = SkillCheckType.REFLEX;      break;
                case R.id.mi_will_check:        checkType = SkillCheckType.WILL;        break;
                case R.id.mi_bluff_check:       checkType = SkillCheckType.BLUFF;       break;
                case R.id.mi_disguise_check:    checkType = SkillCheckType.DISGUISE;    break;
                case R.id.mi_perception_check:  checkType = SkillCheckType.PERCEPTION;  break;
                case R.id.mi_sense_check:       checkType = SkillCheckType.SENSE;       break;
                case R.id.mi_stealth_check:     checkType = SkillCheckType.STEALTH;     break;
            }

            presenter.onSkillCheckSelected(checkType);
            return true;
        }
    };

    private static final String ENCOUNTER_PICKER_ITEM_KEY = "encounters";

    private void showChooseEncounterPicker() {
        Intent pickerIntent = new PickerUtil.Builder(getContext())
                .setTitle(R.string.encounter_picker_title)
                .setSingleChoice(true)
                .addPickableItems(ENCOUNTER_PICKER_ITEM_KEY, getString(R.string.title_fragment_encounters),
                        presenter.getSelectableEncounters(), presenter.getModel().idNamePair())
                .build();
        startActivityForResult(pickerIntent, SELECT_ENCOUNTER_REQUEST_CODE);
    }

    private void showAddCharactersPicker() {
        Intent pickerIntent = new PickerUtil.Builder(getContext())
                .setTitle(R.string.add_participants_to_encounter_picker_title)
                .setSingleChoice(false)
                .setPickableCharacters(presenter.getCharactersAvailableToAddToEncounter())
                .setPickableParties(presenter.getPartiesAvailableToAddToEncounter())
                .build();
        startActivityForResult(pickerIntent, ADD_PARTICIPANTS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADD_PARTICIPANTS_REQUEST_CODE) {
            PickerUtil.ResultData result = new PickerUtil.ResultData(data);
            presenter.onCharactersAndPartiesSelectedToAddToEncounter(
                    result.getCharacters(),
                    result.getParties());
        } else if (requestCode == SELECT_ENCOUNTER_REQUEST_CODE) {
            PickerUtil.ResultData result = new PickerUtil.ResultData(data);
            presenter.onEncounterSelected(
                    result.getSelectedItemForKey(ENCOUNTER_PICKER_ITEM_KEY));
        }
    }


    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.delete_alert_title)
                .setMessage(String.format(getString(R.string.delete_encounter_dialog_title),
                        presenter.getModel().getName()));

        builder.setPositiveButton(R.string.cancel_button_text, null)
                .setNegativeButton(R.string.delete_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteCurrentEncounterSelected();
                    }
                })
                .show();
    }

    private void showNewEncounterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.menu_item_new_encounter)
                .setMessage(R.string.new_encounter_dialog_msg);

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                presenter.onNewEncounterSelected();
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    @Override
    public void updateTitle() {
        setTitle(R.string.title_fragment_encounters);
        setSubtitle(null);
    }
}
