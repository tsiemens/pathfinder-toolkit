package com.lateensoft.pathfinder.toolkit.views.party;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.party.PartyMemberStatListAdapter;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

public class PartyMemberEditorActivity extends ParcelableEditorActivity {
    @SuppressWarnings("unused")
    private static final String TAG = PartyMemberEditorActivity.class.getSimpleName();
    
    private PartyMember m_partyMember;
    private boolean m_memberIsNew = false;
    
    private EditText m_partyMemberNameET;
    private ListView m_statList;
    
    private int m_statSelectedForEdit;
    private EditText m_dialogStatValueET;
    private OnItemClickListener m_listItemClickListener;
    
    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_party_member_editor);    
        
        m_partyMemberNameET = (EditText) findViewById(R.id.editTextPartyMemberName);
        m_partyMemberNameET.setText(m_partyMember.getName());
        
        m_statList = (ListView) findViewById(R.id.listViewPartyMemberStats);        
        PartyMemberStatListAdapter adapter = new PartyMemberStatListAdapter(this, R.layout.party_member_stat_row,
                m_partyMember.getStatFields(this), m_partyMember.getStatsArray());
        m_statList.setAdapter(adapter);
        
        m_listItemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_statSelectedForEdit = position;
                showStatEditDialog(position);
            }
        };
        m_statList.setOnItemClickListener(m_listItemClickListener);
        
        ((PartyMemberStatListAdapter)m_statList.getAdapter()).updateValues(m_partyMember.getStatsArray());
        m_partyMemberNameET.setText(m_partyMember.getName());
    }

    @Override
    protected void updateEditedParcelableValues() throws InvalidValueException {
        String name = m_partyMemberNameET.getText().toString();
        if (name == null || name.isEmpty()) {
            throw new InvalidValueException(getString(R.string.editor_name_required_alert));
        }
        
        m_partyMember.setName(name);
        // Stats are already updated when closing dialog
    }

    @Override
    protected Parcelable getEditedParcelable() {
        return m_partyMember;
    }

    @Override
    protected void setParcelableToEdit(Parcelable p) {
        if (p == null) {
            m_memberIsNew = true;
            m_partyMember = new PartyMember("");
        } else {
            m_partyMember = (PartyMember) p;
        }
    }

    @Override
    protected boolean isParcelableDeletable() {
        return !m_memberIsNew;
    }
    
    private void showStatEditDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Set up dialog layout
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.party_member_stat_dialog, null);
        m_dialogStatValueET = (EditText) dialogView.findViewById(R.id.dialogStatText);
        
        builder.setTitle(m_partyMember.getStatFields(this)[position]);
        m_dialogStatValueET.setText(Integer.toString(m_partyMember.getValueByIndex(position)));

        OnClickListener ocl = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    try{
                        m_partyMember.setStatByIndex(m_statSelectedForEdit, Integer.parseInt(m_dialogStatValueET.getText().toString()));
                        ((PartyMemberStatListAdapter)m_statList.getAdapter()).updateValues(m_partyMember.getStatsArray());
                    }catch (NumberFormatException e){
                        //Do nothing
                    }
                }
            }
        };

        builder.setView(dialogView)
                .setPositiveButton(R.string.ok_button_text, ocl)
                .setNegativeButton(R.string.cancel_button_text, ocl);
        
        AlertDialog alert = builder.create();
        alert.show();
    }
}
