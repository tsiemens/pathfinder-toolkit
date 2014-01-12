package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

public class PTCharacterRepository extends PTBaseRepository<PTCharacter> {
	private static final String TABLE = "Character";
	private static final String GOLD = "Gold";
	
	public PTCharacterRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER, true);
		PTTableAttribute gold = new PTTableAttribute(GOLD, SQLDataType.REAL);
		PTTableAttribute[] columns = {id, gold};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	
	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
	@Override
	public long insert(PTCharacter object) {
		long id = super.insert(object);
		
		if (id != -1) {
			// Sets all character ids of components
			object.setID(id);
			
			// Fluff
			PTFluffInfoRepository fluffRepo = new PTFluffInfoRepository();
			fluffRepo.insert(object.getFluff());
			
			// Combat Stats
			PTCombatStatRepository csRepo = new PTCombatStatRepository();
			csRepo.insert(object.getCombatStatSet());
			
			// Saves
			PTSaveRepository saveRepo = new PTSaveRepository();
			PTSave[] saves = object.getSaveSet().getSaves();
			for (PTSave save : saves) {
				saveRepo.insert(save);
			}
			
			// Skills
			PTSkillRepository skillRepo = new PTSkillRepository();
			PTSkill[] skills = object.getSkillSet().getSkills();
			for (PTSkill skill : skills) {
				skillRepo.insert(skill);
			}

			// Items
			PTItemRepository itemRepo = new PTItemRepository();
			PTItem[] items = object.getInventory().getItems();
			for (PTItem item : items) {
				itemRepo.insert(item);
			}
			
			// Weapons
			PTWeaponRepository weaponRepo = new PTWeaponRepository();
			PTWeapon[] weapons = object.getInventory().getWeaponArray();
			for (PTWeapon weapon : weapons) {
				weaponRepo.insert(weapon);
			}
			
			// Armor
			PTArmorRepository armorRepo = new PTArmorRepository();
			PTArmor[] armors = object.getInventory().getArmorArray();
			for (PTArmor armor : armors) {
				armorRepo.insert(armor);
			}
			
			// TODO add other components
		}
		return id;
	}

	@Override
	protected PTCharacter buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(CHARACTER_ID);
		double gold = (Double) hashTable.get(GOLD);
		
		// Fluff
		PTFluffInfoRepository fluffRepo = new PTFluffInfoRepository();
		PTFluffInfo fluff = fluffRepo.query(id);

		// Combat Stats
		PTCombatStatRepository csRepo = new PTCombatStatRepository();
		PTCombatStatSet csSet = csRepo.query(id);

		// Saves
		PTSaveRepository saveRepo = new PTSaveRepository();
		PTSaveSet saves = new PTSaveSet(saveRepo.querySet(id));

		// Skills
		PTSkillRepository skillRepo = new PTSkillRepository();
		PTSkillSet skills = new PTSkillSet(skillRepo.querySet(id));
		
		PTInventory inventory = new PTInventory();
		// Items
		PTItemRepository itemRepo = new PTItemRepository();
		ArrayList<PTItem> items = new ArrayList<PTItem>(Arrays.asList(itemRepo.querySet(id)));
		inventory.setItems(items);
		
		// Weapons
		PTWeaponRepository weaponRepo = new PTWeaponRepository();
		ArrayList<PTWeapon> weapons = new ArrayList<PTWeapon>(Arrays.asList(weaponRepo.querySet(id)));
		inventory.setWeapons(weapons);
		
		// Armor
		PTArmorRepository armorRepo = new PTArmorRepository();
		ArrayList<PTArmor> armor = new ArrayList<PTArmor>(Arrays.asList(armorRepo.querySet(id)));
		inventory.setArmor(armor);
		
		// TODO add other components

		PTCharacter character = new PTCharacter(id, gold, fluff, csSet, saves,
				skills);
		return character;
	}

	@Override
	protected ContentValues getContentValues(PTCharacter object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) { 
			values.put(CHARACTER_ID, object.getID());
		}
		values.put(GOLD, object.getGold());
		return values;
	}

}
