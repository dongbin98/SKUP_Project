package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class TuitionActivity extends AppCompatActivity {
    private static final String todayURL = "https://sportal.skuniv.ac.kr/sportal/common/selectOne.sku";
    private static final String tuitionURL = "https://sportal.skuniv.ac.kr/sportal/common/selectOne.sku";
    private static final String POST = "POST";
    HttpURLConnection connection;

    TableLayout basicInform;
    TextView name;          // 이름
    String nameStr;
    TextView basicId;       // 학번
    String idStr;
    TextView major;         // 전공
    String majorStr;
    TextView schYR;         // 학년
    String schYRStr;
    TextView entDiv;        // 입학구분
    String entDivStr;
    TextView entDay;        // 입학일자
    String entDayStr;
    TextView regStat;       // 재학
    String regStatStr;
    TextView subPnt;        // 수강신청학점
    String subPntStr;
    TextView isuHakgi;      // 이수학기
    String isuHakgiStr;
    TextView regSchTerm;    // 등록학기
    String regSchTermStr;

    TableLayout tuitionInform;
    TextView yearterm;      // 학년도 & 학기
    String yeartermStr;
    TextView entFee;        // 입학금
    String entFeeStr;
    TextView lesnFee;       // 수업료
    String lesnFeeStr;
    TextView coopDegree;    // 공동학위
    String coopDegreeStr;
    TextView ussEntFee;     // 장학입학금
    String ussEntFeeStr;
    TextView ussLesnFee;    // 장학수업료
    String ussLesnFeeStr;
    TextView sclsTot;       // 장학금(합계)
    String sclsTotStr;
    TextView totAmt;        // 등록금합계
    String totAmtStr;
    TextView regAmt;        // 납부금액
    String regAmtStr;
    TextView paidStat;      // 납부상태
    String paidStatStr;
    TextView nonPay;        // 미납금액
    String nonPayStr;
    TextView payDate;       // 납부일자
    String payDateStr;
    TextView tmpAcct;       // 신한은행 가상계좌
    String tmpAcctStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuition_form);

        Intent intent = getIntent();
        String token = ((userClass) getApplication()).getToken();
        String id = ((userClass) getApplication()).getId();
        String year = ((userClass) getApplication()).getSchYear();
        String term = ((userClass) getApplication()).getSchTerm();

        // BasicTable 내용
        basicInform = (TableLayout) findViewById(R.id.BasicInformTable);        // 기본정보 테이블
        name = (TextView) findViewById(R.id.BasicInformTableName);              // 이름
        basicId = (TextView) findViewById(R.id.BasicInformTableID);             // 학번
        major = (TextView) findViewById(R.id.BasicInformTableMajor);            // 전공
        schYR = (TextView) findViewById(R.id.BasicInformTableSchYR);            // 학년
        entDiv = (TextView) findViewById(R.id.BasicInformTableEntdiv);          // 입학구분
        entDay = (TextView) findViewById(R.id.BasicInformTableEntday);          // 입학일자
        regStat = (TextView) findViewById(R.id.BasicInformTableRegStat);        // 재학
        subPnt = (TextView) findViewById(R.id.BasicInformTableSubjPoint);       // 수강신청학점
        isuHakgi = (TextView) findViewById(R.id.BasicInformTableIsuHakgi);      // 이수학기
        regSchTerm = (TextView) findViewById(R.id.BasicInformTableRegSchTerm);  // 등록학기

        // TuitionTable 내용
        tuitionInform = (TableLayout) findViewById(R.id.TuitionInformTable);    // 등록금내역 테이블
        yearterm = (TextView) findViewById(R.id.TuitionInformYear);             // 학년도 & 학기
        entFee = (TextView) findViewById(R.id.TuitionInformTableEntfee);        // 입학금
        lesnFee = (TextView) findViewById(R.id.TuitionInformTableLesnfee);      // 수업료
        coopDegree = (TextView) findViewById(R.id.TuitionInformTableCoopDegree);// 공동학위
        ussEntFee = (TextView) findViewById(R.id.TuitionInformTableUssEntfee);  // 장학입학금
        ussLesnFee = (TextView) findViewById(R.id.TuitionInformTableUssLesnfee);// 장학수업료
        sclsTot = (TextView) findViewById(R.id.TuitionInformTableSclsTot);      // 장학금(합계)
        totAmt = (TextView) findViewById(R.id.TuitionInformTableTotAmt);        // 등록금합계
        regAmt = (TextView) findViewById(R.id.TuitionInformTableRegAmt);        // 납부금액
        paidStat = (TextView) findViewById(R.id.TuitionInformTablePaidStat);    // 납부상태
        nonPay = (TextView) findViewById(R.id.TuitionInformTableNonPay);        // 미납금액
        payDate = (TextView) findViewById(R.id.TuitionInformTablePayDate);      // 납부일자
        tmpAcct = (TextView) findViewById(R.id.TuitionInformTableTempAcct);     // 신한은행 가상계좌

        new Thread(new Runnable() {
            @Override
            public void run() {

                // 가져온 년도, 학기 정보를 갖고 등록금 납부내역 불러오기
                getTuition(token, id, year, term);
                // 쓰레드 안에서 UI 변경 시 필요
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText("성명\n" + nameStr);
                        basicId.setText("학번\n" + idStr);
                        major.setText("학과/부\n" + majorStr);
                        schYR.setText("학년\n" + schYRStr);
                        entDiv.setText("입학구분\n" + entDivStr);
                        entDay.setText("입학일자\n" + entDayStr);
                        regStat.setText("학적상태\n" + regStatStr);
                        subPnt.setText("수강신청학점\n" + subPntStr);
                        isuHakgi.setText("총이수학기\n" + isuHakgiStr);
                        regSchTerm.setText("등록학기\n" + regSchTermStr);

                        yearterm.setText(yeartermStr);
                        entFee.setText("입학금\n" + entFeeStr);
                        lesnFee.setText("수업료\n" + lesnFeeStr);
                        coopDegree.setText("공동학위\n" + coopDegreeStr);
                        ussEntFee.setText("장학입학금\n" + ussEntFeeStr);
                        ussLesnFee.setText("장학수업료\n" + ussLesnFeeStr);
                        sclsTot.setText("장학금\n" + sclsTotStr);
                        totAmt.setText("등록금합계\n" + totAmtStr);
                        regAmt.setText("납부금액\n" + regAmtStr);
                        paidStat.setText("납부상태\n" + paidStatStr);
                        nonPay.setText("미납금액\n" + nonPayStr);
                        payDate.setText("납부일자\n" + payDateStr);
                        tmpAcct.setText("신한은행 가상계좌번호\n" + tmpAcctStr);
                    }
                });
            }
        }).start();
    }

    public void getInform(String token, String id) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(tuitionURL);
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
            parameter.put("P_STU_NO", id);
            parameter.put("SCH_REG_STAT", "");
            try {
                payload.put("MAP_ID", "education.urg.URG_common.SELECT_URG_STD");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectOne");
                payload.put("programID", "URG_02012_V");
                payload.put("userID", id);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "기본정보 요청 실패!");
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

            String name = jsonResponse.getJSONObject("MAP").get("KOR_NM").toString();
            String number = jsonResponse.getJSONObject("MAP").get("STU_NO").toString();
            String major = jsonResponse.getJSONObject("MAP").get("MAJ_NM").toString();
            String schyear = jsonResponse.getJSONObject("MAP").get("SCHYR").toString();

        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    public void getTuition(String token, String id, String year, String term) {
        try {
            // ----------------------------
            // URL 설정 및 접속
            // ----------------------------
            URL url = new URL(tuitionURL);
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
                payload.put("MAP_ID", "education.urg.URG_02012_V.SELECT");
                payload.put("SYS_ID", "AL");
                payload.put("accessToken", token);
                payload.put("parameter", parameter);
                payload.put("path", "common/selectOne");
                payload.put("programID", "URG_02012_V");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            OutputStream os = connection.getOutputStream();
            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            // 연결 상태 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                Log.d("Failed", "등록금납부내역 요청 실패!");
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

            String tmp = jsonResponse.getJSONObject("MAP").get("SCH_YEAR").toString();
            // Basic Table
            nameStr = ((userClass) getApplication()).getKorName();
            idStr = jsonResponse.getJSONObject("MAP").get("STU_NO").toString();
            majorStr = jsonResponse.getJSONObject("MAP").get("SUBJ_STD_NM").toString();
            schYRStr = jsonResponse.getJSONObject("MAP").get("SCHYR").toString();
            entDivStr = jsonResponse.getJSONObject("MAP").get("ENT_DIV").toString();
            entDayStr = jsonResponse.getJSONObject("MAP").get("ENT_DATE").toString();
            regStatStr = jsonResponse.getJSONObject("MAP").get("SCH_REG_STAT_NM").toString();
            subPntStr = jsonResponse.getJSONObject("MAP").get("SUBJ_PONT").toString();
            isuHakgiStr = jsonResponse.getJSONObject("MAP").get("ISU_HAKGI").toString();
            regSchTermStr = jsonResponse.getJSONObject("MAP").get("REG_SCH_TERM").toString();
            // Tuition Table
            yeartermStr = jsonResponse.getJSONObject("MAP").get("SCH_YEAR").toString() + "학년 " + jsonResponse.getJSONObject("MAP").get("SCH_TERM").toString() + "학기";
            entFeeStr = jsonResponse.getJSONObject("MAP").get("ENT_FEE").toString();
            lesnFeeStr = jsonResponse.getJSONObject("MAP").get("LESN_FEE").toString();
            coopDegreeStr = jsonResponse.getJSONObject("MAP").get("COOP_DGR_AMT").toString();
            ussEntFeeStr = jsonResponse.getJSONObject("MAP").get("USS_ENT_FEE").toString();
            ussLesnFeeStr = jsonResponse.getJSONObject("MAP").get("USS_LESN_FEE").toString();
            sclsTotStr = jsonResponse.getJSONObject("MAP").get("SCLS_TOT").toString();
            totAmtStr = jsonResponse.getJSONObject("MAP").get("TOT_AMT").toString();
            regAmtStr = jsonResponse.getJSONObject("MAP").get("REG_AMT").toString();
            paidStatStr = jsonResponse.getJSONObject("MAP").get("PAID_STAT_NM").toString();
            nonPayStr = jsonResponse.getJSONObject("MAP").get("NON_PAY").toString();
            payDateStr = jsonResponse.getJSONObject("MAP").get("PAY_DATE").toString();
            tmpAcctStr = jsonResponse.getJSONObject("MAP").get("TEMP_ACCT").toString();

        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
