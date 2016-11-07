package xyz.hotchpotch.jutaime;

import java.util.concurrent.Callable;

/**
 * 結果を返さず、例外をスローすることがあるオペレーションを表します。<br>
 * {@code UnsafeRunnable} は {@link java.lang.Runnable java.lang.Runnable} に似ていますが、
 * {@link Runnable#run() Runnable.run()} がチェック例外をスローできないのに対して、
 * {@link UnsafeRunnable#run() UnsafeRunnable.run()} は {@link Throwable} を含む任意のチェック例外をスローできます。<br>
 * 
 * @see UnsafeCallable
 * @see Runnable
 * @see Callable
 * @since 1.0.0
 * @author nmby
 */
@FunctionalInterface
public interface UnsafeRunnable {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * オペレーションを実行するか、実行できない場合は例外をスローします。<br>
     * 
     * @throws Throwable オペレーションを実行できなかった場合
     */
    void run() throws Throwable;
}
