package com.example.hippo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.ViewHolder> {

    public static final String TAG = "MonthsAdapter";

    Context context;
    List<HippoMonth> months;

    public MonthsAdapter(Context context, List<HippoMonth> months){
        this.context = context;
        this.months = months;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HippoMonth month = months.get(position);
        holder.bind(month);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMonth;
        TextView tvYear;
        TableLayout tlMonth;
        RecyclerView rvTasks;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvYear = itemView.findViewById(R.id.tvYear);
            tlMonth = itemView.findViewById(R.id.tlMonth);
            rvTasks = itemView.findViewById(R.id.rvMonthTasks);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rvTasks.setLayoutManager(layoutManager);
        }
        //Populate
        public void bind(HippoMonth month){
            tvYear.setText(String.valueOf(month.getYear()));
            tvMonth.setText(month.getMonth());
            month.arrangeDays(tlMonth);

            CalendarTasksAdapter adapter = new CalendarTasksAdapter(context, month);
            rvTasks.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }
}
