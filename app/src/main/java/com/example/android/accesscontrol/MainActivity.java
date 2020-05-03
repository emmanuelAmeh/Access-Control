package com.example.android.accesscontrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_VISITOR_REQUESTCODE = 1;
    private static final int EDIT_VISITOR_REQUESTCODE = 2;
    private VisitorViewModel visitorViewModel;
    private FloatingActionButton fabAdVisitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up visitor recyclerview
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        final VisitorAdapter visitorAdapter = new VisitorAdapter();
        recyclerView.setAdapter(visitorAdapter);

        visitorViewModel = new ViewModelProvider(this).get(VisitorViewModel.class);

        visitorViewModel.getAllVisitors().observe(this, new Observer<List<Visitor>>() {
            @Override
            public void onChanged(List<Visitor> visitors) {
                visitorAdapter.submitList(visitors);
            }
        });

        //handling swipes
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                visitorViewModel.delete(visitorAdapter.getVisitorAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Visitor Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //handling clicks
        visitorAdapter.setOnItemClickListener(new VisitorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Visitor visitor) {
                Intent intent = new Intent(MainActivity.this, AddEditVisitor.class);
                intent.putExtra(AddEditVisitor.EXTRA_ID, visitor.getId());
                intent.putExtra(AddEditVisitor.EXTRA_FIRSTNAME, visitor.getFirstname());
                intent.putExtra(AddEditVisitor.EXTRA_SURNAME, visitor.getSurname());
                intent.putExtra(AddEditVisitor.EXTRA_ACCESS_CODE, visitor.getAccessCode());
                intent.putExtra(AddEditVisitor.EXTRA_TIME_GENERATED, visitor.getTimeGenerated());
                intent.putExtra(AddEditVisitor.EXTRA_TIME_LOGGED_IN, visitor.getTimeLoggedIn());
                intent.putExtra(AddEditVisitor.EXTRA_TIME_LOGGED_OUT, visitor.getTimeLoggedOut());

                startActivityForResult(intent, EDIT_VISITOR_REQUESTCODE);
            }
        });

        fabAdVisitor = findViewById(R.id.fab_add_visitor);
        fabAdVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditVisitor.class);
                startActivityForResult(intent, ADD_VISITOR_REQUESTCODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_VISITOR_REQUESTCODE && resultCode == RESULT_OK) {
            String firstname = data.getStringExtra(AddEditVisitor.EXTRA_FIRSTNAME);
            String surname = data.getStringExtra(AddEditVisitor.EXTRA_SURNAME);
            int accessCode = Integer.parseInt(data.getStringExtra(AddEditVisitor.EXTRA_ACCESS_CODE));
            long date = Long.parseLong(data.getStringExtra(AddEditVisitor.EXTRA_TIME_GENERATED));

            Visitor visitor = new Visitor(surname, firstname, accessCode, date, 0L, 0L);
            visitorViewModel.insert(visitor);
            Toast.makeText(this, "Visitor Access Generated", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_VISITOR_REQUESTCODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditVisitor.EXTRA_ID, -1);

            if (id == -1) {
                int ids = id;
                Toast.makeText(this, "Visitor cannot be Updated.", Toast.LENGTH_LONG).show();
                return;
            }

            String firstname = data.getStringExtra(AddEditVisitor.EXTRA_FIRSTNAME);
            String surname = data.getStringExtra(AddEditVisitor.EXTRA_SURNAME);
            int accessCode = Integer.parseInt(data.getStringExtra(AddEditVisitor.EXTRA_ACCESS_CODE));
            long timeGenerated = data.getLongExtra(AddEditVisitor.EXTRA_TIME_GENERATED, 11L);
            long timeLoggedIn = data.getLongExtra(AddEditVisitor.EXTRA_TIME_LOGGED_IN, 11L);
            long timeLoggedOut = data.getLongExtra(AddEditVisitor.EXTRA_TIME_LOGGED_OUT, 11L);

            Visitor visitor = new Visitor(surname, firstname, accessCode, timeGenerated, timeLoggedIn, timeLoggedOut);
            visitor.setId(id);

            if (data.getBooleanExtra(AddEditVisitor.EXTRA_DELETE_FLAG, false) == true) {
                visitorViewModel.delete(visitor);
                Toast.makeText(this, "Visitor Deleted", Toast.LENGTH_SHORT).show();
                return;
            }

            visitorViewModel.update(visitor);

        } else {
            Toast.makeText(this, "Visitor Access Not Generated", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_visitors:
                visitorViewModel.deleteAll();
                Toast.makeText(this, "All Visitors Deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.visitor_controller:
                Intent intent = new Intent(MainActivity.this, VisitorControlMain.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
