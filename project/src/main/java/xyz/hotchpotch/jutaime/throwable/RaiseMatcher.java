package xyz.hotchpotch.jutaime.throwable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import xyz.hotchpotch.jutaime.throwable.matchers.InChain;
import xyz.hotchpotch.jutaime.throwable.matchers.InChainExact;
import xyz.hotchpotch.jutaime.throwable.matchers.NoCause;
import xyz.hotchpotch.jutaime.throwable.matchers.Raise;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseExact;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCause;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCauseExact;

/**
 * 複数の {@code Matcher} を and 条件で連結する、コンテナ {@code Matcher} です。<br>
 * このクラスのインスタンスは、{@link RaiseMatchers} クラスの static ファクトリメソッドにより提供されます。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @author nmby
 */
public class RaiseMatcher extends TypeSafeMatcher<Testee> {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private final List<Matcher<Testee>> matchers = new ArrayList<>();
    
    RaiseMatcher(Matcher<Testee> matcher) {
        assert matcher != null;
        matchers.add(matcher);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean matchesSafely(Testee testee) {
        assert testee != null;
        return matchers.parallelStream().allMatch(x -> x.matches(testee));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        if (description != null) {
            description.appendText(
                    matchers.stream().map(x -> x.toString()).collect(Collectors.joining(", ")));
        }
    }
    
    /**
     * この {@code Matcher} に、{@link Raise#raise(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see Raise#raise(Class)
     */
    public RaiseMatcher raise(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        matchers.add(Raise.raise(expectedType));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseExact#raiseExact(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseExact#raiseExact(Class)
     */
    public RaiseMatcher raiseExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        matchers.add(RaiseExact.raiseExact(expectedType));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link Raise#raise(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see Raise#raise(Class, String)
     */
    public RaiseMatcher raise(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        matchers.add(Raise.raise(expectedType, expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseExact#raiseExact(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseExact#raiseExact(Class, String)
     */
    public RaiseMatcher raiseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        matchers.add(RaiseExact.raiseExact(expectedType, expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link Raise#raise(String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @see Raise#raise(String)
     */
    public RaiseMatcher raise(String expectedMessage) {
        matchers.add(Raise.raise(expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link Raise#raise(Matcher)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher スローされた例外に対する判定を行うための {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see Raise#raise(Matcher)
     */
    public RaiseMatcher raise(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        matchers.add(Raise.raise(matcher));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link NoCause#noCause()} で返される {@code Matcher} を追加します。<br>
     * 
     * @return この {@code Matcher}
     * @see NoCause#noCause()
     */
    public RaiseMatcher noCause() {
        matchers.add(NoCause.noCause());
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RootCause#rootCause(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCause#rootCause(Class)
     */
    public RaiseMatcher rootCause(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        matchers.add(RootCause.rootCause(expectedType));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RootCauseExact#rootCauseExact(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCauseExact#rootCauseExact(Class)
     */
    public RaiseMatcher rootCauseExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        matchers.add(RootCauseExact.rootCauseExact(expectedType));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RootCause#rootCause(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCause#rootCause(Class, String)
     */
    public RaiseMatcher rootCause(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        matchers.add(RootCause.rootCause(expectedType, expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RootCauseExact#rootCauseExact(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される根本原因（root cause）の型
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RootCauseExact#rootCauseExact(Class, String)
     */
    public RaiseMatcher rootCauseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        matchers.add(RootCauseExact.rootCauseExact(expectedType, expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RootCause#rootCause(String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedMessage 期待される根本原因（root cause）のメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @see RootCause#rootCause(String)
     */
    public RaiseMatcher rootCause(String expectedMessage) {
        matchers.add(RootCause.rootCause(expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RootCause#rootCause(Matcher)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher 根本原因（root cause）に対する判定を行うための {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see RootCause#rootCause(Matcher)
     */
    public RaiseMatcher rootCause(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        matchers.add(RootCause.rootCause(matcher));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link InChain#inChain(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChain#inChain(Class)
     */
    public RaiseMatcher inChain(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        matchers.add(InChain.inChain(expectedType));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link InChainExact#inChainExact(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChainExact#inChainExact(Class)
     */
    public RaiseMatcher inChainExact(Class<? extends Throwable> expectedType) {
        Objects.requireNonNull(expectedType);
        matchers.add(InChainExact.inChainExact(expectedType));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link InChain#inChain(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChain#inChain(Class, String)
     */
    public RaiseMatcher inChain(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        matchers.add(InChain.inChain(expectedType, expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link InChainExact#inChainExact(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see InChainExact#inChainExact(Class, String)
     */
    public RaiseMatcher inChainExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        Objects.requireNonNull(expectedType);
        matchers.add(InChainExact.inChainExact(expectedType, expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link InChain#inChain(String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @see InChain#inChain(String)
     */
    public RaiseMatcher inChain(String expectedMessage) {
        matchers.add(InChain.inChain(expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link InChain#inChain(Matcher)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher 例外チェインの中の各例外に対する判定を行うための {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see InChain#inChain(Matcher)
     */
    public RaiseMatcher inChain(Matcher<Throwable> matcher) {
        Objects.requireNonNull(matcher);
        matchers.add(InChain.inChain(matcher));
        return this;
    }
    
    /**
     * この {@code Matcher} に任意の {@link Matcher} を追加します。<br>
     * 
     * @param matcher {@code Testee} に対する任意の検査を行う {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     */
    public RaiseMatcher and(Matcher<Testee> matcher) {
        // 自分自身を渡されたりそれに類したことをされるとループになってしまうが、
        // そこまで面倒見てられないので、ケアしないことにする。
        Objects.requireNonNull(matcher);
        matchers.add(matcher);
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link org.hamcrest.CoreMatchers#not(Matcher) org.hamcrest.CoreMatchers.not(Matcher)}
     * で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher {@code Testee} に対する任意の {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see org.hamcrest.CoreMatchers#not(Matcher) org.hamcrest.CoreMatchers.not(Matcher)
     */
    public RaiseMatcher not(Matcher<Testee> matcher) {
        // 自分自身を渡されたりそれに類したことをされるとループになってしまうが、
        // そこまで面倒見てられないので、ケアしないことにする。
        Objects.requireNonNull(matcher);
        matchers.add(org.hamcrest.CoreMatchers.not(matcher));
        return this;
    }
}
