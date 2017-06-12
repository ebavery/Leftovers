package com.ebavery.leftovers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteLeftover extends AppCompatActivity {

    Button btnEat, btnThrow, btnKeep;
    DBHandler db;
    String name, date;
    TextView item;
    String databaseDesc;
    int eaten;
    int wasted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_leftover);
        btnEat = (Button)findViewById(R.id.btnAte);
        btnThrow = (Button)findViewById(R.id.btnThrewAway);
        btnKeep = (Button)findViewById(R.id.btnKeep);
        item = (TextView)findViewById(R.id.txtLeftoverToDelete);
        db = new DBHandler(this);

        Intent receivedIntent = getIntent();
        name = receivedIntent.getStringExtra("name");
        date = receivedIntent.getStringExtra("date");
        String display = name + "\n" + date;

        item.setText(display);

        databaseDesc = name.trim();

        final SharedPreferences initialization = PreferenceManager.getDefaultSharedPreferences(this);
        eaten = initialization.getInt("eaten", 0);
        wasted = initialization.getInt("wasted", 0);

        btnEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = db.getItemId(databaseDesc);
                int itemID = -1;
                while (data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if (itemID > -1){
                    db.deleteLeftover(itemID);
                    eaten++;
                    SharedPreferences.Editor editor = initialization.edit();
                    editor.putInt("eaten", eaten);
                    editor.commit();
                    startActivity(new Intent(DeleteLeftover.this, MainActivity.class));
                }
                else{
                    Toast.makeText(DeleteLeftover.this, "No record associated with that leftover", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = db.getItemId(databaseDesc);
                int itemID = -1;
                while (data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if (itemID > -1) {
                    db.deleteLeftover(itemID);
                    wasted++;
                    SharedPreferences.Editor editor = initialization.edit();
                    editor.putInt("wasted", wasted);
                    editor.commit();
                    startActivity(new Intent(DeleteLeftover.this, MainActivity.class));
                }
                else{
                    Toast.makeText(DeleteLeftover.this, "No record associated with that leftover", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteLeftover.this, MainActivity.class));
            }
        });
    }
}
