# Event Notificator API

**Event Notificator API** — это микросервис для управления уведомлениями о мероприятиях, связанных с изменениями, в которых зарегистрирован пользователь. Этот проект работает в связке с другим микросервисом, **Event Manager API**, отправляя пользователям обновления о запланированных мероприятиях.

> Ссылка на **Event Manager**: [github.com/creper2004/event-manager](https://github.com/creper2004/event-manager)

**Event Manager** отправляет уведомления об изменениях событий через Kafka, а **Event Notificator** обрабатывает их, отправляя обновления пользователям о старых и новых параметрах мероприятия, таких как дата, локация, стоимость и длительность и др.

## Технологии
- **Spring Boot**: Основной фреймворк для создания приложения.
- **Spring Security**: Управление авторизацией пользователей.
- **JWT**: Аутентификация на основе токенов.
- **Kafka**: Асинхронное взаимодействие с микросервисом Event Manager.
- **PostgreSQL**: Для хранения уведомлений о мероприятиях.
- **Maven**: Сборка проекта.

---

## Эндпоинты API

### Получение всех непрочитанных уведомлений
- **URL**: `/notifications`
- **Метод**: `GET`
- **Доступ**: Роли **ADMIN** и **USER**
- **Описание**: Возвращает список всех непрочитанных уведомлений для текущего пользователя.
- **Аутентификация**: JWT токен от **Event Manager** должен быть включён в запрос.
- **Ответ (успех 200)**:
    ```json
    [
        {
            "notificationId": 5,
            "eventId": 19,
            "ownerId": 2,
            "timestamp": "2024-10-29T16:29:00.372097",
            "changes": [
                {
                    "fieldName": "status",
                    "oldValue": "STARTED",
                    "newValue": "FINISHED"
                }
            ]
        },
        {
            "notificationId": 6,
            "eventId": 20,
            "ownerId": 3,
            "timestamp": "2024-10-30T08:15:00.123456",
            "changes": [
                {
                    "fieldName": "name",
                    "oldValue": "Old name",
                    "newValue": "New name"
                },
                {
                    "fieldName": "maxPlaces",
                    "oldValue": "50",
                    "newValue": "100"
                }
            ]
        }
    ]
    ```
- **Ошибки**:
  - **400**: Некорректный запрос
  - **401**: Пользователь не аутентифицирован
  - **403**: Доступ запрещен
  - **404**: Уведомления не найдены
  - **500**: Ошибка сервера

### Пометить уведомления как прочитанные
- **URL**: `/notifications`
- **Метод**: `POST`
- **Доступ**: Роли **ADMIN** и **USER**
- **Описание**: Позволяет отметить указанные уведомления как прочитанные. Если уведомление не найдено, то ошибка не возникает.
- **Аутентификация**: JWT токен от **Event Manager** должен быть включён в запрос.
- **Тело запроса**:
    ```json
    {
        "notificationIds": [1, 2, 3, 5, 7, 12]
    }
    ```
- **Ответ (успех 204)**: Уведомления успешно помечены как прочитанные.
- **Ошибки**:
  - **400**: Некорректный запрос
  - **401**: Пользователь не аутентифицирован
  - **403**: Доступ запрещен
  - **500**: Ошибка сервера

---

## Обработка ошибок
Стандартные ошибки включают:
- **BadRequest (400)**: Ошибка формата или логики запроса.
- **Unauthorized (401)**: Ошибка аутентификации.
- **Forbidden (403)**: Недостаточно прав.
- **NotFound (404)**: Не найдена сущность.
- **ServerError (500)**: Ошибка сервера.

---

## Авторизация
**Event Notificator API** использует JWT токены, выпущенные сервисом **Event Manager**, для обеспечения аутентификации и авторизации.