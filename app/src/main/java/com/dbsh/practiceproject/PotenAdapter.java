package com.dbsh.practiceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PotenAdapter extends RecyclerView.Adapter<PotenAdapter.PotenHolder> {

    // 목록
    ArrayList<String> potenProList;     // 프로그램명
    ArrayList<String> potenDivList;     // 역량
    ArrayList<String> potenMileList;    // 마일리지
    ArrayList<String> potenDateList;    // 신청일자
    ArrayList<String> potenPeopleList;  // 신청인원
    ArrayList<String> potenCenterList;  // 센터구분
    ArrayList<String> potenDaysList;    // 수업일수
    ArrayList<String> potenRoomList;    // 강의실
    ArrayList<String> potenAddrList;    // 장소(호실)
    // 상세보기
        // 프로그램명
        // 역량명
        // 신청일자
        // 신청인원
        // 센터구분
        // 수업일수
        // 강의실
        // 장소(호실)
        // 마일리지
    ArrayList<String> potenOnlineList;  // 온라인 진행여부
    ArrayList<String> potenBigoList;    // 비고
    ArrayList<String> potenEvntDateTimeList;// 일자[~15] (yyyy.mm.dd ~ yyyy.mm.dd)
    // 회차 별 일자 및 시간
    ArrayList<String> potenEvntDateList;/// 일자[~15] yyyy.mm.dd
    ArrayList<String> potenTimeList;    // 시간[~15]
    int type;

    // 일반역량
    PotenAdapter(ArrayList<String> normalpotenProList,
                 ArrayList<String> normalpotenDivList,
                 ArrayList<String> normalpotenMileList,
                 ArrayList<String> normalpotenDateList,
                 ArrayList<String> normalpotenPeopleList,
                 ArrayList<String> normalpotenCenterList,
                 ArrayList<String> normalpotenDaysList,
                 ArrayList<String> normalpotenRoomList,
                 ArrayList<String> normalpotenAddrList,
                 ArrayList<String> normalpotenOnlineList,
                 ArrayList<String> normalpotenBigoList,
                 ArrayList<String> normalpotenEvntDateList,
                 ArrayList<String> normalpotenTimeList,
                        int type) {
        this.potenProList = normalpotenProList;
        this.potenDivList = normalpotenDivList;
        this.potenMileList = normalpotenMileList;
        this.potenDateList = normalpotenDateList;
        this.potenPeopleList = normalpotenPeopleList;
        this.potenCenterList = normalpotenCenterList;
        this.potenDaysList = normalpotenDaysList;
        this.potenRoomList = normalpotenRoomList;
        this.potenAddrList = normalpotenAddrList;
        this.potenOnlineList = normalpotenOnlineList;
        this.potenBigoList = normalpotenBigoList;
        this.potenEvntDateList = normalpotenEvntDateList;
        this.potenTimeList = normalpotenTimeList;
        this.type = type;
    }
    // 전공역량
    PotenAdapter(ArrayList<String> majorpotenProList,
                 ArrayList<String> majorpotenDateList,
                 ArrayList<String> majorpotenPeopleList,
                 ArrayList<String> majorpotenAddrList,
                 ArrayList<String> majorpotenEvntDateTimeList,
                 ArrayList<String> majorpotenRoomList,
                 ArrayList<String> majorpotenDaysList,
                 ArrayList<String> majorpotenOnlineList,
                 ArrayList<String> majorpotenEvntDateList,
                 ArrayList<String> majorpotenTimeList,
                 int type) {
        this.potenProList = majorpotenProList;
        this.potenDateList = majorpotenDateList;
        this.potenPeopleList = majorpotenPeopleList;
        this.potenAddrList = majorpotenAddrList;
        this.potenEvntDateTimeList = majorpotenEvntDateTimeList;
        this.potenRoomList = majorpotenRoomList;
        this.potenDaysList = majorpotenDaysList;
        this.potenOnlineList = majorpotenOnlineList;
        this.potenEvntDateList = majorpotenEvntDateList;
        this.potenTimeList = majorpotenTimeList;
        this.type = type;
    }
    @NonNull
    @Override
    public PotenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(type == 1) view = inflater.inflate(R.layout.normalpoten_list, parent, false);
        else view = inflater.inflate(R.layout.majorpoten_list, parent, false);
        return new PotenHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PotenHolder holder, int position) {
        if(type == 1) { // 일반역량강화
            holder.normalpotenPro.setText(potenProList.get(position));
            holder.normalpotenDiv.setText(potenDivList.get(position));
            holder.normalpotenMile.setText(potenMileList.get(position));
            holder.normalpotenDate.setText(potenDateList.get(position));
            holder.normalpotenPeople.setText(potenPeopleList.get(position));
            holder.normalpotenCenter.setText(potenCenterList.get(position));
            holder.normalpotenDays.setText(potenDaysList.get(position));
        }
        else if(type == 2) { // 전공역량강화
            holder.majorpotenPro.setText(potenProList.get(position));
            holder.majorpotenDate.setText(potenDateList.get(position));
            holder.majorpotenPeople.setText(potenPeopleList.get(position));
            holder.majorpotenAddr.setText(potenAddrList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return potenProList.size();
    }

    class PotenHolder extends RecyclerView.ViewHolder {

        TextView normalpotenPro;
        TextView normalpotenDiv;
        TextView normalpotenMile;
        TextView normalpotenDate;
        TextView normalpotenPeople;
        TextView normalpotenCenter;
        TextView normalpotenDays;

        TextView majorpotenPro;
        TextView majorpotenDate;
        TextView majorpotenPeople;
        TextView majorpotenAddr;

        public PotenHolder(@NonNull View itemView) {
            super(itemView);
            normalpotenPro = itemView.findViewById(R.id.normalpotenPro);
            normalpotenDiv = itemView.findViewById(R.id.normalpotenDiv);
            normalpotenMile = itemView.findViewById(R.id.normalpotenMile);
            normalpotenDate = itemView.findViewById(R.id.normalpotenDate);
            normalpotenPeople = itemView.findViewById(R.id.normalpotenPeople);
            normalpotenDays = itemView.findViewById(R.id.normalpotenDays);
            normalpotenCenter = itemView.findViewById(R.id.normalpotenCenter);

            majorpotenPro = itemView.findViewById(R.id.majorpotenPro);
            majorpotenDate = itemView.findViewById(R.id.majorpotenDate);
            majorpotenPeople = itemView.findViewById(R.id.majorpotenPeople);
            majorpotenAddr = itemView.findViewById(R.id.majorpotenRoom);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (type == 1) {
                            Toast.makeText(view.getContext(), "일반역량클릭", Toast.LENGTH_SHORT).show();
                        } else if (type == 2) {
                            Toast.makeText(view.getContext(), "전공역량클릭", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}