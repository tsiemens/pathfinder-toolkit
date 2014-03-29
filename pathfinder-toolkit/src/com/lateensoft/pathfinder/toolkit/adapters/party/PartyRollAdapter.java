package com.lateensoft.pathfinder.toolkit.adapters.party;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;

import java.util.List;

public class PartyRollAdapter extends ArrayAdapter<PartyMember>{
    public enum CritType {CRIT_FUMBLE, CRIT, NO_CRIT}

	private int m_layoutResourceId;
    private CritTypeValueGetter m_critGetter;

    public PartyRollAdapter(Context context, int layoutResourceId, List<PartyMember> partymembers, CritTypeValueGetter critGetter) {
        super(context, layoutResourceId, partymembers);
        m_layoutResourceId = layoutResourceId;
        m_critGetter = critGetter;
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MemberRowHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new MemberRowHolder();
			
			holder.name = (TextView)row.findViewById(R.id.textViewRollName);
			holder.rollValue = (TextView)row.findViewById(R.id.textViewRollValue);
			
			row.setTag(holder);
		}
		else {
			holder = (MemberRowHolder)row.getTag();
		}
		
		holder.name.setText(this.getItem(position).getName());
		holder.rollValue.setText(Integer.toString(this.getItem(position).getLastRolledValue()));

        if(m_critGetter != null){
            CritType critType = m_critGetter.getCritTypeForMember(this.getItem(position));
            int color = getContext().getResources().getColor(android.R.color.primary_text_dark);
            switch (critType){
                case CRIT_FUMBLE:
                    color = Color.RED;
                    break;
                case CRIT:
                    color = Color.GREEN;
                    break;
            }
            holder.rollValue.setTextColor(color);
		}
		
		return row;
	}

	private static class MemberRowHolder {
		TextView name;
		TextView rollValue;
	}

    public interface CritTypeValueGetter {
        public CritType getCritTypeForMember(PartyMember member);
    }
}
