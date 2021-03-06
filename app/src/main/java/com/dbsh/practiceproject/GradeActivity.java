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
                // ????????? ????????? UI ?????? ??? ??????
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ??? ?????? view update
                        applyInfo.setText(applyLIST);
                        acquireInfo.setText(acquireLIST);
                        // ?????? ?????? view update
                        for(int i = 0; i < majorList.size(); i++) {
                            TableRow tableRow = new TableRow(GradeActivity.this);
                            tableRow.setBackground(getDrawable(R.drawable.tableframe));
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < majorList.get(i).size(); j++) {
                                TextView textView = new TextView(GradeActivity.this);
                                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                textView.setText(majorList.get(i).get(j));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(getColor(R.color.black));
                                tableRow.addView(textView);
                            }
                            majorTable.addView(tableRow);
                        }
                        // ?????? ?????? view update
                        for(int i = 0; i < liberalList.size(); i++) {
                            TableRow tableRow = new TableRow(GradeActivity.this);
                            tableRow.setBackground(getDrawable(R.drawable.tableframe));
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < liberalList.get(i).size(); j++) {
                                TextView textView = new TextView(GradeActivity.this);
                                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                textView.setText(liberalList.get(i).get(j));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(getColor(R.color.black));
                                tableRow.addView(textView);
                            }
                            liberalTable.addView(tableRow);
                        }
                        // ?????? ?????? view update
                        for(int i = 0; i < etcList.size(); i++) {
                            TableRow tableRow = new TableRow(GradeActivity.this);
                            tableRow.setBackground(getDrawable(R.drawable.tableframe));
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < etcList.get(i).size(); j++) {
                                TextView textView = new TextView(GradeActivity.this);
                                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                textView.setText(etcList.get(i).get(j));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(getColor(R.color.black));
                                tableRow.addView(textView);
                            }
                            etcTable.addView(tableRow);
                        }
                        // ?????? ??? ?????? view update
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        int widthPixels = metrics.widthPixels * 8/10;

                        ConstraintLayout.LayoutParams layoutParams =
                                new ConstraintLayout.LayoutParams(widthPixels, ConstraintLayout.LayoutParams.WRAP_CONTENT
                                );
                        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.setMargins(50, 20, 50, 20);

                        // ????????? ????????? ?????? listview ?????? ?????????
                        if(majorList.size() == (Math.max(majorList.size(), liberalList.size()))) {
                            if(majorList.size() == (Math.max(majorList.size(), etcList.size()))) {
                                // ?????? ?????? ???????????? ?????? ??? ??????
                                layoutParams.topToBottom = majorTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                            else {
                                // ?????? ?????? ???????????? ?????? ??? ??????
                                layoutParams.topToBottom = etcTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                        }
                        else {
                            if(liberalList.size() == (Math.max(liberalList.size(), etcList.size()))) {
                                // ?????? ?????? ???????????? ?????? ??? ??????
                                System.out.println("?????? ????????? ??????");
                                layoutParams.topToBottom = liberalTable.getId();
                                listview.setLayoutParams(layoutParams);
                            }
                            else {
                                // ?????? ?????? ???????????? ?????? ??? ??????
                                System.out.println("?????? ????????? ??????");
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

    // ??? ???????????? ??????
    public void getCredit(String token, String id) {
        try {
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
            JSONObject response = Connector.getInstance().getResponse(gradeURL, token, payload);
            System.out.println(response.toString());

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                // ??? ????????????
                applyLIST = jsonArray.getJSONObject(0).get("PNT_TOT").toString();
                // ??? ????????????
                acquireLIST = jsonArray.getJSONObject(1).get("PNT_TOT").toString();

                // ------------------------ ?????? ????????? ------------------------
                // [00 : ????????????], [32 : ????????????], [33 : ????????????], [34 : ????????????]
                // [10 : ????????????], [11 : ????????????], [15 : ????????????]
                // [20 : ????????????], [23 : ??????   ], ???????????? ??????           PTN_??????
                // -----------------------------------------------------------


                String tmp, tmp2;
                ArrayList<String> tmpList = new ArrayList<>();
                // ?????? ??????
                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_23").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_23").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    majorList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_20").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_20").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    majorList.add(tmpList);
                tmpList = new ArrayList<>();

                // ?????? ??????
                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_11").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_11").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    liberalList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_15").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_15").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    liberalList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_10").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_10").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    liberalList.add(tmpList);
                tmpList = new ArrayList<>();

                // ?????? ??????
                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_32").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_32").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_33").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_33").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("??????");
                if (!(tmp = jsonArray.getJSONObject(0).get("PNT_34").toString()).equals("0"))
                    tmpList.add(tmp);
                if (!(tmp2 = jsonArray.getJSONObject(1).get("PNT_34").toString()).equals("0"))
                    tmpList.add(tmp2);
                if (tmpList.size() > 1)
                    etcList.add(tmpList);
                tmpList = new ArrayList<>();

                tmpList.add("??????");
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

        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
    // ?????? ??? ?????? ??????
    public void getEachGrade(String token, String id) {
        try {
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
            JSONObject response = Connector.getInstance().getResponse(gradeURL, token, payload);
            System.out.println(response.toString());

            if (response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                // ????????? ?????? ???
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    String year = jsonArray.getJSONObject(i).get("SCH_YEAR").toString();
                    String term = jsonArray.getJSONObject(i).get("SCH_TERM").toString();

                    ScoreParent scoreparent = new ScoreParent(
                            year + "?????? - " + term + "??????",
                            " ??????/?????? ?????? : " + jsonArray.getJSONObject(i).get("ACQU_PNT").toString() + "/" + jsonArray.getJSONObject(i).get("APPLY_PNT").toString(),
                            " ?????? ?????? : " + jsonArray.getJSONObject(i).get("GRD_MARK_AVG").toString(),
                            " ?????? : " + jsonArray.getJSONObject(i).get("SCH_RANK").toString()
                    );
                    groupList.add(i, scoreparent);

                    getEachSubjectGrade(year, term, token, id, i);
                }
            }

        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
    // ?????? ??? ?????? ??? ?????? ??????
    public void getEachSubjectGrade(String year, String term, String token, String id, int Position) {
        try {
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
            JSONObject response = Connector.getInstance().getResponse(gradeURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                // ????????? ?????? ???
                int count = Integer.parseInt(response.get("COUNT").toString());

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
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
