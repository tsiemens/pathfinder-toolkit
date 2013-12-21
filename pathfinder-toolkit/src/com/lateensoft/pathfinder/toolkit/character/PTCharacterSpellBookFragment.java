package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class PTCharacterSpellBookFragment extends PTCharacterSheetFragment implements
	OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener{
	
	private static final String TAG = PTCharacterSpellBookFragment.class.getSimpleName();
	
	private ListView mListView;
	int mSpellSelectedForEdit;
	private Button mAddButton;
	
	private EditText mDialogName;
	private Spinner mDialogLevel;
	private Spinner mDialogPrepared;
	private EditText mDialogDescription;
	private OnTouchListener mSpinnerOnTouchListener;
	private View mDialogView;
	
	private ViewGroup mContainer;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContainer = container;
		
		setRootView(inflater.inflate(R.layout.character_spellbook_fragment,
				container, false));
		
		mAddButton = (Button) getRootView().findViewById(R.id.addSpell);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		setListViews(getRootView());
		refreshSpellListView();
		setonItemClickListeners();
		
		return getRootView();
	}

	private void setListViews(View fragmentView) {
		mListView = (ListView) fragmentView.findViewById(R.id.spells);
	}

	private void setonItemClickListeners() {
		mListView.setOnItemClickListener(this);
	}


	private void refreshSpellListView() {
		
		PTSpellBookAdapter adapter = new PTSpellBookAdapter(mContainer.getContext(),
				R.layout.character_spellbook_row,
				mCharacter.getSpellBook().getSpells());
		
		Log.v(TAG, "Called refreshSpellListView");
		
		mListView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mSpellSelectedForEdit = position;
		showSpellDialog(mCharacter.getSpellBook().getSpell(position));
	}
	
	/**
	 * Add spell button was tapped
	 */
	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mSpellSelectedForEdit = -1;
		showSpellDialog(null);
	}
	
	private void showSpellDialog(PTSpell spell) {
		if(spell == null) {
			spell = new PTSpell();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View dialogView = inflater.inflate(R.layout.character_spellbook_dialog, null);
		mDialogName = (EditText) dialogView.findViewById(R.id.spellName);
		mDialogLevel = (Spinner) dialogView.findViewById(R.id.spellLevel);
		mDialogPrepared = (Spinner) dialogView.findViewById(R.id.spellPrepared);
		mDialogDescription = (EditText) dialogView.findViewById(R.id.spellDescription);
		
		setupSpinner(mDialogLevel, 10, spell.getLevel());
		setupSpinner(mDialogPrepared, 5, spell.getPrepared());
		
		if(mSpellSelectedForEdit < 0) {
			//Adding a spell
			builder.setTitle(R.string.new_spell_title);
		} else {
			//Editing a spell
			builder.setTitle(spell.getName()).setNeutralButton(R.string.delete_button_text, this);
			mDialogName.setText(spell.getName());
			mDialogDescription.setText(spell.getDescription());
			mDialogLevel.setSelection(spell.getLevel());
			mDialogPrepared.setSelection(spell.getPrepared());
		}
		
		builder.setView(dialogView)
			.setPositiveButton(R.string.ok_button_text, this)
			.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		mDialogView = dialogView;
		alert.show();
	}
	
	private void setupSpinner(Spinner spinner, int options, int pos) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.selectable_values_string, android.R.layout.simple_spinner_item);
		
		if(mSpinnerOnTouchListener == null) {
			mSpinnerOnTouchListener = new OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                closeKeyboard();
	                return false;
	            }
			};
		}
		
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		spinner.setAdapter(adapter);
		spinner.setSelection(pos, true);
		
		spinner.setOnTouchListener(mSpinnerOnTouchListener);
	}
	
	private String[] generateOptionArray(int options) {
		String[] stringOptionArray = new String[options];
		for(int i = 0; i < options; i++) {
			stringOptionArray[i] = Integer.toString(i);
		}
		return stringOptionArray;
	}

	public void onClick(DialogInterface dialogInterface, int selection) {
		PTSpell spell = getSpellFromDialog();
		
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			Log.v(TAG, "Add.edit spell OK: " + mDialogName.getText());
			if(mSpellSelectedForEdit < 0) {
				Log.v(TAG, "Adding a spell");
				if(spell != null) {
					mCharacter.getSpellBook().addSpell(spell);
					refreshSpellListView(); //TODO: change to specific level?
				}
			} else {
				Log.v(TAG, "Editing a spell");
				
				mCharacter.getSpellBook().setSpell(mSpellSelectedForEdit, spell);
				refreshSpellListView();
			}
			
			break;
		
		case DialogInterface.BUTTON_NEUTRAL:
			Log.v(TAG, "Deleting a spell");
			mCharacter.getSpellBook().deleteSpell(mSpellSelectedForEdit);
			refreshSpellListView();
			break;
		
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		
		default:
			break;
		}
		
		closeKeyboard();
	}
	
	private void closeKeyboard() {
		InputMethodManager iMM = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(mDialogName.hasFocus()) {
			iMM.hideSoftInputFromWindow(mDialogView.getWindowToken(), 0);
		}
		else if(mDialogDescription.hasFocus()) {
			iMM.hideSoftInputFromWindow(mDialogView.getWindowToken(), 0);
		}
	}
	
	private PTSpell getSpellFromDialog() {
		String name = new String(mDialogName.getText().toString());
		if(name == null || name.contentEquals("")) {
			return null;
		}
		
		String description = new String(mDialogDescription.getText().toString());
		
		int level = mDialogLevel.getSelectedItemPosition();
		if(level > 10 || level < 0)
			return null;
		
		int prepared = mDialogPrepared.getSelectedItemPosition();
		if(prepared < 0)
			return null;
		
		return new PTSpell(name, level, prepared, description);
	}

	@Override
	public void updateFragmentUI() {
		refreshSpellListView();
		
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_spells);
	}

}
