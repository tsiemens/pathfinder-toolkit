package com.lateensoft.pathfinder.toolkit.party;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;


public class PTPartyMember implements Parcelable{
	
	private String mName;
	private int mInitiative;
	private int mAC;
	private int mTouch;
	private int mFlatFooted;
	private int mSpellResist;
	private int mDamageReduction;
	private int mCMD;
	private int mFortSave;
	private int mReflexSave;
	private int mWillSave;
	private int mBluffSkillBonus;
	private int mDisguiseSkillBonus;
	private int mPerceptionSkillBonus;
	private int mSenseMotiveSkillBonus;
	private int mStealthSkillBonus;
	
	private int mRolledValue;//Cheating!
	
	public PTPartyMember(String name){
		if(name != null)
			mName = name;
		else
			mName = "";
		
		mInitiative = 0;
		mAC = 0;
		mTouch = 0;
		mFlatFooted = 0;
		mSpellResist = 0;
		mDamageReduction = 0;
		mCMD = 0;
		mFortSave = 0;
		mReflexSave = 0;
		mWillSave = 0;
		mBluffSkillBonus = 0;
		mDisguiseSkillBonus = 0;
		mPerceptionSkillBonus = 0;
		mSenseMotiveSkillBonus = 0;
		mStealthSkillBonus = 0;
		
		mRolledValue = 0; //Will be set in roller activities only
	}

	/**
	 * returns a deep copy of memberToCopy
	 */
	public PTPartyMember(PTPartyMember memberToCopy){
		mName = new String(memberToCopy.getName());
		mInitiative = memberToCopy.getInitiative();
		mAC = memberToCopy.getAC();
		mTouch = memberToCopy.getTouch();
		mFlatFooted = memberToCopy.getFlatFooted();
		mSpellResist = memberToCopy.getSpellResist();
		mDamageReduction = memberToCopy.getDamageReduction();
		mCMD = memberToCopy.getCMD();
		mFortSave = memberToCopy.getFortSave();
		mReflexSave = memberToCopy.getReflexSave();
		mWillSave = memberToCopy.getWillSave();
		mBluffSkillBonus = memberToCopy.getBluffSkillBonus();
		mDisguiseSkillBonus = memberToCopy.getDisguiseSkillBonus();
		mPerceptionSkillBonus = memberToCopy.getPerceptionSkillBonus();
		mSenseMotiveSkillBonus = memberToCopy.getSenseMotiveSkillBonus();
		mStealthSkillBonus = memberToCopy.getStealthSkillBonus();
		mRolledValue = memberToCopy.getRolledValue();
	}
	
	public PTPartyMember(Parcel in) {
		mName = in.readString();
		mInitiative = in.readInt();
		mAC = in.readInt();
		mTouch = in.readInt();
		mFlatFooted = in.readInt();
		mSpellResist = in.readInt();
		mDamageReduction = in.readInt();
		mCMD = in.readInt();
		mFortSave = in.readInt();
		mReflexSave = in.readInt();
		mWillSave = in.readInt();
		mBluffSkillBonus = in.readInt();
		mDisguiseSkillBonus = in.readInt();
		mPerceptionSkillBonus = in.readInt();
		mSenseMotiveSkillBonus = in.readInt();
		mStealthSkillBonus = in.readInt();
		mRolledValue = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeInt(mInitiative);
		out.writeInt(mAC);
		out.writeInt(mTouch);
		out.writeInt(mFlatFooted);
		out.writeInt(mSpellResist);
		out.writeInt(mDamageReduction);
		out.writeInt(mCMD);
		out.writeInt(mFortSave);
		out.writeInt(mReflexSave);
		out.writeInt(mWillSave);
		out.writeInt(mBluffSkillBonus);
		out.writeInt(mDisguiseSkillBonus);
		out.writeInt(mPerceptionSkillBonus);
		out.writeInt(mSenseMotiveSkillBonus);
		out.writeInt(mStealthSkillBonus);
		out.writeInt(mRolledValue);
	}


	/**
	 * 
	 * @return an array of the party member's stats, ordered as: 
	 * 0:Initiative,
	 * 1:AC, 
	 * 2:Touch, 
	 * 3:Flat Footed, 
	 * 4:Spell Resist, 
	 * 5:Damage Reduction, 
	 * 6:CMD,
	 * 7:Fort save
	 * 8:Reflex save
	 * 9:Will Save
	 * 10:Bluff,
	 * 11:Disguise, 
	 * 12:Perception Skill Bonus,
	 * 13:Sense Motive,
	 * 14:Stealth
	 */
	public int[] getStatsArray(){
		int[] stats = {mInitiative, mAC, mTouch, mFlatFooted, mSpellResist, mDamageReduction, mCMD, mFortSave, mReflexSave, mWillSave, 
				mBluffSkillBonus, mDisguiseSkillBonus, mPerceptionSkillBonus, mSenseMotiveSkillBonus, mStealthSkillBonus};
		return stats;
	}
	
