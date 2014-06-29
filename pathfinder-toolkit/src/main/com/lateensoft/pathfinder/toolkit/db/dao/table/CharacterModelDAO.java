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

    private FluffDAO fluffRepo;
    private AbilitySetDAO abilitySetDAO;
    private CombatStatDAO combatStatRepo;
    private SaveSetDAO saveSetDAO;
    private SkillSetDAO skillSetDAO;
    private ItemDAO itemRepo;
    private WeaponDAO weaponRepo;
    private ArmorDAO armorRepo;
    private FeatDAO featRepo;
    private SpellDAO spellRepo;

    public CharacterModelDAO(Context context) {
        super(context);
        fluffRepo = new FluffDAO(context);
        abilitySetDAO = new AbilitySetDAO(context);
        combatStatRepo = new CombatStatDAO(context);
        saveSetDAO = new SaveSetDAO(context);
        skillSetDAO = new SkillSetDAO(context);
        itemRepo = new ItemDAO(context);
        weaponRepo = new WeaponDAO(context);
        armorRepo = new ArmorDAO(context);
        featRepo = new FeatDAO(context);
        spellRepo = new SpellDAO(context);
    }

    @Override
    public Long add(PathfinderCharacter object) throws DataAccessException {
        long id = super.add(object);

        try {
            fluffRepo.add(id, object.getFluff());
            abilitySetDAO.add(id, object.getAbilitySet());
            combatStatRepo.add(id, object.getCombatStatSet());
            saveSetDAO.add(id, object.getSaveSet());
            skillSetDAO.add(id, object.getSkillSet());

            List<Item> items = object.getInventory().getItems();
            for (Item item : items) {
                itemRepo.add(id, item);
            }

            List<Weapon> weapons = object.getInventory().getWeapons();
            for (Weapon weapon : weapons) {
                weaponRepo.add(id, weapon);
            }

            List<Armor> armors = object.getInventory().getArmors();
            for (Armor armor : armors) {
                armorRepo.add(id, armor);
            }

            FeatList featList = object.getFeatList();
            for (Feat feat : featList) {
                featRepo.add(id, feat);
            }

            // Spells
            SpellBook spells = object.getSpellBook();
            for (Spell spell : spells) {
                spellRepo.add(id, spell);
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
                .setFluffInfo(fluffRepo.find(id, null))
                .setAbilitySet(abilitySetDAO.findSet(id))
                .setCombatStatSet(combatStatRepo.find(id, null))
                .setSaveSet(saveSetDAO.findSet(id))
                .setSkillSet(skillSetDAO.findSet(id));

        Inventory inventory = new Inventory();
        inventory.getItems().addAll(itemRepo.findAllForOwner(id));
        inventory.getWeapons().addAll(weaponRepo.findAllForOwner(id));
        inventory.getArmors().addAll(armorRepo.findAllForOwner(id));

        builder.setInventory(inventory)
                .setFeats(new FeatList(featRepo.findAllForOwner(id)))
                .setSpellBook(new SpellBook(spellRepo.findAllForOwner(id)));
    }
}
