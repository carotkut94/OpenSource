package com.death.yttorrents.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by rajora_sd on 4/27/2017.
 */

public class UrlUtils {
    public static String Url = "https://yts.ag/api/v2/list_movies.json?limit=50";
    public static String getURL()
    {
       return Url;
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
