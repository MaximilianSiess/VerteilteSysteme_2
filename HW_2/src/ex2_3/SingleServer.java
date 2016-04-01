package ex2_3;

import java.io.*;
import java.net.*;

public class SingleServer {
	
	private static ServerSocket providerSocket;
	private static Socket connection = null;
	private static boolean running = true;
	
	void establishConnection() {
		try{
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());

        }
        catch(IOException e){
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		
		//TODO(Max): Hook does not work inside Eclipse
		
		// Add a hook for shutdown exception handling
		Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("Shutdown hook activated!");
                running = false;           
            }
        });
		
		// Server logic
		SingleServer server = new SingleServer();
		
		try {
			providerSocket = new ServerSocket();
            providerSocket.setReuseAddress(true);
            providerSocket.bind(new InetSocketAddress(Protocol.getPortNumber()), 10);
			
			while (running) {
				server.establishConnection();
				Protocol.InitServer(connection);
				
				boolean socketOpen = true;
				
				while(socketOpen) {
					socketOpen = Protocol.reply();
				}
			}
			connection.close();
		} catch (IOException e) {
			System.out.println("Could not establish a connection!");
			e.printStackTrace();
			
		}
	}

}