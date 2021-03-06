package com.dbsh.practiceproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;

public class TimetableActivity extends AppCompatActivity {
    private static final String timetableURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token;
    String id;
    String year;
    String term;

    ArrayList<String> weekList;
    ArrayList<String> todayList;

    TableLayout timetableWeekTable;
    TableLayout timetableTodayTable;
    TextView timetableText2;

    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_form);

        Intent intent = getIntent();
        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        weekList = new ArrayList<>();
        todayList = new ArrayList<>();

        timetableWeekTable = (TableLayout) findViewById(R.id.timetableWeekTable);
        timetableTodayTable = (TableLayout) findViewById(R.id.timetableTodayTable);
        timetableText2 = (TextView) findViewById(R.id.timetableText2);

        System.out.println(cal.get(Calendar.DAY_OF_WEEK) - 1);
        switch (cal.get(Calendar.DAY_OF_WEEK) - 1) {
            case 1:
                timetableText2.setText("????????? ?????????");
                break;
            case 2:
                timetableText2.setText("????????? ?????????");
                break;
            case 3:
                timetableText2.setText("????????? ?????????");
                break;
            case 4:
                timetableText2.setText("????????? ?????????");
                break;
            case 5:
                timetableText2.setText("????????? ?????????");
                break;
            default:
                timetableText2.setText("?????? ?????????");
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getTimetable(token, id, year, term);
                // ????????? ????????? UI ?????? ??? ??????
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        for(int i = 0; i < weekList.size(); i++) {
                            TableRow tableRow = new TableRow(TimetableActivity.this);
                            tableRow.setLayoutParams(params);
                            TextView textView = new TextView(TimetableActivity.this);
                            textView.setText(weekList.get(i));
                            textView.setTextColor(getColor(R.color.black));
                            textView.setPadding(0,0,0,20);
                            tableRow.addView(textView);
                            timetableWeekTable.addView(tableRow);
                        }
                        for(int i = 0; i < todayList.size(); i++) {
                            TableRow tableRow = new TableRow(TimetableActivity.this);
                            tableRow.setLayoutParams(params);
                            TextView textView = new TextView(TimetableActivity.this);
                            textView.setText(todayList.get(i));
                            textView.setTextColor(getColor(R.color.black));
                            textView.setPadding(0,0,0,20);
                            tableRow.addView(textView);
                            timetableTodayTable.addView(tableRow);
                        }
                    }
                });
            }
        }).start();
    }
    public void getTimetable(String token, String id, String year, String term) {
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("LECT_YEAR", year);
            parameter.put("LECT_TERM", term);
            parameter.put("ID", id);
            parameter.put("STU_NO", id);
            try {
                payload.put("MAP_ID", "education.ucs.UCS_common.SELECT_TIMETABLE_2018");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "UAL_03333_T");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }

            JSONObject response = Connector.getInstance().getResponse(timetableURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    if (Integer.parseInt(jsonArray.getJSONObject(i).get("CLASSDAY").toString()) == cal.get(Calendar.DAY_OF_WEEK) - 1) {
                        todayList.add(jsonArray.getJSONObject(i).get("CLASSHOUR_START_TIME").toString() + " ~ " +
                                jsonArray.getJSONObject(i).get("CLASSHOUR_END_TIME").toString() + "   " +
                                jsonArray.getJSONObject(i).get("CLASS_NAME").toString());
                    }
                    weekList.add(jsonArray.getJSONObject(i).get("CLASSHOUR_START_TIME").toString() + " ~ " +
                            jsonArray.getJSONObject(i).get("CLASSHOUR_END_TIME").toString() + "   " +
                            jsonArray.getJSONObject(i).get("CLASS_NAME").toString());
                }
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
