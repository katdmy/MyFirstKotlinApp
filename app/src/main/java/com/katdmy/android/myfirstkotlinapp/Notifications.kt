package com.katdmy.android.myfirstkotlinapp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.katdmy.android.myfirstkotlinapp.model.Movie
import com.katdmy.android.myfirstkotlinapp.view.MainActivity

class Notifications (private val context: Context) {

    companion object {
        private const val CHANNEL_NEW_MESSAGES = "new_messages"
        private const val REQUEST_CONTENT = 1
        private const val CHAT_TAG = "chat"
    }

    private val TAG = Notifications::class.java.simpleName
    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(CHANNEL_NEW_MESSAGES,
                    NotificationManagerCompat.IMPORTANCE_HIGH
                )
                    .setName(context.getString(R.string.channel_new_messages))
                    .setDescription(context.getString(R.string.channel_new_messages_description))
                    .build()
            )
        }
    }

    fun showNotification(movie: Movie) {
        Log.e(TAG, "Trying to show notification: $movie")

        val contentUri = "com.katdmy.android.myfirstkotlinapp://movie/id/${movie.id}".toUri()

        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MESSAGES)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(movie.title)
            .setSmallIcon(R.drawable.ic_movie)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

        Glide.with(context)
            .asBitmap()
            .load(movie.poster)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    builder.setLargeIcon(resource)
                    notificationManagerCompat.notify(CHAT_TAG, movie.id, builder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }
}