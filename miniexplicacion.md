# 📦 Cómo desarrollar nuevas features en Clean Architecture (Kotlin + Compose)

Esta guía explica de forma simple cómo agregar una nueva feature siguiendo una estructura basada en Clean Architecture:

```
app/
core/
data/
domain/
features/
```

---

# 🧱 1. Responsabilidad de cada carpeta

## 📁 app/
Configuración principal de la aplicación:
- Navegación
- Inyección de dependencias (Hilt/Koin)
- MainActivity
- Application class

---

## 📁 core/
Código compartido entre features:
- Wrappers como `Result`
- Clases base
- Utilidades
- Constantes
- Extensiones

---

## 📁 domain/
Reglas de negocio puras (la parte más importante).

Contiene:
- Entidades
- Interfaces de repositorios
- Use Cases

⚠️ No debe depender de Android, Retrofit, Room ni frameworks externos.

---

## 📁 data/
Implementaciones concretas:
- Repositorios
- API (Retrofit/Ktor)
- Base de datos (Room)
- Mappers (DTO ↔ Domain)

Depende de `domain`.

---

## 📁 features/
Contiene cada módulo funcional de la app.

Ejemplo:

```
features/
    create_event/
        CreateEventScreen.kt
        CreateEventViewModel.kt
        CreateEventState.kt
```

Incluye:
- UI (Compose)
- ViewModel
- Estados
- Eventos

---

# 🚀 Cómo crear una nueva feature (Paso a paso)

Supongamos que quieres crear la feature `CreateEvent`.

---

## 1️⃣ Crear el modelo en `domain`

### a) Entidad (si no existe)

```kotlin
data class Event(
    val id: String,
    val name: String,
    val description: String
)
```

---

### b) Interfaz del repositorio

```kotlin
interface EventRepository {
    suspend fun createEvent(event: Event)
}
```

Aquí defines **qué se puede hacer**, no cómo se hace.

---

### c) Crear el Use Case

```kotlin
class CreateEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        repository.createEvent(event)
    }
}
```

🔹 Un Use Case representa UNA acción específica del negocio.

---

## 2️⃣ Implementar en `data`

### a) Implementación del repositorio

```kotlin
class EventRepositoryImpl(
    private val api: EventApi
) : EventRepository {

    override suspend fun createEvent(event: Event) {
        api.create(event.toDto())
    }
}
```

Aquí sí puedes usar:
- Retrofit
- Room
- Firebase
- etc.

---

## 3️⃣ Crear la feature en `features/`

Estructura recomendada:

```
features/create_event/
    CreateEventScreen.kt
    CreateEventViewModel.kt
    CreateEventState.kt
```

---

### a) ViewModel

```kotlin
class CreateEventViewModel(
    private val createEventUseCase: CreateEventUseCase
) : ViewModel() {

    fun createEvent(event: Event) {
        viewModelScope.launch {
            createEventUseCase(event)
        }
    }
}
```

El ViewModel usa el Use Case, no el repositorio directamente.

---

### b) Screen (Jetpack Compose)

```kotlin
@Composable
fun CreateEventScreen(
    viewModel: CreateEventViewModel
) {
    // UI que llama a viewModel.createEvent()
}
```

⚠️ La UI nunca accede directamente a la capa `data`.

---

# 🧠 Orden mental correcto al desarrollar

Siempre piensa en este orden:

1. ¿Qué acción de negocio quiero? → Crear Use Case
2. ¿Qué necesita el dominio? → Definir interfaz
3. ¿Cómo se implementa? → Implementar en `data`
4. ¿Cómo lo usa la UI? → ViewModel
5. ¿Cómo lo muestra? → Compose Screen

---

# 📏 Reglas importantes

- Domain NO conoce Android
- Domain NO conoce Retrofit/Room
- Data conoce Domain
- Features conoce Domain
- UI nunca accede directamente a Data
- Las dependencias siempre apuntan hacia adentro

---

# 🧪 Checklist al crear una nueva feature

- [ ] Crear entidad (si es necesaria)
- [ ] Crear interfaz en `domain`
- [ ] Crear use case
- [ ] Implementar repositorio en `data`
- [ ] Configurar inyección de dependencias
- [ ] Crear ViewModel
- [ ] Crear Screen
- [ ] Conectar navegación

---

# 🔥 Señal de que lo hiciste bien

Si mañana cambias:

- Retrofit → Ktor
- Room → Firebase
- Compose → XML

Tu carpeta `domain/` debería permanecer intacta.

Si eso ocurre → estás aplicando Clean Architecture correctamente.