# Línea base de caracterización estructural

Este documento registra la primera fase de consolidación estructural de
`cpz-mvvm-processing-controls`. Distingue deliberadamente:

- **hecho observado**: respaldado directamente por código, pruebas,
  documentación o un comando reproducible;
- **inferencia razonable**: comportamiento deseable sugerido por la
  arquitectura, pero todavía no convertido en contrato explícito;
- **decisión pendiente**: el comportamiento actual y el contrato deseable
  difieren, o el repositorio no aporta evidencia suficiente;
- **recomendación**: cambio propuesto para una tarea posterior que sí pueda
  modificar producción.

Esta fase no modifica producción, firmas públicas, visibilidad, paquetes,
dependencias, configuración Maven ni el contrato de medidas relativas.

## 1. Línea base y comandos ejecutados

### Estado anterior a las ediciones

- Rama: `master`, siguiendo `origin/master`.
- Commit: `904c680`.
- Tag exacto: `v0.9.10`.
- Versión Maven: `0.9.10` en `pom.xml`.
- `git status --short --branch` inicial: limpio
  (`## master...origin/master`).
- Estructura: un único módulo Maven de tipo `jar`. Producción, launcher y
  ejemplos Processing están bajo `src/main/java`; `main/**` y `examples/**`
  quedan fuera del JAR binario, del JAR de fuentes y de Javadocs mediante la
  configuración de `pom.xml`.

Había artefactos ignorados bajo `target/`, incluidos paquetes antiguos
`0.9.9`. No se consideraron estado fuente y no se eliminaron.

### Validación anterior a las ediciones

```text
git status --short --branch
git rev-parse --short HEAD
git describe --tags --exact-match HEAD
mvn --batch-mode --no-transfer-progress test
mvn --batch-mode --no-transfer-progress -DskipTests javadoc:javadoc
```

| Validación inicial | Tests | Fallos | Errores | Omitidos | Resultado |
|---|---:|---:|---:|---:|---|
| `mvn test` | 438 | 0 | 0 | 0 | `BUILD SUCCESS` |
| `mvn -DskipTests javadoc:javadoc` | n/a | n/a | n/a | n/a | `BUILD SUCCESS` |

Ambos comandos emitieron una advertencia del proceso Maven sobre
`sun.misc.Unsafe::objectFieldOffset`, originada por la copia de Guava incluida
con Maven. La ejecución incremental inicial no recompiló producción.

### Validación posterior

```text
mvn --batch-mode --no-transfer-progress \
  -Dtest=StructuralInputFocusCharacterizationTest,OverlayAndDropDownRegistryCharacterizationTest,NestedPanelCharacterizationTest \
  test
mvn --batch-mode --no-transfer-progress test
mvn --batch-mode --no-transfer-progress -DskipTests javadoc:javadoc
git diff --check
git diff --no-index --check /dev/null <cada-archivo-nuevo>
```

| Validación final | Tests | Fallos | Errores | Omitidos | Resultado |
|---|---:|---:|---:|---:|---|
| Tres clases nuevas de caracterización | 22 | 0 | 0 | 0 | `BUILD SUCCESS` |
| Suite completa | 460 | 0 | 0 | 0 | `BUILD SUCCESS` |
| Javadocs | n/a | n/a | n/a | n/a | `BUILD SUCCESS` |

Las comprobaciones `--check` no emitieron diagnósticos de whitespace. En los
archivos no rastreados, `git diff --no-index` devuelve código 1 porque existe
una diferencia respecto de `/dev/null`; no indica un fallo de formato.

Durante el desarrollo, la primera ejecución aislada de input/foco detectó que
el fixture de `NumericField` requería `PApplet.g`. Se corrigió exclusivamente
el fixture mediante `ProcessingTestSupport.graphics`; no se cambió producción
ni se debilitó ninguna aserción.

### Validación posterior a la corrección de H1

La fase posterior que corrige recepción y consumo convirtió las
caracterizaciones defectuosas en regresiones y añadió seis casos. Su
validación final fue:

| Validación H1 | Tests | Fallos | Errores | Omitidos | Resultado |
|---|---:|---:|---:|---:|---|
| `StructuralInputFocusCharacterizationTest` | 16 | 0 | 0 | 0 | `BUILD SUCCESS` |
| Suite completa | 466 | 0 | 0 | 0 | `BUILD SUCCESS` |
| `mvn -DskipTests javadoc:javadoc` | n/a | n/a | n/a | n/a | `BUILD SUCCESS` |

La suite específica conjunta de input/foco, Panel, DropDown y overlays ejecutó
65 tests sin fallos antes de añadir la última regresión de oclusión de
Checkbox; la clase de input/foco se volvió a ejecutar después con sus 16 casos.

