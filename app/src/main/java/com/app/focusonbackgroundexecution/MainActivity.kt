package com.app.focusonbackgroundexecution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ReminderReceiver.startRepeatingAlarmFromNow(this)
        //ReminderReceiver.startRepeatingAlarmAtSpecificTime(this)
    }
}