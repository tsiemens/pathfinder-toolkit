package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.util.Pair;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillType;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.util.DiceSet;

import static com.lateensoft.pathfinder.toolkit.views.encounter.EncounterParticipantRowModel.RollState;

public class EncounterRoller {

    private static final DiceSet.Die INIT_DIE = DiceSet.Die.D20;
    private static final DiceSet.Die SKILL_CHECK_DIE = DiceSet.Die.D20;

    private DiceSet dice = new DiceSet();
    private EncounterViewModel encounter;

    public EncounterRoller(EncounterViewModel encounter) {
        this.encounter = encounter;
    }

    public void rollInitiatives() {
        for (EncounterParticipantRowModel row : encounter) {
            EncounterParticipant participant = row.getParticipant();
            participant.setInitiativeScore(rollWithModifier(INIT_DIE, participant.getInitiativeMod()).first);
        }
        encounter.setInCombat(true);
    }

    private Pair<Integer, RollState> rollWithModifier(DiceSet.Die die, int modifier) {
        int roll = dice.roll(die);
        RollState state = RollState.NORMAL;
        if (roll == 1) {
            state = RollState.CRIT_FAIL;
        } else if (roll == die.getSides()) {
            state = RollState.CRIT;
        }
        return new Pair<Integer, RollState>(roll + modifier, state);
    }

    public void resetInitiatives() {
        for (EncounterParticipantRowModel row : encounter) {
            row.getParticipant().setInitiativeScore(0);
        }
    }

    public enum SkillCheckType {
        FORT(R.string.fort_check),
        REFLEX(R.string.reflex_check),
        WILL(R.string.will_check),
        BLUFF(R.string.bluff_check),
        DISGUISE(R.string.disguise_check),
        PERCEPTION(R.string.perception_check),
        SENSE(R.string.sense_check),
        STEALTH(R.string.stealth_check);

        private int displayNameResId;

        SkillCheckType(int displayNameResId) {
            this.displayNameResId = displayNameResId;
        }

        public int getDisplayNameResId() {
            return displayNameResId;
        }
    }

    public void rollSkillChecks(SkillCheckType checkType) {
        for (EncounterParticipantRowModel row : encounter) {
            Pair<Integer, RollState> roll = rollSkillCheck(row.getParticipant(), checkType);
            row.setLastCheckRoll(roll.first);
            row.setLastCheckRollState(roll.second);
        }
    }

    private Pair<Integer, RollState> rollSkillCheck(EncounterParticipant participant, SkillCheckType checkType) {
        return rollWithModifier(SKILL_CHECK_DIE, getModForSkillCheckType(participant, checkType));
    }

    private int getModForSkillCheckType(EncounterParticipant participant, SkillCheckType checkType) {
        switch (checkType) {
            case FORT:
                return participant.getSaveMod(SaveType.FORT);
            case REFLEX:
                return participant.getSaveMod(SaveType.REF);
            case WILL:
                return participant.getSaveMod(SaveType.WILL);
            case BLUFF:
                return participant.getSkillMod(SkillType.BLUFF);
            case DISGUISE:
                return participant.getSkillMod(SkillType.DISGUISE);
            case PERCEPTION:
                return participant.getSkillMod(SkillType.PERCEPTION);
            case SENSE:
                return participant.getSkillMod(SkillType.SENSE_MOTIVE);
            case STEALTH:
                return participant.getSkillMod(SkillType.STEALTH);
            default:
                throw new IllegalArgumentException("Invalid check type: " + checkType);
        }
    }

    public void resetLastSkillChecks() {
        for (EncounterParticipantRowModel row : encounter) {
            row.setLastCheckRoll(0);
        }
    }
}
