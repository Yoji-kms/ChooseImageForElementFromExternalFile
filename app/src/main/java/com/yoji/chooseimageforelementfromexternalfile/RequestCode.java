package com.yoji.chooseimageforelementfromexternalfile;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({RequestCode.IMAGE_FILE_NAME, RequestCode.READ_STORAGE})
public @interface RequestCode {
    int IMAGE_FILE_NAME = 10;
    int READ_STORAGE = 90;
}
