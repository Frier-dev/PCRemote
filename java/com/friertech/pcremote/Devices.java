package com.friertech.pcremote;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class Devices extends AppCompatActivity {

    public Boolean isActive = false;
    //references to button and other controls
    Button btnAdd, btnViewAll;
    EditText name, mac, ip, broadcast;
    ListView lvDevices;
    ArrayAdapter deviceArrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devicespopup);

        btnAdd = findViewById(R.id.addBtn);
        btnViewAll = findViewById(R.id.viewAllBtn);
        name = findViewById(R.id.editDeviceName);
        mac = findViewById(R.id.editMAC);
        ip = findViewById(R.id.editIpv4);
        broadcast = findViewById(R.id.editBroadcast);
        lvDevices = findViewById(R.id.listView);

        databaseHelper = new DatabaseHelper(Devices.this);
        showDevicesOnListView(databaseHelper);


        //auto adding "-" into mac address edit text
        mac.addTextChangedListener(new TextWatcher() {
            int first = 0;
            int last = 0;
            int second;
            int lastcount;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                second = first;
                first = s.length();

                lastcount = last;
                last = s.length() -1;
                System.out.println(before + " : " + first);

                //before does not count numbers - fix issue

                //check the length, and if it got shorter, then don't add the symbol
                if (mac.length() == 2 && before < first || mac.length() == 5 && before < first || mac.length() == 8 && before < first || mac.length() == 11 && before < first || mac.length() == 14 && before < first) {
                    mac.append("-");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // button listeners for the add and view all buttons
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesModel devicesModel;
                if (name.getText().toString().equals("") || mac.getText().toString().equals("") || ip.getText().toString().equals("") || broadcast.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Error on adding device", Toast.LENGTH_SHORT).show();
                    devicesModel = new DevicesModel(-1, "error", "error", "error", "error", false);
                } else if (mac.length() < 17) {
                        Toast.makeText(getApplicationContext(), "Invalid Mac Bro", Toast.LENGTH_SHORT).show();
                } else {
                    devicesModel = new DevicesModel(-1, name.getText().toString(), mac.getText().toString(), ip.getText().toString(), broadcast.getText().toString(), isActive);
                    //Toast.makeText( getApplicationContext(), devicesModel.toString(), Toast.LENGTH_SHORT).show();

                    DatabaseHelper databaseHelper = new DatabaseHelper(Devices.this);
                    boolean success = databaseHelper.addOne(devicesModel);
                    showDevicesOnListView(databaseHelper);
                }
            }
        });

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(Devices.this);
                showDevicesOnListView(databaseHelper);
            }
        });

        //short press click listener on listview
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DevicesModel clickedDevice = (DevicesModel) parent.getItemAtPosition(position);
                databaseHelper.updateAllToFalse(clickedDevice);
                databaseHelper.favoriteOne(clickedDevice);
                Toast.makeText(getApplicationContext(), "Selected device: " + clickedDevice.getName(), Toast.LENGTH_SHORT).show();
                showDevicesOnListView(databaseHelper);
            }
        });

        //long press click listener on listview
        lvDevices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DevicesModel longClickedDevice = (DevicesModel) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(Devices.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete " + longClickedDevice.getName() + "?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.deleteOne(longClickedDevice);
                                Toast.makeText(getApplicationContext(), "Deleted device: " + longClickedDevice.getName(), Toast.LENGTH_SHORT).show();
                                showDevicesOnListView(databaseHelper);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


    }


    private void showDevicesOnListView(DatabaseHelper databaseHelper2) {
        deviceArrayAdapter = new ArrayAdapter<DevicesModel>(Devices.this, android.R.layout.simple_list_item_activated_1, databaseHelper2.getAll());
        lvDevices.setAdapter(deviceArrayAdapter);
    }

}
