package xyz.hotchpotch.jutaime.throwable;

import java.util.Objects;

import org.hamcrest.Matcher;

import xyz.hotchpotch.jutaime.throwable.matchers.InChain;
import xyz.hotchpotch.jutaime.throwable.matchers.InChainExact;
import xyz.hotchpotch.jutaime.throwable.matchers.NoCause;
import xyz.hotchpotch.jutaime.throwable.matchers.Raise;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseExact;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseNothing;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCause;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCauseExact;

/**
 * オペレーションによりスローされる例外およびエラーを検査するための各種 {@code Matcher} を提供するユーティリティクラスです。<br>
 * {@link Testee} と組み合わせた利用方法については、{@link xyz.hotchpotch.jutaime.throwable パッケージの説明}を参照してください。<br>
 * <br>
 * 一般に、このクラスの static ファクトリメソッドにより提供される {@code Matcher} オブジェクトはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @see xyz.hotchpotch.jutaime.throwable
 * @see Testee
 * @see org.junit.Assert#assertThat(Object, org.hamcrest.Matcher)
 * @author nmby
 */
public class RaiseMatchers {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    /**
     * スローされた例外の型を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link Raise#raise(Class)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外の型を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see Raise#raise(Class)
     */
    public static RaiseMatcher raise(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(Raise.raise(expectedType));
    }
    
