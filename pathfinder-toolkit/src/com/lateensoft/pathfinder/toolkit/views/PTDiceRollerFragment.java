package com.lateensoft.pathfinder.toolkit.views;

import android.content.Context;
import android.widget.*;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.utils.PTDiceSet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PTDiceRollerFragment extends PTBasePageFragment {
    @SuppressWarnings("unused")
    private static final String TAG = PTDiceRollerFragment.class.getSimpleName();
    private static final int[] DIE_VALUES = {4, 6, 8, 10, 12, 20, 100};
    private static final int MAX_ROLL_SUM = 9999;

    private int m_rollSum = 0;
    private RollMode m_rollMode;
    private PTDiceSet m_diceSet;
    
    private RadioGroup m_rollTypeRadioGroup;
    private Spinner m_dieTypeSpinner, m_dieQuantitySpinner;
    private Button m_rollButton, m_resetButton;
    private TextView m_dieQuantityTv, m_rollResultTv, m_rollSumTv;
    private ListView m_rollResultList;

    private static enum RollMode {SINGLE, XDY, CUMULATIVE}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_diceSet = new PTDiceSet();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRootView(inflater.inflate(R.layout.fragment_dice_roller, container, false));
		
		setupContent();
        setRollMode(RollMode.SINGLE);
		return getRootView();
	}

    @Override
    public void updateTitle() {
        setTitle(R.string.title_activity_ptdice_roller);
        setSubtitle(null);
    }

    private void setupContent() {
        m_rollTypeRadioGroup = (RadioGroup) getRootView().findViewById(R.id.toggleGroupRollType);

        RollTypeClickListener rollTypeClickListener = new RollTypeClickListener();
        for (int i = 0; i < m_rollTypeRadioGroup.getChildCount(); i++) {
            m_rollTypeRadioGroup.getChildAt(i).setOnClickListener(rollTypeClickListener);
        }

        m_dieTypeSpinner = (Spinner) getRootView().findViewById(R.id.spinnerDieType);
        setupSpinner(m_dieTypeSpinner, R.array.roller_die_types, 0);

        m_dieQuantityTv = (TextView) getRootView().findViewById(R.id.tvDieQuantity);
        m_dieQuantitySpinner = (Spinner) getRootView().findViewById(R.id.spinnerDieQuantity);
        setupSpinner(m_dieQuantitySpinner, R.array.roller_die_quantities, 0);

        m_rollButton = (Button) getRootView().findViewById(R.id.buttonRoll);
        m_rollButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                rollDice();
            }
        });

        m_resetButton = (Button) getRootView().findViewById(R.id.buttonReset);
        m_resetButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                resetRolls();
            }
        });

        m_rollResultTv = (TextView) getRootView().findViewById(R.id.textViewRollResult);
        m_rollSumTv = (TextView) getRootView().findViewById(R.id.textViewRollSum);
        m_rollResultList = (ListView) getRootView().findViewById(R.id.rollResultListView);

        resetRolls();
    }

	public void setRollMode(RollMode rollMode) {
		switch (rollMode) {
		case SINGLE:
			m_rollMode = RollMode.SINGLE;
			m_resetButton.setEnabled(false);
            m_dieQuantitySpinner.setEnabled(false);
            m_dieQuantityTv.setEnabled(false);
			m_rollSumTv.setVisibility(View.INVISIBLE);
			m_rollResultList.setVisibility(View.GONE);
			break;
		case XDY:
			m_rollMode = RollMode.XDY;
			m_resetButton.setEnabled(true);
            m_dieQuantitySpinner.setEnabled(true);
            m_dieQuantityTv.setEnabled(true);
			m_rollSumTv.setVisibility(View.INVISIBLE);
			m_rollResultList.setVisibility(View.VISIBLE);
			break;
		case CUMULATIVE:
			m_rollMode = RollMode.CUMULATIVE;
			m_resetButton.setEnabled(true);
			m_dieQuantitySpinner.setEnabled(true);
            m_dieQuantityTv.setEnabled(true);
			m_rollSumTv.setVisibility(View.VISIBLE);
			m_rollResultList.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void resetRolls() {
		m_rollSum = 0;
		m_rollSumTv.setText(getString(R.string.roller_sum_label) + " "
                + m_rollSum);
		m_dieQuantitySpinner.setSelection(0);
		refreshRollsListView(null);
	}

	public void rollDice() {
		int rollResult;
		int[] rolls;

        switch (m_rollMode) {
            case SINGLE:
                m_rollResultTv.setText(Integer.toString(m_diceSet
                        .singleRoll(getDieType())));
                break;
            case XDY:
                rolls = m_diceSet.multiRollWithResults(getDieQuantity(), getDieType());
                rollResult = sumIntArray(rolls);
                m_rollResultTv.setText(Integer.toString(rollResult));
                refreshRollsListView(rolls);
                break;
            case CUMULATIVE:
                rolls = m_diceSet.multiRollWithResults(getDieQuantity(), getDieType());
                rollResult = sumIntArray(rolls);
                m_rollSum += rollResult;
                if (m_rollSum > MAX_ROLL_SUM)
                    m_rollSum = MAX_ROLL_SUM;
                m_rollSumTv.setText(getString(R.string.roller_sum_label) + " "
                        + m_rollSum);
                m_rollResultTv.setText(Integer.toString(rollResult));
                refreshRollsListView(rolls);
                break;
            default:
                throw new IllegalStateException("Invalid roll mode: "+m_rollMode);
        }
    }

    private int getDieQuantity() {
        return m_dieQuantitySpinner.getSelectedItemPosition() + 1;
    }

    private int getDieType() {
        int position = m_dieTypeSpinner.getSelectedItemPosition();
        if (position < DIE_VALUES.length) {
           return DIE_VALUES[position];
        } else {
            throw new IllegalStateException("Illegal die selected at spinner position "+position);
        }
    }

	private int sumIntArray(int[] rollResults){
		int rollSum = 0;
		if(rollResults != null){
			for(int i : rollResults){
				rollSum += i;
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
			m_rollResultList.setAdapter(adapter);
		} else{
			String[] emptyArray = new String[0];
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emptyArray);
			m_rollResultList.setAdapter(adapter);
		}
	}

    private class RollTypeClickListener implements OnClickListener {
        @Override public void onClick(View view) {
            ToggleButton toggleButton;
            for (int i = 0; i < m_rollTypeRadioGroup.getChildCount(); i++) {
                toggleButton = (ToggleButton) m_rollTypeRadioGroup.getChildAt(i);
                toggleButton.setChecked(toggleButton.getId() == view.getId());
            }
            switch (view.getId()) {
                case R.id.radiotoggleSingleRoll:
                    setRollMode(RollMode.SINGLE);
                    break;
                case R.id.radiotoggleMultiRoll:
                    setRollMode(RollMode.XDY);
                    break;
                case R.id.radiotoggleSumRoll:
                    setRollMode(RollMode.CUMULATIVE);
                    break;
            }
        }
    }

    private void setupSpinner(Spinner spinner, int optionResourceId, int defaultSelection) {
        Context context = getActivity();
        if (context != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    optionResourceId, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(R.layout.spinner_plain);
            spinner.setAdapter(adapter);
            spinner.setSelection(defaultSelection);
        }
    }
}
