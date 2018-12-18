package ojekkeren.ojekkeren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;



    public class StarRatingDriver extends ActionBarActivity {

    public RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_rating);

        // Initialize RatingBar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

    }

    /**
     * Display rating by calling getRating() method.
     * @param view
     */
    public void rateMe(View view){

        //Toast.makeText(getApplicationContext(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(view.getContext(), MainActivity.class);
        startActivity(myIntent);
    }
}
