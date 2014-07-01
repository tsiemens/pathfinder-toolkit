package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats;

public class PTAbilitySetCalc extends PTAbilitySet {
    private PTAbilityScore[] mAbilitiesPostMods;
    
    public PTAbilitySetCalc() {
        super();
        
        //Resources res = android.content.res.Resources.getSystem();
        //int base_ability = res.getInteger(R.integer.base_ability_score);
        mAbilitiesPostMods = new PTAbilityScore[ABILITY_NAMES.length];
        
        for(int i = 0; i < ABILITY_NAMES.length; i++) {
            mAbilitiesPostMods[i] = new PTAbilityScore(ABILITY_NAMES[i], BASE_ABILITY_SCORE);
        }
    }
    
    // Returns the point buy cost of the set
    public int getPointBuyCost() {
        int total = 0;
        for(int i = 0; i < super.getLength(); i++) {
            total += super.getAbilityScore(i).getAbilityPointCost();
        }
            
        return total;
    }
    
    // Increments the ability score with the corresponding 
    // ability string, if it is valid to do so
    public void incAbilityScore(String ability) {
        for(int i = 0; i < super.getLength(); i++) {
            if(ability.equals(super.getAbilityScore(i).getAbility())) {
                super.getAbilityScore(i).incScore();
                return;
            }
        }
    }
    
    // Increments the ability score with the corresponding 
    // ability string, if it is valid to do so
    public void decAbilityScore(String ability) {
        for(int i = 0; i < super.getLength(); i++) {
            if(ability.equals(super.getAbilityScore(i).getAbility())) {
                super.getAbilityScore(i).decScore();
                return;
            }
        }
    }
    
    public void applyMods(PTAbilityModSet modSet) {
        int[] mods = modSet.getMods();
        int[] baseAbilities = super.getScores();
        
        if(mods.length != baseAbilities.length) {
            return;
        }
        
        for(int i = 0; i < mAbilitiesPostMods.length; i++) {
            mAbilitiesPostMods[i].setScore(mods[i] + baseAbilities[i]);
        }
    }
    
    public int[] getScoresPostMods() {
        int[] scores = new int[mAbilitiesPostMods.length];
        for(int i = 0; i < mAbilitiesPostMods.length; i++) {
            scores[i] = mAbilitiesPostMods[i].getScore();
        }
        return scores;
    }
    
    public int getScorePostMod(int index) {
        return mAbilitiesPostMods[index].getScore();
    }
    
    public PTAbilityScore getAbilityScorePostMod(int index) {
        return mAbilitiesPostMods[index];
    }
    
    public PTAbilitySet getAbilitySetPostMods() {
        PTAbilitySet abilitySet = new PTAbilitySet();
        abilitySet.setScores(this.getScoresPostMods());
        return abilitySet;
    }
}
