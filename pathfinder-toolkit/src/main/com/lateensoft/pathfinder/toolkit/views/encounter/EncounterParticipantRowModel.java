package com.lateensoft.pathfinder.toolkit.views.encounter;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.List;

/**
* @author tsiemens
*/
public class EncounterParticipantRowModel {
    private final EncounterParticipant participant;
    private int lastCheckRoll = 0;

    public List<EncounterParticipantRowModel> fromList(List<EncounterParticipant> participants) {
        List<EncounterParticipantRowModel> rows = Lists.newArrayListWithCapacity(participants.size());
        for (EncounterParticipant participant : participants) {
            rows.add(new EncounterParticipantRowModel(participant));
        }
        return rows;
    }

    public EncounterParticipantRowModel(EncounterParticipant participant) {
        this.participant = participant;
    }

    public EncounterParticipant getParticipant() {
        return participant;
    }

    public int getLastCheckRoll() {
        return lastCheckRoll;
    }

    public void setLastCheckRoll(int lastCheckRoll) {
        this.lastCheckRoll = lastCheckRoll;
    }
}
