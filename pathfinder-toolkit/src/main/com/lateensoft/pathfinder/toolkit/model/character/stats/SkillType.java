package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;

import java.util.Collections;
import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType.*;

public enum SkillType {
    ACRO            (1,  DEX, R.string.skill_name_acro),
    APPRAISE        (2,  INT, R.string.skill_name_appraise),
    BLUFF           (3,  CHA, R.string.skill_name_bluff),
    CLIMB           (4,  STR, R.string.skill_name_climb),
    CRAFT           (5,  INT, R.string.skill_name_craft),
    DIPLOM          (6,  CHA, R.string.skill_name_diplom),
    DISABLE_DEV     (7,  DEX, R.string.skill_name_disable_dev),
    DISGUISE        (8,  CHA, R.string.skill_name_disguise),
    ESCAPE          (9,  DEX, R.string.skill_name_escape),
    FLY             (10, DEX, R.string.skill_name_fly),
    HANDLE_ANIMAL   (11, CHA, R.string.skill_name_handle_animal),
    HEAL            (12, WIS, R.string.skill_name_heal),
    INTIMIDATE      (13, CHA, R.string.skill_name_intimidate),
    KNOW_ARCANA     (14, INT, R.string.skill_name_know_arcana),
    KNOW_DUNGEON    (15, INT, R.string.skill_name_know_dungeon),
    KNOW_ENG        (16, INT, R.string.skill_name_know_eng),
    KNOW_GEO        (17, INT, R.string.skill_name_know_geo),
    KNOW_HIST       (18, INT, R.string.skill_name_know_hist),
    KNOW_LOCAL      (19, INT, R.string.skill_name_know_local),
    KNOW_NATURE     (20, INT, R.string.skill_name_know_nature),
    KNOW_NOBILITY   (21, INT, R.string.skill_name_know_nobility),
    KNOW_PLANES     (22, INT, R.string.skill_name_know_planes),
    KNOW_RELIGION   (23, INT, R.string.skill_name_know_religion),
    LING            (24, INT, R.string.skill_name_ling),
    PERCEPT         (25, WIS, R.string.skill_name_percept),
    PERFORM         (26, CHA, R.string.skill_name_perform),
    PROF            (27, WIS, R.string.skill_name_prof),
    RIDE            (28, DEX, R.string.skill_name_ride),
    SENSE_MOTIVE    (29, WIS, R.string.skill_name_sense_motive),
    SLEIGHT_OF_HAND (30, DEX, R.string.skill_name_sleight_of_hand),
    SPELLCRAFT      (31, INT, R.string.skill_name_spellcraft),
    STEALTH         (32, DEX, R.string.skill_name_stealth),
    SURVIVAL        (33, WIS, R.string.skill_name_survival),
    SWIM            (34, STR, R.string.skill_name_swim),
    USE_MAGIC_DEVICE(35, CHA, R.string.skill_name_use_magic_device);

    private final int key;
    private final AbilityType defaultAbility;
    private final int nameResId;

    SkillType(int key, AbilityType defaultAbility, int nameResId) {
        this.key = key;
        this.defaultAbility = defaultAbility;
        this.nameResId = nameResId;
    }

    public int getKey() {
        return key;
    }

    public AbilityType getDefaultAbility() {
        return defaultAbility;
    }

    public int getNameResId() {
        return nameResId;
    }

    public boolean canBeSubTyped() {
        return  this == CRAFT ||
                this == PERFORM ||
                this == PROF;
    }

    public static List<SkillType> getKeySortedValues() {
        List<SkillType> types = Lists.newArrayList(SkillType.values());
        Collections.sort(types);
        return types;
    }

    public static SkillType forKey(int key) {
        for (SkillType type : SkillType.values()) {
            if (type.getKey() == key) {
                return type;
            }
        }
        throw new IllegalArgumentException(Integer.toString(key) +
                " is not a valid " + SkillType.class.getSimpleName());
    }
}
