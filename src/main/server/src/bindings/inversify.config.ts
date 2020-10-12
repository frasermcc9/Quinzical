import { Console } from "console";
import { Container, interfaces } from "inversify";
import { Socket } from "socket.io";
import LogImpl, { Log } from "../helpers/Log";
import { Timer, TimerImpl } from "../helpers/Timer";
import { ActiveQuestionManager, ActiveQuestionManagerImpl } from "../model/ActiveQuestionManager";
import { Game, GameImpl } from "../model/Game";
import { GameRegister, GameRegisterImpl } from "../model/GameRegister";
import { Player, PlayerConstructor, PlayerFactory, PlayerImpl } from "../model/Player";
import { QuestionBank, QuestionBankImpl } from "../model/Questions/QuestionBank";
import { SocketManager, SocketManagerImpl } from "../server/Socket";
import { IdGenerationContext, IdGeneratorContextImpl } from "../util/strategies/IdGenerator";
import { FACTORIES, TYPES } from "./types";

const mainContainer = new Container();
mainContainer.bind<SocketManager>(TYPES.SocketManager).to(SocketManagerImpl).inSingletonScope();
mainContainer.bind<GameRegister>(TYPES.GameRegister).to(GameRegisterImpl).inSingletonScope();
mainContainer.bind<QuestionBank>(TYPES.QuestionBank).to(QuestionBankImpl).inSingletonScope();

mainContainer.bind<IdGenerationContext>(TYPES.IDGeneratorContext).to(IdGeneratorContextImpl);

mainContainer.bind<ActiveQuestionManager>(TYPES.ActiveQuestionManager).to(ActiveQuestionManagerImpl);
mainContainer.bind<Log>(TYPES.Log).to(LogImpl);
mainContainer.bind<Timer>(TYPES.Timer).to(TimerImpl);

mainContainer.bind<Console>("console").toConstantValue(console);
mainContainer.bind<NodeJS.Process>("process").toConstantValue(process);

mainContainer.bind<interfaces.Factory<Game>>(FACTORIES.GameFactory).toFactory<Game>((context: interfaces.Context) => {
    return (type: "GAME") => {
        switch (type) {
            case "GAME":
                return context.container.resolve(GameImpl);
            default:
                throw new TypeError(`${type}: No such GAME found.`);
        }
    };
});

mainContainer.bind<PlayerConstructor>("PlayerConstructor").toConstructor(PlayerImpl);
mainContainer.bind<PlayerFactory>("PlayerFactory").toFactory<Player>((context) => {
    return (name: string, client: Socket) => {
        const constructor = context.container.get<PlayerConstructor>("PlayerConstructor");
        return new constructor(name, client);
    };
});

export { mainContainer as IoCContainer };
