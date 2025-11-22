package com.lama;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import com.lama.MathExpressionEvaluator;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 2345;
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Calculator Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected from " + socket.getRemoteSocketAddress());
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                String expression;
                while ((expression = input.readLine()) != null) {
                    System.out.println("Received expression from client [" + socket.getRemoteSocketAddress() + "]: " + expression);
                    String result;
                    try {
                        double evalResult = MathExpressionEvaluator.evaluate(expression);
                        result = Double.toString(evalResult);
                    } catch (IllegalArgumentException | ArithmeticException e) {
                        System.out.println("Evaluation error: " + e.getMessage());
                        result = "Error: Invalid expression";
                    }
                    output.println(result);
                }
                System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
            } catch (IOException ex) {
                System.out.println("Client handler exception: " + ex.getMessage());
            }
        }
    }
}
