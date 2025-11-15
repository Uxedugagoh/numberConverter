package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main {


    public static void main(String[] args) {
        System.out.println(sumProp(921_611_001_012L, "М", "П"));
        System.out.println(sumProp(9876543L, "М", "И"));
        System.out.println(sumProp(111111111111L, "Ж", "В"));
        System.out.println(sumProp(111111111111L, "Ж", "Т"));
        System.out.println(sumProp(111111111111L, "Ж", "Т"));
        System.out.println(sumProp(1, "Ж", "Т"));
        System.out.println(sumProp(15, "Ж", "Т"));
        System.out.println(sumProp(105, "Ж", "Т"));
        System.out.println(sumProp(14, "Ж", "Т"));
    }

    // Загрузка данных из JSON файла
    private static final NumeralForms forms = NumeralDataLoader.loadData();

    public static String sumProp(long nSum, String sGender, String sCase) {
        // Проверки на некорректный ввод данных
        if (!(Objects.equals(sGender, "Ж") || Objects.equals(sGender, "С") || Objects.equals(sGender, "М"))) {
            return "Неверный формат пола.";
        }
        if (!(Objects.equals(sCase, "И") || Objects.equals(sCase, "Р") || Objects.equals(sCase, "Д") ||
                Objects.equals(sCase, "В") || Objects.equals(sCase, "Т") || Objects.equals(sCase, "П"))) {
            return "Неверный формат падежа.";
        }
        if (nSum == 0) {
            return forms.getZerosForm(sCase);
        }
        if (nSum > 999_999_999_999L || nSum < -999_999_999_999L) {
            return "Данное число вне границ.";
        }

        boolean neagtive = false;
        if (nSum < 0) {
            nSum = Math.abs(nSum);
            neagtive = true;
        }
        // Создание списка триплетов - трёхзначных чисел, которые мы выговариваем при произношении числа.
        List<Short> tripletList = getShorts(nSum);
        // Reverse нужен для того, чтобы идти по полученным триплетам Справа налево, позволяя вставлять слова
        // "миллиард", "миллион", "тысяча" при необходимости.
        Collections.reverse(tripletList);

        StringBuilder result = new StringBuilder();
        int size = tripletList.size();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                // На первой итерации мы опираемся на пол, заданный пользователем
                result.insert(0, convertThree(tripletList.get(0), sCase, sGender));
            } else {
                result.insert(0, " ");
                // Получаем слово "тысяча", "миллион" или "миллиард" в нужной форме
                result.insert(0, forms.getElseForms(sCase, tripletList.get(i), i - 1));
                result.insert(0, " ");
                // На второй итерации мы опираемся на женский пол, так как на этой стадии речь идёт о "тысяче"
                // На остальных опираемся на мужской, так как "миллион" и "миллиард" - мужской род
                result.insert(0, convertThree(tripletList.get(i), sCase, i == 1 ? "Ж" : "М"));
            }
        }
        if (neagtive) {
            result.insert(0, "минус ");
        }
        return result.toString();
    }

    // Метод получения списка short переменных, состоящий из триплетов чисел внутри заданного числа.
    private static List<Short> getShorts(long nSum) {
        long remaning = nSum;
        // log_1000(x) = log_e(x) / log_e(1000), log_e(x) == Math.log(x);
        // Таким образом можно узнать, сколько трёх-разрядных чисел в заданном числе.
        byte tripletCount = (byte) ((Math.log(nSum) / Math.log(1000)) + 1);
        List<Short> tripletList = new ArrayList<>();
        // Здесь определяем число, на которое нужно будет делить заданное число, чтобы получить нужное трёхзначное число
        double bitDepth = Math.pow(1000, tripletCount - 1);
        for (int i = 0; i < tripletCount; i++) {
            // Выясняем трёхзначное число самого высокого разряда.
            tripletList.add((short) (remaning / bitDepth));
            remaning %= (long) bitDepth;
            bitDepth = Math.pow(1000, tripletCount - (double) (i + 2));
        }
        return tripletList;
    }

    // Метод для конвертации числа-триплета в пропись
    public static String convertThree(short nSum, String sCase, String sGender) {
        StringBuilder stringBuilder = new StringBuilder();
        // Преобразуем сотню в пропись, если она есть
        if (nSum / 100 > 0) {
            stringBuilder.append(forms.getHundredsForm(sCase, (short) (nSum / 100)));
            nSum %= 100;
        }
        if (nSum > 0) {
            // " " - если сотни не было
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(" ");
            }
            // Для чисел 10-19 нужно обрабатывать отдельно
            if (nSum >= 10 & nSum <= 19) {
                stringBuilder.append(forms.getTensFormSpecial(sCase, nSum));
            } else {
                // Обрабатываем десятки и единицы
                short tens = (short) (nSum / 10);
                short units = (short) (nSum % 10);
                stringBuilder.append(forms.getTensForm(sCase, tens));
                if (units > 0) {
                    stringBuilder.append(" ");
                    stringBuilder.append(forms.getUnitsForm(sCase, sGender, units));
                }
            }
        }
        // replaceAll() и trim() удаляют ненужные пробелы в местах, где они появляются.
        return stringBuilder.toString().trim().replaceAll("\\s+", " ");
    }
}