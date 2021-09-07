package com.example;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;

public class Notify {
    private final Handler handler = new Handler();
    private final int startTime;
    private int secondsVibrationOn;
    private int secondsVibrationOff;
    private int repeatVibrate;
    Context context;

    public Notify(Context context, int startTime, int secondsVibrationOn, int secondsVibrationOff, int repeatVibrate) {
        this.context = context;
        this.startTime = startTime;
        this.secondsVibrationOn = secondsVibrationOn;
        this.secondsVibrationOff = secondsVibrationOff;
        this.repeatVibrate = repeatVibrate;
    }

    public Notify(Context context, int startTime) {
        this.context = context;
        this.startTime = startTime;
    }

    public Notify(Context context, int secondsVibrationOn, int secondsVibrationOff, int repeatVibrate) {
        this.context = context;
        this.startTime = 20000;
        this.secondsVibrationOn = secondsVibrationOn;
        this.secondsVibrationOff = secondsVibrationOff;
        this.repeatVibrate = repeatVibrate;
    }

    public void executeProcess(Integer type) {
        switch (type) {
            case Constants.PROCESS_START:
                startNotification();
                break;

            case Constants.PROCESS_STOP:
                stopNotification();
                break;

            case Constants.TIME_VIBRATE:
                vibration(secondsVibrationOn, secondsVibrationOff, repeatVibrate);
                break;

            default:
        }
    }

    public void startNotification() {
        notificationRunnable.run();
    }

    public void stopNotification() {
        handler.removeCallbacks(notificationRunnable);
    }

    private final Runnable notificationRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(context, MyService.class);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
            handler.postDelayed(this, startTime);
        }
    };

    private void vibration(int secondsVibrate, int secondsNoVibrate, int repeatVibrate) {
        int i = 1;
        int operation = repeatVibrate*2+1;
        long[] pattern = new long[operation];
        pattern[0] = 0;
        while (i<operation) {
            pattern[i++] = secondsVibrate;
            pattern[i++] = secondsNoVibrate;
        }
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, -1);
    }
}