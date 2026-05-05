package com.coboldemo.accept;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Java equivalent of {@code accept/accept_from.cbl}.
 *
 * <p>The original COBOL program demonstrates the many flavors of the
 * {@code ACCEPT ... FROM ...} statement, which is GnuCOBOL's primary
 * way of pulling values from the runtime environment: command-line
 * arguments, environment variables, system date/time, the logged in
 * user name, and console input. This Java port mirrors each of those
 * behaviors using only the standard library.</p>
 *
 * <p>Mapping summary:
 * <ul>
 *   <li>{@code ACCEPT FROM COMMAND-LINE} / {@code ARGUMENT-NUMBER} /
 *       {@code ARGUMENT-VALUE} &rarr; the {@code String[] args} parameter
 *       passed to {@code main}.</li>
 *   <li>{@code ACCEPT FROM ENVIRONMENT "NAME"} &rarr;
 *       {@link System#getenv(String)}; the COBOL example also writes a
 *       value back via {@code SET ENVIRONMENT}, which has no portable
 *       Java equivalent (the JDK exposes the env map as read-only), so
 *       this port uses a {@link System#setProperty(String, String) system property} for the
 *       "after setting" demonstration instead.</li>
 *   <li>{@code ACCEPT FROM DATE / DATE YYYYMMDD / DAY / DAY YYYYDDD /
 *       TIME / DAY-OF-WEEK} &rarr; {@link LocalDateTime#now()} formatted
 *       through {@link DateTimeFormatter} patterns that match the COBOL
 *       output shape.</li>
 *   <li>{@code ACCEPT FROM USER NAME} &rarr; the {@code user.name}
 *       system property.</li>
 *   <li>{@code ACCEPT ws-variable [FROM CONSOLE]} &rarr;
 *       {@link Scanner#nextLine()}.</li>
 *   <li>{@code ACCEPT FROM LINES / COLUMNS} and
 *       {@code CALL "CBL_GET_SCR_SIZE"} require GnuCOBOL screen mode and
 *       have no portable Java analogue, so we report them as
 *       unsupported with a short explanation.</li>
 * </ul>
 */
public class AcceptExample {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("ACCEPT... FROM... Example Program");
        System.out.println("---------------------------------");
        System.out.println("Pass command line parameters to demo that feature");
        System.out.println();

        // ACCEPT ws-input FROM COMMAND-LINE -> the full command-line
        // string. Java does not preserve the original raw line, so we
        // re-join the parsed args with single spaces.
        String fullCommandLine = String.join(" ", args);
        System.out.println("accept from command-line: " + fullCommandLine);

        // ACCEPT ws-input FROM ARGUMENT-NUMBER -> args.length.
        System.out.println("accept from argument-number: " + args.length);

        // ACCEPT ws-input FROM ARGUMENT-VALUE (looped) -> iterate args[].
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.println("accept from argument-value: " + args[i]);
            }
        }

        // ACCEPT ws-input FROM ENVIRONMENT "COB_TEST_ENV_KEY" before set.
        final String envKey = "COB_TEST_ENV_KEY";
        System.out.println("Before environment setting set:");
        String envValue = System.getenv(envKey);
        System.out.println("accept from environment: " + (envValue == null ? "" : envValue));

        // The COBOL program follows that with ACCEPT FROM EXCEPTION
        // STATUS, which surfaces the runtime exception code raised by
        // the previous statement. The closest Java equivalent is
        // checking whether the lookup returned null/empty.
        System.out.println("accept from exception status: "
                + (envValue == null ? "1537 (env var not set, equivalent to EC-IMP-ACCEPT)" : "0"));

        // SET ENVIRONMENT "COB_TEST_ENV_KEY" TO "NOW SET!" --
        // System.setenv does not exist in Java, so we use a system
        // property to keep the same demonstration shape.
        System.setProperty(envKey, "NOW SET!");
        System.out.println("After environment setting set:");
        System.out.println("accept from environment: " + System.getProperty(envKey));

        // ACCEPT ws-input FROM DATE -> YYMMDD
        LocalDateTime now = LocalDateTime.now();
        System.out.println("accept from date: "
                + now.format(DateTimeFormatter.ofPattern("yyMMdd")));

        // ACCEPT ws-input FROM DATE YYYYMMDD -> YYYYMMDD
        System.out.println("accept from date yyyymmdd: "
                + now.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // ACCEPT ws-input FROM DAY -> YYDDD (day-of-year, two-digit year)
        LocalDate today = now.toLocalDate();
        System.out.println("accept from day: "
                + String.format("%02d%03d", today.getYear() % 100, today.getDayOfYear()));

        // ACCEPT ws-input FROM DAY YYYYDDD -> YYYYDDD
        System.out.println("accept from day yyyyddd: "
                + String.format("%04d%03d", today.getYear(), today.getDayOfYear()));

        // ACCEPT ws-input FROM TIME -> hhmmssnn (two-digit hundredths)
        LocalTime nowTime = now.toLocalTime();
        int hundredths = nowTime.getNano() / 10_000_000;
        System.out.println("accept from time: "
                + String.format("%02d%02d%02d%02d",
                        nowTime.getHour(), nowTime.getMinute(),
                        nowTime.getSecond(), hundredths));

        // ACCEPT ws-input FROM DAY-OF-WEEK -> Monday=1 .. Sunday=7
        System.out.println("accept from day-of-week: " + today.getDayOfWeek().getValue());

        // ACCEPT ws-input FROM USER NAME
        System.out.println("accept from user name: " + System.getProperty("user.name"));

        // ACCEPT ws-input FROM CONSOLE -> read a line from stdin. We
        // skip the prompt entirely if stdin is not a terminal so the
        // class can be run unattended (e.g. by `mvn exec:java` in CI).
        if (System.console() != null) {
            System.out.print("Enter value: ");
            try (Scanner scanner = new Scanner(System.in)) {
                String line = scanner.hasNextLine() ? scanner.nextLine() : "";
                System.out.println("accept from console: " + line);
            }
        } else {
            System.out.println("accept from console: <skipped: no interactive console attached>");
        }

        // ACCEPT ws-input FROM LINES / COLUMNS / CBL_GET_SCR_SIZE rely
        // on GnuCOBOL screen mode (ncurses). There is no portable Java
        // analogue without pulling in a third-party library, so we
        // report the limitation.
        System.out.println("accept from lines:    <screen-mode only; not portably available in JDK>");
        System.out.println("accept from columns:  <screen-mode only; not portably available in JDK>");
        System.out.println("CBL_GET_SCR_SIZE:     <screen-mode only; not portably available in JDK>");
    }
}
