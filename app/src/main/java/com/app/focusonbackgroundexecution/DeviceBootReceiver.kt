package com.app.focusonbackgroundexecution

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast

class DeviceBootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if(intent.action.equals("android.intent.action.BOOT_COMPLETED")){

            //Set the alarm here
            val alarmIntent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(context,0,alarmIntent,PendingIntent.FLAG_IMMUTABLE)
            } else {
                null
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60000,pendingIntent)
            Log.d("FOBE", "Starting Alarm as device booted")
            Toast.makeText(context,"Starting Alarm as device booted.",Toast.LENGTH_SHORT).show()

        }
    }

}