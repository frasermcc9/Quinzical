import { inject, injectable } from "inversify";
import { TYPES } from "../bindings/types";
import LogImpl, { Log } from "../helpers/Log";
import { Player } from "./Player";
import { Question } from "./Questions/Question";

@injectable()
class ActiveQuestionManagerImpl implements ActiveQuestionManager {
    private question?: Question;
    private playerMap: Map<Player, boolean> = new Map();

    constructor(@inject(TYPES.Log) private readonly log: Log) {}

    addPlayers(players: Player[]): void {
        players.forEach((player) => {
            this.playerMap.set(player, false);
        });
    }

    removePlayer(player: Player): void {
        this.playerMap.delete(player);
    }

    setNewQuestion(question: Question): void {
        this.question = question;
        this.playerMap.forEach((_v, k) => {
            this.playerMap.set(k, false);
            k.signalNewQuestion(question.getSendableData());
        });
    }

    isAllAnswered(): boolean {
        return Array.from(this.playerMap.values()).every((v) => v);
    }

    answerQuestion(solution: string, player: Player, timeRatio: number): void {
        if (this.question === undefined) throw new ReferenceError("Question is not set");
        if (!this.playerMap.has(player))
            this.log.error("ActiveQuestionManager", `Player in game was not found in player map.`);

        //answer question
        this.playerMap.set(player, true);

        solution = solution.toLowerCase().trim();
        const correct: boolean = this.question.Solution.map((s) => s.toLowerCase().trim()).includes(solution);
        player.increasePoints(this.calculatePoints(timeRatio, correct));

        player.signalCorrectnessOfAnswer(correct);
    }

    get CorrectAnswer(): string {
        if (this.question === undefined) throw new ReferenceError("Question is not set");
        return this.question.Solution[0];
    }

    /**
     * Calculates the bonus points based on how quickly the user answered the
     * question.
     *
     * 500 points is allocated for getting the question correct. A theoretical
     * maximum of 500 points is allocated for speed.
     *
     * @param timeRatio Ratio of timing remaining (i.e. 1 is instant answer, 0
     * is answered just as time ran out).
     */
    private calculatePoints(timeRatio: number, correct: boolean): number {
        const points = ~~(correct ? timeRatio * 500 + 500 : 0);
        this.log.trace(this.constructor.name, `Points calculated to be ${points}`);

        return points;
    }
}

interface ActiveQuestionManager {
    addPlayers(players: Player[]): void;
    setNewQuestion(question: Question): void;
    isAllAnswered(): boolean;
    answerQuestion(solution: string, player: Player, timeRatio: number): void;
    removePlayer(player: Player): void;

    CorrectAnswer: string;
}

export { ActiveQuestionManager, ActiveQuestionManagerImpl };
