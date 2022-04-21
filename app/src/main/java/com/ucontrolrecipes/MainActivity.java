package com.ucontrolrecipes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.ucontrolrecipes.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String EXTRA_INGREDIENT_NAME = "EXTRA_INGREDIENT_NAME";
    public static final String TAG_RANDOM_RECIPES = "TAG_RANDOM_RECIPES";
    private AppBarConfiguration mAppBarConfiguration;
    private EditText editTextSearch;
    private ImageView[] imageViewRandomRecipes;
    private RequestQueue queue; // creating queue object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing the queue object
        queue = Volley.newRequestQueue(this);

        com.ucontrolrecipes.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                search(view);
                return true;
            }
            return false;
        });

        ImageView imageViewSearch_button = findViewById(R.id.search_button);
        imageViewSearch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view);
            }
        });


        imageViewRandomRecipes = new ImageView[8];
        imageViewRandomRecipes[0] = (ImageView) findViewById(R.id.imageViewRandomRecipe1);
        imageViewRandomRecipes[1] = (ImageView) findViewById(R.id.imageViewRandomRecipe2);
        imageViewRandomRecipes[2] = (ImageView) findViewById(R.id.imageViewRandomRecipe3);
        imageViewRandomRecipes[3] = (ImageView) findViewById(R.id.imageViewRandomRecipe4);
        imageViewRandomRecipes[4] = (ImageView) findViewById(R.id.imageViewRandomRecipe5);
        imageViewRandomRecipes[5] = (ImageView) findViewById(R.id.imageViewRandomRecipe6);
        imageViewRandomRecipes[6] = (ImageView) findViewById(R.id.imageViewRandomRecipe7);
        imageViewRandomRecipes[7] = (ImageView) findViewById(R.id.imageViewRandomRecipe8);

        // cancelling all requests about this search if in queue
        queue.cancelAll(TAG_RANDOM_RECIPES);

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchRandomRecipesStringRequest();
        stringRequest.setTag(TAG_RANDOM_RECIPES);

        // executing the request (adding to queue)
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_saved_recipes) {
            Intent intent = new Intent(this, SavedRecipesActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    */

    private void search(View view){
        Intent intent;
        intent = new Intent(getApplicationContext(), SearchRecipeActivity.class);
        intent.putExtra(EXTRA_INGREDIENT_NAME, editTextSearch.getText().toString());
        startActivity(intent);
        //Snackbar.make(view, editTextSearch.getText().toString(), Snackbar.LENGTH_LONG)
        //       .setAction("Action", null).show();
    }

    private StringRequest searchRandomRecipesStringRequest() {
        final String API_KEY = API.API_KEY;
        final String MAX_ROWS = "&number=";
        final String MAX_LIMIT = "8";
        final String URL_PREFIX = "https://api.spoonacular.com/recipes/random?";

        String url = URL_PREFIX + API_KEY + MAX_ROWS + MAX_LIMIT;
        //https://api.spoonacular.com/recipes/random?&apiKey=0ed87321d6664d1d89368136a483e058&number=8

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
                            JSONArray recipesArray = jsonObj.getJSONArray("recipes");

                            for (int i = 0; i < recipesArray.length(); ++i){
                                JSONObject recipe = recipesArray.getJSONObject(i);
                                int recipeID = recipe.getInt("id");
                                String imageURL = recipe.getString("image");
                                Picasso.get().load(imageURL).into(imageViewRandomRecipes[i]);
                                imageViewRandomRecipes[i].setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view){
                                        ImageView iv = (ImageView) view;

                                        if (!Integer.toString(recipeID).equals("")){
                                            Intent intent;
                                            intent = new Intent(getApplicationContext(), ViewRecipeActivity.class);
                                            intent.putExtra(EXTRA_RECIPE_ID, Integer.toString(recipeID));
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(MainActivity.this, "Recipe source is not responding (Spoonacular API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

}