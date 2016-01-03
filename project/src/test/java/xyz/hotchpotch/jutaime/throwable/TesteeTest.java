package xyz.hotchpotch.jutaime.throwable;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.junit.Test;

public class TesteeTest {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private static final String MSG_COMPLETED_SAFELY = "Completed safely.";
    private static final String MSG_NOT_TESTED = "I haven't yet been tested.";
    
    private static final Object testObj = new HashMap<>();
    private static enum TestEnum { ONE, TWO, THREE; }
    private static final boolean[] testBooleanArray = { true, false, true };
    private static final byte[] testByteArray = { 1, 2, 3 };
    private static final char[] testCharArray = { 'a', 'b', 'c' };
    private static final double[] testDoubleArray = { 1d, 2d, 3d };
    private static final float[] testFloatArray = { 1f, 2f, 3f };
    private static final int[] testIntArray = { 1, 2, 3 };
    private static final long[] testLongArray = { 1L, 2L, 3L };
    private static final short[] testShortArray = { 1, 2, 3 };
    private static final Void[] testVoidArray = { null, null, null };
    private static final Object[] testObjectArray = { testObj, 1, "a", TestEnum.ONE, testIntArray, null };
    private static final String[] testStringArray = { "a", "b", "c", "", null };
    private static final Enum<?>[] testEnumArray = { TestEnum.ONE, TestEnum.TWO, TestEnum.THREE, null };
    private static final Map<?, ?>[] testInterfaceArray = { new HashMap<>(), new EnumMap<>(TestEnum.class), new TreeMap<>(), null };
    private static final Map<?, ?>[] testNullArray = { null, null, null };
    private static final Map<?, ?>[] testEmptyArray = {};
    private static final Object[] testNestedArray = { testIntArray, testStringArray, testNullArray };
    
    private static class Nabeatsu {
        private final String numStr;
        
        private Nabeatsu(int num) {
            numStr = String.valueOf(num);
        }
        
        @Override
        public String toString() {
            if (numStr.contains("3")) {
                throw new RuntimeException(numStr);
            } else {
                return numStr;
            }
        }
    }
    
