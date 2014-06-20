package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;
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

    public static final ImmutableBiMap<Integer, String> SKILL_KEY_STRINGS = buildSaveKeyStrings();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Save createObjectForElement(Element element) throws InvalidObjectException {
        String saveKeyString = getStringAttribute(element, SKILL_KEY_ATTR);
        ImmutableBiMap<String, Integer> saveKeyStrings = SKILL_KEY_STRINGS.inverse();
        Integer saveKey = saveKeyStrings.get(saveKeyString);
        if (saveKey == null) {
            throw new InvalidObjectException("Invalid save: " + saveKeyString);
        }

        String abilityKeyString = getStringAttribute(element, ABILITY_ATTR);
        ImmutableBiMap<String, Integer> abilityKeyStrings = AbilityXMLAdapter.ABILITY_KEY_STRINGS.inverse();
        Integer abilityKey = abilityKeyStrings.get(abilityKeyString);
        if (abilityKey == null) {
            throw new InvalidObjectException("Invalid ability \"" + abilityKeyString + "\" for save: " + saveKeyString);
        }

        Save save = new Save(saveKey, abilityKey);
        save.setBaseSave(getIntAttribute(element, BASE_ATTR));
        save.setMagicMod(getIntAttribute(element, MAGIC_ATTR));
        save.setMiscMod(getIntAttribute(element, MISC_ATTR));
        save.setTempMod(getIntAttribute(element, TEMP_ATTR));
        return save;
    }

    @Override
    protected void setElementContentForObject(Element element, Save save) {
        element.addAttribute(SKILL_KEY_ATTR, SKILL_KEY_STRINGS.get(save.getSaveKey()));
        element.addAttribute(ABILITY_ATTR, AbilityXMLAdapter.ABILITY_KEY_STRINGS.get(save.getAbilityKey()));
        element.addAttribute(BASE_ATTR, Integer.toString(save.getBaseSave()));
        element.addAttribute(MAGIC_ATTR, Integer.toString(save.getMagicMod()));
        element.addAttribute(MISC_ATTR, Integer.toString(save.getMiscMod()));
        element.addAttribute(TEMP_ATTR, Integer.toString(save.getTempMod()));
    }

    private static ImmutableBiMap<Integer, String> buildSaveKeyStrings() {
        ImmutableBiMap.Builder<Integer, String> builder = ImmutableBiMap.builder();
        builder.put(SaveSet.KEY_FORT, "FORT");
        builder.put(SaveSet.KEY_REF, "REF");
        builder.put(SaveSet.KEY_WILL, "WILL");
        return builder.build();
    }
}