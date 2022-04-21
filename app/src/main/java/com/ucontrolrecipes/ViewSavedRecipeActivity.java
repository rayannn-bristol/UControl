package com.ucontrolrecipes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.navigation.ui.AppBarConfiguration;

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

public class ViewSavedRecipeActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static final String TAG_VIEW_RECIPE = "TAG_VIEW_RECIPE";
    private TextView textViewRecipeName2;
    private TextView textViewRecipeIngredients2;
    private TextView textViewPrepMethod2;
    private TextView textViewPrepTime2;
    private TextView textViewServingSize2;
    private ImageView imageViewRecipe2;
    private Button buttonShareRecipe2;
    private AppBarConfiguration mAppBarConfiguration;
    private RequestQueue queue; // creating queue object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initializing the queue object
        queue = Volley.newRequestQueue(this);

        setContentView(R.layout.fragment_view_saved_recipe);

        textViewRecipeName2 = (TextView) findViewById(R.id.textViewRecipeName2);
        textViewRecipeIngredients2 = (TextView) findViewById(R.id.textViewRecipeIngredients2);
        textViewPrepMethod2 = (TextView) findViewById(R.id.textViewPrepMethod2);
        textViewPrepTime2 = (TextView) findViewById(R.id.textViewPrepTime2);
        textViewServingSize2 = (TextView) findViewById(R.id.textViewServingSize2);


        imageViewRecipe2 = (ImageView) findViewById(R.id.imageViewRecipe2);

        buttonShareRecipe2 = findViewById(R.id.buttonShareRecipe2);
        buttonShareRecipe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutRecipeDetails2);
                shareRecipe((View) linearLayout);
            }
        });

        Object obj = getIntent().getSerializableExtra(EXTRA_RECIPE);
        Recipe recipe = null;

        if (obj != null)
            recipe = (Recipe) obj;

        if (recipe != null) {
            String recipeID = Integer.toString(recipe.getId());

            // cancelling all requests about this search if in queue
            queue.cancelAll(TAG_VIEW_RECIPE);

            // first StringRequest: getting items searched
            StringRequest stringRequest = searchRecipeStringRequest(recipeID);
            stringRequest.setTag(TAG_VIEW_RECIPE);

            // executing the request (adding to queue)
            queue.add(stringRequest);
        } else {
            Snackbar.make(textViewRecipeName2, "Error occurred", Snackbar.LENGTH_LONG)
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
                            textViewRecipeName2.setText(title);

                            //Ingredients goes here
                            String ingredientsList = "";
                            JSONArray extendedIngredients = jsonObj.getJSONArray("extendedIngredients");
                            for (int i = 0; i < extendedIngredients.length(); ++i) {
                                JSONObject ingredient = extendedIngredients.getJSONObject(i);
                                String ingredientName = ingredient.getString("original");
                                ingredientsList += ingredientName + "\n";
                            }

                            textViewRecipeIngredients2.setText(ingredientsList);

                            //Prep Method
                            String instructions = jsonObj.getString("instructions");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                textViewPrepMethod2.setText(Html.fromHtml(instructions, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                textViewPrepMethod2.setText(Html.fromHtml(instructions));
                            }

                            String imageURL = jsonObj.getString("image");
                            Picasso.get().load(imageURL).into(imageViewRecipe2);
                            imageViewRecipe2.setVisibility(View.VISIBLE);

                            //Prep Time
                            int readyInMinutes = jsonObj.getInt("readyInMinutes");
                            String prepTime = "Preparation Time: " + Integer.toString(readyInMinutes) + " minutes";
                            textViewPrepTime2.setText(prepTime);

                            //Calories count
                            int servings = jsonObj.getInt("servings");
                            String servingSize = "Serving Size: " + Integer.toString(servings);
                            textViewServingSize2.setText(servingSize);

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(ViewSavedRecipeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(ViewSavedRecipeActivity.this, "Recipe source is not responding (Spoonacular API)", Toast.LENGTH_LONG).show();
                    }
                });
    }


}
