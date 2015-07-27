package xyz.hotchpotch.jutaime.throwable.matchers;

import java.util.Objects;

import org.hamcrest.Matcher;

import xyz.hotchpotch.jutaime.throwable.Testee;

/**
 * スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher} です。<br>
 * この {@code Matcher} は、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
 * その中に期待される例外が含まれるかを検査します。<br>
 * 合格と判定されるものが見つかった場合は、その時点で検査を打ち切ります。<br>
 * スローされた例外そのものや根本原因（root cause）も例外チェインの一部とみなされます。<br>
 * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
 * <br>
 * この {@code Matcher} は、例外チェインの中の例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @author nmby
 */
public class InChain extends InChainBase {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    /**
     * スローされた例外の例外チェインの中に、期待される型の例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee> inChain(Class<? extends Throwable> expectedType) {
        return new InChain(Objects.requireNonNull(expectedType));
    }
    
    /**
     * スローされた例外の例外チェインの中に、期待される型とメッセージの例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     */
    public static Matcher<Testee> inChain(Class<? extends Throwable> expectedType, String expectedMessage) {
        return new InChain(Objects.requireNonNull(expectedType), expectedMessage);
    }
    
    /**
     * スローされた例外の例外チェインの中に、パラメータとして受け取った {@code matcher} で合格と判定されるものがあるかを検査する
     * {@code Matcher} オブジェクトを返します。<br>
     * 
     * @param matcher 例外チェインの中の各例外に対する検査を行うための {@code Matcher}
     * @return スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     */
    public static Matcher<Testee> inChain(Matcher<Throwable> matcher) {
        return new InChain(Objects.requireNonNull(matcher));
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private InChain(Class<? extends Throwable> expectedType) {
        super(false, expectedType);
    }
    
    private InChain(Class<? extends Throwable> expectedType, String expectedMessage) {
        super(false, expectedType, expectedMessage);
    }
    
    private InChain(Matcher<Throwable> matcher) {
        super(matcher);
    }
}
