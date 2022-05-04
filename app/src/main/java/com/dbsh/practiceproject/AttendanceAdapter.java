package com.dbsh.practiceproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private List<AttendanceItem> data;

    public AttendanceAdapter(List<AttendanceItem> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.attendance_list, parent, false);
        AttendanceParentHolder header = new AttendanceParentHolder(view);
        return header;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AttendanceItem item = data.get(position);
        final AttendanceParentHolder itemController = (AttendanceParentHolder) holder;
        itemController.subjectName.setText(item.text);
        itemController.subjectName.setText(item.text);
        itemController.professorName.setText(item.text2);
        itemController.subjectCD.setText(item.text3);
        for(int i = 0; i < item.subtext.size(); i++) {
            TableRow tableRow = new TableRow(((AttendanceParentHolder) holder).attendance_table.getContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            for(int j = 0; j < item.subtext.get(i).size(); j++) {
                TextView textView = new TextView(((AttendanceParentHolder) holder).attendance_table.getContext());
                textView.setText(item.subtext.get(i).get(j));
                textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(((AttendanceParentHolder) holder).attendance_table.getContext().getDrawable(R.drawable.tableframe));
                textView.setTextColor(((AttendanceParentHolder) holder).attendance_table.getContext().getColor(R.color.black));
                tableRow.addView(textView);
            }
            itemController.attendance_table.addView(tableRow);
        }
        itemController.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemController.attendance_table.getVisibility() == View.GONE)
                    itemController.attendance_table.setVisibility(View.VISIBLE);
                else
                    itemController.attendance_table.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class AttendanceParentHolder extends RecyclerView.ViewHolder {
        public TextView subjectName;
        public TextView professorName;
        public TextView subjectCD;
        public TableLayout attendance_table;
        //public AttendanceItem refferalItem;

        public AttendanceParentHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.attendance_subjectname);
            professorName = (TextView) itemView.findViewById(R.id.attendance_professor);
            subjectCD = (TextView) itemView.findViewById(R.id.attendance_cd);
            attendance_table = (TableLayout) itemView.findViewById(R.id.attendance_table);
        }
    }

    public static class AttendanceItem {
        public int type;
        public String text, text2, text3;
        public ArrayList<ArrayList<String>> subtext;
        //public List<AttendanceItem> invisibleChildren;
        public AttendanceItem() {}
        public AttendanceItem(int type, String text, String text2, String text3, ArrayList<ArrayList<String>> subtext) {
            this.type = type;
            this.text = text;
            this.text2 = text2;
            this.text3 = text3;
            this.subtext = subtext;
        }
    }
}
