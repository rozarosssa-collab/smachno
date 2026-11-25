# Смачно

Крос-платформний клієнт тестового REST API [dummyjson](https://dummyjson.com/),
галузь - рецепти. Проєкт на Kotlin Multiplatform + Compose Multiplatform,
збирається під Android, iOS і десктоп (JVM).

## Модулі

* `shared` - спільний код: тема, ресурси, екрани, логіка;
* `androidApp` - точка входу для Android;
* `desktopApp` - точка входу для десктопа.

## Запуск

```
./gradlew :desktopApp:run
```

Для Android - конфігурація `androidApp` в Android Studio.

## Лабораторні роботи

### Лабораторна 4

* новий KMP проєкт з трьома модулями;
* кастомна тема: кольори згенеровані будівельником тем (Material Theme Builder)
  з базового кольору `#B4502B`, шрифти Unbounded (заголовки) та Inter (текст);
* бібліотека kermit для логування (`core/logging/AppLogger.kt`);
* бібліотека multiplatform-settings для налаштувань (`data/settings/AppSettings.kt`);
* екран онбордінгу на компоненті `HorizontalPager`, 4 слайди, картинки намальовані
  на `Canvas` (`ui/onboarding/`);
* локалізація двома мовами: `composeResources/values` (англійська) та
  `composeResources/values-uk` (українська).

### Лабораторна 5

* бібліотека Navigation 3 (`navigation3-ui` + `lifecycle-viewmodel-navigation3`),
  граф у `App.kt`: ключі `Onboarding` та `Login`, стек переживає поворот екрана
  завдяки `SavedStateConfiguration`;
* бібліотека Koin для ін'єкції залежностей, модулі розбиті по рівнях
  (`di/AppModule.kt`), запуск - `di/KoinInit.kt`;
* онбордінг переведено на MVI: `OnboardingAction`, `OnboardingEvent`,
  `OnboardingViewModel`; завершення онбордінгу зберігається варіантом
  використання `MarkOnboardingPassedUseCase` через `PreferencesRepository`;
* `AppViewModel` через `IsOnboardingPassedUseCase` обирає стартовий екран:
  якщо онбордінг уже проходили, застосунок одразу відкриває вхід;
* екран входу з власною MVI-архітектурою: валідація імені користувача
  (від 3 символів) і пароля (від 6), дія клавіатури в полі імені переводить
  фокус на пароль, а в полі пароля виконує вхід.

### Лабораторна 6

* бібліотеки ktor (клієнт, Auth, ContentNegotiation, Logging) та KSafe
  для безпечного зберігання токенів;
* мережевий стек авторизації: запити `LoginRequest`/`RefreshRequest`,
  відповіді `LoginResponse`/`RefreshResponse`, сервіс `AppAuthApiService`,
  репозиторій `AppAuthRepository`, варіанти використання `LoginUseCase`,
  `IsSignedInUseCase`, `SignOutUseCase`, `SignedInUsernameUseCase`;
* власний тип `Result` з `map`, `onSuccess`, `onFailure` і обгорткою
  `safeRequest`, щоб мережеві винятки не виходили за рівень даних;
* токени зберігаються через KSafe (Keystore на Android, Keychain на iOS,
  зашифрований файл на десктопі), оновлення токена підвішене на
  `refreshTokens` плагіна Auth і спрацьовує автоматично;
* екран входу з'єднано з `LoginUseCase`, помилки показуються спливним
  повідомленням через одноразові події;
* порожній домашній екран `ui/home/`;
* `AppViewModel` обирає стартовий екран з урахуванням того, чи є збережений
  токен: домашній -> вхід -> онбордінг.

API: `https://dummyjson.com`. Свої акаунти там не реєструються, тому на екрані
входу є підказка з тестовим користувачем `emilys` / `emilyspass` і кнопка
«Підставити», яка заповнює обидва поля.

### Лабораторна 7

Адаптивна розмітка онбордінгу та екрана входу на `WindowSizeClass`:

* `ui/common/WindowLayout.kt` - перевірка ширини вікна
  (`currentWindowAdaptiveInfoV2().windowSizeClass.isWidthAtLeastBreakpoint`),
  поріг `WIDTH_DP_MEDIUM_LOWER_BOUND`, тобто 600 dp;
* `ui/common/CenteredScrollColumn.kt` - колонка, яка центрує вміст, поки він
  вміщається, і прокручується, коли місця бракує;
* онбордінг: на вузькому вікні картинка, текст і керування йдуть однією
  колонкою, на широкому - картинка займає ліву половину, а текст поточного
  слайда з кнопками стоїть праворуч;
* вхід: на вузькому вікні форма по центру екрана, на широкому - ліворуч
  кольорова заставка з назвою застосунку, праворуч сама форма.

Стан екранів при цьому один і той самий, змінюється лише розмітка.

### Лабораторна 8

Домашній екран з динамічним списком рецептів, який гортається сторінками,
і адаптивна розмітка «список — деталі».

Пагінація на бібліотеці Paging 3 (`paging-common` + `paging-compose`
у `commonMain`):

* `data/recipes/service/` — сервіс просить одну сторінку парою `limit`/`skip`,
  у відповіді `RecipesResponse` приходить ще й `total`;
* `data/recipes/model/RecipePage.kt` — обгортка сторінки: рецепти, загальна
  кількість і зміщення наступної порції (`null` — даних більше немає);
* `data/recipes/AppRecipeRepository.kt` — звичайна suspend-функція, яка віддає
  `Result<RecipePage>`. Типів Paging у рівнях даних і домену немає зовсім:
  вони не знають, хто і як гортатиме список;
* `domain/recipes/ObtainRecipePageUseCase.kt` — варіант використання на одну
  сторінку, зареєстрований у Koin;
* `ui/home/RecipePagingSource.kt` — `PagingSource<Int, Recipe>` у шарі
  представлення: ключ сторінки — зміщення `skip`, помилка рівня даних
  загортається в `LoadResult.Error`, `getRefreshKey` повертає `null`,
  тобто після оновлення список читається з першої сторінки;
* `ui/home/HomeViewModel.kt` — `Pager` з `PagingConfig` (сторінка 10,
  перше завантаження 20, `prefetchDistance` 4, плейсхолдери вимкнені)
  і фабрикою, яка щоразу створює новий `PagingSource`; потік віддається
  назовні через `cachedIn(viewModelScope)`;
* `ui/home/HomeScreen.kt` — `collectAsLazyPagingItems()`, сітка зі стабільними
  ключами `itemKey { it.id }`. Стан `refresh` — індикатор або повідомлення
  з кнопкою «Спробувати ще» (`retry()`), стан `append` — індикатор чи та сама
  кнопка в підвалі списку.

Адаптивна розмітка «список — деталі» на `ListDetailPaneScaffold`
(`material3-adaptive-layout` + `material3-adaptive-navigation`):

* на широкому вікні список і картка рецепта стоять поруч, вибраний рецепт
  підсвічується кольором;
* на вузькому — видно щось одне, а повернення до списку працює і кнопкою
  на панелі, і системною кнопкою «назад»;
* `ui/home/RecipeDetailPane.kt` — права панель: оцінка з кількістю відгуків,
  час підготовки й готування, порції, калорійність, інгредієнти, кроки
  приготування і теги. Окремого запиту за деталями немає, рецепт береться
  з уже завантажених сторінок;
* кількість стовпців у списку рахується від ширини самої панелі
  (`GridCells.Adaptive`), тому сітка однаково доречна і на телефоні,
  і на половині екрана десктопа.

Список лежить за захищеною адресою `/auth/recipes`, тому заголовок
`Authorization` туди підставляє плагін Auth із збереженого токена — рецепти
видно лише після справжнього входу.
