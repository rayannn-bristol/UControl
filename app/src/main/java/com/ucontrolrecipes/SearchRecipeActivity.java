package com.ucontrolrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.ucontrolrecipes.databinding.ActivitySearchBinding;

public class SearchRecipeActivity extends AppCompatActivity {

    public static final String EXTRA_INGREDIENT_NAME = "EXTRA_INGREDIENT_NAME";
    public static final String EXTRA_MAX_CALORIES = "EXTRA_MAX_CALORIES";
    public static final String EXTRA_MAX_PREP_TIME = "EXTRA_MAX_PREP_TIME";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivitySearchBinding binding;
    private EditText editTextIngredient;
    private EditText editTextPrepTime;
    private EditText editTextMaxCalories;
    private ImageButton imageButtonSearch_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(R.layout.fragment_search);

        editTextIngredient = (EditText) findViewById(R.id.editTextIngredient);
        String ingredientName = getIntent().getExtras().getString(EXTRA_INGREDIENT_NAME);
        editTextIngredient.setText(ingredientName);

        editTextPrepTime = (EditText) findViewById(R.id.editTextPrepTime);
        editTextMaxCalories = (EditText) findViewById(R.id.editTextMaxCalories);

        imageButtonSearch_button2 = (ImageButton) findViewById(R.id.search_button2);
        imageButtonSearch_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (editTextIngredient.getText().toString().equals("") && editTextMaxCalories.getText().toString().equals("") && editTextPrepTime.getText().toString().equals(""))
                    Snackbar.make(view, "Please fill out at least one field!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                    intent.putExtra(EXTRA_INGREDIENT_NAME, editTextIngredient.getText().toString());
                    intent.putExtra(EXTRA_MAX_CALORIES, editTextMaxCalories.getText().toString());
                    intent.putExtra(EXTRA_MAX_PREP_TIME, editTextPrepTime.getText().toString());
                    startActivity(intent);
                }
            }
        });


        if (ingredientName != null) {
            editTextIngredient.setText(ingredientName);
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
}
