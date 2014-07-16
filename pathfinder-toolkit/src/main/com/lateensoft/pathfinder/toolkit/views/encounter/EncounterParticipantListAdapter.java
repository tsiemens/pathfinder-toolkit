package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SelectableItemAdapter;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicArrayAdapter;
import com.lateensoft.pathfinder.toolkit.views.widget.ListViewRowFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author tsiemens
 */
public class EncounterParticipantListAdapter extends DynamicArrayAdapter<EncounterParticipantRowModel>
    implements SelectableItemAdapter {

    private static final int LAYOUT_RESOURCE_ID = R.layout.encounter_participant_row;

    public interface RowTouchListener {
        public void onTouch(View v, MotionEvent event, int position);
    }

    public interface RowComponentClickListener {
        public void onClick(View v, int position);
    }

    private ItemSelectionGetter selectionGetter;
    private RowTouchListener dragIconTouchListener;
    private RowComponentClickListener rollsClickListener;

    private List<EncounterParticipantRowModel> participantRows;

    public EncounterParticipantListAdapter(Context context, List<EncounterParticipantRowModel> objects) {
        super(context, LAYOUT_RESOURCE_ID, objects);
        participantRows = objects;
        Collections.sort(participantRows);
    }

    @Override
    public void setItemSelectionGetter(ItemSelectionGetter getter) {
        selectionGetter = getter;
    }

    public void setDragIconTouchListener(RowTouchListener listener) {
        dragIconTouchListener = listener;
    }

    public void setRollsClickListener(RowComponentClickListener listener) {
        rollsClickListener = listener;
    }

    @Override
    public void doItemSwap(int pos1, int pos2) {
        EncounterParticipantRowModel tmp1 = participantRows.get(pos1);
        EncounterParticipantRowModel tmp2 = participantRows.get(pos2);
        participantRows.set(pos1, tmp2);
        participantRows.set(pos2, tmp1);
    }

    private static class RowHolder {
        View row;
        View rolls;
        TextView initRoll;
        TextView checkRoll;
        TextView textView;
        ImageView dragIcon;
    }

    @Override
    protected ListViewRowFactory<RowHolder> newRowFactory() {
        return new RowFactory();
    }

    private class RowFactory extends ListViewRowFactory<RowHolder> {

        @Override
        protected int getRowLayoutResourceIdForPosition(int position) {
            return LAYOUT_RESOURCE_ID;
        }

        @Override
        protected RowHolder holderFrom(View row, int layoutResourceId) {
            RowHolder holder = new RowHolder();
            holder.row = row;
            holder.rolls = row.findViewById(R.id.ll_rolls);
            holder.initRoll = (TextView)row.findViewById(R.id.tv_init_roll);
            holder.checkRoll = (TextView)row.findViewById(R.id.tv_check_roll);
            holder.textView = (TextView)row.findViewById(R.id.tv_name);
            holder.dragIcon = (ImageView)row.findViewById(R.id.icon_drag);
            return holder;
        }

        @Override
        protected void setRowContent(final RowHolder holder, final int position) {
            EncounterParticipantRowModel rowModel = getItem(position);
            holder.textView.setText(rowModel.getParticipant().getName());
            holder.initRoll.setText(Integer.toString(rowModel.getParticipant().getInitiativeScore()));
            int lastCheck = rowModel.getLastCheckRoll();
            holder.checkRoll.setText(lastCheck != 0 ? Integer.toString(lastCheck) : "-");

            holder.dragIcon.setClickable(true);
            holder.dragIcon.setEnabled(true);
            holder.dragIcon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (dragIconTouchListener != null) {
                        dragIconTouchListener.onTouch(v, event, position);
                    }
                    return false;
                }
            });

            holder.rolls.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    rollsClickListener.onClick(v, position);
                }
            });

            if (selectionGetter != null) {
                holder.row.setBackgroundColor(selectionGetter.isItemSelected(position) ?
                        getContext().getResources().getColor(R.color.holo_blue_light_translucent) :
                        getContext().getResources().getColor(android.R.color.transparent));
            }
        }
    }

    @Override
    public Bitmap getBitmapForMobileRow(View row) {
        Bitmap bitmapOfRow = super.getBitmapForMobileRow(row);
        Bitmap displayedMobileRow = bitmapOfRow.copy(bitmapOfRow.getConfig(), true);

        Canvas can = new Canvas(displayedMobileRow);
        Rect rect = new Rect(0, 0, bitmapOfRow.getWidth(), bitmapOfRow.getHeight());

        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.BLACK);

        can.drawRect(rect, backgroundPaint);
        can.drawBitmap(bitmapOfRow, 0, 0, null);

        return displayedMobileRow;
    }
}
