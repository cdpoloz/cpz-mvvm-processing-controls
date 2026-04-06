# Slider

## Concept

Continuous range control for selecting a value between bounds.

## MVVM pieces

- Model: `SliderModel`
- ViewModel: `SliderViewModel`
- View: `SliderView`
- Style: `SliderStyle`
- Renderer: `SliderRenderer`

## Basic usage

```java
SliderModel model = new SliderModel();
model.setMin(java.math.BigDecimal.ZERO);
model.setMax(java.math.BigDecimal.ONE);
model.setStep(new java.math.BigDecimal("0.05"));
model.setValue(new java.math.BigDecimal("0.45"));

SliderViewModel viewModel = new SliderViewModel(model);
SliderView view = new SliderView(this, viewModel, 240f, 180f, 320f, 64f, SliderOrientation.HORIZONTAL);
view.setStyle(SliderDefaultStyles.standard(themeManager));
```

## Related

- `InputManager`
- `SliderInputAdapter`
- `SliderOrientation`
- `SnapMode`
- `ThemeProvider`
