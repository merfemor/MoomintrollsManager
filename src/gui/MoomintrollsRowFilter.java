package gui;

import javax.swing.*;

public class MoomintrollsRowFilter extends RowFilter <MoomintrollsTableModel, Object>{

    public static final MoomintrollsRowFilter DEFAULT_FILTER = new MoomintrollsRowFilter(
            "", true, true, null, Integer.MIN_VALUE, Integer.MAX_VALUE
    );

    private String pattern;
    private boolean enableMale, enableFemale;
    private String kindness;
    private int positionFrom, positionTo;

    public MoomintrollsRowFilter(String pattern, boolean enableMale, boolean enableFemale, String kindness, int positionFrom, int positionTo) {
        this.pattern = pattern;
        this.enableMale = enableMale;
        this.enableFemale = enableFemale;
        this.kindness = kindness;
        this.positionFrom = positionFrom;
        this.positionTo = positionTo;
    }

    @Override
    public boolean include(Entry<? extends MoomintrollsTableModel, ?> entry) {
        String name = entry.getStringValue(0);
    if(!pattern.isEmpty() && !name.startsWith(pattern))
            return false;
        boolean isMale = entry.getStringValue(1).equals("male");
        if((isMale && !enableMale) || (!isMale && !enableFemale))
            return false;
        int position = Integer.parseInt(entry.getStringValue(4));
        if(position < positionFrom || position > positionTo)
            return false;
        String kindnessStr = entry.getStringValue(3);
        kindnessStr = kindnessStr.substring(kindnessStr.indexOf(" ["));
        if(kindness.equals(kindnessStr))
            return false;
        return true;
    }
}
