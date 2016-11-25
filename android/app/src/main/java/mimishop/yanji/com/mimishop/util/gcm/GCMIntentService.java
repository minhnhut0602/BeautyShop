/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mimishop.yanji.com.mimishop.util.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.devsmart.android.StringUtils;
//import com.facebook.android.Util;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.Preference;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.IntroActivity;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";
    private Handler mHandler = null;
    public GCMIntentService() {
        super(Common.SENDER_ID);

        mHandler = new Handler();
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);

        if(context.getClass() == AppController.class) {
            ((AppController)context).requestRegisterGCMRegID(registrationId);
        }
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
    }



    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");

        // notifies user
        // check to see if it is a message
        if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            String title = "";
            String type = "";
            String landingURL = null;
            String bannerURL = null;

            // if your key/value is a JSON string, just extract it and parse it using JSONObject
            String message = intent.getExtras().getString("message");

            if(message != null) {
                try {
                    JSONObject json = new JSONObject(message);
                    type = json.getString("type");
                    title = json.getString("title");
                    message = json.getString("message");

                    JSONObject data = json.getJSONObject("data");
                    bannerURL = data.getString("img_url");
                    landingURL = data.getString("landing_url");
                }
                catch (JSONException e) {

                }
            }

            Preference pref = Preference.getInstance();

            boolean isEnable = false;

            if(type != null) {
                if (type.equals("1")) { // comment
                    if (pref.getSharedPreference(context, Preference.KEY_PUSH_COMMENT, true) == true) {
                        isEnable = true;
                    }
                } else if (type.equals("2")) { // tag
                    if (pref.getSharedPreference(context, Preference.KEY_PUSH_TAG, true) == true) {
                        isEnable = true;
                    }
                } else if (type.equals("3")) { // envelop
                    if (pref.getSharedPreference(context, Preference.KEY_PUSH_ENVELOP, true) == true) {
                        isEnable = true;
                    }
                }
                else if(type.equals("0")) {
                    isEnable = true;
                }

                if (isEnable == true) {
                    if(bannerURL != null && bannerURL.equals("") == false) {

                        final  String strMessage = message;
                        final  String strLandingURL = landingURL;
                        final  String strTitle = title;
                        final  Context contex1 = context;
                        Bitmap bitmap = Utility.getBitmapFromURL(bannerURL);

                        createNotification(contex1, strTitle, strMessage, strLandingURL, bitmap);
                    }
                    else {
                        generateNotification(context, title, message, landingURL);
                    }
                }
            }
        }
    }


    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");

        // notifies user
        generateNotification(context, "mimishop", "onDeletedMessages", null);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);

        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String title, String message, String landingURL) {

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);


        Intent notificationIntent = null;

        if(landingURL != null && landingURL.isEmpty() == false) {
            notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(landingURL));
        }
        else {
            notificationIntent = new Intent(context, IntroActivity.class);
            // set intent so it does not start a new activity
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        try {
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            notification.setLatestEventInfo(context, title, message, intent);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notification);
        }catch(Exception e){}
        Utility.vibrate(context, 500);
    }

    private void createNotification(Context context, String title, String text, String link, Bitmap landingBitmap){

        Notification notification  = new Notification.BigPictureStyle(
                           new Notification.Builder(context).setContentTitle(title).setSmallIcon(R.drawable.ic_launcher)
                      ).bigPicture(landingBitmap).setSummaryText(text).build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pending intent is redirection using the deep-link
        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        resultIntent.setData(Uri.parse(link));

        PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
        notification.setLatestEventInfo(context, title, text, pending);

        mNotificationManager.notify(0, notification);

        Utility.vibrate(context, 500);
    }
}
