package com.example.songscollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView songsListView;
    TextView emptyTV;
    static List<String> songs;
    static ArrayAdapter adapter;

    SharedPreferences sharedPreferences;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences =this.getSharedPreferences("com.example.songscollection", Context.MODE_PRIVATE);

        songsListView= findViewById(R.id.songs_ListView);
        emptyTV=findViewById(R.id.emptyTV);

        songs= new ArrayList<>();

//
        HashSet<String> songSet=(HashSet<String>) sharedPreferences.getStringSet("songs",null);

        if (songSet.isEmpty() || songSet == null ){
            emptyTV.setVisibility(View.VISIBLE);
        }else{
            emptyTV.setVisibility(View.GONE);
            songs=new ArrayList<>(songSet);
        }
//

        adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.custom_songs_row,R.id.songsTV,songs);
        songsListView.setAdapter(adapter);

        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SongEditorActivity.class);
                intent.putExtra("songId", position);
                startActivity(intent);
            }
        });

        songsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                int itemToDelete= position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure")
                        .setMessage("do you want to delete this song?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                songs.remove(itemToDelete);
                                adapter.notifyDataSetChanged();

                                HashSet<String> songSet = new HashSet<>(songs);
                                sharedPreferences.edit().putStringSet("songs",songSet).apply();

                                if (songSet.isEmpty() || songSet== null){
                                    emptyTV.setVisibility(View.VISIBLE);                                }
                            }
                        }).setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_song_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.add_song){
            startActivity(new Intent(getApplicationContext(), SongEditorActivity.class));
            return true;
        }
        return false;
    }

}