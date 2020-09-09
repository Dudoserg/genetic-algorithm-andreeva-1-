import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Individ {

    public static int ARR_SIZE = 32;

    private double func;
    private double survivalRate;

    private double survivePercent;
    // расстояние до лучшего решения
    private  double distance;

    private int arr[] = new int[ARR_SIZE];
    public int x;
    public String str;


    public Individ() {
    }

    public Individ(int num) {
        this.str = this.intToStringBinary(num);
        this.arr = this.stringToArr(str);
    }

    public String intToStringBinary(int num){
        return Integer.toBinaryString(num);
    }

    public int[] stringToArr(String str) {
        int[] arr = new int[ARR_SIZE];
        for (int i = 0 + ARR_SIZE - str.length(); i < ARR_SIZE; i++) {
            arr[i] = str.charAt(i - (ARR_SIZE - str.length())) - '0';
        }
        return arr;
    }

    private String arrToString(int [] arr){
        String str = "";
        for (int i = 0; i < ARR_SIZE; i++) {
            str += arr[i];
        }
        return str;
    }

    private int toInteger(String str) {
        return Integer.parseInt(str, 2);
    }

    public int arrToInteger(){
        return toInteger(arrToString(this.arr));
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
}
