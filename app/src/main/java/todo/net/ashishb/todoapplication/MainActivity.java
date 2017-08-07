package todo.net.ashishb.todoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_ACTION = 1;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = EditActivity.getEditIntent(MainActivity.this, items.get(position), position);
                startActivityForResult(intent, EDIT_ACTION);
            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(view.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ACTION) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(EditActivity.EDIT_POSITION, -1);
                String newValue = data.getStringExtra(EditActivity.EDIT_NEW_VALUE);
                synchronized (items) {
                    items.remove(position);
                    items.add(position, newValue);
                }
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private ArrayList<String> readItems() {
        File file = getFile();
        items = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String item = reader.readLine();
            while (item != null) {
                items.add(item);
                item = reader.readLine();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    private void writeItems() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFile()));
            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile() {
        return new File(getFilesDir(), "todo.txt");
    }
}