### Validación posterior a la corrección de H2

La autoridad de foco se acotó a cada `InputManager`. Las caracterizaciones de
foco múltiple se transformaron en regresiones y se añadieron casos de
transferencia, aislamiento y lifecycle.

- Estado inicial de esta fase: rama `master`, commit `0502053`, working tree
  limpio y versión Maven `0.9.10`.
- La primera ejecución de las 23 pruebas de input/foco, antes de cambiar
  producción, produjo 7 fallos que reproducían exclusividad, transferencia,
  aislamiento y liberación ausentes.

| Validación H2 final | Tests | Fallos | Errores | Omitidos | Resultado |
|---|---:|---:|---:|---:|---|
| `StructuralInputFocusCharacterizationTest` | 23 | 0 | 0 | 0 | `BUILD SUCCESS` |
| Foco, Panel, DropDown, overlays y caracterización anidada | 90 | 0 | 0 | 0 | `BUILD SUCCESS` |
| Suite completa | 473 | 0 | 0 | 0 | `BUILD SUCCESS` |
| `mvn -DskipTests javadoc:javadoc` | n/a | n/a | n/a | n/a | `BUILD SUCCESS` |

El incremento de 466 a 473 corresponde a siete pruebas nuevas; las
caracterizaciones ya existentes se adaptaron sin eliminar casos. Maven
mantiene la advertencia no bloqueante de Guava sobre
`sun.misc.Unsafe::objectFieldOffset`.

### Validación posterior a la corrección de H3

`OverlayManager.clearAll()` pasó de una purga silenciosa a un cierre
coordinado mediante los callbacks de lifecycle ya presentes.

- Estado inicial de esta fase: rama `master`, commit `0d62d9b`, working tree
  limpio y versión Maven `0.9.10`.
- La primera ejecución test-first de las 14 pruebas de caracterización y
  manager produjo 6 fallos: los tres productores desincronizados, callbacks no
  ejecutados y foco no restaurado.

| Validación H3 | Tests | Fallos | Errores | Omitidos | Resultado |
|---|---:|---:|---:|---:|---|
| Caracterización de productores y `OverlayManagerTest` | 16 | 0 | 0 | 0 | `BUILD SUCCESS` |
| Suite completa | 478 | 0 | 0 | 0 | `BUILD SUCCESS` |
| `mvn -DskipTests javadoc:javadoc` | n/a | n/a | n/a | n/a | `BUILD SUCCESS` |

El incremento de 473 a 478 corresponde a cuatro pruebas nuevas de
`OverlayManager` y una prueba de coexistencia de productores. Se mantienen las
advertencias no bloqueantes ya registradas de Guava, API deprecada y
operaciones unchecked.

## 2. Inventario y clasificación de API pública

La inspección mecánica inicial encontró 258 fuentes de producción fuera de
`examples/**` y `main/**`; la adición de `FocusManagerAware` en H2 eleva el
estado actual a 259, de las cuales 258 contienen una declaración top-level
pública.
Como el JAR solo excluye launcher y ejemplos, casi toda la implementación es
técnicamente accesible. La tabla agrupa tipos con la misma evidencia y el
mismo riesgo; no implica que todos los integrantes tengan que compartir
implementación.

Escala de riesgo:

- **bajo**: el tipo no cruza normalmente una firma o construcción de consumo;
- **medio**: el tipo es enlazable y podría haber uso externo no documentado;
- **alto**: consumidores lo construyen, implementan, reciben o persisten.

