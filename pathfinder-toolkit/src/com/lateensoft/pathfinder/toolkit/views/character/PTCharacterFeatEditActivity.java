package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.character.PTFeat;

public class PTCharacterFeatEditActivity extends Activity {
	private static final String TAG = PTCharacterFeatEditActivity.class.getSimpleName();
	
	public static final int RESULT_CUSTOM_DELETE = RESULT_FIRST_USER;
	public static final String INTENT_EXTRAS_KEY_FEAT = "feat";
	
	public static final int FLAG_NEW_ITEM = 0x1;
	
    private EditText m_nameET;
    private EditText m_descriptionET;
	
	private PTFeat m_feat;
	private boolean m_featIsNew = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		m_feat = getIntent().getExtras().getParcelable(INTENT_EXTRAS_KEY_FEAT);
		if (m_feat == null) {
			m_featIsNew = true;
		}
		
		setupContentView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.base_editor_menu, menu);
	    if (m_featIsNew) {
	    	menu.findItem(R.id.mi_delete).setVisible(false);
	    }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_done) {
			if (updateFeatValues()) {
				Log.v(TAG, (m_featIsNew?"Add":"Edit")+" feat done: " + m_nameET.getText());
				Intent resultData = new Intent();
				resultData.putExtra(INTENT_EXTRAS_KEY_FEAT, m_feat);
				setResult(RESULT_OK, resultData);
				finish();
			} else {
				showInvalidNameDialog();
			}
		} else if (item.getItemId() == R.id.mi_cancel || 
				item.getItemId() == android.R.id.home) {
			setResult(RESULT_CANCELED);
			finish();
		} else if (item.getItemId() == R.id.mi_delete) {
			showDeleteConfirmation();
		} else {
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	private void setupContentView() {
		if(m_feat == null) {
			m_feat = new PTFeat();
		}

		setContentView(R.layout.character_feats_editor);

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
	
	private boolean updateFeatValues() {
		String name = new String(m_nameET.getText().toString());
		if(name == null || name.isEmpty()){
			return false;
		}
		
		String description = new String(m_descriptionET.getText().toString());
		
		m_feat.setName(name);
		m_feat.setDescription(description);
		
        return true;
	}

	private void showDeleteConfirmation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.delete_alert_title);
		builder.setMessage(R.string.delete_feat_alert_message);
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_NEGATIVE) {
					setResult(RESULT_CUSTOM_DELETE);
					finish();
				}
			}
		};
		builder.setPositiveButton(R.string.cancel_button_text, ocl);
		builder.setNegativeButton(R.string.ok_button_text, ocl);
		builder.show();
	}
	
	private void showInvalidNameDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.error_alert_title);
		builder.setMessage(R.string.editor_name_required_alert);
		builder.setNeutralButton(R.string.ok_button_text, null);
		builder.show();
	}

}
