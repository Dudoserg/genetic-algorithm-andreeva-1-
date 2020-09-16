import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;


public class Calc {

    public double MNOGITEL = 1.5;

    public static int START = 0;
    public static int END = 35;
    public static int POPULATION_SIZE = 10;
    public static int N = 2;

    public Calc() {
//        int mas[] = new int[10];
//        for (int i = 0; i < 100000; i++)
//            mas[generateRandom(0, 10)]++;
//        System.out.println();

//        for(int i = START; i < END; i++){
//            System.out.println("x = " + i + "\t" + "y = " + func(i));
//        }

        double extr = 0.0;
        double x = 0.0;
        for (double i = START; i < END; i += 0.00001) {
            if (abs(func(i)) > abs(extr)) {
                extr = func(i);
                x = i;
            }
        }
        System.out.println();
    }

    public double func(double x) {
        return (0.8 * cos(3 * x) + cos(x)) * (x - 4);
//        return 3 * ((x + 23) * (x + 23)) + 150;
    }

    public void start() throws Exception {
        List<Individ> list = generatePopulation(POPULATION_SIZE);


        // Считаем у всех функцию
        do {
            for (int z = 0; z < 1; z++) {
                double maxFunc = 0;

                for (Individ individ : list) {
                    final int x = individ.arrToInteger(individ.getArr_first());
                    individ.setX(x);
                    final int x_second = individ.arrToInteger(individ.getArr_second());
                    individ.setX_second(x_second);
                    final double func = func(individ.getFullNum());
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
                            "x = " + individ.getFullNum() +
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
                    if (Math.random() > 0.8) {
                        // мутации подвергается только 33 процента
//                    mutation(individ);

                        {
                            individ.setX(generateRandom((int) START, (int) END));
                            individ.setArr_first(
                                    individ.stringToArr(
                                            individ.intToStringBinary(
                                                    individ.getX()
                                            )
                                    )
                            );
                        }
                        {
                            individ.setX_second(generateRandom((int) 0, (int) 99999));
                            individ.setArr_second(
                                    individ.stringToArr(
                                            individ.intToStringBinary(
                                                    individ.getX_second()
                                            )
                                    )
                            );
                        }
                        recalcValues(individ);
                        System.out.print("");
                    }
                }

                // СЕЛЕКЦИЯ
                for (int i = list.size() - 1; i >= 0; i--) {
                    Individ individ = list.get(i);
                    if (individ.getX() < START || individ.getX() > END)
                        list.remove(individ);
                }
                System.out.println("list.size() = " + list.size());
                list = selection(list);

                System.out.println("//////////////////////////////////////////////////////////////////////");
                for (int i = 0; i < list.size(); i++) {
                    Individ individ = list.get(i);
                    System.out.println("individ(" + i + ") " +
                            "x = " + individ.getFullNum() +
                            "\tfunc = " + individ.getFunc() +
                            "\tdist = " + individ.getDistance() +
                            "\t" + individ.getSurvivePercent() + "%");

                }
                System.out.println("//////////////////////////////////////////////////////////////////////");

            }
            System.out.println();
            boolean isStop = false;
            for (int i = 0; i < list.size(); i++) {
                Individ individ_first = list.get(i);
                int countSame = 0;
                for (int k = 0; k < list.size(); k++) {
                    Individ individ_second = list.get(k);
                    if (abs(individ_first.getFunc() - individ_second.getFunc()) < 0.00000001) {
                        countSame++;
                    }
                }
                if (countSame / list.size() >= 0.79) {
                    isStop = true;
                }

            }
            if (isStop)
                break;
        } while (true);

    }

