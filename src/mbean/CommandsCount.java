package mbean;

import net.protocol.MCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandsCount implements CommandsCountMBean {
    private double averageTime = 0;
    private int totalCommands = 0;
    private Map<Byte, Integer> commandCount =
            new HashMap<>();

    public CommandsCount() {
    }

    public void reportCommand(Byte type, int number, double time) {
        commandCount.put(type, commandCount.getOrDefault(type, 0) + number);
        averageTime = (averageTime * totalCommands + time) / (totalCommands + 1);
        totalCommands++;
    }

    @Override
    public int getAddsNumber() {
        return commandCount.getOrDefault(MCommand.Type.ADD, 0);
    }

    @Override
    public int getUpdatesNumber() {
        return commandCount.getOrDefault(MCommand.Type.UPDATE, 0);
    }

    @Override
    public int getRemovesNumber() {
        return commandCount.getOrDefault(MCommand.Type.REMOVE, 0);
    }

    @Override
    public int getSelectAllNumber() {
        return commandCount.getOrDefault(MCommand.Type.SELECT_ALL, 0);
    }

    @Override
    public String getAverageTime() {
        return averageTime + "ms";
    }
}
