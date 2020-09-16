import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;


public class Calc {

    public double MNOGITEL = 1.00;

    public static int START = 0;
    public static int END = 36;
    public static int POPULATION_SIZE = 10;
    public static int N = 2;

    public Calc() {
        MyPair<Double, Double> max = new MyPair<>(0.0, 0.0);
        for (double x = START; x <= END; x += 0.0001) {
            double y = func(x);
            if (Math.abs(y) > Math.abs(max.getSecond())) {
                max.setFirst(x);
                max.setSecond(y);
            }
//            System.out.println("x = " + x + "\t" + "y = " + y);
        }
        System.out.println("MAX VALUE: " + "x = " + max.getFirst() + "\t" + "y = " + max.getSecond());


//        for(int i = START; i < END; i++){
//            System.out.println("x = " + i + "\t" + "y = " + func(i));
//        }
        System.out.println();
    }

    public double func(double x) {
//        return (0.8 * cos(3 * x) + cos(x)) * (x - 4);
//        return 3 * ((x) * (x )) + 150;
        return -0.3 * ((x - 16.5) * (x - 16.5)) + 150;
    }

    public void start() throws Exception {
        List<Individ> list = generatePopulation(POPULATION_SIZE);


        // Считаем у всех функцию
        do {
            for (int z = 0; z < 100; z++) {
                double maxFunc = 150.0;

                for (Individ individ : list) {
                    final double x = individ.arrToNumber();
                    int tmp = (int) x;
                    individ.setX(x);
                    final double func = func(x);
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
                System.out.println("///////////////////////////ДО МУТАЦИИ///////////////////////////////////////////");
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    System.out.println("individ(" + i + ") " +
                            "x = " + individ.getX() +
                            "\tfunc = " + individ.getFunc() +
                            "\tdist = " + individ.getDistance() +
                            "\t" + individ.getSurvivePercent() + "%");

                }
                System.out.println("//////////////////////////////////////////////////////////////////////");
                // Мутируем некоторые особи
                for (Individ individ : list) {
                    if (Math.random() > 0.85) {
                        // мутации подвергается только 33 процента
                    mutation(individ);
//                        individ.setX(individ.getX() + generateX());
//                        individ.setArr(
//                                individ.stringToArr(
//                                        individ.doubleToStringBinary(
//                                                individ.getX()
//                                        )
//                                )
//                        );
                        recalcValues(individ);
                        System.out.print("");
                    }
                }
                System.out.println("//////////////////////Мутация, до селекции////////////////////////////////////////////////");
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

                System.out.println("//////////////////////ПОСЛЕ МУТАЦИИ И СЕЛЕКЦИИ////////////////////////////////////////////////");
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    System.out.println("individ(" + i + ") " +
                            "x = " + individ.getX() +
                            "\tfunc = " + individ.getFunc() +
                            "\tdist = " + individ.getDistance() +
                            "\t" + individ.getSurvivePercent() + "%");

                }
                System.out.println("//////////////////////////////////////////////////////////////////////");

            }
            System.out.println();
        } while (true);

    }

    public void recalcValues(Individ individ) {
        final double x = individ.arrToNumber();
        individ.setX(x);
        individ.setFunc(func(x));
    }

    // Селекция
    public List<Individ> selection(List<Individ> list) {
//        int[] mas = new int[list.size()];
//        for (int i = 0; i < list.size() / 2; i++) {
//            do {
//                final int index = generateRandom(0, list.size());
//                if (mas[index] == 0) {
//                    mas[index] = 1;
//                    break;
//                }
//            } while (true);
//        }
//        List<Individ> result = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            if (mas[i] != 0) {
//                result.add(list.get(i));
//            }
//        }
        List<Individ> result = new ArrayList<>();
        do {
            final int r = this.generateRandom(0, list.size());
            result.add(list.remove(r));
        } while (result.size() != POPULATION_SIZE);
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

        // cлучайная мутация пары 5 байт
        for(int i = 0 ; i < 5; i++){
            int random = 0;
            do{
                random = generateRandom(0, Individ.ARR_SIZE);
            }while (random == 0 || random == 32);
            individ.getArr()[random] = individ.getArr()[random] == 1 ? 0 : 1;
        }
        recalcValues(individ);
    }

    /// Скрещивание
    public Individ crossbreeding(MyPair<Individ, Individ> pair, int N) {
        Individ individ_first = new Individ();
        Individ individ_second = new Individ();

        int areaSize = Individ.ARR_SIZE / N;

        int iteration = 0;

        int index = 0;
        for (int point = 0; point < N; point++) {
            for (int i = 0; i < areaSize; i++) {
                if (iteration % 2 == 0) {
                    individ_first.getArr()[index] = pair.getFirst().getArr()[index];
                    individ_second.getArr()[index] = pair.getSecond().getArr()[index];
                } else {
                    individ_first.getArr()[index] = pair.getSecond().getArr()[index];
                    individ_second.getArr()[index] = pair.getFirst().getArr()[index];
                }
                index++;
            }
            iteration++;
        }
        for (int i = index; i < Individ.ARR_SIZE; i++) {
            if (iteration % 2 == 0) {
                individ_first.getArr()[index] = pair.getFirst().getArr()[index];
                individ_second.getArr()[index] = pair.getSecond().getArr()[index];
            } else {
                individ_first.getArr()[index] = pair.getSecond().getArr()[index];
                individ_second.getArr()[index] = pair.getFirst().getArr()[index];
            }
            index++;
        }

//        if (Math.random() > 0.5)
//            return individ_first;
//        else
//            return individ_second;
        double x_1 = individ_first.arrToNumber();
        double x_2 = individ_second.arrToNumber();

        individ_first.setX(x_1);
        individ_second.setX(x_2);

        individ_first.setFunc(func(x_1));
        individ_second.setFunc(func(x_2));

        if (abs(individ_first.getFunc()) > abs(individ_second.getFunc()))
            return individ_first;
        else
            return individ_second;
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
            double result = generateX();
            System.out.println("main = " + result);
            Individ individ = new Individ(result);
            population.add(individ);
            System.out.println("real   = " + individ.arrToNumber());
//            if (abs(individ.arrToInteger() - main) > 0.000000001)
//                System.out.println("error");
//            System.out.println();
        }
        return population;
    }

    public double generateX() {
        final int main = generateRandom(START, END);
        final int drob = generateRandom(START, Integer.MAX_VALUE / 10);
        double result = main + (drob / Math.pow(10, 9));
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