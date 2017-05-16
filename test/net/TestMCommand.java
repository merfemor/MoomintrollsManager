package net;

import net.protocol.MCommand;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestMCommand {

    @Test
    public void testSerializing() {
        long[] ids = {1, 80, -3};
        MCommand command;
        try {
            command = MCommand.createRemoveCommand(ids);
        } catch (IOException e) {
            fail();
            return;
        }


        long[] ids2 = {};
        try {
            ids2 = MCommand.parseRemoveCommand(command);
        } catch (IOException e) {
            fail();
        } catch (ClassNotFoundException e) {
            fail();
        }

        assertTrue(Arrays.equals(ids, ids2));
    }

    @Test
    public void testPackingUnpacking() {
        long[] l = {1, -34, 16};
        try {
            MCommand command = MCommand.createRemoveCommand(l);
            long[] result = MCommand.parseRemoveCommand(MCommand.fromPackets(command.toPackets()));
            assertTrue(Arrays.equals(l, result));
        } catch (Exception e) {
            fail();
        }
    }
}