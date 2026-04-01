# Checkbox

## 🧠 Concepto

Control booleano para marcar o desmarcar una opción.

---

## ⚙️ Componentes MVVM

- Model: `CheckboxModel`
- ViewModel: `CheckboxViewModel`
- View: `CheckboxView`
- Style: `CheckboxStyle` / `DefaultCheckboxStyle`
- Renderer: `DefaultCheckboxRenderer` o `SvgCheckboxRenderer`

---

## 🚀 Uso básico

```java
CheckboxModel model = new CheckboxModel(false);
CheckboxViewModel viewModel = new CheckboxViewModel(model);
CheckboxView view = new CheckboxView(this, viewModel, 140f, 100f, 28f);

view.setPosition(180f, 140f);
view.setSize(32f);
view.setStyle(CheckboxDefaultStyles.standard());
```

---

## 🎯 Interacción

```java
viewModel.setChecked(true);
boolean checked = viewModel.isChecked();
```

---

## 🎨 Personalización

```java
CheckboxStyleConfig config = new CheckboxStyleConfig();
config.borderWidth = 2f;
config.borderWidthHover = 3f;
config.cornerRadius = 6f;

view.setStyle(new DefaultCheckboxStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`CheckboxDevSketch.java`](../src/com/cpz/processing/controls/dev/CheckboxDevSketch.java)

---

## ⚠️ Consideraciones

- La selección se gestiona en `ViewModel`
- La `View` solo resuelve geometría e interacción espacial
- El input pasa por `CheckboxInputAdapter`
- El renderer no contiene reglas de cambio de estado

---

## 🔗 Relacionado

- `InputManager`
- `CheckboxInputAdapter`
- `ThemeManager`
