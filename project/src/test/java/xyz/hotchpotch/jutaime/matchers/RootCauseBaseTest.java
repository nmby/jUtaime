package xyz.hotchpotch.jutaime.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.Testee;
import xyz.hotchpotch.jutaime.matchers.RootCauseBase;

public class RootCauseBaseTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
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
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testMatchesSafely1() {
        // cause 無しの場合
        Throwable t1 = new IOException("1");
        assertThat(Testee.of(() -> { throw t1; }), new RootCauseBase(true, IOException.class, "1"));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, UnsupportedOperationException.class, "2")));
        
        TestMatcher testMatcher = new TestMatcher();
        new RootCauseBase(testMatcher).matchesSafely(Testee.of(() -> { throw t1; }));
        assertThat(testMatcher.given, is(Arrays.asList(t1)));
    }
    
    @Test
    public void testMatchesSafely2() {
        // 例外チェインが直線状の場合
        Throwable t1 = new IOException("1");
        Throwable t2 = new UnsupportedOperationException("2");
        Throwable t3 = new IllegalArgumentException("3");
        t1.initCause(t2);
        t2.initCause(t3);
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IOException.class, "1")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, UnsupportedOperationException.class, "2")));
        assertThat(Testee.of(() -> { throw t1; }), new RootCauseBase(true, IllegalArgumentException.class, "3"));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, NoSuchElementException.class, "4")));
        
        TestMatcher testMatcher = new TestMatcher();
        new RootCauseBase(testMatcher).matchesSafely(Testee.of(() -> { throw t1; }));
        assertThat(testMatcher.given, is(Arrays.asList(t3)));
    }
    
    @Test
    public void testMatchesSafely3() {
        // 例外チェインが 1 -> 2 -> 3 -> 1... というループの場合
        Throwable t1 = new IOException("1");
        Throwable t2 = new UnsupportedOperationException("2");
        Throwable t3 = new IllegalArgumentException("3");
        t1.initCause(t2);
        t2.initCause(t3);
        t3.initCause(t1);
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IOException.class, "1")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, UnsupportedOperationException.class, "2")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IllegalArgumentException.class, "3")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, NoSuchElementException.class, "4")));
        
        TestMatcher testMatcher = new TestMatcher();
        new RootCauseBase(testMatcher).matchesSafely(Testee.of(() -> { throw t1; }));
        assertThat(testMatcher.given, is(Arrays.asList()));
    }
    
    @Test
    public void testMatchesSafely4() {
        // 例外チェインが 1 -> 2 -> 1... というループの場合
        Throwable t1 = new IOException("1");
        Throwable t2 = new UnsupportedOperationException("2");
        t1.initCause(t2);
        t2.initCause(t1);
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IOException.class, "1")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, UnsupportedOperationException.class, "2")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IllegalArgumentException.class, "3")));
        
        TestMatcher testMatcher = new TestMatcher();
        new RootCauseBase(testMatcher).matchesSafely(Testee.of(() -> { throw t1; }));
        assertThat(testMatcher.given, is(Arrays.asList()));
    }
    
    @Test
    public void testMatchesSafely5() {
        // 例外チェインが 1 -> 2 -> 3 -> 2... というループの場合
        Throwable t1 = new IOException("1");
        Throwable t2 = new UnsupportedOperationException("2");
        Throwable t3 = new IllegalArgumentException("3");
        t1.initCause(t2);
        t2.initCause(t3);
        t3.initCause(t2);
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IOException.class, "1")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, UnsupportedOperationException.class, "2")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, IllegalArgumentException.class, "3")));
        assertThat(Testee.of(() -> { throw t1; }), not(new RootCauseBase(true, NoSuchElementException.class, "4")));
        
        TestMatcher testMatcher = new TestMatcher();
        new RootCauseBase(testMatcher).matchesSafely(Testee.of(() -> { throw t1; }));
        assertThat(testMatcher.given, is(Arrays.asList()));
    }
    
    @Test
    public void testDescribeTo() {
        assertThat(new RootCauseBase(false, Exception.class).toString(), is("rootCause <java.lang.Exception>"));
        assertThat(new RootCauseBase(true, Error.class, "msg").toString(), is("rootCause <java.lang.Error (msg)>"));
        assertThat(new RootCauseBase(new TestMatcher()).toString(), is("rootCause <I'm TestMatcher.>"));
    }
}
