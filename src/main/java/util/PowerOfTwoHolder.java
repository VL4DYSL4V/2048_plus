package util;

import enums.FieldDimension;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public final class PowerOfTwoHolder {

    private PowerOfTwoHolder(){}

    private static final Map<Integer, BigInteger> HOLDER = new HashMap<>();

    static {
        initialize();
    }

    private static void initialize(){
        int maxPowerNeeded = 0;
        for(FieldDimension dimension : FieldDimension.values()){
            int product = dimension.getWidth() * dimension.getHeight();
            if(product > maxPowerNeeded){
                maxPowerNeeded = product;
            }
        }
        maxPowerNeeded +=1;
        BigInteger last = BigInteger.ONE;
        for(int i = 0; i <= maxPowerNeeded; i++){
            HOLDER.put(i, last);
            last = last.add(last);
        }
    }

    public static BigInteger get(int power){
        return HOLDER.get(power);
    }

}
