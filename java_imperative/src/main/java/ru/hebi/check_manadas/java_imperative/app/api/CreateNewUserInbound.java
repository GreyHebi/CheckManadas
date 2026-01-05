package ru.hebi.check_manadas.java_imperative.app.api;

import org.jspecify.annotations.NonNull;
import ru.hebi.check_manadas.java_imperative.domain.User;

public interface CreateNewUserInbound {

    @NonNull
    Long execute(
            @NonNull User user
    );
}
