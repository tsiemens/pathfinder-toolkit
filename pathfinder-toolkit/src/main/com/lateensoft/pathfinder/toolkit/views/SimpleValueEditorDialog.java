package com.lateensoft.pathfinder.toolkit.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.lateensoft.pathfinder.toolkit.R;

public class SimpleValueEditorDialog {
    private AlertDialog dialog;
    private EditText editor;
    private OnEditingFinishedListener editListener;

    public enum ValueType {
        TEXT(InputType.TYPE_CLASS_TEXT, 1),
        TEXT_MULTILINE(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE, 6),
        NUMBER_UNSIGNED(InputType.TYPE_CLASS_NUMBER, 1),
        NUMBER_SIGNED(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED, 1),
        NUMBER_DECIMAL(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 1),
        NUMBER_DECIMAL_SIGNED(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED, 1);

        private final int inputType;
        private final int defaultMaxLines;

        ValueType(int inputType, int defaultMaxLines) {
            this.inputType = inputType;
            this.defaultMaxLines = defaultMaxLines;
        }
    }

    public interface OnEditingFinishedListener {
        public void onEditingFinished(boolean okWasPressed, Editable editable);
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        Context context;
        CharSequence title;
        CharSequence hint;
        CharSequence initialValue;
        OnEditingFinishedListener listener;
        ValueType type = ValueType.TEXT;

        private Builder(Context context) {
            this.context = context;
        }

        public SimpleValueEditorDialog build() {
            return new SimpleValueEditorDialog(this);
        }

        public Builder withTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder withTitle(int title) {
            this.title = context.getString(title);
            return this;
        }

        public Builder withHint(CharSequence hint) {
            this.hint = hint;
            return this;
        }

        public Builder withHint(int hint) {
            this.hint = context.getString(hint);
            return this;
        }

        public Builder withInitialValue(CharSequence initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public Builder withOnFinishedListener(OnEditingFinishedListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder forType(ValueType type) {
            this.type = type;
            return this;
        }
    }

    private SimpleValueEditorDialog(Builder builder) {
        editListener = builder.listener;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(builder.context);
        LayoutInflater inflater = LayoutInflater.from(builder.context);

        if (builder.title != null) {
            dialogBuilder.setTitle(builder.title);
        } else {
            dialogBuilder.setTitle(R.string.value_editor_title);
        }

        View dialogView = inflater.inflate(R.layout.edit_value_dialog, null);
        editor = (EditText) dialogView.findViewById(R.id.editor);
        editor.setInputType(builder.type.inputType);
        editor.setMaxLines(builder.type.defaultMaxLines);

        if (builder.hint != null) {
            editor.setHint(builder.hint);
        }

        editor.setText("");
        if (builder.initialValue != null) {
            editor.append(builder.initialValue);
        }

        dialogBuilder.setView(dialogView)
                .setPositiveButton(R.string.ok_button_text, clickListener)
                .setNegativeButton(R.string.cancel_button_text, clickListener);
        dialog = dialogBuilder.create();
    }

    private DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
            if (editListener != null) {
                editListener.onEditingFinished(which == DialogInterface.BUTTON_POSITIVE,
                        editor.getText());
            }
        }
    };

    public void show() {
        dialog.show();
    }
}
