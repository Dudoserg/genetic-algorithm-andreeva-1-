import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Individ {

    public static int POW = (int) Math.pow(10, 9);
    public static int ARR_SIZE = 32;

    private double survivalRate;

    private double survivePercent;
    // расстояние до лучшего решения
    private double distance;

//    private int hromosomeFirst[] = new int[ARR_SIZE];
//    private int hromosomeSecond[] = new int[ARR_SIZE];


    public double x;
    private double y;

    public String hromosomeFirstStr;
    public String hromosomeSecondStr;


    public Individ() {
    }

    public Individ(double num) {
        setXAndHromosome(num);
    }

    // задаем
    public void setHromosom(double num) {
        int main = (int) Math.floor(num);
        int drob = (int) ((num - Math.floor(num)) * POW);

        this.hromosomeFirstStr = intToStringBinary(main);
        this.hromosomeSecondStr = intToStringBinary(drob);
    }

    private String intToStringBinary(int num) {
        String s = Integer.toBinaryString(num);
        do {
            s = "0" + s;
        } while (s.length() != ARR_SIZE);
        return s;
    }

    public void setXAndHromosome(double x) {
        this.x = x;
        this.setHromosom(x);
    }

    public double getX_FromHromosome() {
        int main = Integer.parseInt(this.hromosomeFirstStr, 2);
        int drob = Integer.parseInt(this.hromosomeSecondStr, 2);
        double result = main + (double) drob / POW;
        return result;
    }

    public static String setCharByIndex(String str, char c, int index) {
        String tmpStr = str.substring(0, index)
                + c
                + str.substring(index + 1);
        return tmpStr;
    }


}