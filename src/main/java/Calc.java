import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.*;


public class Calc {

    public double MNOGITEL = 1.01;

    public static int START = 0;
    public static int END = 35;
    public static int POPULATION_SIZE = 10;
    public static int N = 5;
    public static int COUNT_STEP = 1000;
    public List<Double> values = new ArrayList<>();
    double needX = 34.56;

    public Function<Double, Double> function = new Function<Double, Double>() {
        @Override
        public Double apply(Double x) {
            return (x - 3) * (x - 2) * (x - 2.5) * cos(x);
//            return (0.8 * cos(0.5 * x) + cos(x)) * (x - 4);
        }
    };
    private int countIteration;

    public Calc() {
        double log = log(COUNT_STEP) / log(2);
        Individ.ARR_SIZE = (int) Math.ceil(log);
        Individ.values = this.values;

        double stepSize = (END - START) / (double) COUNT_STEP;
        for (double x = START; x < stepSize * Math.pow(2, Individ.ARR_SIZE); x += stepSize) {
            values.add(x);
        }

        Double max_y = 0.0;
        Double max_x = 0.0;
        for (Double value : values) {
            if (value > END)
                break;
            double tmp_y = function.apply(value);
            if (abs(tmp_y) > abs(max_y)) {
                max_x = value;
                max_y = tmp_y;
            }
            //System.out.println("x = " + value + "\t y = " + tmp_y);
        }
        System.out.println("====MAX======");
        System.out.println("x = " + max_x + "\t y = " + max_y);
        needX = max_x;
        System.out.println();
    }

//    public double func(double x) {
//        return (0.8 * cos(3 * x) + cos(x)) * (x - 4);
////        return 3 * ((x + 23) * (x + 23)) + 150;
//    }

    public void start() throws Exception {
        List<Individ> list = generatePopulation(POPULATION_SIZE);


        // Считаем у всех функцию
        do {
            double maxFunc = 0;

            for (Individ individ : list) {
                final double x = individ.getX();
                final double func = function.apply(x);
                individ.setFunc(func);
                if (abs(func) > abs(maxFunc)) {
                    maxFunc = func;
                }
            }
            System.out.println("maxFunc = " + maxFunc);
            System.out.println("maxFunc *" + MNOGITEL + " = " + maxFunc * MNOGITEL);
            // Считаем коэффициент выживаемости
            for (int i = 0; i < list.size(); i++) {
                Individ individ = list.get(i);
                final double func = individ.getFunc();
                final double distance = abs(maxFunc * MNOGITEL - func);
                individ.setDistance(distance);
            }
            // считаем сумму обратных коэффициентов
            double sum = 0.0;
            for (Individ individ : list) {
                sum += 1.0 / individ.getDistance();
            }
            // Считаем коэффициент выживаемости в процентах
            double sumV = 0.0;
            System.out.println("===========================");
            for (int i = 0; i < list.size(); i++) {
                Individ individ = list.get(i);
                final double v = (1.0 / individ.getDistance()) / sum;
                individ.setSurvivePercent(v);
                sumV += v;
                System.out.println("individ(" + i + ") " +
                        "x = " + individ.getX() +
                        "\tfunc = " + individ.getFunc() +
                        "\tdist = " + individ.getDistance() +
                        "\t" + v + "%");

            }

            // Выбираем пары для скрещивания
            final List<MyPair<Individ, Individ>> pairs = createPair(list);

            // Получаем потомство
            List<Individ> childrens = new ArrayList<>();
            for (MyPair<Individ, Individ> pair : pairs) {
                final Individ child = crossbreeding(pair, N);
                childrens.add(child);
            }

            list.addAll(childrens);

            // Мутируем некоторые особи
            for (Individ individ : list) {
                if (Math.random() > 0.55) {
                    // мутации подвергается только 33 процента
//                    mutation(individ);
                    int random = generateRandom(0, COUNT_STEP);
                    individ.CHANGE_X(random);
                    individ.getX();

                    recalcValues(individ);
                    System.out.print("");
                }
            }
            System.out.println("////////////////////////mutation://////////////////////////////////////////////");
            for (int i = 0; i < list.size(); i++) {
                Individ individ = list.get(i);
                System.out.println("individ(" + i + ") " +
                        "x = " + individ.getX() +
                        "\tfunc = " + individ.getFunc() +
                        "\tdist = " + individ.getDistance() +
                        "\t" + individ.getSurvivePercent() + "%");

            }
            System.out.println("//////////////////////////////////////////////////////////////////////");
            // СЕЛЕКЦИЯ
            for (int i = list.size() - 1; i >= 0; i--) {
                Individ individ = list.get(i);
                if (individ.getX() < START || individ.getX() > END)
                    list.remove(individ);
            }
            System.out.println("list.size() = " + list.size());
            list = selection(list);

            System.out.println("////////////////////////selection://////////////////////////////////////////////");
            for (int i = 0; i < list.size(); i++) {
                Individ individ = list.get(i);
                System.out.println("individ(" + i + ") " +
                        "x = " + individ.getX() +
                        "\tfunc = " + individ.getFunc() +
                        "\tdist = " + individ.getDistance() +
                        "\t" + individ.getSurvivePercent() + "%");

            }
            System.out.println("//////////////////////////////////////////////////////////////////////");

            countIteration++;
            int countCompare = 0;
            for (int i = 0; i < list.size(); i++) {
                Individ individ = list.get(i);
                double x = individ.getX();
                if (Math.abs(needX - x) < 0.01)
                    countCompare++;
            }
            if (countCompare > list.size() / 2)
                break;

        } while (true);

        System.out.println("============================================================");
        System.out.println(countIteration++);
    }

