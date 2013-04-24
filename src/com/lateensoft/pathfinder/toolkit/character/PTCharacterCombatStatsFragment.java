package com.lateensoft.pathfinder.toolkit.character;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lateensoft.pathfinder.toolkit.R;

public class PTCharacterCombatStatsFragment extends PTCharacterSheetFragment implements OnFocusChangeListener, OnEditorActionListener {

	final String TAG = "PTCharacterCombatStatsFragment";
	
	static final int STR_KEY = 0;
	static final int DEX_KEY = 1;
	static final int CON_KEY = 2;
	static final int INT_KEY = 3;
	static final int WIS_KEY = 4;
	static final int CHA_KEY = 5;
	
	static final int FORT_KEY = 0;
	static final int REF_KEY = 1;
	static final int WILL_KEY = 2;

	private TextView mCurrentHPTextView;
	private EditText mTotalHPEditText;
	private EditText mDamageReductEditText;
	private EditText mWoundsEditText;
	private EditText mNonLethalDmgEditText;

	private EditText mBaseSpeedEditText;

	private TextView mInitTextView;
	private EditText mInitDexEditText;
	private EditText mInitMiscEditText;

	private TextView mACTextView;
	private EditText mArmourBonusEditText;
	private EditText mShieldBonusEditText;
	private EditText mACDexEditText;
	private EditText mACSizeEditText;
	private EditText mNaturalArmourEditText;
	private EditText mDeflectEditText;
	private EditText mACMiscEditText;
	private TextView mACTouchTextView;
	private TextView mACFFTextView;
	private EditText mSpellResistEditText;

	private EditText mBABPrimaryEditText;
	private EditText mBABSecondaryEditText;
	private TextView mCMBTextView;
	private EditText mCmbBABEditText;
	private EditText mCMBStrengthEditText;
	private EditText mCMBSizeEditText;
	private TextView mCMDTextView;
	private EditText mCMDDexEditText;
	
	private TextView mFortTextView;
	private EditText mFortBaseEditText;
	private EditText mFortAbilityModEditText;
	private EditText mFortMagicModEditText;
	private EditText mFortMiscModEditText;
	private EditText mFortTempModEditText;
	
	private TextView mRefTextView;
	private EditText mRefBaseEditText;
	private EditText mRefAbilityModEditText;
	private EditText mRefMagicModEditText;
	private EditText mRefMiscModEditText;
	private EditText mRefTempModEditText;
	
	private TextView mWillTextView;
	private EditText mWillBaseEditText;
	private EditText mWillAbilityModEditText;
	private EditText mWillMagicModEditText;
	private EditText mWillMiscModEditText;
	private EditText mWillTempModEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View fragmentView = inflater.inflate(
				R.layout.character_combat_stats_fragment, container, false);
		setupViews(fragmentView);

