package linfo2252.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StateService {

    public void saveState(SystemState state) {
        String jsonString = convertToJson(state);
        try (FileWriter writer = new FileWriter("app_state.json")) {
            writer.write(jsonString);
        } catch (IOException e) {
            System.err.println("Error writing state: " + e.getMessage());
        }
    }

    public String[] getStateAsLines(SystemState state) {
        return convertToJson(state).split("\n");
    }

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
        return "[ " + list.stream().map(s -> "\"" + escape(s) + "\"").collect(Collectors.joining(", ")) + " ]";
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

        String items = list.stream().map(a -> String.format(
            "\n    { \"date\": \"%s\", \"type\": \"%s\", \"dept\": \"%s\" }",
            a.getDateTime().toString(), // ISO-8601 format
            escape(a.getType()),
            escape(a.getDepartment())
        )).collect(Collectors.joining(","));

        return "[" + items + "\n  ]";
    }

    private String escape(String s) { return s == null ? "" : s.replace("\"", "\\\""); }
}