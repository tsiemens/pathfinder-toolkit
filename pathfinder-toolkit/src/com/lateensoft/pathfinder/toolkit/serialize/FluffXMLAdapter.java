package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class FluffXMLAdapter extends XMLObjectAdapter<FluffInfo> {

    public static final String ELEMENT_NAME = "fluff";
    private static final String NAME_ELMT = "name";
    private static final String ALIGN_ELMT = "alignment";
    private static final String XP_ELMT = "xp";
    private static final String NEXT_XP_ELMT = "next-level-xp";
    private static final String CLASS_ELMT = "class";
    private static final String RACE_ELMT = "race";
    private static final String DEITY_ELMT = "deity";
    private static final String LEVEL_ELMT = "level";
    private static final String SIZE_ELMT = "size";
    private static final String GENDER_ELMT = "gender";
    private static final String HEIGHT_ELMT = "height";
    private static final String WEIGHT_ELMT = "weight";
    private static final String EYES_ELMT = "eyes";
    private static final String HAIR_ELMT = "hair";
    private static final String LANGS_ELMT = "languages";
    private static final String DESC_ELMT = "desc";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected void setElementContentForObject(Element element, FluffInfo fluff) {
        addSubElementText(element, NAME_ELMT, fluff.getName());
        addSubElementText(element, ALIGN_ELMT, fluff.getAlignment());
        addSubElementText(element, XP_ELMT, fluff.getXP());
        addSubElementText(element, NEXT_XP_ELMT, fluff.getNextLevelXP());
        addSubElementText(element, CLASS_ELMT, fluff.getPlayerClass());
        addSubElementText(element, RACE_ELMT, fluff.getRace());
        addSubElementText(element, DEITY_ELMT, fluff.getDeity());
        addSubElementText(element, LEVEL_ELMT, fluff.getLevel());
        addSubElementText(element, SIZE_ELMT, fluff.getSize());
        addSubElementText(element, GENDER_ELMT, fluff.getGender());
        addSubElementText(element, HEIGHT_ELMT, fluff.getHeight());
        addSubElementText(element, WEIGHT_ELMT, fluff.getWeight());
        addSubElementText(element, EYES_ELMT, fluff.getEyes());
        addSubElementText(element, HAIR_ELMT, fluff.getHair());
        addSubElementText(element, LANGS_ELMT, fluff.getLanguages());
        addSubElementText(element, DESC_ELMT, fluff.getDescription());
    }

    @Override
    protected FluffInfo createObjectForElement(Element element) throws InvalidObjectException {
        FluffInfo fluff = new FluffInfo();
        fluff.setName(getSubElementText(element, NAME_ELMT));
        fluff.setAlignment(getSubElementText(element, ALIGN_ELMT));
        fluff.setXP(getSubElementText(element, XP_ELMT));
        fluff.setNextLevelXP(getSubElementText(element, NEXT_XP_ELMT));
        fluff.setPlayerClass(getSubElementText(element, CLASS_ELMT));
        fluff.setRace(getSubElementText(element, RACE_ELMT));
        fluff.setDeity(getSubElementText(element, DEITY_ELMT));
        fluff.setLevel(getSubElementText(element, LEVEL_ELMT));
        fluff.setSize(getSubElementText(element, SIZE_ELMT));
        fluff.setGender(getSubElementText(element, GENDER_ELMT));
        fluff.setHeight(getSubElementText(element, HEIGHT_ELMT));
        fluff.setWeight(getSubElementText(element, WEIGHT_ELMT));
        fluff.setEyes(getSubElementText(element, EYES_ELMT));
        fluff.setHair(getSubElementText(element, HAIR_ELMT));
        fluff.setLanguages(getSubElementText(element, LANGS_ELMT));
        fluff.setDescription(getSubElementText(element, DESC_ELMT));
        return fluff;
    }
}
