# Rest Book Shelf API

**Rest Book Shelf API** — сервис учёта книг

## 🚀 Возможности

* Управление авторами: создание, получение списка, получение по ID.
* Управление книгами: создание, получение списка, получение по ID, обновление, удаление.
* Пагинация для списка авторов.
* Валидация входных данных с понятными сообщениями об ошибках.
* Обработка ошибок через глобальный перехватчик.
* Автоматически генерируемая документация OpenAPI (Swagger).

## 🛠 Технологии

* Java 17
* Spring Boot 3
* Spring Data JPA
* Hibernate
* MapStruct
* Lombok
* OpenAPI 3 (Swagger)
* База данных: H2

## 📥 Установка и запуск

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/22crystyle/rest-bookshelf.git
   cd rest-bookshelf
   ```
2. Настройте параметры подключения к базе в `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:h2:mem:bookshelf;DB_CLOSE_DELAY=-1
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.hibernate.ddl-auto=update
   server.port=1024
   ```
3. Соберите проект и запустите:

   ```bash
   ./mvnw clean package
   ./mvnw spring-boot:run
   ```

   Или запустите сгенерированный JAR:

   ```bash
   java -jar target/rest-bookshelf-1.0.0.jar
   ```

## 📖 Документация API (Swagger)

После запуска приложение будет доступно по адресу `http://localhost:1024`.
Документация Swagger UI — по адресу:

```
http://localhost:1024/swagger-ui/index.html
```

или

```
http://localhost:1024/docs
```

Там вы найдёте полный список всех доступных эндпоинтов с примерами запросов и ответов.

## 🔗 Эндпоинты

### Авторы

| Метод | URL                    | Описание                      |
|-------|------------------------|-------------------------------|
| POST  | `/api/v1/authors`      | Создать нового автора         |
| GET   | `/api/v1/authors`      | Список авторов (с пагинацией) |
| GET   | `/api/v1/authors/{id}` | Получить автора по ID         |

### Книги

| Метод  | URL                  | Описание                     |
|--------|----------------------|------------------------------|
| POST   | `/api/v1/books`      | Создать новую книгу          |
| GET    | `/api/v1/books`      | Список всех книг             |
| GET    | `/api/v1/books/{id}` | Получить книгу по ID         |
| PUT    | `/api/v1/books/{id}` | Полное обновление информации |
| DELETE | `/api/v1/books/{id}` | Удалить книгу по ID          |
