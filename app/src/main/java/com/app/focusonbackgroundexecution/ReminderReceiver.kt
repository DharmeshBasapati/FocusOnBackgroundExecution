package com.app.focusonbackgroundexecution

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.app.focusonbackgroundexecution.network.builder.RetrofitBuilder
import com.app.focusonbackgroundexecution.network.model.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val REQUEST_TIMER1 = 1

        private fun getIntent(context: Context, requestCode: Int): PendingIntent? {
            val intent = Intent(context, ReminderReceiver::class.java)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                return null
            }
        }

        fun startRepeatingAlarmFromNow(context: Context) {
            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // this alarm might execute between now to next day, and repeat daily
            //alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)

            //trigger once every 1 minute = 60000 millis
            val triggerAtMillis = System.currentTimeMillis() + 60000
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, 60000, pendingIntent)
        }

        fun cancelAlarm(context: Context) {
            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }

        fun startRepeatingAlarmAtSpecificTime(context: Context) {
            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            val interval = 1000 * 60 * 20

            /* Set the alarm to start at 15:22 PM */
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, 15)
            calendar.set(Calendar.MINUTE, 22)

            /* Repeating on every 1 minute interval */
            manager!!.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                (60000).toLong(),
                pendingIntent
            )

        }
    }


    override fun onReceive(context: Context, p1: Intent) {

        Log.d("FOBE", "onReceive: Alarm Triggered !!!")

        Toast.makeText(context, "Alarm Triggered!!!", Toast.LENGTH_LONG).show()

        showNotification(context)

        callAnyAPI()

    }

    private fun showNotification(context: Context) {
        val noificationId = Random().nextInt(100)
        val channelId = "notification_channel_3"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val intent = Intent(
            context,
            MainActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(
            context, channelId
        )
        builder.setSmallIcon(R.drawable.ic_small_noti_icon)
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentTitle("HAR ROZ LAKHPATI") // make suer change the channel for image

        builder.setContentText("Aaj ka sawaal jo bana sakta hai aapko lakhpati abhi ke abhi !!!")
        //notification for image
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.sample_image
        )
        builder.setLargeIcon(bitmap)
        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId, "Notification channel 1",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This notification channel is used to notify user"
                notificationChannel.enableVibration(true)
                notificationChannel.enableLights(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        val notification: Notification = builder.build()
        notificationManager?.notify(noificationId, notification)
    }

    private fun callAnyAPI() {

        RetrofitBuilder.focusApiServices.getFacts().enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                Log.d("FOBE", "onResponse: CALLED - ${response.body()}")
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.d("FOBE", "onFailure: CALLED - ${t.message.toString()}")
            }
        })
    }
}