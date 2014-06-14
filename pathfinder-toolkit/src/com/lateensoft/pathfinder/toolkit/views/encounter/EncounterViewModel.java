package com.lateensoft.pathfinder.toolkit.views.encounter;

import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.Collection;

public class EncounterViewModel extends NamedList<EncounterParticipant> {


    public EncounterViewModel(NamedList<EncounterParticipant> encounter) {
        super(encounter.getID(), encounter.getName(), encounter);
    }


}
