package exceptions;

public class LyraFunctionNotFoundException extends LyraException{
    public LyraFunctionNotFoundException(String name) {
        super("Fonction introuvable" + name);
    }

}
