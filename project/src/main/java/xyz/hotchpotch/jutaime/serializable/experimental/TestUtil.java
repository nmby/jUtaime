package xyz.hotchpotch.jutaime.serializable.experimental;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class TestUtil {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    public static byte[] write(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                
            oos.writeObject(obj);
            oos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (IOException e) {
            throw new FailToSerializeException(e);
        }
    }
    
    public static Object read(byte[] bytes) {
        Objects.requireNonNull(bytes);
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
                
            return ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            throw new FailToDeserializeException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T writeAndRead(T obj) {
        byte[] bytes = write(obj);
        return (T) read(bytes);
    }
    
    public static String toHexString(byte[] bytes) {
        Objects.requireNonNull(bytes);
        StringBuilder str = new StringBuilder();
        
        for (byte b : bytes) {
            str.append(String.format(" %02x", b));
        }
        if (0 < bytes.length) {
            str.deleteCharAt(0);
        }
        
        return str.toString();
    }
    
    public static byte[] hexToBytes(String hexStr) {
        Objects.requireNonNull(hexStr);
        
        if ("".equals(hexStr)) {
            return new byte[] {};
        }
        if (!hexStr.matches("[0-9a-f]{2}( [0-9a-f]{2})*")) {
            throw new NumberFormatException("hexStr : " + hexStr);
        }
        
        // byte と java.util.stream.Stream の相性が非常に悪いので、ループで処理することにする。
        String[] hexes = hexStr.split(" ");
        byte[] bytes = new byte[hexes.length];
        for (int i = 0; i < hexes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexes[i], 16);
        }
        
        return bytes;
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private TestUtil() {
    }
}
