import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class EventManager {
    private List<Event> events;
    private String filePath;
    boolean loadingFromFile = false;

    public EventManager(ConfigManager configManager) {
        // This is the constructor that is responsible for initializing
        // correct file path when the program starts
        this.events = new ArrayList<>();
        this.filePath = null;
        this.filePath = ConfigManager.getFilePath(); // Retrieve the file path from the config manager
        if (this.filePath == null) {
            this.filePath = "./events.csv"; // Use default file path if external configuration is not available
        }
        // Load events from CSV file
        loadEventsFromFile();
    }

    // -------------------Methods of file manipulation-------------------
    // Load events from CSV file
    private void loadEventsFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 1; i < lines.size(); i++) { // Skip the first line (header)
                String[] parts = lines.get(i).split(",");
                LocalDate date = LocalDate.parse(parts[0]);
                String category = parts[1];
                String description = parts[2];
                Event event = new Event(date, category, description);
                events.add(event);
            }
        } catch (IOException e) {
            System.err.println("Error loading events from file: " + e.getMessage());
        }
    }

    // Save events to CSV file
    private void saveEventsToFile() {
        try {
            // Get the newest event
            Event newestEvent = events.get(events.size() - 1);
    
            // Create the line for the newest event
            String newLine = String.join(",", newestEvent.getDate().toString(), newestEvent.getCategory(),
                    newestEvent.getDescription());
    
            // Write the new line to the file
            Files.write(Paths.get(filePath), Collections.singletonList(newLine), StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Error saving events to file: " + e.getMessage());
        }
    }
    

    private void deleteEventFromFile(Event event) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            
            // Skip the first line (header)
            List<String> dataLines = lines.subList(1, lines.size());
            
            List<String> updatedLines = dataLines.stream()
                    .filter(line -> {
                        String[] parts = line.split(",");
                        LocalDate date = LocalDate.parse(parts[0]);
                        String category = parts[1];
                        String description = parts[2];
                        return !(date.equals(event.getDate()) && category.equals(event.getCategory())
                                && description.equals(event.getDescription()));
                    })
                    .collect(Collectors.toList());
                    
            // Add the header line back to the updated lines
            updatedLines.add(0, lines.get(0));
            
            Files.write(Paths.get(filePath), updatedLines);
        } catch (IOException e) {
            System.err.println("Error deleting event from file: " + e.getMessage());
        }
    }
    

    // -----------------Methods for searching and filtering events-------------------

    // Show events for today
    public void showEventsForToday() {
        LocalDate today = LocalDate.now();
        List<Event> eventsForToday = events.stream()
                .filter(event -> event.getDate().equals(today))
                .collect(Collectors.toList());
        eventsForToday.forEach(System.out::println);
    }

    // Show events before a specific date
    public void showEventsBeforeDate(LocalDate date) {
        List<Event> eventsBeforeDate = events.stream()
                .filter(event -> event.getDate().isBefore(date))
                .collect(Collectors.toList());
        eventsBeforeDate.forEach(System.out::println);
    }

    // Show events for a specific category
    public void showEventsForCategory(String category) {
        List<Event> eventsForCategory = events.stream()
                .filter(event -> event.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        eventsForCategory.forEach(System.out::println);
    }

    // Show events after a specific date
    public void showEventsAfterDate(LocalDate date) {
        List<Event> eventsAfterDate = events.stream()
                .filter(event -> event.getDate().isAfter(date))
                .collect(Collectors.toList());
        eventsAfterDate.forEach(System.out::println);
    }

    // Show events between two dates
    public void showEventsBetweenDates(LocalDate date1, LocalDate date2) {
        List<Event> eventsBetweenDates = events.stream()
                .filter(event -> event.getDate().isAfter(date1) && event.getDate().isBefore(date2))
                .collect(Collectors.toList());
        eventsBetweenDates.forEach(System.out::println);
    }

    // Show all events
    public void showAllEvents() {
        events.forEach(System.out::println);
    }

    // -------------------Methods for adding events-------------------
    public void addEvent(Event event) {
        // Check if the event already exists
        if (!events.contains(event)) {
            events.add(event);
            saveEventsToFile();
        } else {
            System.out.println("Event already exists.");
        }
    }

    // -------------------Methods for deleting events-------------------
    public void deleteEventByDate(LocalDate date, boolean dryRun) {
        List<Event> eventsToDelete = events.stream()
                .filter(event -> event.getDate().equals(date))
                .collect(Collectors.toList());
        if (!eventsToDelete.isEmpty()) {
            if (dryRun) {
                System.out.println("Events to be deleted:");
                eventsToDelete.forEach(System.out::println);
            } else {
                events.removeAll(eventsToDelete);
                eventsToDelete.forEach(this::deleteEventFromFile);
            }
        } else {
            System.out.println("Event not found for the given date.");
        }
    }

    public void deleteEventByCategory(String category, boolean dryRun) {
        List<Event> eventsToDelete = events.stream()
                .filter(event -> event.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        if (!eventsToDelete.isEmpty()) {
            if (dryRun) {
                System.out.println("Events to be deleted:");
                eventsToDelete.forEach(System.out::println);
            } else {
                events.removeAll(eventsToDelete);
                eventsToDelete.forEach(this::deleteEventFromFile);
            }
        } else {
            System.out.println("No events found for the given category.");
        }
    }

    public void deleteEventByDescription(String description, boolean dryRun) {
        List<Event> eventsToDelete = events.stream()
                .filter(event -> event.getDescription().equalsIgnoreCase(description))
                .collect(Collectors.toList());
        if (!eventsToDelete.isEmpty()) {
            if (dryRun) {
                System.out.println("Events to be deleted:");
                eventsToDelete.forEach(System.out::println);
            } else {
                events.removeAll(eventsToDelete);
                eventsToDelete.forEach(this::deleteEventFromFile);
            }
        } else {
            System.out.println("No events found for the given description.");
        }
    }

    public void deleteAllEventsInFile(boolean dryRun) {
        if (dryRun) {
            System.out.println("Events to be deleted:");
            events.forEach(System.out::println);
        } else {
            events.clear();
            try {
                //delete all lines except the header
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                Files.write(Paths.get(filePath), lines.subList(0, 1));
            } catch (IOException e) {
                System.err.println("Error deleting all events from file: " + e.getMessage());
            }
        }
    }

    public void deleteEventsFile(boolean dryRun) {
        if (dryRun) {
            System.out.println("Events to be deleted:");
            events.forEach(System.out::println);
            System.out.println("Events file to be deleted: " + filePath);
        } else {
            events.clear();
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                System.err.println("Error deleting events file: " + e.getMessage());
            }
        }
    }
    

    // -------------------Methods for CONFIG----------------------------
    // Moved into separate config manager class

}
