package com.friertech.pcremote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView Devices;

    private ImageView On;
    private ImageView Off;
    private ImageView Cancel;
    private ImageView Restart;
    private ImageView Sleep;
    private ImageView Lock;
    private ImageView Program;
    public static String mac;
    public static String ip;
    public static String broadcast;
    SharedPreferences prefs = null;
    Devices devices = new Devices();
    WOLHelper wolHelper = new WOLHelper();
    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.friertech.pcremote", MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        // using threading to perform network actions on main form
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        databaseHelper.Active();
        List<DevicesModel> devices = databaseHelper.Active();

        Cancel = (ImageView) findViewById(R.id.Cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                    databaseHelper.Active();
                    List<DevicesModel> devices = databaseHelper.Active();
                    ip = databaseHelper.ip;
                    Socket s = new Socket(ip, 6666);
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF("-cancel-");
                    dout.flush();
                    dout.close();
                    s.close();
                    Toast.makeText(getApplicationContext(), "Event cancelled", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Invalid IPv4 Address.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        //get restart button
        Restart = (ImageView) findViewById(R.id.Restart);
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to restart your pc?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                                        databaseHelper.Active();
                                        List<DevicesModel> devices = databaseHelper.Active();
                                        ip = databaseHelper.ip;

                                        Socket s = new Socket(ip, 6666);
                                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                        dout.writeUTF("-rebootPc-");
                                        dout.flush();
                                        dout.close();
                                        s.close();
                                        Toast.makeText(getApplicationContext(), "Pc is now restarting! Press cancel to cancel (10 seconds regret time!)", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Invalid IPv4 Address.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        });

        //run program
        Program = (ImageView) findViewById(R.id.Program);
        Program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //get sleep button
        Sleep = (ImageView) findViewById(R.id.Sleep);
        Sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to put your pc in sleep mode");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                                    databaseHelper.Active();
                                    List<DevicesModel> devices = databaseHelper.Active();
                                    ip = databaseHelper.ip;
                                    Socket s = new Socket(ip, 6666);
                                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                    dout.writeUTF("-sleepPc-");
                                    dout.flush();
                                    dout.close();
                                    s.close();
                                    Toast.makeText(getApplicationContext(), "Pc is sleeping!)", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Invalid IPv4 Address.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //get lock button
        Lock = (ImageView) findViewById(R.id.Lock);
        Lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to lock your pc?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                                    databaseHelper.Active();
                                    List<DevicesModel> devices = databaseHelper.Active();
                                    ip = databaseHelper.ip;
                                    Socket s = new Socket(ip, 6666);
                                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                    dout.writeUTF("-lockPc-");
                                    dout.flush();
                                    dout.close();
                                    s.close();
                                    Toast.makeText(getApplicationContext(), "Pc is now locked!", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Invalid IPv4 Address.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //get off button
        Off = (ImageView) findViewById(R.id.Off);
        Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to turn off your pc?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                                    databaseHelper.Active();
                                    List<DevicesModel> devices = databaseHelper.Active();
                                    ip = databaseHelper.ip;

                                    Socket s = new Socket(ip, 6666);
                                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                    dout.writeUTF("-turnOffPc-");
                                    dout.flush();
                                    dout.close();
                                    s.close();
                                    Toast.makeText(getApplicationContext(), "Pc is now turning off! Press cancel to cancel (10 seconds regret time!)", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Invalid IPv4 Address.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //get on button
        On = (ImageView) findViewById(R.id.On);
        On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to turn on your pc?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                                databaseHelper.Active();
                                List<DevicesModel> devices = databaseHelper.Active();
                                mac = databaseHelper.mac;
                                broadcast = databaseHelper.broadcast;
                                if (mac == null) {
                                    Toast.makeText(getApplicationContext(), "No active/added device found", Toast.LENGTH_SHORT).show();
                                } else {
                                    WOLHelper.main();
                                    Toast.makeText(getApplicationContext(), "PC is now waking up", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //get AddDevice button and open dialog
        Devices = (TextView) findViewById(R.id.Devices);
        Devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Devices();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        append active text with device name
         */
        TextView active = (TextView) findViewById(R.id.activeText);
        active.setText("Active: " + databaseHelper.GetActiveDevice());

        /*
        Check if user is on its first run on the app.
         */
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs

            Intent intent = new Intent(this, SplashScreen.class);
            startActivity(intent);

            prefs.edit().putBoolean("firstrun", false).apply();
        }

    }

    public void Devices() {
        Intent intent = new Intent(this, Devices.class);
        startActivity(intent);
    }

    }







