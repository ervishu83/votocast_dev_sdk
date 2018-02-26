package class_adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Chirag on 4/25/2016.
 */
public class Constant {

    //    public static String host_url = "http://votocast-dev.us-west-2.elasticbeanstalk.com/apis/v3/";
//    public static String main_url = "https://votocast.com";
//    public static String main_url = "http://192.168.1.11/votocast";
    public static String main_url = "http://dev.votocast.com";

    public static String host_url = main_url + "/apis/v4/";
//    public static String host_url = "http://192.168.1.11/votocast/apis/v3/";

    public static String get_account_appdata_url = host_url + "get_account_appdata";
    public static String login_url = host_url + "login";
    public static String register_url = host_url + "register";
    public static String forgot_url = host_url + "forget_password";
    public static String get_campaign_detail_url = host_url + "get_campaign";
    public static String get_leaderboard_url = host_url + "get_leaderboard";
    public static String get_newest_videos_url = host_url + "get_newestVideo";
    public static String edit_profile_url = host_url + "edit_profile";
    public static String get_user_url = host_url + "get_user";
    public static String get_user_leadreboard_url = host_url + "get_user_leaderboard";
    public static String follow_campaign_url = host_url + "follow_campaign";
    public static String unfollow_campaign_url = host_url + "unfollow_campaign";
    public static String follow_user_url = host_url + "follow_user";
    public static String unfollow_user_url = host_url + "unfollow_user";
    public static String city_state_url = host_url + "city_state";
    public static String change_password_url = host_url + "change_password";
    public static String add_device_url = host_url + "add_device";
    public static String set_notifications_url = host_url + "set_notifications";
    public static String search_featured_url = host_url + "search_featured";
    public static String get_home_url = host_url + "get_home";
    public static String comment_video_url = host_url + "comment_video";
    public static String like_video_url = host_url + "like_video";
    public static String unlike_video_url = host_url + "unlike_video";
    public static String get_profile_url = host_url + "get_profile";
    public static String get_user_newestvideo_url = host_url + "get_user_newestvideo";
    public static String resend_email_url = host_url + "resend_email";

    public static String add_fbtoken_url = host_url + "add_fbtoken";
    public static String add_twitter_token_url = host_url + "add_twitter_token";
    public static String get_campaings_list_url = host_url + "get_campaings_list";
    public static String get_campaings_list_to_upload_url = host_url + "get_campaings_list_to_upload";

    public static String get_comment_video_url = host_url + "get_comment_video";
    public static String get_video_url = host_url + "get_video";
    public static String report_video_url = host_url + "report_video";
    public static String video_share_url = host_url + "video_share";
    public static String video_show_url = host_url + "video_show";
    public static String get_activities_url = host_url + "get_activities";

    public static String search_all_url = host_url + "search_all";
    public static String add_video_url = host_url + "add_video";
    public static String report_issue_url = host_url + "report_issue";

    public static String delete_comment_video_url = host_url + "delete_comment_video";
    public static String delete_video_url = host_url + "delete_video";
    public static String get_featuredvideo_url = host_url + "get_featuredvideo";
    public static String delete_activities_url = host_url + "delete_activities";
    public static String add_location_url = host_url + "add_location";

    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "votocast";

    public static Location mLastLocation;
    public static String editCommentId="",oldCommentData="";
    public static int editCommentPosition;
    public static boolean isNormalComment = true;

