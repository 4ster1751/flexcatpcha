package io.github.yaforster.flexcaptcha.impl.token;

import java.util.function.Supplier;

public record ExpirationTimeSettings(Long expirationTimeMillisOffset, Supplier<Long> currentTimeProvider) {
    public long getTime() {
        return currentTimeProvider.get();
    }
}
