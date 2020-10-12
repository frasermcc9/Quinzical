import LogImpl from "../helpers/Log";

export const TYPES = {
    SocketManager: Symbol.for("SocketManager"),
    Log: Symbol.for("Log"),
    GameRegister: Symbol.for("GameRegister"),

    IDGeneratorContext: Symbol.for("IdGenerationContext"),

    QuestionBank: Symbol.for("QuestionBank"),

    ActiveQuestionManager: Symbol.for("ActiveQuestionManager"),
    Timer: Symbol.for("Timer"),
};

export const FACTORIES = {
    GameFactory: Symbol.for("GameFactory"),
    PlayerFactory: Symbol.for("PlayerFactory"),
};
