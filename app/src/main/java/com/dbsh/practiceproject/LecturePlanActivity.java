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
import java.util.Locale;

public class LecturePlanActivity extends AppCompatActivity {
    private static final String lecturePlanURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token;
    String id;
    String year;
    String term;

    ArrayList<String> subjectCDList;    //학수번호  -> 리스트에 필요(표시, 검색), 세부정보에 필요
    ArrayList<String> subjectNameList;  //과목명    -> 리스트에 필요(표시, 검색)
    ArrayList<String> deptNameList;     //학과명    -> 리스트에 필요(검색)
    ArrayList<String> professorNameList;//교수명    -> 리스트에 필요(표시, 검색)
    ArrayList<String> classNumberList;  //분반      -> 세부정보에 필요
    ArrayList<String> professorIDList;  //교수번호   -> 세부정보에 필요

    ArrayList<String> filteredSubjectCDList;
    ArrayList<String> filteredSubjectNameList;
    ArrayList<String> filteredDeptNameList;
    ArrayList<String> filteredProfessorNameList;
    ArrayList<String> filteredClassNumberList;
    ArrayList<String> filteredProfessorIDList;

    Spinner lecturePlanYearSpinner;
    Spinner lecturePlanTermSpinner;
    Button lecturePlanSearchBtn;
    EditText lecturePlanEdit;
    RecyclerView lecturePlanList;

    PlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lectureplan_form);

        Intent intent = getIntent();
        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        subjectCDList = new ArrayList<>();
        subjectNameList = new ArrayList<>();
        deptNameList = new ArrayList<>();
        professorNameList = new ArrayList<>();
        classNumberList = new ArrayList<>();
        professorIDList = new ArrayList<>();

        filteredSubjectCDList = new ArrayList<>();
        filteredSubjectNameList = new ArrayList<>();
        filteredDeptNameList = new ArrayList<>();
        filteredProfessorNameList = new ArrayList<>();
        filteredClassNumberList = new ArrayList<>();
        filteredProfessorIDList = new ArrayList<>();

        lecturePlanYearSpinner = (Spinner) findViewById(R.id.lecturePlanYearSpinner);
        lecturePlanTermSpinner = (Spinner) findViewById(R.id.lecturePlanTermSpinner);
        lecturePlanSearchBtn = (Button) findViewById(R.id.lecturePlanSearchBtn);
        lecturePlanEdit = (EditText) findViewById(R.id.lecturePlanEdit);
        lecturePlanList = (RecyclerView) findViewById(R.id.lecturePlanList);

        ArrayList<String> years = ((userClass) getApplication()).getYearlist();
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("1학기");   terms.add("2학기"); terms.add("여름계절학기"); terms.add("겨울계절학기");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, terms);

        adapter = new PlanAdapter(
                subjectCDList,
                subjectNameList,
                deptNameList,
                professorNameList,
                classNumberList,
                professorIDList);

        lecturePlanList.setLayoutManager(new LinearLayoutManager(this));
        lecturePlanList.setAdapter(adapter);

        adapter.setOnItemClicklistener(new PlanClickListener() {
            @Override
            public void onItemClick(PlanAdapter.PlanHolder holder, View view, int position) {
                String cd = adapter.getSubjectCD(position);
                String cn = adapter.getClassNumber(position);
                String pi = adapter.getProfessorID(position);
                String dn = adapter.getDeptName(position);
                String pn = adapter.getProfessorName(position);
                String sn = adapter.getSubjectName(position);
                if(cd != "null" && pn != "null" && dn != "null") {
                    System.out.println(cd + pn + dn);
                    Intent detailIntent = new Intent(LecturePlanActivity.this, LecturePlanDetailActivity.class);
                    detailIntent.putExtra("cd", cd);
                    detailIntent.putExtra("cn", cn);
                    detailIntent.putExtra("pi", pi);
                    detailIntent.putExtra("sn", sn);
                    startActivity(detailIntent);
                }
            }
        });

        lecturePlanYearSpinner.setAdapter(yearAdapter);
        lecturePlanYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = years.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = ((userClass) getApplication()).getSchYear();
            }
        });

        lecturePlanTermSpinner.setAdapter(termAdapter);
        lecturePlanTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                term = Integer.toString(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                term = "1";
            }
        });

        lecturePlanSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        subjectCDList.clear();
                        subjectNameList.clear();
                        deptNameList.clear();
                        professorNameList.clear();
                        classNumberList.clear();
                        professorIDList.clear();
                        getLecturePlan(token, id, year, term);
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

        lecturePlanEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = lecturePlanEdit.getText().toString();
                searchFilter(searchText);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                getLecturePlan(token, id, year, term);
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

    public void getLecturePlan(String token, String id, String year, String term) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(lecturePlanURL);
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

            parameter.put("S_DEPT02", "%");
            parameter.put("S_DEPT03", "%");
            parameter.put("S_DEPT04", "%");
            parameter.put("S_FROM_DT", "");
            parameter.put("S_INPUT_YSNO", "1");
            parameter.put("S_LECT_YEAR", year);
            parameter.put("S_LECT_TERM", term);
            parameter.put("S_UCS04", "%");
            try {
                payload.put("MAP_ID", "education.ucs.UCS_03100_T.SELECT");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectlist");
                payload.put("programID", "UCS_03090_T");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();

            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "강의계획서 요청 실패!");
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
                    subjectCDList.add(jsonArray.getJSONObject(i).get("SUBJ_CD").toString());
                    subjectNameList.add(jsonArray.getJSONObject(i).get("SUBJ_NM").toString());
                    deptNameList.add(jsonArray.getJSONObject(i).get("DEPT_NM").toString());
                    professorNameList.add(jsonArray.getJSONObject(i).get("PROF_NM").toString());
                    classNumberList.add(jsonArray.getJSONObject(i).get("CLSS_NUMB").toString());
                    professorIDList.add(jsonArray.getJSONObject(i).get("PROF1").toString());
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
    public void searchFilter(String searchText) {

        filteredSubjectCDList.clear();
        filteredSubjectNameList.clear();
        filteredDeptNameList.clear();
        filteredProfessorNameList.clear();
        filteredClassNumberList.clear();
        filteredProfessorIDList.clear();


        int i = 0;
        for (i = 0; i < subjectCDList.size(); i++) {
            if (subjectCDList.get(i).toLowerCase().contains(searchText.toLowerCase()) ||
                    subjectNameList.get(i).toLowerCase().contains(searchText.toLowerCase()) ||
                    deptNameList.get(i).toLowerCase().contains(searchText.toLowerCase()) ||
                    professorNameList.get(i).toLowerCase().contains(searchText.toLowerCase()))
            {
                System.out.println(i);
                filteredSubjectCDList.add(subjectCDList.get(i));
                filteredSubjectNameList.add(subjectNameList.get(i));
                filteredDeptNameList.add(deptNameList.get(i));
                filteredProfessorNameList.add(professorNameList.get(i));
                filteredClassNumberList.add(classNumberList.get(i));
                filteredProfessorIDList.add(professorIDList.get(i));
            }
        }
        adapter.setFilter(filteredSubjectCDList, filteredSubjectNameList,
                filteredDeptNameList, filteredProfessorNameList,
                filteredClassNumberList, filteredProfessorIDList);
    }
}
