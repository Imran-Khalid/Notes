package com.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes=new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.add_note){
            Intent intent=new Intent(getApplicationContext(),noteEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listView);
        Log.i("TESTING","ETHE AGYA");
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.notes", Context.MODE_PRIVATE);
        Log.i("TESTING","ETHE AGYA");
        try {
            Set<String> set = sharedPreferences.getStringSet("notes", null);
            Log.i("TESTING","ETHE v AGYA");
            try{
                if(set==null) {
                    notes.add("new note");
                }else{
                    notes=new ArrayList<>(set);
                }}catch (Exception e){
                System.out.println(e);
            }
        }catch(Exception e){
            Log.i("ERROR",e.getMessage());
        }


        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent=new Intent(getApplicationContext(),noteEditorActivity.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.notes", Context.MODE_PRIVATE);
                                HashSet<String> set=new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putString("notes", String.valueOf(set)).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
    }
}
