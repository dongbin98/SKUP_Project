package com.dbsh.practiceproject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

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

public class LecturePlanDetailInfoFragment extends Fragment {

    private static final String detailInfoURL = "https://sportal.skuniv.ac.kr/sportal/common/selectOne.sku";    // 개요
    private static final String bookInfoURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";     // 주차별, 교재
    private static final String POST = "POST";
    HttpURLConnection connection;

    String token, id, year, term, cd, cn, pi, sn;           // 토큰, 학번, 년, 학기, 학수번호, 분반, 교수명, 과목명

    TextView lectureplan_detail_info_subjectName;    // 교과목명 (제목)
    TextView lectureplan_detail_info_subject;           // 교과목명
    TextView lectureplan_detail_info_cd;                // 학수번호
    TextView lectureplan_detail_info_dept;              // 학과(부)명
    TextView lectureplan_detail_info_term;              // 학기
    TextView lectureplan_detail_info_point;             // 학점/시수
    TextView lectureplan_detail_info_evalLevel;         // 대상학년
    TextView lectureplan_detail_info_completeName;      // 이수구분
    TextView lectureplan_detail_info_day;               // 강의시간/교시
    TextView lectureplan_detail_info_room;              // 강의실
    TextView lectureplan_detail_info_professorName;     // 교수명
    TextView lectureplan_detail_info_professorPhone;    // 교수전화
    TextView lectureplan_detail_info_professorEmail;    // 교수이메일
    TextView lectureplan_detail_info_explain;           // 교과목 개요
    TextView lectureplan_detail_info_goal;              // 강의 목표
    // 강의 방법 보류
    TextView lectureplan_detail_info_how_evaluate;      // 평가 방법
    TextView lectureplan_detail_info_major_ability;     // 전공 능력
    TextView lectureplan_detail_info_notice;            // 강의규정 또는 안내사항

    CheckBox lectureplan_detail_info_cbx_1;
    CheckBox lectureplan_detail_info_cbx_2;
    CheckBox lectureplan_detail_info_cbx_3;
    CheckBox lectureplan_detail_info_cbx_4;
    CheckBox lectureplan_detail_info_cbx_5;
    CheckBox lectureplan_detail_info_cbx_6;
    CheckBox lectureplan_detail_info_cbx_7;
    CheckBox lectureplan_detail_info_cbx_8;
    CheckBox lectureplan_detail_info_cbx_9;
    CheckBox lectureplan_detail_info_cbx_10;
    CheckBox lectureplan_detail_info_cbx_11;
    CheckBox lectureplan_detail_info_cbx_12;

    TableLayout lectureplan_detail_info_bookTable;

