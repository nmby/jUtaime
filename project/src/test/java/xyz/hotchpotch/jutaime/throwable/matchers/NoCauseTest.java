package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class NoCauseTest {
    
    @Test
    public void testNoCause() {
        // インスタンス化の検査
        assertThat(NoCause.noCause(), instanceOf(NoCause.class));
    }
    
    @Test
    public void testMatchesSafelyTestee() {
        // 正常終了時は不合格と判定する。
        assertThat(Testee.of(() -> {}), not(NoCause.noCause()));
        assertThat(Testee.of(() -> null), not(NoCause.noCause()));
        assertThat(Testee.of(() -> 1 + 2), not(NoCause.noCause()));
        assertThat(Testee.of(() -> new Exception("not thrown")), not(NoCause.noCause()));
        
        // 例外スロー（cause なし）の場合は合格と判定する。
        assertThat(Testee.of(() -> { throw new Throwable(); }), NoCause.noCause());
        assertThat(Testee.of(() -> { throw new Error(); }), NoCause.noCause());
        assertThat(Testee.of(() -> { throw new Exception(); }), NoCause.noCause());
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), NoCause.noCause());
        assertThat(Testee.of(() -> { throw new RuntimeException((Throwable) null); }), NoCause.noCause());
        
        // 例外スロー（cause あり）の場合は不合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(new Error()); }), not(NoCause.noCause()));
        assertThat(Testee.of(() -> { throw new Error(new RuntimeException(new Throwable())); }), not(NoCause.noCause()));
    }
    
    @Test
    public void testDescribeTo() {
        assertThat(NoCause.noCause().toString(), is("has no cause"));
        
        try {
            // null を与えても安全に終了する。
            NoCause.noCause().describeTo(null);
        } catch (NullPointerException e) {
            fail();
        }
    }
}
