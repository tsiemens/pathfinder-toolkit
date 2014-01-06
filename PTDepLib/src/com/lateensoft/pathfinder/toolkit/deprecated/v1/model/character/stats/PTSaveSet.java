package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats;

import com.lateensoft.pathfinder.toolkit.deprecated.R;

import android.content.Context;
import android.content.res.Resources;

public class PTSaveSet {
	PTSave[] mSaves;
	
	public PTSaveSet(Context context) {
		Resources r = context.getResources();
		String[] names = r.getStringArray(R.array.save_names);
		
		mSaves = new PTSave[3];
		for(int i = 0; i < mSaves.length; i++) {
			mSaves[i] = new PTSave(names[i]);
		}
	}
	
	public PTSave getSave(int index) {
		return mSaves[index];
	}
	
	public void setAbilityMods(PTAbilitySet abilities, Context context) {
		Resources r = context.getResources();
		
		mSaves[r.getInteger(R.integer.key_fort_save)].setAbilityMod(
				abilities.getAbilityScore(R.integer.key_constitution).getModifier());
		mSaves[r.getInteger(R.integer.key_ref_save)].setAbilityMod(
				abilities.getAbilityScore(R.integer.key_dexterity).getModifier());
		mSaves[r.getInteger(R.integer.key_will_save)].setAbilityMod(
				abilities.getAbilityScore(R.integer.key_wisdom).getModifier());
	}
}
