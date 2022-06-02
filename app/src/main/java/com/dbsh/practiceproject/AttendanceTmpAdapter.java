package com.dbsh.practiceproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceTmpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AttendanceItem> data;

    Context context;

    public void dataClear() {data.clear();}
    public AttendanceTmpAdapter(List<AttendanceItem> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.attendance_tmp_list, parent, false);
        AttendanceHolder header = new AttendanceHolder(view);
        return header;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AttendanceItem item = data.get(position);
        final AttendanceHolder itemController = (AttendanceHolder) holder;
        itemController.attendance_title.setText(item.text);
        itemController.attendance_subj.setText(item.text2);
        itemController.attendance_percent.setText(item.text3 + "%");
        
        if(Integer.parseInt(item.text3) == 100) {
            // 파란색 막대기
            System.out.println("파랑입니다");
        }
        else if(Integer.parseInt(item.text3) >= 75 && Integer.parseInt(item.text3) < 100) {
            // 주황색 막대기
            System.out.println("주황입니다");
            itemController.attendance_progressbar.setProgressDrawable(context.getDrawable(R.drawable.attendance_progressbar2));
        }
        else {
            // 빨간색 막대기
            System.out.println("빨강입니다");
            itemController.attendance_progressbar.setProgressDrawable(context.getDrawable(R.drawable.attendance_progressbar3));
        }
        
        itemController.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "무야호", Toast.LENGTH_SHORT).show();
            }
        });
        itemController.attendance_progressbar.setProgress(item.percent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class AttendanceHolder extends RecyclerView.ViewHolder {
        public TextView attendance_title;   // 교과목명
        public TextView attendance_subj;    // 학수번호
        public TextView attendance_percent; // 출석률
        public ProgressBar attendance_progressbar; // 프로그레스바

        public AttendanceHolder(View itemView) {
            super(itemView);
            attendance_title = (TextView) itemView.findViewById(R.id.attendance_title);
            attendance_subj = (TextView) itemView.findViewById(R.id.attendance_subj);
            attendance_percent = (TextView) itemView.findViewById(R.id.attendance_percent);
            attendance_progressbar = (ProgressBar) itemView.findViewById(R.id.attendance_progressbar);
        }
    }

    public static class AttendanceItem {
        public String text, text2, text3;
        public int percent;
        public AttendanceItem() {}
        public AttendanceItem(String text, String text2, String text3, int percent) {
            this.text = text;       // 과목명
            this.text2 = text2;     // 학수번호
            this.text3 = text3;     // 퍼센트
            this.percent = percent;
        }
    }
}
