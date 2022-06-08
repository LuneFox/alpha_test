## Описание

Создать сервис, который обращается к сервису курсов валют, и отображает gif:

* если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную отсюда https://giphy.com/search/rich

* если ниже - отсюда https://giphy.com/search/broke

### **Ссылки**

* [REST API курсов валют](https://docs.openexchangerates.org/)

* [REST API гифок](https://developers.giphy.com/docs/api#quick-start-guide)

### **Must Have**

Сервис на Spring Boot 2 + Java / Kotlin

Запросы приходят на HTTP endpoint (должен быть написан в соответствии с rest conventions), туда передается код валюты по отношению с которой сравнивается USD
Для взаимодействия с внешними сервисами используется Feign

Все параметры (валюта по отношению к которой смотрится курс, адреса внешних сервисов и т.д.) вынесены в настройки
На сервис написаны тесты (для мока внешних сервисов можно использовать @mockbean или WireMock)
Для сборки должен использоваться Gradle

Результатом выполнения должен быть репо на GitHub с инструкцией по запуску

### **Nice to Have**

Сборка и запуск Docker контейнера с этим сервисом

___

## Инструкция по запуску
**Версия Java:** 11+

### С помощью терминала:

* Выполнить `java -jar war/richorbroke.war`

* Перейти на [http://localhost:8080/rates?currency=rub](http://localhost:8080/rates?currency=rub)

### С помощью сервера Tomcat:

* Скопировать `war/richorbroke.war` в директорию `/webapps` сервера Tomcat
* Дождаться распаковки архива и установки приложения (если не срабатывает автоматически - перезагрузить сервер)
* Перейти на [http://localhost:8080/richorbroke/rates?currency=rub](http://localhost:8080/richorbroke/rates?currency=rub)

### С помощью Docker:
* Скачать образ с Tomcat: `docker pull tomcat`
* Построить проект `docker build -t lunefox/alphatest:0.0.1-SNAPSHOT .`
* Запустить `docker run -p 9999:9999 lunefox/alphatest:0.0.1-SNAPSHOT`
* Перейти на [http://localhost:9999/richorbroke/rates?currency=rub](http://localhost:9999/richorbroke/rates?currency=rub)

### С помощью Intellij IDEA:
* Запустить метод `main` в классе `AlphaTestApplication`
* Перейти на [http://localhost:9999/rates?currency=rub](http://localhost:9999/rates?currency=rub)

Порт по-умолчанию указан в файле `application.properties`, можно поменять с 9999 на любой желаемый.
___
## Как работает:

В строке запроса в качестве параметра `currency` передаётся код любой поддерживаемой валюты в любом регистре.

**Например:**
* ../rates?currency=**rub**
* ../rates?currency=**jpy**
* ../rates?currency=**AUD**

В зависимости от отношения сегодняшнего курса валюты ко вчерашнему будет отображена соответствующая гифка.