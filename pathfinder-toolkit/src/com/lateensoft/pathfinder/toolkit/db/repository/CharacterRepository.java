package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.*;
import com.lateensoft.pathfinder.toolkit.model.character.Character;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;

public class CharacterRepository extends BaseRepository<Character> {
	private static final String TABLE = "Character";
	private static final String GOLD = "Gold";
	
	public CharacterRepository() {
		super();
		TableAttribute id = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER, true);
		TableAttribute gold = new TableAttribute(GOLD, SQLDataType.REAL);
		TableAttribute[] columns = {id, gold};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
	@Override
	public long insert(Character object) {
		long id = super.insert(object);
		long subCompId;
		
		if (id != -1) {
			// Sets all character ids of components
			object.setID(id);
			
			// Fluff
			FluffInfoRepository fluffRepo = new FluffInfoRepository();
			subCompId = fluffRepo.insert(object.getFluff());
			if (subCompId == -1) {
				delete(id);
				return subCompId;
			}
			
			// AbilityScores
			AbilityRepository abScoreRepo = new AbilityRepository();
			AbilitySet abilitySet = object.getAbilitySet();
			for (int i = 0; i < abilitySet.size(); i++) {
				subCompId = abScoreRepo.insert(abilitySet.getAbilityAtIndex(i));
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Combat Stats
			CombatStatRepository csRepo = new CombatStatRepository();
			subCompId = csRepo.insert(object.getCombatStatSet());
			if (subCompId == -1) {
				delete(id);
				return subCompId;
			}
			
			// Saves
			SaveRepository saveRepo = new SaveRepository();
			SaveSet saves = object.getSaveSet();
			for (Save save : saves) {
				subCompId = saveRepo.insert(save);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Skills
			SkillRepository skillRepo = new SkillRepository();
			SkillSet skills = object.getSkillSet();
			for (Skill skill : skills) {
				subCompId = skillRepo.insert(skill);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}

			// Items
			ItemRepository itemRepo = new ItemRepository();
			List<Item> items = object.getInventory().getItems();
			for (Item item : items) {
				subCompId = itemRepo.insert(item);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Weapons
			WeaponRepository weaponRepo = new WeaponRepository();
			List<Weapon> weapons = object.getInventory().getWeapons();
			for (Weapon weapon : weapons) {
				subCompId = weaponRepo.insert(weapon);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Armor
			ArmorRepository armorRepo = new ArmorRepository();
			List<Armor> armors = object.getInventory().getArmors();
			for (Armor armor : armors) {
				subCompId = armorRepo.insert(armor);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Feats
			FeatRepository featRepo = new FeatRepository();
			FeatList featList = object.getFeatList();
			for (Feat feat : featList) {
				subCompId = featRepo.insert(feat);
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
			
			// Spells
			SpellRepository spellRepo = new SpellRepository();
			SpellBook spells = object.getSpellBook();
			for (Spell spell : spells) {
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
	protected Character buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(CHARACTER_ID);
		double gold = (Double) hashTable.get(GOLD);
		
		// Fluff
		FluffInfoRepository fluffRepo = new FluffInfoRepository();
		FluffInfo fluff = fluffRepo.query(id);
		
		// Ability Scores
		AbilityRepository abilityRepository = new AbilityRepository();
		AbilitySet abilityScores = abilityRepository.querySet(id);

		// Combat Stats
		CombatStatRepository csRepo = new CombatStatRepository();
		CombatStatSet csSet = csRepo.query(id);

		// Saves
		SaveRepository saveRepo = new SaveRepository();
		SaveSet saves = saveRepo.querySet(id);

		// Skills
		SkillRepository skillRepo = new SkillRepository();
		SkillSet skills = skillRepo.querySet(id);
		
		Inventory inventory = new Inventory();
		// Items
		ItemRepository itemRepo = new ItemRepository();
		inventory.getItems().addAll(itemRepo.querySet(id));
		
		// Weapons
		WeaponRepository weaponRepo = new WeaponRepository();
		inventory.getWeapons().addAll(weaponRepo.querySet(id));
		
		// Armor
		ArmorRepository armorRepo = new ArmorRepository();
		inventory.getArmors().addAll(armorRepo.querySet(id));
		
		// Feats
		FeatRepository featRepo = new FeatRepository();
		FeatList feats = new FeatList(featRepo.querySet(id));
					
		// Spells
		SpellRepository spellRepo = new SpellRepository();
		SpellBook spells = new SpellBook(spellRepo.querySet(id));

		return new Character(id, gold, abilityScores,
				fluff, csSet, saves, skills, inventory, feats, spells);
	}

	@Override
	protected ContentValues getContentValues(Character object) {
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
                FluffInfoRepository.TABLE, CHARACTER_ID,
                TABLE, CHARACTER_ID, id);
		String table = m_tableInfo.getTable()+", "+ FluffInfoRepository.TABLE;
		String[] columns = {FluffInfoRepository.NAME};
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, null, null);
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			return (String)hashTable.get(FluffInfoRepository.NAME);
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
				 FluffInfoRepository.TABLE,  CHARACTER_ID);
		String orderBy = FluffInfoRepository.NAME + " ASC";
		String table = m_tableInfo.getTable()+", "+ FluffInfoRepository.TABLE;
		String[] columns = {m_tableInfo.getTable()+"."+CHARACTER_ID+" "+CHARACTER_ID, 
				FluffInfoRepository.NAME};
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		ArrayList<Entry<Long, String>> characters = new ArrayList<Entry<Long, String>>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			characters.add(new SimpleEntry<Long, String>((Long)hashTable.get(CHARACTER_ID), 
					(String)hashTable.get(FluffInfoRepository.NAME)));
			cursor.moveToNext();
		}
		return characters;
	}
}
