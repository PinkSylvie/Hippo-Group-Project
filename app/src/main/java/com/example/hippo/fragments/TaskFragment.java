package com.example.hippo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hippo.R;
import com.example.hippo.Task;
import com.example.hippo.TasksAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    public static final String TAG = "taskFragment";
    private RecyclerView rvTasks;
    protected TasksAdapter adapter;
    protected List<Task> allTasks;
    SwipeRefreshLayout swipeContainer;


    public TaskFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTasks = view.findViewById(R.id.rvTasks);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                queryTasks();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_purple);

        rvTasks  = view.findViewById(R.id.rvTasks);
        allTasks = new ArrayList<>();
        adapter = new TasksAdapter(getContext(), allTasks);

        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        queryTasks();

    }

    protected void queryTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.setLimit(20);
        query.orderByAscending(Task.KEY_DUE_TIME);
        query.findInBackground(new FindCallback<Task>() {

            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Task task : tasks){
                    Log.i(TAG,"Task: " + task.getDescription() + ", username: " + task.getUser().getUsername());
                }
                adapter.clear();
                adapter.addAll(tasks);
                swipeContainer.setRefreshing(false);

            }
        });
    }
}
