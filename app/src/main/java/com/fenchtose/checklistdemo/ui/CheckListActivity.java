package com.fenchtose.checklistdemo.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.fenchtose.checklistdemo.R;

public class CheckListActivity extends ActionBarActivity implements CheckListView {

    public static final int ADD_NEW_ITEM = 1;
    public static final int REMOVE_LAST_ITEM = 2;
    public static final int GOT_FOCUS = 3;
    public static final int REMOVE_ITEM = 4;

    private LinearLayout container;
    private CheckListPresenter presenter;

    private String jsonArrayStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            jsonArrayStr = savedInstanceState.getString("json_array", "");
        }

        setContentView(R.layout.activity_check_list);
        container = (LinearLayout)findViewById(R.id.view_container);

        presenter = new CheckListPresenterImpl(this, getApplicationContext());

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("json_array", presenter.getCheckListData());
    }

    @Override
    public LinearLayout getContainer() {
        return container;
    }

    @Override
    public String getSavedData() {
        return jsonArrayStr;
    }

}
