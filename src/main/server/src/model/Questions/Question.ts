class QuestionImpl implements Question {
    private solution: string[];
    private question: string;
    private prompt: string;

    private constructor($solutions: string[], $question: string, $prompt: string) {
        this.solution = $solutions;
        this.question = $question;
        this.prompt = $prompt;
    }

    getSendableData(): SendableQuestionData {
        return {
            prompt: this.prompt,
            question: this.question,
        };
    }

    get Solution() {
        return this.solution;
    }

    get Question() {
        return this.question;
    }

    get Prompt() {
        return this.prompt;
    }

    static create({ prompt, question, solutions }: QuestionData): Question {
        return new QuestionImpl(solutions, question, prompt);
    }
}

interface Question {
    Solution: string[];
    Question: string;
    Prompt: string;

    getSendableData(): SendableQuestionData;
}

interface QuestionData extends SendableQuestionData {
    solutions: string[];
}

interface SendableQuestionData {
    question: string;
    prompt: string;
}

export { Question, QuestionImpl, SendableQuestionData };
