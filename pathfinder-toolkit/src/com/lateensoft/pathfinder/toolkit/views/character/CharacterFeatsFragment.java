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
import android.widget.Button;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.FeatListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.FeatRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.model.character.FeatList;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;

public class CharacterFeatsFragment extends AbstractCharacterSheetFragment
		implements OnClickListener, OnItemClickListener {

	private static final String TAG = CharacterFeatsFragment.class.getSimpleName();
	private ListView m_featsListView;
	private Button m_addButton;

	private int m_featSelectedForEdit;
	
	private FeatRepository m_featRepo;
	private FeatList m_featList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_featRepo = new FeatRepository();
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
		FeatListAdapter adapter = new FeatListAdapter(getActivity(),
				R.layout.character_feats_row, m_featList);

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

	private void showFeatEditor(Feat feat) {
		Intent featEditIntent = new Intent(getContext(),
				FeatEditActivity.class);
		featEditIntent.putExtra(
				FeatEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, feat);
		startActivityForResult(featEditIntent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			Feat feat = ParcelableEditorActivity.getParcelableFromIntent(data);
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
		
		case FeatEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting an item");
			Feat featToDelete = m_featList.get(m_featSelectedForEdit);
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
		m_featList = new FeatList(m_featRepo.querySet(getCurrentCharacterID()));
	}

}
