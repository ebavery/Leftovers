package com.ebavery.leftovers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<FridgeItem> leftoversList = new ArrayList<>();
    DBHandler db;
    private GridView grid;
    private TextView instructions, today;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
    Calendar now = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final SharedPreferences initialization = PreferenceManager.getDefaultSharedPreferences(this);
        if (!initialization.contains("diet")){
            startActivity(new Intent(MainActivity.this, Initialize.class));
        }
        String username = initialization.getString("name", null);
        String title = username + "'s Fridge";
        setContentView(R.layout.activity_main);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

        } else {

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddLeftover.class));
            }
        });
        //visible only if nothing in grid
        instructions = (TextView)findViewById(R.id.txtInstructions);

        //set today's date in TextView
        today = (TextView)findViewById(R.id.txtDate);
        String nowTxt = "Today's date: " + dateFormat.format(now.getTime());
        today.setText(nowTxt);

        //populate grid view
        grid = (GridView)findViewById(R.id.gridViewLeftovers);
        db = new DBHandler(this);
        populateGridView();
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FridgeItem item = leftoversList.get(position);
                Intent deleteItem = new Intent(MainActivity.this, DeleteLeftover.class);
                deleteItem.putExtra("name", item.getName());
                deleteItem.putExtra("date", item.getExpDate());
                startActivity(deleteItem);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateGridView(){
        int imageResource;
        Cursor data = db.getData();
        while (data.moveToNext()) {
            String itemName = data.getString(1);
            String itemDate = data.getString(2);
            //String item;
            Calendar expDate = Calendar.getInstance();
            try {
                Date date = dateFormat.parse(itemDate);
                expDate.setTime(date);
            } catch (Exception e){
                itemDate = "unknown";
            }
            if (expDate.before(now)){
                itemDate = "EXPIRED";
                imageResource = R.drawable.red;
            }
            else {
                itemDate = "Exp: \n" + itemDate;
                long difference = expDate.getTimeInMillis() - now.getTimeInMillis();
                if (difference < 4000000){
                    itemDate = "EXPIRED";
                    imageResource = R.drawable.red;
                }
                else if (difference >= 4000000 && difference < 90000000){
                    imageResource = R.drawable.orange;
                }

                else if (difference >= 90000000 && difference < 176400000){
                    imageResource = R.drawable.yellow;
                }
                else {
                    imageResource = R.drawable.white;
                }
            }
            leftoversList.add(new FridgeItem(itemName, itemDate, imageResource));
        }

            LeftoversAdapter adapter = new LeftoversAdapter(this, leftoversList);
            grid.setAdapter(adapter);
            if (leftoversList.isEmpty()) {
                instructions.bringToFront();
            }
            else{
                instructions.setVisibility(View.INVISIBLE);

            }
    }
}

