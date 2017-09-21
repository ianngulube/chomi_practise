
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.codehaus.groovy.control.CompilationFailedException;

public class Main {

    public static void main(String[] args) throws Exception {
        String fileName = "C:\\Users\\BizaDev_B\\Documents\\NetBeansProjects\\SpringMongoDB\\src\\test\\java\\Tester.groovy";
        GroovyClassLoader gcl = new GroovyClassLoader();
        Class clazz = gcl.parseClass(new File(fileName));
        Object aScript = clazz.newInstance();

        TestInterface ifc = (TestInterface) aScript;
        //ifc.printIt();

        //ifc.getTrips();
        //anotherWay();
        //method();
        //dynamic();
        scriptEngine();
    }

    public static void anotherWay() throws IOException, ResourceException, ScriptException {

        String[] roots = new String[]{"C:\\Users\\BizaDev_B\\Documents\\NetBeansProjects\\SpringMongoDB\\src\\test\\java"};
        GroovyScriptEngine gse = new GroovyScriptEngine(roots);
        Binding binding = new Binding();
        binding.setVariable("input", "world");
        gse.run("Tester2.groovy", binding);
        System.out.println(binding.getVariable("output"));
    }

    static void method() throws CompilationFailedException, IOException {
        new GroovyShell().parse(new File("C:\\Users\\BizaDev_B\\Documents\\NetBeansProjects\\SpringMongoDB\\src\\test\\java\\test.groovy")).invokeMethod("hello_world", null);
    }

    static void dynamic() throws CompilationFailedException,
            IOException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            IllegalArgumentException,
            InvocationTargetException {
        Class scriptClass = new GroovyClassLoader().parseClass(new File("C:/Users/BizaDev_B/Documents/NetBeansProjects/SpringMongoDB/src/test/java/test.groovy"));
        Object scriptInstance = scriptClass.newInstance();
        scriptClass.getDeclaredMethod("hello_world", new Class[]{}).invoke(scriptInstance, new Object[]{});
    }

    static void scriptEngine() throws IOException, ResourceException, ScriptException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Class scriptClass = new GroovyScriptEngine("").loadScriptByName("src/test/java/test.groovy");
        Object scriptInstance = scriptClass.newInstance();
        scriptClass.getDeclaredMethod("hello_world", new Class[]{}).invoke(scriptInstance, new Object[]{});
    }

    static void wordCount() {
        
    }
}
