package xyz.hotchpotch.jutaime.throwable.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import xyz.hotchpotch.jutaime.throwable.Testee;

/**
 * スローされた例外またはエラーに対して何らかの検査を行う {@code Matcher} の基底クラスです。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @author nmby
 */
abstract class ThrowableBaseMatcher extends TypeSafeMatcher<Testee> {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private final Matcher<Throwable> matcher;
    
    ThrowableBaseMatcher(boolean exactly, Class<? extends Throwable> expectedType, String expectedMessage) {
        assert expectedType != null;
        
        matcher = new TypeSafeMatcher<Throwable>() {
            @Override
            protected boolean matchesSafely(Throwable actual) {
                assert actual != null;
                return (exactly ? expectedType.equals(actual.getClass())
                        : expectedType.isAssignableFrom(actual.getClass()))
                        && Objects.equals(expectedMessage, actual.getMessage());
            }
            
            @Override
            public void describeTo(Description description) {
                assert description != null;
                description.appendText(String.format("%s (%s)", expectedType.getName(), expectedMessage));
            }
        };
    }
    
    ThrowableBaseMatcher(boolean exactly, Class<? extends Throwable> expectedType) {
        assert expectedType != null;
        
        matcher = new TypeSafeMatcher<Throwable>() {
            @Override
            protected boolean matchesSafely(Throwable actual) {
                assert actual != null;
                return (exactly ? expectedType.equals(actual.getClass())
                        : expectedType.isAssignableFrom(actual.getClass()));
            }
            
            @Override
            public void describeTo(Description description) {
                assert description != null;
                description.appendText(expectedType.getName());
            }
        };
    }
    
    ThrowableBaseMatcher(Matcher<Throwable> matcher) {
        assert matcher != null;
        this.matcher = matcher;
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
            return matchesWhole(actual);
        }
    }
    
    abstract boolean matchesWhole(Throwable actual);
    
    boolean matchesEach(Throwable t) {
        assert t != null;
        return matcher.matches(t);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        if (description != null) {
            description.appendText(String.format("%s <%s>", descriptionTag(), matcher.toString()));
        }
    }
    
    abstract String descriptionTag();
}