		return fragmentView;
	}
	
	
	public void updateAllViews(){
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
	public void updateHP(){
		mCharacter.getCombatStatSet().setTotalHP(getEditTextInt(mTotalHPEditText));
		mCharacter.getCombatStatSet().setWounds(getEditTextInt(mWoundsEditText));
		mCharacter.getCombatStatSet().setNonLethalDamage(getEditTextInt(mNonLethalDmgEditText));
		mCharacter.getCombatStatSet().setDamageReduction(getEditTextInt(mDamageReductEditText));
		updateHPViews();
	}
	
	/**
	 * Updates all views for HP
	 */
	public void updateHPViews(){
		setIntText(mCurrentHPTextView, mCharacter.getCombatStatSet().getCurrentHP());
		setIntText(mTotalHPEditText, mCharacter.getCombatStatSet().getTotalHP());
		setIntText(mWoundsEditText, mCharacter.getCombatStatSet().getWounds());
		setIntText(mNonLethalDmgEditText, mCharacter.getCombatStatSet().getNonLethalDamage());
		setIntText(mDamageReductEditText, mCharacter.getCombatStatSet().getDamageReduction());
	}
	
	/**
	 * Updates all stats for speed
	 */
	public void updateSpeed(){
		mCharacter.getCombatStatSet().setBaseSpeed(getEditTextInt(mBaseSpeedEditText));
	}
	
	/**
	 * Updates all views for speed
	 */
	public void updateSpeedViews(){
		setIntText(mBaseSpeedEditText, mCharacter.getCombatStatSet().getBaseSpeed());
	}
	
	/**
	 * Updates all stats for initiative
	 */
	public void updateInitiative(){
		mCharacter.getCombatStatSet().setInitDexMod(getEditTextInt(mInitDexEditText));
		mCharacter.getCombatStatSet().setInitiativeMiscMod(getEditTextInt(mInitMiscEditText));
		updateInitiativeViews();
	}
	
	/**
	 * Updates all views for initiative
	 */
	public void updateInitiativeViews(){
		setIntText(mInitTextView, mCharacter.getCombatStatSet().getInitiativeMod());
		setIntText(mInitDexEditText, mCharacter.getCombatStatSet().getInitDexMod());
		setIntText(mInitMiscEditText, mCharacter.getCombatStatSet().getInitiativeMiscMod());
	}
	
	/**
	 * Updates all stats for AC
	 */
	public void updateAC(){
		mCharacter.getCombatStatSet().setACArmourBonus(getEditTextInt(mArmourBonusEditText));
		mCharacter.getCombatStatSet().setACShieldBonus(getEditTextInt(mShieldBonusEditText));
		mCharacter.getCombatStatSet().setACDexMod(getEditTextInt(mACDexEditText));
		mCharacter.getCombatStatSet().setSizeModifier(getEditTextInt(mACSizeEditText));
		mCharacter.getCombatStatSet().setNaturalArmour(getEditTextInt(mNaturalArmourEditText));
		mCharacter.getCombatStatSet().setDeflectionMod(getEditTextInt(mDeflectEditText));
		mCharacter.getCombatStatSet().setACMiscMod(getEditTextInt(mACMiscEditText));
		mCharacter.getCombatStatSet().setSpellResistance(getEditTextInt(mSpellResistEditText));
		updateACViews();
	}
	
	/**
	 * Updates all views for ac
	 */
	public void updateACViews(){
		setIntText(mACTextView, mCharacter.getCombatStatSet().getTotalAC());
		setIntText(mACTouchTextView, mCharacter.getCombatStatSet().getTouchAC());
		setIntText(mACFFTextView, mCharacter.getCombatStatSet().getFlatFootedAC());
		setIntText(mArmourBonusEditText, mCharacter.getCombatStatSet().getACArmourBonus());
		setIntText(mShieldBonusEditText, mCharacter.getCombatStatSet().getACShieldBonus());
		setIntText(mACDexEditText, mCharacter.getCombatStatSet().getACDexMod());
		updateSizeModViews();
		setIntText(mNaturalArmourEditText, mCharacter.getCombatStatSet().getNaturalArmour());
		setIntText(mDeflectEditText, mCharacter.getCombatStatSet().getDeflectionMod());
		setIntText(mACMiscEditText, mCharacter.getCombatStatSet().getACMiscMod());
		setIntText(mSpellResistEditText, mCharacter.getCombatStatSet().getSpellResist());
	}
	
	/**
	 * Updates all stats for BAB
	 */
	public void updateBAB(){
		mCharacter.getCombatStatSet().setBABPrimary(getEditTextInt(mBABPrimaryEditText));
		mCharacter.getCombatStatSet().setBABSecondary(mBABSecondaryEditText.getText().toString());
		updateBABViews();
	}
	
	public void updateBABViews(){
		updateCombatManeuverViews();
		mBABSecondaryEditText.setText(mCharacter.getCombatStatSet().getBABSecondary());
	}
	
	/**
	 * Updates all stats for combat maneuvers
	 */
	public void updateCombatManeuvers(){
		mCharacter.getCombatStatSet().setBABPrimary(getEditTextInt(mCmbBABEditText));
		mCharacter.getCombatStatSet().setStrengthMod(getEditTextInt(mCMBStrengthEditText));
		mCharacter.getCombatStatSet().setSizeModifier(getEditTextInt(mCMBSizeEditText));
		mCharacter.getCombatStatSet().setCMDDexMod(getEditTextInt(mCMDDexEditText));
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
		mCharacter.getSaveSet().getSave(0).setBase(getEditTextInt(mFortBaseEditText));
		mCharacter.getSaveSet().getSave(0).setAbilityMod(getEditTextInt(mFortAbilityModEditText));
		mCharacter.getSaveSet().getSave(0).setMagicMod(getEditTextInt(mFortMagicModEditText));
		mCharacter.getSaveSet().getSave(0).setMiscMod(getEditTextInt(mFortMiscModEditText));
		mCharacter.getSaveSet().getSave(0).setTempMod(getEditTextInt(mFortTempModEditText));
	}
	
	public void updateRef() {
		mCharacter.getSaveSet().getSave(1).setBase(getEditTextInt(mRefBaseEditText));
		mCharacter.getSaveSet().getSave(1).setAbilityMod(getEditTextInt(mRefAbilityModEditText));
		mCharacter.getSaveSet().getSave(1).setMagicMod(getEditTextInt(mRefMagicModEditText));
		mCharacter.getSaveSet().getSave(1).setMiscMod(getEditTextInt(mRefMiscModEditText));
		mCharacter.getSaveSet().getSave(1).setTempMod(getEditTextInt(mRefTempModEditText));
	}
	
	public void updateWill() {
		mCharacter.getSaveSet().getSave(2).setBase(getEditTextInt(mWillBaseEditText));
		mCharacter.getSaveSet().getSave(2).setAbilityMod(getEditTextInt(mWillAbilityModEditText));
		mCharacter.getSaveSet().getSave(2).setMagicMod(getEditTextInt(mWillMagicModEditText));
		mCharacter.getSaveSet().getSave(2).setMiscMod(getEditTextInt(mWillMiscModEditText));
		mCharacter.getSaveSet().getSave(2).setTempMod(getEditTextInt(mWillTempModEditText));
	}
	
	/**
	 * Updates all views for combat maneuvers
	 */
	public void updateCombatManeuverViews(){
		setIntText(mCMBTextView, mCharacter.getCombatStatSet().getCombatManeuverBonus());
		updatePrimaryBABViews();
		setIntText(mCMBStrengthEditText, mCharacter.getCombatStatSet().getStrengthMod());
		updateSizeModViews();
		setIntText(mCMDTextView, mCharacter.getCombatStatSet().getCombatManeuverDefense());
		setIntText(mCMDDexEditText, mCharacter.getCombatStatSet().getCMDDexMod());	
	}
	
	/**
	 * Updates the BAB edit texts in BAB and CMB
	 */
	public void updatePrimaryBABViews(){
		setIntText(mBABPrimaryEditText, mCharacter.getCombatStatSet().getBABPrimary());
		setIntText(mCmbBABEditText, mCharacter.getCombatStatSet().getBABPrimary());
	}
	
	/**
	 * Updates the size mod edit texts in AC and CMB
	 */
	public void updateSizeModViews(){
		setIntText(mACSizeEditText, mCharacter.getCombatStatSet().getSizeModifier());
		setIntText(mCMBSizeEditText, mCharacter.getCombatStatSet().getSizeModifier());
	}
	

	public void setIntText(TextView textView, int number) {
		textView.setText(Integer.toString(number));
	}
	
	public void updateSaveViews() {
		updateFortSaveViews();
		updateRefSaveViews();
		updateWillSaveViews();		
	}
	
	public void updateFortSaveViews() {
		setIntText(mFortTextView, mCharacter.getSaveSet().getSave(0).getTotal());
		setIntText(mFortBaseEditText, mCharacter.getSaveSet().getSave(0).getBase());
		setIntText(mFortAbilityModEditText, mCharacter.getSaveSet().getSave(0).getAbilityMod());
		setIntText(mFortMagicModEditText, mCharacter.getSaveSet().getSave(0).getMagicMod());
		setIntText(mFortMiscModEditText, mCharacter.getSaveSet().getSave(0).getMiscMod());
		setIntText(mFortTempModEditText, mCharacter.getSaveSet().getSave(0).getTempMod());
	}
	
	public void updateRefSaveViews() {
		setIntText(mRefTextView, mCharacter.getSaveSet().getSave(1).getTotal());
		setIntText(mRefBaseEditText, mCharacter.getSaveSet().getSave(1).getBase());
		setIntText(mRefAbilityModEditText, mCharacter.getSaveSet().getSave(1).getAbilityMod());
		setIntText(mRefMagicModEditText, mCharacter.getSaveSet().getSave(1).getMagicMod());
		setIntText(mRefMiscModEditText, mCharacter.getSaveSet().getSave(1).getMiscMod());
		setIntText(mRefTempModEditText, mCharacter.getSaveSet().getSave(1).getTempMod());
	}
	
	public void updateWillSaveViews() {
		setIntText(mWillTextView, mCharacter.getSaveSet().getSave(2).getTotal());
		setIntText(mWillBaseEditText, mCharacter.getSaveSet().getSave(2).getBase());
		setIntText(mWillAbilityModEditText, mCharacter.getSaveSet().getSave(2).getAbilityMod());
		setIntText(mWillMagicModEditText, mCharacter.getSaveSet().getSave(2).getMagicMod());
		setIntText(mWillMiscModEditText, mCharacter.getSaveSet().getSave(2).getMiscMod());
		setIntText(mWillTempModEditText, mCharacter.getSaveSet().getSave(2).getTempMod());
	}

	/**
	 * 
	 * @param editText
	 * @return the value in the edit text. Returns Integer.MAX_VALUE if the parse failed
	 */
	public int getEditTextInt(EditText editText){
		try{
			return Integer.parseInt(editText.getText().toString());
		}catch (NumberFormatException e){
			return 0;
		}
	}
	
	//Sets edittext listeners
	public void setEditTextListeners(EditText editText){
		editText.setOnFocusChangeListener(this);
		editText.setOnEditorActionListener(this);
	}

	// Sets up all the text and edit texts
	public void setupViews(View fragmentView) {
		mCurrentHPTextView = (TextView) fragmentView
				.findViewById(R.id.textViewCurrentHP);
		mTotalHPEditText = (EditText) fragmentView
				.findViewById(R.id.editTextTotalHP);
		setEditTextListeners(mTotalHPEditText);
		
		mDamageReductEditText = (EditText) fragmentView
				.findViewById(R.id.editTextDamageReduction);
		setEditTextListeners(mDamageReductEditText);
		
		mWoundsEditText = (EditText) fragmentView
				.findViewById(R.id.editTextWounds);
		setEditTextListeners(mWoundsEditText);
		
		mNonLethalDmgEditText = (EditText) fragmentView
				.findViewById(R.id.editTextNonLethalDmg);
		setEditTextListeners(mNonLethalDmgEditText);

		mBaseSpeedEditText = (EditText) fragmentView
				.findViewById(R.id.editTextBaseSpeed);
		setEditTextListeners(mBaseSpeedEditText);

		mInitTextView = (TextView) fragmentView
				.findViewById(R.id.textViewInitiative);
		mInitDexEditText = (EditText) fragmentView
				.findViewById(R.id.editTextInitDexMod);
		setEditTextListeners(mInitDexEditText);
		
		mInitMiscEditText = (EditText) fragmentView
				.findViewById(R.id.editTextInitMiscMod);
		setEditTextListeners(mInitMiscEditText);

		mACTextView = (TextView) fragmentView.findViewById(R.id.textViewAC);
		mArmourBonusEditText = (EditText) fragmentView
				.findViewById(R.id.editTextArmourBonus);
		setEditTextListeners(mArmourBonusEditText);
		
		mShieldBonusEditText = (EditText) fragmentView
				.findViewById(R.id.editTextShieldBonus);
		setEditTextListeners(mShieldBonusEditText);
		
		mACDexEditText = (EditText) fragmentView
				.findViewById(R.id.editTextACDexMod);
		setEditTextListeners(mACDexEditText);
		
		mACSizeEditText = (EditText) fragmentView
				.findViewById(R.id.editTextACSizeMod);
		setEditTextListeners(mACSizeEditText);
		
		mNaturalArmourEditText = (EditText) fragmentView
				.findViewById(R.id.editTextNaturalArmour);
		setEditTextListeners(mNaturalArmourEditText);
		
		mDeflectEditText = (EditText) fragmentView
				.findViewById(R.id.editTextDeflectionMod);
		setEditTextListeners(mDeflectEditText);
		
		mACMiscEditText = (EditText) fragmentView
				.findViewById(R.id.editTextACMiscMod);
		setEditTextListeners(mACMiscEditText);
		
		mACTouchTextView = (TextView) fragmentView
				.findViewById(R.id.textViewTouchAC);
		mACFFTextView = (TextView) fragmentView
				.findViewById(R.id.textViewFlatFootedAC);
		mSpellResistEditText = (EditText) fragmentView
				.findViewById(R.id.editTextSpellResist);
		setEditTextListeners(mSpellResistEditText);
		

		mBABPrimaryEditText = (EditText) fragmentView
				.findViewById(R.id.editTextBABPrimary);
		setEditTextListeners(mBABPrimaryEditText);
		
		mBABSecondaryEditText = (EditText) fragmentView
				.findViewById(R.id.editTextBABSecondary);
		setEditTextListeners(mBABSecondaryEditText);
		
		mCMBTextView = (TextView) fragmentView.findViewById(R.id.textViewCMB);
		mCmbBABEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCmbBAB);
		setEditTextListeners(mCmbBABEditText);
		
		mCMBStrengthEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMBStrengthMod);
		setEditTextListeners(mCMBStrengthEditText);
		
		mCMBSizeEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMBSizeMod);
		setEditTextListeners(mCMBSizeEditText);
		
		mCMDTextView = (TextView) fragmentView.findViewById(R.id.textViewCMD);
		mCMDDexEditText = (EditText) fragmentView
				.findViewById(R.id.editTextCMDDex);
		setEditTextListeners(mCMDDexEditText);
		
		mFortTextView = (TextView) fragmentView.findViewById(R.id.tvFort);
		mFortBaseEditText = (EditText) fragmentView.findViewById(R.id.etSaveFortBase);
		mFortAbilityModEditText = (EditText) fragmentView.findViewById(R.id.etSaveFortAbilityMod);
		mFortMagicModEditText = (EditText) fragmentView.findViewById(R.id.etSaveFortMagicMod);
		mFortMiscModEditText = (EditText) fragmentView.findViewById(R.id.etSaveFortMiscMod);
		mFortTempModEditText = (EditText) fragmentView.findViewById(R.id.etSaveFortTempMod);
		setEditTextListeners(mFortBaseEditText);
		setEditTextListeners(mFortAbilityModEditText);
		setEditTextListeners(mFortMagicModEditText);
		setEditTextListeners(mFortMiscModEditText);
		setEditTextListeners(mFortTempModEditText);
		
		mRefTextView = (TextView) fragmentView.findViewById(R.id.tvRef);
		mRefBaseEditText = (EditText) fragmentView.findViewById(R.id.etSaveRefBase);
		mRefAbilityModEditText = (EditText) fragmentView.findViewById(R.id.etSaveRefAbilityMod);
		mRefMagicModEditText = (EditText) fragmentView.findViewById(R.id.etSaveRefMagicMod);
		mRefMiscModEditText = (EditText) fragmentView.findViewById(R.id.etSaveRefMiscMod);
		mRefTempModEditText = (EditText) fragmentView.findViewById(R.id.etSaveRefTempMod);
		setEditTextListeners(mRefBaseEditText);
		setEditTextListeners(mRefAbilityModEditText);
		setEditTextListeners(mRefMagicModEditText);
		setEditTextListeners(mRefMiscModEditText);
		setEditTextListeners(mRefTempModEditText);
		
		mWillTextView = (TextView) fragmentView.findViewById(R.id.tvWill);
		mWillBaseEditText = (EditText) fragmentView.findViewById(R.id.etSaveWillBase);
		mWillAbilityModEditText = (EditText) fragmentView.findViewById(R.id.etSaveWillAbilityMod);
		mWillMagicModEditText = (EditText) fragmentView.findViewById(R.id.etSaveWillMagicMod);
		mWillMiscModEditText = (EditText) fragmentView.findViewById(R.id.etSaveWillMiscMod);
		mWillTempModEditText = (EditText) fragmentView.findViewById(R.id.etSaveWillTempMod);
		setEditTextListeners(mWillBaseEditText);
		setEditTextListeners(mWillAbilityModEditText);
		setEditTextListeners(mWillMagicModEditText);
		setEditTextListeners(mWillMiscModEditText);
		setEditTextListeners(mWillTempModEditText);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateAllViews();
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
	 * @param viewID
	 */
	public void finishedEditing(int viewID){
		if(viewID == mWoundsEditText.getId() || viewID == mTotalHPEditText.getId() 
				|| viewID == mNonLethalDmgEditText.getId() || viewID == mDamageReductEditText.getId())
			updateHP();
		
		else if(viewID == mBaseSpeedEditText.getId())
			updateSpeed();

		else if(viewID == mInitDexEditText.getId() || viewID == mInitMiscEditText.getId())
			updateInitiative();

		else if(viewID == mArmourBonusEditText.getId() || viewID == mShieldBonusEditText.getId() 
				|| viewID == mACDexEditText.getId() || viewID == mACSizeEditText.getId()
				|| viewID == mNaturalArmourEditText.getId() || viewID == mDeflectEditText.getId()
				|| viewID ==mACMiscEditText.getId() || viewID == mSpellResistEditText.getId()){
			updateAC();		
			updateCombatManeuverViews();
		}
		
		else if(viewID == mBABPrimaryEditText.getId() || viewID == mBABSecondaryEditText.getId())
			updateBAB();
		
		else if(viewID == mCmbBABEditText.getId() || viewID == mCMBStrengthEditText.getId()
				|| viewID == mCMDDexEditText.getId() || viewID == mCMBSizeEditText.getId()){
			updateCombatManeuvers();
			updateACViews();
		}
		
		else if(viewID == mFortBaseEditText.getId() || viewID == mFortAbilityModEditText.getId()
				|| viewID == mFortMagicModEditText.getId() || viewID == mFortMiscModEditText.getId()
				|| viewID == mFortTempModEditText.getId()) {
			updateFort();
			updateFortSaveViews();
		}
		
		else if(viewID == mRefBaseEditText.getId() || viewID == mRefAbilityModEditText.getId()
				|| viewID == mRefMagicModEditText.getId() || viewID == mRefMiscModEditText.getId()
				|| viewID == mRefTempModEditText.getId()) {
			updateRef();
			updateRefSaveViews();
		}
		
		else if(viewID == mWillBaseEditText.getId() || viewID == mWillAbilityModEditText.getId()
				|| viewID == mWillMagicModEditText.getId() || viewID == mWillMiscModEditText.getId()
				|| viewID == mWillTempModEditText.getId()) {
			updateWill();
			updateWillSaveViews();
		}
		
	}

	public void onFocusChange(View view, boolean hasFocus) {
		if(!hasFocus){
			finishedEditing(view.getId());
		}	
	}
	
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		finishedEditing(view.getId());
		return false;
	}

}