| Tipo cualificado o familia | Clasificación propuesta | Evidencia del repositorio | Riesgo | Recomendación provisional |
|---|---|---|---|---|
| `com.cpz.processing.controls.controls.Control` | API soportada | README, `docs/control.md`, composición de `Panel`, retorno JSON y tests | Alto | Conservar como interfaz mínima |
| `controls.PointerRoutableControl`, `KeyboardRoutableControl`, `ParentSizeAwareControl`, `ParentContextAwareControl` | Infraestructura pública por necesidad técnica | `Panel` enruta y propaga contexto mediante estas capacidades opcionales | Alto | Conservar y documentar sus contratos exactos |
| Fachadas `Button`, `Checkbox`, `Toggle`, `Slider`, `Label`, `RadioGroup`, `TextField`, `NumericField`, `DropDown`, `Panel`, `Indicator`, `ProgressBar` | API soportada | README, documentos por control, ejemplos, factories JSON y tests | Alto | Conservar construcción y fachadas |
| `controls.geometry.ControlBounds`, `ControlMeasure`, `MeasureMode`, `ResolvedBounds` | API soportada | Constructores/setters públicos, JSON, documentación y tests de geometría relativa | Alto | Preservar la semántica actual |
| `common.binding.ValueListener<T>`, `button.util.ButtonListener` | Punto de extensión/callback | Setters públicos y ejemplos de binding | Alto | Conservar; completar contrato de callback/null |
| `core.input.PointerEvent`, `KeyboardEvent`, `InputLayer`, `DefaultInputLayer`, `InputManager` | Infraestructura pública y punto de extensión | `docs/input-system.md`, ejemplos y todas las capas | Alto | Formalizar consumo, prioridad y mutación durante dispatch |
| `controls.*.input.*InputLayer` | Infraestructura pública por necesidad técnica | La documentación de controles exige registrarlas en `InputManager` | Alto | Conservar firmas; corregir semántica solo con regresiones |
| `core.focus.Focusable`, `FocusManager`, `FocusManager.FocusToken`, `FocusManagerAware` | Infraestructura pública | Usados por controles editables, overlays y `InputManager`; `docs/architecture.md` | Medio/alto | Documentar el ámbito por `InputManager`; conservar compatibilidad |
| `core.overlay.OverlayManager`, `OverlayEntry` | Infraestructura pública | Construcción en ejemplos/docs de dropdown, tooltip y notification | Alto | Definir cierre coordinado frente a purga |
| `core.overlay.tooltip.Tooltip`, `TooltipTarget`, `TooltipAttachable`, `TooltipArea`, `TooltipBounds` | API soportada/punto de extensión | `docs/tooltip.md`, APIs de fachadas, ejemplos y tests | Alto | Conservar |
| `TooltipOverlayController` y tipos tooltip de input/config/factory | Infraestructura pública | El host construye el controller; JSON y ejemplos lo exponen | Alto | Conservar construcción y documentar lifecycle |
| `Notification`, `NotificationManager`, `NotificationSeverity`, `NotificationPlacement`, `NotificationPosition`, `NotificationStyle` | API soportada | `docs/notification.md`, ejemplos, JSON independiente y tests | Alto | Conservar fuera de `Control` |
| Config/loaders de notification | API soportada | Esquema JSON independiente, ejemplos y tests de fallbacks | Alto | Preservar esquema y aplicación parcial |
| `controls.config.ControlConfigLoader` | API soportada | Entrada JSON canónica en README/docs/ejemplos | Alto | Conservar |
| `controls.*.config.*ConfigLoader`, `*Config`, `*Factory` | Clasificación pendiente | Loaders/configs son públicos; algunos constructores tienen tests explícitos de compatibilidad; factories se usan sobre todo desde el registro interno | Medio/alto | Inventariar uso publicado individual antes de deprecar |
| Interfaces públicas `*Style` de Button/Checkbox/Toggle/Label/TextField y `*Renderer` | Punto de extensión | Firmas `setStyle`, constructores de estilo y alternativas SVG/shape | Alto | Conservar como extensiones |
| Estilos concretos `SliderStyle`, `RadioGroupStyle`, `NumericFieldStyle`, `DefaultDropDownStyle`, `PanelStyle`, `IndicatorStyle`, `ProgressBarStyle` | API soportada | Setters, documentos, ejemplos y factories JSON | Alto | Conservar; la diferencia con familias basadas en interfaz es legítima |
| `Theme`, `ThemeProvider`, `ThemeManager`, `ThemeSnapshot`, `ThemeTokens`, temas concretos | API soportada/punto de extensión | README, `docs/theme.md`, ejemplos y consumo desde estilos | Alto | Conservar propiedad por sketch |
| `controls.*.model.*`, `viewmodel.*`, `view.*`, `state.*` | Implementación aparentemente pública por accidente | Las fachadas las construyen internamente; README las llama internals ocultos | Medio | No cambiar ahora; crear allowlist de API antes de reducir visibilidad |
| `controls.*.input.*InputAdapter` y controllers internos salvo los documentados | Implementación aparentemente pública | Instanciados por fachadas, no por el uso ordinario documentado | Medio | Investigar/deprecar en el futuro, nunca ocultar directamente |
| Records `style.*RenderStyle` y la mayoría de `style.render.Default*Renderer` | Clasificación pendiente | Algunos son necesarios para implementar renderers públicos; otros son payload interno | Medio/alto | Resolver tipo por tipo antes de ocultar |
| `core.model.*`, `core.view.*`, `core.viewmodel.*`, soporte de hit-test | Implementación pública accidental o sustrato de extensión | MVVM interno; poca documentación de consumo directo | Medio | Separar lo requerido por extensiones de lo puramente interno |
| `core.layout.Anchor`, `LayoutConfig`, `LayoutResolver` | Clasificación pendiente | Referenciados internamente por vistas; sin uso encontrado en fachadas, ejemplos o docs actuales | Medio | Tratar como legado/experimental hasta decisión compatible |
| `com.cpz.processing.controls.util.Util` | Clasificación pendiente | Usado por ejemplos/binding; sus errores mencionan configuración de ejemplos | Medio | Decidir promoción o futura deprecación compatible |
| `com.cpz.processing.controls.input.KeyboardState`, `ProcessingKeyboardAdapter` | Infraestructura pública | Frontera con el host y documentación de input | Medio/alto | Conservar y diferenciar de `core.input` |

