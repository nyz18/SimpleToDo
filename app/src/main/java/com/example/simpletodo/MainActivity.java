package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items; //creates list of string items

    Button btnAdd ;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);   // maps buttons with these ids that we made
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

       ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
           @Override
           public void onItemLongClicked(int position) {
               items.remove(position);
               itemsAdapter.notifyItemRemoved(position); // notify our item

               Toast.makeText(getApplicationContext(),"Item Removed ",Toast.LENGTH_LONG).show();
               saveItems();
           }

       };

        itemsAdapter = new ItemsAdapter(items,onLongClickListener); // calling the constructor
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                items.add(todoItem);        // we added our text on line
                itemsAdapter.notifyItemChanged(items.size()-1);
                etItem.setText("");         // clear our string

                Toast.makeText(getApplicationContext(),"Item Added ",Toast.LENGTH_LONG).show();
                saveItems();
            }
        });
    }
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e); //debug items if you need msg
            items = new ArrayList<>();

        }

    }
    private void saveItems(){
        try {
            //writes to our datafile to save our info
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //logs the error - does same thing with the reading
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}