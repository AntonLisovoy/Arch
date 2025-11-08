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