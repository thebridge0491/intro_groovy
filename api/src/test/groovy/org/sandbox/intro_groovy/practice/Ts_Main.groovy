package org.sandbox.intro_groovy.practice

@org.junit.platform.suite.api.Suite
@org.junit.platform.suite.api.IncludeClassNamePatterns([/^.*Test$/,
  /^Test.*$/, /^.*Prop$/, /^Prop.*$/])
@org.junit.platform.suite.api.SelectClasses([ClassicTest.class, SequenceopsTest.class])
class Ts_Main {
    static void main(String[] args) {
        if (1 > args.length)
            org.junit.platform.console.ConsoleLauncher.main("-c", 
                ClassicTest.class.getName(), "-c",
                SequenceopsTest.class.getName())
        else 
            org.junit.platform.console.ConsoleLauncher.main(args)
    }
}
