package com.lateensoft.pathfinder.toolkit.character;


/**
 * Representation for both Feats and Special  abilities
 * @author trevsiemens
 *
 */
public class PTFeat {
	private String mName;
	private String mDescription;
	
	public PTFeat(){
		mName = "";
		mDescription = "";
	}
	
	public PTFeat(String name, String description){
		mName = new String(name);
		mDescription = new String (description);
	}
	
	public PTFeat(PTFeat otherFeat){
		mName = new String(otherFeat.getName());
		mDescription = new String (otherFeat.getDescription());
		
	}
	
	public void setName(String name){
		if(name != null){
			mName = name;
		}
	}
	
	public String getName(){
		return mName;
	}
	
	public void setDescription(String description){
		if(description != null){
			mDescription = description;
		}
	}
	
	public String getDescription(){
		return mDescription;
	}
}