    public static String colorPrimary;
    public static String colorPrimaryDark;
    public static String unselectedTab;
    public static String searchEditBack;
    public static String Authorization = "Basic cDJ6eGYzOjJkaW80aTBm";  // votocast:Basic cDJ6eGYzOjJkaW80aTBm , o2life:Basic bHk4cnF1OnpiMGxsazAy

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean saveSharedData(Context context, String key, String value) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public static String getShareData(Context context, String key) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, "");
    }

    public static boolean clearData(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.clear();
        editor.commit();
        return true;
    }

    public static Typeface font;

    public static void setTextFontsBold(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIText-Bold.otf");
        textView.setTypeface(font);
    }

    public static void setTextFontsMedium(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIText-Medium.otf");
        textView.setTypeface(font);
    }

    public static void setTextFontsRegular(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIText-Regular.otf");
        textView.setTypeface(font);
    }

    public static void setTextFontsSemibold(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIText-Semibold.otf");
        textView.setTypeface(font);
    }

    public static void setDisplayFontsBold(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Bold.otf");
        textView.setTypeface(font);
    }

    public static void setDisplayFontsRegular(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Regular.otf");
        textView.setTypeface(font);
    }

    public static void setDisplayFontsSemibold(Context context, TextView textView) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Semibold.otf");
        textView.setTypeface(font);
    }

    public static HttpPost postData(String URL, final List<NameValuePair> params, final Handler handler) {
        // Create a new HttpClient and Post Header
        //android.util.Log.d("Utilities", "Called postData");
        Log.e("Param", params.toString());
        Log.e("Url", URL);
        final HttpClient httpclient = new DefaultHttpClient();
        //httpclient.
        final HttpPost httppost = new HttpPost(URL);
        final Message msg = new Message();
        final Bundle dataBundle = new Bundle();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        new Thread() {
            @Override
            public void run() {
                String error = "";
                String data = "";
                try {
                    //add header
                    httppost.setHeader("Authorization",Authorization);

                    httppost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
                    HttpResponse response = httpclient.execute(httppost);
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        response.getEntity().writeTo(out);
                        out.close();
                        data = out.toString();
                    } else {
                        error = EntityUtils.toString(response.getEntity());
                    }
                } catch (ClientProtocolException e) {

                    error = e.toString();
                } catch (IOException e) {

                    error = e.toString();
                } catch (Exception ex) {

                    error = ex.toString();
                }


//                Log.e("Response", data.toString());
//                Log.e("Error", error.toString());
                dataBundle.putString("error", error);
                dataBundle.putString("data", data);
                msg.setData(dataBundle);
                handler.dispatchMessage(msg);
            }
        }.start();
        return httppost;
    }

    public static HttpGet getData(final String URL, final Handler handler) {
        // Create a new HttpClient and GET Header
        Log.e("Url", URL);
        final HttpClient httpclient = new DefaultHttpClient();
        final Message msg = new Message();
        final Bundle dataBundle = new Bundle();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final HttpGet httpGet = new HttpGet(URL);
        //add header
        httpGet.addHeader("Authorization",Authorization);

        new Thread() {
            @Override
            public void run() {
                String error = "";
                String data = "";
                try {
                    HttpResponse response = httpclient.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        response.getEntity().writeTo(out);
                        out.close();
                        data = out.toString();
                    } else {
                        error = EntityUtils.toString(response.getEntity());
                    }
                } catch (ClientProtocolException e) {
                    error = e.toString();
                } catch (IOException e) {
                    error = e.toString();
                } catch (Exception ex) {
                    error = ex.toString();
                }
                dataBundle.putString("error", error);
                dataBundle.putString("data", data);

//                Log.e("DATA", "" + data);
//                Log.e("ERROR", "" + error);
                msg.setData(dataBundle);
                handler.dispatchMessage(msg);
            }
        }.start();
        return httpGet;
    }

    public static void ShowErrorMessage(final String title, final String message, final Context context) {
        //AirbrakeNotifier.register(context, "Title: " + title + ", Message: " + message);
        //Throwable throwable = new Exception
        //AirbrakeNotifier.notify();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // set title
                alertDialogBuilder.setTitle(title);

                // set dialog message
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                //VC_MainActivity.this.finish();

//                                ((Activity) context).finish();

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    public static void ShowFbErrorMessage(final String title, final String message, final Context context) {
        //AirbrakeNotifier.register(context, "Title: " + title + ", Message: " + message);
        //Throwable throwable = new Exception
        //AirbrakeNotifier.notify();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // set title
                alertDialogBuilder.setTitle(title);

                // set dialog message
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                //VC_MainActivity.this.finish();

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    public static int[] GetScreenDimensions(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int width = 0;
        int height = 0;
        try {
            display.getSize(size);
            width = size.x;
            height = size.y;
        } catch (NoSuchMethodError ex) {
            width = display.getWidth();
            height = display.getHeight();
        }
        return new int[]{width, height};
    }

    public static String serverDate;
    public static TimeZone timeZone;

    public static String getDateCurrentTimeZone1(String a) {

        String temp = a.replace("T", " ").replace("Z", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        // timeZone = TimeZone.getDefault();

        //  timeZone = TimeZone.getTimeZone("America/New_York");
        //  format.setTimeZone(timeZone);
        //  String currentDateandTime = format.format(new Date());
        // String currentDateandTime = format.format(serverDate.replace("T"," ").replace("Z",""));


        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = format.parse(temp);
            dt2 = format.parse(serverDate.replace("T", " ").replace("Z", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long diff = dt2.getTime() - dt1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        //  long a = diffHours%24;
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(dt1);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(dt2);

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

//        Log.i("DateDay",diffInDays + "-" + diffHours + "-" + diffMinutes);

//        if (diffInDays >= 5) {
//
//            String temp1[] = format1.format(dt1).split(" ");
//            return "" + temp1[0];
//
//        }
        if (diffInDays > 365) {

            return "" + diffYear + "yr";
        }
        else if (diffInDays > 30) {

            return "" + diffMonth + "mn";
        }
        else if (diffInDays > 21 && diffInDays <= 30) {

            return "" + "4w";
        }
        else if (diffInDays > 14 && diffInDays <= 21) {

            return "" + "3w";
        }
        else if (diffInDays > 7 && diffInDays <= 14) {

            return "" + "2w";
        }
        else if (diffInDays > 0 && diffInDays <= 7) {

            return "" + diffInDays + "d";
        }
//        else if (diffInDays <= 30) {
//
//            return "" + diffInDays + "d";
//        }
        else if (diffInDays > 0 && diffHours > 24) {

            return "" + diffInDays + "d";
        }
        else if (diffHours > 0 && diffHours <= 24) {

            return "" + diffHours + "h";
        } else if (diffMinutes > 0) {

            return "" + diffMinutes + "m";
        } else if(diffSeconds == 0){

            return "now";
        }else {
            return "" + diffSeconds + "s";
        }
    }
}
