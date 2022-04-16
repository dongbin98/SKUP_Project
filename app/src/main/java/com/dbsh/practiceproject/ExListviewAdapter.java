package com.dbsh.practiceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExListviewAdapter extends BaseExpandableListAdapter {
    ArrayList<ScoreParent> scoreParent;
    ArrayList<ArrayList<ScoreChild>> scoreChild;

    @Override
    public int getGroupCount() {
        return scoreParent.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return scoreChild.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return scoreParent.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return scoreChild.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parent_list, viewGroup, false);
        }
        TextView yearterm = (TextView) view.findViewById(R.id.yearterm);
        TextView totCredit = (TextView) view.findViewById(R.id.totCredit);
        TextView totGrade = (TextView) view.findViewById(R.id.totGrade);
        TextView totRank = (TextView) view.findViewById(R.id.totRank);

        yearterm.setText(scoreParent.get(i).getYearterm());
        totCredit.setText(scoreParent.get(i).getCredit());
        totGrade.setText(scoreParent.get(i).getGrade());
        totRank.setText(scoreParent.get(i).getRank());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_list, viewGroup, false);
        }

        TextView subjectName = (TextView) view.findViewById(R.id.subjectName);
        TextView subjectRank = (TextView) view.findViewById(R.id.subjectRank);
        TextView subjectGrade = (TextView) view.findViewById(R.id.subjectGrade);
        TextView subjectDivide = (TextView) view.findViewById(R.id.subjectDivide);
        TextView subjectCredit = (TextView) view.findViewById(R.id.subjectCredit);
        TextView subjectNumber = (TextView) view.findViewById(R.id.subjectNumber);
        TextView subjectProf = (TextView) view.findViewById(R.id.subjectProf);

        subjectName.setText(scoreChild.get(i).get(i1).getSubjectName());
        subjectRank.setText(scoreChild.get(i).get(i1).getSubjectRank());
        subjectGrade.setText(scoreChild.get(i).get(i1).getSubjectGrade());
        subjectDivide.setText(scoreChild.get(i).get(i1).getSubjectDivide());
        subjectCredit.setText(scoreChild.get(i).get(i1).getSubjectCredit());
        subjectNumber.setText(scoreChild.get(i).get(i1).getSubjectNumber());
        subjectProf.setText(scoreChild.get(i).get(i1).getSubjectProf());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void addItem(int groupPosition, ScoreChild item) {
        scoreChild.get(groupPosition).add(item);
    }

    public void removeChild(int groupPosition, int childPosition) {
        scoreChild.get(groupPosition).remove(childPosition);
    }
}