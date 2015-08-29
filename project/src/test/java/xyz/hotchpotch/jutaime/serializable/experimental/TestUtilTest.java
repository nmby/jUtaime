package xyz.hotchpotch.jutaime.serializable.experimental;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static xyz.hotchpotch.jutaime.serializable.experimental.TestUtil.*;
import static xyz.hotchpotch.jutaime.throwable.RaiseMatchers.*;
import static xyz.hotchpotch.jutaime.throwable.Testee.*;

import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

public class TestUtilTest {
    
    private static enum TestEnum {
        ONE,
        TWO,
        THREE;
    }
    
    private static class Writable implements Serializable {
        private static final long serialVersionUID = 1L;
    }
    
    private static class Writable2 implements Serializable {
        private static final long serialVersionUID = 1L;
    }
    
    private static class Writable3 implements Serializable {
        private static final long serialVersionUID = 2L;
    }
    
    private static class NotWritable {
    }
    
    @Test
    public void testWrite() {
        assertThat(toHexString(write(null)), is("ac ed 00 05 70"));
        
        assertThat(write(Integer.valueOf(1)), instanceOf(byte[].class));
        assertThat(write(TestEnum.TWO), instanceOf(byte[].class));
        assertThat(write(new boolean[] { true, false }), instanceOf(byte[].class));
        assertThat(write("Hello, World !!"), instanceOf(byte[].class));
        
        assertThat(write(new Writable()), instanceOf(byte[].class));
        assertThat(of(() -> write(new NotWritable())),
                raise(FailToSerializeException.class).rootCause(NotSerializableException.class));
    }
    
    @Test
    public void testRead() {
        assertThat(read(hexToBytes("ac ed 00 05 70")), nullValue());
        
        assertThat(read(write(Integer.valueOf(1))), is(Integer.valueOf(1)));
        assertThat(read(write(TestEnum.TWO)), sameInstance(TestEnum.TWO));
        assertThat(read(write(new boolean[] { true, false })), is(new boolean[] { true, false }));
        assertThat(read(write("Hello, World !!")), is("Hello, World !!"));
        assertThat(read(write(new Writable())), instanceOf(Writable.class));
        
        assertThat(of(() -> read(null)), raise(NullPointerException.class));
        assertThat(of(() -> read(new byte[] {})), raise(FailToDeserializeException.class));
        assertThat(of(() -> read(new byte[] { 1, 2, 3 })), raise(FailToDeserializeException.class));
    }
    
    @Test
    public void testWriteAndRead() {
        assertThat(writeAndRead(null), nullValue());
        
        assertThat(writeAndRead(Integer.valueOf(1)), is(Integer.valueOf(1)));
        assertThat(writeAndRead(TestEnum.TWO), sameInstance(TestEnum.TWO));
        assertThat(writeAndRead(new boolean[] { true, false }), is(new boolean[] { true, false }));
        assertThat(writeAndRead("Hello, World !!"), is("Hello, World !!"));
        assertThat(writeAndRead(new Writable()), instanceOf(Writable.class));
    }
    
    @Test
    public void testWriteModifyAndRead() {
        Function<byte[], byte[]> modifier1 = bytes -> {
            byte[] bytes2 = Arrays.copyOf(bytes, bytes.length);
            bytes2[bytes2.length - 1] = 0x02;
            return bytes2;
        };
        assertThat(writeModifyAndRead(Integer.valueOf(1), modifier1), is(Integer.valueOf(2)));
        
        Function<byte[], byte[]> modifier2 = bytes -> {
            return replace(bytes, bytes(Writable.class.getName()), bytes(Writable2.class.getName()));
        };;
        assertThat(writeModifyAndRead(new Writable(), modifier2), instanceOf(Writable2.class));
        
        Function<byte[], byte[]> modifier3 = bytes -> {
            return replace(bytes, bytes(Writable.class.getName()), bytes(Writable3.class.getName()));
        };;
        assertThat(of(() -> writeModifyAndRead(new Writable(), modifier3)),
                raise(FailToDeserializeException.class).rootCause(InvalidClassException.class));
        
        Function<byte[], byte[]> modifier4 = bytes -> {
            byte[] bytes2 = replace(bytes, bytes(Writable.class.getName()), bytes(Writable3.class.getName()));
            return replace(bytes2, bytes(1L), bytes(2L));
        };;
        assertThat(writeModifyAndRead(new Writable(), modifier4), instanceOf(Writable3.class));
        
        Function<byte[], byte[]> modifier5 = bytes -> {
            return replace(bytes, bytes(Writable.class.getName()), bytes(NotWritable.class.getName()));
        };
        assertThat(of(() -> writeModifyAndRead(new Writable(), modifier5)),
                raise(FailToDeserializeException.class).rootCause(InvalidClassException.class));
        
        assertThat(of(() -> writeModifyAndRead(Integer.valueOf(1), null)), raise(NullPointerException.class));
    }
    
