/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetCursorAdapter;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PET_LOADER = 0;
    private PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        // Set up Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new PetCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new Intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // For the content URI that represents the spesific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // If the pet with ID 2 was clicked on.
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                // Launch the{@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        // Kick off the Loader
        getSupportLoaderManager().initLoader(PET_LOADER, null, this);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

    // Sementara membantu method menampilkan informasi di LayarScreen TextView tentang database pets
//    private void displayDatabaseInfo() {
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT};
//        // perform a query on the pets table
//        Cursor cursor = getContentResolver().query(
//                PetEntry.CONTENT_URI,    //the content URI of the words table
//                projection,             //the column to return for each row
//                null,                   //selection criteria
//                null,                   //selection criteria
//                null);                  //The sort order
//
//        // Find the ListView which will be populated with the pet data
//        ListView petListView = (ListView) findViewById(R.id.list);
//
//        // Set up an adapter to create a list item for each row of pet data in the Cursor
//        PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);
//
//        // Attach the adapter to the ListView
//        petListView.setAdapter(adapter);


//        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI, projection,null,null,null);
//
//        TextView displayView = (TextView) findViewById(R.id.text_view_pet);
//
//        try {
//            // Create a header in the Text View that looks like this:
//            //
//            // The pets table contains <number of rows in Cursor> pets.
//            // _id - name - breed - gender - weight
//            //
//            // In the while loop below, iterate through the rows of the cursor and display
//            // the information from each column in this order.
//            displayView.setText("The pets table contains : " + cursor.getCount() + " pets.\n\n");
//            displayView.append(PetEntry._ID + " - " +
//                    PetEntry.COLUMN_PET_NAME + " - " +
//                    PetEntry.COLUMN_PET_BREED + " - " +
//                    PetEntry.COLUMN_PET_GENDER + " - " +
//                    PetEntry.COLUMN_PET_WEIGHT + "\n");
//
//            // figure out the index of each column
//            int idColumnIndex=cursor.getColumnIndex(PetEntry._ID);
//            int nameColumnIndex=cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
//            int breedColumnIndex=cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
//            int genderColumnIndex=cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
//            int weightColumnIndex=cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
//
//            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//                // Use that index to extract the string or Int value of the word
//                // at the current row the cursor is on
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentBreed = cursor.getString(breedColumnIndex);
//                int currentGender = cursor.getInt(genderColumnIndex);
//                int currentWeight = cursor.getInt(weightColumnIndex);
//                // Display the values from each column of the current row in the cursor in the TextView
//                displayView.append(("\n" + currentID + " - " +
//                        currentName + " - " +
//                        currentBreed + " - " +
//                        currentGender + " - " +
//                        currentWeight));
//            }
//        }finally {
//            // Selalu menutup kursor ketika Anda selesai membaca dari itu. Hal ini melepaskan semua nya
//            // pada resources dan membuatnya tidak valid
//            cursor.close();
//        }
//    }

    // Helper method to insert hardcoded pet data into the database. For debugging purposes only.
    private void insertPet() {

        // Buat ContentValues ​​obyek di mana nama kolom adalah kunci
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
       Uri newUri=getContentResolver().insert(PetEntry.CONTENT_URI,values);

        Log.v("CatalogActivity","New Row ID "+ newUri);
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertPet();
//                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED};

        // This Loader will execute the contentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                PetEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Column to include in the resulting Cursor
                null,                   // selection clause
                null,                   // selection argument
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

}
