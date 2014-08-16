package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

public class InventoryItemEditActivity extends ParcelableEditorActivity {
    @SuppressWarnings("unused")
    private static final String TAG = InventoryItemEditActivity.class.getSimpleName();
    
    private EditText m_weightET;
    private EditText m_nameET;
    private EditText m_quantityET;
    private CheckBox m_itemContainedCheckbox;
    
    private Item m_item;
    private boolean m_itemIsNew = false;
    
    @Override
    protected void setupContentView() {
        setContentView(R.layout.inventory_item_editor);

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
        String name = m_nameET.getText().toString();
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
        m_item.setContained(contained);
    }

    @Override
    protected Parcelable getEditedParcelable() {
        return m_item;
    }

    @Override
    protected void setParcelableToEdit(Parcelable p) {
        if (p == null) {
            m_itemIsNew = true;
            m_item = new Item();
        } else {
            m_item = (Item) p;
        }
    }

    @Override
    protected boolean isParcelableDeletable() {
        return !m_itemIsNew;
    }

}
