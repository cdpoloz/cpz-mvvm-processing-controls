# NumericField

## 🧠 Concepto

Especialización de entrada numérica basada en texto con valor interno `BigDecimal`. Mantiene dos estados:

- valor numérico real
- buffer de texto

Esto permite edición parcial sin errores. Mientras el usuario escribe, el buffer puede contener estados intermedios válidos que todavía no deben convertirse en valor numérico definitivo.

---

## ⚙️ Componentes MVVM

- Model: `NumericFieldModel`
- ViewModel: `NumericFieldViewModel`
- View: `NumericFieldView`
- Style: `NumericFieldStyle`
- Renderer: `DefaultNumericFieldRenderer`

---

## 🚀 Uso básico

```java
NumericFieldModel model = new NumericFieldModel(
    new java.math.BigDecimal("12.50"),
    java.math.BigDecimal.ZERO,
    new java.math.BigDecimal("99.99"),
    new java.math.BigDecimal("0.25"),
    false,
    true,
    2
);

NumericFieldViewModel viewModel = new NumericFieldViewModel(model);
NumericFieldView view = new NumericFieldView(this, viewModel, 220f, 140f, 320f, 46f);

view.setPosition(260f, 180f);
view.setSize(360f, 48f);
view.setStyle(NumericFieldDefaultStyles.standard());
```

---

## 🎯 Interacción

```java
viewModel.setOnValueChanged(value -> System.out.println(value.toPlainString()));
java.math.BigDecimal currentValue = viewModel.getValue();
```

---

## 🎨 Personalización

```java
NumericFieldStyleConfig config = new NumericFieldStyleConfig();
config.textSize = 18f;

view.setStyle(new NumericFieldStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`NumericFieldDevSketch.java`](../src/com/cpz/processing/controls/dev/NumericFieldDevSketch.java)

---

## ⚠️ Consideraciones

- El parseo y commit viven en el `ViewModel`
- Soporta rueda, flechas y modificadores
- El valor numérico y el buffer de texto no son la misma cosa
- En commit, el valor se normaliza y se sincroniza de nuevo con el buffer

---

## 🔗 Relacionado

- `FocusManager`
- `KeyboardInputAdapter`
- `NumericFieldInputAdapter`
