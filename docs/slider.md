# Slider

## 🧠 Concepto

Control continuo para seleccionar un valor dentro de un rango.

---

## ⚙️ Componentes MVVM

- Model: `SliderModel`
- ViewModel: `SliderViewModel`
- View: `SliderView`
- Style: `SliderStyle`
- Renderer: `SliderRenderer`

---

## 🚀 Uso básico

```java
SliderModel model = new SliderModel();
model.setMin(java.math.BigDecimal.ZERO);
model.setMax(java.math.BigDecimal.ONE);
model.setStep(new java.math.BigDecimal("0.05"));
model.setValue(new java.math.BigDecimal("0.45"));

SliderViewModel viewModel = new SliderViewModel(model);
SliderView view = new SliderView(
    this,
    viewModel,
    240f,
    180f,
    320f,
    64f,
    SliderOrientation.HORIZONTAL
);

view.setPosition(300f, 220f);
view.setSize(360f, 70f);
view.setStyle(SliderDefaultStyles.standard());
```

---

## 🎯 Interacción

```java
viewModel.setFormatter(value -> value.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
viewModel.setOnValueChanged(value -> System.out.println(value));
```

---

## 🎨 Personalización

```java
SliderStyleConfig config = new SliderStyleConfig();
config.trackThickness = 10f;
config.thumbSize = 28f;
config.showValueText = true;

view.setStyle(new SliderStyle(config));
```

---

## 🧪 Ejemplo completo

Ver [`SliderDevSketch.java`](../src/com/cpz/processing/controls/dev/SliderDevSketch.java)

---

## ⚠️ Consideraciones

- Soporta drag y rueda del ratón
- El valor visible puede formatearse desde el `ViewModel`
- La geometría sigue en `View`
- La orientación se define en `SliderView`

---

## 🔗 Relacionado

- `InputManager`
- `SliderInputAdapter`
- `SliderOrientation`
- `SnapMode`
