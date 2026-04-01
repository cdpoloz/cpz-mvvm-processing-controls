# Label

## 🧠 Concepto

Control de texto pasivo para mostrar información o apoyar otros controles.

---

## ⚙️ Componentes MVVM

- Model: `LabelModel`
- ViewModel: `LabelViewModel`
- View: `LabelView`
- Style: `LabelStyle` / `DefaultLabelStyle`
- Renderer: `DefaultTextRenderer`

---

## 🚀 Uso básico

```java
LabelModel model = new LabelModel();
LabelViewModel viewModel = new LabelViewModel(model);
LabelView view = new LabelView(this, viewModel, 120f, 80f);

viewModel.setText("Status");
view.setPosition(160f, 120f);
view.setStyle(LabelDefaultStyles.defaultText());
```

---

## 🎯 Interacción

```java
viewModel.setText("Ready");
```

---

## 🎨 Personalización

```java
LabelStyleConfig config = new LabelStyleConfig();
config.textSize = 18f;
config.setAlign(HorizontalAlign.START, VerticalAlign.BASELINE);

view.setStyle(new DefaultLabelStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`LabelDevSketch.java`](../src/com/cpz/processing/controls/dev/LabelDevSketch.java)

---

## ⚠️ Consideraciones

- `Label` no depende de input
- El color final se resuelve con `ThemeManager`
- El renderer permanece puro
- Es un control pasivo: no necesita adapters de input

---

## 🔗 Relacionado

- `ThemeManager`
- `LabelStyleConfig`
- `LabelTypography`
