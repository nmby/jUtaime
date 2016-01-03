package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class ThrowableBaseMatcherTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private static class TestOuterMatcher extends ThrowableBaseMatcher {
        private Throwable givenViaMatchesWhole;
        
        private TestOuterMatcher(boolean exactly, Class<? extends Throwable> expectedType, String expectedMessage) {
            super(exactly, expectedType, expectedMessage);
        }
        
        private TestOuterMatcher(boolean exactly, Class<? extends Throwable> expectedType) {
            super(exactly, expectedType);
        }
        
        private TestOuterMatcher(Matcher<Throwable> matcher) {
            super(matcher);
        }
        
        @Override
        boolean matchesWhole(Throwable actual) {
            givenViaMatchesWhole = actual;
            return false;
        }
        
        @Override
        String descriptionTag() {
            return "testTag";
        }
    }
    
    private static class TestInnerMatcher extends TypeSafeMatcher<Throwable> {
        private Throwable givenViaMatchesSafely;
        
        @Override
        protected boolean matchesSafely(Throwable t) {
            givenViaMatchesSafely = t;
            return false;
        }
        
        @Override
        public void describeTo(Description description) {
            description.appendText("I'm TestInnerMatcher.");
        }
    }
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testThrowableBaseMatcher() {
        assertThat(new TestOuterMatcher(true, Throwable.class, "msg"), instanceOf(ThrowableBaseMatcher.class));
        assertThat(new TestOuterMatcher(false, Error.class, null), instanceOf(ThrowableBaseMatcher.class));
        assertThat(new TestOuterMatcher(true, Exception.class), instanceOf(ThrowableBaseMatcher.class));
    }
    
    @Test
    public void testMatchesSafely1() {
        // 正常終了時
        ThrowableBaseMatcher matcher = new TestOuterMatcher(true, Exception.class, "test");
        
        assertThat(matcher.matchesSafely(Testee.of(() -> {})), is(false));
        assertThat(matcher.matchesSafely(Testee.of(() -> true)), is(false));
    }
    
    @Test
    public void testMatchesSafely2() {
        // 例外スロー時 ： スローされた例外がサブクラスに渡される。
        TestOuterMatcher matcher = new TestOuterMatcher(true, Exception.class, "test");
        Throwable t = new IllegalArgumentException("test msg");
        matcher.matchesSafely(Testee.of(() -> { throw t; }));
        
        assertThat(matcher.givenViaMatchesWhole, sameInstance(t));
    }
    
    @Test
    public void testMatchesEach1() {
        ThrowableBaseMatcher matcher = new TestOuterMatcher(true, IllegalArgumentException.class, "test");
        
        // メッセージが適切に評価される。
        assertThat(matcher.matchesEach(new IllegalArgumentException("test")), is(true));
        assertThat(matcher.matchesEach(new IllegalArgumentException("diff")), is(false));
        assertThat(matcher.matchesEach(new IllegalArgumentException((String) null)), is(false));
        assertThat(matcher.matchesEach(new IllegalArgumentException()), is(false));
        
        // 例外の型が適切に評価される。
        assertThat(matcher.matchesEach(new RuntimeException("test")), is(false));
        assertThat(matcher.matchesEach(new NumberFormatException("test")), is(false));
        
        // cause ではなく最表層の例外が評価される。
        assertThat(matcher.matchesEach(new IllegalArgumentException("test", new Error("diff"))), is(true));
        assertThat(matcher.matchesEach(new Error("diff", new IllegalArgumentException("test"))), is(false));
    }
    
    @Test
    public void testMatchesEach2() {
        ThrowableBaseMatcher matcher = new TestOuterMatcher(false, IllegalArgumentException.class, "test");
        
        // 例外の型が適切に評価される - 第一引数 false の場合はサブクラス例外も一致と評価される。
        assertThat(matcher.matchesEach(new RuntimeException("test")), is(false));
        assertThat(matcher.matchesEach(new NumberFormatException("test")), is(true));
    }
    
    @Test
    public void testMatchesEach3() {
        ThrowableBaseMatcher matcher = new TestOuterMatcher(true, IllegalArgumentException.class, null);
        
        // メッセージに null を指定した場合も適切に評価される。
        assertThat(matcher.matchesEach(new IllegalArgumentException("test")), is(false));
        assertThat(matcher.matchesEach(new IllegalArgumentException((String) null)), is(true));
        assertThat(matcher.matchesEach(new IllegalArgumentException()), is(new IllegalArgumentException().getMessage() == null));
    }
    
    @Test
    public void testMatchesEach4() {
        ThrowableBaseMatcher matcher = new TestOuterMatcher(true, IllegalArgumentException.class);
        
        // メッセージを指定しない場合は無視される。
        assertThat(matcher.matchesEach(new IllegalArgumentException("test")), is(true));
        assertThat(matcher.matchesEach(new IllegalArgumentException((String) null)), is(true));
        assertThat(matcher.matchesEach(new IllegalArgumentException()), is(true));
        
        // 例外の型は適切に評価される。
        assertThat(matcher.matchesEach(new RuntimeException("test")), is(false));
        assertThat(matcher.matchesEach(new NumberFormatException("test")), is(false));
        
        // cause ではなく最表層の例外が評価される。
        assertThat(matcher.matchesEach(new IllegalArgumentException("test", new Error("diff"))), is(true));
        assertThat(matcher.matchesEach(new Error("diff", new IllegalArgumentException("test"))), is(false));
    }
    
    @Test
    public void testMatchesEach5() {
        ThrowableBaseMatcher matcher = new TestOuterMatcher(false, IllegalArgumentException.class);
        
        // 例外の型が適切に評価される - 第一引数 false の場合はサブクラス例外も一致と評価される。
        assertThat(matcher.matchesEach(new RuntimeException("test")), is(false));
        assertThat(matcher.matchesEach(new NumberFormatException("test")), is(true));
    }
    
    @Test
    public void testMatchesEach6() {
        TestInnerMatcher inner = new TestInnerMatcher();
        ThrowableBaseMatcher matcher = new TestOuterMatcher(inner);
        Throwable t = new IllegalArgumentException("test");
        matcher.matchesEach(t);
        
        // スローされた例外が inner に渡される。
        assertThat(inner.givenViaMatchesSafely, sameInstance(t));
    }
    
    @Test
    public void testDescribeTo1() {
        ThrowableBaseMatcher matcher1 = new TestOuterMatcher(true, Error.class, "testMsg");
        assertThat(matcher1.toString(), is("testTag <java.lang.Error (testMsg)>"));
        
        ThrowableBaseMatcher matcher2 = new TestOuterMatcher(false, Throwable.class, null);
        assertThat(matcher2.toString(), is("testTag <java.lang.Throwable (null)>"));
        
        ThrowableBaseMatcher matcher3 = new TestOuterMatcher(true, IOException.class);
        assertThat(matcher3.toString(), is("testTag <java.io.IOException>"));
        
        ThrowableBaseMatcher matcher4 = new TestOuterMatcher(new TestInnerMatcher());
        assertThat(matcher4.toString(), is("testTag <I'm TestInnerMatcher.>"));
    }
    
    @Test
    public void testDescribeTo2() {
        ThrowableBaseMatcher matcher1 = new TestOuterMatcher(true, Error.class, "testMsg");
        
        try {
            // null を与えても安全に終了する。
            matcher1.describeTo(null);
        } catch (RuntimeException e) {
            fail();
        }
    }
}
