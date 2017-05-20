package mbean;

public interface CommandsCountMBean {
    int getAddsNumber();

    int getUpdatesNumber();

    int getRemovesNumber();

    int getSelectAllNumber();

    String getAverageTime();
}
