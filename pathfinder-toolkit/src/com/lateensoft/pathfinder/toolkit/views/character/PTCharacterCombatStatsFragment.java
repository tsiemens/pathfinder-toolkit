package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCombatStatRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTSaveRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSaveSet;

public class PTCharacterCombatStatsFragment extends PTCharacterSheetFragment
		implements OnFocusChangeListener, OnEditorActionListener {

	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterCombatStatsFragment.class.getSimpleName();

	static final int STR_KEY = 0;
	static final int DEX_KEY = 1;
	static final int CON_KEY = 2;
	static final int INT_KEY = 3;
	static final int WIS_KEY = 4;
	static final int CHA_KEY = 5;

	static final int FORT_KEY = 0;
	static final int REF_KEY = 1;
	static final int WILL_KEY = 2;

	private TextView m_currentHPTextView;
	private EditText m_totalHPEditText;
	private EditText m_damageReductEditText;
	private EditText m_woundsEditText;
	private EditText m_nonLethalDmgEditText;

	private EditText m_baseSpeedEditText;

	private TextView m_initTextView;
	private EditText m_initDexEditText;
	private EditText m_initMiscEditText;

	private TextView m_ACTextView;
	private EditText m_armourBonusEditText;
	private EditText m_shieldBonusEditText;
	private EditText m_ACDexEditText;
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
	private EditText m_CMBStrengthEditText;
	private EditText m_CMBSizeEditText;
	private TextView m_CMDTextView;
	private EditText m_CMDDexEditText;
	private EditText m_CMDMiscModEditText;

	private TextView m_fortTextView;
	private EditText m_fortBaseEditText;
	private EditText m_fortAbilityModEditText;
	private EditText m_fortMagicModEditText;
	private EditText m_fortMiscModEditText;
	private EditText m_fortTempModEditText;

	private TextView m_refTextView;
	private EditText m_refBaseEditText;
	private EditText m_refAbilityModEditText;
	private EditText m_refMagicModEditText;
	private EditText m_refMiscModEditText;
	private EditText m_refTempModEditText;

	private TextView m_willTextView;
	private EditText m_willBaseEditText;
	private EditText m_willAbilityModEditText;
	private EditText m_willMagicModEditText;
	private EditText m_willMiscModEditText;
	private EditText m_willTempModEditText;
	
	private PTCombatStatRepository m_statsRepo;
	private PTSaveRepository m_saveRepo;
	private PTCombatStatSet m_combatStats;
	private PTSaveSet m_saveSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_statsRepo = new PTCombatStatRepository();
		m_saveRepo = new PTSaveRepository();
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

	public void updateAllViews() {
		updateHPViews();
		updateSpeedViews();
		updateInitiativeViews();
		updateACViews();
		updateBABViews();
		updateCombatManeuverViews();
		updateSaveViews();
	}

	/**
	 * Updates all stats for HP
	 */
	public void updateHP() {
		m_combatStats.setTotalHP(getEditTextInt(m_totalHPEditText));
		m_combatStats.setWounds(getEditTextInt(m_woundsEditText));
		m_combatStats.setNonLethalDamage(getEditTextInt(m_nonLethalDmgEditText));
		m_combatStats.setDamageReduction(getEditTextInt(m_damageReductEditText));
		updateHPViews();
	}

	/**
	 * Updates all views for HP
	 */
	public void updateHPViews() {
		setIntText(m_currentHPTextView, m_combatStats.getCurrentHP());
		setIntText(m_totalHPEditText, m_combatStats.getTotalHP());
		setIntText(m_woundsEditText, m_combatStats.getWounds());
		setIntText(m_nonLethalDmgEditText, m_combatStats.getNonLethalDamage());
		setIntText(m_damageReductEditText, m_combatStats.getDamageReduction());
	}

	/**
	 * Updates all stats for speed
	 */
	public void updateSpeed() {
		m_combatStats.setBaseSpeed(getEditTextInt(m_baseSpeedEditText));
	}

	/**
	 * Updates all views for speed
	 */
	public void updateSpeedViews() {
		setIntText(m_baseSpeedEditText, m_combatStats.getBaseSpeed());
	}

	/**
	 * Updates all stats for initiative
	 */
	public void updateInitiative() {
		m_combatStats.setInitDexMod(getEditTextInt(m_initDexEditText));
		m_combatStats.setInitiativeMiscMod(getEditTextInt(m_initMiscEditText));
		updateInitiativeViews();
	}

	/**
	 * Updates all views for initiative
	 */
	public void updateInitiativeViews() {
		setIntText(m_initTextView, m_combatStats.getInitiativeMod());
		setIntText(m_initDexEditText, m_combatStats.getInitDexMod());
		setIntText(m_initMiscEditText, m_combatStats.getInitiativeMiscMod());
	}

	/**
	 * Updates all stats for AC
	 */
	public void updateAC() {
		m_combatStats.setACArmourBonus(getEditTextInt(m_armourBonusEditText));
		m_combatStats.setACShieldBonus(getEditTextInt(m_shieldBonusEditText));
		m_combatStats.setACDexMod(getEditTextInt(m_ACDexEditText));
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
	public void updateACViews() {
		setIntText(m_ACTextView, m_combatStats.getTotalAC());
		setIntText(m_ACTouchTextView, m_combatStats.getTouchAC());
		setIntText(m_ACFFTextView, m_combatStats.getFlatFootedAC());
		setIntText(m_armourBonusEditText, m_combatStats.getACArmourBonus());
		setIntText(m_shieldBonusEditText, m_combatStats.getACShieldBonus());
		setIntText(m_ACDexEditText, m_combatStats.getACDexMod());
		updateSizeModViews();
		setIntText(m_naturalArmourEditText, m_combatStats.getNaturalArmour());
		setIntText(m_deflectEditText, m_combatStats.getDeflectionMod());
		setIntText(m_ACMiscEditText, m_combatStats.getACMiscMod());
		setIntText(m_spellResistEditText, m_combatStats.getSpellResist());
	}

	/**
	 * Updates all stats for BAB
	 */
	public void updateBAB() {
		m_combatStats.setBABPrimary(getEditTextInt(m_BABPrimaryEditText));
		m_combatStats.setBABSecondary(m_BABSecondaryEditText.getText().toString());
		updateBABViews();
	}

	public void updateBABViews() {
		updateCombatManeuverViews();
		m_BABSecondaryEditText.setText(m_combatStats
				.getBABSecondary());
	}

	/**
	 * Updates all stats for combat maneuvers
	 */
	public void updateCombatManeuvers() {
		m_combatStats.setBABPrimary(getEditTextInt(m_CmbBABEditText));
		m_combatStats.setStrengthMod(getEditTextInt(m_CMBStrengthEditText));
		m_combatStats.setSizeModifier(getEditTextInt(m_CMBSizeEditText));
		m_combatStats.setCMDDexMod(getEditTextInt(m_CMDDexEditText));
		m_combatStats.setCMDMiscMod(getEditTextInt(m_CMDMiscModEditText));
		updateCombatManeuverViews();
	}

	/**
	 * Updates all stats for saves
	 */
	public void updateSaves() {
		updateFort();
		updateRef();
		updateWill();
	}

	public void updateFort() {
		m_saveSet.getSave(0).setBase(getEditTextInt(m_fortBaseEditText));
		m_saveSet.getSave(0).setAbilityMod(getEditTextInt(m_fortAbilityModEditText));
		m_saveSet.getSave(0).setMagicMod(getEditTextInt(m_fortMagicModEditText));
		m_saveSet.getSave(0).setMiscMod(getEditTextInt(m_fortMiscModEditText));
		m_saveSet.getSave(0).setTempMod(getEditTextInt(m_fortTempModEditText));
	}

	public void updateRef() {
		m_saveSet.getSave(1).setBase(getEditTextInt(m_refBaseEditText));
		m_saveSet.getSave(1).setAbilityMod(getEditTextInt(m_refAbilityModEditText));
		m_saveSet.getSave(1).setMagicMod(getEditTextInt(m_refMagicModEditText));
		m_saveSet.getSave(1).setMiscMod(getEditTextInt(m_refMiscModEditText));
		m_saveSet.getSave(1).setTempMod(getEditTextInt(m_refTempModEditText));
	}

	public void updateWill() {
		m_saveSet.getSave(2).setBase(getEditTextInt(m_willBaseEditText));
		m_saveSet.getSave(2).setAbilityMod(getEditTextInt(m_willAbilityModEditText));
		m_saveSet.getSave(2).setMagicMod(getEditTextInt(m_willMagicModEditText));
		m_saveSet.getSave(2).setMiscMod(getEditTextInt(m_willMiscModEditText));
		m_saveSet.getSave(2).setTempMod(getEditTextInt(m_willTempModEditText));
	}

	/**
	 * Updates all views for combat maneuvers
	 */
	public void updateCombatManeuverViews() {
		setIntText(m_CMBTextView, m_combatStats.getCombatManeuverBonus());
		updatePrimaryBABViews();
		setIntText(m_CMBStrengthEditText, m_combatStats.getStrengthMod());
		updateSizeModViews();
		setIntText(m_CMDTextView, m_combatStats.getCombatManeuverDefense());
		setIntText(m_CMDDexEditText, m_combatStats.getCMDDexMod());
		setIntText(m_CMDMiscModEditText, m_combatStats.getCMDMiscMod());
	}

	/**
	 * Updates the BAB edit texts in BAB and CMB
	 */
	public void updatePrimaryBABViews() {
		setIntText(m_BABPrimaryEditText, m_combatStats.getBABPrimary());
		setIntText(m_CmbBABEditText, m_combatStats.getBABPrimary());
	}

	/**
	 * Updates the size mod edit texts in AC and CMB
	 */
	public void updateSizeModViews() {
		setIntText(m_ACSizeEditText, m_combatStats.getSizeModifier());
		setIntText(m_CMBSizeEditText, m_combatStats.getSizeModifier());
	}

	private void setIntText(TextView textView, int number) {
		textView.setText(Integer.toString(number));
	}

	public void updateSaveViews() {
		updateFortSaveViews();
		updateRefSaveViews();
		updateWillSaveViews();
	}

	public void updateFortSaveViews() {
		setIntText(m_fortTextView, m_saveSet.getSave(0).getTotal());
		setIntText(m_fortBaseEditText, m_saveSet.getSave(0).getBase());
		setIntText(m_fortAbilityModEditText, m_saveSet.getSave(0).getAbilityMod());
		setIntText(m_fortMagicModEditText, m_saveSet.getSave(0).getMagicMod());
		setIntText(m_fortMiscModEditText, m_saveSet.getSave(0).getMiscMod());
		setIntText(m_fortTempModEditText, m_saveSet.getSave(0).getTempMod());
	}

	public void updateRefSaveViews() {
		setIntText(m_refTextView, m_saveSet.getSave(1).getTotal());
		setIntText(m_refBaseEditText, m_saveSet.getSave(1).getBase());
		setIntText(m_refAbilityModEditText, m_saveSet.getSave(1).getAbilityMod());
		setIntText(m_refMagicModEditText, m_saveSet.getSave(1).getMagicMod());
		setIntText(m_refMiscModEditText, m_saveSet.getSave(1).getMiscMod());
		setIntText(m_refTempModEditText, m_saveSet.getSave(1).getTempMod());
	}

	public void updateWillSaveViews() {
		setIntText(m_willTextView, m_saveSet.getSave(2).getTotal());
		setIntText(m_willBaseEditText, m_saveSet.getSave(2).getBase());
		setIntText(m_willAbilityModEditText, m_saveSet.getSave(2).getAbilityMod());
		setIntText(m_willMagicModEditText, m_saveSet.getSave(2).getMagicMod());
		setIntText(m_willMiscModEditText, m_saveSet.getSave(2).getMiscMod());
		setIntText(m_willTempModEditText, m_saveSet.getSave(2).getTempMod());
	}

	/**
	 * 
	 * @param editText
	 * @return the value in the edit text. Returns Integer.MAX_VALUE if the
	 *         parse failed
	 */
	public int getEditTextInt(EditText editText) {
		try {
			return Integer.parseInt(editText.getText().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// Sets edittext listeners
	public void setEditTextListeners(EditText editText) {
		editText.setOnFocusChangeListener(this);
		editText.setOnEditorActionListener(this);
	}

	// Sets up all the text and edit texts
	public void setupViews(View fragmentView) {
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
		m_initDexEditText = (EditText) fragmentView
				.findViewById(R.id.editTextInitDexMod);
		setEditTextListeners(m_initDexEditText);

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

		m_ACDexEditText = (EditText) fragmentView
				.findViewById(R.id.editTextACDexMod);
		setEditTextListeners(m_ACDexEditText);

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

		m_CMBStrengthEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMBStrengthMod);
		setEditTextListeners(m_CMBStrengthEditText);

		m_CMBSizeEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMBSizeMod);
		setEditTextListeners(m_CMBSizeEditText);

		m_CMDTextView = (TextView) fragmentView.findViewById(R.id.textViewCMD);
		m_CMDDexEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMDDex);
		m_CMDMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMDMiscMod);
		setEditTextListeners(m_CMDDexEditText);
		setEditTextListeners(m_CMDMiscModEditText);

		m_fortTextView = (TextView) fragmentView.findViewById(R.id.tvFort);
		m_fortBaseEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortBase);
		m_fortAbilityModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortAbilityMod);
		m_fortMagicModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortMagicMod);
		m_fortMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortMiscMod);
		m_fortTempModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveFortTempMod);
		setEditTextListeners(m_fortBaseEditText);
		setEditTextListeners(m_fortAbilityModEditText);
		setEditTextListeners(m_fortMagicModEditText);
		setEditTextListeners(m_fortMiscModEditText);
		setEditTextListeners(m_fortTempModEditText);

		m_refTextView = (TextView) fragmentView.findViewById(R.id.tvRef);
		m_refBaseEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefBase);
		m_refAbilityModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefAbilityMod);
		m_refMagicModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefMagicMod);
		m_refMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefMiscMod);
		m_refTempModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveRefTempMod);
		setEditTextListeners(m_refBaseEditText);
		setEditTextListeners(m_refAbilityModEditText);
		setEditTextListeners(m_refMagicModEditText);
		setEditTextListeners(m_refMiscModEditText);
		setEditTextListeners(m_refTempModEditText);

		m_willTextView = (TextView) fragmentView.findViewById(R.id.tvWill);
		m_willBaseEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillBase);
		m_willAbilityModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillAbilityMod);
		m_willMagicModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillMagicMod);
		m_willMiscModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillMiscMod);
		m_willTempModEditText = (EditText) fragmentView
				.findViewById(R.id.etSaveWillTempMod);
		setEditTextListeners(m_willBaseEditText);
		setEditTextListeners(m_willAbilityModEditText);
		setEditTextListeners(m_willMagicModEditText);
		setEditTextListeners(m_willMiscModEditText);
		setEditTextListeners(m_willTempModEditText);
	}

	@Override
	public void onPause() {
		updateHP();
		updateSpeed();
		updateInitiative();
		updateAC();
		updateBAB();
		updateCombatManeuvers();
		updateSaves();

		super.onPause();
	}

	/**
	 * Updates the view which has finished being edited
	 * 
	 * @param viewID
	 */
	public void finishedEditing(int viewID) {
		if (viewID == m_woundsEditText.getId()
				|| viewID == m_totalHPEditText.getId()
				|| viewID == m_nonLethalDmgEditText.getId()
				|| viewID == m_damageReductEditText.getId())
			updateHP();

		else if (viewID == m_baseSpeedEditText.getId())
			updateSpeed();

		else if (viewID == m_initDexEditText.getId()
				|| viewID == m_initMiscEditText.getId())
			updateInitiative();

		else if (viewID == m_armourBonusEditText.getId()
				|| viewID == m_shieldBonusEditText.getId()
				|| viewID == m_ACDexEditText.getId()
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
				|| viewID == m_CMBStrengthEditText.getId()
				|| viewID == m_CMDDexEditText.getId()
				|| viewID == m_CMBSizeEditText.getId()
				|| viewID == m_CMDMiscModEditText.getId()) {
			updateCombatManeuvers();
			updateACViews();
		}

		else if (viewID == m_fortBaseEditText.getId()
				|| viewID == m_fortAbilityModEditText.getId()
				|| viewID == m_fortMagicModEditText.getId()
				|| viewID == m_fortMiscModEditText.getId()
				|| viewID == m_fortTempModEditText.getId()) {
			updateFort();
			updateFortSaveViews();
		}

		else if (viewID == m_refBaseEditText.getId()
				|| viewID == m_refAbilityModEditText.getId()
				|| viewID == m_refMagicModEditText.getId()
				|| viewID == m_refMiscModEditText.getId()
				|| viewID == m_refTempModEditText.getId()) {
			updateRef();
			updateRefSaveViews();
		}

		else if (viewID == m_willBaseEditText.getId()
				|| viewID == m_willAbilityModEditText.getId()
				|| viewID == m_willMagicModEditText.getId()
				|| viewID == m_willMiscModEditText.getId()
				|| viewID == m_willTempModEditText.getId()) {
			updateWill();
			updateWillSaveViews();
		}

	}

	public void onFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			finishedEditing(view.getId());
		}
	}

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
		m_statsRepo.update(m_combatStats);
		PTSave[] saves = m_saveSet.getSaves();
		for(PTSave save : saves) {
			m_saveRepo.update(save);
		}
	}

	@Override
	public void loadFromDatabase() {
		m_combatStats = m_statsRepo.query(getCurrentCharacterID());
		m_saveSet = new PTSaveSet(m_saveRepo.querySet(getCurrentCharacterID()));
	}

}
