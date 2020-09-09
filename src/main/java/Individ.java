import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.nio.ByteBuffer;

@Getter
@Setter
public class Individ {

    private static int ARR_SIZE = 64;
    
    private int arr[] = new int[ARR_SIZE];

    public String str;

    public Individ(double num) {
       str = Long.toBinaryString(Double.doubleToRawLongBits(num));
//       str2Arr(str);
        System.out.println(str.length());
        for(int i = 0 ; i < ARR_SIZE; i++){
            if( str.length() >= i){
                arr[i] = str.charAt(i) - '0';
            }else {
                arr[i] = -99999999;
            }
        }
    }

    public double toDouble(){
        String str = "";
        for(int i = 0 ; i < ARR_SIZE; i++){
            str += arr[i];
        }
        double doubleVal =
                Double.longBitsToDouble(
                        new BigInteger(
                                str, 2
                        ).longValue()
                );
        return doubleVal;
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
