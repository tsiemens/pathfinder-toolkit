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
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;

public class CharacterRepository extends BaseRepository<PathfinderCharacter> {
	private static final String TABLE = "Character";
	private static final String GOLD = "Gold";

    private FluffInfoRepository m_fluffRepo = new FluffInfoRepository();
    private AbilityRepository m_abilityRepo = new AbilityRepository();
    private CombatStatRepository m_combatStatRepo = new CombatStatRepository();
    private SaveRepository m_saveRepo = new SaveRepository();
    private SkillRepository m_skillRepo = new SkillRepository();
    private ItemRepository m_itemRepo = new ItemRepository();
    private WeaponRepository m_weaponRepo = new WeaponRepository();
    private ArmorRepository m_armorRepo = new ArmorRepository();
    private FeatRepository m_featRepo = new FeatRepository();
    private SpellRepository m_spellRepo = new SpellRepository();

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
	public long insert(PathfinderCharacter object) {
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
	protected PathfinderCharacter buildFromHashTable(Hashtable<String, Object> hashTable) {
        PathfinderCharacter.Builder builder = new PathfinderCharacter.Builder();
        populateBuilderFromHashTable(hashTable, builder);
		return builder.build();
	}

    protected void populateBuilderFromHashTable(Hashtable<String, Object> hashTable,
                                             PathfinderCharacter.Builder builder) {
        long id = (Long) hashTable.get(CHARACTER_ID);
        builder.setId(id)
            .setGold((Double) hashTable.get(GOLD))
            .setFluffInfo(m_fluffRepo.query(id))
            .setAbilitySet(m_abilityRepo.querySet(id))
            .setCombatStatSet(m_combatStatRepo.query(id))
            .setSaveSet(m_saveRepo.querySet(id))
            .setSkillSet(m_skillRepo.querySet(id));

        Inventory inventory = new Inventory();
        inventory.getItems().addAll(m_itemRepo.querySet(id));
        inventory.getWeapons().addAll(m_weaponRepo.querySet(id));
        inventory.getArmors().addAll(m_armorRepo.querySet(id));

        builder.setInventory(inventory)
            .setFeats(new FeatList(m_featRepo.querySet(id)))
            .setSpellBook(new SpellBook(m_spellRepo.querySet(id)));
    }

	@Override
	protected ContentValues getContentValues(PathfinderCharacter object) {
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
