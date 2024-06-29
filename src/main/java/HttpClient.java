import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpClient {

    private static final String SERVER_URL = "http://192.168.6.11:5000";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine().toLowerCase().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().toLowerCase().trim();

        try {
            if (authenticateUser(username, password)) {
                System.out.println("\nPlease vote for a food item:");
                String foodItem = scanner.nextLine().toLowerCase().trim();
                submitFoodVote(username, foodItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static boolean authenticateUser(String username, String password) throws Exception {
        String credentials = username + ":" + password;
        HttpURLConnection connection = createHttpPostConnection(SERVER_URL, credentials);

        int responseCode = connection.getResponseCode();
        System.out.println("Authentication Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = readResponse(connection);
            System.out.println("Authentication Response: " + response);

            return response.contains("successfully logged in");
        } else {
            String response = readErrorResponse(connection);
            System.out.println("Authentication Error Response: " + response);

            return false;
        }
    }

    private static void submitFoodVote(String username, String foodItem) throws Exception {
        String voteData = username + ":" + foodItem;
        HttpURLConnection connection = createHttpPostConnection(SERVER_URL, voteData);

        int responseCode = connection.getResponseCode();
        System.out.println("Vote Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = readResponse(connection);
            System.out.println("Vote Response: " + response);
        } else {
            String response = readErrorResponse(connection);
            System.out.println("Vote Error Response: " + response);
        }
    }

    private static HttpURLConnection createHttpPostConnection(String url, String postData) throws Exception {
        URL serverUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        byte[] postDataBytes = postData.getBytes("UTF-8");
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(postDataBytes);
            outputStream.flush();
        }

        return connection;
    }

    private static String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }

    private static String readErrorResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }
}
