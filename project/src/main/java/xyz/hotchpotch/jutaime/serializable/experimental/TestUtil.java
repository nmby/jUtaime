package xyz.hotchpotch.jutaime.serializable.experimental;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * JUnit4でのシリアライズ／デシリアライズに関するテストを効率化するための機能を提供するユーティリティクラスです。<br>
 * 
 * @deprecated このクラスは {@link xyz.hotchpotch.jutaime.serializable.STUtil} として正式リリースされました。<br>
 *             今後は {@link xyz.hotchpotch.jutaime.serializable.experimental} パッケージではなく
 *             {@link xyz.hotchpotch.jutaime.serializable} パッケージの各種クラスを利用してください。<br>
 *             このパッケージは将来のリリースで削除される予定です。<br>
 * @since 1.2.0
 * @author nmby
 */
@Deprecated
public class TestUtil {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    private static final byte[] OBJECT_HEADER = { (byte) 0xac, (byte) 0xed, 0x00, 0x05 };
    
    // **** オブジェクトのシリアライズ／デシリアライズに関するユーティリティ ****
    
    /**
     * オブジェクトをシリアライズすることによって得られるバイト配列を返します。<br>
     * 
     * @param obj シリアライズ対象のオブジェクト（{@code null} が許容されます）
     * @return {@code obj} をシリアライズすることによって得られるバイト配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see ObjectOutputStream#writeObject(Object)
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
     * バイト配列をデシリアライズすることによって得られるオブジェクトを返します。<br>
     * 
     * @param <T> 戻り値のオブジェクトの型
     * @param bytes バイト配列
     * @return バイト配列をデシリアライズすることにより得られるオブジェクト
     * @throws NullPointerException {@code bytes} が {@code null} の場合
     * @throws FailToDeserializeException デシリアライズの過程で何らかの例外が発生した場合
     * @see ObjectInputStream#readObject()
     */
    @SuppressWarnings("unchecked")
    public static <T> T read(byte[] bytes) {
        Objects.requireNonNull(bytes);
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
                
            return (T) ois.readObject();
            
        } catch (Exception e) {
            throw new FailToDeserializeException(e);
        }
    }
    
    /**
     * オブジェクトをバイト配列にシリアライズしたのちデシリアライズすることによって得られるオブジェクトを返します。<br>
     * 
     * @param <T> シリアライズ対象のオブジェクトの型
     * @param obj シリアライズ対象のオブジェクト（{@code null} が許容されます）
     * @return {@code obj} をバイト配列にシリアライズしたのちデシリアライズすることによって得られるオブジェクト
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @throws FailToDeserializeException デシリアライズの過程で何らかの例外が発生した場合
     * @see #write(Object)
     * @see #read(byte[])
     */
    public static <T> T writeAndRead(T obj) {
        return writeModifyAndRead(obj, Function.identity());
    }
    
    /**
     * オブジェクトをシリアライズすることによって得られるバイト配列を改竄したのちデシリアライズすることによって得られるオブジェクトを返します。<br>
     * 
     * @param <T> 戻り値のオブジェクトの型
     * @param obj シリアライズ対象のオブジェクト（{@code null} が許容されます）
     * @param modifier バイト配列を改竄する {@code Function}
     * @return 改竄されたバイト配列をデシリアライズすることによって得られるオブジェクト
     * @throws NullPointerException {@code modifier} が {@code null} の場合
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @throws FailToDeserializeException デシリアライズの過程で何らかの例外が発生した場合
     * @see #write(Object)
     * @see #read(byte[])
     */
    public static <T> T writeModifyAndRead(Object obj, Function<byte[], byte[]> modifier) {
        Objects.requireNonNull(modifier);
        
        byte[] bytes = write(obj);
        byte[] modified = modifier.apply(bytes);
        return read(modified);
    }
    
    // **** プリミティブデータ型とオブジェクトのシリアライズ形式取得に関するユーティリティ ****
    
    /**
     * boolean 値をシリアライズして得られるバイト配列を返します。<br>
     * 具体的には、{@code true} には <code>{ 0x01 }</code> を、
     * {@code false} には <code>{ 0x00 }</code> を返します。<br>
     * 
     * @param b 任意の boolean 値
     * @return boolean 値をシリアライズして得られるバイト配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see DataOutputStream#writeBoolean(boolean)
     */
    public static byte[] bytes(boolean b) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            dos.writeBoolean(b);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * int 値をシリアライズして得られるバイト配列を返します。<br>
     * 例えば、{@code 1} には <code>{ 0x00, 0x00, 0x00, 0x01 }</code> を、
     * {@code -1} には <code>{ 0xff, 0xff, 0xff, 0xff }</code> を返します。<br>
     * 
     * @param i 任意の int 値
     * @return int 値をシリアライズして得られるバイト配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see DataOutputStream#writeInt(int)
     */
    public static byte[] bytes(int i) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            dos.writeInt(i);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * long 値をシリアライズして得られるバイト配列を返します。<br>
     * 例えば、{@code 1L} には <code>{ 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 }</code> を、
     * {@code -1L} には <code>{ 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff }</code> を返します。<br>
     * 
     * @param l 任意の long 値
     * @return long 値をシリアライズして得られるバイト配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see DataOutputStream#writeLong(long)
     */
    public static byte[] bytes(long l) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            dos.writeLong(l);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * float 値をシリアライズして得られるバイト配列を返します。<br>
     * 
     * @param f 任意の float 値
     * @return float 値をシリアライズして得られるバイト配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see DataOutputStream#writeFloat(float)
     */
    public static byte[] bytes(float f) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            dos.writeFloat(f);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * double 値をシリアライズして得られるバイト配列を返します。<br>
     * 
     * @param d 任意の double 値
     * @return double 値をシリアライズして得られるバイト配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see DataOutputStream#writeDouble(double)
     */
    public static byte[] bytes(double d) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            dos.writeDouble(d);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * 文字列を修正 UTF-8 形式でシリアライズして得られるバイト配列を返します。<br>
     * 元の文字列と得られるバイト配列の例を示します。<br>
     * <br>
     * <table border="1">
     *   <caption>元の文字列と得られるバイト配列の例</caption>
     *   <tr><th>元の文字列</th><th>得られるバイト配列</th></tr>
     *   <tr><td>{@code ""}</td><td><code>{ 0x00, 0x00 }</code></td></tr>
     *   <tr><td>{@code "A"}</td><td><code>{ 0x00, 0x01, 0x41 }</code></td></tr>
     *   <tr><td>{@code "a"}</td><td><code>{ 0x00, 0x01, 0x61 }</code></td></tr>
     *   <tr><td>{@code "abc"}</td><td><code>{ 0x00, 0x03, 0x61, 0x62, 0x63 }</code></td></tr>
     *   <tr><td>{@code "123"}</td><td><code>{ 0x00, 0x03, 0x31, 0x32, 0x33 }</code></td></tr>
     *   <tr><td>{@code "あ"}</td><td><code>{ 0x00, 0x03, 0xe3, 0x81, 0x82 }</code></td></tr>
     *   <tr><td>{@code "あいう"}</td><td><code>{ 0x00, 0x09, 0xe3, 0x81, 0x82, 0xe3, 0x81, 0x84, 0xe3, 0x81, 0x86 }</code></td></tr>
     * </table>
     * 
     * @param str 任意の文字列
     * @return {@code str} のプリミティブ・データを修正 UTF-8 形式でシリアライズして得られるバイト配列
     * @throws NullPointerException {@code str} が {@code null} の場合
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     * @see DataOutputStream#writeUTF(String)
     */
    public static byte[] bytes(String str) {
        Objects.requireNonNull(str);
        
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos)) {
                
            dos.writeUTF(str);
            dos.flush(); // 要るのかよく分からないが、念のため実行する。
            return bos.toByteArray();
            
        } catch (Exception e) {
            throw new FailToSerializeException(e);
        }
    }
    
    /**
     * オブジェクトをシリアライズして得られるバイト配列から先頭の固定 4 バイトを除いた配列を返します。<br>
     * 
     * @param obj 任意のオブジェクト（{@code null} が許容されます）
     * @return オブジェクトをシリアライズして得られるバイト配列から先頭の固定 4 バイトを除いた配列
     * @throws FailToSerializeException シリアライズの過程で何らかの例外が発生した場合
     */
    public static byte[] bytes(Object obj) {
        byte[] bytes = write(obj);
        
        assert bytes != null;
        assert Arrays.equals(OBJECT_HEADER, Arrays.copyOf(bytes, OBJECT_HEADER.length));
        
        return Arrays.copyOfRange(bytes, OBJECT_HEADER.length, bytes.length);
    }
    
    // **** バイト配列の加工、およびバイト配列と16進表示形式文字列の変換に関するユーティリティ ****
    
    /**
     * バイト配列内の {@code target} と一致する部分配列を {@code replacement} で置換した新たな配列を返します。
     * 元の配列は変更しません。<br>
     * 置き換えは、バイト配列の先頭から末尾まで進みます。<br>
     * <br>
     * <table border="1">
     *   <caption>置換例１</caption>
     *   <tr><th>{@code bytes}</th><td><code>{ 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 }</code></td></tr>
     *   <tr><th>{@code target}</th><td><code>{ 0x01, 0x01 }</code></td></tr>
     *   <tr><th>{@code replacement}</th><td><code>{ 0x01 }</code></td></tr>
     *   <tr><th>結果</th><td><code>{ 0x01, 0x01, 0x01 }</code></td></tr>
     * </table>
     * <br>
     * <table border="1">
     *   <caption>置換例２</caption>
     *   <tr><th>{@code bytes}</th><td><code>{ 0x01, 0x01, 0x01 }</code></td></tr>
     *   <tr><th>{@code target}</th><td><code>{ 0x01, 0x01 }</code></td></tr>
     *   <tr><th>{@code replacement}</th><td><code>{ 0x02 }</code></td></tr>
     *   <tr><th>結果</th><td><code>{ 0x02, 0x01 }</code> （<code>{ 0x01, 0x02 }</code> ではない）</td></tr>
     * </table>
     * <br>
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
     * <br>
     * <table border="1">
     *   <caption>置換例１</caption>
     *   <tr><th>{@code bytes}</th><td><code>{ 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 }</code></td></tr>
     *   <tr><th>{@code target}</th><td>{@code "01 01"}</td></tr>
     *   <tr><th>{@code replacement}</th><td>{@code "01"}</td></tr>
     *   <tr><th>結果</th><td><code>{ 0x01, 0x01, 0x01 }</code></td></tr>
     * </table>
     * <br>
     * <table border="1">
     *   <caption>置換例２</caption>
     *   <tr><th>{@code bytes}</th><td><code>{ 0x01, 0x01, 0x01 }</code></td></tr>
     *   <tr><th>{@code target}</th><td>{@code "01 01"}</td></tr>
     *   <tr><th>{@code replacement}</th><td>{@code "02"}</td></tr>
     *   <tr><th>結果</th><td><code>{ 0x02, 0x01 }</code> （<code>{ 0x01, 0x02 }</code> ではない）</td></tr>
     * </table>
     * <br>
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
     * 2つのバイト配列を連結して得られる新しいバイト配列を返します。<br>
     * 
     * @param bytes1 バイト配列1
     * @param bytes2 バイト配列2
     * @return 2つのバイト配列を連結して得られる新しいバイト配列
     * @throws NullPointerException {@code bytes1}、{@code bytes2} のいずれかが {@code null} の場合
     */
    public static byte[] concat(byte[] bytes1, byte[] bytes2) {
        Objects.requireNonNull(bytes1);
        Objects.requireNonNull(bytes2);
        
        byte[] marged = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, marged, 0, bytes1.length);
        System.arraycopy(bytes2, 0, marged, bytes1.length, bytes2.length);
        return marged;
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
     *   <li>{@code "00 1 23"} ： 必ず2桁の数字でなければなりません。</li>
     *   <li>{@code "12 "}、{@code " 34"} ： 先頭や末尾に余分なスペースが含まれてはなりません。</li>
     *   <li>{@code "AB FF"} ： {@code 0}～{@code 9}、{@code a}～{@code f} のみが許容されます。大文字は許容されません。</li>
     *   <li>{@code null}</li>
     * </ul>
     * 
     * @param hexStr 検査対象の文字列
     * @return {@code hexStr} が16進表示形式の場合は {@code true}
     *         （空文字列の場合は {@code true}、{@code null} の場合は {@code false}）
     */
    static boolean isHexFormat(String hexStr) {
        return hexStr != null && hexStr.matches("^$|^[0-9a-f]{2}( [0-9a-f]{2})*$");
    }
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    private TestUtil() {
    }
}
