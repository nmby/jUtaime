package xyz.hotchpotch.jutaime.throwable.matchers;

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
 * @since 1.0.0
 * @author nmby
 */
public class RaiseNothing extends TypeSafeMatcher<Testee> {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * 検査対象のオペレーションが例外やエラーをスローせずに正常終了することを検査する {@code Matcher} オブジェクトを返します。<br>
     * 
     * @return 検査対象のオペレーションが正常終了することを検査する {@code Matcher}
     */
    public static Matcher<Testee> raiseNothing() {
        return new RaiseNothing();
    }
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private RaiseNothing() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean matchesSafely(Testee testee) {
        assert testee != null;
        try {
            testee.call();
            return true;
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
            description.appendText("throw nothing");
        }
    }
}
