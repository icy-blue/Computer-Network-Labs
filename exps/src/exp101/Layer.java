package exp101;

import java.util.Objects;

public abstract class Layer {
    String addHeader(String str) {
        return getClass().getSimpleName() + ": " + Objects.requireNonNull(str).length() + " " + str;
    }
}
