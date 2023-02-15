package de.adv.guimaster.frontend.uitools;

import android.content.res.Resources;
import android.util.TypedValue;

public class DynamicUnitUtils {

    /**
     * Converts DP into pixels.
     *
     * @param dp The value in DP to be converted into pixels.
     *
     * @return The converted value in pixels.
     */
    public static int convertDpToPixels(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().getDisplayMetrics()));
    }

    /**
     * Converts pixels into DP.
     *
     * @param pixels The value in pixels to be converted into DP.
     *
     * @return The converted value in DP.
     */
    public static int convertPixelsToDp(int pixels) {
        return Math.round(pixels / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Converts SP into pixels.
     *
     * @param sp The value in SP to be converted into pixels.
     *
     * @return The converted value in pixels.
     */
    public static int convertSpToPixels(float sp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, Resources.getSystem().getDisplayMetrics()));
    }

    /**
     * Converts pixels into SP.
     *
     * @param pixels The value in pixels to be converted into SP.
     *
     * @return The converted value in SP.
     */
    public static int convertPixelsToSp(int pixels) {
        return Math.round(pixels / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Converts DP into SP.
     *
     * @param dp The value in DP to be converted into SP.
     *
     * @return The converted value in SP.
     */
    public static int convertDpToSp(float dp) {
        return Math.round(convertDpToPixels(dp) / (float) convertSpToPixels(dp));
    }
}
