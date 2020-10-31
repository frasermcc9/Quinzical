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
    public final String getName() {
        return name;
    }

    @Override
    public final SocketModel setName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public final Socket getSocket() {
        return socket;
    }

    @Override
    public final SocketModel setSocket(final Socket socket) {
        this.socket = socket;
        return this;
    }

    @Override
    public final void connect() {
        this.socket.connect();
    }

    @Override
    public final void destroy() {
        this.socket.off();
        this.socket.disconnect();
        this.socket = null;
    }
}
