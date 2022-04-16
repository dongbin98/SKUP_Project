package com.dbsh.practiceproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GradeActivity extends AppCompatActivity {

    private static final String gradeURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String applyLIST, acquireLIST;

    TextView applyInfo;
    TextView acquireInfo;

    TableLayout majorTable;
    TableLayout liberalTable;
    TableLayout etcTable;

    ExpandableListView listview;
    ExListviewAdapter adapter;
    ArrayList<ScoreParent> groupList;
    ArrayList<ArrayList<ScoreChild>> childList;
    ArrayList<ArrayList<String>> majorList;
    ArrayList<ArrayList<String>> liberalList;
    ArrayList<ArrayList<String>> etcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_form);

        applyInfo = (TextView) findViewById(R.id.applyInfo);
        acquireInfo = (TextView) findViewById(R.id.acquireInfo);
        majorTable = (TableLayout) findViewById(R.id.MajorTable);
        liberalTable = (TableLayout) findViewById(R.id.LiberalTable);
        etcTable = (TableLayout) findViewById(R.id.EtcTable);
        listview = (ExpandableListView) findViewById(R.id.exlistview);
        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        majorList = new ArrayList<>();
        liberalList = new ArrayList<>();
        etcList = new ArrayList<>();

        adapter = new ExListviewAdapter();
        adapter.scoreParent = groupList;
        adapter.scoreChild = childList;

        listview.setAdapter(adapter);
        listview.setGroupIndicator(null);

        Intent intent = getIntent();
        String token = ((userClass) getApplication()).getToken();
        String id = ((userClass) getApplication()).getId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getCredit(token, id);
                getEachGrade(token, id);
                // 쓰레드 안에서 UI 변경 시 필요
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 총 학점 view update
                        applyInfo.setText(applyLIST);
                        acquireInfo.setText(acquireLIST);
                        // 전공 학점 view update
                        for(int i = 0; i < majorList.size(); i++) {
                            TableRow tableRow = new TableRow(GradeActivity.this);
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < majorList.get(i).size(); j++) {
                                TextView textView = new TextView(GradeActivity.this);
                                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                textView.setText(majorList.get(i).get(j));
                                textView.setBackground(getDrawable(R.drawable.tableframe));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(Color.BLACK);
                                tableRow.addView(textView);
                            }
                            majorTable.addView(tableRow);
                        }
                        // 교양 학점 view update
                        for(int i = 0; i < liberalList.size(); i++) {
                            TableRow tableRow = new TableRow(GradeActivity.this);
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < liberalList.get(i).size(); j++) {
                                TextView textView = new TextView(GradeActivity.this);
                                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                textView.setText(liberalList.get(i).get(j));
                                textView.setBackground(getDrawable(R.drawable.tableframe));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(Color.BLACK);
                                tableRow.addView(textView);
                            }
                            liberalTable.addView(tableRow);
                        }
                        // 기타 학점 view update
                        for(int i = 0; i < etcList.size(); i++) {
                            TableRow tableRow = new TableRow(GradeActivity.this);
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < etcList.get(i).size(); j++) {
                                TextView textView = new TextView(GradeActivity.this);
                                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                textView.setText(etcList.get(i).get(j));
                                textView.setBackground(getDrawable(R.drawable.tableframe));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(Color.BLACK);
                                tableRow.addView(textView);
                            }
                            etcTable.addView(tableRow);
                        }
                        // 학기 별 성적 view update
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        int widthPixels = metrics.widthPixels * 8/10;

                        ConstraintLayout.LayoutParams layoutParams =
                                new ConstraintLayout.LayoutParams(widthPixels, ConstraintLayout.LayoutParams.WRAP_CONTENT
                                );
                        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.setMargins(50, 20, 50, 20);

                        // 테이블 높이에 맞게 listview 위치 정하기
                        if(majorList.size() == (Math.max(majorList.size(), liberalList.size()))) {
                            if(majorList.size() == (Math.max(majorList.size(), etcList.size()))) {
                                // 전공 과목 테이블이 가장 큰 경우
                                layoutParams.topToBottom = majorTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                            else {
                                // 기타 과목 테이블이 가장 큰 경우
                                layoutParams.topToBottom = etcTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                        }
                        else {
                            if(liberalList.size() == (Math.max(liberalList.size(), etcList.size()))) {
                                // 교양 과목 테이블이 가장 큰 경우
                                System.out.println("교양 테이블 기준");
                                layoutParams.topToBottom = liberalTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                            else {
                                // 기타 과목 테이블이 가장 큰 경우
                                System.out.println("기타 테이블 기준");
                                layoutParams.topToBottom = etcTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    // 총 이수학점 로드
    public void getCredit(String token, String id) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(gradeURL);
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
            try {
                payload.put("MAP_ID", "education.usc.USC_09001_V.select02");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "USC_09001_V");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();

            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "이수학점 요청 실패!");
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
            if(jsonResponse.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("LIST");

                // 총 신청학점
                applyLIST = jsonArray.getJSONObject(0).get("PNT_TOT").toString();
                // 총 취득학점
                acquireLIST = jsonArray.getJSONObject(1).get("PNT_TOT").toString();

                // ------------------------ 학점 데이터 ------------------------
                // [00 : 기타합계], [32 : 복수전공], [33 : 일반선택], [34 : 자유선택]
                // [10 : 교양합계], [11 : 교양필수], [15 : 교양선택]
                // [20 : 전공합계], [23 : 전공   ], 전공심화 실종           PTN_숫자
                // -----------------------------------------------------------


                String tmp, tmp2;
                ArrayList<String> tmpList = new ArrayList<>();
                // 전공 학점
                tmpList.add("전공");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_23").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_23").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    majorList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("합계");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_20").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_20").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    majorList.add(tmpList);
                tmpList = new ArrayList<>();

                // 교양 학점
                tmpList.add("교필");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_11").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_11").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    liberalList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("교선");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_15").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_15").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    liberalList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("합계");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_10").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_10").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    liberalList.add(tmpList);
                tmpList = new ArrayList<>();

                // 기타 학점
                tmpList.add("복전");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_32").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_32").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("일반");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_33").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_33").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("자선");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_34").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_34").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("합계");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_00").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_00").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);

                System.out.println(majorList);
                System.out.println(liberalList);
                System.out.println(etcList);
            }

        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
    // 학기 별 성적 로드
    public void getEachGrade(String token, String id) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(gradeURL);
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
            // 서버로 값 요청(전체성적)
            // ------------
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("ID", id);
            parameter.put("STU_NO", id);
            try {
                payload.put("MAP_ID", "education.usc.USC_09001_V.select");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "USC_09001_V");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "학기 별 성적 요청 실패!");
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
            if (jsonResponse.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("LIST");

                // 이수한 학기 수
                int count = Integer.parseInt(jsonResponse.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    String year = jsonArray.getJSONObject(i).get("SCH_YEAR").toString();
                    String term = jsonArray.getJSONObject(i).get("SCH_TERM").toString();

                    ScoreParent scoreparent = new ScoreParent(
                            year + "년도 - " + term + "학기",
                            " 신청/취득 학점 : " + jsonArray.getJSONObject(i).get("ACQU_PNT").toString() + "/" + jsonArray.getJSONObject(i).get("APPLY_PNT").toString(),
                            " 평점 평균 : " + jsonArray.getJSONObject(i).get("GRD_MARK_AVG").toString(),
                            " 석차 : " + jsonArray.getJSONObject(i).get("SCH_RANK").toString()
                    );
                    groupList.add(i, scoreparent);

                    getEachSubjectGrade(year, term, token, id, i);
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
    // 학기 별 과목 당 성적 로드
    public void getEachSubjectGrade(String year, String term, String token, String id, int Position) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(gradeURL);
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
            // 서버로 값 요청(전체성적)
            // ------------
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("SCH_YEAR", year);
            parameter.put("SCH_TERM", term);
            parameter.put("ID", id);
            parameter.put("STU_NO", id);
            try {
                payload.put("MAP_ID", "education.usc.USC_09001_V.select_sub");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "USC_09001_V");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "학기 별 과목 당 성적 요청 실패!");
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
            if(jsonResponse.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = jsonResponse.getJSONArray("LIST");

                // 이수한 학기 수
                int count = Integer.parseInt(jsonResponse.get("COUNT").toString());

                ArrayList<ScoreChild> tmp = new ArrayList<ScoreChild>();

                for (int i = 0; i < count; i++) {
                    ScoreChild scorechild = new ScoreChild(
                            jsonArray.getJSONObject(i).get("SUBJ_NM").toString(),
                            jsonArray.getJSONObject(i).get("GRD_MARK").toString(),
                            jsonArray.getJSONObject(i).get("GRD").toString(),
                            jsonArray.getJSONObject(i).get("CMPT_DIV_NM").toString(),
                            jsonArray.getJSONObject(i).get("PNT").toString(),
                            jsonArray.getJSONObject(i).get("SUBJ_NO").toString(),
                            jsonArray.getJSONObject(i).get("PROF1_NM").toString());

                    tmp.add(scorechild);
                }
                childList.add(Position, tmp);
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
