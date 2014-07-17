package com.lateensoft.pathfinder.toolkit.views;

import android.content.Context;
import android.widget.*;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.util.DiceSet;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

public class DiceRollerFragment extends BasePageFragment {
    private static final int MAX_ROLL_SUM = 9999;

    private int rollSum = 0;
    private RollMode rollMode;
    private DiceSet diceSet;

    private RadioGroup rollTypeRadioGroup;
    private Spinner dieTypeSpinner, dieQuantitySpinner;
    private Button rollButton, resetButton;
    private TextView dieQuantityLabel, rollResultLabel, rollSumLabel;
    private ListView rollResultList;

    private static enum RollMode {SINGLE, XDY, CUMULATIVE}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diceSet = new DiceSet();
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
        rollTypeRadioGroup = (RadioGroup) getRootView().findViewById(R.id.toggleGroupRollType);

        RollTypeClickListener rollTypeClickListener = new RollTypeClickListener();
        for (int i = 0; i < rollTypeRadioGroup.getChildCount(); i++) {
            rollTypeRadioGroup.getChildAt(i).setOnClickListener(rollTypeClickListener);
        }

        dieTypeSpinner = (Spinner) getRootView().findViewById(R.id.spinnerDieType);
        setupSpinner(dieTypeSpinner, R.array.roller_die_types, 0);

        dieQuantityLabel = (TextView) getRootView().findViewById(R.id.tvDieQuantity);
        dieQuantitySpinner = (Spinner) getRootView().findViewById(R.id.spinnerDieQuantity);
        setupSpinner(dieQuantitySpinner, R.array.roller_die_quantities, 0);

        rollButton = (Button) getRootView().findViewById(R.id.buttonRoll);
        rollButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });

        resetButton = (Button) getRootView().findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRolls();
            }
        });

        rollResultLabel = (TextView) getRootView().findViewById(R.id.textViewRollResult);
        rollSumLabel = (TextView) getRootView().findViewById(R.id.textViewRollSum);
        rollResultList = (ListView) getRootView().findViewById(R.id.rollResultListView);

        resetRolls();
    }

    public void setRollMode(RollMode rollMode) {
        switch (rollMode) {
        case SINGLE:
            this.rollMode = RollMode.SINGLE;
            resetButton.setEnabled(false);
            dieQuantitySpinner.setEnabled(false);
            dieQuantityLabel.setEnabled(false);
            rollSumLabel.setVisibility(View.INVISIBLE);
            rollResultList.setVisibility(View.GONE);
            break;
        case XDY:
            this.rollMode = RollMode.XDY;
            resetButton.setEnabled(true);
            dieQuantitySpinner.setEnabled(true);
            dieQuantityLabel.setEnabled(true);
            rollSumLabel.setVisibility(View.INVISIBLE);
            rollResultList.setVisibility(View.VISIBLE);
            break;
        case CUMULATIVE:
            this.rollMode = RollMode.CUMULATIVE;
            resetButton.setEnabled(true);
            dieQuantitySpinner.setEnabled(true);
            dieQuantityLabel.setEnabled(true);
            rollSumLabel.setVisibility(View.VISIBLE);
            rollResultList.setVisibility(View.VISIBLE);
            break;
        }
    }

    public void resetRolls() {
        rollSum = 0;
        rollSumLabel.setText(getString(R.string.roller_sum_label) + " "
                + rollSum);
        dieQuantitySpinner.setSelection(0);
        refreshRollsListView(null);
    }

    public void rollDice() {
        int rollResult;
        List<Integer> rolls;

        switch (rollMode) {
            case SINGLE:
                rollResultLabel.setText(Integer.toString(diceSet.roll(getDieType())));
                break;
            case XDY:
                rolls = diceSet.rollMultiple(getDieType(), getDieQuantity());
                rollResult = sumInts(rolls);
                rollResultLabel.setText(Integer.toString(rollResult));
                refreshRollsListView(rolls);
                break;
            case CUMULATIVE:
                rolls = diceSet.rollMultiple(getDieType(), getDieQuantity());
                rollResult = sumInts(rolls);
                rollSum += rollResult;
                if (rollSum > MAX_ROLL_SUM)
                    rollSum = MAX_ROLL_SUM;
                rollSumLabel.setText(getString(R.string.roller_sum_label) + " "
                        + rollSum);
                rollResultLabel.setText(Integer.toString(rollResult));
                refreshRollsListView(rolls);
                break;
            default:
                throw new IllegalStateException("Invalid roll mode: "+ rollMode);
        }
    }

    private int getDieQuantity() {
        return dieQuantitySpinner.getSelectedItemPosition() + 1;
    }

    private DiceSet.Die getDieType() {
        int position = dieTypeSpinner.getSelectedItemPosition();
        DiceSet.Die[] dice = DiceSet.Die.values();
        if (position < dice.length) {
           return dice[position];
        } else {
            throw new IllegalStateException("Illegal die selected at spinner position "+position);
        }
    }

    private int sumInts(Iterable<Integer> rollResults){
        int rollSum = 0;
        if(rollResults != null){
            for(int i : rollResults){
                rollSum += i;
            }
        }
        return rollSum;
    }

    private void refreshRollsListView(List<Integer> rollResults){
        if(rollResults == null) {
            rollResults = Lists.newArrayList();
        }
        ArrayAdapter adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_list_item_1, rollResults);
        rollResultList.setAdapter(adapter);
    }

    private class RollTypeClickListener implements OnClickListener {
        @Override public void onClick(View view) {
            ToggleButton toggleButton;
            for (int i = 0; i < rollTypeRadioGroup.getChildCount(); i++) {
                toggleButton = (ToggleButton) rollTypeRadioGroup.getChildAt(i);
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
        Context context = getContext();
        if (context != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    optionResourceId, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(R.layout.spinner_centered_wrapped);
            spinner.setGravity(Gravity.CENTER);
            spinner.setAdapter(adapter);
            spinner.setSelection(defaultSelection);
        }
    }
}
