package com.ucontrolrecipes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class SearchResultsActivity extends AppCompatActivity {

    public static final String EXTRA_INGREDIENT_NAME = "EXTRA_INGREDIENT_NAME";
    public static final String EXTRA_MAX_CALORIES = "EXTRA_MAX_CALORIES";
    public static final String EXTRA_MAX_PREP_TIME = "EXTRA_MAX_PREP_TIME";
    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String TAG_SEARCH_QUERY = "TAG_SEARCH_QUERY";
    public static final String TAG_SEARCH_QUERY_PREVIOUS = "TAG_SEARCH_QUERY_PREVIOUS";
    public static final String TAG_SEARCH_QUERY_NEXT = "TAG_SEARCH_QUERY_NEXT";
    private RecipeData[] recipeData;
    private TextView textViewResultsCount;
    private Spinner spinnerSortingOptions;
    private TextView[] textViewResults;
    private ImageView[] imageViewResults;
    private LinearLayout[] linearLayoutResults;
    private LinearLayout linearLayoutNavigationButtons;
    private Button buttonPrevious;
    private Button buttonNext;
    private AppBarConfiguration mAppBarConfiguration;
    private RequestQueue queue; // creating queue object
    private RequestQueue queue2; // creating queue object
    private RequestQueue queue3; // creating queue object
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing the queue object
        queue = Volley.newRequestQueue(this);
        // initializing the queue object
        queue2 = Volley.newRequestQueue(this);
        // initializing the queue object
        queue3 = Volley.newRequestQueue(this);

        setContentView(R.layout.fragment_search_result);

        offset = 0;

        textViewResultsCount = (TextView) findViewById(R.id.textViewResultsCount);

        recipeData = new RecipeData[5];

        textViewResults = new TextView[5];
        textViewResults[0] = (TextView) findViewById(R.id.textViewRecipe1);
        textViewResults[1] = (TextView) findViewById(R.id.textViewRecipe2);
        textViewResults[2] = (TextView) findViewById(R.id.textViewRecipe3);
        textViewResults[3] = (TextView) findViewById(R.id.textViewRecipe4);
        textViewResults[4] = (TextView) findViewById(R.id.textViewRecipe5);

        imageViewResults = new ImageView[5];
        imageViewResults[0] = (ImageView) findViewById(R.id.imageViewRecipe1);
        imageViewResults[1] = (ImageView) findViewById(R.id.imageViewRecipe2);
        imageViewResults[2] = (ImageView) findViewById(R.id.imageViewRecipe3);
        imageViewResults[3] = (ImageView) findViewById(R.id.imageViewRecipe4);
        imageViewResults[4] = (ImageView) findViewById(R.id.imageViewRecipe5);

        linearLayoutResults = new LinearLayout[5];
        linearLayoutResults[0] = (LinearLayout) findViewById(R.id.linearLayoutRecipe1);
        linearLayoutResults[1] = (LinearLayout) findViewById(R.id.linearLayoutRecipe2);
        linearLayoutResults[2] = (LinearLayout) findViewById(R.id.linearLayoutRecipe3);
        linearLayoutResults[3] = (LinearLayout) findViewById(R.id.linearLayoutRecipe4);
        linearLayoutResults[4] = (LinearLayout) findViewById(R.id.linearLayoutRecipe5);

        linearLayoutNavigationButtons = (LinearLayout) findViewById(R.id.linearLayoutNavigationButtons);

        buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        String ingredientName = getIntent().getExtras().getString(EXTRA_INGREDIENT_NAME);
        String maxCalories = getIntent().getExtras().getString(EXTRA_MAX_CALORIES);
        String maxPrepTime = getIntent().getExtras().getString(EXTRA_MAX_PREP_TIME);


        spinnerSortingOptions = (Spinner) findViewById(R.id.spinnerSortingOptions);
        spinnerSortingOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if ((ingredientName != null) && (maxCalories != null) && (maxPrepTime != null)) {
                    offset = 0;

                    // cancelling all requests about this search if in queue
                    queue.cancelAll(TAG_SEARCH_QUERY);

                    String sortOption = parent.getItemAtPosition(pos).toString();

                    // first StringRequest: getting items searched
                    StringRequest stringRequest = searchNameStringRequest(ingredientName, maxCalories, maxPrepTime, sortOption);
                    stringRequest.setTag(TAG_SEARCH_QUERY);

                    // executing the request (adding to queue)
                    queue.add(stringRequest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if ((ingredientName != null) && (maxCalories != null) && (maxPrepTime != null)) {
            //Snackbar.make(textView10, "RESULT:" + ingredientName + " " + maxCalories + " " + maxPrepTime, Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();

            // cancelling all requests about this search if in queue
            queue.cancelAll(TAG_SEARCH_QUERY);

            // first StringRequest: getting items searched
            StringRequest stringRequest = searchNameStringRequest(ingredientName, maxCalories, maxPrepTime, getString(R.string.none));
            stringRequest.setTag(TAG_SEARCH_QUERY);

            // executing the request (adding to queue)
            queue.add(stringRequest);



            buttonPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    offset -= 5;

                    // cancelling all requests about this search if in queue
                    queue2.cancelAll(TAG_SEARCH_QUERY_PREVIOUS);

                    // first StringRequest: getting items searched
                    StringRequest stringRequest = searchNameStringRequest(ingredientName, maxCalories, maxPrepTime, getString(R.string.none));
                    stringRequest.setTag(TAG_SEARCH_QUERY_PREVIOUS);

                    // executing the request (adding to queue)
                    queue2.add(stringRequest);
                }
            });

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    offset += 5;

                    // cancelling all requests about this search if in queue
                    queue3.cancelAll(TAG_SEARCH_QUERY_NEXT);

                    // first StringRequest: getting items searched
                    StringRequest stringRequest = searchNameStringRequest(ingredientName, maxCalories, maxPrepTime, getString(R.string.none));
                    stringRequest.setTag(TAG_SEARCH_QUERY_NEXT);

                    // executing the request (adding to queue)
                    queue3.add(stringRequest);
                }
            });

        } else {
            Snackbar.make(textViewResults[0], "Error occurred", Snackbar.LENGTH_LONG)
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

    private StringRequest searchNameStringRequest(String ingredientName, String maxPreparationTime, String maxCalories, String sortOption) {
        final String API_KEY = API.API_KEY;
        final String INGREDIENT_NAME_SEARCH = "&includeIngredients=";
        final String MAX_CALORIES = "&maxCalories=";
        final String MAX_READY_TIME = "&maxReadyTime=";
        final String MAX_ROWS = "&number=";
        final String SORT_TYPE = "&sort=";
        final String MAX_LIMIT = "5";
        final String BEGINNING_ROW = "&offset=";
        final String URL_PREFIX = "https://api.spoonacular.com/recipes/complexSearch?";

        String url = URL_PREFIX + API_KEY;

        if ((ingredientName != null) && (!ingredientName.equals("")))
            url += INGREDIENT_NAME_SEARCH + ingredientName;

        if ((maxPreparationTime != null) && (!maxPreparationTime.equals("")))
            url += MAX_READY_TIME + maxPreparationTime;

        if ((maxCalories != null) && (!maxCalories.equals("")))
            url += MAX_CALORIES + maxCalories;

        if ((sortOption != null) && ((!sortOption.equals("")) || (!sortOption.equals(getString(R.string.none)))))
            url += SORT_TYPE + sortOption;

        url += MAX_ROWS + MAX_LIMIT + BEGINNING_ROW + Integer.toString(offset);
        //https://api.spoonacular.com/recipes/complexSearch?&apiKey=0ed87321d6664d1d89368136a483e058&includeIngredients=egg&number=5&offset=0
        //https://api.spoonacular.com/recipes/complexSearch?&apiKey=0ed87321d6664d1d89368136a483e058&includeIngredients=egg&maxReadyTime=45&&maxCalories=100&number=5&offset=0
        //https://api.spoonacular.com/recipes/complexSearch?&apiKey=0ed87321d6664d1d89368136a483e058&includeIngredients=egg&number=5&offset=0&addRecipeInformation=true

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

                            int number = jsonObj.getInt("number");
                            int totalResults = jsonObj.getInt("totalResults");

                            if (totalResults == 0){
                                textViewResultsCount.setText("No results found");
                                linearLayoutNavigationButtons.setVisibility(View.GONE);
                            } else {
                                if (offset == 0){
                                    buttonPrevious.setEnabled(false);
                                } else {
                                    buttonPrevious.setEnabled(true);
                                }
                                if (Math.ceil((offset + 5) / 5.0) == Math.ceil(totalResults / 5.0)){
                                    buttonNext.setEnabled(false);
                                } else {
                                    buttonNext.setEnabled(true);
                                }
                                String resultsString = "Page " + Integer.toString((int) Math.ceil((offset + 5) / 5.0)) + " of " + Integer.toString((int) Math.ceil(totalResults / 5.0)) + " (" + Integer.toString(totalResults) + " results)";
                                textViewResultsCount.setText(resultsString);
                                linearLayoutNavigationButtons.setVisibility(View.VISIBLE);
                            }

                            JSONArray resultsArray = jsonObj.getJSONArray("results");

                            for (int i = 0; i < resultsArray.length(); ++i){
                                JSONObject result = resultsArray.getJSONObject(i);
                                String title = result.getString("title");
                                String imageURL = result.getString("image");
                                int recipeID = result.getInt("id");
                                String imageType = result.getString("imageType");

                                recipeData[i] = new RecipeData(recipeID,title,imageURL,imageType);
                                textViewResults[i].setText(recipeData[i].getTitle());

                                Picasso.get().load(recipeData[i].getImageURL()).into(imageViewResults[i]);
                                imageViewResults[i].setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view){
                                        ImageView iv = (ImageView) view;

                                        String recipeID = "";

                                        if (iv == imageViewResults[0]){
                                            recipeID = Integer.toString(recipeData[0].getID());
                                        } else if (iv == imageViewResults[1]){
                                            recipeID = Integer.toString(recipeData[1].getID());
                                        } else if (iv == imageViewResults[2]){
                                            recipeID = Integer.toString(recipeData[2].getID());
                                        } else if (iv == imageViewResults[3]){
                                            recipeID = Integer.toString(recipeData[3].getID());
                                        } else if (iv == imageViewResults[4]){
                                            recipeID = Integer.toString(recipeData[4].getID());
                                        }

                                        if (!recipeID.equals("")){
                                            Intent intent;
                                            intent = new Intent(getApplicationContext(), ViewRecipeActivity.class);
                                            intent.putExtra(EXTRA_RECIPE_ID, recipeID);
                                            startActivity(intent);
                                        }
                                    }
                                });

                                linearLayoutResults[i].setVisibility(View.VISIBLE);
                            }

                            for (int i = resultsArray.length(); i < 5; ++i){
                                linearLayoutResults[i].setVisibility(View.GONE);
                            }
                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(SearchResultsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(SearchResultsActivity.this, "Recipe source is not responding (Spoonacular API)", Toast.LENGTH_LONG).show();
                    }
                });
    }





}
