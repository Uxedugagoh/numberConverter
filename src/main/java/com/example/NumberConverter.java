package com.example;

import java.util.*;

public class NumberConverter {
    // Загрузка данных из JSON файла
    private final NumeralDataLoader dataLoader = new NumeralDataLoader();
    private final NumeralForms forms = dataLoader.loadData();

    public String sumProp(long nSum, String sGender, String sCase) {
        // Проверки на некорректный ввод данных
        if (!(Objects.equals(sGender, "Ж") || Objects.equals(sGender, "С") || Objects.equals(sGender, "М"))) {
            throw new IllegalArgumentException("Неверный формат пола.");
        }
        if (!(Set.of("И", "Р", "Д", "В", "Т", "П").contains(sCase))) {
            throw new IllegalArgumentException("Неверный формат падежа.");
        }
        if (nSum > 999_999_999_999L || nSum < -999_999_999_999L) {
            throw new IllegalArgumentException("Данное число вне границ.");
        }
        if (nSum == 0) {
            return forms.getZerosForm(sCase);
        }

        boolean neagtive = false;
        if (nSum < 0) {
            nSum = Math.abs(nSum);
            neagtive = true;
        }
        // Создание списка триплетов - трёхзначных чисел, которые мы выговариваем при произношении числа.
        List<Short> tripletList = getTriplets(nSum);
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
        // replaceAll() и trim() удаляют ненужные пробелы в местах, где они появляются.
        return result.toString().trim().replaceAll("\\s+", " ");
    }

    /**
     * Метод получения списка short переменных, состоящий из триплетов чисел внутри заданного числа.
     * @param nSum - Заданное число
     * @return - Список List<Short>, содержащий трёхзначные числа, из которых состоит полученное на вход число
     */
    private List<Short> getTriplets(long nSum) {
        List<Short> shorts = new ArrayList<>();
        while (nSum > 0) {
            shorts.add((short) (nSum % 1000));
            nSum /= 1000;
        }
        Collections.reverse(shorts);
        return shorts;
    }

    /**
     * Метод для конвертации числа-триплета в пропись
     * @param nSum - Заданное трёхзначное число
     * @param sCase - Падеж
     * @param sGender - Род слова
     * @return - Строка, в которой содержится число прописью
     */
    private String convertThree(short nSum, String sCase, String sGender) {
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
            if (nSum >= 10 && nSum <= 19) {
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