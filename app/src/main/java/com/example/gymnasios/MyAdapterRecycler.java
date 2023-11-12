package com.example.gymnasios;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MyAdapterRecycler extends RecyclerView.Adapter <MyViewHolder> {
    Context context;
    List<DataClass> dataClassList;

    public MyAdapterRecycler(Context context, List<DataClass> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recTitle.setText(dataClassList.get(position).getExercise());
        holder.recPriority.setText(dataClassList.get(position).getDate());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ExerciseName", dataClassList.get(holder.getAdapterPosition()).getExercise());
                intent.putExtra("FirstSet", dataClassList.get(holder.getAdapterPosition()).getSet1());
                intent.putExtra("SecondSet", dataClassList.get(holder.getAdapterPosition()).getSet2());
                intent.putExtra("ThirdSet", dataClassList.get(holder.getAdapterPosition()).getSet3());
                intent.putExtra("FourthSet", dataClassList.get(holder.getAdapterPosition()).getSet4());
                intent.putExtra("Weight1", dataClassList.get(holder.getAdapterPosition()).getWeight1());
                intent.putExtra("Weight2", dataClassList.get(holder.getAdapterPosition()).getWeight2());
                intent.putExtra("Weight3", dataClassList.get(holder.getAdapterPosition()).getWeight3());
                intent.putExtra("Weight4", dataClassList.get(holder.getAdapterPosition()).getWeight4());
                intent.putExtra("Key", dataClassList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataClassList = searchList;
        notifyDataSetChanged();
    }

    public void sortDataByDate() {
        Collections.sort(dataClassList, new Comparator<DataClass>() {
            @Override
            public int compare(DataClass data1, DataClass data2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date1 = dateFormat.parse(data1.getDate());
                    Date date2 = dateFormat.parse(data2.getDate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView recTitle, recPriority;

    CardView recCard;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        recTitle = itemView.findViewById(R.id.recTitle);
        recPriority = itemView.findViewById(R.id.recPriority);
        recCard = itemView.findViewById(R.id.recCard);
    }
}