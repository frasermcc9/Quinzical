package quinzical.impl.constants;

public enum Attempts {
    ATTEMPT_1("Attempt 1/3"),
    ATTEMPT_2("Attempt 2/3"), 
    ATTEMPT_3("Attempt 3/3");
    
    private String message;
    
    private Attempts(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return message;
    }
}
