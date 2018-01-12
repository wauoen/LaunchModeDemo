package com.wauoen.launchmodedemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningTaskInfo;
import android.util.Log;

public class BaseApplication extends Application{

	private HashMap<Integer, Stack<BaseActivity>> tasks;
	public List<String> backStack ;
	
	private ActivityManager manager;
	
	private boolean intentFilterMode;
	
	@Override
	public void onCreate() {
		super.onCreate();
		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		tasks = new HashMap<Integer, Stack<BaseActivity>>();
		backStack = new ArrayList<>();
	}
	
	public void pushToStack(BaseActivity activity){
		int currentTaskId = getCurrentTaskId();
		if(!tasks.containsKey(currentTaskId)){
			tasks.put(currentTaskId, new Stack<BaseActivity>());
		}
		Stack<BaseActivity> stack = tasks.get(currentTaskId);
		stack.add(activity);
		if (backStack.contains(currentTaskId+"")){
			backStack.remove(currentTaskId+"");
		}
		backStack.add(0,currentTaskId+"");
	}

	List<OnActivityRemovedListener> listeners = new ArrayList<>();
	
	public int getCurrentTaskId() {
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
		RunningTaskInfo runningTask = runningTasks.get(0);
		return runningTask.id;
	}

	public void removeFromStack(BaseActivity activity, int removedTaskId){
		Log.v("ww","removedTaskId:"+removedTaskId);
		Stack<BaseActivity> stack = tasks.get(removedTaskId);
		if(stack != null){
			stack.pop();
		}
		if(stack.empty()){
			tasks.remove(removedTaskId);
		}
		if (backStack.contains(removedTaskId+"")){
			backStack.remove(removedTaskId+"");
		}
		if (!stack.empty()){
			backStack.add(0,removedTaskId+"");
		}
		Log.v("ww","backStackSize:"+backStack.size()+" tasksSize:"+tasks.size());
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).onActivityRemoved();
		}

	}


	public void setOnActivityRemovedListener(OnActivityRemovedListener onActivityRemovedListener) {
		listeners.add(onActivityRemovedListener);
	}

	interface OnActivityRemovedListener{
		void onActivityRemoved();
	}

	public Stack<BaseActivity> getCurrentTask(){
		return tasks.get(getCurrentTaskId());
	}

	public Stack<BaseActivity> getCurrentTask(int taskId){
		return tasks.get(taskId);
	}


	
	public void toggleIntentFilterMode(){
		intentFilterMode = !intentFilterMode;
	}
	
	public boolean isIntentFilterMode(){
		return intentFilterMode;
	}
	
}
