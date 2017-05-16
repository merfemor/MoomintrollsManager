package net;

import net.protocol.MCommand;
import org.junit.Test;
import trolls.Moomintroll;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestMCommand {

    @Test
    public void testSerializingRemove() {
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
        } catch (IOException | ClassNotFoundException e) {
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

    @Test
    public void testSerializingAdd() {
        Moomintroll[] moomintrolls = new Moomintroll[2];
        for (int i = 0; i < moomintrolls.length; i++) {
            moomintrolls[i] = trolls.utils.Random.randomTroll();
        }

        MCommand command;
        try {
            command = MCommand.createAddCommand(moomintrolls);
            Moomintroll[] moomintrolls1 = MCommand.parseAddCommand(command);
            assertEquals(moomintrolls.length, moomintrolls1.length);
            for (int i = 0; i < moomintrolls.length; i++) {
                if (!moomintrolls[i].equals(moomintrolls1[i]))
                    fail();
            }
        } catch (IOException | ClassNotFoundException e) {
            fail();
        }
    }
}