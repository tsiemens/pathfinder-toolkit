package com.lateensoft.pathfinder.toolkit.test.db;

import com.lateensoft.pathfinder.toolkit.db.Database;

import android.database.Cursor;
import android.test.AndroidTestCase;

public class TableCreatorTest extends AndroidTestCase {
	protected Database m_db;
	
	protected void setUp() throws Exception {
		super.setUp();
		m_db = Database.getInstance();
	}
	
	public void testDatabaseCreation() {
		Cursor c = m_db.query("sqlite_master", new String[]{"name"}, 
				"type='table' ORDER BY name ASC");
		c.moveToFirst();
		String[] tables = {"Ability", "Armor", "Character", "CombatStatSet",
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
			assertTrue(tables[i] + " not found ", found);
		}
	}
}
