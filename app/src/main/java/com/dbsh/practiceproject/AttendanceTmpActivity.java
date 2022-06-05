package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class AttendanceTmpActivity extends AppCompatActivity {
    private static final String attendanceURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String attendanceDURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token;
    String id;
    String year;
    String term;

    List<AttendanceTmpAdapter.AttendanceItem> data = new ArrayList<>();
    ArrayList<String> CDList = new ArrayList<>();   // 학수번호 따로 저장
    ArrayList<String> NUMBList = new ArrayList<>(); // 분반번호 따로 저장
    AttendanceTmpAdapter adapter;
    RecyclerView attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_tmp_form);

        Intent intent = getIntent();
        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.attendance_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        attendanceList = (RecyclerView) findViewById(R.id.attendance_recyclerview);

        adapter = new AttendanceTmpAdapter(data);
        adapter.setOnItemClickListener (new AttendanceTmpAdapter.OnItemClickListener () {

            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                String title = data.get(position).text;
                int percent = data.get(position).percent;
                String cd = CDList.get(position);
                String numb = NUMBList.get(position);
                int time = Integer.parseInt(((userClass) getApplication()).getLecttimelist().get(position));

                Intent detailIntent = new Intent(AttendanceTmpActivity.this, AttendanceDetailActivity.class);
                detailIntent.putExtra("TITLE", title);
                detailIntent.putExtra("PERCENT", percent);
                detailIntent.putExtra("CD", cd);
                detailIntent.putExtra("NUMB", numb);
                detailIntent.putExtra("TIME", time);
                startActivity(detailIntent);
            }
        });

        attendanceList.setLayoutManager(new LinearLayoutManager(this));
        attendanceList.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.dataClear();
                if(getAttendance(token, id, year, term))
                // 쓰레드 안에서 UI 변경 시 필요
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

        System.out.println("CDList Size() : " + CDList.size());
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

            if (response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");
                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    int percent;
                    percent = getDetailAttendance(token, id, year, term,
                            jsonArray.getJSONObject(i).get("SUBJ_CD").toString(),
                            jsonArray.getJSONObject(i).get("CLSS_NUMB").toString(),
                            i);
                    CDList.add(jsonArray.getJSONObject(i).get("SUBJ_CD").toString());
                    NUMBList.add(jsonArray.getJSONObject(i).get("CLSS_NUMB").toString());
                    data.add(new AttendanceTmpAdapter.AttendanceItem(
                            jsonArray.getJSONObject(i).get("SUBJ_NM").toString(),
                            jsonArray.getJSONObject(i).get("SUBJ_CLSS").toString(),
                            Integer.toString(percent),
                            percent)
                    );
                }
            }
        } catch (JSONException | NullPointerException exception) {
            exception.printStackTrace();
        }
        return true;
    }
    // 과목별 상세 정보
    public int getDetailAttendance(String token, String id, String year, String term, String CD, String NUMB, int number) {
        ArrayList<ArrayList<String>> subArray = new ArrayList<>();
        int percent = 0;
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

            if (response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");
                int count = Integer.parseInt(response.get("COUNT").toString());

                double all = 0;
                double absn = 0;
                double total = 0;
                for (int i = 0; i < count; i++) {
                    all += Integer.parseInt(((userClass) getApplication()).getLecttimelist().get(number));
                    absn += Integer.parseInt(jsonArray.getJSONObject(i).get("ABSN_TIME").toString());
                }
                total = all - absn;
                percent = (int) (total / all * 100);
            }
            return percent;
        } catch (JSONException | NullPointerException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
