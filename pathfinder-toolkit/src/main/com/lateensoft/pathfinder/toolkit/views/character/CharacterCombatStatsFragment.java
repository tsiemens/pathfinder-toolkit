package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.AbilityRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.ArmorRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.CombatStatRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.SaveRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;

public class CharacterCombatStatsFragment extends AbstractCharacterSheetFragment
		implements OnFocusChangeListener, OnEditorActionListener {

	@SuppressWarnings("unused")
	private static final String TAG = CharacterCombatStatsFragment.class.getSimpleName();
	
	private static enum EAbilityMod { INIT, AC, CMB, CMD, FORT, REF, WILL }
	private EAbilityMod m_abilityModSelectedForEdit;

	private TextView m_currentHPTextView;
	private EditText m_totalHPEditText;
	private EditText m_damageReductEditText;
	private EditText m_woundsEditText;
	private EditText m_nonLethalDmgEditText;

	private EditText m_baseSpeedEditText;

	private TextView m_initTextView;
	private TextView m_initAbilityTv;
	private EditText m_initMiscEditText;

	private TextView m_ACTextView;
	private EditText m_armourBonusEditText;
	private EditText m_shieldBonusEditText;
	private TextView m_ACAbilityTv;
	private EditText m_ACSizeEditText;
	private EditText m_naturalArmourEditText;
	private EditText m_deflectEditText;
	private EditText m_ACMiscEditText;
	private TextView m_ACTouchTextView;
	private TextView m_ACFFTextView;
	private EditText m_spellResistEditText;

	private EditText m_BABPrimaryEditText;
	private EditText m_BABSecondaryEditText;
	private TextView m_CMBTextView;
	private EditText m_CmbBABEditText;
	private TextView m_CMBAbilityTv;
	private EditText m_CMBSizeEditText;
	private TextView m_CMDTextView;
	private TextView m_CMDAbilityTv;
	private EditText m_CMDMiscModEditText;

	private TextView m_fortTextView;
	private EditText m_fortBaseEditText;
	private TextView m_fortAbilityTv;
	private EditText m_fortMagicModEditText;
	private EditText m_fortMiscModEditText;
	private EditText m_fortTempModEditText;

	private TextView m_refTextView;
	private EditText m_refBaseEditText;
	private TextView m_refAbilityTv;
	private EditText m_refMagicModEditText;
	private EditText m_refMiscModEditText;
	private EditText m_refTempModEditText;

	private TextView m_willTextView;
	private EditText m_willBaseEditText;
	private TextView m_willAbilityTv;
	private EditText m_willMagicModEditText;
	private EditText m_willMiscModEditText;
	private EditText m_willTempModEditText;
	
	private OnAbilityTextClickListener m_abilityTextListener;
	
	private CombatStatRepository m_statsRepo;
	private SaveRepository m_saveRepo;
	private AbilityRepository m_abilityRepo;
	private ArmorRepository m_armorRepo;
	
	private CombatStatSet m_combatStats;
	private SaveSet m_saveSet;
	private AbilitySet m_abilitySet;
	private int m_maxDex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_statsRepo = new CombatStatRepository();
		m_saveRepo = new SaveRepository();
		m_abilityRepo = new AbilityRepository();
		m_armorRepo = new ArmorRepository();
		
		m_abilityTextListener = new OnAbilityTextClickListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		setRootView(inflater.inflate(
				R.layout.character_combat_stats_fragment, container, false));
		setupViews(getRootView());

		return getRootView();
	}

	private void updateAllViews() {
		updateHPViews();
		updateSpeedViews();
		updateInitiativeViews();
		updateACViews();
		updateBABViews();
		updateCombatManeuverViews();
		updateSaveViews();
	}
	
	private void updateAbilityView(TextView abilityTv) {
		AbilityType ability = null;
		if (abilityTv == m_initAbilityTv) {
			ability = m_combatStats.getInitAbility();
		} else if (abilityTv == m_ACAbilityTv) {
			ability = m_combatStats.getACAbility();
		} else if (abilityTv == m_CMBAbilityTv) {
			ability = m_combatStats.getCMBAbility();
		} else if (abilityTv == m_CMDAbilityTv) {
			ability = m_combatStats.getCMDAbility();
		}
		// Saves
		else if (abilityTv == m_fortAbilityTv) {
			ability = m_saveSet.getSave(SaveType.FORT).getAbilityType();
		} else if (abilityTv == m_refAbilityTv) {
			ability = m_saveSet.getSave(SaveType.REF).getAbilityType();
		}else if (abilityTv == m_willAbilityTv) {
			ability = m_saveSet.getSave(SaveType.WILL).getAbilityType();
		}
		
		if (ability != null) {
			abilityTv.setText(getString(ability.getNameResId())
					+ " (" + m_abilitySet.getTotalAbilityMod(ability, m_maxDex) + ")");
		}
	}

	/**
	 * Updates all stats for HP
	 */
	private void updateHP() {
		m_combatStats.setTotalHP(getEditTextInt(m_totalHPEditText));
		m_combatStats.setWounds(getEditTextInt(m_woundsEditText));
		m_combatStats.setNonLethalDamage(getEditTextInt(m_nonLethalDmgEditText));
		m_combatStats.setDamageReduction(getEditTextInt(m_damageReductEditText));
		updateHPViews();
	}

	/**
	 * Updates all views for HP
	 */
	private void updateHPViews() {
		setIntText(m_currentHPTextView, m_combatStats.getCurrentHP());
		setIntText(m_totalHPEditText, m_combatStats.getTotalHP());
		setIntText(m_woundsEditText, m_combatStats.getWounds());
		setIntText(m_nonLethalDmgEditText, m_combatStats.getNonLethalDamage());
		setIntText(m_damageReductEditText, m_combatStats.getDamageReduction());
	}

	/**
	 * Updates all stats for speed
	 */
	private void updateSpeed() {
		m_combatStats.setBaseSpeed(getEditTextInt(m_baseSpeedEditText));
	}

	/**
	 * Updates all views for speed
	 */
	private void updateSpeedViews() {
		setIntText(m_baseSpeedEditText, m_combatStats.getBaseSpeed());
	}

	/**
	 * Updates all stats for initiative
	 */
	private void updateInitiative() {
		m_combatStats.setInitiativeMiscMod(getEditTextInt(m_initMiscEditText));
		updateInitiativeViews();
	}

	/**
	 * Updates all views for initiative
	 */
	private void updateInitiativeViews() {
		setIntText(m_initTextView, m_combatStats.getInitiativeMod(m_abilitySet, m_maxDex));
		updateAbilityView(m_initAbilityTv);
		setIntText(m_initMiscEditText, m_combatStats.getInitiativeMiscMod());
	}

	/**
	 * Updates all stats for AC
	 */
	private void updateAC() {
		m_combatStats.setACArmourBonus(getEditTextInt(m_armourBonusEditText));
		m_combatStats.setACShieldBonus(getEditTextInt(m_shieldBonusEditText));
		m_combatStats.setSizeModifier(getEditTextInt(m_ACSizeEditText));
		m_combatStats.setNaturalArmour(getEditTextInt(m_naturalArmourEditText));
		m_combatStats.setDeflectionMod(getEditTextInt(m_deflectEditText));
		m_combatStats.setACMiscMod(getEditTextInt(m_ACMiscEditText));
		m_combatStats.setSpellResistance(getEditTextInt(m_spellResistEditText));
		updateACViews();
	}

	/**
	 * Updates all views for ac
	 */
	private void updateACViews() {
		setIntText(m_ACTextView, m_combatStats.getTotalAC(m_abilitySet, m_maxDex));
		setIntText(m_ACTouchTextView, m_combatStats.getTouchAC(m_abilitySet, m_maxDex));
		setIntText(m_ACFFTextView, m_combatStats.getFlatFootedAC());
		setIntText(m_armourBonusEditText, m_combatStats.getACArmourBonus());
		setIntText(m_shieldBonusEditText, m_combatStats.getACShieldBonus());
		updateAbilityView(m_ACAbilityTv);
		updateSizeModViews();
		setIntText(m_naturalArmourEditText, m_combatStats.getNaturalArmour());
		setIntText(m_deflectEditText, m_combatStats.getDeflectionMod());
		setIntText(m_ACMiscEditText, m_combatStats.getACMiscMod());
		setIntText(m_spellResistEditText, m_combatStats.getSpellResist());
	}

	/**
	 * Updates all stats for BAB
	 */
	private void updateBAB() {
		m_combatStats.setBABPrimary(getEditTextInt(m_BABPrimaryEditText));
		m_combatStats.setBABSecondary(m_BABSecondaryEditText.getText().toString());
		updateBABViews();
	}

	private void updateBABViews() {
		updateCombatManeuverViews();
		m_BABSecondaryEditText.setText(m_combatStats
				.getBABSecondary());
	}

	/**
	 * Updates all stats for combat maneuvers
	 */
	private void updateCombatManeuvers() {
		m_combatStats.setBABPrimary(getEditTextInt(m_CmbBABEditText));
		m_combatStats.setSizeModifier(getEditTextInt(m_CMBSizeEditText));
		m_combatStats.setCMDMiscMod(getEditTextInt(m_CMDMiscModEditText));
		updateCombatManeuverViews();
	}

	/**
	 * Updates all stats for saves
	 */
	private void updateSaves() {
		updateFort();
		updateRef();
		updateWill();
	}

	private void updateFort() {
		m_saveSet.getSaveByIndex(0).setBaseSave(getEditTextInt(m_fortBaseEditText));
		m_saveSet.getSaveByIndex(0).setMagicMod(getEditTextInt(m_fortMagicModEditText));
		m_saveSet.getSaveByIndex(0).setMiscMod(getEditTextInt(m_fortMiscModEditText));
		m_saveSet.getSaveByIndex(0).setTempMod(getEditTextInt(m_fortTempModEditText));
	}

	private void updateRef() {
		m_saveSet.getSaveByIndex(1).setBaseSave(getEditTextInt(m_refBaseEditText));
		m_saveSet.getSaveByIndex(1).setMagicMod(getEditTextInt(m_refMagicModEditText));
		m_saveSet.getSaveByIndex(1).setMiscMod(getEditTextInt(m_refMiscModEditText));
		m_saveSet.getSaveByIndex(1).setTempMod(getEditTextInt(m_refTempModEditText));
	}

	private void updateWill() {
		m_saveSet.getSaveByIndex(2).setBaseSave(getEditTextInt(m_willBaseEditText));
		m_saveSet.getSaveByIndex(2).setMagicMod(getEditTextInt(m_willMagicModEditText));
		m_saveSet.getSaveByIndex(2).setMiscMod(getEditTextInt(m_willMiscModEditText));
		m_saveSet.getSaveByIndex(2).setTempMod(getEditTextInt(m_willTempModEditText));
	}

	/**
	 * Updates all views for combat maneuvers
	 */
	private void updateCombatManeuverViews() {
		setIntText(m_CMBTextView, m_combatStats.getCombatManeuverBonus(m_abilitySet, m_maxDex));
		updatePrimaryBABViews();
		updateAbilityView(m_CMBAbilityTv);
		updateSizeModViews();
		setIntText(m_CMDTextView, m_combatStats.getCombatManeuverDefense(m_abilitySet, m_maxDex));
		updateAbilityView(m_CMDAbilityTv);
		setIntText(m_CMDMiscModEditText, m_combatStats.getCMDMiscMod());
	}

	/**
	 * Updates the BAB edit texts in BAB and CMB
	 */
	private void updatePrimaryBABViews() {
		setIntText(m_BABPrimaryEditText, m_combatStats.getBABPrimary());
		setIntText(m_CmbBABEditText, m_combatStats.getBABPrimary());
	}

	/**
	 * Updates the size mod edit texts in AC and CMB
	 */
	private void updateSizeModViews() {
		setIntText(m_ACSizeEditText, m_combatStats.getSizeModifier());
		setIntText(m_CMBSizeEditText, m_combatStats.getSizeModifier());
	}

	private void setIntText(TextView textView, int number) {
		textView.setText(Integer.toString(number));
	}

	private void updateSaveViews() {
		updateFortSaveViews();
		updateRefSaveViews();
		updateWillSaveViews();
	}

	private void updateFortSaveViews() {
		updateSaveViews(SaveType.FORT, m_fortTextView, m_fortBaseEditText, m_fortAbilityTv,
				m_fortMagicModEditText, m_fortMiscModEditText, m_fortTempModEditText);
	}

	private void updateRefSaveViews() {
		updateSaveViews(SaveType.REF, m_refTextView, m_refBaseEditText, m_refAbilityTv,
				m_refMagicModEditText, m_refMiscModEditText, m_refTempModEditText);
	}

	private void updateWillSaveViews() {
		updateSaveViews(SaveType.WILL, m_willTextView, m_willBaseEditText, m_willAbilityTv,
				m_willMagicModEditText, m_willMiscModEditText, m_willTempModEditText);
	}
	
	private void updateSaveViews(SaveType saveType, TextView sumView, EditText baseEt, TextView abilityText,
			EditText magicEt, EditText miscEt, EditText tempEt) {
		Save save = m_saveSet.getSave(saveType);
		setIntText(sumView, save.getTotal(m_abilitySet, m_maxDex));
		setIntText(baseEt, save.getBaseSave());
		updateAbilityView(abilityText);
		setIntText(magicEt, save.getMagicMod());
		setIntText(miscEt, save.getMiscMod());
		setIntText(tempEt, save.getTempMod());
	}

	/**
	 * @return the value in the edit text. Returns 0 if the parse failed
	 */
	private int getEditTextInt(EditText editText) {
		try {
			return Integer.parseInt(editText.getText().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// Sets edittext listeners
	private void setEditTextListeners(EditText editText) {
		editText.setOnFocusChangeListener(this);
		editText.setOnEditorActionListener(this);
	}
	
	private void setAbilityTextViewListener(TextView tv) {
		tv.setOnClickListener(m_abilityTextListener);
	}
	
	private class OnAbilityTextClickListener implements OnClickListener {

		@Override public void onClick(View v) {
			AbilityType defaultAbilityKey = null;
			AbilityType currentAbility = AbilityType.DEX;
			if (v == m_initAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.INIT;
				defaultAbilityKey = CombatStatSet.DEFAULT_INIT_ABILITY;
				currentAbility = m_combatStats.getInitAbility();
			} else if (v == m_ACAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.AC;
				defaultAbilityKey = CombatStatSet.DEFAULT_AC_ABILITY;
				currentAbility = m_combatStats.getACAbility();
			} else if (v == m_CMBAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.CMB;
				defaultAbilityKey = CombatStatSet.DEFAULT_CMB_ABILITY;
				currentAbility = m_combatStats.getCMBAbility();
			} else if (v == m_CMDAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.CMD;
				defaultAbilityKey = CombatStatSet.DEFAULT_CMD_ABILITY;
				currentAbility = m_combatStats.getCMDAbility();
			} else if (v == m_fortAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.FORT;
				defaultAbilityKey = SaveType.FORT.getDefaultAbility();
				currentAbility = m_saveSet.getSave(SaveType.FORT).getAbilityType();
			} else if (v == m_refAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.REF;
				defaultAbilityKey = SaveType.REF.getDefaultAbility();
				currentAbility = m_saveSet.getSave(SaveType.REF).getAbilityType();
			} else if (v == m_willAbilityTv) {
				m_abilityModSelectedForEdit = EAbilityMod.WILL;
				defaultAbilityKey = SaveType.WILL.getDefaultAbility();
				currentAbility = m_saveSet.getSave(SaveType.WILL).getAbilityType();
			}
			
			if (defaultAbilityKey != null) {
				AbilitySelectionDialog dialog =
						new AbilitySelectionDialog(getContext(), currentAbility, defaultAbilityKey);
				dialog.setOnAbilitySelectedListener(new AbilityDialogListener());
				dialog.show();
			}
		}
		
	}
	
	private class AbilityDialogListener implements AbilitySelectionDialog.OnAbilitySelectedListener {

		@Override public void onAbilitySelected(AbilityType abilityKey) {
			if (abilityKey != null) {
				int viewID;
				switch (m_abilityModSelectedForEdit) {
				case AC:
					m_combatStats.setACAbility(abilityKey);
					viewID = m_ACAbilityTv.getId();
					break;
				case CMB:
					m_combatStats.setCMBAbilityKey(abilityKey);
					viewID = m_CMBAbilityTv.getId();
					break;
				case CMD:
					m_combatStats.setCMDAbility(abilityKey);
					viewID = m_CMDAbilityTv.getId();
					break;
				case FORT:
					m_saveSet.getSave(SaveType.FORT).setAbilityType(abilityKey);
					viewID = m_fortAbilityTv.getId();
					break;
				case INIT:
					m_combatStats.setInitAbility(abilityKey);
					viewID = m_initAbilityTv.getId();
					break;
				case REF:
					m_saveSet.getSave(SaveType.REF).setAbilityType(abilityKey);
					viewID = m_refAbilityTv.getId();
					break;
				case WILL:
					m_saveSet.getSave(SaveType.WILL).setAbilityType(abilityKey);
					viewID = m_willAbilityTv.getId();
					break;
				default:
					return;
				}

				finishedEditing(viewID);
			}
		}
		
	}

	// Sets up all the text and edit texts
	private void setupViews(View fragmentView) {
		m_currentHPTextView = (TextView) fragmentView
				.findViewById(R.id.textViewCurrentHP);
		m_totalHPEditText = (EditText) fragmentView
				.findViewById(R.id.editTextTotalHP);
		setEditTextListeners(m_totalHPEditText);

		m_damageReductEditText = (EditText) fragmentView
				.findViewById(R.id.editTextDamageReduction);
		setEditTextListeners(m_damageReductEditText);

		m_woundsEditText = (EditText) fragmentView
				.findViewById(R.id.editTextWounds);
		setEditTextListeners(m_woundsEditText);

		m_nonLethalDmgEditText = (EditText) fragmentView
				.findViewById(R.id.editTextNonLethalDmg);
		setEditTextListeners(m_nonLethalDmgEditText);

		m_baseSpeedEditText = (EditText) fragmentView
				.findViewById(R.id.editTextBaseSpeed);
		setEditTextListeners(m_baseSpeedEditText);

		m_initTextView = (TextView) fragmentView
				.findViewById(R.id.textViewInitiative);
		m_initAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvInitAbility);
		setAbilityTextViewListener(m_initAbilityTv);

		m_initMiscEditText = (EditText) fragmentView
				.findViewById(R.id.editTextInitMiscMod);
		setEditTextListeners(m_initMiscEditText);

		m_ACTextView = (TextView) fragmentView.findViewById(R.id.textViewAC);
		m_armourBonusEditText = (EditText) fragmentView
				.findViewById(R.id.editTextArmourBonus);
		setEditTextListeners(m_armourBonusEditText);

		m_shieldBonusEditText = (EditText) fragmentView
				.findViewById(R.id.editTextShieldBonus);
		setEditTextListeners(m_shieldBonusEditText);

		m_ACAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvACAbility);
		setAbilityTextViewListener(m_ACAbilityTv);

		m_ACSizeEditText = (EditText) fragmentView
				.findViewById(R.id.editTextACSizeMod);
		setEditTextListeners(m_ACSizeEditText);

		m_naturalArmourEditText = (EditText) fragmentView
				.findViewById(R.id.editTextNaturalArmour);
		setEditTextListeners(m_naturalArmourEditText);

		m_deflectEditText = (EditText) fragmentView
				.findViewById(R.id.editTextDeflectionMod);
		setEditTextListeners(m_deflectEditText);

		m_ACMiscEditText = (EditText) fragmentView
				.findViewById(R.id.editTextACMiscMod);
		setEditTextListeners(m_ACMiscEditText);

		m_ACTouchTextView = (TextView) fragmentView
				.findViewById(R.id.textViewTouchAC);
		m_ACFFTextView = (TextView) fragmentView
				.findViewById(R.id.textViewFlatFootedAC);
		m_spellResistEditText = (EditText) fragmentView
				.findViewById(R.id.editTextSpellResist);
		setEditTextListeners(m_spellResistEditText);

		m_BABPrimaryEditText = (EditText) fragmentView
				.findViewById(R.id.editTextBABPrimary);
		setEditTextListeners(m_BABPrimaryEditText);

		m_BABSecondaryEditText = (EditText) fragmentView
				.findViewById(R.id.editTextBABSecondary);
		setEditTextListeners(m_BABSecondaryEditText);

		m_CMBTextView = (TextView) fragmentView.findViewById(R.id.textViewCMB);
		m_CmbBABEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCmbBAB);
		setEditTextListeners(m_CmbBABEditText);

		m_CMBAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvCMBAbility);
		setAbilityTextViewListener(m_CMBAbilityTv);

		m_CMBSizeEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMBSizeMod);
		setEditTextListeners(m_CMBSizeEditText);

		m_CMDTextView = (TextView) fragmentView.findViewById(R.id.textViewCMD);
		m_CMDAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvCMDAbility);
		m_CMDMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMDMiscMod);
		setAbilityTextViewListener(m_CMDAbilityTv);
		setEditTextListeners(m_CMDMiscModEditText);

		m_fortTextView = (TextView) fragmentView.findViewById(R.id.tvFort);
		m_fortBaseEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortBase);
		m_fortAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvFortAbility);
		m_fortMagicModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortMagicMod);
		m_fortMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortMiscMod);
		m_fortTempModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortTempMod);
		setEditTextListeners(m_fortBaseEditText);
		setAbilityTextViewListener(m_fortAbilityTv);
		setEditTextListeners(m_fortMagicModEditText);
		setEditTextListeners(m_fortMiscModEditText);
		setEditTextListeners(m_fortTempModEditText);

		m_refTextView = (TextView) fragmentView.findViewById(R.id.tvRef);
		m_refBaseEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefBase);
		m_refAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvReflexAbility);
		m_refMagicModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefMagicMod);
		m_refMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefMiscMod);
		m_refTempModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefTempMod);
		setEditTextListeners(m_refBaseEditText);
		setAbilityTextViewListener(m_refAbilityTv);
		setEditTextListeners(m_refMagicModEditText);
		setEditTextListeners(m_refMiscModEditText);
		setEditTextListeners(m_refTempModEditText);

		m_willTextView = (TextView) fragmentView.findViewById(R.id.tvWill);
		m_willBaseEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillBase);
		m_willAbilityTv = (TextView) fragmentView
				.findViewById(R.id.tvWillAbility);
		m_willMagicModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillMagicMod);
		m_willMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillMiscMod);
		m_willTempModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillTempMod);
		setEditTextListeners(m_willBaseEditText);
		setAbilityTextViewListener(m_willAbilityTv);
		setEditTextListeners(m_willMagicModEditText);
		setEditTextListeners(m_willMiscModEditText);
		setEditTextListeners(m_willTempModEditText);
	}

	/**
	 * Updates the view which has finished being edited
	 */
	private void finishedEditing(int viewID) {
		if (viewID == m_woundsEditText.getId()
				|| viewID == m_totalHPEditText.getId()
				|| viewID == m_nonLethalDmgEditText.getId()
				|| viewID == m_damageReductEditText.getId())
			updateHP();

		else if (viewID == m_baseSpeedEditText.getId())
			updateSpeed();

		else if (viewID == m_initAbilityTv.getId()
				|| viewID == m_initMiscEditText.getId())
			updateInitiative();

		else if (viewID == m_armourBonusEditText.getId()
				|| viewID == m_shieldBonusEditText.getId()
				|| viewID == m_ACAbilityTv.getId()
				|| viewID == m_ACSizeEditText.getId()
				|| viewID == m_naturalArmourEditText.getId()
				|| viewID == m_deflectEditText.getId()
				|| viewID == m_ACMiscEditText.getId()
				|| viewID == m_spellResistEditText.getId()) {
			updateAC();
			updateCombatManeuverViews();
		}

		else if (viewID == m_BABPrimaryEditText.getId()
				|| viewID == m_BABSecondaryEditText.getId())
			updateBAB();

		else if (viewID == m_CmbBABEditText.getId()
				|| viewID == m_CMBAbilityTv.getId()
				|| viewID == m_CMDAbilityTv.getId()
				|| viewID == m_CMBSizeEditText.getId()
				|| viewID == m_CMDMiscModEditText.getId()) {
			updateCombatManeuvers();
			updateACViews();
		}

		else if (viewID == m_fortBaseEditText.getId()
				|| viewID == m_fortAbilityTv.getId()
				|| viewID == m_fortMagicModEditText.getId()
				|| viewID == m_fortMiscModEditText.getId()
				|| viewID == m_fortTempModEditText.getId()) {
			updateFort();
			updateFortSaveViews();
		}

		else if (viewID == m_refBaseEditText.getId()
				|| viewID == m_refAbilityTv.getId()
				|| viewID == m_refMagicModEditText.getId()
				|| viewID == m_refMiscModEditText.getId()
				|| viewID == m_refTempModEditText.getId()) {
			updateRef();
			updateRefSaveViews();
		}

		else if (viewID == m_willBaseEditText.getId()
				|| viewID == m_willAbilityTv.getId()
				|| viewID == m_willMagicModEditText.getId()
				|| viewID == m_willMiscModEditText.getId()
				|| viewID == m_willTempModEditText.getId()) {
			updateWill();
			updateWillSaveViews();
		}

	}

	// TODO this should be an object
	public void onFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			finishedEditing(view.getId());
		}
	}

	// TODO this should be an object
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		finishedEditing(view.getId());
		return false;
	}

	@Override
	public void updateFragmentUI() {
		updateAllViews();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_combat_stats);
	}

	@Override
	public void updateDatabase() {
		updateHP();
		updateSpeed();
		updateInitiative();
		updateAC();
		updateBAB();
		updateCombatManeuvers();
		updateSaves();
		
		if (m_combatStats != null) {
			m_statsRepo.update(m_combatStats);
			for(Save save : m_saveSet) {
				m_saveRepo.update(save);
			}
		}
	}

	@Override
	public void loadFromDatabase() {
		m_combatStats = m_statsRepo.query(getCurrentCharacterID());
		m_saveSet = m_saveRepo.querySet(getCurrentCharacterID());
		m_maxDex = m_armorRepo.getMaxDex(getCurrentCharacterID());
		m_abilitySet = m_abilityRepo.querySet(getCurrentCharacterID());
	}

}
