package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
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

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ScholarshipActivity extends AppCompatActivity {

    private static final String scholarURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token;
    String id;
    String year;
    String term;

    ArrayList<String> scholarNameList;
    ArrayList<String> scholarWonList;
    ArrayList<String> scholarExplainList;

    Spinner scholarYearSpinner;
    Spinner scholarTermSpinner;
    Button scholarSearchBtn;
    RecyclerView scholarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scholarship_form);

        Intent intent = getIntent();
        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        scholarNameList = new ArrayList<>();
        scholarWonList = new ArrayList<>();
        scholarExplainList = new ArrayList<>();

        scholarYearSpinner = (Spinner) findViewById(R.id.scholarYearSpinner);
        scholarTermSpinner = (Spinner) findViewById(R.id.scholarTermSpinner);
        scholarSearchBtn = (Button) findViewById(R.id.scholarSearchBtn);
        scholarList = (RecyclerView) findViewById(R.id.scholarList);

        ArrayList<String> years = ((userClass) getApplication()).getYearlist();
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("1학기");   terms.add("2학기"); terms.add("여름계절학기"); terms.add("겨울계절학기");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, terms);

        ScholarAdapter adapter = new ScholarAdapter(
                scholarNameList,
                scholarWonList,
                scholarExplainList);

        scholarList.setLayoutManager(new LinearLayoutManager(this));
        scholarList.setAdapter(adapter);

        scholarYearSpinner.setAdapter(yearAdapter);
        scholarYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = ((userClass) getApplication()).getSchYear();
            }
        });

        scholarTermSpinner.setAdapter(termAdapter);
        scholarTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                term = Integer.toString(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                term = "1";
            }
        });

        scholarSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        scholarNameList.clear();
                        scholarWonList.clear();
                        scholarExplainList.clear();
                        scholarSearchBtn.setClickable(false);
                        if(getScholar(token, id, year, term))
                            scholarSearchBtn.setClickable(true);
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
                scholarSearchBtn.setClickable(false);
                if(getScholar(token, id, year, term))
                    scholarSearchBtn.setClickable(true);
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

    public boolean getScholar(String token, String id, String year, String term) {
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("ID", id);
            parameter.put("STU_NO", id);
            parameter.put("SCH_YEAR", year);
            parameter.put("SCH_TERM", term);

            try {
                payload.put("MAP_ID", "education.uss.USS_01011_T.select_janghak");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "USS_02013_V");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(scholarURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    scholarNameList.add(jsonArray.getJSONObject(i).get("SCLS_NM").toString());
                    String won = jsonArray.getJSONObject(i).get("SCLS_AMT").toString().replace(" ", "");
                    scholarWonList.add(won + "원");
                    scholarExplainList.add(jsonArray.getJSONObject(i).get("REMK_TEXT").toString());
                }
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return true;
    }
}
