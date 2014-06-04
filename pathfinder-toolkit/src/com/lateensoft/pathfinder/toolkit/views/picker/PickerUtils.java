package com.lateensoft.pathfinder.toolkit.views.picker;

import android.content.Context;
import android.content.Intent;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tsiemens
 */
public class PickerUtils {

    private static final String CHARACTERS_KEY = "characters";
    private static final String PARTY_KEY = "parties";

    public static class Builder {
        private Context m_context;
        private String m_title;
        private boolean m_isSingleChoice = false;
        private List<IdStringPair> m_pickableCharacters;
        private List<IdStringPair> m_pickableParties;

        public Builder(Context context) {
            m_context = context;
        }

        public Builder setTitle(int resId) {
            m_title = m_context.getString(resId);
            return this;
        }

        public Builder setTitle(String title) {
            m_title = title;
            return this;
        }

        public Builder setSingleChoice(boolean isSingleChoice) {
            m_isSingleChoice = isSingleChoice;
            return this;
        }

        public Builder setPickableCharacters(List<IdStringPair> pickableCharacters) {
            m_pickableCharacters = pickableCharacters;
            return this;
        }

        public Builder setPickableParties(List<IdStringPair> pickableParties) {
            m_pickableParties = pickableParties;
            return this;
        }

        public Intent build() {
            Intent intent = new Intent(m_context, PickerActivity.class);
            ArrayList<PickerList> pickerLists = Lists.newArrayList();

            if (m_pickableCharacters != null) {
                pickerLists.add(new PickerList(CHARACTERS_KEY,
                        m_context.getString(R.string.picker_tab_title_characters),
                        m_pickableCharacters));
            }

            if (m_pickableParties != null) {
                pickerLists.add(new PickerList(PARTY_KEY,
                        m_context.getString(R.string.picker_tab_title_parties),
                        m_pickableParties));
            }

            intent.putParcelableArrayListExtra(PickerActivity.PICKER_LISTS_KEY, pickerLists);
            intent.putExtra(PickerActivity.TITLE_KEY, m_title);
            intent.putExtra(PickerActivity.IS_SINGLE_CHOICE_KEY, m_isSingleChoice);
            return intent;
        }
    }

    public static class ResultData {
        private Intent m_data;

        public ResultData(Intent data) {
            m_data = data;
        }

        public List<IdStringPair> getCharacters() {
            return m_data.getParcelableArrayListExtra(CHARACTERS_KEY);
        }

        public IdStringPair getCharacter() {
            return getSingleReturnedItem(getCharacters());
        }

        public List<IdStringPair> getParties() {
            return m_data.getParcelableArrayListExtra(PARTY_KEY);
        }

        public IdStringPair getParty() {
            return getSingleReturnedItem(getParties());
        }

        private IdStringPair getSingleReturnedItem(List<IdStringPair> items) {
            if (items == null || items.isEmpty()) {
                return null;
            } else {
                return items.get(0);
            }
        }
    }
}
