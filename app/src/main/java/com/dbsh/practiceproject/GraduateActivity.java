package com.dbsh.practiceproject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
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

public class GraduateActivity extends AppCompatActivity {

    private static final String graduateURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    TableLayout graduateInform;
    ArrayList<ArrayList<String>> tableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graduate_form);

        graduateInform = (TableLayout) findViewById(R.id.GraduateTable);
        tableList = new ArrayList<ArrayList<String>>();

        Intent intent = getIntent();
        String token = ((userClass) getApplication()).getToken();
        String id = ((userClass) getApplication()).getId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getGraduate(token, id);
                // 쓰레드 안에서 UI 변경 시 필요
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < tableList.size(); i++) {
                            TableRow tableRow = new TableRow(GraduateActivity.this);
                            tableRow.setBackground(getDrawable(R.drawable.tableframe));
                            tableRow.setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            for(int j = 0; j < tableList.get(i).size(); j++) {
                                TextView textView = new TextView(GraduateActivity.this);
                                switch(j) {
                                    case 0:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                                        break;
                                    case 1:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f));
                                        break;
                                    case 2:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 5.0f));
                                        break;
                                    case 3:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                        break;
                                    case 4:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                        break;
                                    default:
                                        break;
                                }
                                textView.setText(tableList.get(i).get(j));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(getColor(R.color.black));
                                tableRow.addView(textView);
                            }
                            graduateInform.addView(tableRow);
                        }
                    }
                });
            }
        }).start();
    }

    public void getGraduate(String token, String id) {
        String[] attrs = {"ISU_NAME", "AREA_NM", "SUBJ_NM", "DIS_DATA", "BIGO"};
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("STU_NO", id);
            try {
                payload.put("MAP_ID", "education.ugd.UGD_03031_T.SELECT_UCS_AREASUBJECT");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "UGD_03031_T");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(graduateURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                // 총 개수
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    ArrayList<String> tmpList = new ArrayList<String>();
                    for (String attr : attrs) {
                        tmpList.add(jsonArray.getJSONObject(i).get(attr).toString());
                    }
                    System.out.println("tmpList : " + tmpList);
                    tableList.add(tmpList);
                }
                System.out.println("tableList L :" + tableList);
            }

        } catch (JSONException | NullPointerException exception) {
            exception.printStackTrace();
        }
    }
}