    @Test
    public void testBytesString() {
        assertThat(bytes(""), is(new byte[] { 0x00, 0x00 }));
        assertThat(bytes("A"), is(new byte[] { 0x00, 0x01, 0x41 }));
        assertThat(bytes("a"), is(new byte[] { 0x00, 0x01, 0x61 }));
        assertThat(bytes("abc"), is(new byte[] { 0x00, 0x03, 0x61, 0x62, 0x63 }));
        assertThat(bytes("123"), is(new byte[] { 0x00, 0x03, 0x31, 0x32, 0x33 }));
        assertThat(bytes("あ"), is(new byte[] { 0x00, 0x03, (byte) 0xe3, (byte) 0x81, (byte) 0x82 }));
        assertThat(bytes("あいう"), is(new byte[] { 0x00, 0x09,
                (byte) 0xe3, (byte) 0x81, (byte) 0x82,
                (byte) 0xe3, (byte) 0x81, (byte) 0x84,
                (byte) 0xe3, (byte) 0x81, (byte) 0x86 }));
                
        assertThat(of(() -> bytes(null)), raise(NullPointerException.class));
    }
    
    @Test
    public void testBytesInt() {
        assertThat(bytes(0), is(new byte[] { 0, 0, 0, 0 }));
        assertThat(bytes(1), is(new byte[] { 0, 0, 0, 1 }));
        assertThat(bytes(2), is(new byte[] { 0, 0, 0, 2 }));
        assertThat(bytes(-1), is(new byte[] { -1, -1, -1, -1 }));
        assertThat(bytes(-2), is(new byte[] { -1, -1, -1, -2 }));
    }
    
