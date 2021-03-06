package com.OOSDTeam22.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.OOSDTeam22.todolist.db.TaskContract;
import com.OOSDTeam22.todolist.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
	The home page of the app that displays all of the saved lists
*/
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    public static final String EXTRA_MESSAGE = "com.example.OOSDTeam22.MESSAGE";

	/**
		Loads the layout for the home screen
	*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        updateUI();
    }

	/**
		Loads the top menu for the home screen
		@return true if the menu was loaded
	*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

	/**
		Button listener for adding new lists
		@return true if the list was added successfully
	*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_LIST_NAME, task);
                                values.put(TaskContract.TaskEntry.COL_LIST_ITEM, "Add some list items");
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	/**
		Loads the ListDetailsActivity screen with the desired list
	*/
    public void openTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.list_title);
        String task = String.valueOf(taskTextView.getText());

        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, task);
        startActivity(intent);
    }

	/**
		Refreshes the UI based on the results of the database query
	*/
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_LIST_NAME},
                null, null, null, null, null);

        List<String> names = new ArrayList();
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_LIST_NAME);

            names.add(cursor.getString(idx));
            if(names.indexOf(cursor.getString(idx)) == (names.size() - 1)) {
                taskList.add(cursor.getString(idx));
            }
            else {
                names.remove(names.size() - 1);
            }
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_list,
                    R.id.list_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    /**
        Checks if a listItem spans over multiple lines
     */
    public static boolean IsOneLine(String listItem) {
        if (listItem.contains("\n")) {
            return false;
        }
        else {
            return true;
        }
    }

}
