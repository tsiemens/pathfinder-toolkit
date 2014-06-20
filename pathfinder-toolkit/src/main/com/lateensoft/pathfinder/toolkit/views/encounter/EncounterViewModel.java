package com.lateensoft.pathfinder.toolkit.views.encounter;

import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

public class EncounterViewModel extends NamedList<EncounterParticipant> {


    public EncounterViewModel(NamedList<EncounterParticipant> encounter) {
        super(encounter.getId(), encounter.getName(), encounter);
    }


}
