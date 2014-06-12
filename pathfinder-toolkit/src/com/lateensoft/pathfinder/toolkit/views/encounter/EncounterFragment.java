package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicListView;

import java.util.List;

/**
 * @author tsiemens
 */
public class EncounterFragment extends BasePageFragment {

    private DynamicListView participantListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.fragment_encounters, container, false));

        participantListView = (DynamicListView) getRootView()
                .findViewById(R.id.listview);

        refreshParticipantListContent();
        return getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void refreshParticipantListContent() {
        EncounterParticipantListAdapter adapter = new EncounterParticipantListAdapter(getActivity(), buildTestRows());
        participantListView.setDynamicAdapter(adapter);
        adapter.setDragIconTouchListener(dragIconTouchListener);
    }

    private EncounterParticipantListAdapter.RowTouchListener dragIconTouchListener = new EncounterParticipantListAdapter.RowTouchListener() {
        @Override public void onTouch(View v, MotionEvent event, int position) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && participantListView.canHoverRows()) {
                participantListView.hoverRow(position);
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

    @Override
    public void updateTitle() {
        setTitle("Encounters"); // TODO strings.xml
        setSubtitle("Encounter name here");
    }
}
