package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicListView;

import java.util.List;

public class EncounterFragment extends BasePageFragment {

    private EditText encounterNameEditor;
    private TextView lastSkillCheckName;
    private Button nextTurnButton;
    private DynamicListView participantList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.fragment_encounters, container, false));

        initializeComponents();

        refreshParticipantListContent();
        return getRootView();
    }

    private void initializeComponents() {
        encounterNameEditor = (EditText) getRootView().findViewById(R.id.et_encounter_name);
        lastSkillCheckName = (TextView) getRootView().findViewById(R.id.tv_last_skill_check);
        nextTurnButton = (Button) getRootView().findViewById(R.id.button_next);
        participantList = (DynamicListView) getRootView().findViewById(R.id.listview);
    }

    @Override
    public void updateTitle() {
        setTitle("Encounters"); // TODO strings.xml
        setSubtitle("Encounter name here");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        updateDatabase();
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.encounters_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_start:
                showMenuPopup(R.menu.encounters_start_menu, getActivity().findViewById(R.id.mi_start));
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showMenuPopup(int menuResource, View anchor) {
        if (anchor == null) {
            anchor = getActivity().findViewById(R.id.overflow_popup_anchor);
        }
        PopupMenu popup = new PopupMenu(getActivity(), anchor);
        popup.inflate(menuResource);

        popup.show();
    }


    private void refreshParticipantListContent() {
        EncounterParticipantListAdapter adapter = new EncounterParticipantListAdapter(getActivity(), buildTestRows());
        participantList.setDynamicAdapter(adapter);
        adapter.setDragIconTouchListener(dragIconTouchListener);
    }

    private EncounterParticipantListAdapter.RowTouchListener dragIconTouchListener = new EncounterParticipantListAdapter.RowTouchListener() {
        @Override public void onTouch(View v, MotionEvent event, int position) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && participantList.canHoverRows()) {
                participantList.hoverRow(position);
            }
        }
    };

    @Deprecated // TODO test
    private List<EncounterParticipantRowModel> buildTestRows() {
        return Lists.newArrayList(
                buildRowModel("Person 1", 20, 19),
                buildRowModel("Person 2", 21, 18),
                buildRowModel("Person 3", 22, 17),
                buildRowModel("Person 4", 23, 16),
                buildRowModel("Person 5", 24, 15),
                buildRowModel("Person 6", 25, 14)
        );
    }

    @Deprecated // TODO test
    private EncounterParticipantRowModel buildRowModel(String name, int init, int check) {
        EncounterParticipantRowModel row = new EncounterParticipantRowModel(EncounterParticipant.builder()
                .setName(name)
                .setInitiativeScore(init)
                .build());
        row.setLastCheckRoll(check);
        return row;
    }

    private void updateDatabase() {
        Editable text = encounterNameEditor.getText();
//        m_party.setName(text != null ? text.toString() : "");
//        m_partyRepo.update(m_party);
    }
}
