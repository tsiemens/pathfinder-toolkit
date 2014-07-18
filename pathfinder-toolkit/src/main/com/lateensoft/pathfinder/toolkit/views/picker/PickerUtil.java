package com.lateensoft.pathfinder.toolkit.views.picker;

import android.content.Context;
import android.content.Intent;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tsiemens
 */
public class PickerUtil {

    private static final String CHARACTERS_KEY = "characters";
    private static final String PARTY_KEY = "parties";

    public static class Builder {
        private Context context;
        private String title;
        private boolean isSingleChoice = false;
        private List<IdNamePair> pickableCharacters;
        private List<IdNamePair> selectedCharacters;

        private List<IdNamePair> pickableParties;
        private List<IdNamePair> selectedParties;

        private ArrayList<PickerList> pickerLists;

        public Builder(Context context) {
            this.context = context;
            pickerLists = Lists.newArrayList();
        }

        public Builder setTitle(int resId) {
            title = context.getString(resId);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSingleChoice(boolean isSingleChoice) {
            this.isSingleChoice = isSingleChoice;
            return this;
        }

        public Builder setPickableCharacters(List<IdNamePair> pickableCharacters, IdNamePair... selectedCharacters) {
            this.pickableCharacters = pickableCharacters;
            if (selectedCharacters.length > 0) {
                this.selectedCharacters = Lists.newArrayList(selectedCharacters);
            }
            return this;
        }

        public Builder setPickableCharacters(List<IdNamePair> pickableCharacters, List<IdNamePair> selectedCharacters) {
            this.pickableCharacters = pickableCharacters;
            this.selectedCharacters = selectedCharacters;
            return this;
        }

        public Builder setPickableParties(List<IdNamePair> pickableParties, IdNamePair... selectedParties) {
            this.pickableParties = pickableParties;
            if (selectedParties.length > 0) {
                this.selectedParties = Lists.newArrayList(selectedParties);
            }
            return this;
        }

        public Builder setPickableParties(List<IdNamePair> pickableParties, List<IdNamePair> selectedParties) {
            this.pickableParties = pickableParties;
            this.selectedParties = selectedParties;
            return this;
        }

        public Builder addPickableItems(String itemGroupKey, String name, List<IdNamePair> items, IdNamePair... selectedItems) {
            List<IdNamePair> selectedList = null;
            if (selectedItems.length > 0) {
                selectedList = Lists.newArrayList(selectedItems);
            }

            pickerLists.add(new PickerList(itemGroupKey, name, items, selectedList));
            return this;
        }

        public Builder addPickableItems(String itemGroupKey, String name, List<IdNamePair> items,
                                        List<IdNamePair> selectedItems) {
            pickerLists.add(new PickerList(itemGroupKey, name, items, selectedItems));
            return this;
        }

        public Intent build() {
            Intent intent = new Intent(context, PickerActivity.class);

            if (pickableCharacters != null) {
                pickerLists.add(new PickerList(CHARACTERS_KEY,
                        context.getString(R.string.picker_tab_title_characters),
                        pickableCharacters, selectedCharacters));
            }

            if (pickableParties != null) {
                pickerLists.add(new PickerList(PARTY_KEY,
                        context.getString(R.string.picker_tab_title_parties),
                        pickableParties, selectedParties));
            }

            intent.putParcelableArrayListExtra(PickerActivity.PICKER_LISTS_KEY, pickerLists);
            intent.putExtra(PickerActivity.TITLE_KEY, title);
            intent.putExtra(PickerActivity.IS_SINGLE_CHOICE_KEY, isSingleChoice);
            return intent;
        }
    }

    public static class ResultData {
        private Intent m_data;

        public ResultData(Intent data) {
            m_data = data;
        }

        public List<IdNamePair> getCharacters() {
            return getSelectedItemsForKey(CHARACTERS_KEY);
        }

        public IdNamePair getCharacter() {
            return getSingleReturnedItem(getCharacters());
        }

        public List<IdNamePair> getParties() {
            return getSelectedItemsForKey(PARTY_KEY);
        }

        public IdNamePair getParty() {
            return getSingleReturnedItem(getParties());
        }

        public List<IdNamePair> getSelectedItemsForKey(String key) {
            if (m_data != null) {
                return m_data.getParcelableArrayListExtra(key);
            } else {
                return null;
            }
        }

        public IdNamePair getSelectedItemForKey(String key) {
            return getSingleReturnedItem(getSelectedItemsForKey(key));
        }

        private IdNamePair getSingleReturnedItem(List<IdNamePair> items) {
            if (items == null || items.isEmpty()) {
                return null;
            } else {
                return items.get(0);
            }
        }
    }
}
