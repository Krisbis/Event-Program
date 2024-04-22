import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ANSI escape codes for text color
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String BLACK = "\u001B[30m";
        String YELLOW = "\u001B[33m";

        if (args.length == 0) {
            System.out.println(
                    GREEN + "Usage: java Main <command> " + BLACK + "[<options>]\n" + RESET + "Use 'java Main " + BLACK
                            + "--help" + RESET + " for more information." + RESET);
            System.exit(1);
        }

        // Create instances of ConfigManager and EventManager
        // Pass the ConfigManager instance to EventManager
        // EventManager constructor will use ConfigManager to get the file path
        ConfigManager configManager = new ConfigManager();
        EventManager eventManager = new EventManager(configManager);

        String command = args[0];
        String filePath = System.getenv("EVENTS_FILE_PATH"); // Get file path from environment variable
        String fileName = "events.csv";

        if (filePath == null) {
            // If environment variable is not set, use default file path
            filePath = System.getProperty("user.home") + "/" + fileName;
        }

        switch (command) {
            case "list":
                handleListCommand(args, eventManager);
                break;
            case "add":
                handleAddCommand(args, eventManager);
                break;
            case "delete":
                handleDeleteCommand(args, eventManager);
                break;
            case "config":
                handleConfigCommand(args, configManager);
                break;
            case "--help":
                System.out.println(GREEN + "Supported commands: list, add, delete, config" + RESET);
                System.out.println(
                        "Options for" + GREEN + " list " + RESET + "command: \n" + BLACK
                                + "--today \n--before-date \n--after-date \n--between-dates \n--category \n--all"
                                + RESET);
                System.out.println("Options for" + GREEN + " add " + RESET + "command:" + BLACK
                        + " [--date --category --description]" + RESET);
                System.out.println("Options for" + GREEN + " delete " + RESET + "command:" + BLACK
                        + " \n--date \n--category \n--description \n--all \n--file \n--dry-run " + RESET);
                System.out.println("Options for" + GREEN + " config " + RESET + "command:" + BLACK
                        + " \n--set-file-path \n--get-file-path \n--reset-file-path" + RESET);
                System.out.println();
                System.out.println(YELLOW + "\t\t!! SPECIAL WARNING !!" + BLACK);
                System.out.println("Be extremely careful using " + YELLOW + "delete --all " + BLACK + "and " + YELLOW
                        + "--file " + BLACK + "flags");
                System.out.println("as they will delete all events AND the whole file.");
                System.out.println("Use --dry-run to preview the events that will be deleted.");
                System.out.println(
                        "Check your file path using" + YELLOW + " java Main config --get-file-path" + BLACK + "");
                System.out.println("before using these flags.\n" + RESET);
                System.out
                        .println(GREEN + "Use 'java Main <command> --help' for more information on a command." + RESET);
                break;
            default:
                System.out
                        .println(
                                RED + "Invalid command. Supported commands: list, add, delete, config, --help" + RESET);
                break;
        }
    }

    // ------------------------LIST COMMAND--------------------------
    private static void handleListCommand(String[] args, EventManager eventManager) {
        List<String> argList = Arrays.asList(args);

        // ANSI escape codes for text color
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String BLACK = "\u001B[30m";

        // Check for flags and perform corresponding actions
        if (argList.contains("--help") || argList.contains("-h") || argList.contains("?")) {
            // Display help message
            System.out.println("Usage:" + GREEN + " java Main list " + BLACK + " [<options>]");
            System.out.println("Options:");
            System.out.println(BLACK + "  --today" + RESET + "\t\t\tShow events for today");
            System.out.println(BLACK + "  --before-date <date>" + RESET + "\t\tShow events before a specific date");
            System.out.println(BLACK + "  --after-date <date>" + RESET + "\t\tShow events after a specific date");
            System.out.println(BLACK + "  --between-dates <date1> <date2>" + RESET + "Show events between two dates");
            System.out.println(BLACK + "  --category <category>" + RESET + "\t\tShow events for a specific category");
            System.out.println(BLACK + "  --all" + RESET + "\t\t\t\tShow all events");
        } else if (argList.contains("--today")) {
            // Show events for today
            eventManager.showEventsForToday();
        } else if (argList.contains("--before-date")) {
            // Show events before a specific date
            int index = argList.indexOf("--before-date");
            LocalDate date = LocalDate.parse(args[index + 1]);
            eventManager.showEventsBeforeDate(date);
        } // Add other flag checks and corresponding actions
        else if (argList.contains("--category")) {
            // Show events for a specific category
            int index = argList.indexOf("--category");
            String category = args[index + 1];
            eventManager.showEventsForCategory(category);
        } else if (argList.contains("--after-date")) {
            // Show events after a specific date
            int index = argList.indexOf("--after-date");
            LocalDate date = LocalDate.parse(args[index + 1]);
            eventManager.showEventsAfterDate(date);
        } else if (argList.contains("--between-dates")) {
            // Show events between two dates
            int index1 = argList.indexOf("--between-dates");
            int index2 = index1 + 1;
            LocalDate date1 = LocalDate.parse(args[index1 + 1]);
            LocalDate date2 = LocalDate.parse(args[index2 + 1]);
            eventManager.showEventsBetweenDates(date1, date2);
        } else if (argList.contains("--all")) {
            // Show all events
            eventManager.showAllEvents();
        } else {
            System.out.println(
                    RED + "Invalid arguments. Please refer to" + BLACK + " java Main.java list --help" + RESET);
        }

    }

    // ------------------------ADD COMMAND--------------------------
    private static void handleAddCommand(String[] args, EventManager eventManager) {
        // Functionality to add events based on command-line arguments

        // ANSI escape codes for text color
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String BLACK = "\u001B[30m";

        List<String> argList = Arrays.asList(args);
        if (argList.contains("--help") || argList.contains("-h") || argList.contains("?")) {
            System.out.println("Usage: " + GREEN + "java Main add " + BLACK
                    + "--date <date> --category <category> --description <description>" + RESET);
            System.out.println("Example:" + RESET
                    + " java Main add --date 1998-03-25 --category Birthday --description 'My birthday'");
            return;
        }
        if (argList.contains("--date") && argList.contains("--category") && argList.contains("--description")) {
            int dateIndex = argList.indexOf("--date");
            int categoryIndex = argList.indexOf("--category");
            int descriptionIndex = argList.indexOf("--description");
            LocalDate date = LocalDate.parse(args[dateIndex + 1]);
            String category = args[categoryIndex + 1];
            String description = args[descriptionIndex + 1];
            Event event = new Event(date, category, description);
            eventManager.addEvent(event);
        } else {
            System.out.println(RED + "Invalid arguments. Please provide " + BLACK + "--date" + RED + ", " + BLACK
                    + "--category" + RED + ", and " + BLACK + "--description" + RESET);
        }
    }

    // ------------------------DELETE COMMAND--------------------------
    private static void handleDeleteCommand(String[] args, EventManager eventManager) {
        // Functionality to delete events based on command-line arguments

        // map the ANSI escape codes to the colors array
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String YELLOW = "\u001B[33m";
        String BLACK = "\u001B[30m";

        List<String> argList = Arrays.asList(args);
        // Check if dry-run flag is present
        boolean dryRun = argList.contains("--dry-run");
        // Notify user that dry run mode is enabled
        if (dryRun) {
            System.out.println(YELLOW + "!!! Dry run mode enabled. No events will be deleted !!!" + RESET);
        }

        if (argList.contains("--help") || argList.contains("-h") || argList.contains("?")) {
            System.out.println("Usage: " + GREEN + "java Main delete " + BLACK + "[<options>]" + RESET);
            System.out.println("Options:");
            System.out.println(BLACK + "  --date <date>" + RESET + "\t\t\tDelete events based on date");
            System.out.println(BLACK + "  --category <category>" + RESET + "\t\tDelete events based on category");
            System.out
                    .println(BLACK + "  --description <description>" + RESET + "\tDelete events based on description");
            System.out.println(BLACK + "  --all" + RESET + "\t\t\t\tDelete all events");
            System.out.println(BLACK + "  --file" + RESET + "\t\t\tDelete the whole CSV file containing events");
            System.out.println(BLACK + "  --dry-run" + RESET
                    + "\t\t\tUse in conjunction with other flags. Previews the events that would be deleted");
        } else if (argList.contains("--date")) {
            // Delete events based on date
            int dateIndex = argList.indexOf("--date");
            LocalDate date = LocalDate.parse(args[dateIndex + 1]);
            eventManager.deleteEventByDate(date, dryRun);
        } else if (argList.contains("--category")) {
            // Delete events based on category
            int categoryIndex = argList.indexOf("--category");
            String category = args[categoryIndex + 1];
            eventManager.deleteEventByCategory(category, dryRun);
        } else if (argList.contains("--description")) {
            // Delete events based on description
            int descriptionIndex = argList.indexOf("--description");
            String description = args[descriptionIndex + 1];
            eventManager.deleteEventByDescription(description, dryRun);
        } else if (argList.contains("--all")) {
            // Delete all events
            eventManager.deleteAllEventsInFile(dryRun);
        } else if (argList.contains("--file")) {
            // Delete the whole CSV file containing events
            eventManager.deleteEventsFile(dryRun);
        } else {
            System.out.println(
                    RED + "Invalid arguments. Please refer to " + BLACK + "java Main.java delete --help" + RESET);
            System.out.println("Use " + YELLOW + "--dry-run" + RESET + " to preview the events that will be deleted.");
        }
    }

    // ------------------------CONFIG COMMAND--------------------------
    private static void handleConfigCommand(String[] args, ConfigManager configManager) {
        // Functionality to set configuration based on command-line arguments
        // This can be used to set the file path or local file name for storing events
        // Example: java Main config --set-file-path /path/to/file

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String YELLOW = "\u001B[33m";
        String BLACK = "\u001B[30m";

        List<String> argList = Arrays.asList(args);
        if (argList.contains("--help") || argList.contains("-h") || argList.contains("?")) {
            System.out.println("Usage: " + GREEN + "java Main config " + BLACK + "[<options>]" + RESET);
            System.out.println("Options:");
            System.out.println(BLACK + "  --set-file-path <file-path>" + RESET
                    + "\tConfig the file path for storing and reading events");
            System.out.println(
                    BLACK + "  --get-file-path" + RESET + "\t\tGet the file path for storing and reading events");
            System.out.println(BLACK + "  --reset-file-path" + RESET + "\t\tReset the file path");
        } else if (argList.contains("--set-file-path")) {
            int filePathIndex = argList.indexOf("--set-file-path");
            String newFilePath = args[filePathIndex + 1];
            System.out.println(BLACK + "Setting file path to: " + YELLOW + newFilePath + RESET);
            // Set the file path using ConfigManager
            ConfigManager.setFilePath(newFilePath);
        } else if (argList.contains("--get-file-path")) {
            // Get the file path using ConfigManager
            String currentFilePath = ConfigManager.getFilePath();
            if (currentFilePath != null) {
                System.out.println(BLACK + "File path is set to: " + YELLOW + currentFilePath + RESET);
            } else {
                System.out.println("File path is not set.");
            }
        } else if (argList.contains("--reset-file-path")) {
            // Reset the file path using ConfigManager
            ConfigManager.setFilePath(null);
            System.out.println(BLACK + "File path is reset.");
        } else {
            System.out.println(
                    RED + "Invalid arguments. Please refer to " + BLACK + "java Main.java config --help" + RESET);
        }
    }
}
