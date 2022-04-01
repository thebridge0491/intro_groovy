package org.sandbox.intro_groovy.scriptexplore

@org.junit.platform.suite.api.Suite
@org.junit.platform.suite.api.IncludeClassNamePatterns([/^.*Test$/,
  /^Test.*$/, /^.*Prop$/, /^Prop.*$/])
@org.junit.platform.suite.api.SelectClasses([NewTest.class])
class Ts_Main {
    static void main(String[] args) {
        if (1 > args.length)
            org.junit.platform.console.ConsoleLauncher.main("-c", 
                NewTest.class.getName())
        else 
            org.junit.platform.console.ConsoleLauncher.main(args)
    }
}
