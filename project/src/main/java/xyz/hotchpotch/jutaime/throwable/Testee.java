package xyz.hotchpotch.jutaime.throwable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * オペレーションによりスローされる例外およびエラーを検査するための、オペレーションのラッパーです。<br>
 * 戻り値を返すタイプのオペレーションおよび返さないタイプのオペレーションの双方を検査できます。
 * また、{@link Throwable} を含む任意の例外またはエラーを検査できます。<br>
 * {@link RaiseMatchers} と組み合わせた利用方法については、
 * {@link xyz.hotchpotch.jutaime.throwable xyz.hotchpotch.jutaime.throwable パッケージの説明}を参照してください。<br>
 * <br>
 * 次の例のように、ひとつの {@code Testee} オブジェクトはひとつの {@code assertThat()} メソッド内で複数回評価され得ます。<br>
 * <pre>    assertThat(Testee.of(・・・), allOf(matcher1(・・・), matcher2(・・・), matcher3(・・・)));</pre>
 * 上の例では、{@code Testee} オブジェクトは {@code matcher1}、{@code matcher2}、{@code matcher3} によって最大3回評価されます。<br>
 * テスト結果の一貫性を保つために、{@code Testee} は1回目の評価時に検査対象のオペレーションを一度だけ実行し、
 * 2回目以降はキャプチャした1回目の結果を返します。<br>
 * <br>
 * この実装はスレッドセーフです。<br>
 * たとえば上の例における {@code allOf()} の実装によっては、
 * ひとつの {@code Testee} オブジェクトが複数のスレッド上の {@code Matcher} から操作・参照され得ます。
 * {@code Testee} クラスはそのような場合でも正しく動作するように設計されています。<br>
 * 
 * @see xyz.hotchpotch.jutaime.throwable
 * @see RaiseMatchers
 * @see org.junit.Assert#assertThat(Object, org.hamcrest.Matcher)
 * @since 1.0.0
 * @author nmby
 */
public class Testee implements UnsafeCallable<Object> {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private static final String MSG_COMPLETED_SAFELY = "Completed safely.";
    private static final String MSG_NOT_TESTED = "I haven't yet been tested.";
    
    /**
     * 戻り値を返すタイプのオペレーションを検査するための {@code Testee} オブジェクトを返します。<br>
     * 
     * @param operation 例外またはエラーをスローしうるオペレーション
     * @return {@code operation} を検査するための {@code Testee}
     * @throws NullPointerException {@code operation} が {@code null} の場合
     */
    public static Testee of(UnsafeCallable<?> operation) {
        return new Testee(Objects.requireNonNull(operation));
    }
    
    /**
     * 戻り値を返さないタイプのオペレーションを検査するための {@code Testee} オブジェクトを返します。<br>
     * 
     * @param operation 例外またはエラーをスローしうるオペレーション
     * @return {@code operation} を検査するための {@code Testee}
     * @throws NullPointerException {@code operation} が {@code null} の場合
     */
    public static Testee of(UnsafeRunnable operation) {
        return new Testee(Objects.requireNonNull(operation));
    }
    
    /**
     * オペレーションが正常終了した場合の検査結果の文字列表現を返します。<br>
     * 
     * @param result オペレーションの戻り値
     * @return 検査結果の文字列表現
     */
    private static String descResult(Object result) {
        if (result == null || !result.getClass().isArray()) {
            return Objects.toString(result);
        }
        
        try {
            if (result.getClass().getComponentType().isPrimitive()) {
                Method method = Arrays.class.getMethod("toString", result.getClass());
                return (String) method.invoke(null, result);
            } else {
                return Arrays.deepToString((Object[]) result);
            }
        } catch (Exception e) {
            return Objects.toString(result);
        }
    }
    
