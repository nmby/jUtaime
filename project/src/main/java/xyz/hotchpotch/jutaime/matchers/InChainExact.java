package xyz.hotchpotch.jutaime.matchers;

import java.util.Objects;

import org.hamcrest.Matcher;

import xyz.hotchpotch.jutaime.Testee;

/**
 * スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher} です。<br>
 * この {@code Matcher} は、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
 * その中に期待される例外が含まれるかを検査します。<br>
 * 合格と判定されるものが見つかった場合は、その時点で検査を打ち切ります。<br>
 * スローされた例外そのものや根本原因（root cause）も例外チェインの一部とみなされます。<br>
 * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
 * <br>
 * この {@code Matcher} は、例外チェインの中の例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @since 1.0.0
 * @author nmby
 */
public class InChainExact extends InChainBase {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * スローされた例外の例外チェインの中に、期待される型の例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee<?>> inChainExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new InChainExact(expectedType);
    }
    
    /**
     * スローされた例外の例外チェインの中に、期待される型とメッセージの例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee<?>> inChainExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new InChainExact(expectedType, expectedMessage);
    }
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private InChainExact(Class<? extends Throwable> expectedType) {
        super(true, expectedType);
    }
    
    private InChainExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        super(true, expectedType, expectedMessage);
    }
}
