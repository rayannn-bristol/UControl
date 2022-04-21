package com.ucontrolrecipes;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ViewRecipeActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String TAG_VIEW_RECIPE = "TAG_VIEW_RECIPE";
    public static final String TAG_VIEW_SIMILAR_RECIPES = "TAG_VIEW_SIMILAR_RECIPES";
    public static final String TAG_VIEW_SIMILAR_RECIPES_IMAGE = "TAG_VIEW_SIMILAR_RECIPES_IMAGE";
    private TextView textViewRecipeName;
    private TextView textViewRecipeIngredients;
    private TextView textViewPrepMethod;
    private TextView textViewPrepTime;
    private TextView textViewServingSize;
    private TextView textViewSimilarRecipesFound;
    private ImageView imageViewRecipe;
    private ImageView[] imageViewSimilarRecipes;
    private Button buttonShareRecipe;
    private Button buttonSaveRecipe;
    private LinearLayout linearLayoutSimilarRecipes;
    private AppBarConfiguration mAppBarConfiguration;
    private RecipeData[] similarRecipeData;
    private Recipe recipe;
    private RequestQueue queue; // creating queue object
    private RequestQueue queue2; // creating queue object
    private RequestQueue queue3; // creating queue object
    private RequestQueue queue4; // creating queue object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing the queue object
        queue = Volley.newRequestQueue(this);
        // initializing the queue object
        queue2 = Volley.newRequestQueue(this);
        // initializing the queue object
        queue3 = Volley.newRequestQueue(this);
        // initializing the queue object
        queue4 = Volley.newRequestQueue(this);

        recipe = null;

        setContentView(R.layout.fragment_view_recipe);

        textViewRecipeName = (TextView) findViewById(R.id.textViewRecipeName);
        textViewRecipeIngredients = (TextView) findViewById(R.id.textViewRecipeIngredients);
        textViewPrepMethod = (TextView) findViewById(R.id.textViewPrepMethod);
        textViewPrepTime = (TextView) findViewById(R.id.textViewPrepTime);
        textViewServingSize = (TextView) findViewById(R.id.textViewServingSize);

        textViewSimilarRecipesFound = (TextView) findViewById(R.id.textViewSimilarRecipesFound);

        imageViewRecipe = (ImageView) findViewById(R.id.imageViewRecipe);

        imageViewSimilarRecipes = new ImageView[5];
        imageViewSimilarRecipes[0] = (ImageView) findViewById(R.id.imageViewSimilarRecipe1);
        imageViewSimilarRecipes[1] = (ImageView) findViewById(R.id.imageViewSimilarRecipe2);
        imageViewSimilarRecipes[2] = (ImageView) findViewById(R.id.imageViewSimilarRecipe3);
        imageViewSimilarRecipes[3] = (ImageView) findViewById(R.id.imageViewSimilarRecipe4);
        imageViewSimilarRecipes[4] = (ImageView) findViewById(R.id.imageViewSimilarRecipe5);

        similarRecipeData = new RecipeData[5];

        linearLayoutSimilarRecipes = (LinearLayout) findViewById(R.id.linearLayoutSimilarRecipes);

        buttonShareRecipe = findViewById(R.id.buttonShareRecipe);
        buttonShareRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutRecipeDetails);
                shareRecipe((View) linearLayout);
            }
        });

        buttonSaveRecipe = (Button) findViewById(R.id.buttonSaveRecipe);
        buttonSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipe != null){
                    try {
                        saveToDB(recipe);
                    } catch (Exception e){
                        Snackbar.make(view, "Error occurred", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        String recipeID = getIntent().getExtras().getString(EXTRA_RECIPE_ID);

        if (recipeID != null) {
            // cancelling all requests about this search if in queue
            queue.cancelAll(TAG_VIEW_RECIPE);

            // first StringRequest: getting items searched
            StringRequest stringRequest = searchRecipeStringRequest(recipeID);
            stringRequest.setTag(TAG_VIEW_RECIPE);

            // executing the request (adding to queue)
            queue.add(stringRequest);


            // cancelling all requests about this search if in queue
            queue2.cancelAll(TAG_VIEW_SIMILAR_RECIPES);

            // first StringRequest: getting items searched
            StringRequest stringRequest2 = searchSimilarRecipesStringRequest(recipeID);
            stringRequest.setTag(TAG_VIEW_SIMILAR_RECIPES);

            // executing the request (adding to queue)
            queue2.add(stringRequest2);
        } else {
            Snackbar.make(textViewRecipeName, "Error occurred", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        //setContentView(binding.getRoot());

        /*
        setSupportActionBar(binding.appBarSearch.toolbar);
        binding.appBarSearch.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saved_recipes:
                Intent intent = new Intent(this, SavedRecipesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    */

    private Bitmap viewToImage(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        return returnedBitmap;
    }

    private void shareRecipe(View view){
        Bitmap b = viewToImage(view);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                b, "Title", null);
        Uri imageUri =  Uri.parse(path);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);

        String packageName = getApplicationContext().getPackageName();
        String promotionString = "Recipe from " + getString(R.string.app_name) + "! Try this app!";

        if (packageName != null)
            promotionString += " https://play.google.com/store/apps/details?id=" + packageName;

        share.putExtra(Intent.EXTRA_TEXT, promotionString);

        startActivity(Intent.createChooser(share, "Select"));
    }

    private StringRequest searchRecipeStringRequest(String recipeID) {
        final String API_KEY = API.API_KEY;
        final String URL_PREFIX = "https://api.spoonacular.com/recipes/";
        final String INFORMATION = "information?";
        final String INCLUDE_NUTRITION = "&includeNutrition=false";

        String url = URL_PREFIX;

        if (recipeID != null)
            url += recipeID + "/";

        url += INFORMATION + API_KEY + INCLUDE_NUTRITION;
        //https://api.spoonacular.com/recipes/636589/information?&apiKey=0ed87321d6664d1d89368136a483e058&includeNutrition=false

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String title = jsonObj.getString("title");
                            textViewRecipeName.setText(title);

                            //Ingredients goes here
                            String ingredientsList = "";
                            JSONArray extendedIngredients = jsonObj.getJSONArray("extendedIngredients");
                            for (int i = 0; i < extendedIngredients.length(); ++i) {
                                JSONObject ingredient = extendedIngredients.getJSONObject(i);
                                String ingredientName = ingredient.getString("original");
                                ingredientsList += ingredientName + "\n";
                            }

                            textViewRecipeIngredients.setText(ingredientsList);

                            //Prep Method
                            String instructions = jsonObj.getString("instructions");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                textViewPrepMethod.setText(Html.fromHtml(instructions, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                textViewPrepMethod.setText(Html.fromHtml(instructions));
                            }

                            String imageURL = jsonObj.getString("image");
                            Picasso.get().load(imageURL).into(imageViewRecipe);
                            imageViewRecipe.setVisibility(View.VISIBLE);

                            //Prep Time
                            int readyInMinutes = jsonObj.getInt("readyInMinutes");
                            String prepTime = "Preparation Time: " + Integer.toString(readyInMinutes) + " minutes";
                            textViewPrepTime.setText(prepTime);

                            //Calories count
                            int servings = jsonObj.getInt("servings");
                            String servingSize = "Serving Size: " + Integer.toString(servings);
                            textViewServingSize.setText(servingSize);

                            recipe = new Recipe(Integer.parseInt(recipeID), title, ingredientsList, instructions, imageURL, readyInMinutes, servings);

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(ViewRecipeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(ViewRecipeActivity.this, "Recipe source is not responding (Spoonacular API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private StringRequest searchRecipeImageStringRequest(String recipeID, ImageView iv) {
        final String API_KEY = API.API_KEY;
        final String URL_PREFIX = "https://api.spoonacular.com/recipes/";
        final String INFORMATION = "information?";
        final String INCLUDE_NUTRITION = "&includeNutrition=false";

        String url = URL_PREFIX;

        if (recipeID != null)
            url += recipeID + "/";

        url += INFORMATION + API_KEY + INCLUDE_NUTRITION;
        //https://api.spoonacular.com/recipes/636589/information?&apiKey=0ed87321d6664d1d89368136a483e058&includeNutrition=false

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            String imageURL = jsonObj.getString("image");
                            Picasso.get().load(imageURL).into(iv);
                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ImageView imageViewSimilarRecipe = (ImageView) view;
                                    String id = "";

                                    for (int i = 0; i < 5; ++i){
                                        if (imageViewSimilarRecipe == imageViewSimilarRecipes[i])
                                            if (similarRecipeData[i] != null) {
                                                id = Integer.toString(similarRecipeData[i].getID());
                                                break;
                                            }
                                    }

                                    // cancelling all requests about this search if in queue
                                    queue4.cancelAll(TAG_VIEW_RECIPE);

                                    // first StringRequest: getting items searched
                                    StringRequest stringRequest = searchRecipeStringRequest(id);
                                    stringRequest.setTag(TAG_VIEW_RECIPE);

                                    // executing the request (adding to queue)
                                    queue4.add(stringRequest);
                                }
                            });
                            iv.setVisibility(View.VISIBLE);

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(ViewRecipeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(ViewRecipeActivity.this, "Recipe source is not responding (Spoonacular API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private StringRequest searchSimilarRecipesStringRequest(String recipeID) {
        final String API_KEY = API.API_KEY;
        final String URL_PREFIX = "https://api.spoonacular.com/recipes/";
        final String SIMILAR = "similar?";
        final String MAX_ROWS = "&number=";
        final String MAX_LIMIT = "5";

        String url = URL_PREFIX;

        if (recipeID != null)
            url += recipeID + "/";

        url += SIMILAR + API_KEY + MAX_ROWS + MAX_LIMIT;
        //https://api.spoonacular.com/recipes/636589/similar?&apiKey=0ed87321d6664d1d89368136a483e058&number=5

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() == 0){
                                textViewSimilarRecipesFound.setVisibility(View.VISIBLE);
                            } else {
                                linearLayoutSimilarRecipes.setVisibility(View.VISIBLE);

                                // cancelling all requests about this search if in queue
                                queue3.cancelAll(TAG_VIEW_SIMILAR_RECIPES_IMAGE);

                                for (int i = 0; i < jsonArray.length(); ++i) {
                                    JSONObject recipe = jsonArray.getJSONObject(i);
                                    String id = recipe.getString("id");

                                    similarRecipeData[i] = new RecipeData(Integer.parseInt(id),"","","");

                                    // first StringRequest: getting items searched
                                    StringRequest stringRequest = searchRecipeImageStringRequest(id, imageViewSimilarRecipes[i]);
                                    stringRequest.setTag(TAG_VIEW_SIMILAR_RECIPES_IMAGE);

                                    // executing the request (adding to queue)
                                    queue3.add(stringRequest);
                                }
                            }

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(ViewRecipeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(ViewRecipeActivity.this, "Recipe source is not responding (Spoonacular API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveToDB(Recipe recipe){
        SQLiteDatabase database = new SavedRecipesSQLiteDBHelper(this,null, null, 0).getWritableDatabase();
        ContentValues values = new ContentValues();

        if (recipe != null){
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_ID, recipe.getId());
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_TITLE, recipe.getTitle());
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_INGREDIENTS_LIST, recipe.getIngredientsList());
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_INSTRUCTIONS, recipe.getInstructions());
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_IMAGE_URL, recipe.getImageURL());
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_READY_IN_MINUTES, recipe.getReadyInMinutes());
            values.put(SavedRecipesSQLiteDBHelper.RECIPES_COLUMN_SERVINGS, recipe.getServings());

            long newRowId = database.insert(SavedRecipesSQLiteDBHelper.RECIPES_TABLE_NAME, null, values);
            if (newRowId == -1){
                Toast.makeText(this, "Recipe already in database", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Recipe successfully saved!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
