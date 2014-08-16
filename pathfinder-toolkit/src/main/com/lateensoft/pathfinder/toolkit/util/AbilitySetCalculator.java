package com.lateensoft.pathfinder.toolkit.util;

import android.content.Context;
import android.content.res.Resources;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;

public class AbilitySetCalculator {
    
    public static final int[] RACE_MOD_RES = { R.array.HumanMods, R.array.DwarfMods,
        R.array.ElfMods, R.array.HalflingMods, R.array.GnomeMods, R.array.HumanMods, R.array.HumanMods };
    
    /**
     * This ability set uses the temp bonus for the race mods
     */
    private AbilitySet m_abilitySet;
    
    public AbilitySetCalculator() {
        this(new AbilitySet());
    }
    
    public AbilitySetCalculator(AbilitySet abilitySet) {
        m_abilitySet = abilitySet;
    }
    
    /**
     * Sets the racial mods of the contained ability set
     * @param raceIndex the index for the race, as defined by RACE_MODS_RES
     * @param context
     * @return true if race is at least half human
     */
    public boolean setRacialMods(int raceIndex, Context context) {
        Resources r = context.getResources();
        int[] raceMods = r.getIntArray(RACE_MOD_RES[raceIndex]);
        
        for (int i = 0; i < m_abilitySet.size(); i++) {
            m_abilitySet.getAbilityAtIndex(i).setTempBonus(raceMods[i]);
        }
        
        return (RACE_MOD_RES[raceIndex] == R.array.HumanMods);
    }
    
    /**
     * Sets the racial mods of the contained ability set to human,
     * with the +2 bonus applied to the ability for abilityIndex
     * @param abilityIndex
     * @param context
     */
    public void setHumanRacialMods(int abilityIndex, Context context) {
        Resources r = context.getResources();
        int[] humanMods = r.getIntArray(R.array.HumanMods);
        
        for (int i = 0; i < m_abilitySet.size(); i++) {
            m_abilitySet.getAbilityAtIndex(i).setTempBonus(humanMods[i]);
        }
        m_abilitySet.getAbilityAtIndex(abilityIndex).setTempBonus(2);
    }
    
    /**
     * @return the point buy cost of the set
     */
    public int getPointBuyCost() {
        int total = 0;
        for(int i = 0; i < m_abilitySet.size(); i++) {
            total += m_abilitySet.getAbilityAtIndex(i).getAbilityPointCost();
        }

        return total;
    }

    /**
     *  Increments the ability score with the corresponding 
     *  ability id
     */
    public void incAbilityScore(int index) {
        m_abilitySet.getAbilityAtIndex(index).incScore();
    }
    
    /**
     *  Decrements the ability score with the corresponding 
     *  ability id
     */
    public void decAbilityScore(int index) {
        m_abilitySet.getAbilityAtIndex(index).decScore();
    }
    
    public int getScorePostRaceMod(int index) {
        Ability ability = m_abilitySet.getAbilityAtIndex(index);
        return ability.getScore() + ability.getTempBonus();
    }
    
    public int getBaseScore(int index) {
        return  m_abilitySet.getAbilityAtIndex(index).getScore();
    }
    
    public int getModPostRaceMod(int index) {
        return  m_abilitySet.getAbilityAtIndex(index).getTempModifier();
    }
    
    public int getRaceMod(int index) {
        return  m_abilitySet.getAbilityAtIndex(index).getTempBonus();
    }
    
    public void setCustomRaceMods(int[] mods) {
        for (int i = 0; i < m_abilitySet.size(); i++) {
            m_abilitySet.getAbilityAtIndex(i).setTempBonus(mods[i]);
        }
    }
    
    public void setCalculatedAbilityScores(AbilitySet abilitySet) {
        for (int i = 0; i < abilitySet.size(); i++) {
            Ability calcAb = m_abilitySet.getAbilityAtIndex(i);
            abilitySet.getAbilityAtIndex(i)
            .setScore(calcAb.getScore() + calcAb.getTempBonus());
        }
    }
}