La clasificación es deliberadamente conservadora: que un tipo parezca interno
no autoriza a romper enlace binario. Tampoco se propone obligar a `Panel`,
`Indicator`, `ProgressBar` o `Notification` a adoptar MVVM.

## 3. Matriz contractual de input, foco y solapamiento

La matriz se escribió antes de las aserciones. “Esperado” expresa el contrato
que debe decidirse o preservarse en producción futura, no una legitimación
automática del comportamiento caracterizado.

| Precondición | Receptor esperado | ¿Consumir? | Foco posterior esperado | Prioridad | Estado contractual |
|---|---|---|---|---|---|
| Control visible, puntero dentro | Control/capa elegible superior | Sí para eventos soportados | Adquiere foco si procede | Prioridad de capa y regla explícita dentro de capa | Confirmado por regresión para las capas corregidas |
| Control visible, puntero fuera | Puede observar para limpiar hover/foco, sin bloquear el inferior | No, salvo captura/drag activo | Puede liberar foco en press exterior | Continúa a capas inferiores | Confirmado por regresión para las capas corregidas |
| Control oculto | Ningún receptor de acción | No | No debe adquirir ni retener foco utilizable | Continúa | Confirmado por regresión para Checkbox |
| Control deshabilitado visible, dentro | Sin acción; puede actuar como geometría oclusiva | Sí cuando la familia documenta bloqueo | No adquiere foco | Z/prioridad normal | Confirmado para Button/Toggle/Panel y por regresión para Checkbox |
| Control deshabilitado, fuera | Ningún receptor de acción | No | Sin foco nuevo | Continúa | Inferencia |
| Dos controles no solapados | El que contiene el evento | Solo ese | Como máximo el interactuado | Una capa ajena fuera de bounds no debe bloquear | Confirmado por regresión para Checkbox, TextField, NumericField y RadioGroup |
| Dos controles solapados en capas distintas | Control de capa superior elegible | Sí | El superior si es focusable | `InputManager` descendente | Confirmado |
| Dos controles solapados en una capa multicontrol | Decisión pendiente: previsiblemente último/topmost | Una vez | Un único receptor | Requiere orden intrapa explícito | Pendiente; algunas capas difunden |
| Dos controles focusables de la misma familia en un `InputManager` | Segundo control tras hacer click | Sí | Solo el segundo | Prioridad visual/routing | Confirmado por regresión para TextField |
| Dos familias focusables en un `InputManager` | Control interactuado | Sí | Un único foco en el gestor | Prioridad visual/routing | Confirmado por regresión para TextField/NumericField, RadioGroup y DropDown |
| Click en espacio vacío del panel/host | Ningún control de acción | Normalmente no | Foco liberado en el ámbito aplicable | Continúa, salvo contenedor modal | Confirmado para TextField/NumericField dentro de Panel |
| Controles en dos `InputManager` distintos | Solo el control del gestor que recibe el evento | Según ese gestor | Un propietario independiente por gestor | El límite del gestor precede al orden de capas | Confirmado por regresión |
| Control normal bajo overlay capturador | Overlay | Sí | Política del overlay | Overlay antes de capas normales | Confirmado |
| Campo de `DropDown` abierto | Dropdown actual | Sí | Sigue abierto/focado o alterna según acción | Overlay abierto | Confirmado |
| Opción visible de `DropDown` abierto | Opción del dropdown actual | Sí | Selecciona, cierra y libera | Overlay abierto | Confirmado |
| Segundo dropdown fuera del campo/lista visibles del actual | Sibling después de cerrar actual | Sí | Segundo abierto/focado | Transferencia tras rechazo geométrico del actual | Confirmado |
| Segundo dropdown cubierto por opción visible del actual | Opción del actual | Sí | Actual selecciona/cierra; sibling cerrado | Overlay actual | Confirmado |
| Dropdowns en sketches distintos | Solo el del host que hace dispatch | Según ese host | Otro host inalterado | Límite de host antes que z-order | Contrato deseado inferido; actual contradice |
| Cierre normal por API/callback/outside/dispose | Manager y productor sincronizados | Depende del camino | Estado transitorio coherente | n/a | Confirmado para caminos normales |
| `OverlayManager.clearAll()` | Cada `OverlayEntry.onClose`; fallback de desregistro para entradas sin callback | n/a | Productores cerrados y foco restaurado como en el cierre normal | Orden inverso de alta para overlays con foco; z-order para el resto | Confirmado por regresión |
| Nueva interacción tras `clearAll()` | Productor cerrado vuelve a registrar por su ruta normal | Según interacción normal | Coherente con registro visible | Prioridad normal | Confirmado para DropDown, Tooltip y Notification |
| Panel raíz y control hijo | Hijo recibe coordenadas locales y renderiza desplazado | Según bounds | Según hijo | Último hijo gana en Panel | Confirmado |
| Panel dentro de Panel, descendiente normal | Descendiente en suma de offsets | Según bounds | Según descendiente | Orden anidado | Inferencia confirmada por nueva prueba |
| Panel anidado y dropdown descendiente | Campo local; overlay con offset global total una vez | Sí en posición renderizada | Dropdown abierto/focado | Overlay al abrir | Contrato deseado; actual contradice |
| Medidas relativas anidadas | Cada medida resuelve contra su padre inmediato; posición global suma ancestros | Según bounds resueltos | Según control | Normal/overlay | Fórmula confirmada; overlay actual contradice |

