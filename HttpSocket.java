package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class HttpSocket {

    public static void main(String[] args) throws IOException {
        String url = enterUrl();
        List<String> elements = List.of(socketRequest(url).split("\r\n"));
        StringBuilder headers = new StringBuilder();
        String body = "";
        String new_line;
        for (String element : elements) {
            if (!element.startsWith("<")) {
                new_line = element;
                headers.append(new_line).append("\n");
            } else {
                body = element;
            }
        }

        System.out.printf("Status code: %s\n", elements.get(0).substring(9, 12));
        System.out.printf("Reason: %s\n", elements.get(0).substring(13));
        System.out.printf("Headers: %s\n", headers);
        System.out.printf("Body: %s\n", body);
    }

    public static String enterUrl() {
        System.out.print("Enter URL: ");
        Scanner url = new Scanner(System.in);
        return url.next();
    }

    public static String socketRequest(String url) throws IOException {
        Socket client = null;
        StringBuilder response = new StringBuilder();
        System.out.printf("URL: %s\n", url);
        try {
            client = new Socket(url, 80);
            InputStream is = client.getInputStream();
            OutputStream os = client.getOutputStream();
            String request = "GET /index.html HTTP/1.1\r\nHost:" + url + "\r\n\r\n";
            os.write(request.getBytes());
            os.flush();
            byte[] byte_buff = new byte[1024];
            int size = is.read(byte_buff);
            response.append(new String(byte_buff, 0, size));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        return response.toString();
    }
}