    /**
     * オペレーションが例外をスローして終了した場合の検査結果の文字列表現を返します。
     * @param thrown オペレーションがスローした例外またはエラー
     * @return 検査結果の文字列表現
     */
    private static String descThrown(Throwable thrown) {
        List<Throwable> chain = new ArrayList<>();
        boolean containsLoop = false;
        Throwable t = thrown;
        
        while (t != null) {
            
            // 例外チェインがループ状になっている場合のための処置。
            // ループ状の例外チェインが妥当であるはずがないし実装する輩がいるとは思えないが、
            // 防御的に対処コードを実装しておく。
            // 
            // equals() がオーバーライドされている可能性が無くはないので
            // List#contains() ではなく明示的に == で比較することにする。
            Throwable t2 = t;
            if (chain.stream().anyMatch(x -> x == t2)) {
                chain.add(t);
                containsLoop = true;
                break;
            }
            
            chain.add(t);
            t = t.getCause();
        }
        String chainStr = chain.stream()
                .map(x -> String.format("%s (%s)", x.getClass().getName(), x.getMessage()))
                .collect(Collectors.joining(": "));
                
        StringBuilder str = new StringBuilder();
        str.append("throw ")
                .append(chainStr)
                .append(containsLoop ? ": ..." : "");
                
        return str.toString();
    }
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private final UnsafeCallable<?> operation;
    
    private boolean isVirgin = true;
    private Object result;
    private Throwable thrown;
    private volatile String description = MSG_NOT_TESTED;
    
    private Testee(UnsafeCallable<?> operation) {
        this.operation = operation;
    }
    
    private Testee(UnsafeRunnable operation) {
        this(() -> {
            operation.run();
            return MSG_COMPLETED_SAFELY;
        });
    }
    
    /**
     * 検査対象のオペレーションを実行します。
     * オペレーションが正常に終了した場合はその戻り値を返し、例外またはエラーが発生した場合はそのままスローします。<br>
     * このメソッドは JUnit テストケースの実行時に {@code Matcher} から実行されます。<br>
     * <br>
     * 次の例のように、このメソッドはひとつの {@code assertThat()} 内で複数回実行される可能性があります。<br>
     * <pre>    assertThat(Testee.of(・・・), allOf(matcher1(・・・), matcher2(・・・), matcher3(・・・));</pre>
     * このメソッドが毎回同じ結果を返すために、検査対象のオペレーションはこのメソッドの最初の呼び出し時にのみ実行されます。<br>
     * 2回目以降の呼び出しでは、キャプチャされた1回目の結果が返されるか、1回目の例外が再スローされます。<br>
     * 
     * @return オペレーションの戻り値
     * @throws Throwable オペレーション実行時に例外またはエラーが発生した場合
     */
    @Override
    public synchronized Object call() throws Throwable {
        if (isVirgin) {
            isVirgin = false;
            try {
                result = operation.call();
                description = descResult(result);
            } catch (Throwable t) {
                thrown = t;
                description = descThrown(thrown);
            }
        }
        
        if (thrown != null) {
            throw thrown;
        } else {
            return result;
        }
    }
    
    /**
     * 検査結果の文字列表現を返します。
     * この値が JUnit の障害トレースビューの中で "actual" としてレポートされます。<br>
     * <br>
     * 検査対象のオペレーションが正常に終了した場合は、その戻り値の文字列表現を返します。<br>
     * 但し、検査対象のオペレーションが戻り値を返さないタイプの場合は、次の文字列を返します。
     * <pre>    "Completed safely."</pre>
     * 検査対象のオペレーションにより例外またはエラーが発生した場合は、次の形式の文字列を返します。
     * <pre>    "throw <i>ExceptionClassName</i> (<i>Message</i>): <i>CauseClassName1</i> (<i>Message</i>): <i>CauseClassName2</i> (<i>Message</i>): ..."</pre>
     * 検査対象のオペレーションが未実行の場合は、次の文字列を返します。
     * <pre>    "I haven't yet been tested."</pre>
     * 
     * @return 検査結果の文字列表現
     */
    @Override
    public String toString() {
        return description;
    }
}
