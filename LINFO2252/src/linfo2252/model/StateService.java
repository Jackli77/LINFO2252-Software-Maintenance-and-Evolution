package linfo2252.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StateService {

    // ... (Previous saveState code) ...

    public void saveState(SystemState state) {
        String jsonString = convertToJson(state);
        try (FileWriter writer = new FileWriter("app_state.json")) {
            writer.write(jsonString);
        } catch (IOException e) {
            System.err.println("Error writing state: " + e.getMessage());
        }
    }

    // New helper for the Controller Interface
    public String[] getStateAsLines(SystemState state) {
        // reuse the conversion logic
        String json = convertToJson(state);
        // Split by newline so we return String[] as requested
        return json.split("\n");
    }

    private String convertToJson(SystemState state) {
        // ... (The manual JSON string building logic from previous answer) ...
        // Recap for clarity:
        String featuresJson = formatListToJson(state.getActiveFeatures());
        return String.format(
            "{\n" +
            "  \"timestamp\": \"%s\",\n" +
            "  \"lastAction\": \"%s\",\n" +
            "  \"currentPage\": \"%s\",\n" +
            "  \"activeFeatures\": %s\n" +
            "}",
            escape(state.getTimestamp()),
            escape(state.getLastAction()),
            escape(state.getCurrentPage()),
            featuresJson
        );
    }
    
    private String formatListToJson(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        return "[ " + list.stream().map(s -> "\"" + escape(s) + "\"").collect(Collectors.joining(", ")) + " ]";
    }

    private String escape(String s) { return s == null ? "" : s.replace("\"", "\\\""); }
}