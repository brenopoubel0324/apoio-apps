package dev.brenopoubel.apoio;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/** Optional support links. Never creates, processes or confirms a payment. */
public final class Support {
    private Support() {}

    public enum Channel { GITHUB_APK, WEB, GOOGLE_PLAY, UNKNOWN }

    public enum Provider {
        GITHUB_SPONSORS("github.com"),
        KO_FI("ko-fi.com"),
        BUY_ME_A_COFFEE("buymeacoffee.com"),
        STRIPE_LINK("buy.stripe.com"),
        MERCADO_PAGO_LINK("mpago.la"),
        CUSTOM(null);

        private final String host;
        Provider(String host) { this.host = host; }
    }

    public static final class Method {
        private final String id;
        private final String label;
        private final String recipient;
        private final URI destination;
        private final boolean enabled;
        private final boolean recipientReviewed;

        /**
         * recipientReviewed is an assertion by the app owner after checking the
         * destination and recipient. It is NOT an automated ownership check.
         * customHost is required only for CUSTOM and must be a literal DNS host.
         */
        public Method(String id, String label, String recipient, Provider provider,
                      String url, String customHost, boolean enabled,
                      boolean recipientReviewed) {
            this.id = requireText(id, "id");
            if (!id.matches("[a-z0-9][a-z0-9_-]{0,63}")) {
                throw new IllegalArgumentException("Invalid method id");
            }
            this.label = requireText(label, "label");
            this.recipient = requireText(recipient, "recipient");
            Objects.requireNonNull(provider, "provider");
            String host = provider.host;
            if (provider == Provider.CUSTOM) {
                host = requireText(customHost, "customHost").toLowerCase(Locale.ROOT);
                if (!host.matches("[a-z0-9](?:[a-z0-9.-]*[a-z0-9])?\\.[a-z]{2,}")) {
                    throw new IllegalArgumentException("Expected an exact DNS host");
                }
            }
            this.destination = validatedDestination(url, host);
            if (provider == Provider.GITHUB_SPONSORS
                    && !destination.getPath().matches("/sponsors/[A-Za-z0-9-]+/?")) {
                throw new IllegalArgumentException("Expected a GitHub Sponsors profile");
            }
            this.enabled = enabled;
            this.recipientReviewed = recipientReviewed;
        }

        public String id() { return id; }
        public String label() { return label; }
        public String recipient() { return recipient; }
        public URI destination() { return destination; }
    }

    public static final class Catalog {
        private final String appId;
        private final List<Method> methods;

        public Catalog(String appId, List<Method> methods) {
            this.appId = requireText(appId, "appId");
            Objects.requireNonNull(methods, "methods");
            List<Method> copy = new ArrayList<>();
            Set<String> ids = new HashSet<>();
            for (Method method : methods) {
                Objects.requireNonNull(method, "method");
                if (!ids.add(method.id)) throw new IllegalArgumentException("Duplicate method id");
                copy.add(method);
            }
            this.methods = Collections.unmodifiableList(copy);
        }

        public String appId() { return appId; }

        /** Play needs its own reviewed implementation; unknown channels stay off. */
        public List<Method> available(Channel channel) {
            if (channel != Channel.GITHUB_APK && channel != Channel.WEB) {
                return Collections.emptyList();
            }
            List<Method> visible = new ArrayList<>();
            for (Method method : methods) {
                if (method.enabled && method.recipientReviewed) visible.add(method);
            }
            return Collections.unmodifiableList(visible);
        }

        /** Resolve again on click, using the same channel configured at build time. */
        public Method resolve(String methodId, Channel channel) {
            for (Method method : available(channel)) {
                if (method.id.equals(methodId)) return method;
            }
            return null;
        }
    }

    private static URI validatedDestination(String url, String expectedHost) {
        final URI uri;
        try { uri = new URI(requireText(url, "url")); }
        catch (URISyntaxException e) { throw new IllegalArgumentException("Invalid URL", e); }
        if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.isOpaque()
                || uri.getHost() == null || !expectedHost.equalsIgnoreCase(uri.getHost())
                || uri.getRawUserInfo() != null || uri.getPort() != -1
                || uri.getRawFragment() != null
                || uri.getRawPath() == null || uri.getRawPath().length() < 2) {
            throw new IllegalArgumentException("Expected an HTTPS destination on the exact provider host");
        }
        // No extra attribution parameters, personal data, or payment amounts added here.
        return uri;
    }

    private static String requireText(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " is required");
        }
        return value;
    }
}
