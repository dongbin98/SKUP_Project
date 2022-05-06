package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {
    private static final String attendanceURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String attendanceDURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token;
    String id;
    String year;
    String term;

    Spinner attendanceYearSpinner;
    Spinner attendanceTermSpinner;
    Button attendanceSearchBtn;
    RecyclerView attendanceList;

    List<AttendanceAdapter.AttendanceItem> data = new ArrayList<>();
    AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_form);

        Intent intent = getIntent();
        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        attendanceYearSpinner = (Spinner) findViewById(R.id.attendanceYearSpinner);
        attendanceTermSpinner = (Spinner) findViewById(R.id.attendanceTermSpinner);
        attendanceSearchBtn = (Button) findViewById(R.id.attendanceSearchBtn);
        attendanceList = (RecyclerView) findViewById(R.id.attendanceList);

        ArrayList<String> years = ((userClass) getApplication()).getYearlist();
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("1학기");
        terms.add("2학기");
        terms.add("여름계절학기");
        terms.add("겨울계절학기");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, terms);

        adapter = new AttendanceAdapter(data);

        attendanceList.setLayoutManager(new LinearLayoutManager(this));
        attendanceList.setAdapter(adapter);

        attendanceYearSpinner.setAdapter(yearAdapter);
        attendanceYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = ((userClass) getApplication()).getSchYear();
            }
        });

        attendanceTermSpinner.setAdapter(termAdapter);
        attendanceTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                term = Integer.toString(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                term = "1";
            }
        });

        attendanceSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.dataClear();
                        attendanceSearchBtn.setClickable(false);
                        if(getAttendance(token, id, year, term))
                            attendanceSearchBtn.setClickable(true);
                        // 쓰레드 안에서 UI 변경 시 필요
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.dataClear();
                attendanceSearchBtn.setClickable(false);
                if(getAttendance(token, id, year, term))
                    attendanceSearchBtn.setClickable(true);
                // 쓰레드 안에서 UI 변경 시 필요
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public boolean getAttendance(String token, String id, String year, String term) {
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("ID", id);
            parameter.put("LECT_YEAR", year);
            parameter.put("LECT_TERM", term);
            parameter.put("STNT_NUMB", id);
            try {
                payload.put("MAP_ID", "education.ual.UAL_04004_T.select");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "UAL_04004_T");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(attendanceURL, token, payload);
            System.out.println(response.toString());

            if (response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");
                int count = Integer.parseInt(response.get("COUNT").toString());


                for (int i = 0; i < count; i++) {
                    data.add(new AttendanceAdapter.AttendanceItem(AttendanceAdapter.HEADER,
                            jsonArray.getJSONObject(i).get("SUBJ_NM").toString(),
                            jsonArray.getJSONObject(i).get("PROF_NM").toString(),
                            jsonArray.getJSONObject(i).get("SUBJ_CD").toString(),
                            getDetailAttendance(token, id, year, term,
                                    jsonArray.getJSONObject(i).get("SUBJ_CD").toString(),
                                    jsonArray.getJSONObject(i).get("CLSS_NUMB").toString())));
                }
            }
        } catch (JSONException | NullPointerException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    public ArrayList<ArrayList<String>> getDetailAttendance(String token, String id, String year, String term, String CD, String NUMB) {
        ArrayList<ArrayList<String>> subArray = new ArrayList<>();
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("CLSS_NUMB", NUMB);
            parameter.put("LECT_YEAR", year);
            parameter.put("LECT_TERM", term);
            parameter.put("STNT_NUMB", id);
            parameter.put("SUBJ_CD", CD);
            try {
                payload.put("MAP_ID", "education.ual.UAL_04004_T.select_attend_pop");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "UAL_04004_T");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(attendanceDURL, token, payload);
            System.out.println(response.toString());

            if (response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(jsonArray.getJSONObject(i).get("CHECK_DATE_NM").toString());
                    tmp.add(jsonArray.getJSONObject(i).get("WEEK_TIME").toString());
                    tmp.add(jsonArray.getJSONObject(i).get("ABSN_TIME").toString());
                    subArray.add(tmp);
                }
            }
            return subArray;
        } catch (JSONException | NullPointerException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
