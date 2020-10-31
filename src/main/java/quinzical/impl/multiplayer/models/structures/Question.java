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

    public String getQuestion() {
        return question;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getGivenSolution() {
        return givenSolution;
    }

    public void setGivenSolution(final String givenSolution) {
        this.givenSolution = givenSolution;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(final String solution) {
        this.solution = solution;
    }
}
