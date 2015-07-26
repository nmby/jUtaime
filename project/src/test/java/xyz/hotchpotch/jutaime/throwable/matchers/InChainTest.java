package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class InChainTest {
    
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
    public void testInChain1() {
        // インスタンス化の検査
        assertThat(InChain.inChain(Throwable.class), instanceOf(InChain.class));
        assertThat(InChain.inChain(Exception.class, "message"), instanceOf(InChain.class));
        assertThat(InChain.inChain(RuntimeException.class, null), instanceOf(InChain.class));
        assertThat(InChain.inChain(new TestMatcher()), instanceOf(InChain.class));
    }
    
    @Test(expected  = NullPointerException.class)
    public void testInChain2() {
        InChain.inChain((Class<? extends Throwable>) null);
    }
    
    @Test(expected  = NullPointerException.class)
    public void testInChain3() {
        InChain.inChain(null, "message");
    }
    
    @Test(expected  = NullPointerException.class)
    public void testInChain4() {
        InChain.inChain((Matcher<Throwable>) null);
    }
    
    @Test
    public void testMatchesSafely() {
        // サブクラスも合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(); }), not(InChain.inChain(RuntimeException.class)));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), InChain.inChain(RuntimeException.class));
        assertThat(Testee.of(() -> { throw new NullPointerException(); }), InChain.inChain(RuntimeException.class));
    }
}
