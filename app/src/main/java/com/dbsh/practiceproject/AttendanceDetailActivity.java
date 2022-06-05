package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar;

public class AttendanceDetailActivity extends AppCompatActivity {
    private static final String attendanceDURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    SemiCircleArcProgressBar progressBar;
    TextView attendance_subj_toolbar, attendance_detail_percent;
    TextView attendance_detail_atte_cnt;
    TextView attendance_detail_late_cnt;
    TextView attendance_detail_absn_cnt;

    String token;
    String id;
    String year;
    String term;
    String cd;
    String numb;

    String title;
    int percent;
    int time;

    int atte_cnt, late_cnt, absn_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_detail_form);

        atte_cnt = 0;
        late_cnt = 0;
        absn_cnt = 0;

        Intent intent = getIntent();
        cd = intent.getStringExtra("CD");
        numb = intent.getStringExtra("NUMB");
        title = intent.getStringExtra("TITLE");
        percent = intent.getIntExtra("PERCENT", 0);
        time = intent.getIntExtra("TIME", time);

        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        attendance_detail_atte_cnt = (TextView) findViewById(R.id.attendance_detail_atte_cnt);
        attendance_detail_late_cnt = (TextView) findViewById(R.id.attendance_detail_late_cnt);
        attendance_detail_absn_cnt = (TextView) findViewById(R.id.attendance_detail_absn_cnt);

        attendance_subj_toolbar = (TextView) findViewById(R.id.attendance_subj_toolbar);
        attendance_subj_toolbar.setText(title);

        attendance_detail_percent = (TextView) findViewById(R.id.attendance_detail_percent);
        attendance_detail_percent.setText(Integer.toString(percent) + "%");

        progressBar = (SemiCircleArcProgressBar) findViewById(R.id.attendance_half_progressbar);
        if(percent == 100)
            progressBar.setProgressBarColor(getColor(R.color.MainColor));
        else if(percent >= 75 && percent < 100)
            progressBar.setProgressBarColor(getColor(R.color.attendanceprogressbarBarColor1));
        else
            progressBar.setProgressBarColor(getColor(R.color.attendanceprogressbarBarColor2));
        progressBar.setPercent(percent);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.attendance_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        new Thread(new Runnable() {
            @Override
            public void run() {
                getDetailAttendance(token, id, year, term, cd, numb);
                    // 쓰레드 안에서 UI 변경 시 필요
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            attendance_detail_atte_cnt.setText(Integer.toString(atte_cnt));
                            attendance_detail_late_cnt.setText(Integer.toString(late_cnt));
                            attendance_detail_absn_cnt.setText(Integer.toString(absn_cnt));
                        }
                    });
            }
        }).start();
    }

    // 과목별 상세 정보
    public void getDetailAttendance(String token, String id, String year, String term, String CD, String NUMB) {
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
                for (int i = 0; i < count; i++) {
                    int div = Integer.parseInt(jsonArray.getJSONObject(i).get("ABSN_TIME").toString());
                    if(div == 0)
                        atte_cnt += 1;
                    else if(div < time)
                        late_cnt += 1;
                    else
                        absn_cnt += 1;
                }
            }
        } catch (JSONException | NullPointerException exception) {
            exception.printStackTrace();
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
