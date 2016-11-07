package xyz.hotchpotch.jutaime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import xyz.hotchpotch.jutaime.matchers.InChainBaseTest;
import xyz.hotchpotch.jutaime.matchers.InChainExactTest;
import xyz.hotchpotch.jutaime.matchers.InChainTest;
import xyz.hotchpotch.jutaime.matchers.NoCauseTest;
import xyz.hotchpotch.jutaime.matchers.RaiseBaseTest;
import xyz.hotchpotch.jutaime.matchers.RaiseExactTest;
import xyz.hotchpotch.jutaime.matchers.RaiseNothingTest;
import xyz.hotchpotch.jutaime.matchers.RaiseTest;
import xyz.hotchpotch.jutaime.matchers.RootCauseBaseTest;
import xyz.hotchpotch.jutaime.matchers.RootCauseExactTest;
import xyz.hotchpotch.jutaime.matchers.RootCauseTest;
import xyz.hotchpotch.jutaime.matchers.ThrowableBaseMatcherTest;

@RunWith(Suite.class)
@SuiteClasses({
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
