# Architecture

## 🧠 MVVM

- Model → estado puro, sin Processing
- ViewModel → lógica e interacción, sin coordenadas ni dibujo
- View → layout, hit testing y construcción de `ViewState`
- Style → resolución visual a partir de `ThemeTokens`
- RenderStyle → snapshot visual final para el frame actual
- Renderer → dibujo puro

---

## 🔄 Pipeline

```text
ViewState → Style → RenderStyle → Renderer
```

---

## 🎯 Principios

- Sin lógica en Renderer
- Sin Processing en Model/ViewModel
- Separación estricta de responsabilidades

---

## 🧭 Responsabilidades

- `Model` no valida interacción visual
- `ViewModel` no conoce píxeles ni hit testing
- `View` no toma decisiones de negocio
- `Style` no dibuja
- `Renderer` no resuelve reglas de interacción

---

## 🧩 Sistemas compartidos

- `InputManager` para pointer y keyboard
- `FocusManager` para foco de teclado
- `OverlayManager` para overlays con prioridad
- `ThemeManager` para theming runtime
- `LayoutConfig` y `LayoutResolver` para posicionamiento proporcional

---

## 🔗 Relacionado

- [Input System](input-system.md)
- [README](../README.md)
