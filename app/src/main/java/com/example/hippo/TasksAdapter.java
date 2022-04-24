package com.example.hippo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;


import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;

    public TasksAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void clear() {
        tasks.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Task> list) {
        tasks.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout container;
        private TextView tvTitle;
        private ImageView ivImage;
        private TextView tvDuetime;
        private TextView tvDescription;
        private CheckBox cbDone;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDuetime = itemView.findViewById(R.id.tvDuetime);
            cbDone = itemView.findViewById(R.id.cbDone);
        }

        public void bind(Task task) {
            tvTitle.setText(task.getTitle());
            tvDuetime.setText("Due date: "+ task.getDueTime().toString());
            tvDescription.setText(task.getDescription());
            ParseFile image = task.getAttachment();
            if (image != null){
                Glide.with(context).load(task.getAttachment().getUrl()).into(ivImage);
            }
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("title", task.getTitle());
                    i.putExtra("description", task.getDescription());
                    i.putExtra("duetime", "Due date: "+ task.getDueTime().toString());
//                    i.putExtra("image", task.getAttachment());

                    context.startActivity(i);

                }
            });

        }
    }
}
