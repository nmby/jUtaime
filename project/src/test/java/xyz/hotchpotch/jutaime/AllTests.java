package xyz.hotchpotch.jutaime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import xyz.hotchpotch.jutaime.serializable.STUtilTest;
import xyz.hotchpotch.jutaime.serializable.experimental.TestUtilTest;
import xyz.hotchpotch.jutaime.throwable.RaiseMatcherTest;
import xyz.hotchpotch.jutaime.throwable.RaiseMatchersTest;
import xyz.hotchpotch.jutaime.throwable.TesteeTest;
import xyz.hotchpotch.jutaime.throwable.matchers.InChainBaseTest;
import xyz.hotchpotch.jutaime.throwable.matchers.InChainExactTest;
import xyz.hotchpotch.jutaime.throwable.matchers.InChainTest;
import xyz.hotchpotch.jutaime.throwable.matchers.NoCauseTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseBaseTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseExactTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseNothingTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RaiseTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCauseBaseTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCauseExactTest;
import xyz.hotchpotch.jutaime.throwable.matchers.RootCauseTest;
import xyz.hotchpotch.jutaime.throwable.matchers.ThrowableBaseMatcherTest;

@RunWith(Suite.class)
@SuiteClasses({
    STUtilTest.class,
    TestUtilTest.class,
    
    RaiseMatchersTest.class,
    RaiseMatcherTest.class,
    TesteeTest.class,
    
    InChainBaseTest.class,
    InChainExactTest.class,
    InChainTest.class,
    NoCauseTest.class,
    RaiseBaseTest.class,
    RaiseExactTest.class,
    RaiseNothingTest.class,
    RaiseTest.class,
    RootCauseBaseTest.class,
    RootCauseExactTest.class,
    RootCauseTest.class,
    ThrowableBaseMatcherTest.class
})
public class AllTests {
}
