package quinzical.impl.multiplayer.models;

import com.google.inject.Singleton;
import io.socket.client.Socket;
import quinzical.interfaces.multiplayer.SocketModel;

@Singleton
public class SocketModelImpl implements SocketModel {
    private String name;
    private Socket socket;

    public SocketModelImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SocketModel setName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public SocketModel setSocket(final Socket socket) {
        this.socket = socket;
        return this;
    }

    @Override
    public void connect() {
        this.socket.connect();
    }

    @Override
    public void destroy() {
        this.socket.off();
        this.socket.disconnect();
        this.socket = null;
    }
}
