package xyz.hotchpotch.jutaime.throwable.matchers;

import java.util.Objects;

import org.hamcrest.Matcher;

import xyz.hotchpotch.jutaime.throwable.Testee;

/**
 * スローされた例外を検査する {@code Matcher} です。<br>
 * この {@code Matcher} は、スローされた例外そのものの型やメッセージが期待されたものかを検査します。<br>
 * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
 * <br>
 * この {@code Matcher} は、スローされた例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @since 1.0.0
 * @author nmby
 */
public class Raise extends RaiseBase {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * スローされた例外の型を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee<?>> raise(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new Raise(expectedType);
    }
    
    /**
     * スローされた例外の型とメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee<?>> raise(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new Raise(expectedType, expectedMessage);
    }
    
    /**
     * スローされた例外のメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型は考慮しません。<br>
     * 
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外を検査する {@code Matcher}
     * @since 1.1.0
     */
    public static Matcher<Testee<?>> raise(String expectedMessage) {
        return new Raise(Throwable.class, expectedMessage);
    }
    
    /**
     * スローされた例外を、パラメータとして受け取った {@code matcher} で検査する {@code Matcher} オブジェクトを返します。<br>
     * 
     * @param matcher スローされた例外に対する検査を行うための {@code Matcher}
     * @return スローされた例外に {@code matcher} を適用する {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     */
    public static Matcher<Testee<?>> raise(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        return new Raise(matcher);
    }
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private Raise(Class<? extends Throwable> expectedType) {
        super(false, expectedType);
    }
    
    private Raise(Class<? extends Throwable> expectedType, String expectedMessage) {
        super(false, expectedType, expectedMessage);
    }
    
    private Raise(Matcher<Throwable> matcher) {
        super(matcher);
    }
}
