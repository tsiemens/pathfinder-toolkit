package com.lateensoft.pathfinder.toolkit.views.character;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.set.AbilitySetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.set.SaveSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CombatStatDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.views.SimpleValueEditorDialog;
import roboguice.inject.InjectView;

import java.util.List;
import java.util.Map;

public class CharacterCombatStatsFragment extends AbstractCharacterSheetFragment {
    private static final String TAG = CharacterCombatStatsFragment.class.getSimpleName();

    private enum AbilityProperty { INIT, AC, CMB, CMD }

    private enum VariableProperty {
        TOTAL_HP(R.string.combat_stats_total_hp_editor_title),
        WOUNDS(R.string.combat_stats_wounds_editor_title),
        NON_LETHAL_DMG(R.string.combat_stats_non_lethal_editor_title),
        DMG_REDUCT(R.string.combat_stats_damage_reduction_editor_title),
        BASE_SPEED(R.string.combat_stats_base_speed_editor_title),
        INIT_MISC_MOD(R.string.combat_stats_misc_mod),
        AC_ARMOR(R.string.combat_stats_armour),
        AC_SHIELD(R.string.combat_stats_shield),
        SIZE_MOD(R.string.combat_stats_size_mod),
        AC_NATURAL_ARMOR(R.string.combat_stats_natural_armour),
        DEFLECT_MOD(R.string.combat_stats_deflect_mod),
        AC_MISC_MOD(R.string.combat_stats_misc_mod),
        SPELL_RESIST(R.string.combat_stats_spell_resist_editor_title),
        BAB_PRIMARY(R.string.combat_stats_bab_primary_editor_title),
        BAB_SECONDARY(R.string.combat_stats_bab_secondary_editor_title),
        CMD_MISC_MOD(R.string.combat_stats_misc_mod);

        private final int NameResId;

        VariableProperty(int nameResId) {
            this.NameResId = nameResId;
        }

        public boolean isStringStat() { return this == BAB_SECONDARY; }

        public int getNameResId() {
            return NameResId;
        }
    }

    private enum SaveProperty {
        BASE(R.string.combat_stats_base),
        MAGIC(R.string.combat_stats_magic_mod),
        MISC(R.string.combat_stats_misc_mod),
        TEMP(R.string.combat_stats_temp_mod);

        private final int nameResId;

        SaveProperty(int nameResId) {
            this.nameResId = nameResId;
        }

        public int getNameResId() {
            return nameResId;
        }
    }

    private @InjectView(R.id.tv_current_hp) TextView currentHPLabel;
    private @InjectView(R.id.tv_initiative) TextView initLabel;
    private @InjectView(R.id.tv_ac) TextView ACLabel;
    private @InjectView(R.id.tv_touch_ac) TextView ACTouchLabel;
    private @InjectView(R.id.tv_flat_footed_ac) TextView ACFlatFootedLabel;
    private @InjectView(R.id.tv_cmb) TextView CMBLabel;
    private @InjectView(R.id.tv_cmd) TextView CMDLabel;

    private Map<VariableProperty, List<TextView>> variablePropertyFields;
    private Map<AbilityProperty, AbilityView> abilityPropertyFields;
    private List<ComputedCombatStat> computedStats;

    private Map<SaveType, SaveRow> saveRows;

    private CombatStatDAO combatStatsDao;
    private SaveSetDAO saveSetDao;
    private AbilitySetDAO abilitySetDao;
    private ArmorDAO armorDao;
    
    private CombatStatSet combatStats;
    private SaveSet saveSet;
    private AbilitySet abilitySet;
    private int maxDex;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        combatStatsDao = new CombatStatDAO(context);
        saveSetDao = new SaveSetDAO(context);
        abilitySetDao = new AbilitySetDAO(context);
        armorDao = new ArmorDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRootView(inflater.inflate(
                R.layout.character_combat_stats_fragment, container, false));

