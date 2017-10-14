package com.nilanshi.nigam.personalassistant.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static com.nilanshi.nigam.personalassistant.R.drawable.login;

/**
 * Created by HP on 13-Oct-17.
 */

/**
 * Helper class to handle all the callbacks that occur when interacting with loaders.  Most of the
 * interesting code in this sample app will be in this file.
 */
public class ContactablesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    Context mContext;

    public static final String QUERY_KEY = "query";

    public static final String TAG = "ContactablesLoaderCallbacks";
    private int phoneColumnIndex;
    private int emailColumnIndex;
    private int nameColumnIndex;

    public ContactablesLoaderCallbacks(Context context) {
        mContext = context;

    }

    public ContactablesLoaderCallbacks(MyIntent myIntent) {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderIndex, Bundle args) {
        // Where the Contactables table excels is matching text queries,
        // not just data dumps from Contacts db.  One search term is used to query
        // display name, email address and phone number.  In this case, the query was extracted
        // from an incoming intent in the handleIntent() method, via the
        // intent.getStringExtra() method.

        // BEGIN_INCLUDE(uri_with_query)
        String query = args.getString(QUERY_KEY);
        Uri uri = Uri.withAppendedPath(
                CommonDataKinds.Contactables.CONTENT_FILTER_URI, query);
        // END_INCLUDE(uri_with_query)


        // BEGIN_INCLUDE(cursor_loader)
        // Easy way to limit the query to contacts with phone numbers.
        String selection =
                CommonDataKinds.Contactables.HAS_PHONE_NUMBER + " = " + 1;

        // Sort results such that rows for the same contact stay together.
        String sortBy = CommonDataKinds.Contactables.LOOKUP_KEY;

        return new CursorLoader(
                mContext,  // Context
                uri,       // URI representing the table/resource to be queried
                null,      // projection - the list of columns to return.  Null means "all"
                selection, // selection - Which rows to return (condition rows must match)
                null,      // selection args - can be provided separately and subbed into selection.
                sortBy);   // string specifying sort order
        // END_INCLUDE(cursor_loader)
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

        // Pulling the relevant value from the cursor requires knowing the column index to pull
        // it from.
        // BEGIN_INCLUDE(get_columns)
        phoneColumnIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
        emailColumnIndex = cursor.getColumnIndex(CommonDataKinds.Email.ADDRESS);
        nameColumnIndex = cursor.getColumnIndex(CommonDataKinds.Contactables.DISPLAY_NAME);
        // END_INCLUDE(get_columns)
        /*Intent intent=new Intent(ContactablesLoaderCallbacks.this, CommandActivity.class);
        intent.putExtra("com.nilanshi.nigam.personalassistant", phoneColumnIndex);
        intent.putExtra("com.nilanshi.nigam.personalassistant", emailColumnIndex);
        intent.putExtra("com.nilanshi.nigam.personalassistant", nameColumnIndex);
        mContext.startActivity(intent);*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

}
