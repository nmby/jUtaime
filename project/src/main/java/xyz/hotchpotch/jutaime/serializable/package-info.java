/**
 * JUnit4でのシリアライズ／デシリアライズに関するテストを効率化するためのクラスを提供します。<br>
 * 本パッケージの機能はいずれも {@link xyz.hotchpotch.jutaime.serializable.STUtil} クラスのユーティリティメソッドを通して提供され、
 * 大きく次の3つに分類されます。<br>
 * <ol>
 *   <li>オブジェクトのシリアライズ／デシリアライズに関する機能</li>
 *   <li>プリミティブデータ型とオブジェクトのシリアライズ形式取得に関する機能</li>
 *   <li>バイト配列の加工、およびバイト配列と16進表示形式文字列の変換に関する機能</li>
 * </ol>
 * <h3>1. オブジェクトのシリアライズ／デシリアライズに関する機能</h3>
 * {@link xyz.hotchpotch.jutaime.serializable.STUtil#writeAndRead(Object)} メソッドなどを利用することにより、
 * オブジェクトのシリアライズ／デシリアライズに関する検証を行うことができます。
 * <pre>
 *     // オブジェクトがシリアライズされ、正しくデシリアライズされることの検証
 *     assertThat(STUtil.writeAndRead(mySerializableObj), is(mySerializableObj));
 *     assertThat(STUtil.writeAndRead(MySingleton.getInstance()), theInstance(MySingleton.getInstance()));
 *     
 *     // オブジェクトがシリアライズ不可能であることの検証
 *     assertThat(Testee.of(() -> STUtil.write(myNotSerializableObj)),
 *             RaiseMatchers.raise(FailToSerializationException.class));
 * </pre>
 * <h3>2. プリミティブデータ型とオブジェクトのシリアライズ形式取得に関する機能</h3>
 * {@link xyz.hotchpotch.jutaime.serializable.STUtil#bytes(int)} メソッドなどを利用することにより、
 * 各種データをシリアライズすることによって得られるバイト配列を取得できます。
 * <pre>
 *     byte[] serializedInt   = STUtil.bytes(777);
 *     byte[] serializedFloat = STUtil.bytes(3.14f);
 *     byte[] serializedUTF   = STUtil.bytes(MyClass.class.getName());
 * </pre>
 * これらは次の「3. バイト配列の加工、およびバイト配列と16進表示形式文字列の変換に関する機能」と組み合わせて使用すると便利です。<br>
 * <h3>3. バイト配列の加工、およびバイト配列と16進表示形式文字列の変換に関する機能</h3>
 * {@link xyz.hotchpotch.jutaime.serializable.STUtil#replace(byte[], byte[], byte[])} メソッドなどを利用すると、
 * バイト配列の部分置換を行うことができます。
 * <pre>
 *     byte[] original = STUtil.write(Integer.valueOf(123));
 *     byte[] modified = STUtil.replace(original, STUtil.bytes(123), STUtil.bytes(777));
 *     assertThat(STUtil.read(modified), is(Integer.valueOf(777)));
 * </pre>
 * この機能を利用して、改竄されたバイト配列からのデシリアライズの検証を行うことができます。<br>
 * <pre>
 *     byte[] className = STUtil.bytes(MyClass.class.getName());
 *     byte[] proxyName = STUtil.bytes(MyClass.class.getName() + "$SerializationProxy");
 *     
 *     byte[] original = STUtil.write(new MyClass());
 *     byte[] modified = STUtil.replace(original, proxyName, className);
 *     
 *     assertThat(Testee.of(() -> STUtil.read(modified)),
 *             RaiseMatchers.raise(FailToDeserializeException.class)
 *             .rootCause(ObjectStreamException.class, "proxy required"));
 * </pre>
 * また、{@link xyz.hotchpotch.jutaime.serializable.STUtil#toHexString(byte[])} メソッドは
 * オブジェクトの直列化形式を理解するのに役立ちます。
 * <pre>
 *     byte[] b = STUtil.write("Hello, world!");
 *     System.out.println(STUtil.toHexString(b));
 *     // 「ac ed 00 05 74 00 0d 48 65 6c 6c 6f 2c 20 77 6f 72 6c 64 21」と出力される。
 * </pre>
 * 各機能の詳細とその他の機能については {@link xyz.hotchpotch.jutaime.serializable.STUtil} の説明を参照してください。<br>
 * 
 * @since 1.3.0
 * @author nmby
 */
package xyz.hotchpotch.jutaime.serializable;
