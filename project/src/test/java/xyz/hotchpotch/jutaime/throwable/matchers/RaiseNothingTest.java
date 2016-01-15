package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class RaiseNothingTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testRaiseNothing1() {
        // インスタンス化の検査
        assertThat(RaiseNothing.raiseNothing(), instanceOf(RaiseNothing.class));
    }
    
    @Test
    public void testRaiseNothing2() {
        // インスタンス化の検査
        assertThat(RaiseNothing.raiseNothing(not("A")), instanceOf(RaiseNothing.class));
        assertThat(RaiseNothing.raiseNothing(nullValue()), instanceOf(RaiseNothing.class));
        assertThat(Testee.of(() -> RaiseNothing.raiseNothing((Matcher<?>) null)), Raise.raise(NullPointerException.class));
    }
    
    @Test
    public void testRaiseNothing3() {
        // インスタンス化の検査
        assertThat(RaiseNothing.raiseNothing("A"), instanceOf(RaiseNothing.class));
        assertThat(RaiseNothing.raiseNothing(123), instanceOf(RaiseNothing.class));
        assertThat(Testee.of(() -> RaiseNothing.raiseNothing(null)), Raise.raise(NullPointerException.class));
    }
    
    @Test
    public void testMatchesSafelyTestee1() {
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
    public void testMatchesSafelyTestee2() {
        // 正常終了時かつ戻り値が期待通りの場合に合格と判定する。
        assertThat(Testee.of(() -> null), RaiseNothing.raiseNothing(nullValue()));
        assertThat(Testee.of(() -> null), not(RaiseNothing.raiseNothing(123)));
        assertThat(Testee.of(() -> 1 + 2), RaiseNothing.raiseNothing(3));
        assertThat(Testee.of(() -> 1 + 2), not(RaiseNothing.raiseNothing(4)));
        assertThat(Testee.of(() -> new Exception("not thrown")), RaiseNothing.raiseNothing(instanceOf(Exception.class)));
        assertThat(Testee.of(() -> new Exception("not thrown")), not(RaiseNothing.raiseNothing(instanceOf(Error.class))));
        
        // 例外発生時は不合格と判定する。
        assertThat(Testee.of(() -> { throw new Throwable(); }), not(RaiseNothing.raiseNothing(123)));
        assertThat(Testee.of(() -> { throw new Error(); }), not(RaiseNothing.raiseNothing("abc")));
        assertThat(Testee.of(() -> { throw new Exception(); }), not(RaiseNothing.raiseNothing(nullValue())));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), not(RaiseNothing.raiseNothing(is(true))));
        
        assertThat(Testee.of(() -> RaiseNothing.raiseNothing(null)), Raise.raise(NullPointerException.class));
    }
    
    @Test
    public void testDescribeTo1() {
        assertThat(RaiseNothing.raiseNothing().toString(), is("throws nothing"));
        
        try {
            // null を与えても安全に終了する。
            RaiseNothing.raiseNothing().describeTo(null);
        } catch (NullPointerException e) {
            fail();
        }
    }
    
    @Test
    public void testDescribeTo2() {
        assertThat(RaiseNothing.raiseNothing("A").toString(), is("throws nothing <is \"A\">"));
        assertThat(RaiseNothing.raiseNothing(123).toString(), is("throws nothing <is <123>>"));
        assertThat(RaiseNothing.raiseNothing(not("A")).toString(), is("throws nothing <not \"A\">"));
        assertThat(RaiseNothing.raiseNothing(nullValue()).toString(), is("throws nothing <null>"));
    }
}
