package xyz.hotchpotch.jutaime.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import xyz.hotchpotch.jutaime.Testee;
import xyz.hotchpotch.jutaime.matchers.RootCauseExact;

public class RootCauseExactTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testRootCauseExact1() {
        // インスタンス化の検査
        assertThat(RootCauseExact.rootCauseExact(Throwable.class), instanceOf(RootCauseExact.class));
        assertThat(RootCauseExact.rootCauseExact(Exception.class, "message"), instanceOf(RootCauseExact.class));
        assertThat(RootCauseExact.rootCauseExact(RuntimeException.class, null), instanceOf(RootCauseExact.class));
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRootCauseExact2() {
        RootCauseExact.rootCauseExact(null);
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRootCauseExact3() {
        RootCauseExact.rootCauseExact(null, "message");
    }
    
    @Test
    public void testMatchesSafely() {
        // サブクラスは不合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(); }), not(RootCauseExact.rootCauseExact(RuntimeException.class)));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), RootCauseExact.rootCauseExact(RuntimeException.class));
        assertThat(Testee.of(() -> { throw new NullPointerException(); }), not(RootCauseExact.rootCauseExact(RuntimeException.class)));
    }
}
