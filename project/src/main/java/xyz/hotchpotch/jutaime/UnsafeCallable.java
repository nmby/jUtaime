package xyz.hotchpotch.jutaime;

import java.util.concurrent.Callable;

/**
 * 結果を返し、例外をスローすることがあるオペレーションを表します。<br>
 * {@code UnsafeCallable} は {@link java.util.concurrent.Callable java.util.concurrent.Callable} に似ていますが、
 * {@link Callable#call() Callable.call()} が {@link Exception} とそのサブクラスのチェック例外をスローできるのに対して、
 * {@link UnsafeCallable#call() UnsafeCallable.call()} は {@link Throwable} を含む任意のチェック例外をスローできます。<br>
 *
 * @param <V> 戻り値の型
 * @see UnsafeRunnable
 * @see Callable
 * @see Runnable
 * @since 1.0.0
 * @author nmby
 */
@FunctionalInterface
public interface UnsafeCallable<V> {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * オペレーションを実行して計算結果を返すか、実行できない場合は例外をスローします。<br>
     * 
     * @return 計算結果
     * @throws Throwable オペレーションを実行できなかった場合
     */
    V call() throws Throwable;
}
