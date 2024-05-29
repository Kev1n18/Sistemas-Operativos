package com.example.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpConnectionWorkerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;
    private final String webroot;

    public HttpConnectionWorkerThread(Socket socket, String webroot) {
        this.socket = socket;
        this.webroot = webroot;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            // Leitura da linha de requisição HTTP
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String requestLine = reader.readLine();
            LOGGER.info("Request Line: " + requestLine);

            try {
                HttpRequest request = parseHttpRequest(requestLine);
                LOGGER.info("Parsed Request: " + request);

                // Verificação se o método HTTP é GET ou POST
                if (!request.getMethod().equals("GET") && !request.getMethod().equals("POST")) {
                    throw new IllegalArgumentException("Invalid HTTP method: " + request.getMethod());
                }

                // Servir o arquivo solicitado
                Path filePath = Paths.get(webroot, request.getResource());
                if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    String contentType = Files.probeContentType(filePath);

                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: " + fileContent.length + "\r\n" +
                            "Content-Type: " + contentType + "\r\n" +
                            "\r\n";

                    outputStream.write(response.getBytes());
                    outputStream.write(fileContent);
                } else {
                    String notFoundResponse = "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "\r\n";
                    outputStream.write(notFoundResponse.getBytes());
                }
            } catch (IllegalArgumentException e) {
                // Método HTTP inválido ou requisição malformada, retornar uma resposta 400 Bad Request
                String badRequestHtml = "<html><head><title>400 Bad Request</title></head><body><h1>400 Bad Request</h1><p>Your browser sent a request that this server could not understand.</p></body></html>";
                String badRequestResponse = "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + badRequestHtml.length() + "\r\n" +
                        "\r\n" +
                        badRequestHtml;
                outputStream.write(badRequestResponse.getBytes());
            }

        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Error closing socket", e);
            }
        }
    }

    private HttpRequest parseHttpRequest(String requestLine) {
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Empty request line");
        }

        // Expressão regular para validar a linha de requisição HTTP
        Pattern pattern = Pattern.compile("^(GET|POST)\\s(\\/\\S*)\\s(HTTP\\/\\d\\.\\d)$");
        Matcher matcher = pattern.matcher(requestLine);

        if (matcher.find()) {
            return new HttpRequest(matcher.group(1), matcher.group(2), matcher.group(3));
        } else {
            throw new IllegalArgumentException("Invalid HTTP request line: " + requestLine);
        }
    }
}
