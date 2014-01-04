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
import android.widget.CheckBox;
import android.widget.EditText;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTItem;

public class PTCharacterInventoryEditActivity extends Activity {
	private static final String TAG = PTCharacterInventoryEditActivity.class.getSimpleName();
	
	public static final int RESULT_CUSTOM_DELETE = RESULT_FIRST_USER;
	public static final String INTENT_EXTRAS_KEY_ITEM = "item";
	
	public static final int FLAG_NEW_ITEM = 0x1;
	
    private EditText m_weightET;
    private EditText m_nameET;
    private EditText m_quantityET;
    private CheckBox m_itemContainedCheckbox;
	
	private PTItem m_item;
	private boolean m_itemIsNew = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		m_item = getIntent().getExtras().getParcelable(INTENT_EXTRAS_KEY_ITEM);
		if (m_item == null) {
			m_itemIsNew = true;
		}
		
		setupContentView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.base_editor_menu, menu);
	    if (m_itemIsNew) {
	    	menu.findItem(R.id.mi_delete).setVisible(false);
	    }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_done) {
			if (updateItemValues()) {
				Log.v(TAG, (m_itemIsNew?"Add":"Edit")+" item done: " + m_nameET.getText());
				Intent resultData = new Intent();
				resultData.putExtra(INTENT_EXTRAS_KEY_ITEM, m_item);
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
		if(m_item == null) {
			m_item = new PTItem();
		}

		setContentView(R.layout.character_inventory_editor);

		m_nameET = (EditText) findViewById(R.id.etItemName);
		m_weightET = (EditText) findViewById(R.id.etItemWeight);
		m_quantityET = (EditText) findViewById(R.id.etItemQuantity);
		m_itemContainedCheckbox = (CheckBox) findViewById(R.id.checkboxItemContained);

		if(m_itemIsNew) {
			setTitle(R.string.new_item_title);
		} else {
			setTitle(R.string.edit_item_title);
			m_nameET.setText(m_item.getName());
			m_weightET.setText(Double.toString(m_item.getWeight()));
			m_quantityET.setText(Integer.toString(m_item.getQuantity()));
			m_itemContainedCheckbox.setChecked(m_item.isContained());
		}
	}
	
	private boolean updateItemValues() {
		String name = new String(m_nameET.getText().toString());
		if(name == null || name.isEmpty()){
			return false;
		}
		
		int quantity;
		try{
			quantity = (int)Double.parseDouble(m_quantityET.getText().toString());
		}catch (NumberFormatException e){
			quantity = 1;
		}
		
		double weight;
		try{
			weight = Double.parseDouble(m_weightET.getText().toString());
		}catch (NumberFormatException e){
			weight = 1.0;
		}
		boolean contained = m_itemContainedCheckbox.isChecked();
		
		m_item.setName(name);
		m_item.setWeight(weight);
		m_item.setQuantity(quantity);
		m_item.setIsContained(contained);
		
        return true;
	}

	private void showDeleteConfirmation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.delete_alert_title);
		builder.setMessage(R.string.delete_item_alert_message);
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
