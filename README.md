# Ktor multiplatform architecture

## План семинара:

- Управление состоянием
- Clean Architecture и модульное приложение
- Модули `StateMachine`

## Ссылки:

- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kodein DI](https://kosi-libs.org/kodein/7.28/core/injection-retrieval.html)
- [CommonStateMachine](https://github.com/motorro/CommonStateMachine)

## Запуск сервера:

```bash
./gradlew :server:run
```

## Запуск node-приложения:

```bash
./gradlew :jsApp:assemble
cd jsApp/node/
npm start
```

## ДЗ 1. Чистая архитектура
В этом задании вам предстоит воспользоваться подходом Чистой архитектуры для абстракции модуля 
`shared` от реализации сетевого интерфейса:

- Поместите определение [интерфейсов usecase](shared/src/commonMain/kotlin/ru/otus/arch/net/usecase)
  в доменный модуль.
- Создайте отдельный модуль приложения для реализации usecase
- Перенесите определения API и usecase в новый модуль
- Создайте новый DI-модуль для сетевых компонентов и перенесите [определения интерфейсов](shared/src/commonMain/kotlin/ru/otus/arch/di/SharedModule.kt)
  в модуль реализации.
- Подключите зависимость на новый модуль реализации в [основной модуль приложения](composeApp/build.gradle.kts)
- Подключите DI-модуль реализации в [основной модуль приложения](composeApp/src/commonMain/kotlin/ru/otus/arch/di/AppModule.kt)
- Подключите зависимость на новый модуль реализации в [js-модуль приложения](jsApp/build.gradle.kts)
- Подключите DI-модуль реализации в [js-модуль приложения](jsApp/src/webMain/kotlin/ru/otus/arch/di/AppModule.kt)
