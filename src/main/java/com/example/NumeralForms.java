package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumeralForms {

    @JsonProperty("zerosForm")
    private String[] zerosForm;

    /**
     * В формах единиц, сначала идёт разделение по полу, затем по падежу, затем по числительному
     */
    @JsonProperty("unitsForm")
    private String[][][] unitsForm;

    /**
     * В формах десяток, сначала идёт разделение по падежу, затем по числительному
     */
    @JsonProperty("tensForm")
    private String[][] tensForm;

    /**
     * Специальные формы десяток, для случаев 10 - 19. Сначала идёт разделение по падежу, затем по числительному
     */
    @JsonProperty("tensFormSpecial")
    private String[][] tensFormSpecial;

    /**
     * В формах сотен, сначала идёт разделение по падежу, затем по числительному
     */
    @JsonProperty("hundredsForm")
    private String[][] hundredsForm;

    /**
     * В поле elseForms формы числительных распределены сначала по месту в числительном, затем по правилам form:
     * (одна) "тысяча" - ед. число (form = 0)
     * (две) "тысячи" - род. падеж ед. числа (form = 1)
     * (восемь) "тысяч" - род. падеж множ. числа (form = 2)
     */
    @JsonProperty("elseForms")
    private String[][][] elseForms;

    public String getZerosForm(String sCase) {
        return zerosForm[getCaseIndex(sCase)];
    }

    public String getUnitsForm(String sCase, String sGender, short sNum) {
        int genderIndex = sGender.equals("М") ? 0 : sGender.equals("Ж") ? 1 : 2;
        int caseIndex = getCaseIndex(sCase);
        return unitsForm[genderIndex][caseIndex][sNum];
    }

    public String getTensForm(String sCase, short sNum) {
        int caseIndex = getCaseIndex(sCase);
        return tensForm[caseIndex][sNum];
    }

    public String getTensFormSpecial(String sCase, short sNum) {
        int caseIndex = getCaseIndex(sCase);
        int numIndex = sNum % 10;
        return tensFormSpecial[caseIndex][numIndex];
    }

    public String getHundredsForm(String sCase, short sNum) {
        int caseIndex = getCaseIndex(sCase);
        return hundredsForm[caseIndex][sNum];
    }

    // Метод получения прочих форм предполагает получение форм числительных "тысяч", "миллионов", "миллиардов".
    public String getElseForms(String sCase, short sNum, int iteratorIndex) {
        if (sNum == 0) {
            return "";
        }
        int caseIndex = getCaseIndex(sCase);

        // Определяем форму слова в зависимости от числа
        int lastDigit = sNum % 10;
        int lastTwoDigits = sNum % 100;
        byte form;
        byte singularForm = 0;
        byte singularFormGenetive = 1;
        byte pluralFormGenetive = 2;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            form = pluralFormGenetive;
        } else {
            form = switch (lastDigit) {
                case 1 -> singularForm;
                case 2, 3, 4 -> singularFormGenetive;
                default -> pluralFormGenetive;
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