import java.time.LocalDate;

public class Event {
    private LocalDate date;
    private String category;
    private String description;

    public Event(LocalDate date, String category, String description) {
        this.date = date;
        this.category = category;
        this.description = description;
    }

    // Getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method to print the Event object in a readable (and passable) format.
    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // Method to compare two Event objects based on their date, category, and description.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return date.equals(event.date) &&
               category.equals(event.category) &&
               description.equals(event.description);
    }

    // Method to generate a hash code for the Event object based on its date, category, and description.
    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
