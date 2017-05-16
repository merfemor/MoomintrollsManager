package net.server;

import net.protocol.MAnswer;

@FunctionalInterface
interface AnswerHandler {
    void handleAnswer(MAnswer answer);
}
