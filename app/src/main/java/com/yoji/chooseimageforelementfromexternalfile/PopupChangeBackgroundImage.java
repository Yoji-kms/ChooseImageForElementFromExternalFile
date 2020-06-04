package com.yoji.chooseimageforelementfromexternalfile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PopupChangeBackgroundImage extends Activity {

    private EditText imageFileNameEdtTxt;
    private Button okButton;

    private String imageFileName;

    private TextWatcher imageFileNameTxtWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            imageFileName = imageFileNameEdtTxt.getText().toString();
            okButton.setEnabled(!imageFileName.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private View.OnClickListener okButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageFileName = imageFileNameEdtTxt.getText().toString().trim();
            Intent intent = new Intent();
            intent.putExtra("result", imageFileName);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private View.OnClickListener cancelButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_change_background_image);
        setWidthAndHeight();

        initViews();
    }

    private void setWidthAndHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;

        int widthPercent = getResources().getInteger(R.integer.popup_width_percent);

        getWindow().setLayout(width*widthPercent/100, height);
    }

    private void initViews(){
        imageFileNameEdtTxt = findViewById(R.id.fileNameEdtTxtId);
        okButton = findViewById(R.id.okButtonId);
        Button cancelButton = findViewById(R.id.cancelButtonId);

        imageFileNameEdtTxt.addTextChangedListener(imageFileNameTxtWatcher);
        okButton.setOnClickListener(okButtonOnClickListener);
        cancelButton.setOnClickListener(cancelButtonOnClickListener);
    }
}
