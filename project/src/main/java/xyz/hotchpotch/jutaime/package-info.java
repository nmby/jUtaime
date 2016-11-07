/**
 * JUnit4での例外検証を効率化するためのクラスを提供します。<br>
 * {@link xyz.hotchpotch.jutaime.Testee Testee} と、
 * {@link xyz.hotchpotch.jutaime.RaiseMatchers RaiseMatchers} の static ファクトリにより提供される
 * 各種 {@code Matcher} を組み合わせて使用することにより、任意のオペレーションによりスローされる例外およびエラーを
 * {@link org.junit.Assert#assertThat(Object, org.hamcrest.Matcher) assertThat()} で検査できます。<br>
 * <pre>
 *    assertThat(Testee.of(SomeClass::someMethodShouldFail),
 *            RaiseMatchers.raise(SomeException.class, "expected message"));
 * </pre>
 * {@code Testee} と {@code RaiseMatchers} は static インポートしておくとよいでしょう。<br>
 * 次の例のように、ひとつのテストメソッド内で複数の例外発生コードを検査することができます。<br>
 * <pre>
 *    import static xyz.hotchpotch.jutaime.Testee.*;
 *    import static xyz.hotchpotch.jutaime.RaiseMatchers.*;
 *    ・・・
 *       {@code @Test}
 *        public void testException() {
 *            assertThat(of((){@code ->} Integer.valueOf("abc")), raiseExact(NumberFormatException.class));
 *            assertThat(of((){@code ->} Integer.valueOf("123")), raiseNothing());
 *            assertThat(of((){@code ->} { Object o = null; o.toString(); }), raise(NullPointerException.class));
 *            assertThat(of(obj::dbOperation), rootCause(IOException.class));
 *            ...
 *        }
 * </pre>
 * <br>
 * また、{@link org.hamcrest.CoreMatchers org.hamcrest.CoreMatchers} が提供する標準 {@code Matcher} と組み合わせて使用することもできます。<br>
 * 次の例では、{@link org.hamcrest.CoreMatchers#anyOf(org.hamcrest.Matcher...) anyOf()} と組み合わせることにより、
 * オペレーションの実行により {@code NullPointerException} または {@code IllegalArgumentException} がスローされることを検査します。<br>
 * <pre>
 *    assertThat(of((){@code ->} obj.doSomething(null)),
 *            anyOf(raise(NullPointerException.class), raise(IllegalArgumentException.class)));
 * </pre>
 * 次の例では、{@link org.hamcrest.CoreMatchers#allOf(org.hamcrest.Matcher...) allOf()} と組み合わせることにより、
 * {@code NullPointerException} 以外の何らかの実行時例外を原因として上位例外がスローされることを検証します。<br>
 * <pre>
 *    assertThat(of((){@code ->} obj.doSomething(param)),
 *            allOf(raise(WrappingException.class),
 *                  rootCause(RuntimeException.class),
 *                  not(rootCause(NullPointerException.class))));
 * </pre>
 * {@link org.hamcrest.CoreMatchers#allOf(org.hamcrest.Matcher...) allOf()} の代わりに、
 * 次のポイント連結スタイルで記述することも可能です。<br>
 * <pre>
 *    assertThat(of((){@code ->} obj.doSomething(param)),
 *            raise(WrappingException.class)
 *                    .rootCause(RuntimeException.class)
 *                    .not(rootCause(NullPointerException.class)));
 * </pre>
 * 
 * @see org.junit.Assert#assertThat(Object, org.hamcrest.Matcher)
 * @since 1.0.0
 * @author nmby
 */
package xyz.hotchpotch.jutaime;