        return getRootView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        initCombatStatViews();
        initSaveRows();
        initComputedStats();
    }

    private void initCombatStatViews() {
        View root = getRootView();
        variablePropertyFields = Maps.newHashMap();
        variablePropertyFields.put(VariableProperty.TOTAL_HP, findTextViews(root, R.id.tv_total_hp_val));
        variablePropertyFields.put(VariableProperty.WOUNDS, findTextViews(root, R.id.tv_wounds_val));
        variablePropertyFields.put(VariableProperty.NON_LETHAL_DMG, findTextViews(root, R.id.tv_non_lethal_dmg_val));
        variablePropertyFields.put(VariableProperty.DMG_REDUCT, findTextViews(root, R.id.tv_dmg_reduct_val));
        variablePropertyFields.put(VariableProperty.BASE_SPEED, findTextViews(root, R.id.tv_base_speed_val));
        variablePropertyFields.put(VariableProperty.INIT_MISC_MOD, findTextViews(root, R.id.tv_init_misc_val));
        variablePropertyFields.put(VariableProperty.AC_ARMOR, findTextViews(root, R.id.tv_ac_armor_val));
        variablePropertyFields.put(VariableProperty.AC_SHIELD, findTextViews(root, R.id.tv_ac_shield_val));
        variablePropertyFields.put(VariableProperty.SIZE_MOD, findTextViews(root, R.id.tv_ac_size_mod_val, R.id.tv_cmb_size_mod_val));
        variablePropertyFields.put(VariableProperty.AC_NATURAL_ARMOR, findTextViews(root, R.id.tv_ac_natural_armor_val));
        variablePropertyFields.put(VariableProperty.DEFLECT_MOD, findTextViews(root, R.id.tv_deflect_mod_val));
        variablePropertyFields.put(VariableProperty.AC_MISC_MOD, findTextViews(root, R.id.tv_ac_misc_val));
        variablePropertyFields.put(VariableProperty.SPELL_RESIST, findTextViews(root, R.id.tv_spell_resist_val));
        variablePropertyFields.put(VariableProperty.BAB_PRIMARY, findTextViews(root, R.id.tv_bab_primary_val, R.id.tv_cmb_bab_val));
        variablePropertyFields.put(VariableProperty.BAB_SECONDARY, findTextViews(root, R.id.tv_bab_secondary_val));
        variablePropertyFields.put(VariableProperty.CMD_MISC_MOD, findTextViews(root, R.id.tv_cmd_misc_mod_val));

        for (VariableProperty property : variablePropertyFields.keySet()) {
            for (TextView tv : variablePropertyFields.get(property)) {
                tv.setTag(property);
                tv.setOnClickListener(numberValueTextListener);
            }
        }

        abilityPropertyFields = Maps.newHashMap();
        abilityPropertyFields.put(AbilityProperty.INIT, abilityViewFrom(root, R.id.tv_init_ability));
        abilityPropertyFields.put(AbilityProperty.AC, abilityViewFrom(root, R.id.tv_ac_ability));
        abilityPropertyFields.put(AbilityProperty.CMB, abilityViewFrom(root, R.id.tv_cmb_ability));
        abilityPropertyFields.put(AbilityProperty.CMD, abilityViewFrom(root, R.id.tv_cmd_ability));

        for (AbilityProperty property : abilityPropertyFields.keySet()) {
            AbilityView abilityView = abilityPropertyFields.get(property);
            abilityView.setTextViewTag(property);
            abilityView.setOnClickListener(combatStatAbilityTextListener);
        }
    }

    private static List<TextView> findTextViews(View v, int... ids) {
        List<TextView> textViews = Lists.newArrayListWithCapacity(ids.length);
        for (int id : ids) {
            textViews.add((TextView) v.findViewById(id));
        }
        return textViews;
    }

    private AbilityView abilityViewFrom(View parent, int id) {
        return new AbilityView((TextView) parent.findViewById(id));
    }

    private OnClickListener numberValueTextListener = new OnClickListener() {
        @Override public void onClick(View v) {
            showValueEditorForProperty(getPropertyForTextView((TextView) v));
        }
    };

    private VariableProperty getPropertyForTextView(TextView v) {
        return (VariableProperty) v.getTag();
    }

    private void showValueEditorForProperty(VariableProperty property) {
        if (property == null) return;
        SimpleValueEditorDialog.builder(getContext())
                .forType(property.isStringStat() ? SimpleValueEditorDialog.ValueType.TEXT
                        : SimpleValueEditorDialog.ValueType.NUMBER_SIGNED)
                .withTitle(property.getNameResId())
                .withInitialValue(getTextValueForProperty(property))
                .withOnFinishedListener(new NumberValueEditListener(property))
                .build()
                .show();
    }

    private String getTextValueForProperty(VariableProperty property) {
        if (property == VariableProperty.BAB_SECONDARY) {
            return combatStats.getBABSecondary();
        } else {
            int val;
            switch (property) {
                case TOTAL_HP:          val = combatStats.getTotalHP();           break;
                case WOUNDS:            val = combatStats.getWounds();            break;
                case NON_LETHAL_DMG:    val = combatStats.getNonLethalDamage();   break;
                case DMG_REDUCT:        val = combatStats.getDamageReduction();   break;
                case BASE_SPEED:        val = combatStats.getBaseSpeed();         break;
                case INIT_MISC_MOD:     val = combatStats.getInitiativeMiscMod(); break;
                case AC_ARMOR:          val = combatStats.getACArmourBonus();     break;
                case AC_SHIELD:         val = combatStats.getACShieldBonus();     break;
                case SIZE_MOD:          val = combatStats.getSizeModifier();      break;
                case AC_NATURAL_ARMOR:  val = combatStats.getNaturalArmour();     break;
                case DEFLECT_MOD:       val = combatStats.getDeflectionMod();     break;
                case AC_MISC_MOD:       val = combatStats.getACMiscMod();         break;
                case SPELL_RESIST:      val = combatStats.getSpellResist();       break;
                case BAB_PRIMARY:       val = combatStats.getBABPrimary();        break;
                case CMD_MISC_MOD:      val = combatStats.getCMDMiscMod();        break;
                default: throw new IllegalArgumentException("Unexpected property: " + property);
            }
            return Integer.toString(val);
        }
    }

    private class NumberValueEditListener implements SimpleValueEditorDialog.OnEditingFinishedListener {
        VariableProperty property;

        public NumberValueEditListener(VariableProperty property) {
            this.property = property;
        }

        @Override
        public void onEditingFinished(boolean okWasPressed, Editable editable) {
            if (okWasPressed) {
                setStatForPropertyFromText(property, editable.toString());
            }
            hideKeyboardDelayed(0);
        }
    }

    private void setStatForPropertyFromText(VariableProperty property, String valueString) {
        if (property == VariableProperty.BAB_SECONDARY) {
            combatStats.setBABSecondary(valueString == null ? "" : valueString);
        } else {
            try {
                int value = Integer.parseInt(valueString);
                switch (property) {
                    case TOTAL_HP:          combatStats.setTotalHP(value);           break;
                    case WOUNDS:            combatStats.setWounds(value);            break;
                    case NON_LETHAL_DMG:    combatStats.setNonLethalDamage(value);   break;
                    case DMG_REDUCT:        combatStats.setDamageReduction(value);   break;
                    case BASE_SPEED:        combatStats.setBaseSpeed(value);         break;
                    case INIT_MISC_MOD:     combatStats.setInitiativeMiscMod(value); break;
                    case AC_ARMOR:          combatStats.setACArmourBonus(value);     break;
                    case AC_SHIELD:         combatStats.setACShieldBonus(value);     break;
                    case SIZE_MOD:          combatStats.setSizeModifier(value);      break;
                    case AC_NATURAL_ARMOR:  combatStats.setNaturalArmour(value);     break;
                    case DEFLECT_MOD:       combatStats.setDeflectionMod(value);     break;
                    case AC_MISC_MOD:       combatStats.setACMiscMod(value);         break;
                    case SPELL_RESIST:      combatStats.setSpellResistance(value);   break;
                    case BAB_PRIMARY:       combatStats.setBABPrimary(value);        break;
                    case CMD_MISC_MOD:      combatStats.setCMDMiscMod(value);        break;
                    default: throw new IllegalArgumentException("Unexpected property: " + property);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Failed to parse int from " + valueString);
            }
        }
        onPropertyValueChanged(property);
    }

    private void onPropertyValueChanged(VariableProperty property) {
        updateViewsForProperty(property);
        updateComputedStatsForPropertyChange(property);
    }

    private void updateComputedStatsForPropertyChange(Object property) {
        for (ComputedCombatStat c : computedStats) {
            c.onPropertyChange(property);
        }
    }

    private void updateViewsForProperty(VariableProperty property) {
        for (TextView tv : variablePropertyFields.get(property)) {
            tv.setText(getTextValueForProperty(property));
        }
    }

    private OnClickListener combatStatAbilityTextListener = new OnClickListener() {
        @Override public void onClick(View v) {
            AbilityProperty ability = (AbilityProperty) v.getTag();

            AbilityType defaultAbilityKey = getDefaultAbilityForProperty(ability);
            AbilityType currentAbility = getCurrentAbilityForProperty(ability);

            AbilitySelectionDialog dialog =
                    new AbilitySelectionDialog(getContext(), currentAbility, defaultAbilityKey);
            dialog.setOnAbilitySelectedListener(new AbilityTypeDialogListener(ability));
            dialog.show();
        }
    };

    private AbilityType getDefaultAbilityForProperty(AbilityProperty ability) {
        switch (ability) {
            case INIT:  return CombatStatSet.DEFAULT_INIT_ABILITY;
            case AC:    return CombatStatSet.DEFAULT_AC_ABILITY;
            case CMB:   return CombatStatSet.DEFAULT_CMB_ABILITY;
            case CMD:   return CombatStatSet.DEFAULT_CMD_ABILITY;
            default: throw new IllegalArgumentException("Unexpected ability " + ability);
        }
    }

    private AbilityType getCurrentAbilityForProperty(AbilityProperty ability) {
        switch (ability) {
            case INIT:  return combatStats.getInitAbility();
            case AC:    return combatStats.getACAbility();
            case CMB:   return combatStats.getCMBAbility();
            case CMD:   return combatStats.getCMDAbility();
            default: throw new IllegalArgumentException("Unexpected ability " + ability);
        }
    }

    private class AbilityTypeDialogListener implements AbilitySelectionDialog.OnAbilitySelectedListener {
        final AbilityProperty ability;

        private AbilityTypeDialogListener(AbilityProperty ability) {
            this.ability = ability;
        }

        @Override public void onAbilitySelected(AbilityType abilityKey) {
            if (abilityKey != null) {
                setAbilityType(ability, abilityKey);
            }
        }
    }

    private void setAbilityType(AbilityProperty ability, AbilityType type) {
        switch (ability) {
            case INIT:  combatStats.setInitAbility(type);
            case AC:    combatStats.setACAbility(type);
            case CMB:   combatStats.setCMBAbility(type);
            case CMD:   combatStats.setCMDAbility(type);
        }
        onAbilityPropertyValueChanged(ability, type);
    }

    private void onAbilityPropertyValueChanged(AbilityProperty ability, AbilityType newValue) {
        abilityPropertyFields.get(ability).setAbility(newValue);
        updateComputedStatsForPropertyChange(ability);
    }

    private void initSaveRows() {
        View root = getRootView();

        saveRows = Maps.newHashMap();
        Map<SaveType, Pair<Integer, Integer>> rowIdsAndTitles = Maps.newHashMap();
        rowIdsAndTitles.put(SaveType.FORT, new Pair<Integer, Integer>(R.id.row_fort_stats, R.string.combat_stats_fort));
        rowIdsAndTitles.put(SaveType.REF, new Pair<Integer, Integer>(R.id.row_ref_stats, R.string.combat_stats_ref));
        rowIdsAndTitles.put(SaveType.WILL, new Pair<Integer, Integer>(R.id.row_will_stats, R.string.combat_stats_will));

        for (SaveType saveType : rowIdsAndTitles.keySet()) {
            Pair<Integer, Integer> idAndTitle = rowIdsAndTitles.get(saveType);
            View saveRow = root.findViewById(idAndTitle.first);
            ((TextView) saveRow.findViewById(R.id.tv_save_row_name)).setText(idAndTitle.second);

            saveRows.put(saveType, new SaveRow(saveType,
                    (TextView) saveRow.findViewById(R.id.tv_save_total_value),
                    (TextView) saveRow.findViewById(R.id.tv_save_base_value),
                    (TextView) saveRow.findViewById(R.id.tv_save_magic_value),
                    (TextView) saveRow.findViewById(R.id.tv_save_misc_value),
                    (TextView) saveRow.findViewById(R.id.tv_save_temp_value),
                    (TextView) saveRow.findViewById(R.id.tv_save_ability)));
        }
    }

    private class SaveRow {
        SaveType saveType;

        TextView total;
        AbilityView ability;
        Map<SaveProperty, TextView> propertyViews;

        private SaveRow(SaveType saveType, TextView total, TextView base, TextView magic, TextView misc, TextView temp, TextView ability) {
            this.saveType = saveType;
            this.total = total;
            this.ability = new AbilityView(ability);
            propertyViews = Maps.newHashMap();
            propertyViews.put(SaveProperty.BASE, base);
            propertyViews.put(SaveProperty.MAGIC, magic);
            propertyViews.put(SaveProperty.MISC, misc);
            propertyViews.put(SaveProperty.TEMP, temp);

            configurePropertyViews();
            this.ability.setOnClickListener(abilityTextClickListener);
        }

        private void configurePropertyViews() {
            for (SaveProperty property : propertyViews.keySet()) {
                TextView view = propertyViews.get(property);
                view.setTag(property);
                view.setOnClickListener(valueTextListener);
            }
        }

        private OnClickListener valueTextListener = new OnClickListener() {
            @Override public void onClick(View v) {
                showValueEditorForSave(getPropertyForView((TextView) v));
            }
        };

        private SaveProperty getPropertyForView(TextView v) {
            return (SaveProperty) v.getTag();
        }

        private void showValueEditorForSave(SaveProperty property) {
            if (property == null) return;
            SimpleValueEditorDialog.builder(getContext())
                    .forType(SimpleValueEditorDialog.ValueType.NUMBER_SIGNED)
                    .withTitle(property.getNameResId())
                    .withInitialValue(Integer.toString(getValueForProperty(property)))
                    .withOnFinishedListener(new SaveValueEditListener(property))
                    .build()
                    .show();
        }

        private int getValueForProperty(SaveProperty property) {
            Save save = getSave();
            switch (property) {
                case BASE:  return save.getBaseSave();
                case MAGIC: return save.getMagicMod();
                case MISC:  return save.getMiscMod();
                case TEMP:  return save.getTempMod();
                default:    throw new IllegalArgumentException("Unexpected property " + property);
            }
        }

        public Save getSave() {
            return saveSet.getSave(saveType);
        }

        private class SaveValueEditListener implements SimpleValueEditorDialog.OnEditingFinishedListener {
            SaveProperty property;

            public SaveValueEditListener(SaveProperty property) {
                this.property = property;
            }

            @Override
            public void onEditingFinished(boolean okWasPressed, Editable editable) {
                if (okWasPressed) {
                    try {
                        setSavePropertyValue(property, Integer.parseInt(editable.toString()));
                    } catch (NumberFormatException e) {
                        setSavePropertyValue(property, 0);
                    }
                }
                hideKeyboardDelayed(0);
            }
        }

        private void setSavePropertyValue(SaveProperty property, int val) {
            Save save = getSave();
            switch (property) {
                case BASE:  save.setBaseSave(val);  break;
                case MAGIC: save.setMagicMod(val);  break;
                case MISC:  save.setMiscMod(val);   break;
                case TEMP:  save.setTempMod(val);   break;
                default: throw new IllegalArgumentException("Unexpected property " + property);
            }
            onSavePropertyValueChanged(property, val);
        }

        private void onSavePropertyValueChanged(SaveProperty property, int newVal) {
            updateViewForPropertyValue(property, newVal);
            updateTotalView();
        }

        private void updateViewForPropertyValue(SaveProperty property, int newVal) {
            propertyViews.get(property).setText(Integer.toString(newVal));
        }

        private void updateTotalView() {
            total.setText(Integer.toString(getSave().getTotal(abilitySet, maxDex)));
        }

        private OnClickListener abilityTextClickListener = new OnClickListener() {

            @Override public void onClick(View v) {
                AbilityType defaultAbilityKey = saveType.getDefaultAbility();
                AbilityType currentAbility = getSave().getAbilityType();

                if (defaultAbilityKey != null) {
                    AbilitySelectionDialog dialog =
                            new AbilitySelectionDialog(getContext(), currentAbility, defaultAbilityKey);
                    dialog.setOnAbilitySelectedListener(new SaveAbilityDialogListener());
                    dialog.show();
                }
            }
        };

        private class SaveAbilityDialogListener implements AbilitySelectionDialog.OnAbilitySelectedListener {
            @Override public void onAbilitySelected(AbilityType abilityKey) {
                if (abilityKey != null) {
                    getSave().setAbilityType(abilityKey);
                    onAbilityChanged(abilityKey);
                }
            }
        }

        private void onAbilityChanged(AbilityType abilityKey) {
            ability.setAbility(abilityKey);
            updateTotalView();
        }

        public void updateViews() {
            for (SaveProperty property : propertyViews.keySet()) {
                updateViewForProperty(property);
            }
            ability.setAbility(getSave().getAbilityType());
            updateTotalView();
        }

        private void updateViewForProperty(SaveProperty property) {
            updateViewForPropertyValue(property, getValueForProperty(property));
        }
    }

    private abstract class ComputedCombatStat {
        private List<Object> dependees;

        public ComputedCombatStat(Object... dependees) {
            this.dependees = Lists.newArrayList(dependees);
        }

        public void onPropertyChange(Object property) {
            if (dependees.contains(property)) {
                this.updateViews();
            }
        }

        protected abstract void updateViews();
    }

    private void initComputedStats() {
        computedStats = Lists.newArrayList(
                new ComputedCombatStat(VariableProperty.TOTAL_HP, VariableProperty.WOUNDS, VariableProperty.NON_LETHAL_DMG) {
                    @Override protected void updateViews() {
                        setIntText(currentHPLabel, combatStats.getCurrentHP());
                    }
                },
                new ComputedCombatStat(VariableProperty.INIT_MISC_MOD, AbilityProperty.INIT) {
                    @Override protected void updateViews() {
                        setIntText(initLabel, combatStats.getInitiativeMod(abilitySet, maxDex));
                    }
                },
                new ComputedCombatStat(AbilityProperty.AC, VariableProperty.AC_ARMOR, VariableProperty.AC_SHIELD,
                        VariableProperty.SIZE_MOD, VariableProperty.AC_NATURAL_ARMOR, VariableProperty.DEFLECT_MOD,
                        VariableProperty.AC_MISC_MOD) {
                    @Override protected void updateViews() {
                        setIntText(ACLabel, combatStats.getTotalAC(abilitySet, maxDex));
                    }
                },
                new ComputedCombatStat(AbilityProperty.AC, VariableProperty.SIZE_MOD, VariableProperty.DEFLECT_MOD,
                        VariableProperty.AC_MISC_MOD) {
                    @Override protected void updateViews() {
                        setIntText(ACTouchLabel, combatStats.getTouchAC(abilitySet, maxDex));
                    }
                },
                new ComputedCombatStat(VariableProperty.AC_ARMOR, VariableProperty.AC_SHIELD,
                        VariableProperty.SIZE_MOD, VariableProperty.AC_NATURAL_ARMOR, VariableProperty.DEFLECT_MOD,
                        VariableProperty.AC_MISC_MOD) {
                    @Override protected void updateViews() {
                        setIntText(ACFlatFootedLabel, combatStats.getFlatFootedAC());
                    }
                },
                new ComputedCombatStat(AbilityProperty.CMB, VariableProperty.SIZE_MOD, VariableProperty.BAB_PRIMARY) {
                    @Override protected void updateViews() {
                        setIntText(CMBLabel, combatStats.getCombatManeuverBonus(abilitySet, maxDex));
                    }
                },
                new ComputedCombatStat(AbilityProperty.CMD, AbilityProperty.CMB, VariableProperty.SIZE_MOD,
                        VariableProperty.BAB_PRIMARY, VariableProperty.CMD_MISC_MOD) {
                    @Override protected void updateViews() {
                        setIntText(CMDLabel, combatStats.getCombatManeuverDefense(abilitySet, maxDex));
                    }
                }
        );
    }

    private class AbilityView {
        private final TextView textView;

        public AbilityView(TextView textView) {
            this.textView = textView;
        }

        public void setAbility(AbilityType ability) {
            textView.setText(getString(ability.getNameResId())
                    + " (" + abilitySet.getTotalAbilityMod(ability, maxDex) + ")");
        }

        public void setOnClickListener(OnClickListener l) {
            textView.setOnClickListener(l);
        }

        public void setTextViewTag(Object tag) {
            textView.setTag(tag);
        }
    }

    private static void setIntText(TextView textView, int number) {
        textView.setText(Integer.toString(number));
    }

    @Override
    public void updateFragmentUI() {
        updateAllViews();
    }

    private void updateAllViews() {
        for (VariableProperty property : variablePropertyFields.keySet()) {
            updateViewsForProperty(property);
        }

        for (AbilityProperty abilityProp : abilityPropertyFields.keySet()) {
            abilityPropertyFields.get(abilityProp).setAbility(getCurrentAbilityForProperty(abilityProp));
        }

        for (ComputedCombatStat combatStat : computedStats) {
            combatStat.updateViews();
        }

        for (SaveType saveType : saveRows.keySet()) {
            saveRows.get(saveType).updateViews();
        }
    }
    
    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_combat_stats);
    }

    @Override
    public void updateDatabase() {
        if (combatStats != null) {
            try {
                combatStatsDao.update(getCurrentCharacterID(), combatStats);
                for(Save save : saveSet) {
                    saveSetDao.getComponentDAO().update(getCurrentCharacterID(), save);
                }
            } catch (DataAccessException e) {
                Log.e(TAG, "Failed to update stats", e);
            }
        }
    }

    @Override
    public void loadFromDatabase() {
        combatStats = combatStatsDao.find(getCurrentCharacterID());
        saveSet = saveSetDao.findSet(getCurrentCharacterID());
        maxDex = armorDao.getMaxDexForCharacter(getCurrentCharacterID());
        abilitySet = abilitySetDao.findSet(getCurrentCharacterID());
    }

}
