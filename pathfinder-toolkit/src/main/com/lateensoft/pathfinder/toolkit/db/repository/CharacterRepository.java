package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.character.*;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;

public class CharacterRepository extends BaseRepository<PathfinderCharacter> {
    public static final String TABLE = "Character";

    public static final String NAME = "Name";
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
        TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
        TableAttribute gold = new TableAttribute(GOLD, SQLDataType.REAL);
        TableAttribute[] columns = {id, name, gold};
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
            object.setId(id);

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
            values.put(CHARACTER_ID, object.getId());
        }
        values.put(NAME, object.getName());
        values.put(GOLD, object.getGold());
        return values;
    }

    /**
     * @return name of character with id
     */
    public String queryName(long id) {
        Locale l = null;
        String selector = String.format(l, "%s.%s=%d",
                TABLE, CHARACTER_ID, id);
        String table = m_tableInfo.getTable();
        String[] columns = {NAME};
        Cursor cursor = getDatabase().query(true, table, columns, selector,
                null, null, null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            return (String)hashTable.get(NAME);
        }
        return null;
    }

    @Deprecated
    public void updateName(long id, String name) {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        getDatabase().update(TABLE, values, getSelector(id));
    }

    /**
     * Returns all characters
     * @return Array of IdNamePair, ordered alphabetically by name
     */
    public List<IdStringPair> queryIdNameList() {
        String orderBy = NAME + " ASC";
        String table = m_tableInfo.getTable();
        String[] columns = {CHARACTER_ID, NAME};
        Cursor cursor = getDatabase().query(true, table, columns, null,
                null, null, null, orderBy, null);

        ArrayList<IdStringPair> characters = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            characters.add(new IdStringPair((Long)hashTable.get(CHARACTER_ID),
                    (String)hashTable.get(NAME)));
            cursor.moveToNext();
        }
        cursor.close();
        return characters;
    }

    protected List<IdStringPair> queryFilteredIdNameList(String selector) {
        String orderBy = NAME + " ASC";
        String table = m_tableInfo.getTable();
        String[] columns = {CHARACTER_ID,NAME};
        Cursor cursor = getDatabase().query(true, table, columns, selector,
                null, null, null, orderBy, null);

        List<IdStringPair> characters = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            characters.add(new IdStringPair((Long)hashTable.get(CHARACTER_ID),
                    (String)hashTable.get(NAME)));
            cursor.moveToNext();
        }
        cursor.close();
        return characters;
    }

    public boolean doesExist(long id) {
        Cursor cursor= getDatabase().rawQuery("select count(*) count from " + TABLE + " where " +
                CHARACTER_ID + "=" + id, null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("count"));
        cursor.close();
        return count != 0;
    }

    public String[] getCombinedTableColumns(String[] subTableCols) {
        List<String> subTableColsList = Lists.newArrayList(subTableCols);
        subTableColsList.remove(CHARACTER_ID);

        List<String> combinedCols = Lists.newArrayList(m_tableInfo.getColumns());
        int idColIndex = combinedCols.indexOf(CHARACTER_ID);
        // renames the column to simply character_id. ie. SELECT Character.character_id character_id FROM ...
        combinedCols.set(idColIndex, CharacterRepository.TABLE + "." + CHARACTER_ID + " " + CHARACTER_ID);

        combinedCols.addAll(subTableColsList);

        String[] colsArray = new String[combinedCols.size()];
        combinedCols.toArray(colsArray);
        return colsArray;
    }
}
