package linfo2252.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for persisting the application's state.
 * <p>
 * This class handles the serialization of the {@link SystemState} object into a 
 * JSON-formatted file ("app_state.json"). It constructs the JSON string manually 
 * to maintain zero external dependencies.
 */
public class StateService {

    private static final String OUTPUT_FILE = "app_state.json";

    /**
     * Persists the current system state to a local file.
     * Uses try-with-resources to ensure the file stream is closed securely.
     *
     * @param state The current snapshot of the system to save.
     */
    public void saveState(SystemState state) {
        String jsonString = convertToJson(state);
        
        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            writer.write(jsonString);
        } catch (IOException e) {
            System.err.println("Error writing state: " + e.getMessage());
        }
    }

    /**
     * Converts the state to a formatted JSON string array.
     * Useful for displaying the current state in the Console View.
     *
     * @param state The system state to convert.
     * @return An array of strings, where each line is a line of the JSON output.
     */
    public String[] getStateAsLines(SystemState state) {
        return convertToJson(state).split("\n");
    }

    // -------------------------------------------------------------------------
    // JSON Formatting Helpers (Manual Serialization)
    // -------------------------------------------------------------------------

    /**
     * Orchestrates the construction of the main JSON object.
     */
    private String convertToJson(SystemState state) {
        String featuresJson = formatStringList(state.getActiveFeatures());
        String apptsJson = formatAppointmentList(state.getAppointments());
        String historyJson = formatAppointmentList(state.getHistory());
        String userJson = formatUser(state.getUserProfile());

        return String.format(
            "{\n" +
            "  \"timestamp\": \"%s\",\n" +
            "  \"simulatedTime\": \"%s\",\n" +
            "  \"lastAction\": \"%s\",\n" +
            "  \"currentPage\": \"%s\",\n" +
            "  \"activeFeatures\": %s,\n" +
            "  \"userProfile\": %s,\n" +
            "  \"appointments\": %s,\n" +
            "  \"history\": %s\n" +
            "}",
            escape(state.getTimestamp()),
            escape(state.getSimulatedDate()),
            escape(state.getLastAction()),
            escape(state.getCurrentPage()),
            featuresJson,
            userJson,
            apptsJson,
            historyJson
        );
    }

    private String formatStringList(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        
        // Maps list elements to quoted strings: "Item1", "Item2"
        return "[ " + list.stream()
                .map(s -> "\"" + escape(s) + "\"")
                .collect(Collectors.joining(", ")) + " ]";
    }

    private String formatUser(UserProfile user) {
        if (user == null) return "null";
        
        return String.format(
            "{ \"name\": \"%s\", \"insurance\": \"%s\" }",
            escape(user.getName()),
            user.getInsurance()
        );
    }

    private String formatAppointmentList(List<Appointment> list) {
        if (list == null || list.isEmpty()) return "[]";

        // Formats each appointment as a nested JSON object
        String items = list.stream().map(a -> String.format(
            "\n    { \"date\": \"%s\", \"type\": \"%s\", \"dept\": \"%s\" }",
            a.getDateTime().toString(), // Uses ISO-8601 format
            escape(a.getType()),
            escape(a.getDepartment())
        )).collect(Collectors.joining(","));

        return "[" + items + "\n  ]";
    }

    /**
     * Sanitizes string inputs to prevent JSON format breakage.
     * Replaces double quotes with escaped quotes.
     */
    private String escape(String s) { 
        return s == null ? "" : s.replace("\"", "\\\""); 
    }
}