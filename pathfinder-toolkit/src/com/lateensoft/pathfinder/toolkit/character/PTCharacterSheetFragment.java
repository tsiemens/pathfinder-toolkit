package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.PTCharacterSheetActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//This is a base class for all fragments in the character sheet activity
public class PTCharacterSheetFragment extends Fragment{
	final String TAG = "PTCharacterSheetFragment";
	public PTCharacter mCharacter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCharacter = ((PTCharacterSheetActivity)getActivity()).getCurrentCharacter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onPause() {
		((PTCharacterSheetActivity)getActivity()).updateCharacterDatabase();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCharacter = ((PTCharacterSheetActivity)getActivity()).getCurrentCharacter();
	}
	
}
