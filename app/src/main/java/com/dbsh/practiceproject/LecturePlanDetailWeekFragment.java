package com.dbsh.practiceproject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class LecturePlanDetailWeekFragment extends Fragment {
    private static final String bookInfoURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";     // 주차별, 교재
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token, id, year, term, cd, cn, pi, sn;           // 토큰, 학번, 년, 학기, 학수번호, 분반, 교수명, 과목명
    TableLayout lectureplan_detail_week_table;
    TextView lectureplan_detail_week_subjectName;
    ArrayList<ArrayList<String>> tableList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.lectureplan_detail_week_form, container, false);

        lectureplan_detail_week_table = rootView.findViewById(R.id.lectureplan_detail_week_table);
        lectureplan_detail_week_subjectName = rootView.findViewById(R.id.lectureplan_detail_week_subjectName);
        tableList = new ArrayList<>();

        if (getArguments() != null) {
            token = getArguments().getString("token");
            id = getArguments().getString("id");
            year = getArguments().getString("year");
            term = getArguments().getString("term");
            cd = getArguments().getString("cd");
            cn = getArguments().getString("cn");
            pi = getArguments().getString("pi");
            sn = getArguments().getString("sn");

            System.out.println(token+ id+ year+ term+ cd+ cn+ pi);
        }
        lectureplan_detail_week_subjectName.setText(sn);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getWeekInfo(token, id, year, term, cd, cn, pi);
                // 쓰레드 안에서 UI 변경 시 필요
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < tableList.size(); i++) {
                            TableRow tableRow = new TableRow(rootView.getContext());
                            tableRow.setBackground(rootView.getContext().getDrawable(R.drawable.tableframe));
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < tableList.get(i).size(); j++) {
                                TextView textView = new TextView(rootView.getContext());
                                switch(j) {
                                    case 0:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                        break;
                                    case 1:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                                        break;
                                    case 2:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 5.0f));
                                        break;
                                    case 3:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                                        break;
                                    case 4:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4.0f));
                                        break;
                                    default:
                                        break;
                                }
                                textView.setText(tableList.get(i).get(j));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(rootView.getContext().getColor(R.color.black));
                                tableRow.addView(textView);
                            }
                            lectureplan_detail_week_table.addView(tableRow);
                        }
                    }
                });
            }
        }).start();

        return rootView;
    }
    public void getWeekInfo(String token, String id, String year, String term, String cd, String cn, String pi) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(bookInfoURL);

            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("CLSS_NUMB", cn);
            parameter.put("LECT_YEAR", year);
            parameter.put("LECT_TERM", term);
            parameter.put("SUBJ_CD", cd);
            parameter.put("STAF_NO", pi);
            try {
                payload.put("MAP_ID", "education.ucs.UCS_03100_T.SELECT_REPORT_CLASSPLAN");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectList");
                payload.put("programID", "UCS_03090_P");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(bookInfoURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                // 이수한 학기 수
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    ArrayList<String> tmpList = new ArrayList<>();
                    tmpList.add(jsonArray.getJSONObject(i).get("WEEK_NM").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("WEEK_TITL01").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("LECT_PLAN01").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("LECT_MTHD01").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("REPOT_ETC01").toString());
                    tableList.add(tmpList);
                }
            }
        } catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
