package com.example.demo;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>(); // SET USED FOR UNIQUE PRINTWRITER

    public static void main(String[] args) throws Exception {
        System.out.println("Chat server started...");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally { // ensures the server is closed after use.
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        return;
                    }
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    synchronized (clientWriters) {  // -->> removing the clients after being disconnected.
                        clientWriters.remove(out);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) { // -->> iterate through all the Clients and send them our message.
                                                            // ---> we can tweak this to make individual and group chats. Right now it is sending messages to
                    writer.println(message);                // everyone
                }
            }
        }
    }
}


/*
Set is used to maintain unique printwriter objects;
Printwriter: is a class that is used to write text to an output destination, i.e, a network socket / file / console;

Socket is basically the end points of a two-way communication link. A server socket binds to a port number and waits to listen to clients.(passive)
When a client connects, it returns a socket object for communication.

ServerSocket serverSocket= new ServerSocket (Port: )
Socket clientSocket =serverSocket.accpet() ;


CLient socket initiates communication , needs ip address and port number of server.

- Server creates a ServerSocket and waits.
- Client creates a Socket and connects to the server.
- ServerSocket accepts the connection and returns a new Socket.
- Now both sides have a Socket object and can exchange data using streams.


Why is the while loop used?
-- serverSocket.accept() will always wait for the client to send info.till then , the code will run till infinity

 new Handler(listener.accept()).start(); ---> Handler is the name of a thread class , that takes a socket as input in constructor
 , it extends "Thread" so we get to use the ".start()" method that starts the thread.


BufferdReader and PrintWriter used instead of ObjectInputStream & ObjectOutputStream .
 |||                                                |||
Good for text and simple                         Good for variables , objects ,wrappers




synchronized (clientWriters) {          --> Set is not thread safe .
    clientWriters.add(out);     -----> ensures that only one thread will enter the set at a time to prevent data corruption .
}


 */