## 4. Mapa entre riesgos y pruebas añadidas

| Riesgo | Pruebas de caracterización | Cobertura y naturaleza |
|---|---|---|
| Consumo fuera de bounds, oculto/deshabilitado y prioridad | `StructuralInputFocusCharacterizationTest`: `checkboxPressOutsideItsBoundsContinuesToEligibleLowerLayer`, `invisibleCheckboxDoesNotConsumeOutOfBoundsPress`, `textFieldOutsidePressReleasesFocusWithoutBlockingLowerLayer`, regresiones de NumericField/RadioGroup y oclusión de Button/Checkbox deshabilitados | Las caracterizaciones originales se convirtieron en regresiones del contrato corregido |
| Consumo de teclado | `unfocusedRadioGroupDoesNotConsumeKeyboardEvent`, `focusedRadioGroupConsumesKeyboardEventAndMovesSelection` | Confirma ausencia de consumo sin foco y consumo/efecto con foco |
| Captura iniciada dentro | `textFieldCaptureConsumesDragAndReleaseOutsideAfterEligiblePress` | Conserva DRAG/RELEASE exteriores hasta terminar la interacción |
| Exclusividad/liberación de foco | `sameInputManagerKeepsFocusExclusiveBetweenTextFieldsInPanel`, `sameInputManagerTransfersFocusBetweenTextAndNumericInBothDirections`, `differentInputManagersKeepIndependentFocusScopes`, `keyboardRoutingFollowsNewOwnerAfterCrossFamilyTransfer`, regresiones de deshabilitado, retirada, Panel, RadioGroup y DropDown | Misma familia, familias distintas, aislamiento por gestor, teclado y lifecycle observable |
| Prioridad del dropdown abierto | `openDropDownVisibleOptionHasPriorityOverOverlappingLowerControl` | Preserva explícitamente la prioridad contractual |
| Ciclo normal frente a `clearAll()` | Regresiones individuales y mixtas de DropDown, Tooltip y Notification en `OverlayAndDropDownRegistryCharacterizationTest`; mutación de callbacks, idempotencia, altas durante cierre, excepciones y restauración de foco en `OverlayManagerTest` | Cierre coordinado reutiliza `OverlayEntry.onClose` sin imponer implementación común a los productores |
| Registro global de dropdowns | `currentStaticRegistryTransfersPressAcrossDifferentSketchHosts`, prueba de `dispose()` y prueba de dispose abierto | Observable entre hosts; reflexión solo para contar altas/bajas |
| Panel directo/anidado y offsets | Cuatro pruebas de `NestedPanelCharacterizationTest` | Render, hit-test, overlay global, medidas absolutas/relativas |

La reflexión se limita al tamaño de
`DropDownOverlayController.CONTROLLERS`, porque no existe una API pública de
consulta de lifecycle. El aislamiento entre sketches y el enrutado se prueban
por efectos observables. No se intenta caracterizar por reflexión la forma
interna de las entradas.

## 5. Hallazgos confirmados

