package com.lateensoft.pathfinder.toolkit.test.util;

import android.graphics.Path;
import android.test.AndroidTestCase;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.util.ImportExportUtils;
import org.dom4j.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author trevsiemens
 */
public class ImportExportUtilsTest extends AndroidTestCase {

    public void testImportExport() throws IOException, DocumentException {
        PathfinderCharacter fullCharacter = buildTestCharacter();
        PathfinderCharacter defaultCharacter = new PathfinderCharacter("");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImportExportUtils.exportCharactersToStream(Lists.newArrayList(fullCharacter, defaultCharacter), os);
        String xmlText = new String(os.toByteArray());

        InputStream is = new ByteArrayInputStream(xmlText.getBytes());
        List<PathfinderCharacter> characters =ImportExportUtils.importCharactersFromStream(is);

        assertEquals(2, characters.size());
        assertEquals(fullCharacter, characters.get(0));
        assertEquals(defaultCharacter, characters.get(1));
    }

    private static PathfinderCharacter buildTestCharacter() {
        PathfinderCharacter character = new PathfinderCharacter("test \nchar");
        long id = character.getID();
        character.setGold(1131415.4);

        // Abilities
        Ability ability = character.getAbilitySet().getAbilityAtIndex(0);
        ability.setScore(1);
        ability.setTempBonus(2);
        ability = character.getAbilitySet().getAbilityAtIndex(5);
        ability.setScore(3);
        ability.setTempBonus(-3);

        // Items
        character.getInventory().getItems().add(new Item(id, "Cauld\n ron", 5.0, 1, false));
        character.getInventory().getItems().add(new Item(id, "Wands \n ", 1.3, 2, true));

        // Armor
        character.getInventory().getArmors().add(newArmor(id, "Heavy \narmor",
                7.5, true, 1, /*ACP*/-2, 3, 4, 5, "armo\nr", "M"));
        character.getInventory().getArmors().add(newArmor(id, "Hat",
                1.0, false, 10, /*ACP*/-4, 39, 11, 20, "Hat", "S"));

        // Weapons
        character.getInventory().getWeapons().add(newWeapon(id, "Great \nSword",
                7.5, 5, "4/\n2", "x\n2", 5, "It's on \nfire!", 0, "S", "L"));
        character.getInventory().getWeapons().add(newWeapon(id, "Long Bow",
                4.0, 3, "5", "x3", 30, "None", 20, "B", "M"));

        // Combat stats
        CombatStatSet combatStatSet = character.getCombatStatSet();
        combatStatSet.setTotalHP(50);
        combatStatSet.setWounds(3);
        combatStatSet.setNonLethalDamage(5);
        combatStatSet.setDamageReduction(8);
        combatStatSet.setBaseSpeed(30);
        combatStatSet.setInitAbilityKey(1);
        combatStatSet.setInitiativeMiscMod(2);
        combatStatSet.setACArmourBonus(10);
        combatStatSet.setACShieldBonus(11);
        combatStatSet.setACAbilityKey(4);
        combatStatSet.setSizeModifier(13);
        combatStatSet.setNaturalArmour(14);
        combatStatSet.setDeflectionMod(15);
        combatStatSet.setACMiscMod(16);
        combatStatSet.setBABPrimary(17);
        combatStatSet.setBABSecondary("2/6/7");
        combatStatSet.setCMBAbilityKey(2);
        combatStatSet.setCMDAbilityKey(5);
        combatStatSet.setCMDMiscMod(56);
        combatStatSet.setSpellResistance(67);

        // Feats
        character.getFeatList().add(new Feat(id, "A \n Feat", "description\n 1"));
        character.getFeatList().add(new Feat(id, "B Feat", ""));

        // Fluff
        FluffInfo fluff = character.getFluff();
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

        // Saves
        Save save = character.getSaveSet().getSaveByIndex(0);
        save.setBaseSave(7);
        save.setAbilityKey(2);
        save.setMagicMod(3);
        save.setMiscMod(5);
        save.setTempMod(-11);

        // Skills
        Skill skill = character.getSkillSet().getSkillByIndex(0);
        skill.setMiscMod(3);
        skill.setRank(2);
        skill.setClassSkill(true);
        skill.setAbilityKey(5);

        character.getSkillSet().addNewSubSkill(SkillSet.CRAFT).setSubType("slkdfj\nsjdhf");

        // Spells
        character.getSpellBook().add(new Spell(id, "B\n Spell", 1, 4, "description\n 1"));
        character.getSpellBook().add(new Spell(id, "A Spell", 2, 5, "description 2"));

        sortCharacter(character);
        return character;
    }

    private static Weapon newWeapon(long characterId, String name,
                                 double weight, int totalAttackB, String dmg, String crit, int range, String specProp,
                                 int ammo, String type, String size) {
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
                                 int speed, String specProp, String size) {
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
