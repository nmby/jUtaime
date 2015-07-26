package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class RootCauseTest {
    
    private static class TestMatcher extends TypeSafeMatcher<Throwable> {
        @Override
        protected boolean matchesSafely(Throwable t) {
            return false;
        }
        
        @Override
        public void describeTo(Description description) {
            description.appendText("I'm TestMatcher.");
        }
    }
    
    @Test
    public void testRootCause1() {
        // インスタンス化の検査
        assertThat(RootCause.rootCause(Throwable.class), instanceOf(RootCause.class));
        assertThat(RootCause.rootCause(Exception.class, "message"), instanceOf(RootCause.class));
        assertThat(RootCause.rootCause(RuntimeException.class, null), instanceOf(RootCause.class));
        assertThat(RootCause.rootCause(new TestMatcher()), instanceOf(RootCause.class));
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRootCause2() {
        RootCause.rootCause((Class<? extends Throwable>) null);
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRootCause3() {
        RootCause.rootCause(null, "message");
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRootCause4() {
        RootCause.rootCause((Matcher<Throwable>) null);
    }
    
    @Test
    public void testMatchesSafely() {
        // サブクラスも合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(); }), not(RootCause.rootCause(RuntimeException.class)));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), RootCause.rootCause(RuntimeException.class));
        assertThat(Testee.of(() -> { throw new NullPointerException(); }), RootCause.rootCause(RuntimeException.class));
    }
}
