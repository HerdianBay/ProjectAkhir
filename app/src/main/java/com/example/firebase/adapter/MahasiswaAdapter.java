package com.example.firebase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.NoteListener;
import com.example.firebase.R;
import com.example.firebase.database.Mahasiswa;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {
    private ArrayList<Mahasiswa> mahasiswas = new ArrayList<>();
    private Context context;
    private NoteListener noteListener;
    private FirebaseUser firebaseUser;

    public MahasiswaAdapter(ArrayList<Mahasiswa> mahasiswas, Context context, NoteListener noteListener, FirebaseUser firebaseUser){
        this.mahasiswas = mahasiswas;
        this.context = context;
        this.noteListener = noteListener;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mahasiswa mahasiswa = mahasiswas.get(position);

        if (position % 2 == 0){
            holder.relativeLayout.setBackgroundResource(R.drawable.coba_button);
        }else{
            holder.relativeLayout.setBackgroundResource(R.drawable.coba_button1);
        }
        holder.judul.setText(mahasiswa.getNama());
        holder.deskripsi.setText(mahasiswa.getNIM());
        if(firebaseUser.getUid().equals(mahasiswa.getUid())){
            holder.button_update.setOnClickListener(view -> {
                noteListener.onClickUbah(mahasiswas.get(holder.getAdapterPosition()), holder.button_update);
            });
            holder.button_delete.setOnClickListener(view -> {
                noteListener.onClickHapus(mahasiswas.get(holder.getAdapterPosition()), holder.button_delete);
            });
        }else {
            holder.button_update.setVisibility(View.GONE);
            holder.button_delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mahasiswas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView judul, deskripsi;
        RelativeLayout relativeLayout;
        ImageButton button_delete, button_update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.mahasiswa);
            judul = itemView.findViewById(R.id.judul);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            button_delete = itemView.findViewById(R.id.delete);
            button_update = itemView.findViewById(R.id.ubah);
        }
    }
}
