package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import xyz.hotchpotch.jutaime.throwable.Testee;

/**
 * 検査対象のオペレーションが例外やエラーをスローせずに正常終了することを検査する {@code Matcher} です。<br>
 * この {@code Matcher} は、検査対象オペレーションが例外やエラーをスローせずに終了した場合に合格と判定します。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @param <T> 検査対象のオペレーションの戻り値の型
 * @since 1.0.0
 * @author nmby
 */
public class RaiseNothing<T> extends TypeSafeMatcher<Testee<T>> {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * 検査対象のオペレーションが例外やエラーをスローせずに正常終了することを検査する {@code Matcher} オブジェクトを返します。<br>
     * 
     * @return 検査対象のオペレーションが正常終了することを検査する {@code Matcher}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Matcher<Testee<?>> raiseNothing() {
        return new RaiseNothing();
    }
    
    /**
     * 検査対象のオペレーションが例外やエラーをスローせずに正常終了し、戻り値が期待通りであることを検査する {@code Matcher} オブジェクトを返します。<br>
     * 
     * @param <T> 検査対象のオペレーションの戻り値の型
     * @param matcher オペレーションの戻り値を検査する {@code Matcher}
     * @return 検査対象のオペレーションが正常終了し、戻り値が期待通りであることを検査する {@code Matcher}
     * @throws NullPointerException {@code matcher} が {@code null} の場合
     * @since 1.4.0
     */
    public static <T> Matcher<Testee<T>> raiseNothing(Matcher<? super T> matcher) {
        Objects.requireNonNull(matcher);
        return new RaiseNothing<>(matcher);
    }
    
    /**
     * 検査対象のオペレーションが例外やエラーをスローせずに正常終了し、戻り値が期待通りであることを検査する {@code Matcher} オブジェクトを返します。<br>
     * 次の2つの呼び出しは同値です。
     * <pre>
     *     raiseNothing(expectedValue);
     *     raiseNothing(org.hamcrest.CoreMatchers.is(expectedValue));
     * </pre>
     * なお、{@code expectedValue} に {@code null} を指定することはできません。<br>
     * 検査対象のオペレーションの戻り値として {@code null} が期待される場合は、次のコードで検証してください。<br>
     * <pre>
     *     raiseNothing(org.hamcrest.CoreMatchers.nullValue());
     * </pre>
     * 
     * @param <T> 検査対象のオペレーションの戻り値の型
     * @param expectedValue 期待される戻り値
     * @return 検査対象のオペレーションが正常終了し、戻り値が期待通りであることを検査する {@code Matcher}
     * @throws NullPointerException {@code expectedValue} が {@code null} の場合
     * @see org.hamcrest.CoreMatchers#is(Object)
     * @see org.hamcrest.CoreMatchers#nullValue()
     * @since 1.4.0
     */
    public static <T> Matcher<Testee<T>> raiseNothing(T expectedValue) {
        Objects.requireNonNull(expectedValue);
        return new RaiseNothing<>(is(expectedValue));
    }
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private final Matcher<? super T> matcher;
    
    private RaiseNothing() {
        matcher = null;
    }
    
    private RaiseNothing(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean matchesSafely(Testee<T> testee) {
        assert testee != null;
        try {
            T result = testee.call();
            return matcher == null || matcher.matches(result);
        } catch (Throwable t) {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        if (description != null) {
            description.appendText("throws nothing");
            if (matcher != null) {
                description.appendText(String.format(" <%s>", matcher.toString()));
            }
        }
    }
}
