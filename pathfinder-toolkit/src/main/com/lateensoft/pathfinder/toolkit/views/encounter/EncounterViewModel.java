package com.lateensoft.pathfinder.toolkit.views.encounter;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EncounterViewModel extends NamedList<EncounterParticipantRowModel> {

    public EncounterViewModel() {
        super("New Encounter");
    }

    public EncounterViewModel(NamedList<EncounterParticipant> encounter) {
        super(encounter.getId(), encounter.getName(), rowModelsForParticipants(encounter));
        Collections.sort(this);
    }

    public void updateTurnOrders() {
        for (int i = 0; i < size(); i++) {
            get(i).getParticipant().setTurnOrder(i);
        }
    }

    private static List<EncounterParticipantRowModel> rowModelsForParticipants(Collection<EncounterParticipant> participants) {
        List<EncounterParticipantRowModel> rows = Lists.newArrayListWithCapacity(participants.size());
        for (EncounterParticipant participant : participants) {
            rows.add(new EncounterParticipantRowModel(participant));
        }
        return rows;
    }

    public NamedList<EncounterParticipant> buildEncounterForModel() {
        List<EncounterParticipant> participants = Lists.newArrayListWithCapacity(this.size());
        for (EncounterParticipantRowModel row : this) {
            participants.add(row.getParticipant());
        }
        return new NamedList<EncounterParticipant>(this.getId(), this.getName(), participants);
    }

    public List<IdNamePair> getParticipantIds() {
        List<IdNamePair> participantIds = Lists.newArrayList();
        for (EncounterParticipantRowModel row : this) {
            EncounterParticipant participant = row.getParticipant();
            participantIds.add(new IdNamePair(participant.getId(), participant.getName()));
        }
        return participantIds;
    }
}
