# Input System

## 🧠 Concepto

Sistema centralizado que desacopla Processing del framework y evita rutas paralelas de input hacia los controles.

---

## ⚙️ Componentes MVVM

- InputManager
- InputLayer
- PointerInputAdapter
- KeyboardInputAdapter
- FocusManager
- OverlayManager

---

## 🚀 Uso básico

```java
InputManager inputManager = new InputManager();

inputManager.dispatchPointer(...);
inputManager.dispatchKeyboard(...);
```

En los `DevSketch`, Processing solo genera eventos y los entrega a `InputManager`.

---

## 🎯 Interacción

Flujo típico:

1. Processing emite un evento
2. `InputManager` lo distribuye por prioridad
3. Un `InputLayer` decide si lo consume
4. Un adapter específico traduce el evento
5. El `ViewModel` actualiza el estado del control

Para teclado:

- `FocusManager` decide qué control recibe el input
- `KeyboardInputAdapter` reenvía el evento al target enfocado

Para overlays:

- `OverlayManager` registra overlays activos
- el overlay superior puede capturar input antes que el resto

---

## 🎨 Personalización

El comportamiento se ajusta creando `InputLayer` específicos por sketch o por overlay. Los adapters compartidos permanecen en `core/input`, y los adapters de control viven dentro de cada control.

---

## 🧪 Ejemplo completo

Ver [`ThemeDevSketch.java`](../src/com/cpz/processing/controls/dev/ThemeDevSketch.java) y [`DropDownDevSketch.java`](../src/com/cpz/processing/controls/dev/DropDownDevSketch.java)

---

## ⚠️ Consideraciones

- No enviar input directo desde Processing al `ViewModel`
- Los adapters traducen, no implementan negocio
- El foco y los overlays afectan prioridad de input

---

## 🔗 Relacionado

- `InputManager`
- `FocusManager`
- `OverlayManager`
- `KeyboardInputAdapter`
