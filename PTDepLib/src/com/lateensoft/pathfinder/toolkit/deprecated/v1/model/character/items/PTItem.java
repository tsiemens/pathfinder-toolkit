package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items;

public class PTItem {
    String mName;
    int mWeight;
    int mQuantity;
    boolean mIsContained; //set if in a container such as a bag of holding (will be used to set effective weight to 0)
    
    public PTItem(String name, int weight, int quantity, boolean contained) {
        mName = name;
        mWeight = weight;
        mQuantity = quantity;
        mIsContained = contained;
    }
    
    /**
     * Creates new instance of an Item. Defaults quantity to 1 and contained to false. 
     * @param name
     * @param weight
     */
    public PTItem(String name, int weight) {
        mName = name;
        mWeight = weight;
        mQuantity = 1;
        mIsContained = false;
    }
    
    public PTItem() {
        mName = "";
        mWeight = 1;
        mQuantity = 1;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(String name) {
        mName = name;
    }
    
    public int getWeight() {
        return mWeight;
    }
    
    public void setWeight(int weight) {
        mWeight = weight;
    }
    
    public int getQuantity() {
        return mQuantity;
    }
    
    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }
    
    public boolean isContained(){
        return mIsContained;
    }
    
    public void setIsContained(boolean isContained){
        mIsContained = isContained;
    }
}
