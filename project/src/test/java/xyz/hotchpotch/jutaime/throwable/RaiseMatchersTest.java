package xyz.hotchpotch.jutaime.throwable;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOError;
import java.io.IOException;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.matchers.RaiseNothing;

public class RaiseMatchersTest {
    
    @Test
    public void testRaiseClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.raise(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException(new Throwable()); }),
                RaiseMatchers.raise(Exception.class));
        assertThat(Testee.of(() -> { throw new IOError(new Throwable()); }),
                not(RaiseMatchers.raise(Exception.class)));
    }
    
    @Test
    public void testRaiseExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.raiseExact(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("msg"); }),
                RaiseMatchers.raiseExact(Exception.class));
        assertThat(Testee.of(() -> { throw new IOException("msg"); }),
                not(RaiseMatchers.raiseExact(Exception.class)));
    }
    
    @Test
    public void testRaiseClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.raise(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException("test msg"); }),
                RaiseMatchers.raise(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new IOException("diff msg"); }),
                not(RaiseMatchers.raise(Exception.class, "test msg")));
    }
    
    @Test
    public void testRaiseExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.raiseExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("test msg"); }),
                RaiseMatchers.raiseExact(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new IOException("test msg"); }),
                not(RaiseMatchers.raiseExact(Exception.class, "test msg")));
    }
    
    @Test
    public void testRaiseMatcherOfThrowable() {
        assertThat(RaiseMatchers.raise(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException(new Throwable()); }),
                RaiseMatchers.raise(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> { throw new IOError(new Throwable()); }),
                not(RaiseMatchers.raise(instanceOf(Exception.class))));
    }
    
    @Test
    public void testRaiseNothing() {
        assertThat(RaiseMatchers.raiseNothing(), instanceOf(RaiseNothing.class));
        
        assertThat(Testee.of(() -> {}),
                RaiseMatchers.raiseNothing());
        assertThat(Testee.of(() -> { throw new Exception(); }),
                not(RaiseMatchers.raiseNothing()));
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
    public void testRootCauseClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.rootCause(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new RuntimeException()); }),
                RaiseMatchers.rootCause(Exception.class));
        assertThat(Testee.of(() -> { throw new Throwable(new Throwable()); }),
                not(RaiseMatchers.rootCause(Exception.class)));
    }
    
    @Test
    public void testRootCauseExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new Exception()); }),
                RaiseMatchers.rootCauseExact(Exception.class));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException()); }),
                not(RaiseMatchers.rootCauseExact(Exception.class)));
    }
    
    @Test
    public void testRootCauseClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.rootCause(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("test msg")); }),
                RaiseMatchers.rootCause(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("diff msg")); }),
                not(RaiseMatchers.rootCause(Exception.class, "test msg")));
    }
    
    @Test
    public void testRootCauseExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new Exception("test msg")); }),
                RaiseMatchers.rootCauseExact(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Throwable(new IOException("test msg")); }),
                not(RaiseMatchers.rootCauseExact(Exception.class, "test msg")));
    }
    
    @Test
    public void testRootCauseMatcherOfThrowable() {
        assertThat(RaiseMatchers.rootCause(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Throwable(new RuntimeException()); }),
                RaiseMatchers.rootCause(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> { throw new Throwable(new RuntimeException(new Error())); }),
                not(RaiseMatchers.rootCause(instanceOf(Exception.class))));
    }
    
    @Test
    public void testInChainClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.inChain(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException(new Error())); }),
                RaiseMatchers.inChain(Exception.class));
        assertThat(Testee.of(() -> { throw new Error(new IOError(new Error())); }),
                not(RaiseMatchers.inChain(Exception.class)));
    }
    
    @Test
    public void testInChainExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.inChainExact(Exception.class), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception(new Error())); }),
                RaiseMatchers.inChainExact(Exception.class));
        assertThat(Testee.of(() -> { throw new Error(new IOException(new Error())); }),
                not(RaiseMatchers.inChainExact(Exception.class)));
    }
    
    @Test
    public void testInChainClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.inChain(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException("test msg", new Error())); }),
                RaiseMatchers.inChain(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Error(new IOException("diff msg", new Error())); }),
                not(RaiseMatchers.inChain(Exception.class, "test msg")));
    }
    
    @Test
    public void testInChainExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.inChainExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception("test msg", new Error())); }),
                RaiseMatchers.inChainExact(Exception.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Error(new IOException("test msg", new Error())); }),
                not(RaiseMatchers.inChainExact(Exception.class, "test msg")));
    }
    
    @Test
    public void testInChainMatcherOfThrowable() {
        assertThat(RaiseMatchers.inChain(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new IOException(new Error())); }),
                RaiseMatchers.inChain(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> { throw new Error(new IOError(new Error())); }),
                not(RaiseMatchers.inChain(instanceOf(Exception.class))));
    }
}