### H1 — capas que consumían fuera de su propiedad geométrica — corregido

**Hecho originalmente caracterizado.** `CheckboxInputLayer` devolvía `true`
para todo evento no-wheel aunque `Checkbox.canConsumePointerEvent` fuese
`false`; TextField y RadioGroup presentaban la misma separación incorrecta, y
RadioGroup consumía teclado sin foco. La revisión completa confirmó además que
`NumericFieldInputLayer` usaba el foco posterior como sustituto del consumo,
por lo que podía absorber un DRAG o RELEASE exterior no capturado.

**Estado corregido.** `CheckboxInputLayer`, `TextFieldInputLayer`,
`RadioGroupInputLayer` y `NumericFieldInputLayer` consultan ahora la capacidad
de la fachada antes de entregar el evento y devuelven `true` solo si el control
era elegible o la capa mantenía una captura iniciada por un PRESS elegible
(`handlePointerEvent`, líneas 24-41 en cada capa). La entrega sigue ocurriendo
fuera de bounds para limpiar hover, selección o foco, pero ya no bloquea capas
inferiores. TextField, NumericField y RadioGroup consultan también
`canConsumeKeyboardEvent`; RadioGroup no consume teclado sin foco.

**Contratos preservados.** La captura consume DRAG/RELEASE exteriores hasta
RELEASE. La elegibilidad geométrica de las fachadas sigue incluyendo controles
visibles deshabilitados dentro de bounds, por lo que su oclusión no se vuelve
transparente. La semántica de consumo de las capas de overlay y DropDown no se
modificó; la capa base de DropDown solo adquirió la capacidad de vinculación de
foco descrita en H2.

### H2 — el foco no era exclusivo dentro del `InputManager` — corregido

**Hecho originalmente caracterizado.** `TextField`, `NumericField`,
`RadioGroup` y `DropDown`
crean su propio `FocusManager` (por ejemplo `TextField.java:80`,
`NumericField.java:85`, `RadioGroup.java:102`, `DropDown.java:116`). En un
`Panel`, dos TextFields quedan simultáneamente enfocados; también TextField y
NumericField. El routing del Panel se detiene tras el hijo consumidor, por lo
que el anterior no observa el click sobre el nuevo. Un click en área vacía sí
visita ambos y libera ambos focos.

**Decisión aplicada.** El ámbito es un `InputManager`: cada instancia posee un
`FocusManager` y no comparte estado estático. `FocusManagerAware` expresa la
capacidad opcional de participar en ese ámbito. `InputManager.registerLayer`
vincula las capas focusables y `unregisterLayer` las desvincula; `Panel`
propaga la misma autoridad a descendientes sin convertirse en propietario.

**Estado corregido.** Los gestores locales de `TextField`, `NumericField`,
`RadioGroup` y `DropDown` actúan como delegados de la autoridad mientras su
capa está registrada. Conceder foco revoca primero el propietario anterior del
mismo gestor. Dos gestores conservan autoridades independientes. Retirar una
capa o un hijo focusable de Panel libera su ownership activo. Las fachadas y
sus métodos públicos de foco se mantienen.

**Límite de lifecycle.** TextField, NumericField y RadioGroup no ofrecen
`dispose()`; su salida observable es `InputManager.unregisterLayer` o
`Panel.remove`. `DropDown.dispose()` conserva su contrato existente y no se
amplía en esta fase: el consumidor sigue siendo responsable de retirar su capa
base del routing cuando corresponda.

### H3 — `OverlayManager.clearAll()` desincronizaba a los productores — corregido

**Hecho originalmente caracterizado.** `OverlayManager.clearAll` descartaba
registros y tokens sin ejecutar `OverlayEntry.onClose`.

- DropDown queda `isExpanded()==true` sin overlay visual; el primer click en
  un control inferior es capturado por su capa interna y cierra el estado
  fantasma. Después puede abrirse de nuevo.
- Tooltip conserva `registered=true`; volver a mostrarlo no lo registra hasta
  que el controller se resetea mediante su API normal.
- Notification conserva notificaciones y `registered=true`; añadir otra no
  vuelve a registrar el overlay. `NotificationManager.clear()` seguido de
  una nueva notificación sí recupera el camino normal.

**Impacto.** Hay estado visual y de input divergente. Los tres productores no
necesitan compartir implementación, pero sí una semántica explícita de purga o
cierre.

**Estado corregido.** `clearAll()` ejecuta el callback de cierre normal de cada
entrada mediante snapshots estables y usa `unregister` como fallback para
entradas sin callback. DropDown ejecuta `closeOverlay`, Tooltip ejecuta
`hideTooltip` y Notification ejecuta `clear`; los tres actualizan así su
estado propio antes de reutilizarse. Las entradas con foco se cierran en orden
inverso al alta para preservar la restauración normal.

