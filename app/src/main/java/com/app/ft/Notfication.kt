package com.app.ft

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val goal = intent.getStringExtra("goal") ?: return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "goal_notifications"
        val channelName = "Goal Notifications"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.img)
            .setContentTitle("Motivational Reminder")
            .setContentText("Remember to: $goal")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(goal.hashCode(), notification)
    }
}
