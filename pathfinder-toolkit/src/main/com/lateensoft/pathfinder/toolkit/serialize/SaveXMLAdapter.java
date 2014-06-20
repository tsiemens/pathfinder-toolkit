package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveType;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class SaveXMLAdapter extends XMLObjectAdapter<Save> {

    public static final String ELEMENT_NAME = "save";
    private static final String SKILL_KEY_ATTR = "save-key";
    private static final String BASE_ATTR = "base-mod";
    private static final String ABILITY_ATTR = "ability";
    private static final String MAGIC_ATTR = "magic-mod";
    private static final String MISC_ATTR = "misc-mod";
    private static final String TEMP_ATTR = "temp-mod";

    public static final ImmutableBiMap<SaveType, String> SKILL_KEY_STRINGS = buildSaveKeyStrings();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Save createObjectForElement(Element element) throws InvalidObjectException {
        String saveKeyString = getStringAttribute(element, SKILL_KEY_ATTR);
        ImmutableBiMap<String, SaveType> saveKeyStrings = SKILL_KEY_STRINGS.inverse();
        SaveType saveType = saveKeyStrings.get(saveKeyString);
        if (saveType == null) {
            throw new InvalidObjectException("Invalid save: " + saveKeyString);
        }

        String abilityKeyString = getStringAttribute(element, ABILITY_ATTR);
        ImmutableBiMap<String, AbilityType> abilityKeyStrings = AbilityXMLAdapter.ABILITY_TYPE_STRINGS.inverse();
        AbilityType abilityType = abilityKeyStrings.get(abilityKeyString);
        if (abilityType == null) {
            throw new InvalidObjectException("Invalid ability \"" + abilityKeyString + "\" for save: " + saveKeyString);
        }

        Save save = new Save(saveType, abilityType);
        save.setBaseSave(getIntAttribute(element, BASE_ATTR));
        save.setMagicMod(getIntAttribute(element, MAGIC_ATTR));
        save.setMiscMod(getIntAttribute(element, MISC_ATTR));
        save.setTempMod(getIntAttribute(element, TEMP_ATTR));
        return save;
    }

    @Override
    protected void setElementContentForObject(Element element, Save save) {
        element.addAttribute(SKILL_KEY_ATTR, SKILL_KEY_STRINGS.get(save.getType()));
        element.addAttribute(ABILITY_ATTR, AbilityXMLAdapter.ABILITY_TYPE_STRINGS.get(save.getAbilityType()));
        element.addAttribute(BASE_ATTR, Integer.toString(save.getBaseSave()));
        element.addAttribute(MAGIC_ATTR, Integer.toString(save.getMagicMod()));
        element.addAttribute(MISC_ATTR, Integer.toString(save.getMiscMod()));
        element.addAttribute(TEMP_ATTR, Integer.toString(save.getTempMod()));
    }

    private static ImmutableBiMap<SaveType, String> buildSaveKeyStrings() {
        ImmutableBiMap.Builder<SaveType, String> builder = ImmutableBiMap.builder();
        builder.put(SaveType.FORT, "FORT");
        builder.put(SaveType.REF, "REF");
        builder.put(SaveType.WILL, "WILL");
        return builder.build();
    }
}