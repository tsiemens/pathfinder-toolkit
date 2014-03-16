package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.PTFeatRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeatList;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

import java.util.Collections;

public class PTCharacterFeatsFragment extends PTCharacterSheetFragment
		implements OnClickListener, OnItemClickListener {

	private static final String TAG = PTCharacterFeatsFragment.class.getSimpleName();
	private ListView m_featsListView;
	private Button m_addButton;

	private int m_featSelectedForEdit;
	
	private PTFeatRepository m_featRepo;
	private PTFeatList m_featList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_featRepo = new PTFeatRepository();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		setRootView(inflater.inflate(R.layout.character_feats_fragment,
				container, false));

		m_addButton = (Button) getRootView().findViewById(R.id.buttonAddFeat);
		m_addButton.setOnClickListener(this);

		m_featsListView = (ListView) getRootView()
				.findViewById(R.id.listViewFeats);
		m_featsListView.setOnItemClickListener(this);

		return getRootView();
	}

	private void refreshFeatsListView() {
        Collections.sort(m_featList);
		String[] featNames = m_featList.getFeatNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getContext(), android.R.layout.simple_list_item_1,
				featNames);
		m_featsListView.setAdapter(adapter);
	}

	// Add Feat button was tapped
	public void onClick(View button) {
		m_featSelectedForEdit = -1;
		showFeatEditor(null);

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		m_featSelectedForEdit = position;
		showFeatEditor(m_featList.get(position));

	}
	
	private void showFeatEditor(PTFeat feat) {
		Intent featEditIntent = new Intent(getContext(),
				PTCharacterFeatEditActivity.class);
		featEditIntent.putExtra(
				PTCharacterFeatEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, feat);
		startActivityForResult(featEditIntent, PTParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PTParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTFeat feat = PTParcelableEditorActivity.getParcelableFromIntent(data);
            if (feat != null && m_featList != null) {
                if(m_featSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a feat");
                    feat.setCharacterID(getCurrentCharacterID());
                    if(m_featRepo.insert(feat) != -1) {
                        m_featList.add(feat);
                        refreshFeatsListView();
                    }
                } else {
                    Log.v(TAG, "Editing a feat");
                    if(m_featRepo.update(feat) != 0) {
                        m_featList.set(m_featSelectedForEdit, feat);
                        refreshFeatsListView();
                    }
                }
            }
			
			break;
		
		case PTCharacterFeatEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting an item");
			PTFeat featToDelete = m_featList.get(m_featSelectedForEdit);
			if(featToDelete != null && m_featRepo.delete(featToDelete) != 0) {
				m_featList.remove(m_featSelectedForEdit);
				refreshFeatsListView();
			}
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateDatabase();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void updateFragmentUI() {
		refreshFeatsListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_feats);
	}

	@Override
	public void updateDatabase() {
		// Done dynamically
	}

	@Override
	public void loadFromDatabase() {
		m_featList = new PTFeatList(m_featRepo.querySet(getCurrentCharacterID()));
	}

}
