package dev.brenopoubel.apoio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SupportTest {
    private static int checks;

    public static void main(String[] args) {
        Support.Method reviewed = method("coffee", "https://ko-fi.com/example", true, true);
        Support.Method disabled = method("disabled", "https://ko-fi.com/disabled", false, true);
        Support.Method unreviewed = method("unreviewed", "https://ko-fi.com/unreviewed", true, false);
        List<Support.Method> input = new ArrayList<>(Arrays.asList(reviewed, disabled, unreviewed));
        Support.Catalog catalog = new Support.Catalog("app-example", input);
        input.clear();
        check(catalog.available(Support.Channel.GITHUB_APK).size() == 1);
        check(catalog.available(Support.Channel.WEB).get(0) == reviewed);
        check(catalog.available(Support.Channel.GOOGLE_PLAY).isEmpty());
        check(catalog.available(Support.Channel.UNKNOWN).isEmpty());
        check(catalog.available(null).isEmpty());
        check(catalog.resolve("coffee", Support.Channel.GOOGLE_PLAY) == null);
        check(catalog.resolve("disabled", Support.Channel.GITHUB_APK) == null);
        check(catalog.resolve("unreviewed", Support.Channel.GITHUB_APK) == null);
        check(catalog.resolve("missing", Support.Channel.GITHUB_APK) == null);
        check(catalog.resolve("coffee", Support.Channel.GITHUB_APK) == reviewed);
        check(reviewed.destination().toString().equals("https://ko-fi.com/example"));
        fails(() -> catalog.available(Support.Channel.WEB).clear());
        fails(() -> new Support.Catalog("app", Arrays.asList(reviewed, reviewed)));
        fails(() -> new Support.Catalog(" ", Collections.emptyList()));
        for (String bad : Arrays.asList(
                "http://ko-fi.com/example", "javascript:alert(1)",
                "https://ko-fi.com.attacker.org/example", "https://attacker.org/ko-fi.com/example",
                "https://ko-fi.com@attacker.org/example", "https://user@ko-fi.com/example",
                "https://ko-fi.com:444/example", "https://ko-fi.com/example#redirect",
                "https://ko-fi.com/", "https://ko-fi.com/example x", "//ko-fi.com/example")) {
            fails(() -> method("bad", bad, true, true));
        }
        Support.Method custom = new Support.Method("pix", "Apoiar", "Exemplo",
                Support.Provider.CUSTOM, "https://support.example.org/pix", "support.example.org", true, true);
        check(custom.destination().getHost().equals("support.example.org"));
        fails(() -> new Support.Method("pix", "Apoiar", "Exemplo", Support.Provider.CUSTOM,
                "https://other.example.org/pix", "support.example.org", true, true));
        fails(() -> new Support.Method("pix", "Apoiar", "Exemplo", Support.Provider.CUSTOM,
                "https://support.example.org/pix", "*.example.org", true, true));
        fails(() -> new Support.Method("github", "GitHub", "Exemplo", Support.Provider.GITHUB_SPONSORS,
                "https://github.com/example", null, true, true));
        check(new Support.Method("github", "GitHub", "Exemplo", Support.Provider.GITHUB_SPONSORS,
                "https://github.com/sponsors/example", null, true, true).id().equals("github"));
        for (Support.Provider provider : Arrays.asList(Support.Provider.BUY_ME_A_COFFEE,
                Support.Provider.STRIPE_LINK, Support.Provider.MERCADO_PAGO_LINK)) {
            String host = provider == Support.Provider.BUY_ME_A_COFFEE ? "buymeacoffee.com"
                    : provider == Support.Provider.STRIPE_LINK ? "buy.stripe.com" : "mpago.la";
            check(new Support.Method("support", "Support", "Example", provider,
                    "https://" + host + "/example", null, true, true).destination().getHost().equals(host));
        }
        System.out.println("OK: " + checks + " contract checks");
    }

    private static Support.Method method(String id, String url, boolean enabled, boolean reviewed) {
        return new Support.Method(id, "Apoiar", "Example (test fixture)",
                Support.Provider.KO_FI, url, null, enabled, reviewed);
    }
    private static void check(boolean value) {
        checks++;
        if (!value) throw new AssertionError("Check " + checks);
    }
    private static void fails(Runnable action) {
        checks++;
        try { action.run(); }
        catch (IllegalArgumentException | UnsupportedOperationException expected) { return; }
        throw new AssertionError("Expected rejection: " + checks);
    }
}
