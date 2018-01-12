package com.wauoen.launchmodedemo;

import java.util.Stack;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TaskInfoDisplayer implements Runnable, Constants {

    private final BaseApplication app;
    private final TextView taskIdField;
//    private final LinearLayout taskView;
    private final LinearLayout backStack;
    private int height = 100;
    private int margin = 10;

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public TaskInfoDisplayer(BaseActivity baseActivity) {
        app = (BaseApplication) baseActivity.getApplication();
        taskIdField = (TextView) baseActivity.findViewById(R.id.task_id_field);
//        taskView = (LinearLayout) baseActivity.findViewById(R.id.task_view);
        backStack = (LinearLayout) baseActivity.findViewById(R.id.back_stack);

        Log.v("ww","backStackChild:"+backStack.getChildCount()+"");
//        taskView.removeAllViews();
    }

    @Override
    public void run() {
        Log.v(LOG_TAG, "===============================");
//        showCurrentTaskId();
        backStack.removeAllViews();
        backStack.postInvalidate();
        showBackStack();
//        showCurrentTaskActivites();
        Log.v(LOG_TAG, "===============================");
    }

    public void runA() {
        Log.v(LOG_TAG, "===============================");
//        showCurrentTaskId();
        backStack.removeAllViews();
        backStack.postInvalidate();
        showBackStack();
//        showCurrentTaskActivites();
        Log.v(LOG_TAG, "===============================");
    }

    private void showBackStack() {
        for (int i = 0; i <app.backStack.size(); i++){

            LinearLayout taskView = showCurrentTaskActivites(Integer.valueOf(app.backStack.get(i)));
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(4,4,20,4);
            backStack.addView(taskView,layoutParams);
            Log.v("ww","backStackSize:"+app.backStack.size()+" backStackAddView:"+backStack.getChildCount()+"");
        }
    }

    private void showCurrentTaskId() {
        int taskId = app.getCurrentTaskId();
        String taskMessage = "Activities in current task (id: " + taskId + ")";
        taskIdField.setText("Task id: " + taskId);
        Log.v(LOG_TAG, taskMessage);
    }

    private LinearLayout showCurrentTaskActivites(int taskId) {
        Stack<BaseActivity> task = app.getCurrentTask(taskId);
        LinearLayout taskView = (LinearLayout) LayoutInflater.from(app).inflate(R.layout.layout_task,null);
        LinearLayout taskViewContent = (LinearLayout) taskView.findViewById(R.id.taskViewContent);
        LinearLayout taskViewContentReal = (LinearLayout) taskViewContent.findViewById(R.id.task_view);
        TextView tv_taskId = (TextView) taskViewContent.findViewById(R.id.task_id_field);
        tv_taskId.setText("Task id: " + taskId);
        for (int location = task.size() - 1; location >= 0; location--) {
            BaseActivity activity = task.get(location);
            showActivityDetails(activity,taskViewContentReal);
        }

        return taskView;
    }

    private void showActivityDetails(BaseActivity activity,LinearLayout taskView) {
        String activityName = activity.getClass().getSimpleName();
        Log.v(LOG_TAG, activityName);
        ImageView activityRepresentation = getActivityRepresentation(activity);
        taskView.addView(activityRepresentation);
    }

    private ImageView getActivityRepresentation(BaseActivity activity) {
        ImageView image = new ImageView(activity);
        int color = activity.getBackgroundColour();
        image.setBackgroundResource(color);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, height);
        params.setMargins(5, margin, 5, 5);
        image.setLayoutParams(params);
        return image;
    }
}
