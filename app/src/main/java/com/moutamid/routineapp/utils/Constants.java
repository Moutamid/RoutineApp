package com.moutamid.routineapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.models.AddStepsChildModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    static Dialog dialog;
    public static final String DATEFORMATE = "dd/MM/yyyy";
    public static final String USER = "users";
    public static final String Steps = "Steps";
    public static final String Routines = "Routines";
    public static final String STEPS_LIST = "STEPS_LIST";

    public static String getFormatedDate(long date){
        return new SimpleDateFormat(DATEFORMATE, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static List<Integer> extractTimeValues(ArrayList<AddStepsChildModel> timeStrings) {
        List<Integer> timeValues = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+) min");

        for (AddStepsChildModel timeString : timeStrings) {
            Matcher matcher = pattern.matcher(timeString.getTime());
            if (matcher.find()) {
                int value = Integer.parseInt(matcher.group(1));
                timeValues.add(value);
            }
        }

        return timeValues;
    }

    public static String convertMinutesToHHMM(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        return String.format("%02d:%02d", hours, remainingMinutes);
    }


    public static void showDialog(){
        dialog.show();
    }

    public static void dismissDialog(){
        dialog.dismiss();
    }

    public static void checkApp(Activity activity) {
        String appName = "routineApp";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }
    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("routineApp");
        db.keepSynced(true);
        return db;
    }
}
