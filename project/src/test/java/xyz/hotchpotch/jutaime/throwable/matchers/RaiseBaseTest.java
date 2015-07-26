package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class RaiseBaseTest {
    
    private static class TestMatcher extends TypeSafeMatcher<Throwable> {
        private final List<Throwable> given = new ArrayList<>();
        
        @Override
        protected boolean matchesSafely(Throwable t) {
            given.add(t);
            return false;
        }
        
        @Override
        public void describeTo(Description description) {
            description.appendText("I'm TestMatcher.");
        }
    }
    
    @Test
    public void testMatchesSafely() {
        // 例外チェインが 1 -> 2 -> 3 -> 1... というループの場合
        Throwable t1 = new IOException("1");
        Throwable t2 = new UnsupportedOperationException("2");
        Throwable t3 = new IllegalArgumentException("3");
        t1.initCause(t2);
        t2.initCause(t3);
        t3.initCause(t1);
        assertThat(Testee.of(() -> { throw t1; }), new RaiseBase(true, IOException.class, "1"));
        assertThat(Testee.of(() -> { throw t1; }), not(new RaiseBase(true, UnsupportedOperationException.class, "2")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RaiseBase(true, IllegalArgumentException.class, "3")));
        
        TestMatcher testMatcher = new TestMatcher();
        new RaiseBase(testMatcher).matchesSafely(Testee.of(() -> { throw t1; }));
        assertThat(testMatcher.given, is(Arrays.asList(t1)));
    }
    
    @Test
    public void testDescriptionTag() {
        assertThat(new RaiseBase(false, Exception.class).toString(), is("throw <java.lang.Exception>"));
        assertThat(new RaiseBase(true, Error.class, "msg").toString(), is("throw <java.lang.Error (msg)>"));
        assertThat(new RaiseBase(new TestMatcher()).toString(), is("throw <I'm TestMatcher.>"));
    }
}
