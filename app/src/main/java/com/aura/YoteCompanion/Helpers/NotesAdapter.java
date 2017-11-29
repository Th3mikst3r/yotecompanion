package com.aura.YoteCompanion.Helpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aura.YoteCompanion.Models.Note;
import com.aura.YoteCompanion.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private List<Note> notesList;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, details, date;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.lbl_title);
            details = (TextView) view.findViewById(R.id.lbl_details);
            date = (TextView) view.findViewById(R.id.lbl_date);
        }
    }

    public NotesAdapter(List<Note> noteList) {
        this.notesList = noteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return  new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.title.setText(note.getTitle().toUpperCase());
        holder.details.setText(note.getDetails());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String reportDate = df.format(note.getDateSaved());
        holder.date.setText(reportDate);
    }

    @Override
    public int getItemCount(){
        return  notesList.size();
    }
}
