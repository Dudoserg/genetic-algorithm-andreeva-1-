import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Individ {

    public static int POW = (int)Math.pow(10, 9);
    public static int ARR_SIZE = 32;

    private double survivalRate;

    private double survivePercent;
    // расстояние до лучшего решения
    private  double distance;

//    private int hromosomeFirst[] = new int[ARR_SIZE];
//    private int hromosomeSecond[] = new int[ARR_SIZE];

    public double x;
    private double y;

    public String hromosomeFirstStr;
    public String hromosomeSecondStr;


    public Individ() {
    }

    public Individ(double num) {
        int main = (int)Math.floor(num);
        int drob = (int)((num - Math.floor(num)) * POW);

        this.hromosomeFirstStr = intToStringBinary(main);
        this.hromosomeSecondStr = intToStringBinary(drob);
    }


    public String intToStringBinary(int num){
        String s = Integer.toBinaryString(num);
        do{
            s = "0" + s;
        }while (s.length() != 32);
        return s;
    }

    public String doubleToStringBinary(double num){
        String tmpStr = "";

        int main = (int)Math.floor(num);
        tmpStr += this.intToStringBinary(main);


        int drob = (int)((num - Math.floor(num)) * Math.pow(10, 9));
        tmpStr += this.intToStringBinary(drob);
        return tmpStr;
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

    private double toNumber(String str) {
        String mainStr = str.substring(0, 32);
        String drobStr = str.substring(32, 64);
        int main = Integer.parseInt(mainStr, 2);
        int drob = Integer.parseInt(drobStr, 2);

        double result = main + drob / Math.pow(10, 9);

        return result;
    }

    public double arrToNumber(){
        String s = arrToString(this.arr);
        return toNumber(s);
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