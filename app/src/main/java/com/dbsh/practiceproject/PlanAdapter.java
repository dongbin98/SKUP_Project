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

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanHolder> implements PlanClickListener {

    PlanClickListener listener;         // 여기서 클릭 시 다이얼로그를 띄울 수 없기 때문에
                                        // 클릭 이벤트 발생 시 해당 이벤트를 액티비티에 전달하기 위함

    ArrayList<String> subjectCDList;
    ArrayList<String> subjectNameList;
    ArrayList<String> deptNameList;
    ArrayList<String> professorNameList;
    ArrayList<String> classNumberList;
    ArrayList<String> professorIDList;

    PlanAdapter(ArrayList<String> subjectCDList,
                 ArrayList<String> subjectNameList,
                 ArrayList<String> deptNameList,
                 ArrayList<String> professorNameList,
                ArrayList<String> classNumberList,
                ArrayList<String> professorIDList) {
        this.subjectCDList = subjectCDList;
        this.subjectNameList = subjectNameList;
        this.deptNameList = deptNameList;
        this.professorNameList = professorNameList;
        this.classNumberList = classNumberList;
        this.professorIDList = professorIDList;
    }
    @NonNull
    @Override
    public PlanAdapter.PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.lectureplan_list, parent, false);
        return new PlanAdapter.PlanHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.PlanHolder holder, int position) {
        holder.lectureplan_subject.setText(subjectNameList.get(position));
        holder.lectureplan_cd.setText(subjectCDList.get(position));
        holder.lectureplan_professor.setText(professorNameList.get(position));
    }

    @Override
    public int getItemCount() {
        if(subjectCDList == null)
            return 0;
        else
            return subjectCDList.size();
    }

    public String getSubjectCD(int position) { return subjectCDList.get(position); }
    public String getSubjectName(int position) { return subjectNameList.get(position); }
    public String getDeptName(int position) { return deptNameList.get(position); }
    public String getProfessorName(int position) { return professorNameList.get(position); }
    public String getClassNumber(int position) { return classNumberList.get(position); }
    public String getProfessorID(int position) { return professorIDList.get(position); }

    public void setFilter(ArrayList<String> filteredSubjectCDList,
                        ArrayList<String> filteredSubjectNameList,
                        ArrayList<String> filteredDeptNameList,
                        ArrayList<String> filteredProfessorNameList,
                          ArrayList<String> filteredClassNumberList,
                          ArrayList<String> filteredProfessorIDList)
    {
        this.subjectCDList = filteredSubjectCDList;
        this.subjectNameList = filteredSubjectNameList;
        this.deptNameList = filteredDeptNameList;
        this.professorNameList = filteredProfessorNameList;
        this.classNumberList = filteredClassNumberList;
        this.professorIDList = filteredProfessorIDList;
        notifyDataSetChanged();
    }

    @Override   // PlanClickListener를 위한 오버라이딩 함수
    public void onItemClick(PlanHolder holder, View view, int position) {
        if(listener != null)
            listener.onItemClick(holder,view,position);
    }
    public void setOnItemClicklistener(PlanClickListener listener) {
        this.listener = listener;
    }


    class PlanHolder extends RecyclerView.ViewHolder {

        TextView lectureplan_subject;
        TextView lectureplan_cd;
        TextView lectureplan_professor;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            lectureplan_subject = itemView.findViewById(R.id.lectureplan_subject);
            lectureplan_cd = itemView.findViewById(R.id.lectureplan_cd);
            lectureplan_professor = itemView.findViewById(R.id.lectureplan_professor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if(listener != null)
                            listener.onItemClick(PlanHolder.this, view, pos);
                    }
                }
            });
        }
    }
}