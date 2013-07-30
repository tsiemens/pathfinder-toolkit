package com.lateensoft.pathfinder.toolkit.repository;

import com.lateensoft.pathfinder.toolkit.stats.PTSkill;

import android.content.ContentValues;
import android.database.Cursor;

public class PTSkillRepository extends PTBaseRepository<PTSkill> {
	static final String TABLE = "Skill";
	static final String ID = "skill_id";
	static final String NAME = "Name";
	static final String CLASS_SKILL = "ClassSkill";
	static final String KEY_ABILITY = "KeyAbility";
	static final String ABILITY_MOD = "AbilityMod";
	static final String MISC_MOD = "MiscMod";
	static final String ARMOR_CHECK_PENALTY = "ArmorCheckPenalty";
	static final String KEY_ABILITY_KEY = "KeyAbilityKey";
	
	@Override
	protected PTSkill buildFromCursor(Cursor cursor) {
		// TODO
		return null;
	}
	@Override
	protected ContentValues getContentValues(PTSkill object) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void baseUpdate(PTSkill object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void baseDelete(PTSkill object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected String TABLE() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String[] COLUMNS() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String ID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