La operación incluye cualquier overlay nuevo registrado por un callback,
notifica una sola vez cada identidad durante la limpieza y es idempotente. Si
uno o más callbacks lanzan una excepción runtime, se completa el cierre del
resto y se relanza la primera, adjuntando las demás como suprimidas. No se
añadió una API pública de purga administrativa silenciosa.

### H4 — el coordinador estático de dropdowns cruza sketches

**Hecho observado.** `DropDownOverlayController.CONTROLLERS` es una lista
estática (`DropDownOverlayController.java:29`) y
`routePressToSibling` itera toda la lista (`:121-132`) sin filtrar por
`InputManager`, `OverlayManager` o `PApplet`. Un evento enviado por el manager
del primer host puede cerrar su dropdown y abrir el de un segundo sketch.

`DropDown.dispose()` sí elimina la instancia del registro y de los managers,
y ciclos repetidos vuelven al recuento inicial. Por tanto, la hipótesis
“dispose no limpia” queda descartada. Si se dispone abierto, el flag
`isExpanded` permanece `true`; la prueba lo documenta como estado terminal, no
como autorización para reutilizar una instancia dispuesta.

**Impacto.** Sin `dispose()` hay una referencia fuerte estática y posible
contaminación entre hosts/tests. No se añadió una prueba GC deliberadamente
frágil; el riesgo de retención se deduce de la referencia estática observada.

### H5 — el fallo anidado está acotado al contexto global del overlay

**Hecho observado.**

- Panel raíz + hijo funciona.
- Panel raíz + Panel anidado + Button renderiza y hace hit-test en la suma
  exacta de offsets.
- El campo cerrado de un DropDown anidado también renderiza y recibe input en
  esa suma.
- Al abrirlo, el overlay pierde exactamente el offset del Panel raíz; no hay
  doble offset. El mismo resultado aparece con medidas relativas.
- Las medidas relativas siguen resolviéndose contra el padre inmediato:
  canvas `800x600` → raíz `(80,60,300,180)` → panel anidado
  `(30,36,90,90)` → dropdown relativo dentro de este.

**Causa probable, no corrección aplicada.** `Panel.applyParentContextTo`
entrega al hijo únicamente su propio `(x,y)` resuelto, mientras
`ParentContextAwareControl` documenta un offset en espacio global. El Panel
anidado no acumula el contexto recibido al propagárselo a sus hijos.

### H6 — la prioridad ya documentada del dropdown se mantiene

**Hecho observado.** Una opción visible de un DropDown abierto recibe el click
antes que un Button inferior en la misma posición. Las pruebas existentes de
`DropDownPanelCompositionTest` siguen cubriendo transferencia entre dropdowns
y geometría de una sola profundidad. Esta diferencia frente a controles
normales es intencional y debe preservarse.

## 6. Hipótesis descartadas o pendientes

### Descartadas o acotadas

- **“La composición de Paneles anidados aplica doble offset”.** Refutada. El
  descendiente normal y el campo cerrado usan la suma correcta; el defecto es
  la ausencia de un offset ancestro en el overlay global.
- **“Los Paneles anidados están rotos en general”.** Refutada. Render e input
  normal coinciden.
- **“`dispose()` no elimina dropdowns del registro”.** Refutada para llamadas
  explícitas: restaura el recuento y evita transferencia cross-host.
- **“`clearAll()` inutiliza permanentemente el DropDown”.** Corregida:
  `clearAll()` sigue ahora la ruta normal de cierre, elimina la captura y
  permite reabrirlo; Tooltip y Notification también vuelven a registrarse.

### Pendientes

- Regla z-order dentro de una misma capa multicontrol. `InputManager` define
  prioridad entre capas, no necesariamente entre controles de la misma capa.
- API oficial individual de configs, factories, payloads de renderer y tipos
  `core.*`; el agrupamiento actual necesita una allowlist revisada.
- Comportamiento de concurrencia o modificación de registros durante dispatch.
- Recolección real de memoria sin `dispose()`: la retención fuerte se demuestra
  por estructura, no mediante una prueba GC no determinista.

## 7. Contradicciones entre código, tests y documentación

1. Resuelta: `PointerRoutableControl.canConsumePointerEvent` representa
   propiedad geométrica y las cuatro capas corregidas ya respetan la capacidad
   de sus fachadas, manteniendo captura separada.
