package com.blacksugar.mytodolist;


import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter<String> iAdapter;
    ArrayAdapter<String> cAdapter;
    ListView listIncompleteTask;
    ListView listCompleteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        TextView incompleteHeader = new TextView(this);
        incompleteHeader.setText("TO DO");

        TextView completeHeader = new TextView(this);
        completeHeader.setText("DONE");

        listIncompleteTask = (ListView)findViewById(R.id.listIncompleteTask);
        listCompleteTask = (ListView)findViewById(R.id.listCompleteTask);

        listIncompleteTask.addHeaderView(incompleteHeader);
        listCompleteTask.addHeaderView(completeHeader);

        loadIncompleteTaskList();
        loadCompleteTaskList();
    }

    private void loadIncompleteTaskList() {
        ArrayList<String> taskList = dbHelper.getIncompleteTaskList();

        if (iAdapter == null) {
            iAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.task_title, taskList);
            listIncompleteTask.setAdapter(iAdapter);
        } else {
            iAdapter.clear();
            iAdapter.addAll(taskList);
            iAdapter.notifyDataSetChanged();
        }
    }

    private void loadCompleteTaskList() {
        ArrayList<String> taskList = dbHelper.getCompleteTaskList();

        if (cAdapter == null) {
            cAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.task_title, taskList);
            listCompleteTask.setAdapter(cAdapter);
        } else {
            cAdapter.clear();
            cAdapter.addAll(taskList);
            cAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        // Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                dbHelper.insertNewTask(task);
                                loadIncompleteTaskList();
                                loadCompleteTaskList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask (View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadIncompleteTaskList();
        loadCompleteTaskList();
    }

    public void updateTask (View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        Toast toast = Toast.makeText(getApplicationContext(), task, Toast.LENGTH_SHORT);
        toast.show();
        dbHelper.updateTask(task);
        loadIncompleteTaskList();
        loadCompleteTaskList();
    }
/*
    public void checkTasks (View view) {

        CheckBox checkBox = (CheckBox)findViewById(R.id.check_box);

                View parent = (View) view.getParent();
                TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
                String task = String.valueOf(taskTextView.getText());

                if(checkBox.isChecked()) {
                    dbHelper.markAsComplete(task);
                    Toast toast = Toast.makeText(getApplicationContext(), "checked", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    dbHelper.markAsIncomplete(task);
                    Toast toast = Toast.makeText(getApplicationContext(), "unchecked", Toast.LENGTH_SHORT);
                    toast.show();
                }

        loadIncompleteTaskList();
        loadCompleteTaskList();
    }*/
}
