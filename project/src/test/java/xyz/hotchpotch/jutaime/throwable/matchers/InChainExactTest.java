package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class InChainExactTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testInChainExact1() {
        // インスタンス化の検査
        assertThat(InChainExact.inChainExact(Throwable.class), instanceOf(InChainExact.class));
        assertThat(InChainExact.inChainExact(Exception.class, "message"), instanceOf(InChainExact.class));
        assertThat(InChainExact.inChainExact(RuntimeException.class, null), instanceOf(InChainExact.class));
    }
    
    @Test(expected  = NullPointerException.class)
    public void testInChainExact2() {
        InChainExact.inChainExact(null);
    }
    
    @Test(expected  = NullPointerException.class)
    public void testInChainExact3() {
        InChainExact.inChainExact(null, "message");
    }
    
    @Test
    public void testMatchesSafely() {
        // サブクラスは不合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(); }), not(InChainExact.inChainExact(RuntimeException.class)));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), InChainExact.inChainExact(RuntimeException.class));
        assertThat(Testee.of(() -> { throw new NullPointerException(); }), not(InChainExact.inChainExact(RuntimeException.class)));
    }
}
