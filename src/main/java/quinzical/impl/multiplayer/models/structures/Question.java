package quinzical.impl.multiplayer.models.structures;

public class Question {
    private final String question;
    private final String prompt;

    private String solution;
    private String givenSolution;

    public Question(final String question, final String prompt) {
        this.question = question;
        this.prompt = prompt;
    }

    public final String getQuestion() {
        return question;
    }

    public final String getPrompt() {
        return prompt;
    }

    public final String getGivenSolution() {
        return givenSolution;
    }

    public final void setGivenSolution(final String givenSolution) {
        this.givenSolution = givenSolution;
    }

    public final String getSolution() {
        return solution;
    }

    public final void setSolution(final String solution) {
        this.solution = solution;
    }
}
