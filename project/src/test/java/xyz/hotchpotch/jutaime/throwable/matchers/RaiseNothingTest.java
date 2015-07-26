package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class RaiseNothingTest {
    
    @Test
    public void testRaiseNothing() {
        // インスタンス化の検査
        assertThat(RaiseNothing.raiseNothing(), instanceOf(RaiseNothing.class));
    }
    
    @Test
    public void testMatchesSafelyTestee() {
        // 正常終了時は合格と判定する。
        assertThat(Testee.of(() -> {}), RaiseNothing.raiseNothing());
        assertThat(Testee.of(() -> null), RaiseNothing.raiseNothing());
        assertThat(Testee.of(() -> 1 + 2), RaiseNothing.raiseNothing());
        assertThat(Testee.of(() -> new Exception("not thrown")), RaiseNothing.raiseNothing());
        
        // 例外発生時は不合格と判定する。
        assertThat(Testee.of(() -> { throw new Throwable(); }), not(RaiseNothing.raiseNothing()));
        assertThat(Testee.of(() -> { throw new Error(); }), not(RaiseNothing.raiseNothing()));
        assertThat(Testee.of(() -> { throw new Exception(); }), not(RaiseNothing.raiseNothing()));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), not(RaiseNothing.raiseNothing()));
    }
    
    @Test
    public void testDescribeTo() {
        assertThat(RaiseNothing.raiseNothing().toString(), is("throw nothing"));
        
        try {
            // null を与えても安全に終了する。
            RaiseNothing.raiseNothing().describeTo(null);
        } catch (NullPointerException e) {
            fail();
        }
    }
}
