package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;

import java.util.List;

public class AbilitySelectionDialog {

    private OnAbilitySelectedListener m_listener;
    private AlertDialog.Builder builder;
    
    public AbilitySelectionDialog(Context context, AbilityType checkedAbility, AbilityType defaultAbility) {
        builder = new AlertDialog.Builder(context);
        Resources r = builder.getContext().getResources();
        String[] abilityNames = AbilityType.getKeySortedNames(r);
        
        // Making the default visible to user
        int defaultAbilityIndex = getIndexForAbilityKey(defaultAbility);
        abilityNames[defaultAbilityIndex] = abilityNames[defaultAbilityIndex] +
                r.getString(R.string.default_ability_label);
        
        OnClickListener clickListener = new OnClickListener() {    
            private AbilityType selectedAbility = null;
            
            @Override public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (m_listener != null) {
                        m_listener.onAbilitySelected(selectedAbility);
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                default:
                    selectedAbility = AbilityType.values()[which];
                    break;

                }
            }
        };
        builder.setSingleChoiceItems(abilityNames,
                getIndexForAbilityKey(checkedAbility), clickListener);
        builder.setTitle(R.string.select_ability_dialog_title);
        builder.setPositiveButton(R.string.ok_button_text, clickListener);
        builder.setNegativeButton(R.string.cancel_button_text, clickListener);
    }
    
    public AbilitySelectionDialog setOnAbilitySelectedListener(OnAbilitySelectedListener listener) {
        m_listener = listener;
        return this;
    }
    
    public void show() {
        builder.show();
    }

    protected int getIndexForAbilityKey(AbilityType abilityType) {
        List<AbilityType> abilities = AbilityType.getKeySortedValues();
        for (int i = 0; i < abilities.size(); i++) {
            if (abilities.get(i) == abilityType) {
                return i;
            }
        }
        return -1;
    }
    
    public static interface OnAbilitySelectedListener {
        
        /** @param abilityType the id of the ability selected, or null if none selected. */
        public void onAbilitySelected(AbilityType abilityType);
    }
}
