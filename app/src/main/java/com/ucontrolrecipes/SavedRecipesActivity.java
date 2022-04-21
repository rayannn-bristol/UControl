package com.ucontrolrecipes;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SavedRecipesActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    private long numRecipes;
    private SQLiteDatabase database;
    private TextView textViewSavedRecipes;
    private ListView listViewRecipes;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_saved_recipes);
        database = new SavedRecipesSQLiteDBHelper(this,null, null, 0).getReadableDatabase();
        numRecipes =  DatabaseUtils.queryNumEntries(database, SavedRecipesSQLiteDBHelper.RECIPES_TABLE_NAME);
        textViewSavedRecipes = (TextView) findViewById(R.id.textViewSavedRecipes);
        listViewRecipes = (ListView) findViewById(R.id.listViewRecipes);

        if (numRecipes != 0){
            textViewSavedRecipes.setText("Saved Recipes: " + numRecipes);


            // on below line we are creating a cursor with query to read data from database.
            Cursor cursorRecipes = database.rawQuery("SELECT * FROM " + SavedRecipesSQLiteDBHelper.RECIPES_TABLE_NAME, null);

            // on below line we are creating a new array list.
            ArrayList<Recipe> recipesArrayList = new ArrayList<Recipe>();

            // moving our cursor to first position.
            if (cursorRecipes.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    recipesArrayList.add(new Recipe(cursorRecipes.getInt(0),
                            cursorRecipes.getString(1),
                            cursorRecipes.getString(2),
                            cursorRecipes.getString(3),
                            cursorRecipes.getString(4),
                            cursorRecipes.getInt(5),
                            cursorRecipes.getInt(6)));
                } while (cursorRecipes.moveToNext());
                // moving our cursor to next.
            }
            // at last closing our cursor
            cursorRecipes.close();

            ArrayList<String> recipeNamesArrayList = new ArrayList<String>();
            for (int i = 0; i < recipesArrayList.size(); ++i)
                recipeNamesArrayList.add(recipesArrayList.get(i).getTitle());

            ArrayAdapter<String> arrayAdapter;
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.recipes_listview, R.id.textViewRecipeNameItem, recipeNamesArrayList);
            listViewRecipes.setAdapter(arrayAdapter);


            listViewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Intent i = new Intent(getApplicationContext(), ViewSavedRecipeActivity.class);
                    i.putExtra(EXTRA_RECIPE, recipesArrayList.get(pos));
                    startActivity(i);
                }
            });

            listViewRecipes.setVisibility(View.VISIBLE);
        } else {
            textViewSavedRecipes.setText("No Saved Recipes to Display");
        }
    }
}
