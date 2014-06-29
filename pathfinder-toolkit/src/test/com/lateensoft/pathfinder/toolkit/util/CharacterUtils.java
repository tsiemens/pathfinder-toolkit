package com.lateensoft.pathfinder.toolkit.util;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.model.character.*;
import com.lateensoft.pathfinder.toolkit.model.character.items.*;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.Collections;

/**
 * @author trevsiemens
 */
public class CharacterUtils {

    public static PathfinderCharacter buildTestCharacter() {
        PathfinderCharacter.Builder builder = new PathfinderCharacter.Builder();
        populateTestCharacter(builder);
        PathfinderCharacter character = builder.build();
        sortCharacter(character);
        return character;
    }

    public static EncounterParticipant buildTestEncounterParticipant() {
        EncounterParticipant.Builder builder = new EncounterParticipant.Builder();
        populateTestParticipant(builder);
        EncounterParticipant participant = builder.build();
        sortCharacter(participant);
        return participant;
    }

    private static void populateTestParticipant(EncounterParticipant.Builder builder) {
        populateTestCharacter(builder);
        builder.setInitiativeScore(19);
        builder.setTurnOrder(3);
    }

    private static void populateTestCharacter(PathfinderCharacter.Builder builder) {
        builder.setName("test \nchar");
        long id = Identifiable.UNSET_ID;
        builder.setId(id);
        builder.setGold(1131415.4);

        // Abilities
        AbilitySet abilitySet = new AbilitySet();
        Ability ability = abilitySet.getAbilityAtIndex(0);
        ability.setScore(1);
        ability.setTempBonus(2);
        ability = abilitySet.getAbilityAtIndex(5);
        ability.setScore(3);
        ability.setTempBonus(-3);
        builder.setAbilitySet(abilitySet);

        // Items
        Inventory inventory = new Inventory();
        inventory.getItems().add(new Item(id, "Cauld\n ron", 5.0, 1, false));
        inventory.getItems().add(new Item(id, "Wands \n ", 1.3, 2, true));

        // Armor
        inventory.getArmors().add(newArmor(id, "Heavy \narmor",
                7.5, true, 1, /*ACP*/-2, 3, 4, 5, "armo\nr", Size.MEDIUM));
        inventory.getArmors().add(newArmor(id, "Hat",
                1.0, false, 10, /*ACP*/-4, 39, 11, 20, "Hat", Size.SMALL));

        // Weapons
        inventory.getWeapons().add(newWeapon(id, "Great \nSword",
                7.5, 5, "4/\n2", "x\n2", 5, "It's on \nfire!", 0, WeaponType.SLASHING, Size.LARGE));
        inventory.getWeapons().add(newWeapon(id, "Long Bow",
                4.0, 3, "5", "x3", 30, "None", 20, WeaponType.BLUDGEONING, Size.MEDIUM));
        builder.setInventory(inventory);

        // Combat stats
        CombatStatSet combatStatSet = new CombatStatSet();
        combatStatSet.setTotalHP(50);
        combatStatSet.setWounds(3);
        combatStatSet.setNonLethalDamage(5);
        combatStatSet.setDamageReduction(8);
        combatStatSet.setBaseSpeed(30);
        combatStatSet.setInitAbility(AbilityType.STR);
        combatStatSet.setInitiativeMiscMod(2);
        combatStatSet.setACArmourBonus(10);
        combatStatSet.setACShieldBonus(11);
        combatStatSet.setACAbility(AbilityType.INT);
        combatStatSet.setSizeModifier(13);
        combatStatSet.setNaturalArmour(14);
        combatStatSet.setDeflectionMod(15);
        combatStatSet.setACMiscMod(16);
        combatStatSet.setBABPrimary(17);
        combatStatSet.setBABSecondary("2/6/7");
        combatStatSet.setCMBAbility(AbilityType.CON);
        combatStatSet.setCMDAbility(AbilityType.WIS);
        combatStatSet.setCMDMiscMod(56);
        combatStatSet.setSpellResistance(67);
        builder.setCombatStatSet(combatStatSet);

        // Feats
        FeatList featList = new FeatList();
        featList.add(new Feat(id, "A \n Feat", "description\n 1"));
        featList.add(new Feat(id, "B Feat", ""));
        builder.setFeats(featList);

        // Fluff
        FluffInfo fluff = new FluffInfo();
        fluff.setAlignment("Chaotic \nEvil");
        fluff.setXP("100\n0");
        fluff.setNextLevelXP("4\n5");
        fluff.setPlayerClass("Rog\nue");
        fluff.setRace("Huma\nn");
        fluff.setDeity("Glo\nb");
        fluff.setLevel("2\n0");
        fluff.setSize("me\nd");
        fluff.setGender("M\ng");
        fluff.setHeight("5'\n11\"");
        fluff.setWeight("150 \nlbs");
        fluff.setEyes("Red\nfg");
        fluff.setHair("None\ndfg");
        fluff.setLanguages("Demon\n dfic");
        fluff.setDescription("Pretty much worse \nthan Sauron");
        builder.setFluffInfo(fluff);

        // Saves
        SaveSet saveSet = new SaveSet();
        Save save = saveSet.getSaveByIndex(0);
        save.setBaseSave(7);
        save.setAbilityType(AbilityType.DEX);
        save.setMagicMod(3);
        save.setMiscMod(5);
        save.setTempMod(-11);
        builder.setSaveSet(saveSet);

        // Skills
        SkillSet skillSet = new SkillSet();
        Skill skill = skillSet.getSkillByIndex(0);
        skill.setMiscMod(3);
        skill.setRank(2);
        skill.setClassSkill(true);
        skill.setAbility(AbilityType.WIS);

        skillSet.addNewSubSkill(SkillType.CRAFT).setSubType("slkdfj\nsjdhf");
        builder.setSkillSet(skillSet);

        // Spells
        SpellBook spellBook = new SpellBook();
        spellBook.add(new Spell(id, "B\n Spell", 1, 4, "description\n 1"));
        spellBook.add(new Spell(id, "A Spell", 2, 5, "description 2"));
        builder.setSpellBook(spellBook);
    }

