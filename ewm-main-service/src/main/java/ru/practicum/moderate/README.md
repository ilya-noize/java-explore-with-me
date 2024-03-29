# Модерация

Простая "ручная" модерация контента человеком.

## Возможности

- Сохранить очередную итерацию от модератора
- Показать результаты проверки конкретного события его инициатору
- Показать все результаты проверки конкретного события его инициатору
- Показать все проверки всех событий согласно фильтру и сортировке:
    - Фильтрация всех проверок:
        - По совпадению теста в причине отказа или в комментарии к ревью;
        - По результатам проверки (Одобрено/Отклонено);
        - По дате создания записи о проверке (от и до),

    - Сортировка:
        - по событиям (по убыванию ID);
        - по результату проверки (одобренные - отклонённые);
        - по причине отказа (по алфавиту);
        - по дате создания записи (от новой к старой).

## Tests

1. Сохранить очередную итерацию от модератора

> PATCH /events/{eventId}/review CREATED

Подготовка:
- создание автора события;
- создание категории;
- создание события;
- создание ревью.

Обработка ошибок:
- 400: Review not for this event;
- 404: Event not exists.

Ожидается:
- `EventDto`: `reviews` as list;
- статус код ответа 201.

2. Показать результаты проверки конкретного события его инициатору
> GET /events/{eventId}/review/{reviewId} OK

Обработка ошибок:
- 404: Event not exists;
- 404: Reviews not exists.

Ожидается:
- id;
- eventId;
- created;
- state;
- reason;
- comment;
- статус код ответа 200.


3. Показать все результаты проверки конкретного события его инициатору

> GET /events/{eventId}/review OK

Ожидается:
- список;
- статус код ответа 200.

4. Показать все проверки всех событий согласно фильтру и сортировке

> GET /admin/review OK

Обработка ошибок:
- 400: Wrong sorting parameters.

Ожидается
- список;
- статус код ответа 200.
