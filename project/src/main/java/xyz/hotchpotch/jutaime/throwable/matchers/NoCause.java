package xyz.hotchpotch.jutaime.throwable.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import xyz.hotchpotch.jutaime.throwable.Testee;

/**
 * 検査対象のオペレーションがスローした例外やエラーが原因（cause）を持たないことを検査する {@code Matcher} です。<br>
 * この {@code Matcher} は、スローされた例外 {@code actual} に対して、
 * {@code actual.getCause() == null} の場合に合格と判定します。<br>
 * 検査対象のオペレーションが正常終了した場合は、不合格と判定します。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから実行されることは想定されていません。<br>
 * 
 * @author nmby
 */
public class NoCause extends TypeSafeMatcher<Testee> {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    /**
     * 検査対象のオペレーションがスローした例外やエラーが原因（cause）を持たないことを検査する {@code Matcher} オブジェクトを返します。<br>
     * 
     * @return 検査対象のオペレーションがスローした例外やエラーが原因（cause）を持たないことを検査する {@code Matcher}
     */
    public static Matcher<Testee> noCause() {
        return new NoCause();
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private NoCause() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean matchesSafely(Testee testee) {
        assert testee != null;
        try {
            testee.call();
            return false;
        } catch (Throwable actual) {
            return actual.getCause() == null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        if (description != null) {
            description.appendText("has no cause");
        }
    }
}
