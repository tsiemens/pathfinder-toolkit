package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.ArrayList;
import java.util.Collection;

import android.os.Parcel;
import android.os.Parcelable;

public class FeatList extends ArrayList<Feat> implements Parcelable {
    public FeatList(){
        super();
    }
    
    public FeatList(Collection<? extends Feat> feats) {
        super(feats);
    }
    
    public FeatList(Parcel in) {
        in.readTypedList(this, Feat.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Parcelable.Creator<FeatList> CREATOR = new Parcelable.Creator<FeatList>() {
        public FeatList createFromParcel(Parcel in) {
            return new FeatList(in);
        }
        
        public FeatList[] newArray(int size) {
            return new FeatList[size];
        }
    };
}
