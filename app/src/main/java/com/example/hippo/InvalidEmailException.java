package com.example.hippo;

public class InvalidEmailException extends Exception{
    int emailConditionViolated = 0;

    public InvalidEmailException(int conditionViolated){
        super("Invalid Email: ");
        emailConditionViolated = conditionViolated;
    }

    public String printMessage(){
        switch (emailConditionViolated){
            case 1:
                return("Doesn't comply with email format");
            case 2:
                return("Email already in use");
            case 3:
                return("No email typed");
        }
        return "";
    }
}
