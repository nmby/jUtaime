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
            assertThat(of(() -> { Object o = null; o.toString(); }), raise(NullPointerException.class));
            assertThat(of(obj::dbOperation), rootCause(IOException.class));
            ...
        }

また、他の Matcher と組み合わせて使用することもできます。  
次の例では、[hamcrest.org](http://hamcrest.org/JavaHamcrest/) が提供する `anyOf`、`allOf`、`not` と組み合わせて使用しています。  

    // NullPointerException または IllegalArgumentException がスローされることを検証する。
    assertThat(of(() -> obj.doSomething(null)),
            anyOf(raise(NullPointerException.class), raise(IllegalArgumentException.class));
    
    // NullPointerException 以外の何らかの実行時例外を原因として上位例外がスローされることを検証する。
    assertThat(of(() -> obj.doSomething(param)),
            allOf(raise(WrappingException.class, "expected message"),
                  rootCause(RuntimeException.class),
                  not(rootCause(NullPointerException.class))));

`allOf()` の代わりに、次の連結スタイルで記述することも可能です。  

    assertThat(of(() -> obj.doSomething(param)),
            raise(WrappingException.class, "expected message")
                    .rootCause(RuntimeException.class)
                    .not(rootCause(NullPointerException.class)));

詳細は [javadoc](http://nmby.github.io/jUtaime/api-docs/index.html) の中の
[こちらのページ](http://nmby.github.io/jUtaime/api-docs/xyz/hotchpotch/jutaime/throwable/package-summary.html)
を参照してください。  
  
#### シリアライズ／デシリアライズ検証の効率化

※この機能は実験的機能の位置づけであり、API仕様を互換性のない形で予告なく変更・廃止することがあります。  
  
Serializable を実装したクラスに対するシリアライズ／デシリアライズ検証を効率化する機能を提供します。  
大きく分けて、次の3分類の機能を提供します。  
* オブジェクトのシリアライズ／デシリアライズに関するユーティリティ  
* プリミティブデータ型とオブジェクトのシリアライズ形式取得に関するユーティリティ  
* バイト配列の加工、およびバイト配列と16進表示形式文字列の変換に関するユーティリティ  
  
**利用例１**：オブジェクトをシリアライズ／デシリアライズし、挙動を確認しています。

    assertThat(TestUtil.writeAndRead(mySerializableObj), is(mySerializableObj));
    assertThat(TestUtil.writeAndRead(MySingleton.getInstance()), theInstance(MySingleton.getInstance()));
    assertThat(of(() -> TestUtil.write(myNotSerializableObj)), raise(FailToSerializeException.class));

**利用例２**：シリアライズされたバイト配列を改竄し、デシリアライズ時の挙動を確認しています。

    // test2-1
    Function<byte[], byte[]> modifier1 = bytes -> {
        byte[] modified = Arrays.copyOf(bytes, bytes.length);
        modified[modified.length - 1] = 0x02;
        return modified;
    };
    assertThat(TestUtil.writeModifyAndRead(Integer.valueOf(1), modifier1), is(Integer.valueOf(2)));
    
    // test2-2
    Function<byte[], byte[]> modifier2 = bytes -> {
        return TestUtil.replace(bytes, TestUtil.bytes(MyClass1.class.getName()), TestUtil.bytes(MyClass2.class.getName()));
    };
    assertThat(TestUtil.writeModifyAndRead(new MyClass1(), modifier2), instanceOf(MyClass2.class));
    
    // test2-3
    assertThat(of(() -> TestUtil.writeModifyAndRead(new MyOdd(7),
            bytes -> TestUtil.replace(bytes, TestUtil.bytes(7), TestUtil.bytes(8)))),
            raise(FailToDeserializeException.class).rootCause(InvalidObjectException.class));

詳細は [javadoc](http://nmby.github.io/jUtaime/api-docs/index.html) の中の
[こちらのページ](http://nmby.github.io/jUtaime/api-docs/xyz/hotchpotch/jutaime/serializable/experimental/TestUtil.html)
を参照してください。  

## 前提・依存
* ジュテームのサポート対象は java 8 です。java 7 以前では利用できません。
* ジュテームは [hamcrest-core](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.hamcrest) に依存しています。  
従ってジュテームを利用するためには [hamcrest-core](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.hamcrest) を
ビルド・パスに含める必要がありますが、JUnit4を利用しているのであれば、通常はすでに含まれているはずです。  

## 使い方
[こちら](https://github.com/nmby/jUtaime/releases)から xyz.hotchpotch.jutaime-X.X.X-yyyymmdd.jar をダウンロードして任意の場所に配置し、
ビルド・パスを設定してください。  

## 更新履歴
#### Version 1.2.2 (2015/09/07)
* 次の2つのメソッドの戻り値の型を Object から &lt;T&gt; に変更
  - TestUtil#read(byte[])
  - TestUtil#writeModifyAndRead(Object, Function&lt;byte[], byte[]&gt;)

#### Version 1.2.1 (2015/09/06)
* ドキュメント内の用語を統一

#### Version 1.2.0 (2015/09/06)
* シリアライズ／デシリアライズテスト支援機能を実験的に追加

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

