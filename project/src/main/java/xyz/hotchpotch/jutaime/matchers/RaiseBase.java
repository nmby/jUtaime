package xyz.hotchpotch.jutaime.matchers;

import org.hamcrest.Matcher;

/**
 * スローされた例外が期待通りのものかを検査する {@code Matcher} の基底クラスです。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @author nmby
 */
/*package*/ class RaiseBase extends ThrowableBaseMatcher {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /*package*/ RaiseBase(boolean exactly, Class<? extends Throwable> expectedType, String expectedMessage) {
        super(exactly, expectedType, expectedMessage);
    }
    
    /*package*/ RaiseBase(boolean exactly, Class<? extends Throwable> expectedType) {
        super(exactly, expectedType);
    }
    
    /*package*/ RaiseBase(Matcher<Throwable> matcher) {
        super(matcher);
    }
    
    @Override
    /*package*/ boolean matchesWhole(Throwable actual) {
        assert actual != null;
        return matchesEach(actual);
    }
    
    @Override
    /*package*/ String descriptionTag() {
        return "throw";
    }
}