    public void recalcValues(Individ individ) {
        final double x = individ.getX();
        individ.setFunc(function.apply(x));
    }

    // Селекция
    public List<Individ> selection(List<Individ> list) {
        // RANDOM
       /* List<Individ> result = new ArrayList<>();
        do {
            final int r = this.generateRandom(0, list.size());
            result.add(list.remove(r));
        } while (result.size() != POPULATION_SIZE);
        return result;*/
        list.forEach(individ -> {
            individ.setFunc(function.apply(individ.getX()));
        });
        // BEST
        List<Individ> tmp = new ArrayList<>(list);
        tmp.sort((first, second) -> abs(first.getFunc()) >= abs(second.getFunc()) ? -1 : 1);
        List<Individ> result = tmp.subList(0, tmp.size() > 10 ? 10 : tmp.size());
        return result;

    }

    // Мутация
    public void mutation(Individ individ) {
        individ.getX();
        // реверс
        int random = generateRandom(0, Individ.ARR_SIZE);
        individ.setStr(changeCharacter(individ.getStr(), random));
        individ.getX();

        individ.getX();
        // реверс
        int random2 = generateRandom(0, Individ.ARR_SIZE);
        individ.setStr(changeCharacter(individ.getStr(), random2));
        individ.getX();
        System.out.print("");
    }

    private String changeCharacter(String s, int index) {
        StringBuilder str = new StringBuilder(s);
        str.setCharAt(index, str.charAt(index) == '0' ? '1' : '0');
        return str.toString();
    }

    private String setByIndex(String s, Character character, int index) {
        StringBuilder str = new StringBuilder(s);
        str.setCharAt(index, character);
        return str.toString();
    }

    /// Скрещивание
    public Individ crossbreeding(MyPair<Individ, Individ> pair, int N) {
        Individ individ_first = new Individ();
        Individ individ_second = new Individ();

        int areaSize = Individ.ARR_SIZE / N;

        int iteration = 0;
        String[] first_arr = splitByNumber(pair.getFirst().getStr(), pair.getFirst().getStr().length() / N);
        String[] second_arr = splitByNumber(pair.getSecond().getStr(), pair.getSecond().getStr().length() / N);
        StringBuilder builder_first = new StringBuilder("");
        StringBuilder builder_second = new StringBuilder("");
        for (int i = 0; i < first_arr.length; i++) {
            if (iteration % 2 == 0) {
                builder_first.append(first_arr[i]);
                builder_second.append(second_arr[i]);
            } else {
                builder_first.append(second_arr[i]);
                builder_second.append(first_arr[i]);
            }
            iteration++;
        }
        individ_first.setStr(builder_first.toString());
        individ_second.setStr(builder_second.toString());

        individ_first.setFunc(function.apply(individ_first.getX()));
        individ_second.setFunc(function.apply(individ_second.getX()));

        if (abs(individ_first.getFunc()) > abs(individ_second.getFunc()))
            return individ_first;
        else
            return individ_second;
    }

    // разбиваем строку на части
    private static String[] splitByNumber(String str, int size) {
        return (size < 1 || str == null) ? null : str.split("(?<=\\G.{" + size + "})");
    }

    // Создаем пары родителей( у наиболее сильной особи, шанс размножиться больше)
    public List<MyPair<Individ, Individ>> createPair(List<Individ> list) {
        List<MyPair<Individ, Individ>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final double random = Math.random();
            Individ first = null;
            do {
                first = getIndividInLineWithCoeff(list, random);
            } while (first.getX() < START || first.getX() > END);
            Individ second = null;
            do {
                second = getIndividInLineWithCoeff(list, Math.random());
            } while (first == second || (second.getX() < START || second.getX() > END));
            result.add(new MyPair<>(first, second));
        }
        return result;
    }

    // Получаем из всех списка особей одну, с заданным коэффициентом
    // (если представить отложении на отрезке, то у наиболее сильной особи, отрезок будет больше)
    public Individ getIndividInLineWithCoeff(List<Individ> list, double coeff) {
        double sum = 0.0;
        for (Individ individ : list) {
            sum += individ.getSurvivePercent();
            if (coeff < sum)
                return individ;
        }
        return null;
    }


    // generate population
    public List<Individ> generatePopulation(int size) {
        List<Individ> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final int random = generateRandom(0, COUNT_STEP);
            System.out.println("random = " + random);
            Individ individ = new Individ(random);
            individ.getX();
            population.add(individ);
            System.out.println("real   = " + individ.getX());
//            if (abs(individ.arrToInteger() - random) > 0.000000001)
//                System.out.println("error");
//            System.out.println();
        }
        return population;
    }

//    public double generateRandom(double from, double to) {
//        double random_number = from + (Math.random() * to);
//        return random_number;
//    }

    public int generateRandom(int from, int to) {
        int random_number = from + (int) (Math.random() * (to - from));
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
