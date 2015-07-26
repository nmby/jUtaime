package xyz.hotchpotch.jutaime.throwable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから実行されることは想定されていません。<br>
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
        return matchers.stream().allMatch(x -> x.matches(testee));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        if (description != null) {
            description.appendText(
                    String.join(", ", matchers.stream().map(x -> x.toString()).toArray(String[]::new)));
        }
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#raise(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#raise(Class)
     */
    public RaiseMatcher raise(Class<? extends Throwable> expectedType) {
        matchers.add(Raise.raise(Objects.requireNonNull(expectedType)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#raiseExact(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#raiseExact(Class)
     */
    public RaiseMatcher raiseExact(Class<? extends Throwable> expectedType) {
        matchers.add(RaiseExact.raiseExact(Objects.requireNonNull(expectedType)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#raise(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#raise(Class, String)
     */
    public RaiseMatcher raise(Class<? extends Throwable> expectedType, String expectedMessage) {
        matchers.add(Raise.raise(Objects.requireNonNull(expectedType), expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#raiseExact(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#raiseExact(Class, String)
     */
    public RaiseMatcher raiseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        matchers.add(RaiseExact.raiseExact(Objects.requireNonNull(expectedType), expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#raise(Matcher)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher スローされた例外に対する判定を行うための {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see RaiseMatchers#raise(Matcher)
     */
    public RaiseMatcher raise(Matcher<Throwable> matcher) {
        matchers.add(Raise.raise(Objects.requireNonNull(matcher)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#noCause()} で返される {@code Matcher} を追加します。<br>
     * 
     * @return この {@code Matcher}
     * @see RaiseMatchers#noCause()
     */
    public RaiseMatcher noCause() {
        matchers.add(NoCause.noCause());
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#rootCause(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される root cause の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#rootCause(Class)
     */
    public RaiseMatcher rootCause(Class<? extends Throwable> expectedType) {
        matchers.add(RootCause.rootCause(Objects.requireNonNull(expectedType)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#rootCauseExact(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される root cause の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#rootCauseExact(Class)
     */
    public RaiseMatcher rootCauseExact(Class<? extends Throwable> expectedType) {
        matchers.add(RootCauseExact.rootCauseExact(Objects.requireNonNull(expectedType)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#rootCause(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される root cause の型
     * @param expectedMessage 期待される root cause のメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#rootCause(Class, String)
     */
    public RaiseMatcher rootCause(Class<? extends Throwable> expectedType, String expectedMessage) {
        matchers.add(RootCause.rootCause(Objects.requireNonNull(expectedType), expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#rootCauseExact(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される root cause の型
     * @param expectedMessage 期待される root cause のメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#rootCauseExact(Class, String)
     */
    public RaiseMatcher rootCauseExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        matchers.add(RootCauseExact.rootCauseExact(Objects.requireNonNull(expectedType), expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#rootCause(Matcher)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher 根本原因（root cause）に対する判定を行うための {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see RaiseMatchers#rootCause(Matcher)
     */
    public RaiseMatcher rootCause(Matcher<Throwable> matcher) {
        matchers.add(RootCause.rootCause(Objects.requireNonNull(matcher)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#inChain(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#inChain(Class)
     */
    public RaiseMatcher inChain(Class<? extends Throwable> expectedType) {
        matchers.add(InChain.inChain(Objects.requireNonNull(expectedType)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#inChainExact(Class)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#inChainExact(Class)
     */
    public RaiseMatcher inChainExact(Class<? extends Throwable> expectedType) {
        matchers.add(InChainExact.inChainExact(Objects.requireNonNull(expectedType)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#inChain(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#inChain(Class, String)
     */
    public RaiseMatcher inChain(Class<? extends Throwable> expectedType, String expectedMessage) {
        matchers.add(InChain.inChain(Objects.requireNonNull(expectedType), expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#inChainExact(Class, String)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param expectedType 期待される例外の型
     * @param expectedMessage 期待されるメッセージ（{@code null} が許容されます）
     * @return この {@code Matcher}
     * @throws NullPointerException {@code expectedType} が {@code null} の場合
     * @see RaiseMatchers#inChainExact(Class, String)
     */
    public RaiseMatcher inChainExact(Class<? extends Throwable> expectedType, String expectedMessage) {
        matchers.add(InChainExact.inChainExact(Objects.requireNonNull(expectedType), expectedMessage));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link RaiseMatchers#inChain(Matcher)} で返される {@code Matcher} を追加します。<br>
     * 
     * @param matcher 例外チェインの中の各例外に対する判定を行うための {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @see RaiseMatchers#inChain(Matcher)
     */
    public RaiseMatcher inChain(Matcher<Throwable> matcher) {
        matchers.add(InChain.inChain(Objects.requireNonNull(matcher)));
        return this;
    }
    
    /**
     * この {@code Matcher} に、{@link Testee} に対する任意の {@link Matcher} を追加します。<br>
     * 
     * @param matcher {@code Testee} に対する任意の検査を行う {@code Matcher}
     * @return この {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     */
    public RaiseMatcher and(Matcher<Testee> matcher) {
        // 自分自身を渡されたりそれに類したことをされるとループになってしまうが、
        // そこまで面倒見てられないので、ケアしないことにする。
        matchers.add(Objects.requireNonNull(matcher));
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
        matchers.add(org.hamcrest.CoreMatchers.not(Objects.requireNonNull(matcher)));
        return this;
    }
}
