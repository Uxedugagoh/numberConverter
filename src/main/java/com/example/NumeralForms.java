package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumeralForms {
    @JsonProperty("zerosForm")
    private String[] zerosForm;

    @JsonProperty("unitsForm")
    private String[][][] unitsForm;

    @JsonProperty("tensForm")
    private String[][] tensForm;

    @JsonProperty("tensFormSpecial")
    private String[][] tensFormSpecial;

    @JsonProperty("hundredsForm")
    private String[][] hundredsForm;

    @JsonProperty("elseForms")
    private String[][][] elseForms;

    public String getZerosForm(String sCase) {
        return zerosForm[getCaseIndex(sCase)];
    }

    public String getUnitsForm(String sCase, String sGender, short sNum) {
        int genderIndex = sGender.equals("М") ? 0 : sGender.equals("Ж") ? 1 : 2;
        int caseIndex = getCaseIndex(sCase);
        // В формах единиц, сначала идёт разделение по полу, затем по падежу, затем по числительному
        return unitsForm[genderIndex][caseIndex][sNum];
    }

    public String getTensForm(String sCase, short sNum) {
        int caseIndex = getCaseIndex(sCase);
        // В формах десяток, сначала идёт разделение по падежу, затем по числительному
        return tensForm[caseIndex][sNum];
    }

    // Метод, необходимый для корректной обработки чисел с 10 по 19
    public String getTensFormSpecial(String sCase, short sNum) {
        int caseIndex = getCaseIndex(sCase);
        // Предполагается, что sNum в пределах интервала 10 - 19.
        int numIndex = sNum % 10;
        return tensFormSpecial[caseIndex][numIndex];
    }

    public String getHundredsForm(String sCase, short sNum) {
        int caseIndex = getCaseIndex(sCase);
        // В формах сотен, как и в формах десяток, разделение сначала по падежу, затем по числительному
        return hundredsForm[caseIndex][sNum];
    }

    // Метод получения прочих форм предполагает получение форм числительных "тысяч", "миллионов", "миллиардов".
    public String getElseForms(String sCase, short sNum, int iteratorIndex) {
        int caseIndex = getCaseIndex(sCase);

        // Определяем форму слова в зависимости от числа
        int lastDigit = sNum % 10;
        int lastTwoDigits = sNum % 100;
        int form;
        // В поле elseForms формы числительных распределены сначала по месту в числительном, затем по правилам form:
        // (одна) "тысяча" - ед. число (form = 0)
        // (две) "тысячи" - род. падеж ед. числа (form = 1)
        // (восемь) "тысяч" - род. падеж множ. числа (form = 2)
        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            form = 2;
        } else {
            form = switch (lastDigit) {
                case 1 -> 0;
                case 2, 3, 4 -> 1;
                default -> 2;
            };
        }
        return elseForms[iteratorIndex][caseIndex][form];
    }

    public static int getCaseIndex(String sCase) {
        return switch (sCase) {
            case "И" -> 0; // именительный
            case "Р" -> 1; // родительный
            case "Д" -> 2; // дательный
            case "В" -> 3; // винительный
            case "Т" -> 4; // творительный
            case "П" -> 5; // предложный
            default -> 0;
        };
    }
}