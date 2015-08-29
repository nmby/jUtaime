package xyz.hotchpotch.jutaime.serializable.experimental;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class TestUtil {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    /**
     * オブジェクトをシリアル化することによって得られるバイト配列を返します。<br>
     * 
     * @param obj シリアル化対象のオブジェクト（{@code null} が許容されます）
     * @return {@code obj} をシリアル化することによって得られるバイト配列
     * @throws FailToSerializeException シリアル化の過程で何らかの例外が発生した場合
     */
    public static byte[] write(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                
            oos.writeObject(obj);
            oos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * バイト配列をデシリアル化することによって得られるオブジェクトを返します。<br>
     * 
     * @param bytes バイト配列
     * @return バイト配列をデシリアル化することにより得られるオブジェクト
     * @throws FailToDeserializeException デシリアル化の過程で何らかの例外が発生した場合
     */
    public static Object read(byte[] bytes) {
        Objects.requireNonNull(bytes);
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
                
            return ois.readObject();
            
        } catch (Exception e) {
            throw new FailToDeserializeException(e);
        }
    }
    
    /**
     * オブジェクトをバイト配列にシリアル化したのちデシリアル化することによって得られるオブジェクトを返します。<br>
     * 
     * @param obj シリアル化対象のオブジェクト
     * @return {@code obj} をバイト配列にシリアル化したのちデシリアル化することによって得られるオブジェクト
     * @see #write(Object)
     * @see #read(byte[])
     */
    @SuppressWarnings("unchecked")
    public static <T> T writeAndRead(T obj) {
        return (T) writeModifyAndRead(obj, Function.identity());
    }
    
    /**
     * オブジェクトをシリアル化することによって得られるバイト配列を改竄したのちデシリアル化することによって得られるオブジェクトを返します。<br>
     * 
     * @param obj シリアル化対象のオブジェクト
     * @param modifier バイト配列を改竄する {@code Function}
     * @return 改竄されたバイト配列をデシリアル化することによって得られるオブジェクト
     * @see #write(Object)
     * @see #read(byte[])
     */
    public static Object writeModifyAndRead(Object obj, Function<byte[], byte[]> modifier) {
        Objects.requireNonNull(modifier);
        
        // write
        byte[] bytes = write(obj);
        
        // modify
        byte[] bytes2 = modifier.apply(bytes);
        
        // and read
        return read(bytes2);
    }
    
    /**
     * 文字列を修正 UTF-8 形式でシリアル化して得られるバイト配列を返します。<br>
     * 元の文字列と得られるバイト配列の例を示します。
     * <ul>
     *   <li>{@code ""} ： {@code 00 00}</li>
     *   <li>{@code "A"} ： {@code 00 01 41}</li>
     *   <li>{@code "a"} ： {@code 00 01 61}</li>
     *   <li>{@code "abc"} ： {@code 00 03 61 62 63}</li>
     *   <li>{@code "123"} ： {@code 00 03 31 32 33}</li>
     *   <li>{@code "あ"} ： {@code 00 03 e3 81 82}</li>
     *   <li>{@code "あいう"} ： {@code 00 09 e3 81 82 e3 81 84 e3 81 86}</li>
     * </ul>
     * 
     * @param str 任意の文字列
     * @return {@code str} のプリミティブ・データを修正 UTF-8 形式でシリアル化して得られるバイト配列
     * @throws NullPointerException {@code str} が {@code null} の場合
     * @see DataOutputStream#writeUTF(String)
     */
    public static byte[] bytes(String str) {
        Objects.requireNonNull(str);
        
        return bytes(dos -> {
            try {
                dos.writeUTF(str);
            } catch (Exception e) {
                throw new FailToSerializeException(e);
            }
        });
    }
    
    /**
     * int 値をシリアル化して得られるバイト配列を返します。<br>
     * 例えば、{@code 1} には {@code 00 00 00 01} を、{@code -1} には {@code ff ff ff ff} を返します。<br>
     * 
     * @param n 任意の int 値
     * @return int 値をシリアル化して得られるバイト配列
     */
    public static byte[] bytes(int n) {
        return bytes(dos -> {
            try {
                dos.writeInt(n);
            } catch (Exception e) {
                throw new FailToSerializeException(e);
            }
        });
    }
    
    /**
     * long 値をシリアル化して得られるバイト配列を返します。<br>
     * 例えば、{@code 1L} には {@code 00 00 00 00 00 00 00 01} を、
     * {@code -1L} には {@code ff ff ff ff ff ff ff ff} を返します。<br>
     * 
     * @param n 任意の long 値
     * @return long 値をシリアル化して得られるバイト配列
     */
    public static byte[] bytes(long n) {
        return bytes(dos -> {
            try {
                dos.writeLong(n);
            } catch (Exception e) {
                throw new FailToSerializeException(e);
            }
        });
    }
    
    private static byte[] bytes(Consumer<DataOutputStream> writeAction) {
        assert writeAction != null;
        
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            writeAction.accept(dos);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * バイト配列内の {@code target} と一致する部分配列を、{@code replacement} と一致する部分配列で置換した新たな配列を返します。
     * 元の配列は変更しません。<br>
     * 置き換えは、バイト配列の先頭から末尾まで進みます。<br>
     * 例1 ：
     * <ul>
     *   <li>{@code bytes} ： <code>{ 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 }</code></li>
     *   <li>{@code target} ： <code>{ 0x01, 0x01 }</code></li>
     *   <li>{@code replacement} ： <code>{ 0x01 }</code></li>
     *   <li>結果 ： <code>{ 0x01, 0x01, 0x01 }</code></li>
     * </ul>
     * 例2 ：
     * <ul>
     *   <li>{@code bytes} ： <code>{ 0x01, 0x01, 0x01 }</code></li>
     *   <li>{@code target} ： <code>{ 0x01, 0x01 }</code></li>
     *   <li>{@code replacement} ： <code>{ 0x02 }</code></li>
     *   <li>結果 ： <code>{ 0x02, 0x01 }</code> （<code>{ 0x01, 0x02 }</code> ではない）</li>
     * </ul>
     * {@code target} が長さ 0 の配列の場合、何も置換を行わず、{@code bytes} のコピーを返します。<br>
     * 
     * @param bytes 置換前のバイト配列
     * @param target 置換対象の部分配列
     * @param replacement 置換後の部分配列
     * @return 置換後のバイト配列
     * @throws NullPointerException {@code bytes}、{@code target}、{@code replacement} のいずれかが {@code null} の場合
     */
    public static byte[] replace(byte[] bytes, byte[] target, byte[] replacement) {
        Objects.requireNonNull(bytes);
        Objects.requireNonNull(target);
        Objects.requireNonNull(replacement);
        
        return replace(bytes, toHexString(target), toHexString(replacement));
    }
    
    /**
     * バイト配列内の {@code target} で表される部分配列を、{@code replacement} で表される部分配列で置換した新たな配列を返します。
     * 元の配列は変更しません。<br>
     * 置き換えは、バイト配列の先頭から末尾まで進みます。<br>
     * 例1 ：
     * <ul>
     *   <li>{@code bytes} ： <code>{ 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 }</code></li>
     *   <li>{@code target} ： {@code "01 01"}</li>
     *   <li>{@code replacement} ： {@code "01"}</li>
     *   <li>結果 ： <code>{ 0x01, 0x01, 0x01 }</code></li>
     * </ul>
     * 例2 ：
     * <ul>
     *   <li>{@code bytes} ： <code>{ 0x01, 0x01, 0x01 }</code></li>
     *   <li>{@code target} ： {@code "01 01"}</li>
     *   <li>{@code replacement} ： {@code "02"}</li>
     *   <li>結果 ： <code>{ 0x02, 0x01 }</code> （<code>{ 0x01, 0x02 }</code> ではない）</li>
     * </ul>
     * {@code target} が空文字列の場合、何も置換を行わず、{@code bytes} のコピーを返します。<br>
     * 
     * @param bytes 置換前のバイト配列
     * @param target 置換対象の部分配列を表す16進表示形式の文字列
     * @param replacement 置換後の部分配列を表す16進表示形式の文字列
     * @return 置換後のバイト配列
     * @throws NullPointerException {@code bytes} が {@code null} の場合
     * @throws NumberFormatException {@code target}、{@code replacement} のいずれかが16進表示形式でない場合
     * @see String#replace(CharSequence, CharSequence)
     */
    public static byte[] replace(byte[] bytes, String target, String replacement) {
        Objects.requireNonNull(bytes);
        if (!isHexFormat(target) || !isHexFormat(replacement)) {
            throw new NumberFormatException(String.format("target : %s, replacement : %s", target, replacement));
        }
        if ("".equals(target)) {
            return Arrays.copyOf(bytes, bytes.length);
        }
        
        String hexStr = toHexString(bytes);
        String hexStr2 = normalize(hexStr.replace(target, replacement));
        
        return hexToBytes(hexStr2);
    }
    
    private static String normalize(String hexStr) {
        return hexStr.trim().replaceAll("[ ]{2,}", " ");
    }
    
    /**
     * バイト配列を16進表示形式の文字列に変換します。<br>
     * 
     * @param bytes バイト配列
     * @return 16進表示形式の文字列（{@code bytes} が長さ 0 の配列の場合は空文字列）
     * @throws NullPointerException {@code bytes} が {@code null} の場合
     */
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
    
    /**
     * 16進表示形式の文字列をバイト配列に変換します。<br>
     * 
     * @param hexStr 16進表示形式の文字列
     * @return バイト配列（{@code hexStr} が空文字列の場合は長さ 0 の配列）
     * @throws NumberFormatException {@code hexStr} が16進表示形式ではない場合
     */
    public static byte[] hexToBytes(String hexStr) {
        if (!isHexFormat(hexStr)) {
            throw new NumberFormatException(hexStr);
        }
        if ("".equals(hexStr)) {
            return new byte[] {};
        }
        
        // byte と java.util.stream.Stream の相性が非常に悪いので、ループで処理することにする。
        String[] hexes = hexStr.split(" ");
        byte[] bytes = new byte[hexes.length];
        for (int i = 0; i < hexes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexes[i], 16);
        }
        
        return bytes;
    }
    
    /**
     * 文字列が16進表示形式かを返します。<br>
     * ここでいう16進表示形式とは、{@code "00"}～{@code "ff"} の2文字がスペース区切りで連結された形式のことを指します。<br>
     * 次の文字列は16進表示形式です。
     * <ul>
     *   <li>{@code "00"}</li>
     *   <li>{@code "01 2a b3 ff"}</li>
     *   <li>{@code ""}</li>
     * </ul>
     * 次の文字列は16進表示形式ではありません。
     * <ul>
     *   <li>{@code "00 1 23"} ： 必ず2文字のペアでなければなりません。</li>
     *   <li>{@code "12 "}、{@code " 34"} ： 先頭や末尾に余分なスペースが含まれてはなりません。</li>
     *   <li>{@code "AB FF"} ： {@code 0}～{@code 9}、{@code a}～{@code f} のみが許容されます。大文字は許容されません。</li>
     *   <li>{@code null}</li>
     * </ul>
     * 
     * @param hexStr 検査対象の文字列
     * @return {@code hexStr} が16進表示形式の場合は {@code true}
     *         （{@code null} の場合は {@code false}、空文字列の場合は {@code true}）
     */
    static boolean isHexFormat(String hexStr) {
        return hexStr != null && hexStr.matches("^$|^[0-9a-f]{2}( [0-9a-f]{2})*$");
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private TestUtil() {
    }
}
