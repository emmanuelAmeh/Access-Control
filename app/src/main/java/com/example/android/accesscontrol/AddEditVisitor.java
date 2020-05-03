package com.example.android.accesscontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.security.SecureRandom;
import java.util.Calendar;

public class AddEditVisitor extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.android.accesscontrol.RXTRA_ID";
    public static final String EXTRA_FIRSTNAME = "com.example.android.accesscontrol.RXTRA_FIRSTNAME";
    public static final String EXTRA_SURNAME = "com.example.android.accesscontrol.RXTRA_SURNAME";
    public static final String EXTRA_ACCESS_CODE = "com.example.android.accesscontrol.RXTRA_ACCESS_CODE";
    public static final String EXTRA_TIME_GENERATED = "com.example.android.accesscontrol.EXTRA_TIME_GENERATED";
    public static final String EXTRA_TIME_LOGGED_IN = "com.example.android.accesscontrol.EXTRA_TIME_LOGGED_IN";
    public static final String EXTRA_TIME_LOGGED_OUT = "com.example.android.accesscontrol.EXTRA_TIME_LOGGED_OUT";
    public static final String EXTRA_DELETE_FLAG = "com.example.android.accesscontrol.EXTRA_DELETE_FLAG";

    private EditText mSurname;
    private EditText mFirstname;
    private TextView mTvAccessCode;
    private Button btnGenerateCode;
    private Button btnLogOut;
    private Button btnDelete;
    private Button btnSubmit;

    private boolean codeGenerated = false;
    private boolean textfieldsFilled = false;
    private boolean deleteFlag = false;

    private long timeGenerated;
    private long timeLoggedIn;
    private long timeLoggedOut;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Edit Visitor function
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setContentView(R.layout.activity_edit_visitor);
            setTitle("Edit Visitor");
            //linking views

            mSurname = findViewById(R.id.surname);
            mFirstname = findViewById(R.id.firstname);
            mTvAccessCode = findViewById(R.id.access_code);
            btnLogOut = findViewById(R.id.btn_log_out);
            btnDelete = findViewById(R.id.btn_delete);
            btnSubmit = findViewById(R.id.btn_submit);

            mFirstname.setText(intent.getStringExtra(EXTRA_FIRSTNAME));
            mSurname.setText(intent.getStringExtra(EXTRA_SURNAME));
            int access = intent.getIntExtra(EXTRA_ACCESS_CODE, -1);
            mTvAccessCode.setText(String.valueOf(access));

            //buttons control
            long timeLoggedIn = intent.getLongExtra(EXTRA_TIME_LOGGED_IN, 10L);
            if (timeLoggedIn > 100L) {
                btnDelete.setEnabled(false);
                btnSubmit.setEnabled(false);
            }


            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteVisitor();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editVisitor();
                }
            });

        } else {
            setContentView(R.layout.activity_add_visitor);
            setTitle("Add Visitor");
            //linking views
            mSurname = findViewById(R.id.surname);
            mFirstname = findViewById(R.id.firstname);
            mTvAccessCode = findViewById(R.id.access_code);
            btnSubmit = findViewById(R.id.btn_submit);
            btnGenerateCode = findViewById(R.id.btn_access_code);

            btnGenerateCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generatePass();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveVisitor();
                }
            });
        }
    }

    private void editVisitor() {
        String firstname = mFirstname.getText().toString();
        String surname = mSurname.getText().toString();
        String accessCode = mTvAccessCode.getText().toString();

        if (firstname.trim().isEmpty() || surname.trim().isEmpty()) {
            Snackbar.make(btnDelete, "Please fill all fields and generate Access Code", Snackbar.LENGTH_LONG).show();
            return;
        }
        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_ID, -1);
        long timeGenerated = intent.getLongExtra(EXTRA_TIME_GENERATED, 10L);
        long timeLoggedIn = intent.getLongExtra(EXTRA_TIME_LOGGED_IN, 10L);
        long timeLoggedOut = intent.getLongExtra(EXTRA_TIME_LOGGED_OUT, 10L);

        Intent dataIntent = new Intent();

        dataIntent.putExtra(EXTRA_ID, id);
        dataIntent.putExtra(EXTRA_FIRSTNAME, firstname);
        dataIntent.putExtra(EXTRA_SURNAME, surname);
        dataIntent.putExtra(EXTRA_ACCESS_CODE, accessCode);
        dataIntent.putExtra(EXTRA_TIME_GENERATED, timeGenerated);
        dataIntent.putExtra(EXTRA_TIME_LOGGED_IN, timeLoggedIn);
        dataIntent.putExtra(EXTRA_TIME_LOGGED_OUT, timeLoggedOut);

        if (deleteFlag) {
            dataIntent.putExtra(EXTRA_DELETE_FLAG, true);
        }

        setResult(RESULT_OK, dataIntent);
        finish();
    }

    private void deleteVisitor() {
        deleteFlag = true;
        editVisitor();
    }

    private void saveVisitor() {
        String firstname = mFirstname.getText().toString();
        String surname = mSurname.getText().toString();
        String accessCode = mTvAccessCode.getText().toString();

        //Time generated
        Calendar c = Calendar.getInstance();
        String timeGenerated = String.valueOf(c.getTimeInMillis());

        if (firstname.trim().isEmpty() || surname.trim().isEmpty()) {
            Snackbar.make(btnSubmit, "Please Fill the First and Surname", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!codeGenerated) {
            Snackbar.make(btnSubmit, "Please Generate Code", Snackbar.LENGTH_LONG).show();
            return;
        }

        Intent dataIntent = new Intent();
        dataIntent.putExtra(EXTRA_FIRSTNAME, firstname);
        dataIntent.putExtra(EXTRA_SURNAME, surname);
        dataIntent.putExtra(EXTRA_ACCESS_CODE, accessCode);
        dataIntent.putExtra(EXTRA_TIME_GENERATED, timeGenerated);

        setResult(RESULT_OK, dataIntent);
        finish();
    }

    private void generatePass() {

        SecureRandom secureRandom = new SecureRandom();
        // String passcode = String.valueOf(secureRandom.nextInt(10000));
        int mPasscode = secureRandom.nextInt(10000);

        while (mPasscode < 1000) {
            mPasscode = secureRandom.nextInt(10000);
        }
        String passcode = String.valueOf(mPasscode);

        mTvAccessCode.setText(passcode);
        mTvAccessCode.setTypeface(Typeface.DEFAULT_BOLD);
        codeGenerated = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.add_visitor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_visitor:
                saveVisitor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
