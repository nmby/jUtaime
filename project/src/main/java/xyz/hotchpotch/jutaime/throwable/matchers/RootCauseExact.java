package xyz.hotchpotch.jutaime.throwable.matchers;

import java.util.Objects;

import org.hamcrest.Matcher;

import xyz.hotchpotch.jutaime.throwable.Testee;

/**
 * スローされた例外の根本原因（root cause）を検査する {@code Matcher} です。<br>
 * この {@code Matcher} は、スローされた例外の例外チェーンを {@code cause.getCause() == null} となるまで辿り、
 * 最後に辿り着いた {@code cause} に対して判定を行います。<br>
 * スローされた例外が原因（cause）を持たない場合は、スローされた例外自身が根本原因（root cause）とみなされます。<br>
 * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
 * <br>
 * この {@code Matcher} は、根本原因（root cause）の型が期待された型と完全に同一の場合に一致と判定します。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @author nmby
 */
public class RootCauseExact extends RootCauseBase {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    /**
     * スローされた例外の根本原因（root cause）の型を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @return スローされた例外の根本原因（root cause）を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee> rootCauseExact(Class<? extends Throwable> expectedType) {
        return new RootCauseExact(Objects.requireNonNull(expectedType));
    }
    
    /**
     * スローされた例外の根本原因（root cause）の型とメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return スローされた例外の根本原因（root cause）を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee> rootCauseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        return new RootCauseExact(Objects.requireNonNull(expectedType), expectedMessage);
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private RootCauseExact(Class<? extends Throwable> expectedType) {
        super(true, expectedType);
    }
    
    private RootCauseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        super(true, expectedType, expectedMessage);
    }
}