    private static Weapon newWeapon(long characterId, String name,
                                 double weight, int totalAttackB, String dmg, String crit, int range, String specProp,
                                 int ammo, WeaponType type, Size size) {
        Weapon weapon = new Weapon();
        weapon.setCharacterID(characterId);
        weapon.setName(name);
        weapon.setWeight(weight);
        weapon.setTotalAttackBonus(totalAttackB);
        weapon.setDamage(dmg);
        weapon.setCritical(crit);
        weapon.setRange(range);
        weapon.setSpecialProperties(specProp);
        weapon.setAmmunition(ammo);
        weapon.setType(type);
        weapon.setSize(size);
        return weapon;
    }

    private static Armor newArmor(long characterId, String name,
                                 double weight, boolean worn, int ACBonus, int checkPen, int maxDex, int spellFail,
                                 int speed, String specProp, Size size) {
        Armor armor = new Armor();
        armor.setCharacterID(characterId);
        armor.setName(name);
        armor.setWeight(weight);
        armor.setWorn(worn);
        armor.setACBonus(ACBonus);
        armor.setCheckPen(checkPen);
        armor.setMaxDex(maxDex);
        armor.setSpellFail(spellFail);
        armor.setSpeed(speed);
        armor.setSpecialProperties(specProp);
        armor.setSize(size);
        return armor;
    }

    private static void sortCharacter(PathfinderCharacter character) {
        Collections.sort(character.getFeatList());
        Collections.sort(character.getSpellBook());
        Collections.sort(character.getInventory().getItems());
        Collections.sort(character.getInventory().getWeapons());
        Collections.sort(character.getInventory().getArmors());
        Collections.sort(character.getSkillSet().getSkills());
    }
}
