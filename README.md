# jUtaime （ジュテーム）
JUnitでのテストを効率化するためのライブラリです。  

#### 例外検証の効率化
通常の検証と同じスタイルで、`assertThat()` を使用して例外発生コードを検証することができます。  

    assertThat(Testee.of(SomeClass::someMethodShouldFail),
            RaiseMatchers.raise(ExpectedException.class, "expected message"));

* 例外またはエラーをスローしうる検査対象の処理を、`Testee.of()` の中に記述します。
* 期待する例外の型やメッセージ、原因（cause）の型などを、`RaiseMatchers.raise()` メソッドなどで指定します。

次のように、ひとつのテストケースメソッド内に複数の例外検証コードを記述できます。  

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

また、[hamcrest.org](http://hamcrest.org/JavaHamcrest/) が提供する各種 Matcher と組み合わせて使用することもできます。  
次の例では、オペレーションの実行により NullPointerException または IllegalArgumentException がスローされることを検証しています。  

    assertThat(of(() -> obj.doSomething(null)),
            anyOf(raise(NullPointerException.class), raise(IllegalArgumentException.class));

次の例では、NullPointerException 以外の何らかの実行時例外を原因として上位例外がスローされることを検証しています。  

    assertThat(of(() -> obj.doSomething(param)),
            allOf(raise(WrappingException.class, "expected message"),
                  rootCause(RuntimeException.class),
                  not(rootCause(NullPointerException.class)));

`allOf()` の代わりに、次の連結スタイルで記述することも可能です。  

    assertThat(of(() -> obj.doSomething(param)),
            raise(WrappingException.class, "expected message")
                    .rootCause(RuntimeException.class)
                    .not(rootCause(NullPointerException.class)));

詳細は [javadoc](http://nmby.github.io/jUtaime/api-doc/index.html) を参照してください。  

## 前提・依存
* ジュテームのサポート対象は java 8 です。java 7 以前では利用できません。
* ジュテームは [hamcrest-core](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.hamcrest) に依存しています。  
従ってジュテームを利用するためには [hamcrest-core](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.hamcrest) を
ビルド・パスに含める必要がありますが、JUnit4を利用しているのであれば、通常はすでに含まれているはずです。  

## 使い方
[こちら](https://github.com/nmby/jUtaime/releases)から xyz.hotchpotch.jutaime-X.X.X-yyyymmdd.jar をダウンロードして任意の場所に配置し、
ビルド・パスを設定してください。  

## 更新履歴
#### Version 1.0.0 (2015/07/26)
* 初版

## ライセンス
Licensed under the MIT License, see LICENSE.txt.  
Copyright (c) 2015 nmby  

---
Repository : [https://github.com/nmby/jUtaime](https://github.com/nmby/jUtaime)

