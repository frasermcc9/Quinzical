package quinzical.impl.multiplayer.models;

import io.socket.client.Socket;

public class SocketModel {
    private static SocketModel instance;
    private String name;
    private Socket socket;

    private SocketModel() {
    }

    public static SocketModel getInstance() {
        if (instance == null) {
            instance = new SocketModel();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public SocketModel setName(String name) {
        this.name = name;
        return this;
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketModel setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }

    public void connect() {
        this.socket.connect();
    }

    public void destroy() {
        this.socket.off();
        this.socket.disconnect();
        this.socket = null;
    }
}
