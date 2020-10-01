import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;


public class Calc {

    public double MNOGITEL = 1.20;

    public static int START = 0;
    public static int END = 36;
    public static int POPULATION_SIZE = 10;
    public static int N = 16;

    public Calc() {
        MyPair<Double, Double> max = new MyPair<>(0.0, 0.0);
        for (double x = START; x <= END; x += 0.0001) {
            double y = func(x);
            if (Math.abs(y) > Math.abs(max.getSecond())) {
                max.setFirst(x);
                max.setSecond(y);
            }
        }
        System.out.println("MAX VALUE: " + "x = " + max.getFirst() + "\t" + "y = " + max.getSecond());
        System.out.println("\n\n\n\n");
    }

    public double func(double x) {
        return (0.8 * cos(3 * x) + cos(x)) * (x - 4);
//        return 3 * ((x) * (x )) + 150;
//        return -0.3 * ((x - 16.5) * (x - 16.5)) + 150;
    }

    public void start() throws Exception {
        List<Individ> list = generatePopulation(POPULATION_SIZE);
        System.out.println("\n\n\n\n");
        do {
            for (int z = 0; z < 10000; z++) {
                double maxFunc = 0;

                for (Individ individ : list) {
                    final double x = individ.getX();
                    individ.setXAndHromosome(x);
                    final double y = func(x);
                    individ.setY(y);
                    if (abs(y) > abs(maxFunc)) {
                        maxFunc = y;
                    }
                }
                System.out.println("maxFunc = " + maxFunc);
                System.out.println("maxFunc *" + MNOGITEL + " = " + maxFunc * MNOGITEL);
                // Считаем коэффициент выживаемости
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    final double func = individ.getY();
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
                            "\tfunc = " + individ.getY() +
                            "\tdist = " + individ.getDistance() +
                            "\t" + v + "%");

                }
                assert  Math.abs(sumV - 1.0) < 0.0000001;
                // Выбираем пары для скрещивания
                final List<MyPair<Individ, Individ>> pairs = createPair(list);

                // Получаем потомство
                List<Individ> childrens = new ArrayList<>();
                for (MyPair<Individ, Individ> pair : pairs) {
                    final Individ child = crossbreeding(pair, N);
                    childrens.add(child);
                }

                list.addAll(childrens);
                System.out.println("///////////////////////////ДО МУТАЦИИ///////////////////////////////////////////");
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    System.out.println("individ(" + i + ") " +
                            "x = " + individ.getX() +
                            "\tfunc = " + individ.getY() +
                            "\tdist = " + individ.getDistance() +
                            "\t" + individ.getSurvivePercent() + "%");

                }
                System.out.println("//////////////////////////////////////////////////////////////////////");
                // Мутируем некоторые особи
                for (Individ individ : list) {
                    if (Math.random() > 0.75) {
                        mutation(individ);
//                        individ.setX(individ.getX() + generateX());
//                        individ.setArr(
//                                individ.stringToArr(
//                                        individ.doubleToStringBinary(
//                                                individ.getX()
//                                        )
//                                )
//                        );
                        recalc_X_Y(individ);
                        System.out.print("");
                    }
                }
                System.out.println("//////////////////////Мутация, до селекции////////////////////////////////////////////////");
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    System.out.println("individ(" + i + ") " +
                            "x = " + individ.getX() +
                            "\tfunc = " + individ.getY() +
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

                System.out.println("//////////////////////ПОСЛЕ МУТАЦИИ И СЕЛЕКЦИИ////////////////////////////////////////////////");
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    System.out.println("individ(" + i + ") " +
                            "x = " + individ.getX() +
                            "\tfunc = " + individ.getY() +
                            "\tdist = " + individ.getDistance() +
                            "\t" + individ.getSurvivePercent() + "%");

                }
                System.out.println("//////////////////////////////////////////////////////////////////////");

            }
            System.out.println();
        } while (true);

    }

    public void recalc_X_Y(Individ individ) {
        double x = individ.getX_FromHromosome();
        individ.setX(x);
        individ.setY(func(x));
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

        // BEST
        List<Individ> tmp = new ArrayList<>(list);
        tmp.sort((first, second) -> first.getY() >= second.getY() ? -1 : 1);
        List<Individ> result = tmp.subList(0, tmp.size() > 10 ? 10 : tmp.size());
        return result;

    }

    // Мутация
    public void mutation(Individ individ) {
//        // реверс
//        int start = generateRandom(1, Individ.ARR_SIZE - 2);
//        int end = generateRandom(start, Individ.ARR_SIZE - 1);
//        for (int i = start; i < (end - start) / 2 + start; i++) {
//            int tmp = individ.getArr()[i];
//            individ.getArr()[i] = individ.getArr()[individ.getArr().length - i - 1];
//            individ.getArr()[individ.getArr().length - i - 1] = tmp;
//        }

        // cлучайная мутация  5 байт
        int countSwap = 3;
        for (int i = 0; i < countSwap; i++) {
            int random = 0;
            do {
                random = generateRandom(0, Individ.ARR_SIZE);
            } while (random <= 0 || random >= 32);
            String tmpStr = individ.getHromosomeFirstStr().substring(0, random)
                    + (individ.getHromosomeFirstStr().charAt(random) == '0' ? '1' : '0')
                    + individ.getHromosomeFirstStr().substring(random + 1);
            individ.setHromosomeFirstStr(tmpStr);
        }
        for (int i = 0; i < countSwap; i++) {
            int random = 0;
            do {
                random = generateRandom(0, Individ.ARR_SIZE);
            } while (random <= 0 || random >= 32);
            String tmpStr = individ.getHromosomeSecondStr().substring(0, random)
                    + (individ.getHromosomeSecondStr().charAt(random) == '0' ? '1' : '0')
                    + individ.getHromosomeSecondStr().substring(random + 1);
            individ.setHromosomeSecondStr(tmpStr);
        }
        recalc_X_Y(individ);
    }

    /// Скрещивание
    public Individ crossbreeding(MyPair<Individ, Individ> pair, int N) throws Exception {
        Individ individ_first = new Individ(0);
        Individ individ_second = new Individ(0);

        // Скрещиваем первую хромосому пары
        MyPair<String, String> firstHromosoms =
                this.crossHromos(pair.getFirst().getHromosomeFirstStr(), pair.getSecond().getHromosomeFirstStr(), N);
        individ_first.setHromosomeFirstStr(firstHromosoms.getFirst());
        individ_second.setHromosomeFirstStr(firstHromosoms.getSecond());

        // Скрещиваем вторую хромосому пары
        MyPair<String, String> secondHromosoms =
                this.crossHromos(pair.getFirst().getHromosomeSecondStr(), pair.getSecond().getHromosomeSecondStr(), N);
        individ_first.setHromosomeSecondStr(secondHromosoms.getFirst());
        individ_second.setHromosomeSecondStr(secondHromosoms.getSecond());

        // Пересчитываем значения X, Y
        recalc_X_Y(individ_first);
        recalc_X_Y(individ_second);

        // Выбираем одного потомка
        switch ("best"){
            case "random":{
                if (Math.random() > 0.5)
                    return individ_first;
                else
                    return individ_second;
            }
            case "best":{
                if (abs(individ_first.getY()) > abs(individ_second.getY()))
                    return individ_first;
                else
                    return individ_second;
            }
            default:{
                throw new Exception("wrong param");
            }
        }
    }

    // скрещиваем две строки
    private MyPair<String, String> crossHromos(String str_1, String str_2, int N) {
        String[] arr_1 = splitByNumber(str_1, N);
        String[] arr_2 = splitByNumber(str_2, N);
        String result_1 = "";
        String result_2 = "";
        for (int i = 0; i < arr_1.length; i++) {
            if (i % 2 == 0) {
                result_1 += arr_1[i];
                result_2 += arr_2[i];
            } else {
                result_1 += arr_2[i];
                result_2 += arr_1[i];
            }
        }
        return new MyPair<>(result_1, result_2);
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
            double x = generateX();
            System.out.println("generated x = " + x);
            Individ individ = new Individ(x);
            population.add(individ);
            System.out.println("verificate   = " + individ.getX_FromHromosome());
        }
        return population;
    }

    public double generateX() {
        final int main = generateRandom(START, END);
        final int drob = generateRandom(START, Integer.MAX_VALUE / 10);
        double result = main + ((double)drob / Individ.POW);
        return result;
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