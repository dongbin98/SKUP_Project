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
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String loginURL = "https://sportal.skuniv.ac.kr/sportal/auth2/login.sku";
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

            JSONObject jsonResponse = new JSONObject(sb.toString());
            // 로그인 성공 실패 유무 출력
            // 쓰레드 단에서는 핸들러로 처리해줘야 Toast 메시지 출력가능
            Handler handler = new Handler(Looper.getMainLooper());
            if(jsonResponse.get("RTN_STATUS").toString().equals("S")) {
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
            // 유저정보 받아오기
            JSONObject UserInfo = jsonResponse.getJSONObject("USER_INFO");
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
            ((userClass) getApplication()).setToken(jsonResponse.get("access_token").toString());
            JSONArray YearList = jsonResponse.getJSONArray("YEAR_LIST");
            if (!((userClass) getApplication()).getYearlist().isEmpty()) {
                ((userClass) getApplication()).clearYearlist();
            }
            for (int i = 0; i < YearList.length(); i++) {
                    ((userClass) getApplication()).addYearlist(YearList.getJSONObject(i).get("value").toString());
            }
            // 메뉴 페이지로 넘어가기
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}