package com.ucontrolrecipes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;

public class SavedRecipesSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static String DATABASE_DIRECTORY = File.separator + "U Control Recipes" + File.separator;
    public static final String DATABASE_NAME = "Saved_Recipes.db";
    public static final String RECIPES_TABLE_NAME = "recipes";
    public static final String RECIPES_COLUMN_ID = "_id";
    public static final String RECIPES_COLUMN_TITLE = "title";
    public static final String RECIPES_COLUMN_INGREDIENTS_LIST = "ingredientsList";
    public static final String RECIPES_COLUMN_INSTRUCTIONS = "instructions";
    public static final String RECIPES_COLUMN_IMAGE_URL = "imageURL";
    public static final String RECIPES_COLUMN_READY_IN_MINUTES = "readyInMinutes";
    public static final String RECIPES_COLUMN_SERVINGS = "servings";

    public SavedRecipesSQLiteDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, context.getFilesDir() + DATABASE_DIRECTORY + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE " + RECIPES_TABLE_NAME + " (" +
                RECIPES_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                RECIPES_COLUMN_TITLE + " TEXT, " +
                RECIPES_COLUMN_INGREDIENTS_LIST + " TEXT, " +
                RECIPES_COLUMN_INSTRUCTIONS + " TEXT, " +
                RECIPES_COLUMN_IMAGE_URL + " TEXT, " +
                RECIPES_COLUMN_READY_IN_MINUTES + " INT UNSIGNED, " +
                RECIPES_COLUMN_SERVINGS + " INT UNSIGNED" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
