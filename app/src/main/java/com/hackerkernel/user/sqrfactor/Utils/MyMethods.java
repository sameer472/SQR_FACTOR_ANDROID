package com.hackerkernel.user.sqrfactor.Utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyMethods {


    public static String convertDate(String strDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                .parse(strDate);
        Date currentDate = new Date();
        CharSequence cs = DateUtils.getRelativeTimeSpanString(date.getTime(),
                currentDate.getTime(), DateUtils.SECOND_IN_MILLIS);
        return (String) cs;
    }
}
