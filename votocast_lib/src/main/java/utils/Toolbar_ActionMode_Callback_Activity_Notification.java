package utils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.votocast.votocast.R;
import com.votocast.votocast.R2;

import java.util.ArrayList;

import class_adapter.ActivityAdapter;
import db.ActivityClass;
import fragments.ActivityFragment;

/**
 * Created by Admin on 7/25/2017.
 */

public class Toolbar_ActionMode_Callback_Activity_Notification implements ActionMode.Callback {
    private Activity activity;
    private ActivityAdapter listView_adapter;
    private ArrayList<ActivityClass> message_models;
    private boolean isListViewFragment;
    private ActionMode mActionMode;

    public Toolbar_ActionMode_Callback_Activity_Notification(Activity context, ActivityAdapter listView_adapter, ArrayList<ActivityClass> message_models, boolean isListViewFragment) {
        this.activity = context;
        this.listView_adapter = listView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }

    public Toolbar_ActionMode_Callback_Activity_Notification(Activity context, ActivityAdapter listView_adapter, ArrayList<ActivityClass> message_models, boolean isListViewFragment,ActionMode mActionMode) {
        this.activity = context;
        this.listView_adapter = listView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
        this.mActionMode=mActionMode;
    }

    public ActionMode getmActionMode() {
        return mActionMode;
    }

    public void setmActionMode(ActionMode mActionMode) {
        this.mActionMode = mActionMode;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_notification_contextual, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Fragment listFragment = new ActivityFragment();//Get list view Fragment

        switch (item.getItemId()) {
            case R2.id.action_delete:
//                listView_adapter.deleteRow(mode);
//                confirmDelete(mode);
                if (listFragment != null)
                    //If list fragment is not null
                    ((ActivityFragment) listFragment).deleteRows(activity,listView_adapter,message_models,mode);//delete selected rows
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode

        listView_adapter.removeSelection();  // remove selection
        Fragment listFragment =new ActivityFragment();//Get list fragment
        if (listFragment != null)
            ((ActivityFragment) listFragment).setNullToActionMode(listView_adapter);//Set action mode null

    }
}

