import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class Individ {

    public static int ARR_SIZE = 32;
    public static List<Double> values;

    private double func;
    private double x;
    private double survivalRate;

    private double survivePercent;
    // расстояние до лучшего решения
    private  double distance;

    public String str;

    public Individ() {
    }

    public double getX(){
        int i = this.toInteger(str);
        x = values.get(i);
        return x;
    }



    public Individ(int num) {
        this.str = this.intToStringBinary(num);
    }

    public String intToStringBinary(int num){
        StringBuilder s = new StringBuilder(Integer.toBinaryString(num));
        while (s.length() < ARR_SIZE)
            s.insert(0, '0');
        return s.toString();
    }

    private int toInteger(String str) {
        return Integer.parseInt(str, 2);
    }






//    private int[] str2Arr(String str){
//
//    }

    
   /* public int[] double2Arr(double d){
        String sResult = "";
        long numberBits = Double.doubleToLongBits(d);
        sResult = Long.toBinaryString(numberBits);
        String str = d > 0 ? "0" + sResult : sResult;
        System.out.println("str = " + str);
        int arr[] = new int[ARR_SIZE];
        for(int i = 0 ; i < str.length(); i++){
            arr[i] = (int)(str.charAt(i) - '0');
        }
        this.str = str;
        return arr;
    }

    public double arr2Double (int[] arr){
        String str = "";
        for (int i : arr) {
            str += Integer.toString(i);
        }
        return Double.longBitsToDouble(new BigInteger(str, 2).longValue());
    }

    public static  double ieeeToFloat(String binString) throws Exception {
        binString = binString.replace(" ", "");
        *//* 32-bit *//*
        if (binString.length() == 32) {
            return Float.intBitsToFloat(Integer.parseUnsignedInt(binString, 2));
        }
        *//* 64-bit *//*
        else if (binString.length() == 64) {
            return Double.longBitsToDouble(Long.parseUnsignedLong(binString, 2));
        }
        *//* An exception thrown for mismatched strings *//*
        else {
            throw new Exception("Does not represent internal bits of a floating-point number");
        }
    }*/

    public void CHANGE_X(int x){
        this.str = this.intToStringBinary(x);
        this.getX();
    }
}
