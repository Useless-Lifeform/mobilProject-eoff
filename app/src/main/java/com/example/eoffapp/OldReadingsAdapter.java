package com.example.eoffapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eoffapp.models.Meres;
import com.example.eoffapp.models.MeroOra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OldReadingsAdapter extends RecyclerView.Adapter< OldReadingsAdapter.ViewHolder > {
    private ArrayList<Meres> meresekData;
private ArrayList<Meres> meresekDataAll;
private Context context;
private int lastPosition = -1;

    public OldReadingsAdapter(Context contx, ArrayList<Meres> itemsData){
        context = contx;
        meresekData = itemsData;        meresekDataAll = itemsData;
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
            //private Button deleteButton; //kell majd kitörölhető legyen a mérés
            ViewHolder(View itemView) {
                super(itemView);
                meroszam = itemView.findViewById(R.id.meroID);
                datum = itemView.findViewById(R.id.datum);
                mertek = itemView.findViewById(R.id.mertKWH);
                itemView.findViewById(R.id.meresDeleteButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Meres meres = meresekData.get(position);
                            torolMerest(meres, position);
                        }
                    }
                });
            }
        public void bindTo(Meres currentItem) {
            meroszam.setText("Serial: " + currentItem.getMeroszama());
            datum.setText("Dátum: " + currentItem.getDate());
            mertek.setText("Érték: " + currentItem.getMeres() + " kWh");
        }
    };
    private void torolMerest(Meres meres, int position){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String uid = user.getUid();

        FirebaseFirestore.getInstance()
                .collection("readings")
                .document(meres.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    meresekData.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Mérés törölve", Toast.LENGTH_SHORT).show();
                    Log.d("OldReadings", "vmit kitoroltem");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Hiba a törlés során", Toast.LENGTH_SHORT).show();
                });
    }
}
/** 21 minit**/