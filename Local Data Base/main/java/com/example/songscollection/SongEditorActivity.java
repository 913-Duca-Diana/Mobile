package com.example.songscollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashSet;

public class SongEditorActivity extends AppCompatActivity {
    EditText songEditText;
    int songId;

    SharedPreferences sharedPreferences;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_editor);

        ActionBar actionBar=getSupportActionBar();

        sharedPreferences =this.getSharedPreferences("com.example.songscollection", Context.MODE_PRIVATE);
        songEditText=findViewById(R.id.song_EditText);

        Intent intent= getIntent();
        songId=intent.getIntExtra("songId",-1);

        if (songId !=-1){
            songEditText.setText(MainActivity.songs.get(songId));
            actionBar.setTitle("Edit song");

        }else{
            MainActivity.songs.add("");
            songId=MainActivity.songs.size() -1;
            actionBar.setTitle("Add song");

        }

        songEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.songs.set(songId, String.valueOf(s));
                MainActivity.adapter.notifyDataSetChanged();

                HashSet<String> songSet= new HashSet<>(MainActivity.songs);
                sharedPreferences.edit().putStringSet("songs",songSet).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.save_song_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       super.onOptionsItemSelected(item);


       if(item.getItemId()== R.id.savesong){
           startActivity(new Intent(getApplicationContext(),MainActivity.class));
           finish();
           return true;
       }
       return false;
    }
}