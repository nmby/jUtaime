package xyz.hotchpotch.jutaime;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOError;
import java.io.IOException;

import org.junit.Test;

import xyz.hotchpotch.jutaime.RaiseMatcher;
import xyz.hotchpotch.jutaime.RaiseMatchers;
import xyz.hotchpotch.jutaime.Testee;
import xyz.hotchpotch.jutaime.matchers.RaiseNothing;

public class RaiseMatchersTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testRaiseClass() {
        assertThat(RaiseMatchers.raise(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException(new Throwable()); }),
                RaiseMatchers.raise(Exception.class));
        assertThat(Testee.of(() -> { throw new IOError(new Throwable()); }),
                not(RaiseMatchers.raise(Exception.class)));
    }
    
    @Test
    public void testRaiseExactClass() {
        assertThat(RaiseMatchers.raiseExact(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("msg"); }),
                RaiseMatchers.raiseExact(Exception.class));
        assertThat(Testee.of(() -> { throw new IOException("msg"); }),
                not(RaiseMatchers.raiseExact(Exception.class)));
    }
    
    @Test
    public void testRaiseClassString() {
        assertThat(RaiseMatchers.raise(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException("test msg"); }),
                RaiseMatchers.raise(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new IOException("diff msg"); }),
                not(RaiseMatchers.raise(Exception.class, "test msg")));
    }
    
    @Test
    public void testRaiseExactClassString() {
        assertThat(RaiseMatchers.raiseExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("test msg"); }),
                RaiseMatchers.raiseExact(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new IOException("test msg"); }),
                not(RaiseMatchers.raiseExact(Exception.class, "test msg")));
    }
    
    @Test
    public void testRaiseString() {
        assertThat(RaiseMatchers.raise("test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException("test msg"); }),
                RaiseMatchers.raise("test msg"));
        assertThat(Testee.of(() -> { throw new IOException("diff msg"); }),
                not(RaiseMatchers.raise("test msg")));
    }
    
    @Test
    public void testRaiseMatcher() {
        assertThat(RaiseMatchers.raise(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException(new Throwable()); }),
                RaiseMatchers.raise(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> { throw new IOError(new Throwable()); }),
                not(RaiseMatchers.raise(instanceOf(Exception.class))));
    }
    
    @Test
    public void testRaiseNothing1() {
        assertThat(RaiseMatchers.raiseNothing(), instanceOf(RaiseNothing.class));
        
        assertThat(Testee.of(() -> {}),
                RaiseMatchers.raiseNothing());
        assertThat(Testee.of(() -> { throw new Exception(); }),
                not(RaiseMatchers.raiseNothing()));
    }
    
    @Test
    public void testRaiseNothing2() {
        assertThat(RaiseMatchers.raiseNothing(not(123)), instanceOf(RaiseNothing.class));
        
        assertThat(Testee.of(() -> null),
                RaiseMatchers.raiseNothing(nullValue()));
        assertThat(Testee.of(() -> { throw new Exception(); }),
                not(RaiseMatchers.raiseNothing(nullValue())));
    }
    
    @Test
    public void testRaiseNothing3() {
        assertThat(RaiseMatchers.raiseNothing(123), instanceOf(RaiseNothing.class));
        
        assertThat(Testee.of(() -> 123),
                RaiseMatchers.raiseNothing(123));
        assertThat(Testee.of(() -> { throw new Exception(); }),
                not(RaiseMatchers.raiseNothing(123)));
    }
    
    @Test
    public void testNoCause() {
        assertThat(RaiseMatchers.noCause(), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(); }),
                RaiseMatchers.noCause());
        assertThat(Testee.of(() -> { throw new Exception(new Throwable()); }),
                not(RaiseMatchers.noCause()));
    }
    
    @Test
    public void testRootCauseClass() {
        assertThat(RaiseMatchers.rootCause(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new RuntimeException()); }),
                RaiseMatchers.rootCause(Exception.class));
        assertThat(Testee.of(() -> { throw new Throwable(new Throwable()); }),
                not(RaiseMatchers.rootCause(Exception.class)));
    }
    
    @Test
    public void testRootCauseExactClass() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new Exception()); }),
                RaiseMatchers.rootCauseExact(Exception.class));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException()); }),
                not(RaiseMatchers.rootCauseExact(Exception.class)));
    }
    
    @Test
    public void testRootCauseClassString() {
        assertThat(RaiseMatchers.rootCause(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("test msg")); }),
                RaiseMatchers.rootCause(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("diff msg")); }),
                not(RaiseMatchers.rootCause(Exception.class, "test msg")));
    }
    
    @Test
    public void testRootCauseExactClassString() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new Exception("test msg")); }),
                RaiseMatchers.rootCauseExact(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("test msg")); }),
                not(RaiseMatchers.rootCauseExact(Exception.class, "test msg")));
    }
    
    @Test
    public void testRootCauseString() {
        assertThat(RaiseMatchers.rootCause("test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("test msg")); }),
                RaiseMatchers.rootCause("test msg"));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("diff msg")); }),
                not(RaiseMatchers.rootCause("test msg")));
    }
    
    @Test
    public void testRootCauseMatcher() {
        assertThat(RaiseMatchers.rootCause(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new RuntimeException()); }),
                RaiseMatchers.rootCause(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> { throw new Throwable(new RuntimeException(new Error())); }),
                not(RaiseMatchers.rootCause(instanceOf(Exception.class))));
    }
    
    @Test
    public void testInChainClass() {
        assertThat(RaiseMatchers.inChain(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException(new Error())); }),
                RaiseMatchers.inChain(Exception.class));
        assertThat(Testee.of(() -> { throw new Error(new IOError(new Error())); }),
                not(RaiseMatchers.inChain(Exception.class)));
    }
    
    @Test
    public void testInChainExactClass() {
        assertThat(RaiseMatchers.inChainExact(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception(new Error())); }),
                RaiseMatchers.inChainExact(Exception.class));
        assertThat(Testee.of(() -> { throw new Error(new IOException(new Error())); }),
                not(RaiseMatchers.inChainExact(Exception.class)));
    }
    
    @Test
    public void testInChainClassString() {
        assertThat(RaiseMatchers.inChain(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException("test msg", new Error())); }),
                RaiseMatchers.inChain(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Error(new IOException("diff msg", new Error())); }),
                not(RaiseMatchers.inChain(Exception.class, "test msg")));
    }
    
    @Test
    public void testInChainExactClassString() {
        assertThat(RaiseMatchers.inChainExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception("test msg", new Error())); }),
                RaiseMatchers.inChainExact(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Error(new IOException("test msg", new Error())); }),
                not(RaiseMatchers.inChainExact(Exception.class, "test msg")));
    }
    
    @Test
    public void testInChainString() {
        assertThat(RaiseMatchers.inChain("test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException("test msg", new Error())); }),
                RaiseMatchers.inChain("test msg"));
        assertThat(Testee.of(() -> { throw new Error(new IOException("diff msg", new Error())); }),
                not(RaiseMatchers.inChain("test msg")));
    }
    
    @Test
    public void testInChainMatcher() {
        assertThat(RaiseMatchers.inChain(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException(new Error())); }),
                RaiseMatchers.inChain(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> { throw new Error(new IOError(new Error())); }),
                not(RaiseMatchers.inChain(instanceOf(Exception.class))));
    }
}
