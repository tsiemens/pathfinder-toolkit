package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class FluffXMLAdapter extends XMLObjectAdapter<FluffInfo> {

    public static final String ELEMENT_NAME = "fluff";
    private static final String NAME_ATTR = "name";
    private static final String ALIGN_ATTR = "alignment";
    private static final String XP_ATTR = "xp";
    private static final String NEXT_XP_ATTR = "next-level-xp";
    private static final String CLASS_ATTR = "class";
    private static final String RACE_ATTR = "race";
    private static final String DEITY_ATTR = "deity";
    private static final String LEVEL_ATTR = "level";
    private static final String SIZE_ATTR = "size";
    private static final String GENDER_ATTR = "gender";
    private static final String HEIGHT_ATTR = "height";
    private static final String WEIGHT_ATTR = "weight";
    private static final String EYES_ATTR = "eyes";
    private static final String HAIR_ATTR = "hair";
    private static final String LANGS_ATTR = "languages";
    private static final String DESC_ATTR = "desc";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected void setElementContentForObject(Element element, FluffInfo fluff) {
        element.addAttribute(NAME_ATTR, fluff.getName());
        element.addAttribute(ALIGN_ATTR, fluff.getAlignment());
        element.addAttribute(XP_ATTR, fluff.getXP());
        element.addAttribute(NEXT_XP_ATTR, fluff.getNextLevelXP());
        element.addAttribute(CLASS_ATTR, fluff.getPlayerClass());
        element.addAttribute(RACE_ATTR, fluff.getRace());
        element.addAttribute(DEITY_ATTR, fluff.getDeity());
        element.addAttribute(LEVEL_ATTR, fluff.getLevel());
        element.addAttribute(SIZE_ATTR, fluff.getSize());
        element.addAttribute(GENDER_ATTR, fluff.getGender());
        element.addAttribute(HEIGHT_ATTR, fluff.getHeight());
        element.addAttribute(WEIGHT_ATTR, fluff.getWeight());
        element.addAttribute(EYES_ATTR, fluff.getEyes());
        element.addAttribute(HAIR_ATTR, fluff.getHair());
        element.addAttribute(LANGS_ATTR, fluff.getLanguages());
        element.addAttribute(DESC_ATTR, fluff.getDescription());
    }

    @Override
    protected FluffInfo createObjectForElement(Element element) throws InvalidObjectException {
        FluffInfo fluff = new FluffInfo();
        fluff.setName(getStringAttribute(element, NAME_ATTR));
        fluff.setAlignment(getStringAttribute(element, ALIGN_ATTR));
        fluff.setXP(getStringAttribute(element, XP_ATTR));
        fluff.setNextLevelXP(getStringAttribute(element, NEXT_XP_ATTR));
        fluff.setPlayerClass(getStringAttribute(element, CLASS_ATTR));
        fluff.setRace(getStringAttribute(element, RACE_ATTR));
        fluff.setDeity(getStringAttribute(element, DEITY_ATTR));
        fluff.setLevel(getStringAttribute(element, LEVEL_ATTR));
        fluff.setSize(getStringAttribute(element, SIZE_ATTR));
        fluff.setGender(getStringAttribute(element, GENDER_ATTR));
        fluff.setHeight(getStringAttribute(element, HEIGHT_ATTR));
        fluff.setWeight(getStringAttribute(element, WEIGHT_ATTR));
        fluff.setEyes(getStringAttribute(element, EYES_ATTR));
        fluff.setHair(getStringAttribute(element, HAIR_ATTR));
        fluff.setLanguages(getStringAttribute(element, LANGS_ATTR));
        fluff.setDescription(getStringAttribute(element, DESC_ATTR));
        return fluff;
    }
}
