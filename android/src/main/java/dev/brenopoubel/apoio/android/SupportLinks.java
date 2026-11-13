package dev.brenopoubel.apoio.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import dev.brenopoubel.apoio.Support;
import java.util.Objects;

/** Opens a reviewed support URL. LAUNCHED means only that Android accepted the Intent. */
public final class SupportLinks {
    private SupportLinks() {}
    public enum Result { LAUNCHED, UNAVAILABLE, NO_HANDLER, BLOCKED }

    public static Result open(Context context, Support.Catalog catalog,
                              Support.Channel channel, String methodId) {
        return open(context, catalog, channel, methodId, null);
    }

    /** browserPackage is the external browser selected by the host app, or null for Android's default. */
    public static Result open(Context context, Support.Catalog catalog,
                              Support.Channel channel, String methodId, String browserPackage) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(catalog, "catalog");
        Support.Method method = catalog.resolve(methodId, channel);
        if (method == null) return Result.UNAVAILABLE;
        if (browserPackage != null && (browserPackage.trim().isEmpty()
                || browserPackage.equals(context.getPackageName()))) return Result.BLOCKED;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(method.destination().toString()));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        if (browserPackage != null) intent.setPackage(browserPackage);
        if (!(context instanceof Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            return Result.LAUNCHED;
        } catch (ActivityNotFoundException e) {
            return Result.NO_HANDLER;
        } catch (SecurityException e) {
            return Result.BLOCKED;
        }
    }
}
