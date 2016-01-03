package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class RaiseExactTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testRaiseExact1() {
        // インスタンス化の検査
        assertThat(RaiseExact.raiseExact(Throwable.class), instanceOf(RaiseExact.class));
        assertThat(RaiseExact.raiseExact(Exception.class, "message"), instanceOf(RaiseExact.class));
        assertThat(RaiseExact.raiseExact(RuntimeException.class, null), instanceOf(RaiseExact.class));
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRaiseExact2() {
        RaiseExact.raiseExact(null);
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRaiseExact3() {
        RaiseExact.raiseExact(null, "message");
    }
    
    @Test
    public void testMatchesSafely() {
        // サブクラスは不合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(); }), not(RaiseExact.raiseExact(RuntimeException.class)));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), RaiseExact.raiseExact(RuntimeException.class));
        assertThat(Testee.of(() -> { throw new NullPointerException(); }), not(RaiseExact.raiseExact(RuntimeException.class)));
    }
}
