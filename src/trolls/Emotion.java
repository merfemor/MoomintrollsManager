package trolls;

public enum Emotion {
    wonder,
    normal,
    embarrassment,
    bigEmbrassment,
    friendliness,
    loneliness;

    @Override
    public String toString() {
        switch (this){
            case loneliness:
                return "чувствует себя одиноко";
            case friendliness:
                return "дружелюбен(-на)";
            case bigEmbrassment:
                return "вне себя от смущения";
            case wonder:
                return "удивлен(-а)";
            case embarrassment:
                return "смущен(-а)";
            default:
                return "не понятен(-на)";
        }
    }
}