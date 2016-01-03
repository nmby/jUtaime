package xyz.hotchpotch.jutaime.serializable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 単一の入力引数を受け取って結果を返さないオペレーションを表します。<br>
 * {@code UnsafeConsumer} は {@link java.util.function.Consumer java.util.function.Consumer} に似ていますが、
 * {@link Consumer#accept(Object) Consumer.accept()} がチェック例外をスローできないのに対して、
 * {@link UnsafeConsumer#accept(Object) UnsafeConsumer.accept()} は {@link Throwable} を含む任意のチェック例外をスローできます。<br>
 *
 * @param <T> オペレーションの入力の型
 * @see Consumer
 * @author nmby
 */
@FunctionalInterface
/*package*/ interface UnsafeConsumer<T> {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * 指定された引数でこのオペレーションを実行します。<br>
     * 
     * @param t 入力引数
     * @throws Throwable オペレーション実行時に例外またはエラーが発生した場合
     */
    void accept(T t) throws Throwable;
    
    /**
     * このオペレーションを実行した後、続けて {@code after} オペレーションを実行する合成 {@code UnsafeConsumer} を返します。<br>
     * いずれかのオペレーションの実行時に例外がスローされた場合、その例外は合成オペレーションの呼出し元に中継されます。
     * このオペレーションの実行時に例外がスローされた場合、{@code after} オペレーションは実行されません。<br>
     * 
     * @param after このオペレーションの後で実行するオペレーション
     * @return このオペレーションを実行した後、続けて {@code after} オペレーションを実行する合成 {@code UnsafeConsumer}
     */
    default UnsafeConsumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
