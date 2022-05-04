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
                        data.clear();
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
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(attendanceURL);
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
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();

            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "출결조회 요청 실패!");
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

            if (jsonResponse.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("LIST");
                int count = Integer.parseInt(jsonResponse.get("COUNT").toString());

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
        } catch (IOException | JSONException | NullPointerException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    public ArrayList<ArrayList<String>> getDetailAttendance(String token, String id, String year, String term, String CD, String NUMB) {
        ArrayList<ArrayList<String>> subArray = new ArrayList<>();
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(attendanceDURL);
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
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();

            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "출결세부조회 요청 실패!");
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

            if (jsonResponse.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("LIST");
                int count = Integer.parseInt(jsonResponse.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(jsonArray.getJSONObject(i).get("CHECK_DATE_NM").toString());
                    tmp.add(jsonArray.getJSONObject(i).get("WEEK_TIME").toString());
                    tmp.add(jsonArray.getJSONObject(i).get("ABSN_TIME").toString());
                    subArray.add(tmp);
                }
            }
            return subArray;
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
            return null;
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
