package com.mairos.ymoneytransfer.storage;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface SharedPrefs {
    @DefaultString("")
    String token();
}