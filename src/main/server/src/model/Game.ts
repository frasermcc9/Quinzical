import {inject, injectable} from "inversify";
import {Player, PlayerFactory, PlayerSummary} from "./Player";
import {Question} from "./Questions/Question";
import {Socket} from "socket.io";
import {TYPES} from "../bindings/types";
import {QuestionBank} from "./Questions/QuestionBank";
import {ActiveQuestionManager} from "./ActiveQuestionManager";
import {Timer} from "../helpers/Timer";
import {EventEmitter} from "events";
import {Log} from "../helpers/Log";

@injectable()
class GameImpl extends EventEmitter implements Game {
    //#region FIELDS

    private players: Player[] = [];
    private code: string = "AAAA";
    private host?: Player;

    private questionsCompleted: number;

    private questions: number;
    private timeToAnswer: number;
    private maxPlayers: number;
    private isGamePublic: boolean;

    private static default_questions = 5;
    private static initial_questions_answered = 0;
    private static default_time_to_answer = 10;
    private static default_max_players = 4;

    constructor(
        @inject("PlayerFactory") private readonly playerFactory: PlayerFactory,
        @inject(TYPES.QuestionBank) private readonly questionBank: QuestionBank,
        @inject(TYPES.ActiveQuestionManager) private readonly questionManager: ActiveQuestionManager,
        @inject(TYPES.Timer) private readonly timer: Timer,
        @inject(TYPES.Log) private readonly log: Log
    ) {
        super();
        this.questions = GameImpl.default_questions;
        this.questionsCompleted = GameImpl.initial_questions_answered;
        this.timeToAnswer = GameImpl.default_time_to_answer;
        this.maxPlayers = GameImpl.default_max_players;
        this.isGamePublic = false;
    }

    //#endregion

    //#region IN GAME

    startGame(): void {
        //'Close' the game to new players
        this.maxPlayers = this.CurrentPlayers;

        this.timer.setDelay(this.timeToAnswer * 1000).setFunction(() => this.progressToRoundEnd());

        this.questionManager.addPlayers(this.players);

        this.checkReady();

        this.players.forEach((player) => {
            player.signalGameStart();
        });
    }

    getNextQuestion(): Question {
        return this.questionBank.getQuestion();
    }

    progressToNextRound(): void {
        if (this.questionsCompleted == this.questions) return this.handleGameEnd();
        this.questionsCompleted++;

        this.players.forEach((player) =>
            player.getSocket().once("questionAnswered", (answer: string) => {
                this.questionManager.answerQuestion(answer, player, this.getTimeRatio());
                if (this.questionManager.isAllAnswered()) {
                    this.timer.stop();
                    this.progressToRoundEnd();
                }
            })
        );

        const nextQuestion: Question = this.getNextQuestion();
        this.questionManager.setNewQuestion(nextQuestion);
        this.log.trace("GameImpl", "Sending question.");

        this.timer.start();
    }

    progressToRoundEnd(): void {
        this.log.trace("GameImpl", `Round Ended. Waiting 3 seconds before continuing.`);
        this.players.forEach((player) =>
            player.signalRoundOver(this.questionManager.CorrectAnswer, player.Points, this.getTopPlayers())
        );
        setTimeout(() => {
            this.players.forEach((p) => p.getSocket().emit("goNextRound"));
            this.checkReady();
        }, 3000);
    }

    //#endregion

    //#region PRE-GAME

    addPlayer(name: string, player: Socket, host: boolean = false): boolean {
        if (this.players.length === this.maxPlayers) return false;
        if (this.players.find((p) => p.Name == name) !== undefined) return false;

        const playerObj: Player = this.playerFactory(name, player);
        this.players.push(playerObj);

        const names = this.players.map((p) => p.Name);
        this.players.forEach((p) => p.signalPlayerCountChange(names, this.maxPlayers));

        playerObj.getSocket().once("playerDisconnect", () => this.removePlayer(playerObj));

        if (host) {
            this.host = playerObj;
            this.host
                .getSocket()
                .once("startGame", () => this.startGame())
                .on("kickMember", (memberName: string) => this.removePlayer(memberName));
        }

        return true;
    }

    addHostPlayer(name: string, player: Socket): boolean {
        this.addPlayer(name, player, true);

        return true;
    }