    String info_subject;
    String info_cd;
    String info_dept;
    String info_term;
    String info_point;
    String info_evalLevel;
    String info_completeName;
    String info_day;
    String info_room;
    String info_professorName;
    String info_professorPhone;
    String info_professorEmail;
    String info_explain;
    String info_goal;
    String info_how_evaluate;
    String info_major_ability;
    String info_notice;
    String info_invitation;
    String info_problem;
    String info_online;
    String info_team;
    String info_experience;
    String info_individual;
    String info_debate;
    String info_action;
    String info_project;
    String info_flip;
    String info_practical;
    String info_lecture;
    ArrayList<ArrayList<String>> tableList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.lectureplan_detail_info_form, container, false);

        lectureplan_detail_info_subjectName = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_subjectName);
        lectureplan_detail_info_subject = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_subject);
        lectureplan_detail_info_cd = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_cd);
        lectureplan_detail_info_dept = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_dept);
        lectureplan_detail_info_term = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_term);
        lectureplan_detail_info_point = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_point);
        lectureplan_detail_info_evalLevel = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_evalLevel);
        lectureplan_detail_info_completeName = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_completeName);
        lectureplan_detail_info_day = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_day);
        lectureplan_detail_info_room = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_room);
        lectureplan_detail_info_professorName = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_professorName);
        lectureplan_detail_info_professorPhone = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_professorPhone);
        lectureplan_detail_info_professorEmail = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_professorEmail);
        lectureplan_detail_info_explain = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_explain);
        lectureplan_detail_info_goal = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_goal);
        lectureplan_detail_info_how_evaluate = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_how_evaluate);
        lectureplan_detail_info_major_ability = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_major_ability);
        lectureplan_detail_info_notice = (TextView) rootView.findViewById(R.id.lectureplan_detail_info_notice);

        lectureplan_detail_info_cbx_1 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_1);
        lectureplan_detail_info_cbx_2 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_2);
        lectureplan_detail_info_cbx_3 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_3);
        lectureplan_detail_info_cbx_4 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_4);
        lectureplan_detail_info_cbx_5 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_5);
        lectureplan_detail_info_cbx_6 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_6);
        lectureplan_detail_info_cbx_7 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_7);
        lectureplan_detail_info_cbx_8 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_8);
        lectureplan_detail_info_cbx_9 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_9);
        lectureplan_detail_info_cbx_10 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_10);
        lectureplan_detail_info_cbx_11 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_11);
        lectureplan_detail_info_cbx_12 = (CheckBox) rootView.findViewById(R.id.lectureplan_detail_info_cbx_12);

        lectureplan_detail_info_bookTable = (TableLayout) rootView.findViewById(R.id.lectureplan_detail_info_bookTable);

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
        lectureplan_detail_info_subjectName.setText(sn);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getDetailInfo(token, id, year, term, cd, cn, pi);
                getBookInfo(token, id, year, term, cd, cn, pi);
                // 쓰레드 안에서 UI 변경 시 필요
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lectureplan_detail_info_subject.setText(info_subject);
                        lectureplan_detail_info_cd.setText(info_cd);
                        lectureplan_detail_info_dept.setText(info_dept);
                        lectureplan_detail_info_term.setText(info_term);
                        lectureplan_detail_info_point.setText(info_point);
                        lectureplan_detail_info_evalLevel.setText(info_evalLevel);
                        lectureplan_detail_info_completeName.setText(info_completeName);
                        lectureplan_detail_info_day.setText(info_day);
                        lectureplan_detail_info_room.setText(info_room);
                        lectureplan_detail_info_professorName.setText(info_professorName);
                        lectureplan_detail_info_professorPhone.setText(info_professorPhone);
                        lectureplan_detail_info_professorEmail.setText(info_professorEmail);
                        lectureplan_detail_info_explain.setText(info_explain);
                        lectureplan_detail_info_goal.setText(info_goal);
                        lectureplan_detail_info_how_evaluate.setText(info_how_evaluate);
                        lectureplan_detail_info_major_ability.setText(info_major_ability);
                        lectureplan_detail_info_notice.setText(info_notice);
                        if(info_lecture.equals("Y")) lectureplan_detail_info_cbx_1.setChecked(true);
                        if(info_debate.equals("Y")) lectureplan_detail_info_cbx_2.setChecked(true);
                        if(info_practical.equals("Y")) lectureplan_detail_info_cbx_3.setChecked(true);
                        if(info_team.equals("Y")) lectureplan_detail_info_cbx_4.setChecked(true);
                        if(info_project.equals("Y")) lectureplan_detail_info_cbx_5.setChecked(true);
                        if(info_problem.equals("Y")) lectureplan_detail_info_cbx_6.setChecked(true);
                        if(info_flip.equals("Y")) lectureplan_detail_info_cbx_7.setChecked(true);
                        if(info_invitation.equals("Y")) lectureplan_detail_info_cbx_8.setChecked(true);
                        if(info_individual.equals("Y")) lectureplan_detail_info_cbx_9.setChecked(true);
                        if(info_experience.equals("Y")) lectureplan_detail_info_cbx_10.setChecked(true);
                        if(info_online.equals("Y")) lectureplan_detail_info_cbx_11.setChecked(true);
                        if(info_action.equals("Y")) lectureplan_detail_info_cbx_12.setChecked(true);
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
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4.0f));
                                        break;
                                    case 1:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                                        break;
                                    case 2:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                                        break;
                                    case 3:
                                        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                                        break;
                                    default:
                                        break;
                                }
                                textView.setText(tableList.get(i).get(j));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(rootView.getContext().getColor(R.color.black));
                                tableRow.addView(textView);
                            }
                            lectureplan_detail_info_bookTable.addView(tableRow);
                        }
                    }
                });
            }
        }).start();
        return rootView;
    }
    public void getDetailInfo(String token, String id, String year, String term, String cd, String cn, String pi) {
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
                payload.put("programID", "UCS_03090_P");
                payload.put("userID", id);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            JSONObject response = Connector.getInstance().getResponse(detailInfoURL, token, payload);

            if(response.get("RTN_STATUS").toString().equals("S")) {
                JSONObject MAP = response.getJSONObject("MAP");

                info_subject = "교과목명\n" + MAP.get("SUBJ_NM").toString();
                info_cd = "학수번호-분반\n" + MAP.get("SUBJ_CLSS").toString();
                info_dept = "개설학과(부)\n" + MAP.get("DEPT_SHYR").toString();
                info_term = "개설학기\n" + MAP.get("LECT_TERM").toString();
                info_point = "학점 및 시수\n" + MAP.get("SUBJ_PONT").toString() + "학점 / " + MAP.get("SUBJ_TIME").toString() + "시간";
                info_evalLevel = "대상학년\n" + MAP.get("EVAL_LEVEL").toString();
                info_completeName = "이수구분\n" + MAP.get("COMPLETE_NM").toString();
                if(MAP.get("PROF2_TIME").toString() == "null") {
                    info_day = "";
                }
                else if(MAP.get("PROF3_TIME").toString() == "null") {
                    info_day = "";
                }
                else if(MAP.get("PROF4_TIME").toString() == "null") {
                    info_day = "";
                }
                else if(MAP.get("PROF5_TIME").toString() == "null") {
                    info_day = "";
                }
                else {
                    info_day = "강의시간 및 교시\n" + MAP.get("DAY").toString() + "-" + MAP.get("TIME").toString() + "(" + MAP.get("PROF1_TIME").toString() + ")";
                }
                info_room = "강의실\n" + MAP.get("ROOM").toString();
                info_professorName = "교수명\n" + MAP.get("NAME").toString();
                info_professorPhone = "연락처\n" + MAP.get("HP_NO").toString();
                info_professorEmail = "E-mail\n" + MAP.get("E_MAIL").toString();
                info_explain = MAP.get("LECT_PURP").toString();
                info_goal = MAP.get("LTPG_MTHD").toString();
                info_how_evaluate = "중간시험" + MAP.get("MID_EXAM_RATE").toString() +
                        "% 기말시험" + MAP.get("FINAL_EXAM_RATE").toString() +
                        "%\n 과제점수" + MAP.get("REPORT_RATE").toString() +
                        "% 출석점수" + MAP.get("ATTEND_RATE").toString() +
                        "% 기타점수" + MAP.get("ETC1_RATE").toString() + "%";
                info_major_ability = MAP.get("MAJOR_MTHD").toString();
                info_notice = MAP.get("REMK_TEXT").toString();
                info_invitation = MAP.get("INVITATION_YN").toString();
                info_problem = MAP.get("PROBLEM_YN").toString();
                info_online = MAP.get("ONLINE_YN").toString();
                info_team = MAP.get("TEAM_YN").toString();
                info_experience = MAP.get("EXPERIENCE_YN").toString();
                info_individual = MAP.get("INDIVIDUAL_YN").toString();
                info_debate = MAP.get("DEBATE_YN").toString();
                info_action = MAP.get("ACTION_YN").toString();
                info_project = MAP.get("PROJECT_YN").toString();
                info_flip = MAP.get("FLIP_YN").toString();
                info_practical = MAP.get("PRACTICAL_YN").toString();
                info_lecture = MAP.get("LECTURE_YN").toString();
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    public void getBookInfo(String token, String id, String year, String term, String cd, String cn, String pi) {
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
                payload.put("MAP_ID", "education.ucs.UCS_03100_T.SELECT_REPORT_BOOKINFO");
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
                    tmpList.add(jsonArray.getJSONObject(i).get("BOOK_NAME").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("BOOK_AUTHOR").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("BOOK_PUB_YEAR").toString());
                    tmpList.add(jsonArray.getJSONObject(i).get("BOOK_PUB_CO").toString());
                    tableList.add(tmpList);
                }
            }
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
    }
}
