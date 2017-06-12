package com.ebavery.leftovers;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddLeftover extends AppCompatActivity {

    String desc;
    Calendar dateMade = Calendar.getInstance();
    Boolean alert;
    int alertTime;
    Spinner spinIng1, spinIng2, spinIng3;
    ArrayAdapter<CharSequence> adapter;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leftover);
        dbHandler = new DBHandler(this);

        //set ingredient spinners based on user's diet preference
        spinIng1 = (Spinner)findViewById(R.id.spinIng1);
        spinIng2 = (Spinner)findViewById(R.id.spinIng2);
        spinIng3 = (Spinner)findViewById(R.id.spinIng3);
        final SharedPreferences initialization = PreferenceManager.getDefaultSharedPreferences(this);
        int dietSelection = initialization.getInt("diet", 0);
        switch (dietSelection){
            case 0:
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.Omnivore, android.R.layout.simple_spinner_item); break;
            case 1:
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.Vegetarian, android.R.layout.simple_spinner_item); break;
            case 2:
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.Vegan, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinIng1.setAdapter(adapter);
        spinIng2.setAdapter(adapter);
        spinIng3.setAdapter(adapter);

        //set alert Spinner
        final Spinner spinAlert = (Spinner)findViewById(R.id.spinAlert);
        ArrayAdapter<CharSequence> adapterAlert = ArrayAdapter.createFromResource(this,
                R.array.alertOptions, android.R.layout.simple_spinner_item);
        adapterAlert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAlert.setAdapter(adapterAlert);

        final EditText etDescription = (EditText)findViewById(R.id.etDesc);
        Button btnDatePicker = (Button)findViewById(R.id.btnPickDate);

        Button btnAdd = (Button)findViewById(R.id.btnAdd);

        Button btnCancel = (Button)findViewById(R.id.btnCancel);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddLeftover.this, d, dateMade.get(Calendar.YEAR),
                        dateMade.get(Calendar.MONTH), dateMade.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve values
                desc = etDescription.getText().toString();
                String ing1 = spinIng1.getSelectedItem().toString();
                String ing2 = spinIng2.getSelectedItem().toString();
                String ing3 = spinIng3.getSelectedItem().toString();

                //ensure required selections have been made
                if (desc.isEmpty()){
                    Toast.makeText(AddLeftover.this, "Name required.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (ing1.contains("Ingredients:") && ing2.contains("Ingredients:") && ing3.contains("Ingredients")){
                    Toast.makeText(AddLeftover.this, "Must choose at least one ingredient.", Toast.LENGTH_LONG).show();
                    return;
                }
                //make sure name is not a duplicate
                Cursor data = dbHandler.getItemId(desc);
                int itemID = -1;
                while (data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if (itemID > -1){
                    Toast.makeText(AddLeftover.this, "Name already exists. Choose a new name.", Toast.LENGTH_LONG).show();
                    return;
                }
                //calculate expiration date
                DoggyBag newLeftover = new DoggyBag(dateMade);

                if (!ing1.contains("Ingredients:")){
                    newLeftover.setIngredients(ing1);
                }
                if (!ing2.contains("Ingredients:")){
                    newLeftover.setIngredients(ing2);
                }
                if (!ing3.contains("Ingredients:")){
                    newLeftover.setIngredients(ing3);
                }
                //see whether leftover has already expired
                Calendar expDate = newLeftover.getExpDate();
                Calendar now = Calendar.getInstance();
                boolean alreadyExpired = expDate.before(now);
                if (alreadyExpired){
                    Toast.makeText(AddLeftover.this, "Leftover already expired. Do not save!", Toast.LENGTH_LONG).show();
                    return;
                }
                //if date is good, add everything to SQL Database
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                String exp = dateFormat.format(expDate.getTime());
                boolean addResult = dbHandler.addLeftover(desc, exp);
                if (addResult){
                    startActivity(new Intent(AddLeftover.this, MainActivity.class));
                }
                else{
                    Toast.makeText(AddLeftover.this,"Something went wrong", Toast.LENGTH_LONG).show();
                }
                //set alert
                int alertSelection = spinAlert.getSelectedItemPosition();
                switch (alertSelection){
                    case 0:
                        alert = false; break;
                    case 1:
                        alert = true; alertTime = 1; break;
                    case 2:
                        alert =true; alertTime = 2; break;
                }
                if (alert) {
                    long futureInMillis = expDate.getTimeInMillis() - (alertTime * 86400000);
                    String expNotice = desc + " will expire in " + alertTime + " days ";
                    scheduleNotification(getNotification(expNotice), futureInMillis);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddLeftover.this, MainActivity.class));
            }
        });
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateMade.set(Calendar.YEAR, year);
            dateMade.set(Calendar.MONTH, month);
            dateMade.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    //notification setting modified from https://gist.github.com/BrandonSmith/6679223
    private void scheduleNotification(Notification notification, long futureInMillis) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
    }

}

