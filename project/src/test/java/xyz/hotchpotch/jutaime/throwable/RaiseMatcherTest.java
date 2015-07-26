package xyz.hotchpotch.jutaime.throwable;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOError;
import java.io.IOException;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.matchers.InChainExact;
import xyz.hotchpotch.jutaime.throwable.matchers.Raise;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCause;

public class RaiseMatcherTest {
    
    @Test
    public void testDescribeTo() {
        assertThat(RaiseMatchers.raise(Exception.class)
                .inChainExact(Throwable.class, "msg1")
                .rootCause(Error.class, "msg2")
                .toString(),
                is(String.join(", ",
                        Raise.raise(Exception.class).toString(),
                        InChainExact.inChainExact(Throwable.class, "msg1").toString(),
                        RootCause.rootCause(Error.class, "msg2").toString())));
        
        try {
            RaiseMatchers.noCause().describeTo(null);
        } catch (RuntimeException e) {
            fail();
        }
    }
    
    @Test
    public void testMatchesSafely() {
        assertThat(Testee.of(() -> { throw new Error("msg1", new Error("msg2", new Error("msg3"))); }),
                RaiseMatchers.raise(Error.class, "msg1").inChain(Error.class, "msg2").rootCause(Error.class, "msg3"));
        
        assertThat(Testee.of(() -> { throw new Error("diff", new Error("msg2", new Error("msg3"))); }),
                not(RaiseMatchers.raise(Error.class, "msg1").inChain(Error.class, "msg2").rootCause(Error.class, "msg3")));
        assertThat(Testee.of(() -> { throw new Error("msg1", new Error("diff", new Error("msg3"))); }),
                not(RaiseMatchers.raise(Error.class, "msg1").inChain(Error.class, "msg2").rootCause(Error.class, "msg3")));
        assertThat(Testee.of(() -> { throw new Error("msg1", new Error("msg2", new Error("diff"))); }),
                not(RaiseMatchers.raise(Error.class, "msg1").inChain(Error.class, "msg2").rootCause(Error.class, "msg3")));
        assertThat(Testee.of(() -> { throw new Error("diff", new Error("diff", new Error("diff"))); }),
                not(RaiseMatchers.raise(Error.class, "msg1").inChain(Error.class, "msg2").rootCause(Error.class, "msg3")));
    }
    
