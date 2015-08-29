package xyz.hotchpotch.jutaime.serializable.experimental;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static xyz.hotchpotch.jutaime.serializable.experimental.TestUtil.*;
import static xyz.hotchpotch.jutaime.throwable.RaiseMatchers.*;
import static xyz.hotchpotch.jutaime.throwable.Testee.*;

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
    
    private static final boolean[] testArr = { true, false };
    
    private static class Writable implements Serializable {
    }
    
    private static class NotWritable {
    }
    
    @Test
    public void testWrite() {
        assertThat(toHexString(write(null)), is("ac ed 00 05 70"));
        
        assertThat(write(Integer.valueOf(1)), instanceOf(byte[].class));
        assertThat(write(TestEnum.TWO), instanceOf(byte[].class));
        assertThat(write(testArr), instanceOf(byte[].class));
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
        assertThat(read(write(testArr)), is(testArr));
        assertThat(read(write("Hello, World !!")), is("Hello, World !!"));
        
        assertThat(of(() -> read(null)), raise(NullPointerException.class));
        assertThat(of(() -> read(new byte[] {})), raise(FailToDeserializeException.class));
        assertThat(of(() -> read(new byte[] { 1, 2, 3 })), raise(FailToDeserializeException.class));
    }
    
    @Test
    public void testWriteAndRead() {
        assertThat(writeAndRead(null), nullValue());
        
        assertThat(writeAndRead(Integer.valueOf(1)), is(Integer.valueOf(1)));
        assertThat(writeAndRead(TestEnum.TWO), sameInstance(TestEnum.TWO));
        assertThat(writeAndRead(testArr), is(testArr));
        assertThat(writeAndRead("Hello, World !!"), is("Hello, World !!"));
    }
    
    @Test
    public void testWriteModifyAndRead() {
        Function<byte[], byte[]> bytesModifier = bytes -> {
            byte[] bytes2 = Arrays.copyOf(bytes, bytes.length);
            bytes2[bytes2.length - 1] = 0x02;
            return bytes2;
            };
        assertThat(writeModifyAndRead(Integer.valueOf(1), bytesModifier), is(Integer.valueOf(2)));
        
        assertThat(of(() -> writeModifyAndRead(Integer.valueOf(1), null)), raise(NullPointerException.class));
    }
    
    @Test
    public void testBytesModifier() {
        Function<String, String> stringModifier = hexStr -> {
            return hexStr.substring(0, hexStr.length() - 3 * 4) + " 00 00 00 02";
        };
        Function<byte[], byte[]> bytesModifier = bytesModifier(stringModifier);
        assertThat(writeModifyAndRead(Integer.valueOf(1), bytesModifier), is(Integer.valueOf(2)));
        
        assertThat(of(() -> bytesModifier(null)), raise(NullPointerException.class));
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
        
        assertThat(of(() -> hexToBytes(null)), raise(NullPointerException.class));
        assertThat(of(() -> hexToBytes("0")), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes("00 ")), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes(" 00")), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes("00 01 2 03")), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes("FF")), raise(NumberFormatException.class));
        assertThat(of(() -> hexToBytes("Hello, World !!")), raise(NumberFormatException.class));
    }
}
