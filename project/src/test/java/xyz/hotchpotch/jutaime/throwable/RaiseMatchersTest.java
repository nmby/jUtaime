package xyz.hotchpotch.jutaime.throwable;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.matchers.InChain;
import xyz.hotchpotch.jutaime.throwable.matchers.InChainExact;
import xyz.hotchpotch.jutaime.throwable.matchers.NoCause;
import xyz.hotchpotch.jutaime.throwable.matchers.Raise;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseExact;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseNothing;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCause;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCauseExact;

public class RaiseMatchersTest {
    
    @Test
    public void testRaiseClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.raise(Exception.class), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.raise(Exception.class).toString(),
                is(Raise.raise(Exception.class).toString()));
    }
    
    @Test
    public void testRaiseExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.raiseExact(Exception.class), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.raiseExact(Exception.class).toString(),
                is(RaiseExact.raiseExact(Exception.class).toString()));
    }
    
    @Test
    public void testRaiseClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.raise(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.raise(Exception.class, "test msg").toString(),
                is(Raise.raise(Exception.class, "test msg").toString()));
    }
    
    @Test
    public void testRaiseExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.raiseExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.raiseExact(Exception.class, "test msg").toString(),
                is(RaiseExact.raiseExact(Exception.class, "test msg").toString()));
    }
    
    @Test
    public void testRaiseMatcherOfThrowable() {
        assertThat(RaiseMatchers.raise(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.raise(instanceOf(Exception.class)).toString(),
                is(Raise.raise(instanceOf(Exception.class)).toString()));
    }
    
    @Test
    public void testRaiseNothing() {
        assertThat(RaiseMatchers.raiseNothing(), instanceOf(RaiseNothing.class));
    }
    
    @Test
    public void testNoCause() {
        assertThat(RaiseMatchers.noCause(), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.noCause().toString(),
                is(NoCause.noCause().toString()));
    }
    
    @Test
    public void testRootCauseClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.rootCause(Exception.class), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.rootCause(Exception.class).toString(),
                is(RootCause.rootCause(Exception.class).toString()));
    }
    
    @Test
    public void testRootCauseExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.rootCauseExact(Exception.class).toString(),
                is(RootCauseExact.rootCauseExact(Exception.class).toString()));
    }
    
    @Test
    public void testRootCauseClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.rootCause(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.rootCause(Exception.class, "test msg").toString(),
                is(RootCause.rootCause(Exception.class, "test msg").toString()));
    }
    
    @Test
    public void testRootCauseExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.rootCauseExact(Exception.class, "test msg").toString(),
                is(RootCauseExact.rootCauseExact(Exception.class, "test msg").toString()));
    }
    
    @Test
    public void testRootCauseMatcherOfThrowable() {
        assertThat(RaiseMatchers.rootCause(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.rootCause(instanceOf(Exception.class)).toString(),
                is(RootCause.rootCause(instanceOf(Exception.class)).toString()));
    }
    
    @Test
    public void testInChainClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.inChain(Exception.class), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.inChain(Exception.class).toString(),
                is(InChain.inChain(Exception.class).toString()));
    }
    
    @Test
    public void testInChainExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.inChainExact(Exception.class), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.inChainExact(Exception.class).toString(),
                is(InChainExact.inChainExact(Exception.class).toString()));
    }
    
    @Test
    public void testInChainClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.inChain(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.inChain(Exception.class, "test msg").toString(),
                is(InChain.inChain(Exception.class, "test msg").toString()));
    }
    
    @Test
    public void testInChainExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.inChainExact(Exception.class, "test msg"), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.inChainExact(Exception.class, "test msg").toString(),
                is(InChainExact.inChainExact(Exception.class, "test msg").toString()));
    }
    
    @Test
    public void testInChainMatcherOfThrowable() {
        assertThat(RaiseMatchers.inChain(instanceOf(Exception.class)), instanceOf(RaiseMatcher.class));
        assertThat(RaiseMatchers.inChain(instanceOf(Exception.class)).toString(),
                is(InChain.inChain(instanceOf(Exception.class)).toString()));
    }
}
