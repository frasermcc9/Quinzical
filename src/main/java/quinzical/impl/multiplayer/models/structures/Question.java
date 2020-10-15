package quinzical.impl.multiplayer.models.structures;

public class Question {
    private final String question;
    private final String prompt;

    private String solution;
    private String givenSolution;

    public Question(String question, String prompt) {
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

    public void setGivenSolution(String givenSolution) {
        this.givenSolution = givenSolution;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
