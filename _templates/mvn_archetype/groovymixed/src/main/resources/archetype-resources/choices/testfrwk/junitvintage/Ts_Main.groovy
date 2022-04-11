#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

@org.junit.runner.RunWith(org.junit.platform.runner.JUnitPlatform.class)
@org.junit.platform.suite.api.Suite
@org.junit.platform.suite.api.IncludeClassNamePatterns([/^.*Test$/,
  /^Test.*$/, /^.*Prop$/, /^Prop.*$/])
@org.junit.platform.suite.api.SelectClasses([NewTest.class])
//@org.junit.platform.suite.api.SelectClasses([NewTest.class, ClassicTest.class])
public class Ts_Main {
    public static void main(String[] args) {
        if (1 > args.length)
            org.junit.platform.console.ConsoleLauncher.main("-c",
                NewTest.class.getName())
        else
            org.junit.platform.console.ConsoleLauncher.main(args)
    }
}
