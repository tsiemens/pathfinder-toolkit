package com.lateensoft.pathfinder.toolkit.test.db;

import com.lateensoft.pathfinder.toolkit.db.PTDatabase;

import android.database.Cursor;
import android.test.AndroidTestCase;

public class PTTableCreatorTest extends AndroidTestCase {
	protected PTDatabase m_db;
	
	protected void setUp() throws Exception {
		super.setUp();
		m_db = PTDatabase.getInstance();
	}
	
	public void testDatabaseCreation() {
		Cursor c = m_db.query("sqlite_master", new String[]{"name"}, 
				"type='table' ORDER BY name ASC");
		c.moveToFirst();
		String[] tables = {"AbilityScore", "Armor", "Character", "CombatStatSet",
				"Feat", "FluffInfo","Item","Party","PartyMember","Save","Skill",
				"Spell","Weapon"};

		boolean found;
		for (int i=0; i < tables.length; i++) {
			found = false;
			c.moveToFirst();
			while(!c.isAfterLast()) {
				if (tables[i].contentEquals(c.getString(0))) {
					found = true;
					break;
				}
				c.moveToNext();
			}
			assertTrue(tables[i] + "not found", found);
		}
	}
}
