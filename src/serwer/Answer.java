package serwer;

import java.io.Serializable;

public class Answer implements Serializable {
    private String answer;
    private String nick;

    public Answer(String answer, String nick) {
        this.answer=answer;
        this.nick=nick;
    }

    public String getAnswer() {
        return this.answer;
    }

    public String getNick() {
        return this.nick;
    }
}