package org.sandbox.intro_groovy.util

@org.junit.runner.RunWith(org.junit.runners.Suite.class)
//@org.junit.runner.RunWith(org.spockframework.runtime.Sputnik.class)
@org.junit.runners.Suite.SuiteClasses([NewTest.class])
class Ts_Main {
    static void main(String[] args) {
        if (1 > args.length)
            org.junit.runner.JUnitCore.main(Ts_Main.class.getName())
        else {
            for (s in args)
                try {
                    Class<?> cls = Class.forName(s)
                } catch (ClassNotFoundException exc) {
                    System.err.println exc  //exc.printStackTrace()
                    System.exit 1
                }
            org.junit.runner.JUnitCore.main args 
        }
    }
}