    isPublic(): boolean {
        return this.isGamePublic;
    }

    isFull(): boolean {
        return this.players.length == this.maxPlayers;
    }

    //#endregion

    //#region GAME SETTINGS

    getGameInfo(): GameData {
        return {
            code: this.code,
            currentPlayers: this.CurrentPlayers,
            host: this.getHostName(),
            maxPlayers: this.MaxPlayers,
            questions: this.QuestionCount,
            timePerQuestion: this.TimePerQuestion,
        };
    }

    setGameSettings(settings: GameSettings) {
        this.questions = settings.questions ?? GameImpl.default_questions;
        this.timeToAnswer = settings.timePerQuestion ?? GameImpl.default_time_to_answer;
        this.maxPlayers = settings.maxPlayers ?? GameImpl.default_max_players;
        this.isGamePublic = settings.isGamePublic ?? false;
    }

    get Code(): string {
        return this.code;
    }

    set Code(value: string) {
        this.code = value;
    }

    get CurrentPlayers() {
        return this.players.length;
    }

    get MaxPlayers() {
        return this.maxPlayers;
    }

    get QuestionCount() {
        return this.questions;
    }

    get TimePerQuestion() {
        return this.timeToAnswer;
    }

    //#endregion

    getPlayerNames(): string[] {
        return this.players.map((p) => p.Name);
    }

    getHostName(): string {
        const name: string | undefined = this.host?.Name;
        if (name === undefined) {
            throw new Error("Host does not have a name");
        }
        return name;
    }

    /**
     * returns the time ratio at the current point
     */
    private getTimeRatio(): number {
        const result = this.timer.getTimeLeft() / (this.timeToAnswer * 1000);
        return result;
    }

    private handleGameEnd(): void {
        this.log.trace("GameImpl", `Game ${this.code} has concluded.`);
        const top = this.getTopPlayers();
        this.players.forEach((p) => p.signalGameOver(top));
        this.emit("gameEnd");
    }

    /**
     * Gets top players (up to 5), their names and score.
     */
    private getTopPlayers(): PlayerSummary[] {
        return this.players
            .sort((a, b) => b.Points - a.Points)
            .slice(0, Math.min(5, this.players.length))
            .map((p) => ({ Name: p.Name, Points: p.Points }));
    }

    private removePlayer(player: Player): void;
    private removePlayer(player: string): void;
    private removePlayer(player: string | Player): void {
        if (typeof player == "string") {
            const playerIntermediate = this.players.find((p) => p.Name == player);
            if (playerIntermediate === undefined) return;
            player = playerIntermediate;
        }
        this.players.splice(this.players.indexOf(player), 1);
        this.questionManager.removePlayer(player);

        const names = this.players.map((p) => p.Name);
        this.players.forEach((p) => p.signalPlayerCountChange(names, this.maxPlayers));
    }

    private checkReady(): void {
        const readyMap: Map<Player, boolean> = new Map();
        this.players.forEach((player) => {
            readyMap.set(player, false);
            player.getSocket().once("readyToPlay", () => {
                readyMap.set(player, true);
                if (Array.from(readyMap.values()).every((b) => b)) {
                    this.log.trace(
                        "GameImpl",
                        `All players ready. Progressing to next round.`
                    );
                    this.progressToNextRound();
                }
            });
        });
    }
}

interface Game {
    getNextQuestion(): Question;

    progressToNextRound(): void;

    addPlayer(name: string, player: Socket): boolean;

    addHostPlayer(name: string, player: Socket): boolean;

    setGameSettings(settings: GameSettings): void;

    isPublic(): boolean;

    isFull(): boolean;

    getHostName(): string;

    getGameInfo(): GameData;

    getPlayerNames(): string[];

    on(event: "gameEnd", listener: () => void): void;

    CurrentPlayers: number;

    Code: string;

    MaxPlayers: number;

    QuestionCount: number;

    TimePerQuestion: number;
}

interface GameData {
    host: string;
    code: string;
    currentPlayers: number;
    maxPlayers: number;
    questions: number;
    timePerQuestion: number;
}

interface GameSettings {
    questions?: number;
    timePerQuestion?: number;
    maxPlayers?: number;
    isGamePublic?: boolean;
}

export { Game, GameImpl, GameSettings };
