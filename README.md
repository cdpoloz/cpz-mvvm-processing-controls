# CPZ MVVM Processing Controls

Framework UI en Java sobre Processing con arquitectura MVVM estricta, input centralizado y theming dinámico en runtime.

## Características principales

- Arquitectura estricta: `Model -> ViewModel -> View -> Style -> RenderStyle -> Renderer`
- Input centralizado con `InputManager` e `InputLayer`
- Gestión de foco con `FocusManager`
- Overlays con `OverlayManager`
- Theming dinámico con `ThemeManager` y `ThemeTokens`
- Layout proporcional y utilidades de anclaje
- Controles reutilizables con `DevSketch` para validación rápida

## Modelo mental

La forma correcta de trabajar con el framework es siempre la misma:

1. Crear el `Model` con el estado base
2. Crear el `ViewModel` con la lógica de interacción
3. Crear la `View` para layout e hit testing
4. Configurar `Style` o `StyleConfig` si hace falta
5. Registrar el control en el flujo de input correspondiente
6. Llamar a `draw()` en cada frame

Si un comportamiento parece visual, suele vivir en `View`, `Style` o `Renderer`.
Si parece una regla de interacción, suele vivir en `ViewModel`.

## Estructura del proyecto

```text
src/com/cpz/processing/controls
├── core
│   ├── focus
│   ├── input
│   ├── layout
│   ├── model
│   ├── overlay
│   ├── style
│   ├── theme
│   ├── util
│   ├── view
│   └── viewmodel
├── controls
│   ├── button
│   ├── checkbox
│   ├── dropdown
│   ├── label
│   ├── numericfield
│   ├── radiogroup
│   ├── slider
│   ├── textfield
│   └── toggle
└── dev
```

## Quick start

La forma más rápida de explorar el framework es ejecutar un `DevSketch` desde [`Launcher.java`](src/com/cpz/processing/controls/dev/Launcher.java) o lanzar directamente una clase `*DevSketch`.

```java
// En Launcher.java
// PApplet.main(ButtonDevSketch.class);
// PApplet.main(ToggleDevSketch.class);
// PApplet.main(TextFieldDevSketch.class);
PApplet.main(ThemeDevSketch.class);
```

Un `DevSketch` muestra el patrón real de uso:

- instancia de `Model`
- instancia de `ViewModel`
- instancia de `View`
- adapters específicos del control
- registro en `InputManager`
- dibujo por frame

## MVVM en breve

- `Model`: estado puro, sin Processing
- `ViewModel`: interacción y reglas de uso, sin coordenadas ni dibujo
- `View`: layout, hit testing y construcción de `ViewState`
- `Style`: resuelve apariencia usando `ThemeTokens`
- `RenderStyle`: snapshot visual final del frame
- `Renderer`: dibujo puro

## 🔄 Ciclo de vida

En cada frame, el flujo normal es:

1. Processing genera eventos
2. `InputManager` distribuye pointer y keyboard por prioridad
3. Los adapters traducen eventos a intenciones del control
4. El `ViewModel` actualiza estado
5. La `View` construye `ViewState`
6. El `Style` resuelve `RenderStyle`
7. El `Renderer` dibuja

## Pipeline de render

```text
ViewState → Style → RenderStyle → Renderer
```

El `Renderer` no contiene lógica de negocio y el `ViewModel` no conoce píxeles.

## Input system

El input del framework es centralizado:

- `InputManager` es el punto único de entrada
- `InputLayer` define prioridad y consumo de eventos
- los adapters específicos de control traducen input a métodos del `ViewModel`
- `FocusManager` coordina el target de teclado
- `OverlayManager` permite que overlays como `DropDown` tengan prioridad cuando están activos

La idea clave es evitar rutas paralelas de input desde Processing hacia los controles.

## Controles

- [Button](docs/button.md)
- [Toggle](docs/toggle.md)
- [Label](docs/label.md)
- [Checkbox](docs/checkbox.md)
- [Slider](docs/slider.md)
- [TextField](docs/textfield.md)
- [NumericField](docs/numericfield.md)
- [DropDown](docs/dropdown.md)
- [RadioGroup](docs/radiogroup.md)

## Theming

El framework resuelve el tema en runtime mediante `ThemeManager` y `ThemeTokens`. El estilo final se calcula en cada render, lo que permite alternar entre temas como `LightTheme` y `DarkTheme` sin recrear controles.

Puntos clave:

- `ThemeManager.setTheme(...)` cambia el tema activo
- `Style` usa `ThemeManager.getTheme().tokens()`
- `RenderStyle` contiene solo valores finales para el frame actual

## 📚 Documentación

- [Architecture](docs/architecture.md)
- [Input System](docs/input-system.md)
- [Button](docs/button.md)
- [Toggle](docs/toggle.md)
- [Label](docs/label.md)
- [Checkbox](docs/checkbox.md)
- [Slider](docs/slider.md)
- [TextField](docs/textfield.md)
- [NumericField](docs/numericfield.md)
- [DropDown](docs/dropdown.md)
- [RadioGroup](docs/radiogroup.md)

## Roadmap

- Binding entre controles y modelos externos
- UI declarativa
- Layout más avanzado
- Mejoras de documentación y ejemplos
- Extensiones de renderer y assets vectoriales
