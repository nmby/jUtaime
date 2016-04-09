# jUtaime （ジュテーム）
JUnitでのテストを効率化するためのライブラリです。  
* 例外検証の効率化  
* シリアライズ／デシリアライズ検証の効率化  
  
  
#### 例外検証の効率化

通常の検証と同じスタイルで、`assertThat()` を使用して例外発生コードを検証することができます。  

    assertThat(Testee.of(SomeClass::someMethodShouldFail),
            RaiseMatchers.raise(ExpectedException.class, "expected message"));

* 例外またはエラーをスローしうる検査対象の処理を、`Testee.of()` の中に記述します。
* 期待する例外の型やメッセージ、原因（cause）の型などを、`RaiseMatchers.raise()` メソッドなどで指定します。

次のように、様々なスタイルで例外検証コードを記述できます。  

    import static xyz.hotchpotch.jutaime.throwable.RaiseMatchers.*;
    import static xyz.hotchpotch.jutaime.throwable.Testee.*;
        ...
        @Test
        public void testException() {
            assertThat(of(() -> Integer.valueOf("abc")), raiseExact(NumberFormatException.class));
            assertThat(of(() -> Integer.valueOf("123")), raiseNothing());
            assertThat(of(() -> { Object o = null; o.toString(); }), raise(RuntimeException.class));
            assertThat(of(obj::dbOperation), rootCause(IOException.class));
            ...
        }

詳細は [javadoc](http://jutaime.hotchpotch.xyz/docs/api/index.html) の中の
[こちらのページ](http://jutaime.hotchpotch.xyz/docs/api/index.html?xyz/hotchpotch/jutaime/throwable/package-summary.html)
を参照してください。  
  
  
#### シリアライズ／デシリアライズ検証の効率化

Serializable 実装クラスに対するシリアライズ／デシリアライズ検証を効率化する機能を提供します。  
大きく分けて、次の3分類の機能を提供します。  
* オブジェクトのシリアライズ／デシリアライズに関するユーティリティ  
* プリミティブデータ型とオブジェクトのシリアライズ形式取得に関するユーティリティ  
* バイト配列の加工、およびバイト配列と16進表示形式文字列の変換に関するユーティリティ  

利用例をいくつか挙げます。
  
**利用例１**：オブジェクトをシリアライズ／デシリアライズし、挙動を検証しています。

    assertThat(STUtil.writeAndRead(mySerializableObj), is(mySerializableObj));
    assertThat(STUtil.writeAndRead(MySingleton.getInstance()), theInstance(MySingleton.getInstance()));
    assertThat(Testee.of(() -> STUtil.write(myNotSerializableObj)),
            RaiseMatchers.raise(FailToSerializeException.class));

**利用例２**：シリアライズされたバイト配列を改竄し、デシリアライズ時の挙動を検証しています。

    // test1 : オブジェクトをシリアライズしたのちバイト列を改竄し、改竄された内容にデシリアライズできることを検証しています。
    byte[] original = STUtil.write(Integer.valueOf(123));
    byte[] modified = STUtil.replace(original, STUtil.bytes(123), STUtil.bytes(777));
    
    assertThat(STUtil.read(modified), is(Integer.valueOf(777)));

    // test2 : シリアライゼーションプロキシを迂回したデシリアライズが抑止されることを検証しています。
    byte[] className = STUtil.bytes(MyClass.class.getName());
    byte[] proxyName = STUtil.bytes(MyClass.class.getName() + "$SerializationProxy");
    
    byte[] original = STUtil.write(new MyClass());
    byte[] modified = STUtil.replace(original, proxyName, className);
    
    assertThat(Testee.of(() -> STUtil.read(modified)),
            RaiseMatchers.raise(FailToDeserializeException.class)
                    .rootCause(ObjectStreamException.class, "proxy required"));

詳細は [javadoc](http://jutaime.hotchpotch.xyz/docs/api/index.html) の中の
[こちらのページ](http://jutaime.hotchpotch.xyz/docs/api/index.html?xyz/hotchpotch/jutaime/serializable/package-summary.html)
を参照してください。  
  
  
## 前提・依存
* jUtaime のサポート対象は java 8 です。java 7 以前では利用できません。
* jUtaime は [hamcrest-core](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.hamcrest) に依存しています。  
従って jUtaime を利用するためには [hamcrest-core](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.hamcrest) を
ビルド・パスに含める必要がありますが、JUnit4を利用しているのであれば、通常はすでに含まれているはずです。  
  
  
## 使い方
[こちら](https://github.com/nmby/jUtaime/releases)から xyz.hotchpotch.jutaime-X.X.X-yyyymmdd.jar をダウンロードして任意の場所に配置し、
ビルド・パスを設定してください。  
  
  
## 更新履歴
#### Version 1.4.0 (2016/01/10)
* 例外検証支援機能に raiseNothing(T) と raiseNothing(Matcher) を追加

#### Version 1.3.0 (2016/01/04)
* シリアライズ／デシリアライズ検証支援機能を正式リリース
* 例外検証支援機能の Testee クラスに総称型を導入

#### Version 1.2.2 (2015/09/07)
* 次の2つのメソッドの戻り値の型を Object から &lt;T&gt; に変更
  - TestUtil#read(byte[])
  - TestUtil#writeModifyAndRead(Object, Function&lt;byte[], byte[]&gt;)

#### Version 1.2.1 (2015/09/06)
* ドキュメント内の用語を統一

#### Version 1.2.0 (2015/09/06)
* シリアライズ／デシリアライズ検証支援機能を実験的に追加

#### Version 1.1.0 (2015/09/06)
* 例外の型は問わず例外メッセージの期待値のみを指定する機能を追加

#### Version 1.0.1 (2015/08/27)
* 内部実装を微修正
* API ドキュメントを微修正

#### Version 1.0.0 (2015/07/26)
* 初版
  
  
## ライセンス
Licensed under the MIT License, see LICENSE.txt.  
Copyright (c) 2015 nmby  

---
Repository : [https://github.com/nmby/jUtaime](https://github.com/nmby/jUtaime)

