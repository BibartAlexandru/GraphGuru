package com.example.graphguru;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity implements EditGraphFragment.EditFragmentInterface, MyGraphsFragment.my_graphs_interface {

    DatabaseHandler databaseHandler ;
    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    ActionBarDrawerToggle toggle ;
    Bundle informationFromIntent ;

    EditGraphFragment editGraphFragment ;
    MyGraphsFragment myGraphsFragment ;

    FragmentManager fragmentManager ;

    TextView navHeaderText ;

    MainMenu thisObject ;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisObject = this ;
        setContentView(R.layout.activity_main_menu);
        databaseHandler = new DatabaseHandler(MainMenu.this) ;
        fragmentManager = getSupportFragmentManager() ;
        informationFromIntent = getIntent().getExtras() ;

        //region pun in framelayotfragmentul default
        ArrayList<String> user_table_names = databaseHandler.FetchUserGraphs(informationFromIntent.getString("name"));
        myGraphsFragment = new MyGraphsFragment() ;
        Bundle fragm_arguments = new Bundle() ;
        fragm_arguments.putStringArrayList("graph_names",user_table_names);
        myGraphsFragment.set_interface(thisObject);
        myGraphsFragment.setArguments(fragm_arguments);
        fragmentManager.beginTransaction().replace(R.id.fragment_container,myGraphsFragment)
                .addToBackStack(null)
                .commit();
        //endregion



        drawerLayout = findViewById(R.id.drawer_layout) ;
        navigationView = findViewById(R.id.main_menu_drawer) ;
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav_drawer,R.string.close_nav_drawer) ;
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0) ;
        navHeaderText = headerView.findViewById(R.id.header_text) ;

        if(informationFromIntent != null)
        {
            navHeaderText.setText("Welcome, " + informationFromIntent.getString("name") + "!");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId() ;
                if(itemId == R.id.logout)
                {
                    Intent goToRegister = new Intent(MainMenu.this,MainActivity.class) ;
                    startActivity(goToRegister);

                }
                else if(itemId == R.id.create_graph)
                {
                    editGraphFragment = new EditGraphFragment() ;
                    editGraphFragment.setEditFragmentInterface(thisObject);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container,editGraphFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                else if(itemId == R.id.my_graphs)
                {
                    ArrayList<String> user_table_names = databaseHandler.FetchUserGraphs(informationFromIntent.getString("name"));
                    myGraphsFragment = new MyGraphsFragment() ;
                    Bundle fragm_arguments = new Bundle() ;
                    myGraphsFragment.set_interface(thisObject);
                    fragm_arguments.putStringArrayList("graph_names",user_table_names);
                    myGraphsFragment.setArguments(fragm_arguments);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container,myGraphsFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if(itemId == R.id.help)
                {
                    Intent go_to_help = new Intent(getApplicationContext(),HelpPage.class) ;
                    startActivity(go_to_help);
                }

                return false;
            }
        });


    }

    @Override
    public void on_graph_save_click(Graph graph) {
        Log.d("GRAPH_TO_SAVE",graph.toString()) ;
        if(databaseHandler.CreateGraph(graph,informationFromIntent.getString("name")) == true)
            Toast.makeText(editGraphFragment.getContext(),"GRAPH SAVED!",Toast.LENGTH_LONG).show();
        else {
            databaseHandler.ReplaceGraph(graph,informationFromIntent.getString("name")) ;
            Toast.makeText(editGraphFragment.getContext(), "GRAPH REPLACED!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void go_to_graph(String graph_name) {
        ArrayList<String> user_table_names = databaseHandler.FetchUserGraphs(informationFromIntent.getString("name"));
        editGraphFragment = new EditGraphFragment() ;
        editGraphFragment.setEditFragmentInterface(thisObject);
        Bundle fragm_arguments = new Bundle() ;
        fragm_arguments.putString("graph_name",graph_name);
        editGraphFragment.setArguments(fragm_arguments);
        fragmentManager.beginTransaction().replace(R.id.fragment_container,editGraphFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}