    @Test
    public void testBytesLong() {
        assertThat(bytes(0L), is(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }));
        assertThat(bytes(1L), is(new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 }));
        assertThat(bytes(2L), is(new byte[] { 0, 0, 0, 0, 0, 0, 0, 2 }));
        assertThat(bytes(-1L), is(new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 }));
        assertThat(bytes(-2L), is(new byte[] { -1, -1, -1, -1, -1, -1, -1, -2 }));
    }
    
    @Test
    public void testReplace1() {
        final byte[] emptyArr = {};
        assertThat(of(() -> replace(null, emptyArr, emptyArr)), raise(NullPointerException.class));
        assertThat(of(() -> replace(emptyArr, null, emptyArr)), raise(NullPointerException.class));
        assertThat(of(() -> replace(emptyArr, emptyArr, null)), raise(NullPointerException.class));
        
        final byte[] arr00 = { 0 };
        final byte[] arrff = { -1 };
        assertThat(replace(emptyArr, emptyArr, emptyArr), is(emptyArr));
        assertThat(replace(emptyArr, arr00, arrff), is(emptyArr));
        
        final byte[] testArr1 = { 0, 1, 2, 0, 1, 2 };
        assertThat(replace(testArr1, emptyArr, arrff), is(testArr1));
        assertThat(replace(testArr1, emptyArr, arrff), not(sameInstance(testArr1)));
        
        assertThat(replace(testArr1, arr00, arrff), is(new byte[] { -1, 1, 2, -1, 1, 2 }));
        assertThat(replace(testArr1, new byte[] { 1, 2 }, new byte[] { 0x0a, 0x0b }), is(new byte[] { 0, 10, 11, 0, 10, 11 }));
        assertThat(replace(testArr1, new byte[] { 1 }, new byte[] { 0x0a, 0x0b }), is(new byte[] { 0, 10, 11, 2, 0, 10, 11, 2 }));
        assertThat(replace(testArr1, new byte[] { 0, 1 }, arrff), is(new byte[] { -1, 2, -1, 2 }));
        assertThat(replace(testArr1, new byte[] { 0, 1, 2 }, emptyArr), is(emptyArr));
        
        final byte[] testArr2 = { 1, 1, 2, 2, 3, 3 };
        assertThat(replace(testArr2, new byte[] { 1 }, emptyArr), is(new byte[] { 2, 2, 3, 3 }));
        assertThat(replace(testArr2, new byte[] { 2 }, emptyArr), is(new byte[] { 1, 1, 3, 3 }));
        assertThat(replace(testArr2, new byte[] { 3 }, emptyArr), is(new byte[] { 1, 1, 2, 2 }));
    }
    
    @Test
    public void testReplace2() {
        final byte[] emptyArr = {};
        assertThat(of(() -> replace(null, "", "")), raise(NullPointerException.class));
        assertThat(of(() -> replace(emptyArr, null, "")), raise(NumberFormatException.class));
        assertThat(of(() -> replace(emptyArr, "", null)), raise(NumberFormatException.class));
        
        assertThat(replace(emptyArr, "", ""), is(emptyArr));
        assertThat(replace(emptyArr, "00", "ff"), is(emptyArr));
        
        final byte[] testArr1 = { 0, 1, 2, 0, 1, 2 };
        assertThat(replace(testArr1, "", "ff"), is(testArr1));
        assertThat(replace(testArr1, "", "ff"), not(sameInstance(testArr1)));
        
        assertThat(replace(testArr1, "00", "ff"), is(new byte[] { -1, 1, 2, -1, 1, 2 }));
        assertThat(replace(testArr1, "01 02", "0a 0b"), is(new byte[] { 0, 10, 11, 0, 10, 11 }));
        assertThat(replace(testArr1, "01", "0a 0b"), is(new byte[] { 0, 10, 11, 2, 0, 10, 11, 2 }));
        assertThat(replace(testArr1, "00 01", "ff"), is(new byte[] { -1, 2, -1, 2 }));
        assertThat(replace(testArr1, "00 01 02", ""), is(emptyArr));
        
        final byte[] testArr2 = { 1, 1, 2, 2, 3, 3 };
        assertThat(replace(testArr2, "01", ""), is(new byte[] { 2, 2, 3, 3 }));
        assertThat(replace(testArr2, "02", ""), is(new byte[] { 1, 1, 3, 3 }));
        assertThat(replace(testArr2, "03", ""), is(new byte[] { 1, 1, 2, 2 }));
    }
    
    @Test
    public void testToHexString() {
        assertThat(toHexString(new byte[] {}), is(""));
        assertThat(toHexString(new byte[] { 0 }), is("00"));
        assertThat(toHexString(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }), is("01 02 03 04 05 06 07 08"));
        assertThat(toHexString(new byte[] { 9, 10, 11, 12, 13, 14, 15, 16 }), is("09 0a 0b 0c 0d 0e 0f 10"));
        assertThat(toHexString(new byte[] { 64, (byte) 128, (byte) 144, (byte) 160, (byte) 176 }), is("40 80 90 a0 b0"));
        assertThat(toHexString(new byte[] { (byte) 192, (byte) 208, (byte) 224, (byte) 240 }), is("c0 d0 e0 f0"));
        assertThat(toHexString(new byte[] { -1, (byte) 255, -2, (byte) 254 }), is("ff ff fe fe"));
        
        assertThat(of(() -> toHexString(null)), raise(NullPointerException.class));
    }
    
    @Test
    public void testHexToBytes() {
        assertThat(hexToBytes(""), is(new byte[] {}));
        assertThat(hexToBytes("00"), is(new byte[] { 0 }));
        assertThat(hexToBytes("01 02 03 04 05 06 07 08"), is(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }));
        assertThat(hexToBytes("09 0a 0b 0c 0d 0e 0f 10"), is(new byte[] { 9, 10, 11, 12, 13, 14, 15, 16 }));
        assertThat(hexToBytes("40 80 90 a0 b0"), is(new byte[] { 64, (byte) 128, (byte) 144, (byte) 160, (byte) 176 }));
        assertThat(hexToBytes("c0 d0 e0 f0"), is(new byte[] { (byte) 192, (byte) 208, (byte) 224, (byte) 240 }));
        assertThat(hexToBytes("ff fe"), is(new byte[] { -1, -2 }));
        
        assertThat(of(() -> hexToBytes(null)), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes("0")), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes("Hello, World !!")), raise(NumberFormatException.class));
    }
    
    @Test
    public void testIsHexFormat() {
        assertThat(isHexFormat(null), is(false));
        assertThat(isHexFormat(""), is(true));
        
        assertThat(isHexFormat("00"), is(true));
        assertThat(isHexFormat("ff"), is(true));
        assertThat(isHexFormat("1e"), is(true));
        assertThat(isHexFormat("d2"), is(true));
        assertThat(isHexFormat("01 23 45 67 89 ab cd ef"), is(true));
        
        assertThat(isHexFormat("0"), is(false));
        assertThat(isHexFormat(" 00 00 00"), is(false));
        assertThat(isHexFormat("00 00 00 "), is(false));
        assertThat(isHexFormat("00 0 00"), is(false));
        assertThat(isHexFormat("00  00 00"), is(false));
        assertThat(isHexFormat("00 0000"), is(false));
        
        assertThat(isHexFormat("-1"), is(false));
        assertThat(isHexFormat("0A"), is(false));
        assertThat(isHexFormat("F0"), is(false));
        
        assertThat(isHexFormat("Hello," + System.lineSeparator() + "World !!"), is(false));
        assertThat(isHexFormat("憤怒"), is(false));
    }
}
