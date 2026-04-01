# DropDown

## 🧠 Concepto

Selector de una opción con overlay desplegable integrado en el propio control.

---

## ⚙️ Componentes MVVM

- Model: `DropDownModel`
- ViewModel: `DropDownViewModel`
- View: `DropDownView`
- Style: `DefaultDropDownStyle`
- Renderer: `DefaultDropDownRenderer`

---

## 🚀 Uso básico

```java
DropDownModel model = new DropDownModel(
    java.util.List.of("Alpha", "Beta", "Gamma"),
    0
);

DropDownViewModel viewModel = new DropDownViewModel(model);
DropDownView view = new DropDownView(this, viewModel, 220f, 140f, 280f, 42f);

view.setPosition(260f, 180f);
view.setSize(300f, 42f);
view.setStyle(new DefaultDropDownStyle(new DropDownStyleConfig()));
```

---

## 🎯 Interacción

```java
viewModel.toggleExpanded();
viewModel.selectIndex(1);
String selectedText = viewModel.getSelectedText();
```

---

## 🎨 Personalización

```java
DropDownStyleConfig config = new DropDownStyleConfig();
config.cornerRadius = 10f;
config.listCornerRadius = 10f;
config.textSize = 16f;
config.maxVisibleItems = 6;

view.setStyle(new DefaultDropDownStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`DropDownDevSketch.java`](../src/com/cpz/processing/controls/dev/DropDownDevSketch.java)

---

## ⚠️ Consideraciones

- El overlay no es un control independiente
- El overlay sigue perteneciendo al mismo `DropDown`
- La `View` resuelve hit testing base + lista
- `OverlayManager` coordina el overlay activo y su prioridad
- La prioridad de input depende de `OverlayManager` e `InputManager`

---

## 🔗 Relacionado

- `OverlayManager`
- `DropDownOverlayController`
- `FocusManager`
- `ThemeManager`
