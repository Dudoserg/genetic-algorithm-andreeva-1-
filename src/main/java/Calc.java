import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.cos;


public class Calc {


    public static double START = 0;
    public static double END = 35;
    public static int POPULATION_SIZE = 10;
    public static int N = 2;

    public Calc() {
    }

    public double func(double x) {
        return (0.8 * cos(3 * x) + cos(x)) * (x - 4);
    }

    public void start() throws Exception {
        List<Individ> list = generatePopulation(POPULATION_SIZE);


        // Считаем у всех функцию
        double maxFunc = 0;
        do {
            for (Individ individ : list) {
                final double x = individ.arrToDouble();
                individ.setX(x);
                final double func = func(x);
                individ.setFunc(func);
                if (abs(func) > abs(maxFunc)) {
                    maxFunc = func;
                }
            }
            // Считаем коэффициент выживаемости
            for (Individ individ : list) {
                final double func = individ.getFunc();
                final double distance = abs(maxFunc * 2 - func);
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
            for (Individ individ : list) {
                final double v = (1.0 / individ.getDistance()) / sum;
                individ.setSurvivePercent(v);
                System.out.println(v);
                sumV += v;
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
                if (Math.random() > 0.66) {
                    // мутации подвергается только 33 процента
                    mutation(individ);
                }
            }

            // СЕЛЕКЦИЯ
            list = selection(list);
        } while (true);

    }

    // Селекция
    public List<Individ> selection(List<Individ> list) {
        int[] mas = new int[list.size()];
        for (int i = 0; i < list.size() / 2; i++) {
            do {
                final int index = generateRandom(0, list.size());
                if (mas[index] == 0) {
                    mas[index] = 1;
                    break;
                }
            } while (true);
        }
        List<Individ> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (mas[i] != 0) {
                result.add(list.get(i));
            }
        }
        return result;
    }

    // Мутация
    public void mutation(Individ individ) {
        // реверс
        int start = generateRandom(0, Individ.ARR_SIZE - 2);
        int end = generateRandom(start, Individ.ARR_SIZE - 1);
        for (int i = start; i < (end - start) / 2 + start; i++) {
            int tmp = individ.getArr()[i];
            individ.getArr()[i] = individ.getArr()[individ.getArr().length - i - 1];
            individ.getArr()[individ.getArr().length - i - 1] = tmp;
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
        if (abs(individ_first.arrToDouble()) > abs(individ_second.arrToDouble()))
            return individ_first;
        else
            return individ_second;
    }

    // Создаем пары родителей
    public List<MyPair<Individ, Individ>> createPair(List<Individ> list) {
        List<MyPair<Individ, Individ>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final int first = generateRandom(0, list.size() - 1);
            int second = generateRandom(0, list.size() - 1);
            if (first == second) {
                if (second + 1 < list.size())
                    second++;
                else
                    second--;
            }
            result.add(new MyPair<>(list.get(first), list.get(second)));
        }
        return result;
    }


    // generate population
    public List<Individ> generatePopulation(int size) {
        List<Individ> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final double random = generateRandom(START, END);
            System.out.println("random = " + random);
            Individ individ = new Individ(random);
            population.add(individ);
            System.out.println("real   = " + individ.arrToDouble());
            if (abs(individ.arrToDouble() - random) > 0.000000001)
                System.out.println("error");
            System.out.println();
        }
        return population;
    }

    public double generateRandom(double from, double to) {
        double random_number = from + (Math.random() * to);
        return random_number;
    }

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
