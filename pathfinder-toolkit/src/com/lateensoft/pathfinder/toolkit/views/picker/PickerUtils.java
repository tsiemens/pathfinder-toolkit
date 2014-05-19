package com.lateensoft.pathfinder.toolkit.views.picker;

import android.content.Context;
import android.content.Intent;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tsiemens
 */
public class PickerUtils {

    private static final String CHARACTERS_KEY = "characters";
    private static final String PARTY_KEY = "parties";

    public static Intent createCharacterPickerActivityIntent(Context context, String pickerTitle, boolean isSingleChoice,
                                                             List<IdStringPair> pickableCharacters) {
        return createCharacterAndPartyPickerActivityIntent(context, pickerTitle, isSingleChoice, pickableCharacters, null);
    }


    public static Intent createPartyPickerActivityIntent(Context context, String pickerTitle, boolean isSingleChoice,
                                                         List<IdStringPair> pickableParties) {
        return createCharacterAndPartyPickerActivityIntent(context, pickerTitle, isSingleChoice, null, pickableParties);
    }

    public static Intent createCharacterAndPartyPickerActivityIntent(Context context, String pickerTitle,
                                                                     List<IdStringPair> pickableCharacters,
                                                                     List<IdStringPair> pickableParties) {
        return createCharacterAndPartyPickerActivityIntent(context, pickerTitle, false, pickableCharacters, pickableParties);
    }

    private static Intent createCharacterAndPartyPickerActivityIntent(Context context, String pickerTitle, boolean isSingleChoice,
                                                                     @Nullable List<IdStringPair> pickableCharacters,
                                                                     @Nullable List<IdStringPair> pickableParties) {
        Intent intent = new Intent(context, PickerActivity.class);
        ArrayList<PickerList> pickerLists = Lists.newArrayList();

        if (pickableCharacters != null) {
            pickerLists.add(new PickerList(CHARACTERS_KEY,
                    context.getString(R.string.picker_tab_title_characters),
                    pickableCharacters));
        }

        if (pickableCharacters != null) {
            pickerLists.add(new PickerList(PARTY_KEY,
                    context.getString(R.string.picker_tab_title_parties),
                    pickableParties));
        }

        intent.putParcelableArrayListExtra(PickerActivity.PICKER_LISTS_KEY, pickerLists);
        intent.putExtra(PickerActivity.TITLE_KEY, pickerTitle);
        intent.putExtra(PickerActivity.IS_SINGLE_CHOICE_KEY, isSingleChoice);
        return intent;
    }

    public static List<IdStringPair> getReturnedCharacters(Intent data) {
        return data.getParcelableArrayListExtra(CHARACTERS_KEY);
    }

    public static IdStringPair getReturnedCharacter(Intent data) {
        return getSingleReturnedItem(getReturnedCharacters(data));
    }

    public static List<IdStringPair> getReturnedParties(Intent data) {
        return data.getParcelableArrayListExtra(PARTY_KEY);
    }

    public static IdStringPair getReturnedParty(Intent data) {
        return getSingleReturnedItem(getReturnedParties(data));
    }

    private static IdStringPair getSingleReturnedItem(List<IdStringPair> items) {
        if (items == null || items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }
}
