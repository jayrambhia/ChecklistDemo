package com.fenchtose.checklistdemo.views;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by Jay Rambhia on 04/03/15.
 */
public class CheckListEditText extends EditText {

    private TextWatcher textWatcher;
    private boolean addedLast = false;


    private static final String TAG = "CheckListEditText";

    public CheckListEditText(Context context) {
        super(context);
    }

    public CheckListEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckListEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (textWatcher != null) {
            super.removeTextChangedListener(textWatcher);
        }

        textWatcher = watcher;
        super.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode== KeyEvent.KEYCODE_ENTER)
        {
            // Just ignore the [Enter] key
            return false;
        }
        // Handle all other keys in the default way
        return super.onKeyDown(keyCode, event);
    }

    // http://stackoverflow.com/questions/5014219/multiline-edittext-with-done-softinput-action-label-on-2-3/5037488#5037488
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int imeActions = outAttrs.imeOptions&EditorInfo.IME_MASK_ACTION;
        if ((imeActions&EditorInfo.IME_ACTION_DONE) != 0) {
            // clear the existing action
            outAttrs.imeOptions ^= imeActions;
            // set the DONE action
            outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
        }
        if ((outAttrs.imeOptions&EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return connection;
    }

    public void removeTextChangedListener() {
        if (textWatcher != null) {
            Log.i(TAG, "remove textwatcher");
            super.removeTextChangedListener(textWatcher);
        }

        textWatcher = null;
    }

    public boolean isAddedLast() {
        return addedLast;
    }

    public void setAddedLast(boolean addedLast) {
        this.addedLast = addedLast;
    }
}
