package com.lateensoft.pathfinder.toolkit;

import com.lateensoft.pathfinder.toolkit.functional.PTDiceSet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PTDiceRollerFragment extends PTBasePageFragment implements OnClickListener,
		RadioGroup.OnCheckedChangeListener {

	int mCurrentDie = 4;
	int mDieQuantity = 1;
	int mRollSum = 0;
	int mRollMode;
	PTDiceSet mDiceSet;
	RadioGroup mRollTypeRadioGroup, mDieTypeRadioGroup;
	Button mRollButton, mResetButton, mDieQuantityUpButton,
			mDieQuantityDownButton;
	TextView mDieQuantityLabel, mRollResultLabel, mRollSumLabel;
	ListView mRollResultList;

	final String TAG = "PTDiceRollerActivity";

	final int ROLLMODE_SINGLE = 0;
	final int ROLLMODE_MULTI = 1;
	final int ROLLMODE_SUM = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDiceSet = new PTDiceSet();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mParentView = inflater.inflate(R.layout.fragment_dice_roller, container, false);
		setActionBarTitle(R.string.title_activity_ptdice_roller, null);
		setupContent();
		mRollMode = ROLLMODE_SINGLE;
		setRollTypeView(R.id.radiotoggleSingleRoll);
	
		return mParentView;
	}



	// Responds to roll type radio toggle buttons
	public void rollTypeClicked(View view) {
		ToggleButton toggleButton = (ToggleButton) view;

		if (toggleButton.isChecked()) {
			mRollTypeRadioGroup.check(view.getId());
			setRollTypeView(view.getId());
		} else
			toggleButton.setChecked(true);
	}

	// Takes the id of a roll type radio button (usually the one just pressed)
	// and sets the rest of the layout accordingly
	public void setRollTypeView(int rollTypeButtonId) {
		switch (rollTypeButtonId) {
		case R.id.radiotoggleSingleRoll:
			mRollMode = ROLLMODE_SINGLE;
			mResetButton.setEnabled(false);
			mDieQuantityUpButton.setEnabled(false);
			mDieQuantityDownButton.setEnabled(false);
			mDieQuantityLabel.setEnabled(false);
			mRollSumLabel.setVisibility(View.INVISIBLE);
			mRollResultList.setVisibility(View.INVISIBLE);
			break;
		case R.id.radiotoggleMultiRoll:
			mRollMode = ROLLMODE_MULTI;
			mResetButton.setEnabled(true);
			mDieQuantityUpButton.setEnabled(true);
			mDieQuantityDownButton.setEnabled(true);
			mDieQuantityLabel.setEnabled(true);
			mRollSumLabel.setVisibility(View.INVISIBLE);
			mRollResultList.setVisibility(View.VISIBLE);
			break;
		case R.id.radiotoggleSumRoll:
			mRollMode = ROLLMODE_SUM;
			mResetButton.setEnabled(true);
			mDieQuantityUpButton.setEnabled(true);
			mDieQuantityDownButton.setEnabled(true);
			mDieQuantityLabel.setEnabled(true);
			mRollSumLabel.setVisibility(View.VISIBLE);
			mRollResultList.setVisibility(View.VISIBLE);
			break;
		}
	}

	// Responds to die type radio toggle buttons
	public void dieTypeClicked(View view) {
		ToggleButton toggleButton = (ToggleButton) view;

		if (toggleButton.isChecked()) {
			mDieTypeRadioGroup.check(view.getId());
			switch (view.getId()) {
			case R.id.radiotoggleD4Die:
				mCurrentDie = 4;
				break;
			case R.id.radiotoggleD6Die:
				mCurrentDie = 6;
				break;
			case R.id.radiotoggleD8Die:
				mCurrentDie = 8;
				break;
			case R.id.radiotoggleD10Die:
				mCurrentDie = 10;
				break;
			case R.id.radiotoggleD12Die:
				mCurrentDie = 12;
				break;
			case R.id.radiotoggleD20Die:
				mCurrentDie = 20;
				break;
			case R.id.radiotoggleD100Die:
				mCurrentDie = 100;
				break;
			}
		} else
			toggleButton.setChecked(true);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		ToggleButton toggleButton;
		for (int j = 0; j < group.getChildCount(); j++) {
			toggleButton = (ToggleButton) group.getChildAt(j);
			toggleButton.setChecked(toggleButton.getId() == checkedId);
		}
	}

	public void resetRolls() {
		mRollSum = 0;
		mRollSumLabel.setText(getString(R.string.roller_sum_label) + " "
				+ mRollSum);
		mDieQuantity = 1;
		mDieQuantityLabel.setText(Integer.toString(mDieQuantity));
		refreshRollsListView(null);
	}

	// Rolls the dice according to the current settings and updates the screen
	public void rollDice() {
		int rollResult;
		int[] rolls = null;
		
		switch (mRollMode) {
		case ROLLMODE_SINGLE:
			mRollResultLabel.setText(Integer.toString(mDiceSet
					.singleRoll(mCurrentDie)));
			break;
		case ROLLMODE_MULTI:
			rolls = mDiceSet.multiRollWithResults(mDieQuantity, mCurrentDie);
			rollResult = getMultiRollSum(rolls);
			mRollResultLabel.setText(Integer.toString(rollResult));
			refreshRollsListView(rolls);
			break;
		case ROLLMODE_SUM:
			rolls = mDiceSet.multiRollWithResults(mDieQuantity, mCurrentDie);
			rollResult = getMultiRollSum(rolls);
			mRollSum += rollResult;
			if (mRollSum > getResources().getInteger(R.integer.max_roll_sum))
				mRollSum = getResources().getInteger(R.integer.max_roll_sum);
			mRollSumLabel.setText(getString(R.string.roller_sum_label) + " "
					+ mRollSum);
			mRollResultLabel.setText(Integer.toString(rollResult));
			refreshRollsListView(rolls);
			break;
		}
	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.buttonRoll:
			rollDice();
			break;
		case R.id.buttonReset:
			resetRolls();
			break;
		case R.id.buttonPlusDiceNumber:
			if (mDieQuantity < getResources().getInteger(
					R.integer.max_dice_per_roll)) {
				mDieQuantity++;
				mDieQuantityLabel.setText(Integer.toString(mDieQuantity));
			}
			break;
		case R.id.buttonMinusDiceNumber:
			if (mDieQuantity > 1) {
				mDieQuantity--;
				mDieQuantityLabel.setText(Integer.toString(mDieQuantity));
			}
			break;
		default:
			if ("roll_type_toggle".equals(view.getTag())) {
				rollTypeClicked(view);
			} else if ("die_type_toggle".equals(view.getTag())) {
				dieTypeClicked(view);
			}
			break;
		}

	}
	
	private int getMultiRollSum(int[] rollResults){
		int rollSum = 0;
		if(rollResults != null){
			for(int i = 0; i < rollResults.length; i++){
				rollSum += rollResults[i];
			}
		}
		return rollSum;
	}
	
	private void refreshRollsListView(int[] rollResults){
		if(rollResults != null){
			String[] rollResultStrings = new String[rollResults.length];
			for(int i = 0; i < rollResults.length; i++)
				rollResultStrings[i] = Integer.toString(rollResults[i]);
				
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, rollResultStrings);
			mRollResultList.setAdapter(adapter);
		}
		else{
			String[] emptyArray = new String[0];
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emptyArray);
			mRollResultList.setAdapter(adapter);
		}
	}

	private void setupContent() {
		mRollTypeRadioGroup = (RadioGroup) mParentView.findViewById(R.id.toggleGroupRollType);
		mRollTypeRadioGroup.setOnCheckedChangeListener(this);
		
		for (int i = 0; i < mRollTypeRadioGroup.getChildCount(); i++) {
			mRollTypeRadioGroup.getChildAt(i).setOnClickListener(this);
		}

		mDieTypeRadioGroup = (RadioGroup) mParentView.findViewById(R.id.toggleGroupDieType);
		mDieTypeRadioGroup.setOnCheckedChangeListener(this);
		
		for (int i = 0; i < mDieTypeRadioGroup.getChildCount(); i++) {
			mDieTypeRadioGroup.getChildAt(i).setOnClickListener(this);
		}

		mDieQuantityUpButton = (Button) mParentView.findViewById(R.id.buttonPlusDiceNumber);
		mDieQuantityUpButton.setOnClickListener(this);
		mDieQuantityDownButton = (Button) mParentView.findViewById(R.id.buttonMinusDiceNumber);
		mDieQuantityDownButton.setOnClickListener(this);

		mRollButton = (Button) mParentView.findViewById(R.id.buttonRoll);
		mRollButton.setOnClickListener(this);
		mResetButton = (Button) mParentView.findViewById(R.id.buttonReset);
		mResetButton.setOnClickListener(this);

		mDieQuantityLabel = (TextView) mParentView.findViewById(R.id.textViewDiceNumberPicker);
		mDieQuantityLabel.setText(Integer.toString(mDieQuantity));

		mRollResultLabel = (TextView) mParentView.findViewById(R.id.textViewRollResult);

		mRollSumLabel = (TextView) mParentView.findViewById(R.id.textViewRollSum);
		
		mRollResultList = (ListView) mParentView.findViewById(R.id.rollResultListView);
		
		resetRolls();
	}
}
