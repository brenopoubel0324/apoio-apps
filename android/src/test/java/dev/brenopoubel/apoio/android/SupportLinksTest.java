package dev.brenopoubel.apoio.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import dev.brenopoubel.apoio.Support;
import java.util.Collections;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** JVM contract tests with mocked Android API; not an emulator/device test. */
public class SupportLinksTest {
    private Support.Catalog catalog(boolean enabled, boolean reviewed) {
        return new Support.Catalog("sample", Collections.singletonList(new Support.Method(
                "coffee", "Apoiar", "Example", Support.Provider.KO_FI,
                "https://ko-fi.com/example", null, enabled, reviewed)));
    }

    @Test public void unavailableMethodsNeverStartAnActivity() {
        Context context = mock(Context.class);
        for (Support.Channel channel : new Support.Channel[] {Support.Channel.GOOGLE_PLAY, Support.Channel.UNKNOWN, null}) {
            assertEquals(SupportLinks.Result.UNAVAILABLE, SupportLinks.open(context, catalog(true, true), channel, "coffee"));
        }
        assertEquals(SupportLinks.Result.UNAVAILABLE, SupportLinks.open(context, catalog(false, true), Support.Channel.GITHUB_APK, "coffee"));
        assertEquals(SupportLinks.Result.UNAVAILABLE, SupportLinks.open(context, catalog(true, false), Support.Channel.GITHUB_APK, "coffee"));
        verifyNoInteractions(context);
    }

    @Test public void explicitSelfRoutingIsBlockedBeforeCreatingAnIntent() {
        Context context = mock(Context.class);
        when(context.getPackageName()).thenReturn("dev.sample.router");
        assertEquals(SupportLinks.Result.BLOCKED, SupportLinks.open(context, catalog(true, true),
                Support.Channel.GITHUB_APK, "coffee", "dev.sample.router"));
        verify(context, never()).startActivity(any(Intent.class));
    }

    @Test public void externalBrowserReceivesTheOriginalUrlAndApplicationContextFlag() {
        Context context = mock(Context.class);
        when(context.getPackageName()).thenReturn("dev.sample.router");
        Uri uri = mock(Uri.class);
        try (MockedStatic<Uri> uris = mockStatic(Uri.class);
             MockedConstruction<Intent> intents = mockConstruction(Intent.class, (intent, args) -> {
                 assertEquals(Intent.ACTION_VIEW, args.arguments().get(0));
                 assertSame(uri, args.arguments().get(1));
             })) {
            uris.when(() -> Uri.parse("https://ko-fi.com/example")).thenReturn(uri);
            assertEquals(SupportLinks.Result.LAUNCHED, SupportLinks.open(context, catalog(true, true),
                    Support.Channel.GITHUB_APK, "coffee", "com.example.browser"));
            Intent intent = intents.constructed().get(0);
            verify(intent).addCategory(Intent.CATEGORY_BROWSABLE);
            verify(intent).setPackage("com.example.browser");
            verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            verify(context).startActivity(intent);
        }
    }

    @Test public void activityContextNeedsNoNewTaskFlag() {
        Activity context = mock(Activity.class);
        try (MockedStatic<Uri> uris = mockStatic(Uri.class);
             MockedConstruction<Intent> intents = mockConstruction(Intent.class)) {
            assertEquals(SupportLinks.Result.LAUNCHED, SupportLinks.open(context, catalog(true, true),
                    Support.Channel.GITHUB_APK, "coffee"));
            verify(intents.constructed().get(0), never()).addFlags(anyInt());
            verify(intents.constructed().get(0), never()).setPackage(anyString());
        }
    }

    @Test public void missingHandlerIsReportedWithoutRetry() {
        Context context = mock(Context.class);
        ActivityNotFoundException failure = mock(ActivityNotFoundException.class);
        doThrow(failure).when(context).startActivity(any(Intent.class));
        try (MockedStatic<Uri> uris = mockStatic(Uri.class);
             MockedConstruction<Intent> intents = mockConstruction(Intent.class)) {
            assertEquals(SupportLinks.Result.NO_HANDLER, SupportLinks.open(context, catalog(true, true),
                    Support.Channel.GITHUB_APK, "coffee"));
            verify(context, times(1)).startActivity(any(Intent.class));
        }
    }

    @Test public void platformSecurityFailureIsReportedWithoutRetry() {
        Context context = mock(Context.class);
        doThrow(new SecurityException("blocked")).when(context).startActivity(any(Intent.class));
        try (MockedStatic<Uri> uris = mockStatic(Uri.class);
             MockedConstruction<Intent> intents = mockConstruction(Intent.class)) {
            assertEquals(SupportLinks.Result.BLOCKED, SupportLinks.open(context, catalog(true, true),
                    Support.Channel.GITHUB_APK, "coffee"));
            verify(context, times(1)).startActivity(any(Intent.class));
        }
    }
}
