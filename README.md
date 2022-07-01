![img.png](img.png)

![img_3.png](img_3.png)

Щоб запустити апі(на дефолтному хості 8080) необхідно запустити main метод класу TermPaperApplication(щоб зробити це через консоль потрібно
перейти в кореневу директорію проекту та викликати команду "mvn org.springframework.boot:spring-boot-maven-plugin:run")

Надалі через локалхост 8080 можна буде користуватися CRUD операціями(найзручніше це робити у застосунку Postman)

URL-mapping мого апі:

**Отримати всі сутності даного типу(GET):**
- /farmers
- /plots
- /sensors
- /sensorReadings

**Отримати об'єкт сутності певного типу за id(GET):**
- /farmers/{id}
- /plots/{id}
- /sensors/{id}
- /sensorReadings/{id}

**Отримати список всіх ділянок фермера за id фермера(GET):**
- /farmers{id}/plots

**Отримати список всіх сенсорів на ділянці за id ділянки(GET):**
- /plots{id}/sensors

**Отримати список всіх показів сенсора за id сенсора(GET):**
- /sensors{id}/sensorReadings

**Створити об'єкт даного типу(POST):**
- /farmers
- /plots
- /sensors
- /sensorReadings

**Редагувати об'єкт даного типу за id(PUT):**
- /farmers/{id}
- /plots/{id}
- /sensors/{id}
- /sensorReadings/{id}

**Видалити об'єкт даного типу за id(DELETE):**
- /farmers/{id}
- /plots/{id}
- /sensors/{id}
- /sensorReadings/{id}

