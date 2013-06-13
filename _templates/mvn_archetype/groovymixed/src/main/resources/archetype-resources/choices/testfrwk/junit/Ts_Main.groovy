#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

@org.junit.runner.RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses([NewTest.class])
//@org.junit.runners.Suite.SuiteClasses([NewTest.class, ClassicTest.class])
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
