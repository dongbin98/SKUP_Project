package com.dbsh.practiceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.NameList;

import java.util.ArrayList;

public class ScholarAdapter extends RecyclerView.Adapter<ScholarAdapter.ScholarHolder> {

    ArrayList<String> scholarNameList;      // 장학명
    ArrayList<String> scholarWonList;       // 금액
    ArrayList<String> scholarExplainList;   // 비고

    ScholarAdapter(ArrayList<String> scholarNameList,
                   ArrayList<String> scholarWonList,
                   ArrayList<String> scholarExplainList)
    {
        this.scholarExplainList = scholarExplainList;
        this.scholarNameList = scholarNameList;
        this.scholarWonList = scholarWonList;
    }
    @NonNull
    @Override
    public ScholarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.scholarship_list, parent, false);
        return new ScholarHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ScholarHolder holder, int position) {
        holder.scholarName.setText(scholarNameList.get(position));
        holder.scholarWon.setText(scholarWonList.get(position));
        holder.scholarExplain.setText(scholarExplainList.get(position));
    }

    @Override
    public int getItemCount() {
        return scholarNameList.size();
    }

    class ScholarHolder extends RecyclerView.ViewHolder {

        TextView scholarName;
        TextView scholarWon;
        TextView scholarExplain;

        public ScholarHolder(@NonNull View itemView) {
            super(itemView);
            scholarName = itemView.findViewById(R.id.scholarName);
            scholarWon = itemView.findViewById(R.id.scholarWon);
            scholarExplain = itemView.findViewById(R.id.scholarExplain);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Toast.makeText(view.getContext(), "장학 클릭", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}