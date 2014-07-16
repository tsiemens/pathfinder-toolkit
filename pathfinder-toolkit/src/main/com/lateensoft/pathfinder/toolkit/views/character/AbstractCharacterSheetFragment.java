package com.lateensoft.pathfinder.toolkit.views.character;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterModelDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterNameDAO;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.util.*;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.dom4j.DocumentException;
import roboguice.RoboGuice;

//This is a base class for all fragments in the character sheet activity
public abstract class AbstractCharacterSheetFragment extends BasePageFragment {
    private static final String TAG = AbstractCharacterSheetFragment.class.getSimpleName();

    public static final int GET_IMPORT_REQ_CODE = 3056;
    
    private long currentCharacterID;

    private CharacterModelDAO characterModelDao;
    private CharacterNameDAO characterNameDao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterModelDao = new CharacterModelDAO(getContext());
        characterNameDao = new CharacterNameDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void updateTitle() {
        setTitle(characterNameDao.find(currentCharacterID).getName());
        setSubtitle(getFragmentTitle());
    }

    @Override
    public void onPause() {
        updateDatabase();
        super.onPause();
    }

    @Override
    protected void onResumeWithoutResult() {
        super.onResumeWithoutResult();
        loadSelectedCharacter();
    }

    private void setSelectedCharacter(long characterId) {
        Preferences preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
        preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, characterId);
        currentCharacterID = characterId;
    }

    /**
     * Load the currently set character in shared prefs If there is no character
     * set in user prefs, it automatically generates a new one.
     */
    public void loadSelectedCharacter() {
        Preferences preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
        long currentCharacterID = preferences.get(GlobalPrefs.SELECTED_CHARACTER_ID, -1L);

        if (currentCharacterID == -1) { 
            // There was no current character set in shared prefs
            Log.v(TAG, "Default character add");
            addNewCharacterAndSelect();
        } else {
            this.currentCharacterID = currentCharacterID;
            loadFromDatabase();
            if (getRootView() != null) {
                updateFragmentUI();
            }
        }
        updateTitle();
    }

    /**
     * Generates a new character and sets it to the current character. Reloads
     * the fragments.
     */
    public void addNewCharacterAndSelect() {
        PathfinderCharacter newChar = PathfinderCharacter.newDefaultCharacter("New Adventurer");
        long id = addCharacterToDB(newChar);
        if (id != -1) {
            setSelectedCharacter(id);
        } else {
            Toast.makeText(getContext(), "Error creating new character. Please contact developers for support if issue persists.", Toast.LENGTH_LONG).show();
        }
        performUpdateReset();
    }

    public long addCharacterToDB(PathfinderCharacter character) {
        long id = -1;
        try {
            id = characterModelDao.add(character);
            Log.i(TAG, "Added new character");
        } catch (DataAccessException e) {
            Log.e(TAG, "Error occurred creating new character", e);
        }
        return id;
    }

    /**
     * Deletes the current character and loads the first in the list, or creates
     * a new blank one, if there was only one.
     */
    public void deleteCurrentCharacter() {
        int currentCharacterIndex = 0;
        List<IdNamePair> characters = characterNameDao.findAll();
        long characterForDeletion = currentCharacterID;

        for (int i = 0; i < characters.size(); i++) {
            if (characterForDeletion == characters.get(i).getId()){
                currentCharacterIndex = i;
                break;
            }
        }

        if (characters.size() == 1) {
            addNewCharacterAndSelect();
        } else {
            int charToSelect = (currentCharacterIndex == 0) ? 1 : 0;
            setSelectedCharacter(characters.get(charToSelect).getId());
            loadSelectedCharacter();
        }

        try {
            characterModelDao.removeById(characterForDeletion);
            Log.i(TAG, "Deleted current character: " + characterForDeletion);
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to delete character " + characterForDeletion, e);
            Toast.makeText(getContext(), "Failed to delete character", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_character_list:
                showCharacterSelectionDialog();
                break;
            case R.id.mi_new_character:
                showCreateCharacterDialog();
                break;
            case R.id.mi_delete_character:
                showDeleteCharacterDialog();
                break;
            case R.id.mi_export_character:
                exportCurrentCharacter();
                break;
            case R.id.mi_export_all:
                exportAllCharacters();
                break;
            case R.id.mi_import_character:
                importCharacters();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.character_sheet_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showCharacterSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.select_character_dialog_header));

        List<IdNamePair> characterEntries = characterNameDao.findAll();
        String[] characterNames = IdNamePair.nameArray(characterEntries);
        int currentCharacterIndex = 0;

        for (int i = 0; i < characterEntries.size(); i++) {
            if (currentCharacterID == characterEntries.get(i).getId()) {
                currentCharacterIndex = i;
            }
        }

        CharacterSelectionClickListener clickListener = new CharacterSelectionClickListener(characterEntries, currentCharacterIndex);
        builder.setSingleChoiceItems(characterNames, currentCharacterIndex,
                clickListener)
                .setPositiveButton(R.string.ok_button_text, clickListener)
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.show();
    }

    private class CharacterSelectionClickListener implements OnClickListener {
        private List<IdNamePair> characterEntries;
        private long selectedCharacterId;

        public CharacterSelectionClickListener(List<IdNamePair> characterEntries, int initialSelection) {
            this.characterEntries = characterEntries;
            this.selectedCharacterId = characterEntries.get(initialSelection).getId();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (selectedCharacterId != currentCharacterID) {
                    updateDatabase();

                    setSelectedCharacter(selectedCharacterId);
                    loadSelectedCharacter();
                }
            } else if (which >= 0 && which < characterEntries.size()) {
                selectedCharacterId = characterEntries.get(which).getId();
            }
        }
    }

    private void showCreateCharacterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.menu_item_new_character));
        builder.setMessage(getString(R.string.new_character_dialog_message))
                .setPositiveButton(R.string.ok_button_text, new OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        performUpdateReset();
                        addNewCharacterAndSelect();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.show();
    }

    private void showDeleteCharacterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.menu_item_delete_character));
        builder.setMessage(String.format(getString(R.string.confirm_delete_item_message),
                        characterNameDao.find(currentCharacterID)))
                .setPositiveButton(R.string.delete_button_text, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentCharacter();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.show();
    }

    public void exportCurrentCharacter() {
        PathfinderCharacter character = characterModelDao.find(getCurrentCharacterID());
        ImportExportUtils.exportCharacterWithDialog(getContext(), character, new ActivityLauncher.ActivityLauncherFragment(this));
    }

    public void exportAllCharacters() {
        List<PathfinderCharacter> characters = characterModelDao.findAll();
        ImportExportUtils.exportCharactersWithDialog(getContext(), characters, "All Characters", new ActivityLauncher.ActivityLauncherFragment(this));
    }

    public void importCharacters() {
        ImportExportUtils.importCharacterFromContent(new ActivityLauncher.ActivityLauncherFragment(this),
                GET_IMPORT_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMPORT_REQ_CODE ) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Exception thrownException = null;
                try {
                    if (uri == null) {
                        throw new FileNotFoundException();
                    }

                    InputStream is = getContext().getContentResolver().openInputStream(uri);
                    List<PathfinderCharacter> characters = ImportExportUtils.importCharactersFromStream(is);

                    long lastCharacterId = -1;
                    boolean didFailToImport = false;
                    for(PathfinderCharacter character : characters) {
                        lastCharacterId = addCharacterToDB(character);
                        if (lastCharacterId == -1) {
                            didFailToImport = true;
                        }
                    }
                    if (lastCharacterId != -1) {
                        setSelectedCharacter(lastCharacterId);
                        loadSelectedCharacter();
                    }
                    if(didFailToImport) {
                        Toast.makeText(getContext(), R.string.import_error_unknown, Toast.LENGTH_LONG).show();
                    }

                } catch (FileNotFoundException e) {
                    Toast.makeText(getContext(), R.string.import_error_file_not_found, Toast.LENGTH_LONG).show();
                    thrownException = e;
                } catch (DocumentException e) {
                    Toast.makeText(getContext(), R.string.import_error_invalid_xml, Toast.LENGTH_LONG).show();
                    thrownException = e;
                } catch (InvalidObjectException e) {
                    Toast.makeText(getContext(), String.format(getString(R.string.import_error_exception), e.getMessage()),
                            Toast.LENGTH_LONG).show();
                    thrownException = e;
                } finally {
                    if (thrownException != null) {
                        Log.e(TAG, "Failed to import character(s) from " + uri, thrownException);
                    }
                }
            } else {
                Toast.makeText(getContext(), R.string.unable_to_load_file_msg, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Depending on the use, this forces the current tab to save its values to
     * mCharacter, and updates them. Ends with current tab set to Fluff.
     */
    public void performUpdateReset() {
        switchToPage(CharacterFluffFragment.class);
    }

    public long getCurrentCharacterID() {
        return currentCharacterID;
    }
    
    public CharacterModelDAO getCharacterModelDAO() {
        return characterModelDao;
    }

    /**
     * Refreshes the UI. Is automatically called onResume
     */
    public abstract void updateFragmentUI();
    
    /**
     * @return The title of the fragment instance
     */
    public abstract String getFragmentTitle();
    
    /**
     * Called to have the subclass update any relevant parts of the database.
     * Called onPause, among other areas.
     */
    public abstract void updateDatabase();
    
    /**
     * Called to notify the base class that it should load its data from the database.
     * Called onResume
     */
    public abstract void loadFromDatabase();

}
