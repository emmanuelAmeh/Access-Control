package com.example.android.accesscontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.android.accesscontrol.App.CHANNEL_1_ID;
import static com.example.android.accesscontrol.App.CHANNEL_2_ID;

public class VisitorControlMain extends AppCompatActivity {

    private static final int EDIT_VISITOR_CONTROL_REQUESTCODE = 3;
    private VisitorViewModel mVisitorViewModel;

    private NotificationManagerCompat notifMgrCompact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_control_main);
        setTitle("Visitor Control");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //notification things
        notifMgrCompact = NotificationManagerCompat.from(this);


        RecyclerView recyclerView = findViewById(R.id.recycler_view_control);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        final VisitorControlAdapter adapter = new VisitorControlAdapter();
        recyclerView.setAdapter(adapter);

        mVisitorViewModel = new ViewModelProvider(VisitorControlMain.this).get(VisitorViewModel.class);

        mVisitorViewModel.getAllVisitors().observe(this, new Observer<List<Visitor>>() {
            @Override
            public void onChanged(List<Visitor> visitors) {
                adapter.setVisitors(visitors);
            }
        });


        adapter.setOnItemClickListener(new VisitorControlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Visitor visitor) {
                Intent intent = new Intent(VisitorControlMain.this, VisitorController.class);
                intent.putExtra(VisitorController.EXTRA_ID, visitor.getId());
                intent.putExtra(VisitorController.EXTRA_FIRSTNAME, visitor.getFirstname());
                intent.putExtra(VisitorController.EXTRA_SURNAME, visitor.getSurname());
                intent.putExtra(VisitorController.EXTRA_ACCESS_CODE, visitor.getAccessCode());
                intent.putExtra(VisitorController.EXTRA_TIME_GENERATED, visitor.getTimeGenerated());
                intent.putExtra(VisitorController.EXTRA_TIME_LOGGED_IN, visitor.getTimeLoggedIn());
                intent.putExtra(VisitorController.EXTRA_TIME_LOGGED_OUT, visitor.getTimeLoggedOut());

                startActivityForResult(intent, EDIT_VISITOR_CONTROL_REQUESTCODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_VISITOR_CONTROL_REQUESTCODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(VisitorController.EXTRA_ID, -1);
            String firstname = data.getStringExtra(VisitorController.EXTRA_FIRSTNAME);
            String surname = data.getStringExtra(VisitorController.EXTRA_SURNAME);
            int accessCode = data.getIntExtra(VisitorController.EXTRA_ACCESS_CODE, 0);
            long timeGenerated = data.getLongExtra(VisitorController.EXTRA_TIME_GENERATED, 12L);
            long timeLoggedIn = data.getLongExtra(VisitorController.EXTRA_TIME_LOGGED_IN, 12L);
            Log.e("control log in time", String.valueOf(timeLoggedIn));
            long timeLoggedOut = data.getLongExtra(VisitorController.EXTRA_TIME_LOGGED_OUT, 12L);

            Visitor visitor = new Visitor(surname, firstname, accessCode, timeGenerated, timeLoggedIn, timeLoggedOut);

            visitor.setId(id);

            mVisitorViewModel.update(visitor);

            Toast.makeText(this, "Visitor Updated", Toast.LENGTH_LONG).show();


            if (timeLoggedIn > timeLoggedOut) {
                //Notification
                //to open mainactivity when delivered notification is clicked, use pending intent
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                //to do sth, react to actionbutton
                Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
                broadcastIntent.putExtra(NotificationReceiver.TOAST_MESSAGE, firstname + " " + surname + " logged in and will arrive soon.");
                PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_visibility)
                        .setContentTitle("Visitor Logged In")
                        .setContentText(firstname + " " + surname + " Logged In")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        //to respond to notification click
                        .setContentIntent(pendingIntent)
                        //to respond to action button click. Dont forget to add receiver in the manifest
                        .addAction(R.drawable.ic_visibility, "Send Toast", actionIntent)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .build();

                notifMgrCompact.notify(1, notification);
            } else {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                        .setSmallIcon(R.drawable.ic_visibility)
                        .setContentTitle("Visitor Logged Out")
                        .setContentText(firstname + " " + surname + " Logged Out")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .build();

                notifMgrCompact.notify(2, notification);
            }


        }

    }
}
