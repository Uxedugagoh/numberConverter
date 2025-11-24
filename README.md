## Преобразование целого числа в строку прописью в любом падеже и роде.
#### Краткий обзор:
- [NumberConverter](https://github.com/Uxedugagoh/numberConverter/blob/master/src/main/java/com/example/NumberConverter.java) - Основной java класс, с реализованными методами sumProp, getTriplets и convertThree.
- [NumeralForms](https://github.com/Uxedugagoh/numberConverter/blob/master/src/main/java/com/example/NumeralForms.java) - Класс, содержащий поля со строками правильных склонений всех необходимых числительных. Также, в методах get реализована логика их получения в зависимости от полученных из класса NumberConverter параметров. 
- [NumeralDataLoader](https://github.com/Uxedugagoh/numberConverter/blob/master/src/main/java/com/example/NumeralDataLoader.java) - Класс, содержащий реализацию загрузки данных из JSON файла со всеми необходимыми склонениями числительных. Необходим для того, чтобы не хранить большой блок статичных данных в полях класса Main, а вынести их в отдельный файл JSON.
- [numeralForms.json](https://github.com/Uxedugagoh/numberConverter/blob/master/src/main/resources/numeralForms.json) - JSON файл с необходимыми формами сколнения числительных
- [TestsForNumberConverter](https://github.com/Uxedugagoh/numberConverter/blob/master/src/test/java/TestsForNumberConverter.java) - Тесты, написанные на Junit5, реализующие 100% покрытие.
#### Использованные технологии:
- Java 17, Maven
- Junit 5 - для написания тестов
- Jackson - библиотека для работы с JSON форматом
