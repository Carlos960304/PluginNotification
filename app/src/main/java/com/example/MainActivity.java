/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.example;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.apache.cordova.*;

public class MainActivity extends CordovaActivity {
    private final Notify notifyNotification = new Notify(this, 20000);
    Intent intent = null;
    private Integer service = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);

        // Method to start the notification process
        //notifyNotification.executeProcess(Constants.PROCESS_START);
        // Method to start the vibrate of device
        //notifyVibrate.executeProcess(Constants.TIME_VIBRATE);
        // Method to ON|OFF the service
        //serviceBackground(Constants.STOP_SERVICE);
    }

    public void serviceBackground(int service) {
        this.service = service;
    }

    public void serviceOn() {
        if(intent == null) {
            intent = new Intent(this, MyService.class);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        notifyNotification.executeProcess(Constants.PROCESS_START);
    }

    public void serviceOff() {
        if(intent == null) {
            intent = new Intent(this, MyService.class);
        }
        notifyNotification.executeProcess(Constants.PROCESS_STOP);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        notifyNotification.executeProcess(Constants.PROCESS_START);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(service == Constants.START_SERVICE) {
            serviceOn();
        } else if(service == Constants.STOP_SERVICE) {
            serviceOff();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        notifyNotification.executeProcess(Constants.PROCESS_STOP);
        super.onDestroy();
        Log.d("My Service", "Activity destroyed...");
    }
}
