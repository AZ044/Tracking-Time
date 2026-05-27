# Tracking-Time

A Java application that calculates the time you spend on each Windows application.

## Stack

- Java 17+ (tested with Java 17 / 21)
- JavaFX 21 (controls + fxml)
- JNA 5.8.0 (Windows User32 / Kernel32)
- Gson 2.14.0 (JSON persistence)
- Maven (build)

## Platform

Windows only. The tracking logic uses JNA to call `User32.GetForegroundWindow` and
`Kernel32.QueryFullProcessImageName`, which are Windows-specific APIs.

## How it works

Two entry points:

- `app` (JavaFX UI) — opens the dashboard window (`mvn javafx:run` target). Click **Refresh**
  to load today's data from `db/User.json` and display the top 5 apps, total time, etc.
- `Main` (CLI tracker) — runs an infinite loop that polls the foreground window every 500 ms
  and appends time per app into `db/User.json`. Run it in a terminal while you work.

The UI does **not** track on its own; run `Main` to record data, then click **Refresh** in
the UI to visualize it.

## Run on Windows

Prerequisites:
- JDK 17+ on the PATH (`java -version` should print 17 or higher)
- Maven on the PATH (`mvn -v`). Install via [Chocolatey](https://chocolatey.org/install)
  with `choco install maven` or download from [maven.apache.org](https://maven.apache.org/).

### Launch the UI

```cmd
cd path\to\projet_Hub
mvn clean javafx:run
```

### Run the tracker (separate terminal)

```cmd
cd path\to\projet_Hub
mvn clean compile
mvn exec:java -Dexec.mainClass=Main
```

Or, after a `mvn package` build, run the compiled classes directly:

```cmd
java -cp "target\classes;%USERPROFILE%\.m2\repository\net\java\dev\jna\jna\5.8.0\jna-5.8.0.jar;%USERPROFILE%\.m2\repository\net\java\dev\jna\jna-platform\5.8.0\jna-platform-5.8.0.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.14.0\gson-2.14.0.jar" Main
```

Data is written to `db/User.json` (created on first run). App icons are cached in `icons/`.

## Project layout

```
src/
  app.java              JavaFX entry point (UI)
  MainController.java   FXML controller, Refresh handler
  Main.java             CLI tracker (foreground window polling)
  json.java             JSON read/write helpers
  rsc/
    main-view.fxml      Dashboard layout
    Style.css           Dashboard styles
pom.xml                 Maven build config
db/User.json            Runtime tracking data (gitignored)
icons/                  Cached app icons (gitignored)
```
