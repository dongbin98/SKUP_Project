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
        terms.add("1학기");   terms.add("2학기"); terms.add("여름계절학기"); terms.add("겨울계절학기");

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
                        getMajorpotenInfo(token, id, year, term);
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
                getMajorpotenInfo(token, id, year, term);
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

    public void getMajorpotenInfo(String token, String id, String year, String term) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(majorPotenURL);
            connection = (HttpURLConnection) url.openConnection();
            // Bearer
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setConnectTimeout(10000);// 연결 대기 시간 설정
            connection.setRequestMethod(POST);  // 전송 방식 POST
            connection.setDoInput(true);        // InputStream으로 서버로부터 응답받음
            connection.setDoOutput(true);       // OutputStream으로 POST데이터를 넘겨줌
            // 서버 Response를 JSON 형식의 타입으로 요청
            connection.setRequestProperty("Accept", "application/json");
            // 서버에게 Request Body 전달 시 application/json으로 서버에 전달
            connection.setRequestProperty("content-type", "application/json");
            // ------------
            // 서버로 값 요청(이수학점)
            // ------------
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
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();

            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "전공역량강화프로그램 요청 실패!");
            // --------------
            // 서버에서 전송받기
            // --------------
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            os.close();

            JSONObject jsonResponse = new JSONObject(sb.toString());
            System.out.println(jsonResponse.toString());

            if(jsonResponse.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("LIST");
                int count = Integer.parseInt(jsonResponse.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    majorpotenProList.add("프로그램명 : " + jsonArray.getJSONObject(i).get("EVNT_NAME").toString());
                    majorpotenDateList.add("신청기간 : " + jsonArray.getJSONObject(i).get("ACPT_DATE").toString());
                    majorpotenPeopleList.add("신청인원/총인원 : " + jsonArray.getJSONObject(i).get("SINCHEONG_CNT").toString());
                    majorpotenAddrList.add("장소 : " + jsonArray.getJSONObject(i).get("EVNT_ADDR").toString());
                    majorpotenEvntDateTimeList.add(jsonArray.getJSONObject(i).get("EVNT_ADDR").toString());
                    majorpotenRoomList.add(jsonArray.getJSONObject(i).get("ROOM1NM").toString());
                    majorpotenDaysList.add(jsonArray.getJSONObject(i).get("EVNT_DAYS").toString());
                    majorpotenOnlineList.add(jsonArray.getJSONObject(i).get("ONLINE").toString());
                    majorpotenEvntDateList.add(jsonArray.getJSONObject(i).get("EVNT_DATE").toString());
                    majorpotenTimeList.add(jsonArray.getJSONObject(i).get("EVNT_TIME_RATE").toString());
                }
            }

        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
