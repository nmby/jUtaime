package xyz.hotchpotch.jutaime.throwable.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

/**
 * 例外チェインの中に目的の例外が含まれるかを検査する {@code Matcher} の基底クラスです。<br>
 * <br>
 * このクラスはスレッドセーフではありません。<br>
 * ひとつの {@code Matcher} オブジェクトが複数のスレッドから操作されることは想定されていません。<br>
 * 
 * @author nmby
 */
class InChainBase extends ThrowableBaseMatcher {
    
    // ++++++++++++++++ static members ++++++++++++++++
    
    // ++++++++++++++++ instance members ++++++++++++++++
    
    InChainBase(boolean exactly, Class<? extends Throwable> expectedType, String expectedMessage) {
        super(exactly, expectedType, expectedMessage);
    }
    
    InChainBase(boolean exactly, Class<? extends Throwable> expectedType) {
        super(exactly, expectedType);
    }
    
    InChainBase(Matcher<Throwable> matcher) {
        super(matcher);
    }
    
    @Override
    boolean matchesWhole(Throwable actual) {
        List<Throwable> chain = new ArrayList<>();
        Throwable t = actual;
        
        while (t != null) {
            // 例外チェインがループ状になっている場合のための処置。
            // ループ状の例外チェインが妥当であるはずがないし実装する輩がいるとは思えないが、
            // 防御的に対処コードを実装しておく。
            // 
            // equals() がオーバーライドされている可能性が無くはないので
            // List#contains() ではなく明示的に == で比較することにする。
            Throwable t2 = t;
            if (chain.parallelStream().anyMatch(x -> x == t2)) {
                // ループしている場合はこれ以上調べても仕方ない
                return false;
            }
            
            if (matchesEach(t)) {
                return true;
            }
            chain.add(t);
            t = t.getCause();
        }
        return false;
    }
    
    @Override
    String descriptionTag() {
        return "inChain";
    }
}
