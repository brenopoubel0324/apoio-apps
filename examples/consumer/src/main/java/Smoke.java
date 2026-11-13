import dev.brenopoubel.apoio.Support;
import dev.brenopoubel.apoio.android.SupportLinks;
import java.util.Collections;

public final class Smoke {
    public static void main(String[] args) {
        Support.Catalog catalog = new Support.Catalog("smoke", Collections.emptyList());
        if (!catalog.available(Support.Channel.GOOGLE_PLAY).isEmpty()) throw new AssertionError();
        System.out.println("Maven consumer OK: core transitive; " + SupportLinks.Result.UNAVAILABLE);
    }
}
