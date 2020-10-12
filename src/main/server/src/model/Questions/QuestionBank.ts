import { injectable } from "inversify";
import { Question, QuestionImpl } from "./Question";
import JsonQuestions from "./Questions.json";

@injectable()
class QuestionBankImpl implements QuestionBank {
    private categories;

    constructor() {
        this.categories = JsonQuestions;
    }

    getQuestion(): Question {
        const keys = Object.keys(this.categories);
        const keysLength = keys.length;

        const rndIndex = ~~(Math.random() * keysLength);

        const selectedKey = keys[rndIndex];

        //@ts-ignore
        const questions: Array<{ question: string; prompt: string; solution: string[] }> = this.categories[selectedKey];

        const question = questions[~~(Math.random() * questions.length)];

        return QuestionImpl.create({
            prompt: question.prompt,
            question: question.question,
            solutions: question.solution,
        });
    }
}

interface QuestionBank {
    getQuestion(): Question;
}

export { QuestionBank, QuestionBankImpl };
