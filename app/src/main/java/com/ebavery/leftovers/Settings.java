package com.ebavery.leftovers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Settings extends AppCompatActivity {

    private Button btnUpdate, btnReset, btnReturn;
    private EditText eTnameReview;
    private Spinner sPdietReview;
    private TextView eaten, wasted, since;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final SharedPreferences initialization = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = initialization.edit();
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnReset = (Button)findViewById(R.id.btnResetCount);
        btnReturn = (Button)findViewById(R.id.btnReturn);
        eTnameReview = (EditText)findViewById(R.id.eTnameReview);
        sPdietReview = (Spinner)findViewById(R.id.spinDietReview);
        eaten = (TextView)findViewById(R.id.txtNumberLeftoversEaten);
        wasted = (TextView)findViewById(R.id.txtNumberLeftoversWasted);
        since = (TextView)findViewById(R.id.txtSince);

        String name = initialization.getString("name", null);
        int dietSelection = initialization.getInt("diet", 0);
        String date = initialization.getString("date", null);
        int numEaten = initialization.getInt("eaten", 0);
        int numWasted = initialization.getInt("wasted", 0);

        eTnameReview.setText(name);
        since.setText("Since " + date + ": ");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPdietReview.setAdapter(adapter);
        sPdietReview.setSelection(dietSelection);
        String numEatenTxt = String.valueOf(numEaten);
        String numWastedTxt = String.valueOf(numWasted);
        eaten.setText(numEatenTxt);
        wasted.setText(numWastedTxt);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = eTnameReview.getText().toString();
                int updatedDiet = sPdietReview.getSelectedItemPosition();
                editor.putString("name", updatedName);
                editor.putInt("diet", updatedDiet);
                editor.commit();
                Toast.makeText(Settings.this, "Name and/or Diet Updated", Toast.LENGTH_LONG).show();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                String date = dateFormat.format(now.getTime());

                editor.putString("date", date);
                editor.putInt("eaten", 0);
                editor.putInt("wasted", 0);
                editor.commit();
                since.setText(date);
                eaten.setText("0");
                wasted.setText("0");
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });

    }
}
