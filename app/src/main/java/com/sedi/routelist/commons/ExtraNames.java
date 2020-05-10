package com.sedi.routelist.commons;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ExtraNames {

    @StringDef({KeysField.KEY_EXTRA_NOTICE})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_EXTRA_NOTICE = "EXTRA_NOTICE";
    }
}