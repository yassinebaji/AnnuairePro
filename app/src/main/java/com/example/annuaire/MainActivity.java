package com.example.annuaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase database;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize database
        database=AppDatabase.getInstance(this);
//        database.contact().insert(new Contact(2,"Yasine","BAji@gmail","jdhfjdhf","djfhdjhf"));

        ArrayList<Contact> contatcs= (ArrayList<Contact>) database.contact().getAll();

        ContactsAdapter adapter = new ContactsAdapter(this, contatcs);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        MainActivity m = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                TextView ID = view.findViewById(R.id.ID);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("ID",ID.getText().toString());
                startActivity(i);

                Log.d("1","Click working");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ID = view.findViewById(R.id.ID);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select your answer")
                        .setMessage("Are you sure, you want to delete this contatct ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contact contact = new Contact();
                                contact.setID(Integer.parseInt(ID.getText().toString()));
                                database.contact().delete(contact);
                                RefreshListView((ArrayList<Contact>) database.contact().getAll());

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

                return true;
            }
        });
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_contact_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(myIntent);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshListView((ArrayList<Contact>) database.contact().getAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MainActivity m =this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                RefreshListView((ArrayList<Contact>) database.contact().findByName("%"+newText+"%"));

                return false;
            }
        });
        return true;
    }

    void RefreshListView(ArrayList<Contact> contatcs){
        ContactsAdapter adapter = new ContactsAdapter(this,contatcs);
        listView.setAdapter(adapter);
    }


}

class ContactsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> items;

    //public constructor
    public ContactsAdapter(Context context, ArrayList<Contact> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.simple_list, parent, false);
        }
        // get current item to be displayed
        Contact currentItem = (Contact)getItem(position);
        // get the TextView for item name and item description
        TextView textViewItemID = (TextView) convertView.findViewById(R.id.ID);
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.Name);
        TextView textViewItemJob = (TextView) convertView.findViewById(R.id.Job);
        TextView textViewItemTel = (TextView) convertView.findViewById(R.id.Tel);
        TextView textViewItemEmail = (TextView) convertView.findViewById(R.id.Email);
        ImageButton btn = (ImageButton) convertView.findViewById(R.id.Call);
        textViewItemID.setText(String.valueOf(currentItem.getID()));
        textViewItemName.setText(currentItem.getName());
        textViewItemJob.setText(currentItem.getJob());
        textViewItemTel.setText(currentItem.getPhone());
        textViewItemEmail.setText(currentItem.getEmail());

        btn.setOnClickListener((ev) -> {
            Uri telephone = Uri.parse("tel:"+currentItem.getPhone());
            Intent SA = new Intent(Intent.ACTION_DIAL,telephone);
            context.startActivity(SA);
        });
        return convertView;
    }
}