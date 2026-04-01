# RadioGroup

## 🧠 Concepto

Grupo de opciones mutuamente excluyentes para seleccionar una sola alternativa.

---

## ⚙️ Componentes MVVM

- Model: `RadioGroupModel`
- ViewModel: `RadioGroupViewModel`
- View: `RadioGroupView`
- Style: `RadioGroupStyle`
- Renderer: `DefaultRadioGroupRenderer`

---

## 🚀 Uso básico

```java
RadioGroupModel model = new RadioGroupModel(
    java.util.List.of("Mercury", "Venus", "Earth"),
    1
);

RadioGroupViewModel viewModel = new RadioGroupViewModel(model);
RadioGroupView view = new RadioGroupView(this, viewModel, 220f, 140f, 240f);

view.setPosition(260f, 180f);
view.setStyle(RadioGroupDefaultStyles.standard());
```

---

## 🎯 Interacción

```java
viewModel.setOnOptionSelected(index -> System.out.println("Selected: " + index));
int selectedIndex = viewModel.getSelectedIndex();
```

---

## 🎨 Personalización

```java
RadioGroupStyleConfig config = new RadioGroupStyleConfig();
config.itemHeight = 30f;
config.itemSpacing = 8f;
config.textSize = 16f;

view.setStyle(new RadioGroupStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`RadioGroupDevSketch.java`](../src/com/cpz/processing/controls/dev/RadioGroupDevSketch.java)

---

## ⚠️ Consideraciones

- El índice desde coordenadas se calcula en la `View`
- El `ViewModel` controla selección y navegación por teclado
- El renderer solo consume `RenderStyle`
- `RadioGroup` representa selección exclusiva, no múltiple

---

## 🔗 Relacionado

- `InputManager`
- `FocusManager`
- `RadioGroupInputAdapter`