	/**
	 * sets a stat by its assigned "index", ordered as:
	 * 0:Initiative
	 * 1:AC, 
	 * 2:Touch, 
	 * 3:Flat Footed, 
	 * 4:Spell Resist, 
	 * 5:Damage Reduction, 
	 * 6:CMD, 
	 * 7:Bluff,
	 * 8:Disguise, 
	 * 9:Perception Skill Bonus,
	 * 10:Sense Motive,
	 * 11:Stealth
	 * @param index
	 * @param value
	 */
	public void setStatByIndex(int index, int value){
		switch(index){
		case 0:
			mInitiative = value;
			break;
		case 1:
			mAC = value;
			break;
		case 2:
			mTouch = value;
			break;
		case 3:
			mFlatFooted = value;
			break;
		case 4:
			mSpellResist = value;
			break;
		case 5:
			mDamageReduction = value;
			break;
		case 6:
			mCMD = value;
			break;
		case 7:
			mFortSave = value;
			break;
		case 8:
			mReflexSave = value;
			break;
		case 9:
			mWillSave = value;
			break;
		case 10:
			mBluffSkillBonus = value;
			break;
		case 11:
			mDisguiseSkillBonus = value;
			break;
		case 12:
			mPerceptionSkillBonus = value;
			break;
		case 13:
			mSenseMotiveSkillBonus = value;
			break;
		case 14:
			mStealthSkillBonus = value;
			break;
		}
	}
	
	/**
	 * 
	 * @param index
	 * @return the value of the stat with "index", as follows:
	 * 0:Initiative
	 * 1:AC, 
	 * 2:Touch, 
	 * 3:Flat Footed, 
	 * 4:Spell Resist, 
	 * 5:Damage Reduction, 
	 * 6:CMD,
	 * 7:Fort save
	 * 8:Reflex save
	 * 9:Will save
	 * 10:Bluff,
	 * 11:Disguise, 
	 * 12:Perception Skill Bonus,
	 * 13:Sense Motive,
	 * 14:Stealth
	 * 
	 */
	public int getValueByIndex(int index){
		switch(index){
		case 0:
			return mInitiative;
		case 1:
			return mAC;
		case 2:
			return mTouch;
		case 3:
			return mFlatFooted;
		case 4:
			return mSpellResist;
		case 5:
			return mDamageReduction;
		case 6:
			return mCMD;
		case 7:
			return mFortSave;
		case 8:
			return mReflexSave;
		case 9:
			return mWillSave;
		case 10:
			return mBluffSkillBonus;
		case 11:
			return mDisguiseSkillBonus;
		case 12:
			return mPerceptionSkillBonus;
		case 13:
			return mSenseMotiveSkillBonus;
		case 14:
			return mStealthSkillBonus;
		default:
			return 0;
		}
	}
	
	public String[] getStatFields(Context context){
		Resources r = context.getResources();
		return r.getStringArray(R.array.party_member_stats);
	}
	
	public String getName() {
		if(mName != null)
			return mName;
		else{ 
			mName = new String("");
			return mName;
		}
	}

	public void setName(String name) {
		if(name != null)
			this.mName = name;
	}
	
	public int getInitiative() {
		return mInitiative;
	}

	public void setInitiative(int initiative) {
		this.mInitiative = initiative;
	}

	public int getAC() {
		return mAC;
	}

	public void setAC(int AC) {
		this.mAC = AC;
	}

	public int getTouch() {
		return mTouch;
	}

	public void setTouch(int touch) {
		this.mTouch = touch;
	}

	public int getFlatFooted() {
		return mFlatFooted;
	}

	public void setFlatFooted(int flatFooted) {
		this.mFlatFooted = flatFooted;
	}

	public int getSpellResist() {
		return mSpellResist;
	}

	public void setSpellResist(int spellResist) {
		this.mSpellResist = spellResist;
	}

	public int getDamageReduction() {
		return mDamageReduction;
	}

	public void setDamageReduction(int damageReduction) {
		this.mDamageReduction = damageReduction;
	}

	public int getCMD() {
		return mCMD;
	}

	public void setCMD(int CMD) {
		this.mCMD = CMD;
	}
	
	public int getFortSave() {
		return mFortSave;
	}

	public void setFortSave(int FortSave) {
		this.mFortSave = FortSave;
	}
	
	public int getReflexSave() {
		return mReflexSave;
	}

	public void setFeflexSave(int reflexSave) {
		this.mReflexSave = reflexSave;
	}
	
	public int getWillSave() {
		return mWillSave;
	}

	public void setWillSave(int willSave) {
		this.mWillSave = willSave;
	}

	public int getBluffSkillBonus() {
		return mBluffSkillBonus;
	}

	public void setBluffSkillBonus(int bluffSkillBonus) {
		this.mBluffSkillBonus = bluffSkillBonus;
	}
	
	public int getDisguiseSkillBonus() {
		return mDisguiseSkillBonus;
	}

	public void setDisguiseSkillBonus(int disguiseSkillBonus) {
		this.mDisguiseSkillBonus = disguiseSkillBonus;
	}
	
	public int getPerceptionSkillBonus() {
		return mPerceptionSkillBonus;
	}

	public void setPerceptionSkillBonus(int perceptionSkillBonus) {
		this.mPerceptionSkillBonus = perceptionSkillBonus;
	}
	
	public int getSenseMotiveSkillBonus() {
		return mSenseMotiveSkillBonus;
	}

	public void setSenseMotiveSkillBonus(int senseMotiveSkillBonus) {
		this.mSenseMotiveSkillBonus = senseMotiveSkillBonus;
	}
	
	public int getStealthSkillBonus() {
		return mStealthSkillBonus;
	}

	public void setStealthSkillBonus(int stealthSkillBonus) {
		this.mStealthSkillBonus = stealthSkillBonus;
	}
	
	
	public int getRolledValue() {
		return mRolledValue;
	}



	public void setRolledValue(int rolledVal) {
		this.mRolledValue = rolledVal;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTPartyMember> CREATOR = new Parcelable.Creator<PTPartyMember>() {
		public PTPartyMember createFromParcel(Parcel in) {
			return new PTPartyMember(in);
		}
		
		public PTPartyMember[] newArray(int size) {
			return new PTPartyMember[size];
		}
	};
	
}