    /**
     * スローされた例外の型を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RaiseExact#raiseExact(Class)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外の型を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseExact#raiseExact(Class)
     */
    public static RaiseMatcher raiseExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(RaiseExact.raiseExact(expectedType));
    }
    
    /**
     * スローされた例外の型とメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link Raise#raise(Class, String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の型とメッセージを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see Raise#raise(Class, String)
     */
    public static RaiseMatcher raise(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(Raise.raise(expectedType, expectedMessage));
    }
    
    /**
     * スローされた例外の型とメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RaiseExact#raiseExact(Class, String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の型とメッセージを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseExact#raiseExact(Class, String)
     */
    public static RaiseMatcher raiseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(RaiseExact.raiseExact(expectedType, expectedMessage));
    }
    
    /**
     * スローされた例外のメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の型は考慮しません。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link Raise#raise(String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外のメッセージを検査する {@code Matcher}
     * @see Raise#raise(String)
     * @since 1.1.0
     */
    public static RaiseMatcher raise(String expectedMessage) {
        return new RaiseMatcher(Raise.raise(expectedMessage));
    }
    
    /**
     * スローされた例外を検査する {@code Matcher} オブジェクトを返します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、パラメータとして受け取った {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param matcher スローされた例外に対する判定を行うための {@code Matcher}
     * @return スローされた例外に {@code matcher} を適用する {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see Raise#raise(Matcher)
     */
    public static RaiseMatcher raise(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        return new RaiseMatcher(Raise.raise(matcher));
    }
    
    /**
     * 検査対象のオペレーションが例外やエラーをスローせずに正常終了することを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、検査対象オペレーションが例外やエラーをスローせずに終了した場合に合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RaiseNothing#raiseNothing()} が返す {@code Matcher} オブジェクトを返します。<br>
     * 
     * @return 検査対象のオペレーションが正常終了することを検査する {@code Matcher}
     * @see RaiseNothing#raiseNothing()
     */
    public static Matcher<Testee> raiseNothing() {
        return RaiseNothing.raiseNothing();
    }
    
    /**
     * 検査対象のオペレーションがスローした例外やエラーが原因（cause）を持たないことを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外 {@code actual} に対して
     * {@code actual.getCause() == null} の場合に合格と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link NoCause#noCause()} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @return 検査対象のオペレーションがスローした例外が原因（cause）を持たないことを検査する {@code Matcher}
     * @see NoCause#noCause()
     */
    public static RaiseMatcher noCause() {
        return new RaiseMatcher(NoCause.noCause());
    }
    
    /**
     * スローされた例外の根本原因（root cause）の型を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * 最後に辿り着いた {@code cause} に対して判定を行います。<br>
     * スローされた例外が原因（cause）を持たない場合は、スローされた例外自体が根本原因（root cause）とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RootCause#rootCause(Class)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @return スローされた例外の根本原因（root cause）の型を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCause#rootCause(Class)
     */
    public static RaiseMatcher rootCause(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(RootCause.rootCause(expectedType));
    }
    
    /**
     * スローされた例外の根本原因（root cause）の型を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * 最後に辿り着いた {@code cause} に対して判定を行います。<br>
     * スローされた例外が原因（cause）を持たない場合は、スローされた例外自体が根本原因（root cause）とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RootCauseExact#rootCauseExact(Class)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @return スローされた例外の根本原因（root cause）の型を検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCauseExact#rootCauseExact(Class)
     */
    public static RaiseMatcher rootCauseExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(RootCauseExact.rootCauseExact(expectedType));
    }
    
    /**
     * スローされた例外の根本原因（root cause）の型とメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * 最後に辿り着いた {@code cause} に対して判定を行います。<br>
     * スローされた例外が原因（cause）を持たない場合は、スローされた例外自体が根本原因（root cause）とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RootCause#rootCause(Class, String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return スローされた例外の根本原因（root cause）の型とメッセージを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCause#rootCause(Class, String)
     */
    public static RaiseMatcher rootCause(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(RootCause.rootCause(expectedType, expectedMessage));
    }
    
    /**
     * スローされた例外の根本原因（root cause）の型とメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * 最後に辿り着いた {@code cause} に対して判定を行います。<br>
     * スローされた例外が原因（cause）を持たない場合は、スローされた例外自体が根本原因（root cause）とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RootCauseExact#rootCauseExact(Class, String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return スローされた例外の根本原因（root cause）の型とメッセージを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCauseExact#rootCauseExact(Class, String)
     */
    public static RaiseMatcher rootCauseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(RootCauseExact.rootCauseExact(expectedType, expectedMessage));
    }
    
    /**
     * スローされた例外の根本原因（root cause）のメッセージを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * 最後に辿り着いた {@code cause} に対して判定を行います。<br>
     * スローされた例外が原因（cause）を持たない場合は、スローされた例外自体が根本原因（root cause）とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、根本原因（root cause）の型は考慮しません。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RootCause#rootCause(String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return スローされた例外の根本原因（root cause）のメッセージを検査する {@code Matcher}
     * @see RootCause#rootCause(String)
     * @since 1.1.0
     */
    public static RaiseMatcher rootCause(String expectedMessage) {
        return new RaiseMatcher(RootCause.rootCause(expectedMessage));
    }
    
    /**
     * スローされた例外の根本原因（root cause）を検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getGause == null} となるまで辿り、
     * 最後に辿り着いた {@code cause} を、パラメータとして受け取った {@code matcher} で検査します。<br>
     * スローされた例外が原因（cause）を持たない場合は、スローされた例外自体が根本原因（root cause）とみなされます。<br>
     * <br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link RootCause#rootCause(Matcher)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param matcher 根本原因（root cause）に対する判定を行うための {@code Matcher}
     * @return 根本原因（root cause）に {@code matcher} を適用する {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see RootCause#rootCause(Matcher)
     */
    public static RaiseMatcher rootCause(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        return new RaiseMatcher(RootCause.rootCause(matcher));
    }
    
    /**
     * スローされた例外の例外チェインの中に期待される型の例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * その中に期待される型の例外が含まれるかを判定します。<br>
     * スローされた例外そのものや根本原因（root cause）も、例外チェインの一部とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link InChain#inChain(Class)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外の例外チェインの中に期待される型の例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChain#inChain(Class)
     */
    public static RaiseMatcher inChain(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(InChain.inChain(expectedType));
    }
    
    /**
     * スローされた例外の例外チェインの中に期待される型の例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * その中に期待される型の例外が含まれるかを判定します。<br>
     * スローされた例外そのものや根本原因（root cause）も、例外チェインの一部とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link InChainExact#inChainExact(Class)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return スローされた例外の例外チェインの中に期待される型の例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChainExact#inChainExact(Class)
     */
    public static RaiseMatcher inChainExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(InChainExact.inChainExact(expectedType));
    }
    
    /**
     * スローされた例外の例外チェインの中に期待される型とメッセージの例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * その中に期待される型とメッセージの例外が含まれるかを判定します。<br>
     * スローされた例外そのものや根本原因（root cause）も、例外チェインの一部とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型のサブクラスの場合も一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link InChain#inChain(Class, String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の例外チェインの中に期待される型とメッセージの例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChain#inChain(Class, String)
     */
    public static RaiseMatcher inChain(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(InChain.inChain(expectedType, expectedMessage));
    }
    
    /**
     * スローされた例外の例外チェインの中に期待される型とメッセージの例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * その中に期待される型とメッセージの例外が含まれるかを判定します。<br>
     * スローされた例外そのものや根本原因（root cause）も、例外チェインの一部とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型が期待された型と完全に同一の場合に一致と判定します。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link InChainExact#inChainExact(Class, String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の例外チェインの中に期待される型とメッセージの例外が含まれるかを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChainExact#inChainExact(Class, String)
     */
    public static RaiseMatcher inChainExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        return new RaiseMatcher(InChainExact.inChainExact(expectedType, expectedMessage));
    }
    
    /**
     * スローされた例外の例外チェインの中に期待されるメッセージの例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getCause() == null} となるまで辿り、
     * その中に期待される型とメッセージの例外が含まれるかを判定します。<br>
     * スローされた例外そのものや根本原因（root cause）も、例外チェインの一部とみなされます。<br>
     * <br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、例外チェインの中の例外の型は考慮しません。<br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link InChain#inChain(String)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return スローされた例外の例外チェインの中に期待されるメッセージの例外が含まれるかを検査する {@code Matcher}
     * @see InChain#inChain(String)
     * @since 1.1.0
     */
    public static RaiseMatcher inChain(String expectedMessage) {
        return new RaiseMatcher(InChain.inChain(expectedMessage));
    }
    
    /**
     * スローされた例外の例外チェインの中に期待される例外が含まれるかを検査する {@code Matcher} オブジェクトを返します。<br>
     * このメソッドにより返される {@code Matcher} オブジェクトは、スローされた例外の例外チェインを {@code cause.getGause == null} となるまで辿り、
     * その中に、パラメータとして受け取った {@code matcher} により合格と判定されるものがあるかを検査します。<br>
     * 合格と判定されるものが見つかった場合は、その時点で検査を打ち切ります。<br>
     * スローされた例外そのものや根本原因（root cause）も、例外チェインの一部とみなされます。<br>
     * <br>
     * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
     * <br>
     * より具体的に説明すると、このメソッドは、{@link InChain#inChain(Matcher)} が返す {@code Matcher} を格納した
     * {@link RaiseMatcher} オブジェクトを返します。<br>
     * 
     * @param matcher 例外チェインの中の各例外に対する判定を行うための {@code Matcher}
     * @return 例外チェインに含まれる各例外に {@code matcher} を適用する {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see InChain#inChain(Matcher)
     */
    public static RaiseMatcher inChain(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        return new RaiseMatcher(InChain.inChain(matcher));
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private RaiseMatchers() {
    }
}
