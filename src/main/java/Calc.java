import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;



public class Calc {

    public static double START = 0;
    public static double END = 35;
    public static int POPULATION_SIZE = 10;

    public Calc() {
    }

    public void start() throws Exception {
        Individ individ = new Individ(124.2391882469336637);

        System.out.println(Long.toBinaryString(Double.doubleToRawLongBits(0.2391882469336637)));


        System.out.println(doubleVal);



//        final List<Individ> individs = generatePopulation(POPULATION_SIZE);
//        System.out.println();
//        for (Individ individ : individs) {
//            System.out.println(individ.arr2Double(individ.getArr()));
//        }
    }



    // generate population
    public List<Individ> generatePopulation(int size) {
        List<Individ> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final double random = generateRandom(START, END);
            System.out.println(random);
            Individ individ = new Individ(random);
            population.add(individ);
        }
        return population;
    }

    public double generateRandom(double from, double to) {
        double random_number = from + (Math.random() * to);
        return random_number;
    }


 /*   public double func(double x) {
        return (0.8 * Math.cos(3 * x) + Math.cos(x)) * (x - 4);
    }

    class MyClass {
        double x;
        double y;

        public MyClass(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public void start() {
        List<MyClass> list = new LinkedList<MyClass>();
        double step = 0.0001;
        MyClass modul = new MyClass(-1, -1);
        for (double x = 0; x <= 35; x += step) {
            list.add(new MyClass(x, func(x)));
            if (Math.abs(list.get(list.size() - 1).y) > Math.abs(modul.y))
                modul = list.get(list.size() - 1);
        }


        System.out.println("x = " + modul.x + "\t" + "y = " + modul.y);
        System.out.println("x = " + 35 + "\t" + "y = " + func(35));
    }*/
}
