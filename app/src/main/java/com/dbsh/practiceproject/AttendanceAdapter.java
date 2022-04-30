package com.dbsh.practiceproject;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        switch (viewType) {
            case HEADER:
                LayoutInflater inflaterHeader = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterHeader.inflate(R.layout.attendance_parent_list, parent, false);
                AttendanceParentHolder header = new AttendanceParentHolder(view);
                return header;
            case CHILD:
                LayoutInflater inflaterChild = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterChild.inflate(R.layout.attendance_child_list, parent, false);
                AttendanceChildHolder child = new AttendanceChildHolder(view);
                return child;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AttendanceItem item = data.get(position);
        switch (item.type) {
            case HEADER:
                final AttendanceParentHolder itemController = (AttendanceParentHolder) holder;
                itemController.refferalItem = item;
                itemController.subjectName.setText(item.text);
                itemController.professorName.setText(item.text2);
                itemController.subjectCD.setText(item.text3);

                // 클릭으로 열고 닫기
                itemController.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            System.out.println("비어있어요");
                            item.invisibleChildren = new ArrayList<AttendanceItem>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                        } else {
                            System.out.println("비어있지않아요");
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (AttendanceItem i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final AttendanceChildHolder itemController1 = (AttendanceChildHolder) holder;
                itemController1.refferalItem = item;
                itemController1.date.setText(item.text);
                itemController1.time.setText(item.text2);
                itemController1.absent.setText(item.text3);
                break;
        }
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
        public AttendanceItem refferalItem;

        public AttendanceParentHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.attendance_subjectname);
            professorName = (TextView) itemView.findViewById(R.id.attendance_professor);
            subjectCD = (TextView) itemView.findViewById(R.id.attendance_cd);
        }
    }
    private static class AttendanceChildHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView time;
        public TextView absent;
        public AttendanceItem refferalItem;

        public AttendanceChildHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.attendance_date);
            time = (TextView) itemView.findViewById(R.id.attendance_time);
            absent = (TextView) itemView.findViewById(R.id.attendance_absent);
        }
    }

    public static class AttendanceItem {
        public int type;
        public String text, text2, text3;
        public List<AttendanceItem> invisibleChildren;
        public AttendanceItem() {}
        public AttendanceItem(int type, String text, String text2, String text3) {
            this.type = type;
            this.text = text;
            this.text2 = text2;
            this.text3 = text3;
        }
    }
}
