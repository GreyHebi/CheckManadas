package ru.hebi.check_manadas.java_custom.domain;

import org.jspecify.annotations.NonNull;
import ru.hebi.check_manadas.java_custom.domain.error.Error;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Значение функции
 *
 * @param <E> тип ошибки
 * @param <R> тип значения
 */
public final class Result<E extends Error, R> {

    /** Ошибка */
    private final E error;
    /** Значение */
    private final R value;

    private Result(final E error, final R value) {
        this.error = error;
        this.value = value;
    }

    /**
     * Положительный ответ исполнения функции
     *
     * @param result значение ответа
     * @param <E>    тип ошибки
     * @param <R>    тип значения
     * @return ответ функции
     */
    public static <E extends Error, R> Result<E, R> ok(@NonNull R result) {
        return new Result<>(
                null, result
        );
    }

    /**
     * Отрицательный ответ исполнения функции
     *
     * @param error значение ответа-ошибки
     * @return ответ функции
     * @param <E> тип ошибки
     * @param <R> тип значения
     */
    public static <E extends Error, R> Result<E, R> error(@NonNull E error) {
        return new Result<>(
                error, null
        );
    }


    /**
     * Получить результат выполнения функции
     *
     * @param catchFunction функция обработки исключения при выполнении {@code trySupplier}
     * @param trySupplier функция
     * @return значение функции {@code trySupplier}
     * @param <E> тип ошибки
     * @param <R> тип значения
     */
    public static <E extends Error, R> Result<E, R> attempt(
            @NonNull Function<Exception, Result<E, R>> catchFunction,
            @NonNull Supplier<Result<E, R>> trySupplier
    ) {
        try {
            return trySupplier.get();
        } catch (Exception e) {
            return catchFunction.apply(e);
        }
    }

    /**
     * Преобразовать значение, учитывая что функция преобразования может бросить ошибку
     *
     * @param catchFunction функция обработки исключения при выполнении {@code function}
     * @param function      функция преобразования, которая может бросить исключение
     * @param <E>           тип ошибки
     * @param <N>           тип значения
     * @return рассчитанное значение функции {@code function}
     */
    public <E extends Error, N> Result<E, N> map(
            @NonNull Function<Exception, Result<E, N>> catchFunction,
            @NonNull Function<R, Result<E, N>> function
    ) {
        if (this.error != null)
            return (Result<E, N>) this;

        try {
            return function.apply(this.value);
        } catch (Exception e) {
            return catchFunction.apply(e);
        }
    }

    /**
     * Преобразовать значение
     *
     * @param function функция преобразования
     * @param <E>      тип ошибки
     * @param <N>      тип значения
     * @return рассчитанное значение функции {@code function}
     */
    public <E extends Error, N> Result<E, N> map(
            @NonNull Function<R, Result<E, N>> function
    ) {
        return this.error != null
                ? (Result<E, N>) this
                : function.apply(this.value);
    }

    /**
     * Извлечение значения
     *
     * @param errorHandle  обработка ошибки
     * @param resultHandle обработка значение
     * @param <N>          тип преобразованного значения
     * @return извлеченное значение
     */
    @NonNull
    public <N> N apply(
            @NonNull Function<E, N> errorHandle,
            @NonNull Function<R, N> resultHandle
    ) {
        return this.error != null
                ? errorHandle.apply(this.error)
                : resultHandle.apply(this.value);
    }
}
