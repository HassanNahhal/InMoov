package com.example.hassannahhal.inmoov;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final String TAG = "InMoov";

    ToggleButton ringTbtn, littleTbtn, groomingTbtn, middleTbtn, thumbTbtn, allTbtn;

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "07:12:05:16:60:04";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "In onCreate()");

        setContentView(R.layout.activity_main);
        ringTbtn = (ToggleButton) findViewById(R.id.ringTbtn);
        littleTbtn = (ToggleButton) findViewById(R.id.littleTbtn);
        groomingTbtn = (ToggleButton) findViewById(R.id.groomingTbtn);
        middleTbtn = (ToggleButton) findViewById(R.id.middleTbtn);
        thumbTbtn = (ToggleButton) findViewById(R.id.thumbTbtn);
        allTbtn = (ToggleButton) findViewById(R.id.allTbtn);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();


        allTbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AllsendDataStart();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked On", Toast.LENGTH_SHORT);
                    msg.show();
                } else {
                    AllsendDataStop();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked Off", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });

        ringTbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    RingsendDataStart();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked On", Toast.LENGTH_SHORT);
                    msg.show();
                } else {
                    RingsendDataStop();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked Off", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });

        littleTbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    LitlesendDataStart();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked On", Toast.LENGTH_SHORT);
                    msg.show();
                } else {
                    litlesendDataStop();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked Off", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });

        groomingTbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    GroomingsendDataStart();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked On", Toast.LENGTH_SHORT);
                    msg.show();
                } else {
                    GroomingsendDataStop();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked Off", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });

        middleTbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    MiddlesendDataStart();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked On", Toast.LENGTH_SHORT);
                    msg.show();
                } else {
                    MiddlesendDataStop();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked Off", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });

        thumbTbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ThumbsendDataStart();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked On", Toast.LENGTH_SHORT);
                    msg.show();
                } else {
                    ThumbsendDataStop();
                    Toast msg = Toast.makeText(getBaseContext(),
                            "You have clicked Off", Toast.LENGTH_SHORT);
                    msg.show();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...In onResume - Attempting client connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting to Remote...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Creating Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth is enabled...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast msg = Toast.makeText(getBaseContext(),
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }

    //===========ALL==================
    //To open or close all the buttons at on click
    //If not connected to a BT adapter it will crash
    //Start will close the finger
    //Stop will open the finger

    private void AllsendDataStart() {

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 1);
            outStream.write((byte) 180);
            outStream.write((byte) 2);
            outStream.write((byte) 180);
            outStream.write((byte) 3);
            outStream.write((byte) 180);
            outStream.write((byte) 4);
            outStream.write((byte) 180);
            outStream.write((byte) 5);
            outStream.write((byte) 180);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void AllsendDataStop() {
        //byte[] msgBuffer = byte(255,1, 0, 2, 0, 3, 0, 4, 0, 5, 0);
        // Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 1);
            outStream.write((byte) 0);
            outStream.write((byte) 2);
            outStream.write((byte) 0);
            outStream.write((byte) 3);
            outStream.write((byte) 0);
            outStream.write((byte) 4);
            outStream.write((byte) 0);
            outStream.write((byte) 5);
            outStream.write((byte) 0);

        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    //===========Ring==================
    //To open or close the ring finger at on click
    //If not connected to a BT adapter it will crash
    //Start will close the finger
    //Stop will open the finger
    private void RingsendDataStart() {
        //byte[] msgBuffer = message.getBytes(255, 1, 180, 2, 180, 3, 180, 4, 180, 5, 180);
        //Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 4);
            outStream.write((byte) 180);

        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void RingsendDataStop() {
        //byte[] msgBuffer = byte(255,1, 0, 2, 0, 3, 0, 4, 0, 5, 0);
        // Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 4);
            outStream.write((byte) 0);


        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    //===========Middle==================
    //To open or close the middle finger at on click
    //If not connected to a BT adapter it will crash
    //Start will close the finger
    //Stop will open the finger
    private void MiddlesendDataStart() {
        //byte[] msgBuffer = message.getBytes(255, 1, 180, 2, 180, 3, 180, 4, 180, 5, 180);
        //Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 3);
            outStream.write((byte) 180);

        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void MiddlesendDataStop() {
        //byte[] msgBuffer = byte(255,1, 0, 2, 0, 3, 0, 4, 0, 5, 0);
        // Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 3);
            outStream.write((byte) 0);


        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    //===========Litle==================
    //To open or close the little finger at on click
    //If not connected to a BT adapter it will crash
    //Start will close the finger
    //Stop will open the finger
    private void LitlesendDataStart() {
        //byte[] msgBuffer = message.getBytes(255, 1, 180, 2, 180, 3, 180, 4, 180, 5, 180);
        //Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 5);
            outStream.write((byte) 180);

        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void litlesendDataStop() {
        //byte[] msgBuffer = byte(255,1, 0, 2, 0, 3, 0, 4, 0, 5, 0);
        // Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 5);
            outStream.write((byte) 0);


        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    //===========Grooming==================
    //To open or close the grooming finger at on click
    //If not connected to a BT adapter it will crash
    //Start will close the finger
    //Stop will open the finger
    private void GroomingsendDataStart() {
        //byte[] msgBuffer = message.getBytes(255, 1, 180, 2, 180, 3, 180, 4, 180, 5, 180);
        //Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 2);
            outStream.write((byte) 180);

        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void GroomingsendDataStop() {
        //byte[] msgBuffer = byte(255,1, 0, 2, 0, 3, 0, 4, 0, 5, 0);
        // Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 2);
            outStream.write((byte) 0);


        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }


    //===========Thumb==================
    //To open or close the thumb finger at on click
    //If not connected to a BT adapter it will crash
    //Start will close the finger
    //Stop will open the finger
    private void ThumbsendDataStart() {
        //byte[] msgBuffer = message.getBytes(255, 1, 180, 2, 180, 3, 180, 4, 180, 5, 180);
        //Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 1);
            outStream.write((byte) 180);

        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    private void ThumbsendDataStop() {
        //byte[] msgBuffer = byte(255,1, 0, 2, 0, 3, 0, 4, 0, 5, 0);
        // Log.d(TAG, "...Sending data: " + message + "...");

        try {
            outStream.write((byte) 255);
            outStream.write((byte) 1);
            outStream.write((byte) 0);


        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
}