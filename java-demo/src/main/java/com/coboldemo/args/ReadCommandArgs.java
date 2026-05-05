package com.coboldemo.args;

/**
 * Java equivalent of the two programs in
 * {@code read_command_args/}: {@code read_cmd_line_args.cbl} and
 * {@code read_specific_cmd_line_args.cbl}.
 *
 * <p>Mapping summary:
 * <ul>
 *   <li>{@code ACCEPT ws-cmd-args FROM COMMAND-LINE} returns the full
 *       command-line string. In Java the JVM has already split it
 *       into {@code String[] args}, so we re-join the elements with
 *       single spaces to mirror the COBOL output.</li>
 *   <li>{@code ACCEPT ws-num-args FROM ARGUMENT-NUMBER} &rarr;
 *       {@code args.length}.</li>
 *   <li>{@code DISPLAY ws-counter UPON ARGUMENT-NUMBER} followed by
 *       {@code ACCEPT ws-cmd-args FROM ARGUMENT-VALUE} &rarr;
 *       indexed access {@code args[i]}.</li>
 *   <li>The "{@code --test}" handling uses {@code INSPECT ... TALLYING}
 *       in COBOL; in Java we just call {@link String#equalsIgnoreCase}
 *       on each token.</li>
 * </ul>
 */
public class ReadCommandArgs {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Pass arg '--test' for special message.");

        // ACCEPT ws-cmd-args FROM COMMAND-LINE
        String fullCommandLine = String.join(" ", args);
        System.out.println("Full command line args: " + fullCommandLine);

        // INSPECT ... TALLYING ws-test-arg-count FOR ALL "--test"
        boolean sawTest = false;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--test")) {
                sawTest = true;
                break;
            }
        }
        if (sawTest) {
            System.out.println("You entered the '--test' cmd arg!");
        }
        System.out.println();

        // Second program: iterate through args one at a time.
        System.out.println("Argument-by-argument iteration:");
        System.out.println(String.format(
                "argument-number = %d", args.length));
        for (int i = 0; i < args.length; i++) {
            // COBOL prints just the value; we add the 1-based index
            // to make the output easier to read while debugging.
            System.out.println(String.format(
                    "  arg[%d] = %s", i + 1, args[i]));
        }
    }
}