    public void recalcValues(Individ individ) {
        final int x = individ.arrToInteger(individ.getArr_first());
        individ.setX(x);

        final int x_second = individ.arrToInteger(individ.getArr_second());
        individ.setX_second(x_second);

        individ.setFunc(func(individ.getFullNum()));
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
        // реверс
        {
            int start = generateRandom(1, Individ.ARR_SIZE - 2);
            int end = generateRandom(start, Individ.ARR_SIZE - 1);
            for (int i = start; i < (end - start) / 2 + start; i++) {
                int tmp = individ.getArr_first()[i];
                individ.getArr_first()[i] = individ.getArr_first()[individ.getArr_first().length - i - 1];
                individ.getArr_first()[individ.getArr_first().length - i - 1] = tmp;
            }
        }
        {
            int start = generateRandom(1, Individ.ARR_SIZE - 2);
            int end = generateRandom(start, Individ.ARR_SIZE - 1);
            for (int i = start; i < (end - start) / 2 + start; i++) {
                int tmp = individ.getArr_second()[i];
                individ.getArr_second()[i] = individ.getArr_second()[individ.getArr_second().length - i - 1];
                individ.getArr_second()[individ.getArr_second().length - i - 1] = tmp;
            }
        }
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
                    individ_first.getArr_first()[index] = pair.getFirst().getArr_first()[index];
                    individ_second.getArr_first()[index] = pair.getSecond().getArr_first()[index];

                    individ_first.getArr_second()[index] = pair.getFirst().getArr_second()[index];
                    individ_second.getArr_second()[index] = pair.getSecond().getArr_second()[index];
                } else {
                    individ_first.getArr_first()[index] = pair.getSecond().getArr_first()[index];
                    individ_second.getArr_first()[index] = pair.getFirst().getArr_first()[index];

                    individ_first.getArr_second()[index] = pair.getSecond().getArr_second()[index];
                    individ_second.getArr_second()[index] = pair.getFirst().getArr_second()[index];
                }
                index++;
            }
            iteration++;
        }
        for (int i = index; i < Individ.ARR_SIZE; i++) {
            if (iteration % 2 == 0) {
                individ_first.getArr_first()[index] = pair.getFirst().getArr_first()[index];
                individ_second.getArr_first()[index] = pair.getSecond().getArr_first()[index];

                individ_first.getArr_second()[index] = pair.getFirst().getArr_second()[index];
                individ_second.getArr_second()[index] = pair.getSecond().getArr_second()[index];
            } else {
                individ_first.getArr_first()[index] = pair.getSecond().getArr_first()[index];
                individ_second.getArr_first()[index] = pair.getFirst().getArr_first()[index];

                individ_first.getArr_second()[index] = pair.getSecond().getArr_second()[index];
                individ_second.getArr_second()[index] = pair.getFirst().getArr_second()[index];
            }
            index++;
        }

//        if (Math.random() > 0.5)
//            return individ_first;
//        else
//            return individ_second;
        int x_first_1 = individ_first.arrToInteger(individ_first.getArr_first());
        int x_second_1 = individ_first.arrToInteger(individ_first.getArr_second());

        int x_first_2 = individ_second.arrToInteger(individ_second.getArr_first());
        int x_second_2 = individ_second.arrToInteger(individ_second.getArr_second());

        individ_first.setX(x_first_1);
        individ_first.setX_second(x_second_1);

        individ_second.setX(x_first_2);
        individ_second.setX_second(x_second_2);

        individ_first.setFunc(func(individ_first.getFullNum()));
        individ_second.setFunc(func(individ_first.getFullNum()));


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
            } while (first.getFullNum() < START || first.getFullNum() > END);
            Individ second = null;
            do {
                second = getIndividInLineWithCoeff(list, Math.random());
            } while (first == second || (second.getFullNum() < START || second.getFullNum() > END));
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
            final int random = generateRandom(START, END);
            final int random_second = generateRandom(START, END);
            System.out.println("random = " + random);
            Individ individ = new Individ(random, random_second);
            population.add(individ);
            System.out.println("real   = " + individ.arrToInteger(individ.getArr_first()));
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
