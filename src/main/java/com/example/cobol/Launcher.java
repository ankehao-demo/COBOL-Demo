package com.example.cobol;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.cobol.accept.AcceptExample;
import com.example.cobol.accept.AcceptFromExample;
import com.example.cobol.accept.AcceptSecure;
import com.example.cobol.compdemo.CompConversionTest;
import com.example.cobol.displaytest.DisplayTest;
import com.example.cobol.displaytiming.DisplayTiming;
import com.example.cobol.isnumeric.IsNumericTest;
import com.example.cobol.jsongenerate.JsonGenerateExample;
import com.example.cobol.mergesort.MergeSortExample;
import com.example.cobol.mouse.MouseExample;
import com.example.cobol.numval.NumvalTest;
import com.example.cobol.readcommandargs.ReadCmdLineArgs;
import com.example.cobol.readcommandargs.ReadSpecificCmdLineArgs;
import com.example.cobol.redefines.RedefinesTest;
import com.example.cobol.reportwriter.ReportTest;
import com.example.cobol.screensize.GetScreenSize;
import com.example.cobol.search.SearchExample;
import com.example.cobol.sql.SqlExample;
import com.example.cobol.subprogram.MainApp;
import com.example.cobol.trim.TrimFunctionTest;
import com.example.cobol.unstring.UnstringExample;
import com.example.cobol.xmlgenerate.XmlGenerateExample;

/**
 * Entry point for the bundled fat-jar. Picks one of the example demos by name
 * from the first argument and forwards the rest to the demo's {@code main}.
 *
 * <pre>
 *   java -jar target/cobol-demo.jar accept-secure
 *   java -jar target/cobol-demo.jar read-cmd-args --test
 * </pre>
 *
 * Run with no args to see the list of available demos.
 */
public final class Launcher {

    @FunctionalInterface
    private interface Demo {
        void run(String[] args) throws Exception;
    }

    private static final Map<String, Demo> DEMOS = new LinkedHashMap<>();

    static {
        DEMOS.put("accept", AcceptExample::main);
        DEMOS.put("accept-secure", AcceptSecure::main);
        DEMOS.put("accept-from", AcceptFromExample::main);
        DEMOS.put("comp", CompConversionTest::main);
        DEMOS.put("display", DisplayTest::main);
        DEMOS.put("display-timing", DisplayTiming::main);
        DEMOS.put("is-numeric", IsNumericTest::main);
        DEMOS.put("json-generate", JsonGenerateExample::main);
        DEMOS.put("merge-sort", MergeSortExample::main);
        DEMOS.put("mouse", MouseExample::main);
        DEMOS.put("numval", NumvalTest::main);
        DEMOS.put("read-cmd-args", ReadCmdLineArgs::main);
        DEMOS.put("read-specific-cmd-args", ReadSpecificCmdLineArgs::main);
        DEMOS.put("redefines", RedefinesTest::main);
        DEMOS.put("report-writer", ReportTest::main);
        DEMOS.put("screen-size", GetScreenSize::main);
        DEMOS.put("search", SearchExample::main);
        DEMOS.put("sql", SqlExample::main);
        DEMOS.put("sub-program", MainApp::main);
        DEMOS.put("trim", TrimFunctionTest::main);
        DEMOS.put("unstring", UnstringExample::main);
        DEMOS.put("xml-generate", XmlGenerateExample::main);
    }

    private Launcher() {}

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "--help".equals(args[0]) || "-h".equals(args[0])) {
            printUsage();
            return;
        }

        String name = args[0];
        Demo demo = DEMOS.get(name);
        if (demo == null) {
            System.err.println("Unknown demo: " + name);
            printUsage();
            System.exit(2);
            return;
        }

        String[] rest = new String[args.length - 1];
        System.arraycopy(args, 1, rest, 0, rest.length);
        demo.run(rest);
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar cobol-demo.jar <demo-name> [demo-args...]");
        System.out.println();
        System.out.println("Available demos:");
        for (String name : DEMOS.keySet()) {
            System.out.println("  - " + name);
        }
    }
}
