package trolls;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Moomintroll extends Wight implements BowTo, Emotionable, Comparable <Moomintroll> {

    private enum TongueCondition {
        normal, paralyzed
    }
    public enum Action {
        bow, movement, generalBow, handshake, hug
    }
    protected class ActionLog {
        private Vector<Action> log = new Vector <>();
        public void push(Action action) {
            this.log.add(action);
        }
        public int countAction(Action actionToCount) {
            int answer = 0;
            for(Action action: log) {
                if(action == actionToCount) {
                    answer++;
                }
            }
            return answer;
        }
    }
    public transient ActionLog actionLog;
    private transient TongueCondition tongueCondition;
    private transient Emotion emotionalCondition;
    private Kindness kindness;
    private Map<Action, Integer> maxDistance;

    public Moomintroll(String name, boolean isMale, int position, Wight.BodyColor bodyColor) {
        super(name, isMale, position, bodyColor);
        actionLog = new ActionLog();
        this.emotionalCondition =  Emotion.normal;
        this.tongueCondition =  TongueCondition.normal;
        maxDistance = new HashMap<>();
        maxDistance.put(Action.bow, 1);
        maxDistance.put(Action.handshake, 0);
        maxDistance.put(Action.hug, 0);
        this.kindness = Kindness.NORMAL;
    }

    public void stepForward() {
        this.position++;
        System.out.println(this.name + " выступил(-а) вперед");
        this.actionLog.push(Action.movement);
    }

    public void stepBack() {
        this.position--;
        System.out.println(this.name + " отступил(-а) назад");
        this.actionLog.push(Action.movement);
    }

    public Emotion getEmotionalCondition() {
        return emotionalCondition;
    }

    public void setEmotionalCondition(Emotion emotionalCondition) {
        if(this.emotionalCondition == Emotion.embarrassment && emotionalCondition == Emotion.embarrassment) {
            emotionalCondition = Emotion.bigEmbrassment;
        }
        // когда Тролль-мальчик чему-то сильно удивляется, у него отнимается язык
        if(Moomintroll.this.isMale && emotionalCondition == Emotion.wonder &&
                Moomintroll.this.tongueCondition != TongueCondition.paralyzed) {
            Moomintroll.this.tongueCondition = TongueCondition.paralyzed;
            System.out.println("У " + Moomintroll.this.name + " язык отнялся от неожиданности");
        }
        // Тролли-мальчики при смущении краснеют
        if(Moomintroll.this.isMale && emotionalCondition == Emotion.embarrassment) {
            setBodyColor(Wight.BodyColor.redAsLobster);
        }
        // после того как проверили все исключительне ситуации, устанавливаем новое настроение
        this.emotionalCondition = emotionalCondition;
        System.out.println(name + " " + this.emotionalCondition.toString());
    }

    public Kindness getKindness() {
        return kindness;
    }

    public void setKindness(Kindness kindness) {
        this.kindness = kindness;
    }

    @Override
    public void bowTo(Wight wight) throws SelfBowException {
        if(!(wight instanceof Emotionable)) { // если существо не эмоционально
            System.out.println(wight.getName() + " игнорирует " + this.getName());
            return;
        }
        if(this == wight) { // если нужно поздороваться с самим собой
            throw new SelfBowException(this);
        }
        if(this.isMale != wight.isMale) { // если нужно поклониться противоположному полу
            this.setEmotionalCondition(Emotion.embarrassment); // тролль смущается
        }
        if(this.isMale == wight.isMale) {
            this.setEmotionalCondition(Emotion.friendliness);
            moveTo(wight.getPosition(), maxDistance.get((this.isMale)? Action.handshake : Action.hug));
            System.out.println(this.name + " и " + wight.name + (this.isMale? " пожали друг другу руки": " обнялись"));
            actionLog.push(this.isMale? Action.handshake: Action.hug);
        }
        else { // противоположный пол
            moveTo(wight.getPosition(), maxDistance.get(Action.bow)); // подходит на расстояние достатоное для поклона
            System.out.println(this.name + (this.isMale? " отвесил низкий поклон": " сделала книксен"));
            this.actionLog.push(Action.bow);
            if(!wight.isMale) {
                ((Emotionable) wight).setEmotionalCondition(Emotion.embarrassment);
            }
        }
    }

    @Override
    public void bowTo(Set<Wight> wights) throws SelfBowException {
        int numberOfMen= 0, numberOfWomen = 0;
        for(Wight wight: wights) {
            if (wight.isMale()) {
                numberOfMen++;
            } else {
                numberOfWomen++;
            }
        }
        if(numberOfMen != 0 && numberOfWomen != 0) { // если есть оба пола
            this.setEmotionalCondition(Emotion.friendliness);
            String sentence = this.name + " сделал(-а) общий поклон ";
            for(Wight wight: wights) {
                sentence += (wight.name + ", ");
            }
            System.out.println(sentence.substring(0, sentence.length() - 2));
            this.actionLog.push(Action.generalBow);
        }
        else { // иначе приветствует каждого отдельно
            for(Wight wight: wights) {
                this.bowTo(wight);
            }
        }
    }

    @Override
    public void moveTo(int position, int maxDistance) {
        while (Math.abs(this.position - position) > maxDistance) {
            if(this.position > position) {
                stepBack();
            }
            else {
                stepForward();
            }
        }
    }

    @Override
    public int compareTo(Moomintroll moomintroll) {
        if(moomintroll == null) {
            return -1;
        }
        return moomintroll.position - this.position;
    }

    @Override
    public String toString() {
        return "Moomintroll{" + position + '}';
    }
}