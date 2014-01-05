package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTItem;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

public class PTCharacterInventoryEditActivity extends PTParcelableEditorActivity {
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterInventoryEditActivity.class.getSimpleName();
	
    private EditText m_weightET;
    private EditText m_nameET;
    private EditText m_quantityET;
    private CheckBox m_itemContainedCheckbox;
	
	private PTItem m_item;
	private boolean m_itemIsNew = false;
	
	@Override
	protected void setupContentView() {
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

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		String name = new String(m_nameET.getText().toString());
		if(name == null || name.isEmpty()){
			throw new InvalidValueException(getString(R.string.editor_name_required_alert));
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
	}

	@Override
	protected Parcelable getEditedParcelable() {
		return m_item;
	}

	@Override
	protected void setParcelableToEdit(Parcelable p) {
		if (p == null) {
			m_itemIsNew = true;
			m_item = new PTItem();
		} else {
			m_item = (PTItem) p;
		}
	}

	@Override
	protected boolean isParcelableDeletable() {
		return !m_itemIsNew;
	}

}
