# TextField

## 🧠 Concepto

Campo de texto editable con cursor, selección y foco.

---

## ⚙️ Componentes MVVM

- Model: `TextFieldModel`
- ViewModel: `TextFieldViewModel`
- View: `TextFieldView`
- Style: `TextFieldStyle` / `DefaultTextFieldStyle`
- Renderer: `DefaultTextFieldRenderer`

---

## 🚀 Uso básico

```java
TextFieldModel model = new TextFieldModel();
TextFieldViewModel viewModel = new TextFieldViewModel(model);
TextFieldView view = new TextFieldView(this, viewModel, 220f, 140f, 320f, 46f);

view.setPosition(260f, 180f);
view.setSize(360f, 48f);
view.setStyle(TextFieldDefaultStyles.standard());
```

---

## 🎯 Interacción

```java
viewModel.setText("Hello");
String currentText = viewModel.getText();
```

---

## 🎨 Personalización

```java
TextFieldStyleConfig config = new TextFieldStyleConfig();
config.textSize = 18f;

view.setStyle(new DefaultTextFieldStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`TextFieldDevSketch.java`](../src/com/cpz/processing/controls/dev/TextFieldDevSketch.java)

---

## ⚠️ Consideraciones

- El foco se integra con `FocusManager`
- `TextFieldInputAdapter` traduce input a intenciones
- El renderer no resuelve reglas de edición
- La selección y el cursor forman parte del estado del `ViewModel`

---

## 🔗 Relacionado

- `FocusManager`
- `KeyboardInputAdapter`
- `TextFieldInputAdapter`
