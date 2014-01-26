package com.lateensoft.pathfinder.toolkit.deprecated.v1.db;

import com.google.gson.*;
import com.lateensoft.pathfinder.toolkit.deprecated.R;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTParty;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PTDatabaseManager extends SQLiteOpenHelper {
	private final String TAG = "PTDatabaseManager";
	
	public static final String dbName = "pathfinderToolkitDB";
	static final String characterTable = "Characters";
	static final String colChtrID = "CharacterID";
	static final String colChtrName = "CharacterName";
	static final String colChtrGSON = "CharacterGSON";

	static final String partyTable = "Parties";
	static final String colPrtyID = "PartyID";
	static final String colPrtyName = "PartyName";
	static final String colPrtyGSON = "PartyGSON";

	static int dbVersion = 1;
	
	private Gson mGson;
	private SQLiteDatabase mDatabase;

	public PTDatabaseManager(Context appContext) {
		super(appContext, dbName, null, dbVersion);
		mGson = new Gson();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.v(TAG, "Creating new database");
		//Create character table
		db.execSQL("CREATE TABLE "+characterTable+" ("+colChtrID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			    colChtrName+ " TEXT, " +colChtrGSON+ " TEXT)");
			  
		//Create party table
		  db.execSQL("CREATE TABLE "+partyTable+" ("+colPrtyID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			    colPrtyName+ " TEXT, " +colPrtyGSON+ " TEXT)");
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS "+characterTable);
		//db.execSQL("DROP TABLE IF EXISTS "+partyTable);

		//onCreate(db);
	}
	
	/**
	 * Intended to fix the database so it is compatible across all versions
	 * @param context
	 */
	public void performUpdates(Context context){
		int characterIDs[] = getCharacterIDs();
		
		if (characterIDs.length > 0){
			Log.v(TAG, "Updating character table to current version");
			PTCharacter character;
			
			Resources r = context.getResources();
			int[] skillAbilityKeys = r.getIntArray(R.array.skill_ability_keys);
			String[] skillAbilityShortStrings = r.getStringArray(R.array.abilities_short);
			
			for(int characterIndex = 0; characterIndex < characterIDs.length; characterIndex++){
				character = getCharacter(characterIDs[characterIndex]);
		
				//Ensure that skills are up to date with string resources
				for(int i = 0; i < skillAbilityKeys.length; i++) {
					character.getSkillSet().getSkill(i).setKeyAbilityKey(skillAbilityKeys[i]);
					character.getSkillSet().getSkill(i).setKeyAbility(skillAbilityShortStrings[skillAbilityKeys[i]]);
				}
				
				updateCharacter(character);
			}
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param context
	 * @return a character newly added into the database
	 */
	public PTCharacter addNewCharacter(String name, Context context){
		PTCharacter newCharacter = new PTCharacter(name, context);
		ContentValues cv=new ContentValues();
		cv.put(colChtrName, newCharacter.getName());
		cv.put(colChtrGSON, mGson.toJson(newCharacter));
		
		open();
		mDatabase.insert(characterTable, null, cv);

		//Getting the character's id
		Cursor cursor = mDatabase.query(characterTable, new String[]{colChtrID}, null ,
				null, null, null, colChtrID);

		cursor.moveToLast();
		int characterID = cursor.getInt(cursor.getColumnIndex(colChtrID));
		close();
		
		newCharacter.mID = characterID;
		updateCharacter(newCharacter);
		Log.v(TAG, "New character added to database: " + characterID);
		
		return newCharacter;
	}
	
	/**
	 * Updates the character's info in the database. Will only work if the character was created using PTDatabaseManager
	 * @param character
	 */
	public void updateCharacter(PTCharacter character){
		ContentValues editCon = new ContentValues();
		editCon.put(colChtrName, character.getName());
		editCon.put(colChtrGSON, mGson.toJson(character));

		open();
		mDatabase.update(characterTable, editCon, colChtrID+"="+character.mID, null);
		close();
		Log.v(TAG, "Character saved in database");
	}
	
	/**
	 * 
	 * @param id
	 * @return the character with the id passed
	 */
	public PTCharacter getCharacter(int id){
		Log.v(TAG, "Retrieving character");
		open();
		Cursor cursor = mDatabase.query(characterTable, null, colChtrID+"="+id  , null, null, null, null);
		
		cursor.moveToFirst();
		PTCharacter character = mGson.fromJson(cursor.getString(cursor.getColumnIndex(colChtrGSON)), PTCharacter.class);
		close();
		Log.v(TAG, "Character retrieved from database");
		return character;
	}
	
	/**
	 * 
	 * @return an array in int, filled with the IDs of all currently available characters in the database
	 */
	public int[] getCharacterIDs(){
		open();
		Cursor cursor = mDatabase.query(characterTable, new String[]{colChtrID}, null, null, null, null, colChtrID);
		
		int characterIDs[] = new int[cursor.getCount()];
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			characterIDs[cursor.getPosition()] = cursor.getInt(cursor.getColumnIndex(colChtrID));
			cursor.moveToNext();
		}
		return characterIDs;
	}
	
	/**
	 * 
	 * @return an array of strings with the names of all current characters, ordered by their IDs
	 */
	public String[] getCharacterNames(){
		open();
		Cursor cursor = mDatabase.query(characterTable, new String[]{colChtrName}, null, null, null, null, colChtrID);
		
		String characterNames[] = new String[cursor.getCount()];
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			characterNames[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(colChtrName));
			cursor.moveToNext();
		}
		
		return characterNames;
	}
	
	/**
	 * deletes the character with the passed id
	 * @param id
	 */
	public void deleteCharacter(int id){
		open();
		mDatabase.delete(characterTable, colChtrID+"="+id, null);
		close();
		Log.v(TAG, "Character deleted from database");
	}
	
	/**
	 * 
	 * @param name
	 * @return a party newly added into the database
	 */
	public PTParty addNewParty(String name){
		PTParty newParty = new PTParty(name);
		ContentValues cv=new ContentValues();
		cv.put(colPrtyName, newParty.getName());
		cv.put(colPrtyGSON, mGson.toJson(newParty));
		
		open();
		mDatabase.insert(partyTable, null, cv);

		//Getting the party's id
		Cursor cursor = mDatabase.query(partyTable, new String[]{colPrtyID}, null ,
				null, null, null, colPrtyID);

		cursor.moveToLast();
		int partyID = cursor.getInt(cursor.getColumnIndex(colPrtyID));
		close();
		
		newParty.mID = partyID;
		updateParty(newParty);
		Log.v(TAG, "New party added to database: " + partyID); 
		
		return newParty;
	}
	
	/**
	 * Updates the party's info in the database. Will only work if the party was created using PTDatabaseManager
	 * @param party
	 */
	public void updateParty(PTParty party){
		ContentValues editCon = new ContentValues();
		editCon.put(colPrtyName, party.getName());
		editCon.put(colPrtyGSON, mGson.toJson(party));

		open();
		mDatabase.update(partyTable, editCon, colPrtyID+"="+party.mID, null);
		close();
		Log.v(TAG, "Party saved in database");
	}
	
	/**
	 * 
	 * @param id
	 * @return the party with the id passed
	 */
	public PTParty getParty(int id){
		Log.v(TAG, "Retrieving party");
		open();
		Cursor cursor = mDatabase.query(partyTable, null, colPrtyID+"="+id  , null, null, null, null);
		
		cursor.moveToFirst();
		PTParty party = mGson.fromJson(cursor.getString(cursor.getColumnIndex(colPrtyGSON)), PTParty.class);
		close();
		Log.v(TAG, "Party retrieved from database");
		return party;
	}
	
	/**
	 * 
	 * @return an array in int, filled with the IDs of all currently available partys in the database
	 */
	public int[] getPartyIDs(){
		open();
		Cursor cursor = mDatabase.query(partyTable, new String[]{colPrtyID}, null, null, null, null, colPrtyID);
		
		int partyIDs[] = new int[cursor.getCount()];
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			partyIDs[cursor.getPosition()] = cursor.getInt(cursor.getColumnIndex(colPrtyID));
			cursor.moveToNext();
		}
		return partyIDs;
	}
	
	/**
	 * 
	 * @return an array of strings with the names of all current partys, ordered by their IDs
	 */
	public String[] getPartyNames(){
		open();
		Cursor cursor = mDatabase.query(partyTable, new String[]{colPrtyName}, null, null, null, null, colPrtyID);
		
		String partyNames[] = new String[cursor.getCount()];
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			partyNames[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(colPrtyName));
			cursor.moveToNext();
		}
		
		return partyNames;
	}
	
	/**
	 * deletes the party with the passed id
	 * @param id
	 */
	public void deleteParty(int id){
		open();
		mDatabase.delete(partyTable, colPrtyID+"="+id, null);
		close();
		Log.v(TAG, "Party deleted from database");
	}
	
	public void open() throws SQLException {
		//open database in reading/writing mode
		mDatabase = this.getWritableDatabase();
	}

	public void close() {
		if (mDatabase != null)
		mDatabase.close();
	}
}