    @Test
    public void testRaiseClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.rootCause(Exception.class).raise(Error.class),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOError(new Exception()); }),
                RaiseMatchers.rootCause(Exception.class).raise(Error.class));
        assertThat(Testee.of(() -> { throw new IOException(new Exception()); }),
                not(RaiseMatchers.rootCause(Exception.class).raise(Error.class)));
    }
    
    @Test
    public void testRaiseExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.rootCause(instanceOf(Exception.class)).raiseExact(Error.class),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception()); }),
                RaiseMatchers.rootCause(instanceOf(Exception.class)).raiseExact(Error.class));
        assertThat(Testee.of(() -> { throw new IOError(new Exception()); }),
                not(RaiseMatchers.rootCause(instanceOf(Exception.class)).raiseExact(Error.class)));
    }
    
    @Test
    public void testRaiseClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.rootCause(Exception.class, "test msg1").raise(Error.class, "test msg2"),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new LinkageError("test msg2", new Exception("test msg1")); }),
                RaiseMatchers.rootCause(Exception.class, "test msg1").raise(Error.class, "test msg2"));
        assertThat(Testee.of(() -> { throw new Throwable("test msg2", new Exception("test msg1")); }),
                not(RaiseMatchers.rootCause(Exception.class, "test msg1").raise(Error.class, "test msg2")));
    }
    
    @Test
    public void testRaiseExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class).raiseExact(Error.class, "test msg"),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error("test msg", new Exception()); }),
                RaiseMatchers.rootCauseExact(Exception.class).raiseExact(Error.class, "test msg"));
        assertThat(Testee.of(() -> { throw new LinkageError("test msg", new Exception()); }),
                not(RaiseMatchers.rootCauseExact(Exception.class).raiseExact(Error.class, "test msg")));
    }
    
    @Test
    public void testRaiseMatcherOfThrowable() {
        assertThat(RaiseMatchers.rootCauseExact(Exception.class, "test msg").raise(instanceOf(Error.class)),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception("test msg")); }),
                RaiseMatchers.rootCauseExact(Exception.class, "test msg").raise(instanceOf(Error.class)));
        assertThat(Testee.of(() -> { throw new Exception(new Exception("test msg")); }),
                not(RaiseMatchers.rootCauseExact(Exception.class, "test msg").raise(instanceOf(Error.class))));
    }
    
    @Test
    public void testNoCause() {
        assertThat(RaiseMatchers.raise(Exception.class).noCause(),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new IOException(); }),
                RaiseMatchers.raise(Exception.class).noCause());
        assertThat(Testee.of(() -> { throw new IOException(new Exception()); }),
                not(RaiseMatchers.raise(Exception.class).noCause()));
    }
    
    @Test
    public void testRootCauseClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.inChain(instanceOf(Exception.class)).rootCause(Error.class),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(new LinkageError()); }),
                RaiseMatchers.inChain(instanceOf(Exception.class)).rootCause(Error.class));
        assertThat(Testee.of(() -> { throw new Exception(new Throwable()); }),
                not(RaiseMatchers.inChain(instanceOf(Exception.class)).rootCause(Error.class)));
    }
    
    @Test
    public void testRootCauseExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.inChain(Exception.class, "test msg").rootCauseExact(Error.class),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("test msg", new Error()); }),
                RaiseMatchers.inChain(Exception.class, "test msg").rootCauseExact(Error.class));
        assertThat(Testee.of(() -> { throw new Exception("test msg", new LinkageError()); }),
                not(RaiseMatchers.inChain(Exception.class, "test msg").rootCauseExact(Error.class)));
    }
    
    @Test
    public void testRootCauseClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.inChainExact(Exception.class).rootCause(Error.class, "test msg"),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(new LinkageError("test msg")); }),
                RaiseMatchers.inChainExact(Exception.class).rootCause(Error.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Exception(new LinkageError("diff msg")); }),
                not(RaiseMatchers.inChainExact(Exception.class).rootCause(Error.class, "test msg")));
    }
    
    @Test
    public void testRootCauseExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.inChainExact(Exception.class, "test msg1").rootCauseExact(Error.class, "test msg2"),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("test msg1", new Error("test msg2")); }),
                RaiseMatchers.inChainExact(Exception.class, "test msg1").rootCauseExact(Error.class, "test msg2"));
        assertThat(Testee.of(() -> { throw new Exception("test msg1", new LinkageError("test msg2")); }),
                not(RaiseMatchers.inChainExact(Exception.class, "test msg1").rootCauseExact(Error.class, "test msg2")));
    }
    
    @Test
    public void testRootCauseMatcherOfThrowable() {
        assertThat(RaiseMatchers.raise(Exception.class).rootCause(instanceOf(Error.class)),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(new LinkageError()); }),
                RaiseMatchers.raise(Exception.class).rootCause(instanceOf(Error.class)));
        assertThat(Testee.of(() -> { throw new Exception(new Throwable()); }),
                not(RaiseMatchers.raise(Exception.class).rootCause(instanceOf(Error.class))));
    }
    
    @Test
    public void testInChainClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.raise(instanceOf(Exception.class)).inChain(Error.class),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(new IOError(new Throwable())); }),
                RaiseMatchers.raise(instanceOf(Exception.class)).inChain(Error.class));
        assertThat(Testee.of(() -> { throw new Exception(new Throwable(new Throwable())); }),
                not(RaiseMatchers.raise(instanceOf(Exception.class)).inChain(Error.class)));
    }
    
    @Test
    public void testInChainExactClassOfQextendsThrowable() {
        assertThat(RaiseMatchers.raise(Exception.class, "test msg").inChainExact(Error.class),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("test msg", new Error(new Exception())); }),
                RaiseMatchers.raise(Exception.class, "test msg").inChainExact(Error.class));
        assertThat(Testee.of(() -> { throw new Exception("test msg", new IOError(new Exception())); }),
                not(RaiseMatchers.raise(Exception.class, "test msg").inChainExact(Error.class)));
    }
    
    @Test
    public void testInChainClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.raiseExact(Exception.class).inChain(Error.class, "test msg"),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(new LinkageError("test msg", new Exception())); }),
                RaiseMatchers.raiseExact(Exception.class).inChain(Error.class, "test msg"));
        assertThat(Testee.of(() -> { throw new Exception(new LinkageError("diff msg", new Exception())); }),
                not(RaiseMatchers.raiseExact(Exception.class).inChain(Error.class, "test msg")));
    }
    
    @Test
    public void testInChainExactClassOfQextendsThrowableString() {
        assertThat(RaiseMatchers.raiseExact(Exception.class, "test msg1").inChainExact(Error.class, "test msg2"),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception("test msg1", new Error("test msg2", new Throwable())); }),
                RaiseMatchers.raiseExact(Exception.class, "test msg1").inChainExact(Error.class, "test msg2"));
        assertThat(Testee.of(() -> { throw new Exception("test msg1", new LinkageError("test msg2", new Throwable())); }),
                not(RaiseMatchers.raiseExact(Exception.class, "test msg1").inChainExact(Error.class, "test msg2")));
    }
    
    @Test
    public void testInChainMatcherOfThrowable() {
        assertThat(RaiseMatchers.raise(Exception.class).inChain(instanceOf(Error.class)),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Exception(new IOError(new Throwable())); }),
                RaiseMatchers.raise(Exception.class).inChain(instanceOf(Error.class)));
        assertThat(Testee.of(() -> { throw new Exception(new IOException(new Throwable())); }),
                not(RaiseMatchers.raise(Exception.class).inChain(instanceOf(Error.class))));
    }
    
    @Test
    public void testAnd() {
        assertThat(RaiseMatchers.inChain(Exception.class).and(RaiseMatchers.raiseExact(Error.class)),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error(new Exception()); }),
                RaiseMatchers.inChain(Exception.class).and(RaiseMatchers.raiseExact(Error.class)));
        assertThat(Testee.of(() -> { throw new Throwable(new Exception()); }),
                not(RaiseMatchers.inChain(Exception.class).and(RaiseMatchers.raiseExact(Error.class))));
    }
    
    @Test
    public void testNot() {
        assertThat(RaiseMatchers.noCause().not(RaiseMatchers.raise(Exception.class)),
                instanceOf(RaiseMatcher.class));
        
        assertThat(Testee.of(() -> { throw new Error();}),
                RaiseMatchers.noCause().not(RaiseMatchers.raise(Exception.class)));
        assertThat(Testee.of(() -> { throw new Exception();}),
                not(RaiseMatchers.noCause().not(RaiseMatchers.raise(Exception.class))));
    }
}