    private static final Object[] testNabeatsuArray123 = { new Nabeatsu(1), new Nabeatsu(2), new Nabeatsu(3)};
    private static final Object[] testNabeatsuArray246 = { new Nabeatsu(2), new Nabeatsu(4), new Nabeatsu(6)};
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    @Test
    public void testOfUnsafeCallable1() {
        assertThat(Testee.of(() -> null), instanceOf(Testee.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void testOfUnsafeCallable2() {
        Testee.of((UnsafeCallable<?>) null);
    }
    
    @Test
    public void testOfUnsafeRunnable1() {
        assertThat(Testee.of(() -> {}), instanceOf(Testee.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void testOfUnsafeRunnable2() {
        Testee.of((UnsafeRunnable) null);
    }
    
    @Test
    public void testCall1() throws Throwable {
        // 様々なオペレーションを実行できることの確認
        assertThat(Testee.of(() -> {}).call(), is(MSG_COMPLETED_SAFELY));
        assertThat(Testee.of(() -> 1 + 2).call(), is(3));
        
        Date date = new Date();
        assertThat(Testee.of(() -> date.toString()).call(), is(date.toString()));
    }
    
    @Test(expected = Throwable.class)
    public void testCall2() throws Throwable {
        // 各種例外をスローできることの確認 - チェック例外である Throwable
        Testee.of(() -> { throw new Throwable(); }).call();
    }
    
    @Test(expected = Exception.class)
    public void testCall3() throws Throwable {
        // 各種例外をスローできることの確認 - チェック例外である Exception
        Testee.of(() -> { throw new Exception(); }).call();
    }
    
    @Test(expected = RuntimeException.class)
    public void testCall4() throws Throwable {
        // 各種例外をスローできることの確認 - 非チェック例外である RuntimeException
        Testee.of(() -> { throw new RuntimeException(); }).call();
    }
    
    @Test(expected = Error.class)
    public void testCall5() throws Throwable {
        // 各種例外をスローできることの確認 - 非チェック例外である Error
        Testee.of(() -> { throw new Error(); }).call();
    }
    
    private Testee testee1 = null;
    
    @Test
    public void testCall6() throws Throwable {
        try {
            // こんな妙なことをしても、安全に終了することはする。
            testee1 = Testee.of(() -> testee1.call());
            testee1.call();
        } catch (Throwable t) {
            fail();
        }
    }
    
    @Test
    public void testCall7() throws Throwable {
        // 1回目の結果がキャプチャされることの確認
        Testee testee = Testee.of(() -> Math.random());
        assertThat(testee.call(), is(testee.call()));
        
        testee = Testee.of(() -> { throw new Exception(String.valueOf(Math.random())); });
        Throwable thrown1 = null;
        Throwable thrown2 = null;
        try {
            testee.call();
        } catch (Exception e) {
            thrown1 = e;
        }
        try {
            testee.call();
        } catch (Exception e) {
            thrown2 = e;
        }
        assertThat(thrown1, sameInstance(thrown2));
        assertThat(thrown1.getMessage(), is(thrown2.getMessage()));
    }
    
    @Test
    public void testToString1() {
        // 未実行の場合
        assertThat(Testee.of(() -> {}).toString(), is(MSG_NOT_TESTED));
        assertThat(Testee.of(() -> 12).toString(), is(MSG_NOT_TESTED));
    }
    
    private void innerTestToString2(Testee testee, String expected) throws Throwable {
        testee.call();
        assertThat(testee.toString(), is(expected));
    }
    
    @Test
    public void testToString2() throws Throwable {
        // 実行済み - 正常終了の場合（配列以外）
        innerTestToString2(Testee.of(() -> {}), MSG_COMPLETED_SAFELY);
        innerTestToString2(Testee.of(() -> null), "null");
        innerTestToString2(Testee.of(() -> 1 + 2), "3");
        innerTestToString2(Testee.of(() -> "testString"), "testString");
        innerTestToString2(Testee.of(() -> testObj), "{}");
        innerTestToString2(Testee.of(() -> TestEnum.TWO), "TWO");
    }
    
    @Test
    public void testToString3() throws Throwable {
        // 実行済み - 正常終了の場合（配列）
        innerTestToString2(Testee.of(() -> testBooleanArray), "[true, false, true]");
        innerTestToString2(Testee.of(() -> testByteArray), "[1, 2, 3]");
        innerTestToString2(Testee.of(() -> testCharArray), "[a, b, c]");
        innerTestToString2(Testee.of(() -> testDoubleArray), "[1.0, 2.0, 3.0]");
        innerTestToString2(Testee.of(() -> testFloatArray), "[1.0, 2.0, 3.0]");
        innerTestToString2(Testee.of(() -> testIntArray), "[1, 2, 3]");
        innerTestToString2(Testee.of(() -> testLongArray), "[1, 2, 3]");
        innerTestToString2(Testee.of(() -> testShortArray), "[1, 2, 3]");
        innerTestToString2(Testee.of(() -> testVoidArray), "[null, null, null]");
        innerTestToString2(Testee.of(() -> testObjectArray), "[{}, 1, a, ONE, [1, 2, 3], null]");
        innerTestToString2(Testee.of(() -> testStringArray), "[a, b, c, , null]");
        innerTestToString2(Testee.of(() -> testEnumArray), "[ONE, TWO, THREE, null]");
        innerTestToString2(Testee.of(() -> testInterfaceArray), "[{}, {}, {}, null]");
        innerTestToString2(Testee.of(() -> testNullArray), "[null, null, null]");
        innerTestToString2(Testee.of(() -> testEmptyArray), "[]");
        innerTestToString2(Testee.of(() -> testNestedArray), "[[1, 2, 3], [a, b, c, , null], [null, null, null]]");
        innerTestToString2(Testee.of(() -> testNabeatsuArray123), Objects.toString(testNabeatsuArray123));
        innerTestToString2(Testee.of(() -> testNabeatsuArray246), "[2, 4, 6]");
    }
    
    private void innerTestToString4(Testee testee, String expected) {
        try {
            testee.call();
            fail();
        } catch (Throwable t) {
            assertThat(testee.toString(), is(expected));
        }
    }
    
    @Test
    public void testToString4() throws Throwable {
        // 実行済み - 例外スロー（cause なし）
        innerTestToString4(Testee.of(() -> { throw new Throwable(); }), "throw java.lang.Throwable (null)");
        innerTestToString4(Testee.of(() -> { throw new Error((String) null); }), "throw java.lang.Error (null)");
        innerTestToString4(Testee.of(() -> { throw new Exception(""); }), "throw java.lang.Exception ()");
        innerTestToString4(Testee.of(() -> { throw new RuntimeException("test message"); }), "throw java.lang.RuntimeException (test message)");
        innerTestToString4(Testee.of(() -> Integer.valueOf("abc")), "throw java.lang.NumberFormatException (For input string: \"abc\")");
    }
    
    @Test
    public void testToString5() throws Throwable {
        // 実行済み - 例外スロー（cause あり）
        innerTestToString4(
                Testee.of(() -> { throw new Error(""); }),
                "throw java.lang.Error ()");
        innerTestToString4(
                Testee.of(() -> { throw new Error("abc", new Exception()); }),
                "throw java.lang.Error (abc): java.lang.Exception (null)");
        innerTestToString4(
                Testee.of(() -> { throw new Error("", new Exception("abc", new Throwable())); }),
                "throw java.lang.Error (): java.lang.Exception (abc): java.lang.Throwable (null)");
        innerTestToString4(
                Testee.of(() -> { throw new Error(null, new Exception("", new Throwable("abc", new NullPointerException("def")))); }),
                "throw java.lang.Error (null): java.lang.Exception (): java.lang.Throwable (abc): java.lang.NullPointerException (def)");
    }
    
    @Test
    public void testToString6() throws Throwable {
        // 実行済み - 例外スロー（cause あり） - 1 -> 2 -> 1... のループ状の場合
        Throwable t1 = new Error("1");
        Throwable t2 = new IOException("2");
        t1.initCause(t2);
        t2.initCause(t1);
        innerTestToString4(
                Testee.of(() -> { throw t1; }),
                "throw java.lang.Error (1): java.io.IOException (2): java.lang.Error (1): ...");
    }
    
    @Test
    public void testToString7() throws Throwable {
        // 実行済み - 例外スロー（cause あり） - 1 -> 2 -> 3 -> 1... のループ状の場合
        Throwable t1 = new Error("1");
        Throwable t2 = new IOException("2");
        Throwable t3 = new Throwable("3");
        t1.initCause(t2);
        t2.initCause(t3);
        t3.initCause(t1);
        innerTestToString4(
                Testee.of(() -> { throw t1; }),
                "throw java.lang.Error (1): java.io.IOException (2): java.lang.Throwable (3): java.lang.Error (1): ...");
    }
    
    @Test
    public void testToString8() throws Throwable {
        // 実行済み - 例外スロー（cause あり） - 1 -> 2 -> 3 -> 2... のループ状の場合
        Throwable t1 = new Error("1");
        Throwable t2 = new IOException("2");
        Throwable t3 = new Throwable("3");
        t1.initCause(t2);
        t2.initCause(t3);
        t3.initCause(t2);
        innerTestToString4(
                Testee.of(() -> { throw t1; }),
                "throw java.lang.Error (1): java.io.IOException (2): java.lang.Throwable (3): java.io.IOException (2): ...");
    }
}
