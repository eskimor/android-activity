package com.gonimo.baby;

import android.content.Intent;
import android.app.PendingIntent;
import android.content.Context;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification;
import android.app.Service;
import android.os.IBinder;
import android.util.Log;
import com.gonimo.baby.R;
import systems.obsidian.HaskellActivity;
import com.gonimo.baby.AndroidCompat;



// Service needed so we can reliably get rid of the Gonimo-running notification when Gonimo gets destroyed.
public class GonimoRunning extends Service {

  @Override
  public void onCreate() {
      Notification.Builder templ =
          new Notification.Builder(this)
          .setSmallIcon(R.drawable.ic_launcher)
          .setContentTitle(getString(R.string.gonimo_running));

      Intent intent = new Intent(this, HaskellActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

      Intent stopIntent = new Intent(this, HaskellActivity.class);
      stopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      stopIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      stopIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
      stopIntent.putExtra("com.gonimo.baby.stopIt", true);
      PendingIntent stopPending = PendingIntent.getActivity(this, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

      Notification notification = templ
          .setContentIntent(pendingIntent)
          .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop_gonimo), stopPending)
          .setDeleteIntent(stopPending)
          .build();
      startForegroundCompat(notificationId, notification);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      // If we get killed, after returning from here, restart
      // showRunningNotification();
      return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
      // We don't provide binding, so return null
      return null;
  }

  @Override
  public void onDestroy() {
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
  }


  private void startForegroundCompat(int id, Notification notification) {
    try {
      startForeground(id, notification);
    }
    catch(NoSuchMethodError e) {
      Log.d("HaskellActivity", "Asking for foreground service failed. (Not supported and thus irrelevant ;-) ");
    }
  }
  final public static int notificationId = 31415926;
}
