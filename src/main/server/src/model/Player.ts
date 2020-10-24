import {Socket} from "socket.io";
import {SendableQuestionData} from "./Questions/Question";

class PlayerImpl implements Player {
    private points: number = 0;

    constructor(private readonly name: string, private readonly client: Socket) {}

    signalGameOver(winners: PlayerSummary[]): void {
        this.client.emit("gameFinished", winners);
    }

    signalRoundOver(solution: string, playerPoints: number, topPlayers: PlayerSummary[]): void {
        this.client.emit("roundOver", solution, playerPoints, topPlayers);
    }

    signalPlayerCountChange(players: string[], max: number): void {
        this.client.emit("playersChange", players, max);
    }

    signalNewQuestion(question: SendableQuestionData): void {
        this.client.emit("newQuestion", question.question, question.prompt);
    }

    signalGameStart(): void {
        this.client.emit("gameStart");
    }

    signalCorrectnessOfAnswer(correct: boolean): void {
        this.client.emit("answerResult", correct);
    }

    get Name(): string {
        return this.name;
    }

    get Points(): number {
        return this.points;
    }

    getSocket() {
        return this.client;
    }

    increasePoints(pointsToAdd: number): void {
        this.points += pointsToAdd;
    }
}

interface PlayerConstructor {
    new (name: string, client: Socket): PlayerImpl;
}

interface Player extends PlayerSummary {
    signalNewQuestion(question: SendableQuestionData): void;

    signalRoundOver(solution: string, playerPoints: number, topPlayers: PlayerSummary[]): void;

    signalGameOver(winners: PlayerSummary[]): void;

    signalPlayerCountChange(players: string[], max: number): void;

    signalCorrectnessOfAnswer(correct: boolean): void;

    signalGameStart(): void;

    getSocket(): Socket;

    increasePoints(pointsToAdd: number): void;
}

interface PlayerSummary {
    Name: string;

    Points: number;
}

interface PlayerFactory extends Function {
    (name: string, client: Socket): Player;
}

export { PlayerConstructor, PlayerImpl, Player, PlayerFactory, PlayerSummary };
