import {inject, injectable} from "inversify";
import * as io from "socket.io";
import {createServer, Server} from "http";
import {TYPES} from "../bindings/types";
import {Log} from "../helpers/Log";
import {GameRegister} from "../model/GameRegister";
import {GameSettings} from "../model/Game";

@injectable()
class SocketManagerImpl {
    public static readonly PORT = 7373;

    private ioServer: io.Server;

    constructor(
        @inject(TYPES.Log) private readonly log: Log,
        @inject(TYPES.GameRegister) private readonly gameRegister: GameRegister
    ) {
        const server: Server = createServer();
        this.ioServer = io.default(server);

        this.ioServer.listen(SocketManagerImpl.PORT);

        this.ioServer.on("connection", (client) => {

            log.trace("SocketManagerImpl", "Client connected to server");

            client.on("disconnect", () => {
                log.trace("SocketManagerImpl", "Client disconnected from server.");
            });

            client.on("hostGameRequest", (name: string = "", gameSettings: GameSettings = {}) => {
                const game = gameRegister.generateGame();
                game.setGameSettings(gameSettings);

                (client as EmittableEvents).emit("gameHostGiven", game.Code);
                client.on("clientReady", () => {
                    game.addHostPlayer(name, client);
                });

                log.trace("SocketManagerImpl", `New Game Initiated by ${name} with code ${game.Code}`);
            });

            client.on("joinGameRequest", (name: string, id: string) => {
                const game = gameRegister.findGame(id);

                if (game === undefined) {
                    client.emit("joinGameNotification", false, "Game was not found.");
                    return log.trace("SocketManagerImpl", `${name} tried to join game with code ${id}.`);
                }

                if (game.isFull()) {
                    client.emit("joinGameNotification", false, "Game is full.");
                    return log.trace("SocketManagerImpl", `${name} tried to join game with code ${id}.`);
                }

                if (game.getPlayerNames().includes(name)) {
                    client.emit("joinGameNotification", false, "Someone already has that name.");
                    return log.trace("SocketManagerImpl", `${name} tried to join game with code ${id}.`);
                }

                client.emit("joinGameNotification", true, game.getPlayerNames());
                client.once("clientReady", () => {
                    game.addPlayer(name, client);
                    log.trace("SocketManagerImpl", `${name} joined game joined with code ${id}.`);
                });
            });

            client.on("browseGames", () => {
                const games = gameRegister.getPublicGames();
                const transmitData = games.map((g) => g.getGameInfo());
                client.emit("browseGameDataLoaded", transmitData);
            });

        });
    }

    private listenToClient() {}
}

interface SocketManager {}

export { SocketManager, SocketManagerImpl };

declare interface EmittableEvents extends io.Socket {
    emit(event: "gameHostGiven", code: string): boolean;
}
