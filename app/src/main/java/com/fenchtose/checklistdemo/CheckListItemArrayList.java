package com.fenchtose.checklistdemo;

import com.fenchtose.checklistdemo.views.CheckListItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jay Rambhia on 04/03/15.
 */
public class CheckListItemArrayList extends ArrayList<CheckListItemView> {

    private static final int CHECKLIST_UNCHECKED = 0;

    @Override
    public boolean add(CheckListItemView item) {

        int size = this.size();
        if (size >= 2) {
            this.get(size - 2).setState(CheckListItemView.STATE_OTHER);
        }
        if (size != 0) {
            this.get(size - 1).setState(CheckListItemView.STATE_SECOND_LAST);
        }

        return super.add(item);
    }

    public CheckListItemView removeLastView() {
        int size = this.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {

        } else if (size == 2) {
            this.get(0).setState(CheckListItemView.STATE_LAST);
        } else {
            this.get(size - 2).setState(CheckListItemView.STATE_LAST);
            this.get(size - 3).setState(CheckListItemView.STATE_SECOND_LAST);

        }

        return this.remove(size - 1);
    }

    public CheckListItemView removeView(int position) {
        if (position < 0) {
            return null;
        }

        int size = this.size();

        if (position >= size) {
            return null;
        }

        if (position == size -1) {
            return this.removeLastView();
        }

        if (position == size -2) {
            if (size > 2) {
                this.get(size - 3).setState(CheckListItemView.STATE_SECOND_LAST);
            }
        }

        for (int i=position+1; i<size; i++) {
            this.get(i).decremetPosition();
        }

        this.get(position+1).setEditTextFocus();

        return this.remove(position);
    }

    public JSONArray getData() {
        JSONArray jsonArray = new JSONArray();

        for(int i=0; i<this.size(); i++) {

            String text = this.get(i).getText();
            if (text.length() > 0) {
                JSONObject jsondata = new JSONObject();
                try {
                    jsondata.put("item", text);
                    jsondata.put("status", CHECKLIST_UNCHECKED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray.put(jsondata);
            }
        }

        return jsonArray;
    }
}
