package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
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
import com.lateensoft.pathfinder.toolkit.db.dao.table.FeatDAO;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.model.character.FeatList;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CharacterFeatsFragment extends IndexedParcelableListFragment<Feat, FeatDAO> {
    private static final String TAG = CharacterFeatsFragment.class.getSimpleName();

    private ListView featsListView;
    private Button addButton;

    private FeatDAO featDao;
    private FeatList feats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featDao = new FeatDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRootView(inflater.inflate(R.layout.character_feats_fragment,
                container, false));

        addButton = (Button) getRootView().findViewById(R.id.buttonAddFeat);
        addButton.setOnClickListener(getAddButtonClickListener());

        featsListView = (ListView) getRootView()
                .findViewById(R.id.listViewFeats);
        featsListView.setOnItemClickListener(getListItemClickListener());

        return getRootView();
    }

    @Override
    protected Class<? extends ParcelableEditorActivity> getParcelableEditorActivity() {
        return FeatEditActivity.class;
    }

    @Override
    protected FeatDAO getDAO() {
        return featDao;
    }

    @Override
    protected List<Feat> getModel() {
        return feats;
    }

    @Override
    public void updateFragmentUI() {
        refreshFeatsListView();
    }

    private void refreshFeatsListView() {
        Collections.sort(feats);
        FeatListAdapter adapter = new FeatListAdapter(getActivity(),
                R.layout.character_feats_row, feats);

        featsListView.setAdapter(adapter);
    }
    
    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_feats);
    }

    @Override
    public void loadFromDatabase() {
        feats = new FeatList(featDao.findAllForOwner(getCurrentCharacterID()));
    }
}
