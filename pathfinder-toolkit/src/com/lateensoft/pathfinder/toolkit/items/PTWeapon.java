package com.lateensoft.pathfinder.toolkit.items;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;

public class PTWeapon extends PTItem {
	int mTotalAttackBonus;
	String mDamage;
	String mCritical;
	int mRange;
	String mSpecialProperties;
	int mAmmunition;
	String mType;
	String mSize;
	
	public PTWeapon() {
		super();
		mTotalAttackBonus = 0;
		mDamage = new String("");
		mCritical = new String("x2");
		mRange = 5;
		mSpecialProperties = new String("");
		mAmmunition = 0;
		mType = new String("");
		mSize = "M";
	}
	
	public PTWeapon(String name) {
		this();
		mName = name;
	}

	/**
	 * @return the mTotalAttackBonus
	 */
	public int getTotalAttackBonus() {
		return mTotalAttackBonus;
	}

	/**
	 * @param mTotalAttackBonus the mTotalAttackBonus to set
	 */
	public void setTotalAttackBonus(int mTotalAttackBonus) {
		this.mTotalAttackBonus = mTotalAttackBonus;
	}
	
	public int getSizeInt() {
		String[] sizeArray = {"S", "M", "L"};
		for(int i = 0; i < sizeArray.length; i++) {
			if(mSize.equals(sizeArray[i]))
				return i;
		}
		return 1;
	}
	
	/**
	 * @return the mDamage
	 */
	public String getDamage() {
		return mDamage;
	}

	/**
	 * @param mDamage the mDamage to set
	 */
	public void setDamage(String mDamage) {
		this.mDamage = mDamage;
	}

	/**
	 * @return the mCritical
	 */
	public String getCritical() {
		return mCritical;
	}

	/**
	 * @param mCritical the mCritical to set
	 */
	public void setCritical(String mCritical) {
		this.mCritical = mCritical;
	}

	/**
	 * @return the mRange
	 */
	public int getRange() {
		return mRange;
	}

	/**
	 * @param mRange the mRange to set
	 */
	public void setRange(int mRange) {
		this.mRange = mRange;
	}

	/**
	 * @return the mSpecialProperties
	 */
	public String getSpecialProperties() {
		return mSpecialProperties;
	}

	/**
	 * @param mSpecialProperties the mSpecialProperties to set
	 */
	public void setSpecialProperties(String mSpecialProperties) {
		this.mSpecialProperties = mSpecialProperties;
	}

	/**
	 * @return the mAmmunition
	 */
	public int getAmmunition() {
		return mAmmunition;
	}

	/**
	 * @param mAmmunition the mAmmunition to set
	 */
	public void setAmmunition(int mAmmunition) {
		this.mAmmunition = mAmmunition;
	}

	/**
	 * @return the mType
	 */
	public String getType() {
		return mType;
	}

	/**
	 * @param mType the mType to set
	 */
	public void setType(String mType) {
		this.mType = mType;
	}

	/**
	 * @return the mSize
	 */
	public String getSize() {
		return mSize;
	}

	/**
	 * @param mSize the mSize to set
	 */
	public void setSize(String mSize) {
		this.mSize = mSize;
	}

	public int getTypeInt(Context context) {
		Resources r = context.getResources();
		String[] options = r.getStringArray(R.array.weapon_type_options);
		for(int i = 0; i < options.length; i++) {
			if(mType.equals(options[i]))
				return i;
		}
		return 0;
	}
	
	
}
