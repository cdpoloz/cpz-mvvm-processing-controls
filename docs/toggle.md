# Toggle

## 🧠 Concepto

Control cíclico de estados. Puede comportarse como toggle binario o como selector multiestado.

---

## ⚙️ Componentes MVVM

- Model: `ToggleModel`
- ViewModel: `ToggleViewModel`
- View: `ToggleView`
- Style: `ToggleStyle` / `ParametricToggleStyle`
- Renderer: `ToggleShapeRenderer` y sus implementaciones

---

## 🚀 Uso básico

```java
ToggleModel model = new ToggleModel();
ToggleViewModel viewModel = new ToggleViewModel(model);
ToggleView view = new ToggleView(this, viewModel, 200f, 120f, 64f);

viewModel.setTotalStates(2);
view.setPosition(220f, 160f);
view.setSize(72f);
view.setStyle(ToggleDefaultStyles.circular());
```

---

## 🎯 Interacción

```java
viewModel.setTotalStates(3);

int currentState = viewModel.getState();
boolean changed = viewModel.hasChanged();
```

`Toggle` no usa un callback `setOnX` propio en esta versión. La forma habitual de reaccionar es consultar el estado desde el sketch o desde la lógica que lo coordina.

---

## 🎨 Personalización

```java
ToggleStyleConfig config = new ToggleStyleConfig();
config.strokeWidth = 2f;
config.strokeWidthHover = 3f;
config.stateColors = new Integer[] { 0xFF444444, 0xFF2DBE60 };

view.setStyle(new ParametricToggleStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`ToggleDevSketch.java`](../src/com/cpz/processing/controls/dev/ToggleDevSketch.java)

---

## ⚠️ Consideraciones

- El control no dibuja directo desde el `ViewModel`
- La forma visual depende de `ToggleShapeRenderer`
- El input sigue el pipeline centralizado
- `Toggle` puede usarse como binario o multiestado

---

## 🔗 Relacionado

- `InputManager`
- `ToggleInputAdapter`
- `ThemeManager`
- `CircleShapeRenderer`
