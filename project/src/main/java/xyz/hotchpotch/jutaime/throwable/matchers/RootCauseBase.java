package xyz.hotchpotch.jutaime.throwable.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

/**
 * 根本原因（root cause）が期待通りのものかを検査する {@code Matcher} の基底クラスです。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @since 1.0.0
 * @author nmby
 */
/*package*/ class RootCauseBase extends ThrowableBaseMatcher {
    
    // [static members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // [instance members] ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /*package*/ RootCauseBase(boolean exactly, Class<? extends Throwable> expectedType, String expectedMessage) {
        super(exactly, expectedType, expectedMessage);
    }
    
    /*package*/ RootCauseBase(boolean exactly, Class<? extends Throwable> expectedType) {
        super(exactly, expectedType);
    }
    
    /*package*/ RootCauseBase(Matcher<Throwable> matcher) {
        super(matcher);
    }
    
    @Override
    /*package*/ boolean matchesWhole(Throwable actual) {
        assert actual != null;
        List<Throwable> chain = new ArrayList<>();
        Throwable t = actual;
        
        while (t.getCause() != null) {
            chain.add(t);
            t = t.getCause();
            
            // 例外チェインがループ状になっている場合のための処置。
            // ループ状の例外チェインが妥当であるはずがないし実装する輩がいるとは思えないが、
            // 防御的に対処コードを実装しておく。
            // 
            // equals() がオーバーライドされている可能性が無くはないので
            // List#contains() ではなく明示的に == で比較することにする。
            Throwable t2 = t;
            if (chain.stream().anyMatch(x -> x == t2)) {
                // ループしている場合は不合格とする
                return false;
            }
        }
        return matchesEach(t);
    }
    
    @Override
    /*package*/ String descriptionTag() {
        return "rootCause";
    }
}