2. `docs/input-system.md:82-83` recomienda capas compartidas por tipo; Checkbox,
   RadioGroup, TextField, NumericField y DropDown reciben una sola fachada.
3. Resuelta: `docs/architecture.md` y `docs/input-system.md` delimitan ahora la
   autoridad de foco al `InputManager`; los gestores locales se vinculan
   automáticamente durante el registro de capas.
4. Resuelta: el nombre y Javadoc de `OverlayManager.clearAll()` describen una
   operación de lifecycle y el código ejecuta los callbacks de cierre.
5. README `:19` afirma que no hay estado global/singletons en el contexto de
   theming, mientras el coordinador de dropdowns sí mantiene una lista estática.
   La afirmación debe acotarse, no reinterpretarse como prohibición absoluta.
6. README `:20` llama ocultos a los internals MVVM aunque sus tipos y Javadocs
   sean públicos y estén dentro del JAR.
7. `ParentContextAwareControl:15-19` exige offset global, pero la propagación
   anidada solo conserva el padre inmediato.
8. La documentación de Panel describe correctamente composición runtime,
   dropdown global y ausencia de `Panel.children` JSON, pero no advierte la
   limitación actual para más de una profundidad.

Las pruebas con nombres `current...` que permanecen caracterizan únicamente
los defectos todavía pendientes del registro de dropdowns y paneles anidados.
Las caracterizaciones de H1, H2 y H3 se renombraron y transformaron en
regresiones de los contratos corregidos.

## 8. Correcciones de producción recomendadas

| Prioridad | Tarea posterior | Cambio conceptual | Evidencia | Riesgo/compatibilidad |
|---|---|---|---|---|
| Alta | Acotar coordinación de dropdown al host | Registro propiedad del host/coordinador, manteniendo `dispose()` | H4 | Medio; afecta transferencia entre dropdowns. Preservar contrato dentro del mismo sketch |
| Media | Propagar contexto acumulado en Panel anidado | Entregar a descendientes el offset global acumulado, sin alterar coordenadas locales ni medidas relativas | H5 | Medio; regresión en overlays/tooltips. Las nuevas pruebas dan coordenadas exactas |
| Media | Establecer baseline/allowlist de API | Clasificar los 257 tipos y documentar soportado, extensión e interno antes de cualquier deprecación | Inventario §2 | Bajo ahora; evita una futura ruptura binaria accidental |
| Baja | Actualizar documentación después de decidir contratos | Documentar foco, consumo, `clearAll`, lifecycle y límite de API | §7 | Bajo, pero no debe adelantarse a la decisión de producción |

No se recomienda una clase base universal, un MVVM forzado para controles
simples, integrar Notification en `Control`, añadir hijos JSON a Panel ni
eliminar `dispose()`.

## 9. Propuesta de división de tareas siguientes

La separación de recepción, elegibilidad, captura y consumo de H1, la
autoridad por `InputManager` de H2 y el cierre coordinado de H3 se completaron
sin resolver el orden intrapa, que sigue siendo una decisión independiente.

1. **Coordinador de dropdown por host.** Sustituir el registro estático global
   conservando prioridad y transferencia dentro del mismo host. Depende del
   contrato de input y debe preservar `dispose()`.
2. **Contexto acumulado de Panel.** Corregir únicamente la propagación global
   a descendientes; no cambiar coordenadas locales, medidas relativas ni JSON.
3. **Baseline de API soportada.** Convertir el inventario agrupado en allowlist
   verificable y política de deprecación antes de cualquier movimiento de
   paquetes o visibilidad.
4. **Actualización documental.** Solo después de que las decisiones anteriores
   estén cerradas.

Cada tarea puede usar estas pruebas como punto de partida, pero las pruebas
`current...` que describen defectos deberán dejar paso a aserciones del
contrato deseado, sin mantener ambos comportamientos como válidos.

## 10. Archivos de esta fase

- Nuevo: `docs/structural-characterization-baseline.md`.
- Nuevo:
  `src/test/java/com/cpz/processing/controls/characterization/StructuralInputFocusCharacterizationTest.java`.
- Nuevo:
  `src/test/java/com/cpz/processing/controls/characterization/OverlayAndDropDownRegistryCharacterizationTest.java`.
- Nuevo:
  `src/test/java/com/cpz/processing/controls/characterization/NestedPanelCharacterizationTest.java`.

En la fase de caracterización original no se modificó ningún archivo bajo
`src/main`. La corrección posterior de H1 modifica exclusivamente las cuatro
capas de input enumeradas en ese hallazgo y adapta su clase de pruebas. Siguen
sin cambios `pom.xml`, CI, recursos, ejemplos, versión y dependencias.
