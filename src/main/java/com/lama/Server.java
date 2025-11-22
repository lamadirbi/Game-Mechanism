package com.lama;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234");
            while (true) {
                System.out.println("Waiting for a new client...");
                Socket socket = serverSocket.accept();
                System.out.println("New client connected from " + socket.getRemoteSocketAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                String message;
                while ((message = input.readLine()) != null) {
                    System.out.println("Received from client [" + socket.getRemoteSocketAddress() + "]: " + message);
                    output.println("Echo: " + message);
                }
                System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
            } catch (IOException ex) {
                System.out.println("Client handler exception: " + ex.getMessage());
            }
        }
    }
}
