# Button

## 🧠 Concepto

Control de acción para disparar una operación puntual mediante click.

---

## ⚙️ Componentes MVVM

- Model: `ButtonModel`
- ViewModel: `ButtonViewModel`
- View: `ButtonView`
- Style: `ButtonStyle` / `DefaultButtonStyle`
- Renderer: `DefaultButtonRenderer` o `SvgButtonRenderer`

---

## 🚀 Uso básico

```java
ButtonModel model = new ButtonModel("Save");
ButtonViewModel viewModel = new ButtonViewModel(model);
ButtonView view = new ButtonView(this, viewModel, 120f, 80f, 180f, 48f);

view.setPosition(160f, 120f);
view.setSize(200f, 52f);
view.setStyle(ButtonDefaultStyles.primary());
```

---

## 🎯 Interacción

```java
viewModel.setClickListener(() -> System.out.println("Button clicked"));
viewModel.setText("Apply");
```

---

## 🎨 Personalización

```java
ButtonStyleConfig config = new ButtonStyleConfig();
config.cornerRadius = 12f;
config.strokeWeight = 2f;
config.strokeWeightHover = 3f;

view.setStyle(new DefaultButtonStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`ButtonDevSketch.java`](../src/com/cpz/processing/controls/dev/ButtonDevSketch.java)

---

## ⚠️ Consideraciones

- El input entra por `InputManager`
- `ButtonInputAdapter` traduce pointer a intenciones del control
- La `View` resuelve hit testing
- El `Renderer` solo dibuja

---

## 🔗 Relacionado

- `InputManager`
- `PointerInputAdapter`
- `ThemeManager`
- `ButtonInputAdapter`
