package net;

import net.protocol.MCommand;
import net.protocol.MRequest;
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
            command = MRequest.createRemoveRequest(ids);
        } catch (IOException e) {
            fail();
            return;
        }


        long[] ids2 = {};
        try {
            ids2 = MRequest.parseRemoveRequest(command);
        } catch (IOException | ClassNotFoundException e) {
            fail();
        }

        assertTrue(Arrays.equals(ids, ids2));
    }

    @Test
    public void testPackingUnpacking() {
        long[] l = {1, -34, 16};
        try {
            MCommand command = MRequest.createRemoveRequest(l);
            long[] result = MRequest.parseRemoveRequest(MCommand.fromPackets(command.toPackets()));
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
            command = MRequest.createAddRequest(moomintrolls);
            Moomintroll[] moomintrolls1 = MRequest.parseAddRequest(command);
            assertEquals(moomintrolls.length, moomintrolls1.length);
            for (int i = 0; i < moomintrolls.length; i++) {
                if (!moomintrolls[i].equals(moomintrolls1[i]))
                    fail();
            }
        } catch (IOException | ClassNotFoundException e) {
            fail();
        }
    }

    @Test
    public void testSerializingUpdate() {
        Moomintroll moomintroll = trolls.utils.Random.randomTroll();
        Moomintroll m = new Moomintroll(moomintroll.getName(), moomintroll.isMale(), moomintroll.getPosition(), moomintroll.getRgbBodyColor(), moomintroll.getKindness(), moomintroll.getCreationDateTime());
        m.setId((long) 248);
        Moomintroll im = m;

        try {
            MCommand command = MRequest.createUpdateRequest(im);
            Moomintroll im1 = MRequest.parseUpdateRequest(command);
            assertEquals(im.getId(), im1.getId());
            assertTrue(im.equals(im1));
        } catch (IOException | ClassNotFoundException e) {
            fail();
        }
    }
}