package util;

import enums.FieldDimension;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class PowerOfTwoHolderTest {

    @Test
    void get() {
        int maxPowerNeeded = 0;
        for (FieldDimension dimension : FieldDimension.values()) {
            int product = dimension.getWidth() * dimension.getHeight();
            if (product > maxPowerNeeded) {
                maxPowerNeeded = product;
            }
        }
        maxPowerNeeded += 1;
        BigInteger two = new BigInteger("2");
        for (int i = 0; i <= maxPowerNeeded; i++) {
           assertEquals(two.pow(i), PowerOfTwoHolder.get(i));
        }
    }
}