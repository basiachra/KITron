package server.main;

import server.main.room.Room;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {


    private static final int STANDARD_BOARD_WIDTH = 600;
    private static final int STANDARD_BOARD_HEIGHT = 440;
    private int portNumber = 12345;
    private ServerSocket serverSocket;

    private PrintWriter out;
    private Socket clientSocket;


    private AtomicInteger playerIdGiver = new AtomicInteger();

    List<Room> rooms = new ArrayList<>();
    List<Player> players = new ArrayList<>();


    Server(){

    }

    public void process(int poolSize) throws IOException {

        System.out.println("SERVER");
        System.out.println("Waiting for players...");
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        try {
            serverSocket = new ServerSocket(portNumber);

            while(true){

                clientSocket = serverSocket.accept();
                System.out.println("Client " + playerIdGiver.incrementAndGet() + " connected");
                Player player = new Player(playerIdGiver.get());

                players.add(player);

                Runnable clientListener = new ClientHandler(this, player, clientSocket);
                executorService.execute(clientListener);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }


    public synchronized void addRoom(String name, int maxPeople, Player player) {
        rooms.add(new Room(STANDARD_BOARD_WIDTH,STANDARD_BOARD_HEIGHT,maxPeople,name));
        Room room = getRoomByName(name);
        room.join(player);
        System.out.println("Player " + player.getName() + " added new Room " + name + " " + maxPeople);
    }

    private Room getRoomByName(String name) {
        for(Room room : rooms) {
            if(room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public int getRoomId(String name) {
        for(int i = 0; i < rooms.size(); i++){
            if(rooms.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }


    public synchronized void joinRoom(Player player, int roomID){
        rooms.get(roomID).join(player);
        System.out.println("Player " + player.getName() + " joined Room " + roomID);
    }

    public synchronized void leaveRoom(Player player){
        for(Room room : rooms) {
            if(room.containsPlayer(player)){
                room.leave(player);
            }
        }
        System.out.println("Player " + player.getName() + " left the room");
    }
}
