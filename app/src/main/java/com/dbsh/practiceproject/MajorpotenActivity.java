package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MajorpotenActivity extends AppCompatActivity {

    private static final String majorPotenURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token;
    String id;
    String year;
    String term;

    ArrayList<String> majorpotenProList;
    ArrayList<String> majorpotenDateList;
    ArrayList<String> majorpotenPeopleList;
    ArrayList<String> majorpotenAddrList;
    ArrayList<String> majorpotenEvntDateTimeList;
    ArrayList<String> majorpotenRoomList;
    ArrayList<String> majorpotenDaysList;
    ArrayList<String> majorpotenOnlineList;
    ArrayList<String> majorpotenEvntDateList;
    ArrayList<String> majorpotenTimeList;

    Spinner majorpotenYearSpinner;
    Spinner majorpotenTermSpinner;
    Button majorpotenSearchBtn;
    RecyclerView majorpotenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.majorpoten_form);

        Intent intent = getIntent();
        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        majorpotenProList = new ArrayList<>();
        majorpotenDateList = new ArrayList<>();
        majorpotenPeopleList = new ArrayList<>();
        majorpotenAddrList = new ArrayList<>();
        majorpotenEvntDateTimeList = new ArrayList<>();
        majorpotenRoomList = new ArrayList<>();
        majorpotenDaysList = new ArrayList<>();
        majorpotenOnlineList = new ArrayList<>();
        majorpotenEvntDateList = new ArrayList<>();
        majorpotenTimeList = new ArrayList<>();

        majorpotenYearSpinner = (Spinner) findViewById(R.id.majorpotenYearSpinner);
        majorpotenTermSpinner = (Spinner) findViewById(R.id.majorpotenTermSpinner);
        majorpotenSearchBtn = (Button) findViewById(R.id.majorpotenSearchBtn);
        majorpotenList = (RecyclerView) findViewById(R.id.majorpotenList);

        ArrayList<String> years = ((userClass) getApplication()).getYearlist();
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("1??????");   terms.add("2??????"); terms.add("??????????????????"); terms.add("??????????????????");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, terms);

        PotenAdapter adapter = new PotenAdapter(
                majorpotenProList,
                majorpotenDateList,
                majorpotenPeopleList,
                majorpotenAddrList,
                majorpotenEvntDateTimeList,
                majorpotenRoomList,
                majorpotenDaysList,
                majorpotenOnlineList,
                majorpotenEvntDateList,
                majorpotenTimeList,
                2);

        majorpotenList.setLayoutManager(new LinearLayoutManager(this));
        majorpotenList.setAdapter(adapter);

        majorpotenYearSpinner.setAdapter(yearAdapter);
        majorpotenYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = ((userClass) getApplication()).getSchYear();
            }
        });

        majorpotenTermSpinner.setAdapter(termAdapter);
        majorpotenTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                term = Integer.toString(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                term = "1";
            }
        });

        majorpotenSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        majorpotenProList.clear();
                        majorpotenDateList.clear();
                        majorpotenPeopleList.clear();
                        majorpotenAddrList.clear();
                        majorpotenEvntDateTimeList.clear();
                        majorpotenRoomList.clear();
                        majorpotenDaysList.clear();
                        majorpotenOnlineList.clear();
                        majorpotenEvntDateList.clear();
                        majorpotenTimeList.clear();
                        majorpotenSearchBtn.setClickable(false);
                        if(getMajorpotenInfo(token, id, year, term))
                            majorpotenSearchBtn.setClickable(true);
                        // ????????? ????????? UI ?????? ??? ??????
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
                majorpotenSearchBtn.setClickable(false);
                if(getMajorpotenInfo(token, id, year, term))
                    majorpotenSearchBtn.setClickable(true);
                // ????????? ????????? UI ?????? ??? ??????
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public boolean getMajorpotenInfo(String token, String id, String year, String term) {
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("ID", id);
            parameter.put("STU_NO", id);
            parameter.put("CENTER_GUBN", "");
            parameter.put("EVNT_GUBN", "");
            parameter.put("EVNT_NAME", "");
            parameter.put("LECT_YEAR", year);
            parameter.put("LECT_TERM", term);
            try {
                payload.put("MAP_ID", "education.uem.UEM_05105_T.SELECT_UEM_MAJ_CAPACITY_APP");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "UEM_05105_T");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }

            JSONObject response = Connector.getInstance().getResponse(majorPotenURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    majorpotenProList.add("??????????????? : " + jsonArray.getJSONObject(i).get("EVNT_NAME").toString());
                    majorpotenDateList.add("???????????? : " + jsonArray.getJSONObject(i).get("ACPT_DATE").toString());
                    majorpotenPeopleList.add("????????????/????????? : " + jsonArray.getJSONObject(i).get("SINCHEONG_CNT").toString());
                    majorpotenAddrList.add("?????? : " + jsonArray.getJSONObject(i).get("EVNT_ADDR").toString());
                    majorpotenEvntDateTimeList.add(jsonArray.getJSONObject(i).get("EVNT_ADDR").toString());
                    majorpotenRoomList.add(jsonArray.getJSONObject(i).get("ROOM1NM").toString());
                    majorpotenDaysList.add(jsonArray.getJSONObject(i).get("EVNT_DAYS").toString());
                    majorpotenOnlineList.add(jsonArray.getJSONObject(i).get("ONLINE").toString());
                    majorpotenEvntDateList.add(jsonArray.getJSONObject(i).get("EVNT_DATE").toString());
                    majorpotenTimeList.add(jsonArray.getJSONObject(i).get("EVNT_TIME_RATE").toString());
                }
            }

        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return true;
    }
}
