package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.set.AbilitySetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.set.SaveSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.set.SkillSetDAO;
import com.lateensoft.pathfinder.toolkit.model.character.*;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

import java.util.Hashtable;
import java.util.List;

public class CharacterModelDAO extends AbstractCharacterDAO<PathfinderCharacter> {
    public static final String TABLE = "Character";

    protected static final String CHARACTER_ID = "character_id";
    protected static final String GOLD = "Gold";

    private FluffDAO fluffDAO;
    private AbilitySetDAO abilitySetDAO;
    private CombatStatDAO combatStatDAO;
    private SaveSetDAO saveSetDAO;
    private SkillSetDAO skillSetDAO;
    private ItemDAO itemDAO;
    private WeaponDAO weaponDAO;
    private ArmorDAO armorDAO;
    private FeatDAO featDAO;
    private SpellDAO spellDAO;

    public CharacterModelDAO(Context context) {
        super(context);
        fluffDAO = new FluffDAO(context);
        abilitySetDAO = new AbilitySetDAO(context);
        combatStatDAO = new CombatStatDAO(context);
        saveSetDAO = new SaveSetDAO(context);
        skillSetDAO = new SkillSetDAO(context);
        itemDAO = new ItemDAO(context);
        weaponDAO = new WeaponDAO(context);
        armorDAO = new ArmorDAO(context);
        featDAO = new FeatDAO(context);
        spellDAO = new SpellDAO(context);
    }

    @Override
    public Long add(PathfinderCharacter object) throws DataAccessException {
        long id = super.add(object);

        try {
            fluffDAO.add(id, object.getFluff());
            abilitySetDAO.add(id, object.getAbilitySet());
            combatStatDAO.add(id, object.getCombatStatSet());
            saveSetDAO.add(id, object.getSaveSet());
            skillSetDAO.add(id, object.getSkillSet());

            List<Item> items = object.getInventory().getItems();
            for (Item item : items) {
                itemDAO.add(id, item);
            }

            List<Weapon> weapons = object.getInventory().getWeapons();
            for (Weapon weapon : weapons) {
                weaponDAO.add(id, weapon);
            }

            List<Armor> armors = object.getInventory().getArmors();
            for (Armor armor : armors) {
                armorDAO.add(id, armor);
            }

            FeatList featList = object.getFeatList();
            for (Feat feat : featList) {
                featDAO.add(id, feat);
            }

            SpellBook spells = object.getSpellBook();
            for (Spell spell : spells) {
                spellDAO.add(id, spell);
            }
        } catch (DataAccessException e) {
            removeById(id);
            throw new DataAccessException("Failed to insert " + id, e);
        }

        return id;
    }

    @Override
    protected ContentValues getContentValues(PathfinderCharacter object) {
        ContentValues values = new ContentValues();
        if (isIdSet(object)) {
            values.put(CHARACTER_ID, object.getId());
        }
        values.put(NAME, object.getName());
        values.put(GOLD, object.getGold());
        return values;
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
                .setFluffInfo(fluffDAO.find(id))
                .setAbilitySet(abilitySetDAO.findSet(id))
                .setCombatStatSet(combatStatDAO.find(id))
                .setSaveSet(saveSetDAO.findSet(id))
                .setSkillSet(skillSetDAO.findSet(id));

        Inventory inventory = new Inventory();
        inventory.getItems().addAll(itemDAO.findAllForOwner(id));
        inventory.getWeapons().addAll(weaponDAO.findAllForOwner(id));
        inventory.getArmors().addAll(armorDAO.findAllForOwner(id));

        builder.setInventory(inventory)
                .setFeats(new FeatList(featDAO.findAllForOwner(id)))
                .setSpellBook(new SpellBook(spellDAO.findAllForOwner(id)));
    }
}
