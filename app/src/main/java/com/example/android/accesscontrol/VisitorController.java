package com.example.android.accesscontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class VisitorController extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.android.accesscontrol.RXTRA_ID";
    public static final String EXTRA_FIRSTNAME = "com.example.android.accesscontrol.RXTRA_FIRSTNAME";
    public static final String EXTRA_SURNAME = "com.example.android.accesscontrol.RXTRA_SURNAME";
    public static final String EXTRA_ACCESS_CODE = "com.example.android.accesscontrol.RXTRA_ACCESS_CODE";
    public static final String EXTRA_TIME_GENERATED = "com.example.android.accesscontrol.EXTRA_TIME_GENERATED";
    public static final String EXTRA_TIME_LOGGED_IN = "com.example.android.accesscontrol.EXTRA_TIME_LOGGED_IN";
    public static final String EXTRA_TIME_LOGGED_OUT = "com.example.android.accesscontrol.EXTRA_TIME_LOGGED_OUT";

    private TextView mSurname;
    private TextView mFirstname;
    private TextView mTvAccessCode;
    private Button btnLogIn;
    private Button btnLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_controller);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Visitor Control");

        //linking views
        mSurname = findViewById(R.id.surname);
        mFirstname = findViewById(R.id.firstname);
        mTvAccessCode = findViewById(R.id.access_code);
        btnLogIn = findViewById(R.id.btn_log_in);
        btnLogOut = findViewById(R.id.btn_log_out);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logVisitorIn();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logVisitorOut();
            }
        });

        //Edit Visitor function
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            mFirstname.setText("Firstname:   " + intent.getStringExtra(EXTRA_FIRSTNAME));
            mSurname.setText("Surname:     " + intent.getStringExtra(EXTRA_SURNAME));
            mTvAccessCode.setText(String.valueOf(intent.getIntExtra(EXTRA_ACCESS_CODE, 0)));
            long timeLoggedIn = intent.getLongExtra(EXTRA_TIME_LOGGED_IN, 13L);
            long timeLoggedOut = intent.getLongExtra(EXTRA_TIME_LOGGED_OUT, 13L);

            if (timeLoggedIn > 100L) {
                btnLogIn.setEnabled(false);
            } else {
                btnLogOut.setEnabled(false);
            }

            if (timeLoggedOut > 100L) {
                btnLogOut.setEnabled(false);
            }

        }
    }

    private void logVisitorOut() {
        //getting intent contents
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        String firstname = getIntent().getStringExtra(EXTRA_FIRSTNAME);
        String surname = getIntent().getStringExtra(EXTRA_SURNAME);
        int accessCode = getIntent().getIntExtra(EXTRA_ACCESS_CODE, 0);

        long timeGenerated = getIntent().getLongExtra(EXTRA_TIME_GENERATED, 13L);
        long timeLoggedIn = getIntent().getLongExtra(EXTRA_TIME_LOGGED_IN, 13L);

        //for time logged in
        Calendar calendar = Calendar.getInstance();
        long timeLoggedOut = calendar.getTimeInMillis();

        Intent dataIntent = new Intent();

        dataIntent.putExtra(EXTRA_ID, id);
        dataIntent.putExtra(EXTRA_FIRSTNAME, firstname);
        dataIntent.putExtra(EXTRA_SURNAME, surname);
        dataIntent.putExtra(EXTRA_ACCESS_CODE, accessCode);
        dataIntent.putExtra(EXTRA_TIME_GENERATED, timeGenerated);
        dataIntent.putExtra(EXTRA_TIME_LOGGED_IN, timeLoggedIn);
        dataIntent.putExtra(EXTRA_TIME_LOGGED_OUT, timeLoggedOut);

        setResult(RESULT_OK, dataIntent);
        finish();

    }

    private void logVisitorIn() {

        //getting intent contents
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        String firstname = getIntent().getStringExtra(EXTRA_FIRSTNAME);
        String surname = getIntent().getStringExtra(EXTRA_SURNAME);
        int accessCode = getIntent().getIntExtra(EXTRA_ACCESS_CODE, 0);

        long timeGenerated = getIntent().getLongExtra(EXTRA_TIME_GENERATED, 13L);
        long timeLoggedOut = getIntent().getLongExtra(EXTRA_TIME_LOGGED_OUT, 13L);

        //for time logged in
        Calendar calendar = Calendar.getInstance();
        long timeLoggedIn = calendar.getTimeInMillis();
        Log.e("control log in time", String.valueOf(timeLoggedIn));

        Intent dataIntent = new Intent();

        dataIntent.putExtra(EXTRA_ID, id);
        dataIntent.putExtra(EXTRA_FIRSTNAME, firstname);
        dataIntent.putExtra(EXTRA_SURNAME, surname);
        dataIntent.putExtra(EXTRA_ACCESS_CODE, accessCode);
        dataIntent.putExtra(EXTRA_TIME_GENERATED, timeGenerated);
        dataIntent.putExtra(EXTRA_TIME_LOGGED_IN, timeLoggedIn);
        dataIntent.putExtra(EXTRA_TIME_LOGGED_OUT, timeLoggedOut);

        setResult(RESULT_OK, dataIntent);
        finish();
    }
}
