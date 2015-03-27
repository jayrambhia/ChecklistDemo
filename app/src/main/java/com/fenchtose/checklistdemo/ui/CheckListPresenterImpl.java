package com.fenchtose.checklistdemo.ui;

import android.content.Context;
import android.os.Handler;
import android.util.Pair;
import android.widget.LinearLayout;

import com.fenchtose.checklistdemo.CheckListItemArrayList;
import com.fenchtose.checklistdemo.utils.CheckListCallback;
import com.fenchtose.checklistdemo.views.CheckListItemView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jay Rambhia on 27/03/15.
 */
public class CheckListPresenterImpl implements CheckListPresenter {

    private CheckListView checkListView;
    private Context context;
    private LinearLayout container;
    private CheckListItemArrayList itemArray;
    private boolean blockAddition;

    public CheckListPresenterImpl(CheckListView view, Context ctx) {
        checkListView = view;
        context = ctx;
        itemArray = new CheckListItemArrayList();
        container = checkListView.getContainer();

        final String jsonArrayStr = checkListView.getSavedData();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (jsonArrayStr != null && !jsonArrayStr.isEmpty()) {
                    try {
                        loadCheckList(new JSONArray(jsonArrayStr));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addNewItem();
                }
            }
        }, 500);
    }

    @Override
    public String getCheckListData() {
        JSONArray jsonArray = itemArray.getData();
        if (jsonArray.length() == 0) {
            return "";
        }
        return jsonArray.toString();
    }

    private void loadCheckList(JSONArray jsonArray) {
        blockAddition = true;
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                String item_text = jsonArray.getJSONObject(i).getString("item");
                int state = CheckListItemView.STATE_OTHER;
                if (i == jsonArray.length()-1) {
                    state = CheckListItemView.STATE_SECOND_LAST;
                }
                addNewItem(item_text, state);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        addNewItem();
        blockAddition = false;
    }

    private void addNewItem(String text, int state) {
        CheckListItemView view = new CheckListItemView(context,
                new ChangeCallback(),
                itemArray.size());

        itemArray.add(view);
        container.addView(view);
        view.setText(text);
        view.setState(state);
    }

    private void addNewItem() {
        CheckListItemView view = new CheckListItemView(context,
                new ChangeCallback(),
                itemArray.size());
        itemArray.add(view);
        container.addView(view);
    }

    private void removeLastItem() {
        CheckListItemView view = itemArray.removeLastView();
        container.removeView(view);
    }

    private void removeItem(int position) {
        CheckListItemView view = itemArray.removeView(position);
        container.removeView(view);
    }



    private class ChangeCallback implements CheckListCallback {

        @Override
        public boolean invoke(Object data) {

            Pair pair = (Pair)data;

            int change = (int)pair.first;
            int position = (int)pair.second;

            switch(change) {
                case CheckListActivity.ADD_NEW_ITEM:
                    if (!blockAddition) {
                        addNewItem();
                    }
                    break;
                case CheckListActivity.REMOVE_LAST_ITEM:
                    removeLastItem();
                    break;
                case CheckListActivity.GOT_FOCUS:
                    // do something
                    break;
                case CheckListActivity.REMOVE_ITEM:
                    removeItem(position);
                    break;
            }

            return false;
        }
    }
}
