package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeatList;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;

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
		long subCompId;
		
		if (id != -1) {
			// Sets all character ids of components
			object.setID(id);
			
			// Fluff
			PTFluffInfoRepository fluffRepo = new PTFluffInfoRepository();
			subCompId = fluffRepo.insert(object.getFluff());
			if (subCompId == -1) {
				delete(id);
				return subCompId;
			}
			
			// AbilityScores
			PTAbilityRepository abScoreRepo = new PTAbilityRepository();
			PTAbilitySet abilitySet = object.getAbilitySet();
			for (int i = 0; i < abilitySet.size(); i++) {
				subCompId = abScoreRepo.insert(abilitySet.getAbilityAtIndex(i));
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Combat Stats
			PTCombatStatRepository csRepo = new PTCombatStatRepository();
			subCompId = csRepo.insert(object.getCombatStatSet());
			if (subCompId == -1) {
				delete(id);
				return subCompId;
			}
			
			// Saves
			PTSaveRepository saveRepo = new PTSaveRepository();
			PTSave[] saves = object.getSaveSet().getSaves();
			for (PTSave save : saves) {
				subCompId = saveRepo.insert(save);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Skills
			PTSkillRepository skillRepo = new PTSkillRepository();
			List<PTSkill> skills = object.getSkillSet().getSkills();
			for (PTSkill skill : skills) {
				subCompId = skillRepo.insert(skill);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}

			// Items
			PTItemRepository itemRepo = new PTItemRepository();
			List<PTItem> items = object.getInventory().getItems();
			for (PTItem item : items) {
				subCompId = itemRepo.insert(item);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Weapons
			PTWeaponRepository weaponRepo = new PTWeaponRepository();
			List<PTWeapon> weapons = object.getInventory().getWeapons();
			for (PTWeapon weapon : weapons) {
				subCompId = weaponRepo.insert(weapon);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Armor
			PTArmorRepository armorRepo = new PTArmorRepository();
			List<PTArmor> armors = object.getInventory().getArmors();
			for (PTArmor armor : armors) {
				subCompId = armorRepo.insert(armor);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Feats
			PTFeatRepository featRepo = new PTFeatRepository();
			PTFeatList featList = object.getFeatList();
			for (PTFeat feat : featList) {
				subCompId = featRepo.insert(feat);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Spells
			PTSpellRepository spellRepo = new PTSpellRepository();
			PTSpellBook spells = object.getSpellBook();
			for (PTSpell spell : spells) {
				subCompId = spellRepo.insert(spell);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
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
		
		// Ability Scores
		PTAbilityRepository abilityRepository = new PTAbilityRepository();
		PTAbilitySet abilityScores = abilityRepository.querySet(id);

		// Combat Stats
		PTCombatStatRepository csRepo = new PTCombatStatRepository();
		PTCombatStatSet csSet = csRepo.query(id);

		// Saves
		PTSaveRepository saveRepo = new PTSaveRepository();
		PTSaveSet saves = new PTSaveSet(saveRepo.querySet(id));

		// Skills
		PTSkillRepository skillRepo = new PTSkillRepository();
		PTSkillSet skills = PTSkillSet.newValidatedSkillSet(skillRepo.queryAllForCharacter(id), new PTSkillSet.CorrectionListener() {
            @Override
            public void onInvalidSkillRemoved(PTSkill removedSkill) {
                PTSkillRepository skillRepo = new PTSkillRepository();
                skillRepo.delete(removedSkill);
            }

            @Override
            public void onMissingSkillAdded(PTSkill addedSkill) {
                PTSkillRepository skillRepo = new PTSkillRepository();
                skillRepo.insert(addedSkill);
            }

            @Override
            public void onSkillModified(PTSkill modifiedSkill) {
                PTSkillRepository skillRepo = new PTSkillRepository();
                skillRepo.update(modifiedSkill);
            }
        });
		
		PTInventory inventory = new PTInventory();
		// Items
		PTItemRepository itemRepo = new PTItemRepository();
		inventory.getItems().addAll(itemRepo.querySet(id));
		
		// Weapons
		PTWeaponRepository weaponRepo = new PTWeaponRepository();
		inventory.getWeapons().addAll(weaponRepo.querySet(id));
		
		// Armor
		PTArmorRepository armorRepo = new PTArmorRepository();
		inventory.getArmors().addAll(armorRepo.querySet(id));
		
		// Feats
		PTFeatRepository featRepo = new PTFeatRepository();
		PTFeatList feats = new PTFeatList(featRepo.querySet(id));
					
		// Spells
		PTSpellRepository spellRepo = new PTSpellRepository();
		PTSpellBook spells = new PTSpellBook(spellRepo.querySet(id));

		return new PTCharacter(id, gold, abilityScores,
				fluff, csSet, saves, skills, inventory, feats, spells);
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

	/**
	 * @return name of character with id
	 */
	public String queryName(long id) {
		Locale l = null;
		String selector = String.format(l, "%s.%s=%s.%s AND %s.%s=%d",
                TABLE, CHARACTER_ID,
                PTFluffInfoRepository.TABLE, CHARACTER_ID,
                TABLE, CHARACTER_ID, id);
		String table = m_tableInfo.getTable()+", "+PTFluffInfoRepository.TABLE;
		String[] columns = {PTFluffInfoRepository.NAME};
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, null, null);
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			return (String)hashTable.get(PTFluffInfoRepository.NAME); 
		}
		return null;
	}
	
	/**
	 * Returns all characters
	 * @return Array of IdNamePair, ordered alphabetically by name
	 */
	public List<Entry<Long, String>> queryList() {
		Locale l = null;
		String selector = String.format(l, "%s.%s=%s.%s", 
				TABLE, CHARACTER_ID,
				 PTFluffInfoRepository.TABLE,  CHARACTER_ID);
		String orderBy = PTFluffInfoRepository.NAME + " ASC";
		String table = m_tableInfo.getTable()+", "+PTFluffInfoRepository.TABLE;
		String[] columns = {m_tableInfo.getTable()+"."+CHARACTER_ID+" "+CHARACTER_ID, 
				PTFluffInfoRepository.NAME};
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		ArrayList<Entry<Long, String>> characters = new ArrayList<Entry<Long, String>>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			characters.add(new SimpleEntry<Long, String>((Long)hashTable.get(CHARACTER_ID), 
					(String)hashTable.get(PTFluffInfoRepository.NAME)));
			cursor.moveToNext();
		}
		return characters;
	}
}
