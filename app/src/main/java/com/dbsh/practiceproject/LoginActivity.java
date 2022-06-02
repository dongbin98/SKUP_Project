package com.dbsh.practiceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String loginURL = "https://sportal.skuniv.ac.kr/sportal/auth2/login.sku";
    private static final String lectURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";
    private static final String lecttimeURL = "https://sportal.skuniv.ac.kr/sportal/common/selectOne.sku";    // 개요
    private static final String POST = "POST";
    public static userClass user = new userClass();
    HttpURLConnection connection;

    String userName, passWord;
    Boolean checked;

    EditText loginID;
    EditText loginPW;
    Switch loginAuto;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        userName = auto.getString("userName", null);
        passWord = auto.getString("passWord", null);
        checked = auto.getBoolean("checked", false);

        loginID = (EditText) findViewById(R.id.loginID);
        loginPW = (EditText) findViewById(R.id.loginPW);
        loginAuto = (Switch) findViewById(R.id.loginAuto);

        loginID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    loginID.setBackgroundResource(R.drawable.login_edittext_focused);
                }
                else {
                    loginID.setBackgroundResource(R.drawable.login_edittext);
                }
            }
        });
        loginPW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    loginPW.setBackgroundResource(R.drawable.login_edittext_focused);
                }
                else {
                    loginPW.setBackgroundResource(R.drawable.login_edittext);
                }
            }
        });

        if(checked) {
            loginAuto.setChecked(true);
            loginID.setText(userName);
            loginPW.setText(passWord);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    login(userName, passWord);
                }
            }).start();
        }
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                userName = loginID.getText().toString();
                passWord = loginPW.getText().toString();
                if(!loginAuto.isChecked()) {
                    SharedPreferences.Editor Editor = auto.edit();
                    Editor.clear();
                    Editor.commit();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login(userName, passWord);
                    }
                }).start();
            }
        });
    }

    public void login(String id, String pw) {
        try {
            // --------------------------
            // URL 설정 및 접속 (포탈 로그인)
            // --------------------------
            URL url = new URL(loginURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(10000);// 연결 대기 시간 설정
            connection.setRequestMethod(POST);  // 전송 방식 POST
            connection.setDoInput(true);        // InputStream으로 서버로부터 응답받음
            connection.setDoOutput(true);       // OutputStream으로 POST데이터를 넘겨줌
            // 서버 Response를 JSON 형식의 타입으로 요청
            connection.setRequestProperty("Accept", "application/json");
            // 서버에게 Request Body 전달 시 application/json으로 서버에 전달
            connection.setRequestProperty("content-type", "application/json");
            // ------------
            // 서버로 값 요청
            // ------------
            JSONObject payload = new JSONObject();
            try {
                payload.put("username", id);
                payload.put("password", pw);
                payload.put("grant_type", "password");
                payload.put("userType", "sku");
            } catch(JSONException excepton) {
                excepton.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            // 연결 상태 확인
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "로그인 요청 실패!");
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
            connection.disconnect();

            JSONObject response = new JSONObject(sb.toString());
            // 로그인 성공 실패 유무 출력
            // 쓰레드 단에서는 핸들러로 처리해줘야 Toast 메시지 출력가능
            Handler handler = new Handler(Looper.getMainLooper());
            if(response.get("RTN_STATUS").toString().equals("S")) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
            }
            else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
                return;
            }
            // 자동 로그인
            if (loginAuto.isChecked()) {
                // TODO : CheckBox is checked.
                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLoginEdit = auto.edit();
                autoLoginEdit.putString("userName", id);
                autoLoginEdit.putString("passWord", pw);
                autoLoginEdit.putBoolean("checked", true);
                autoLoginEdit.commit();
            }
            // 유저정보 저장하기
            JSONObject UserInfo = response.getJSONObject("USER_INFO");
            ((userClass) getApplication()).setId(UserInfo.get("ID").toString());
            ((userClass) getApplication()).setKorName(UserInfo.get("KOR_NAME").toString());
            ((userClass) getApplication()).setPhoneNumber(UserInfo.get("PHONE_MOBILE").toString());
            ((userClass) getApplication()).setMajor(UserInfo.get("COL_NM").toString(), UserInfo.get("TEAM_NM").toString());
            ((userClass) getApplication()).setEmailAddress(UserInfo.get("EMAIL").toString());
            ((userClass) getApplication()).setWebmailAddress(UserInfo.get("WEBMAIL_ID").toString());
            ((userClass) getApplication()).setTutorName(UserInfo.get("TUTOR_NAME").toString());
            ((userClass) getApplication()).setSchInfo(UserInfo.get("SCH_YEAR").toString(),
                    UserInfo.get("SCH_TERM").toString(),
                    UserInfo.get("SCHYR").toString(),
                    UserInfo.get("SCH_REG_STAT_NM").toString());
            ((userClass) getApplication()).setToken(response.get("access_token").toString());
            JSONArray YearList = response.getJSONArray("YEAR_LIST");
            if (!((userClass) getApplication()).getYearlist().isEmpty()) {
                ((userClass) getApplication()).clearYearlist();
            }
            if (!((userClass) getApplication()).getLectlist().isEmpty()) {
                ((userClass) getApplication()).clearLectlist();
            }
            if (!((userClass) getApplication()).getLecttimelist().isEmpty()) {
                ((userClass) getApplication()).clearLecttimelist();
            }
            if (!((userClass) getApplication()).getLectnumlist().isEmpty()) {
                ((userClass) getApplication()).clearLectnumlist();
            }
            if (!((userClass) getApplication()).getLectproflist().isEmpty()) {
                ((userClass) getApplication()).clearLectproflist();
            }
            for (int i = 0; i < YearList.length(); i++) {
                    ((userClass) getApplication()).addYearlist(YearList.getJSONObject(i).get("value").toString());
            }
            getLectInfo(((userClass) getApplication()).getToken(),
                    ((userClass) getApplication()).getId(),
                    ((userClass) getApplication()).getSchYear(),
                    ((userClass) getApplication()).getSchTerm());
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
    }

    // 해당 학기 수강 교과목 학수번호, 분반 가져오기
    public void getLectInfo(String token, String id, String year, String term){
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
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
                payload.put("path", "common/selectList");
                payload.put("programID", "main");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(lectURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONArray jsonArray = response.getJSONArray("LIST");

                int count = Integer.parseInt(response.get("COUNT").toString());

                for (int i = 0; i < count; i++) {
                    ((userClass) getApplication()).addLectlist(jsonArray.getJSONObject(i).get("SUBJ_CD").toString());
                    ((userClass) getApplication()).addLectnumlist(jsonArray.getJSONObject(i).get("CLSS_NUMB").toString());
                    ((userClass) getApplication()).addLectproflist(jsonArray.getJSONObject(i).get("PROF_NUMB").toString());
                    getLecttime(token, id, year, term,
                            ((userClass) getApplication()).getLectlist().get(i),
                            ((userClass) getApplication()).getLectnumlist().get(i),
                            ((userClass) getApplication()).getLectproflist().get(i)
                    );
                }
                // 메뉴 페이지로 넘어가기
                //Intent intent = new Intent(this, MenuActivity.class);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    // 해당 학기 수강 교과목 수업시간 가져오기
    public void getLecttime(String token, String id, String year, String term, String cd, String cn, String pi) {
        try {
            JSONObject payload = new JSONObject();
            JSONObject parameter = new JSONObject();

            parameter.put("CLSS_NUMB", cn);
            parameter.put("LECT_YEAR", year);
            parameter.put("LECT_TERM", term);
            parameter.put("SUBJ_CD", cd);
            parameter.put("STAF_NO", pi);
            try {
                payload.put("MAP_ID", "education.ucs.UCS_03100_T.SELECT_REPORT_MAIN");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectOne");
                payload.put("programID", "main");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(lecttimeURL, token, payload);
            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONObject MAP = response.getJSONObject("MAP");
                ((userClass) getApplication()).addLecttimelist(MAP.get("SUBJ_TIME").toString());
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}