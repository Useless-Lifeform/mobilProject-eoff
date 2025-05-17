package com.example.eoffapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eoffapp.models.Meres;
import com.example.eoffapp.models.MeroOra;

import java.util.ArrayList;

public class OldReadingsAdapter extends RecyclerView.Adapter< OldReadingsAdapter.ViewHolder > {
    private ArrayList<Meres> meresekData;
private ArrayList<Meres> meresekDataAll;
private Context context;
private int lastPosition = -1;

    public OldReadingsAdapter(Context contx, ArrayList<Meres> itemsData){
        context = contx;
        meresekData = itemsData;
        meresekDataAll = itemsData;
    }

        @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.one_card, parent, false));

    }

    @Override
    public void onBindViewHolder( OldReadingsAdapter.ViewHolder holder, int position) {
        Meres currentItem = meresekData.get(position);
        holder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return meresekData.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
            private TextView meroszam;
            private TextView datum;
            private TextView mertek;
            //private Button deleteButton;
            ViewHolder(View itemView) {
                super(itemView);
                meroszam = itemView.findViewById(R.id.meroID);
                datum = itemView.findViewById(R.id.datum);
                mertek = itemView.findViewById(R.id.mertKWH);
                itemView.findViewById(R.id.buziDeleteButton).setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //visszaszólunk a hívó acticitinek
                    }
                });
            }
            public void bindTo(Meres currentItem) {
                meroszam.setText(currentItem.getMeroszama());
                datum.setText(currentItem.getDate());
                mertek.setText(currentItem.getMeres());
                /** 21 minit**/
            }
    };
}
