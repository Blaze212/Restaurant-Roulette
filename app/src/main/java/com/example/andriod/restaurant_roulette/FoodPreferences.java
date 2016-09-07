package com.example.andriod.restaurant_roulette;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

/**
 * Created by Barton on 7/28/2016.
 */
public class FoodPreferences extends AppCompatActivity{

    String LOG_TAG = this.getClass().toString();
    //static CheckBox chkFastFood;
    boolean[] checked = new boolean[15];
    CheckBox[] checkBoxes = new CheckBox[15];
    //static CheckBox chkFancyRest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.food_prefs);
        if (savedInstanceState!= null) {
            //get your values to restore...
            LoadPreferences();
            setCheckBoxState();
        }
        else {
            super.onCreate(savedInstanceState);
            populateCheckboxes();

        }

        //GridView gridview = (GridView) findViewById(R.id.grid_layout);

       // gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       //     public void onItemClick(AdapterView<?> parent, View v,
       //                             int position, long id) {
       //         Toast.makeText(FoodPreferences.this, "" + position,
       //                 Toast.LENGTH_SHORT).show();
       //     }
       // });

        }

    public void setCheckBoxState(){
        for(int i=0;i<checked.length;i++){
                checkBoxes[i].setChecked(checked[i]);
        }
    }
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBooleanArray("checked",checked);

    }
    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_settings, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            //startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        if (id == R.id.food_categories) {
            startActivity(new Intent(this,FoodPreferences.class));
            return true;
        }

        if( id == android.R.id.home){
            SavePreferences();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        SavePreferences();
        super.onBackPressed();

    }
    private void SavePreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(int i=0;i<checkBoxes.length;i++) {
            if (checkBoxes[i] != null) {
                if (checkBoxes[i].isChecked()) {
                    checked[i] = true;
                    editor.putBoolean(Integer.toString(i), checked[i]);
                } else {
                    checked[i] = false;
                    editor.putBoolean(Integer.toString(i), checked[i]);
                }
            }
        }
        Log.d(LOG_TAG,"CheckedBoxes Indexes: " + editor);
        editor.commit();
    }
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        for(int i=0;i<checkBoxes.length;i++){
            checkBoxes[i].setChecked(sharedPreferences.getBoolean(Integer.toString(i), false));
        }

    }


    public void populateCheckboxes(){
        CheckBox chkFastFood = (CheckBox)findViewById(R.id.chk_fastfood);
        CheckBox chkFancyRest= (CheckBox)findViewById(R.id.chk_fancy);
        CheckBox chkAmerican= (CheckBox)findViewById(R.id.chk_american);
        CheckBox chkChinese= (CheckBox)findViewById(R.id.chk_chinese);
        CheckBox chkFrench= (CheckBox)findViewById(R.id.chk_french);
        CheckBox chkCzech= (CheckBox)findViewById(R.id.chk_czech);
        CheckBox chkGerman= (CheckBox)findViewById(R.id.chk_german);
        Boolean a = chkFastFood.isChecked();
        checkBoxes[0] = chkFancyRest;
        checkBoxes[1] = chkFastFood;
        checkBoxes[2] = chkAmerican;
        checkBoxes[3] = chkChinese;
        checkBoxes[4] = chkFrench;
        checkBoxes[5] = chkCzech;
        checkBoxes[6] = chkGerman;
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    public String getCheckedSearchTerms(){
        String searchTerm = "";
        String notChecked = "";
        CheckBox[] checkBoxes = new CheckBox[7];



        for(int i = 0; i<checkBoxes.length;i++){
            if(checkBoxes[i].isChecked()){
                searchTerm += checkBoxes[i].getText();
                checked[i] = true;
            }
            if(!checkBoxes[i].isChecked()){
                notChecked += " -" + checkBoxes[i].getText();
                checked[i] = false;
            }
        }
        searchTerm += notChecked;
        Log.d(LOG_TAG, "SearchTerm" + searchTerm);
        Log.d(LOG_TAG, "Checked? : " + checked.toString());
        return searchTerm;
    }
}
