package com.ebavery.leftovers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Initialize extends AppCompatActivity {

    String diet, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        final SharedPreferences initialization = PreferenceManager.getDefaultSharedPreferences(this);
        final EditText nameEntry = (EditText)findViewById(R.id.eTname);
        final Spinner dietEntry = (Spinner)findViewById(R.id.spinDiet);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietEntry.setAdapter(adapter);
        Button btnStart = (Button)findViewById(R.id.btnInitialize);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEntry.getText().toString();
                int dietSelection = dietEntry.getSelectedItemPosition();
                SharedPreferences.Editor editor = initialization.edit();
                Calendar now = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                String date = dateFormat.format(now.getTime());
                editor.putString("date", date);
                editor.putInt("diet", dietSelection);
                editor.putString("name", name);
                editor.putInt("eaten", 0);
                editor.putInt("wasted", 0);
                editor.commit();
                startActivity(new Intent(Initialize.this, MainActivity.class));
            }
        });
    }
}
