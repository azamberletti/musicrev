package com.example.andrea.musicreview.utility;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.andrea.musicreview.R;

public class CheckForm {

    private static final int MIN_PSWD_LENGTH = 6;
    private static final int PSWD_WARNING = R.string.pswd_warning;
    private static final int WARNING = R.string.warning;
    private static final int PSWD_UNMATCHING_WARNING = R.string.pwsd_unmatching_warning;
    private final Context context;

    public CheckForm(final Context context) {
        this.context = context;
    }

    public boolean isEmpty(final EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            setErrorState(editText, WARNING);
            editText.requestFocus();
            editText.addTextChangedListener(new myTextWatcher(editText));
            return true;
        } else {
            return false;
        }
    }

    public boolean pswdIsIncorrect(final EditText editText) {
        if (editText.getText().toString().trim().length()<MIN_PSWD_LENGTH) {
            setErrorState(editText, PSWD_WARNING);
            editText.requestFocus();
            editText.addTextChangedListener(new myTextWatcher(editText));
            return true;
        } else {
            return false;
        }
    }

    public boolean pswdIsSame(final EditText first, final EditText second) {
        if (!first.getText().toString().equals(second.getText().toString())) {
            setErrorState(second, PSWD_UNMATCHING_WARNING);
            second.requestFocus();
            second.addTextChangedListener(new myTextWatcher(second));
            return true;
        } else {
            return false;
        }
    }

    private void setErrorState(final EditText editText, int errorMsg) {
        editText.setError(context.getString(errorMsg));
    }

    private void removeErrorState(final EditText mEditText) {
        mEditText.setError(null);
    }

    public class myTextWatcher implements TextWatcher {

        private final EditText mEditText;

        public myTextWatcher(final EditText mEditText) {
            this.mEditText = mEditText;
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            removeErrorState(mEditText);
        }
    }

}