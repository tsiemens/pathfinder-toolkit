package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.widget.EditText;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

public class FeatEditActivity extends ParcelableEditorActivity {
    @SuppressWarnings("unused")
    private static final String TAG = FeatEditActivity.class.getSimpleName();
    
    private EditText m_nameET;
    private EditText m_descriptionET;
    
    private Feat m_feat;
    private boolean m_featIsNew = false;
    
    @Override
    protected void setupContentView() {
        setContentView(R.layout.feats_editor);

        m_nameET = (EditText) findViewById(R.id.etFeatName);
        m_descriptionET = (EditText) findViewById(R.id.etFeatDescription);

        if(m_featIsNew) {
            setTitle(R.string.new_feat_title);
        } else {
            setTitle(R.string.edit_feat_title);
            m_nameET.setText(m_feat.getName());
            m_descriptionET.setText(m_feat.getDescription());
        }
    }

    @Override
    protected void updateEditedParcelableValues() throws InvalidValueException {
        String name = new String(m_nameET.getText().toString());
        if(name == null || name.isEmpty()) {
            throw new InvalidValueException(getString(R.string.editor_name_required_alert));
        }
        
        String description = new String(m_descriptionET.getText().toString());
        
        m_feat.setName(name);
        m_feat.setDescription(description);    
    }

    @Override
    protected Parcelable getEditedParcelable() {
        return m_feat;
    }

    @Override
    protected void setParcelableToEdit(Parcelable p) {
        if(p == null) {
            m_featIsNew = true;
            m_feat = new Feat();
        } else {
            m_feat = (Feat) p;
        }
    }

    @Override
    protected boolean isParcelableDeletable() {
        return !m_featIsNew;
    }

}
