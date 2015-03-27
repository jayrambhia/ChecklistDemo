package com.fenchtose.checklistdemo.views;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fenchtose.checklistdemo.ui.CheckListActivity;
import com.fenchtose.checklistdemo.utils.CheckListCallback;
import com.fenchtose.checklistdemo.R;

/**
 * Created by Jay Rambhia on 04/03/15.
 */
public class CheckListItemView extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {

    public static final int STATE_LAST = 1;
    public static final int STATE_SECOND_LAST = 2;
    public static final int STATE_OTHER = 3;

    public static final int MODE_EDITING = 5;
    public static final int MODE_SAVED = 6;

    private ImageView imageView;
    private CheckListEditText editText;
    private ImageView removeButton;

    private int position;

    private int state;
    private int mode;

    private Drawable drawable_state_edit;
    private Drawable drawable_state_saved;

    private CheckListCallback callback;

    private final Context context;

    public CheckListItemView(Context context, CheckListCallback callback, int position) {
        super(context);
        this.callback = callback;
        this.context = context;
        this.position = position;
        setupLayout();

    }

    public CheckListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupLayout();
    }

    public CheckListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setupLayout();
    }

    private void setupLayout() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.checklist_note_object_viewgroup_layout, this, true);

        editText = (CheckListEditText)findViewById(R.id.checklist_edittext);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);

        imageView = (ImageView)findViewById(R.id.checklist_imageview);

        removeButton = (ImageView)findViewById(R.id.checklist_remove_button);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.invoke(new Pair(CheckListActivity.REMOVE_ITEM, position));
                }
            }
        });

        state = STATE_LAST;
        mode = MODE_EDITING;

        drawable_state_edit = context.getResources().getDrawable(R.mipmap.ic_add_grey600_24dp);
        drawable_state_saved = context.getResources().getDrawable(R.mipmap.ic_check_box_outline_blank_black_24dp);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (state != STATE_LAST) {
            imageView.setImageDrawable(drawable_state_saved);
        } else {
            imageView.setImageDrawable(drawable_state_edit);
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void decremetPosition() {
        this.position -= 1;
    }

    public void incrementPosition() {
        this.position += 1;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (state == STATE_LAST && s.toString().length() > 0) {
            // add new view
            if (callback != null) {
                callback.invoke(new Pair(CheckListActivity.ADD_NEW_ITEM, -1));
            }
        } else if (state == STATE_SECOND_LAST && s.toString().length() == 0) {
            if (callback != null) {
                callback.invoke(new Pair(CheckListActivity.REMOVE_LAST_ITEM, -1));
                imageView.setImageDrawable(drawable_state_edit);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus) {
            if (callback != null) {
                callback.invoke(new Pair(CheckListActivity.GOT_FOCUS, position));
            }
        } else {
            if (mode == MODE_EDITING) {
                mode = MODE_SAVED;
            }
        }

        if (state != STATE_LAST) {
            if (hasFocus) {
                removeButton.setVisibility(View.VISIBLE);
            } else {
                removeButton.setVisibility(View.GONE);
            }
        }

        if (!hasFocus && state != STATE_LAST) {
            imageView.setImageDrawable(drawable_state_saved);
        }
    }

    public void setEditTextFocus() {
        editText.requestFocus();
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }

}
