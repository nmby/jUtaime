package xyz.hotchpotch.jutaime.throwable.matchers;

import org.hamcrest.Matcher;

/**
 * スローされた例外が期待通りのものかを検査する {@code Matcher} の基底クラスです。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから実行されることは想定されていません。<br>
 * 
 * @author nmby
 */
class RaiseBase extends ThrowableBaseMatcher {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    RaiseBase(boolean exactly, Class<? extends Throwable> expectedType, String expectedMessage) {
        super(exactly, expectedType, expectedMessage);
    }
    
    RaiseBase(boolean exactly, Class<? extends Throwable> expectedType) {
        super(exactly, expectedType);
    }
    
    RaiseBase(Matcher<Throwable> matcher) {
        super(matcher);
    }
    
    @Override
    boolean matchesWhole(Throwable actual) {
        assert actual != null;
        return matchesEach(actual);
    }
    
    @Override
    String descriptionTag() {
        return "throw";
    }
}
