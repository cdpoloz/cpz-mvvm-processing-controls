# 0.9.11 API/ABI Compatibility

## Scope

This audit compares the packaged `0.9.11` candidate with the immutable
`io.github.cdpoloz:cpz-mvvm-processing-controls:0.9.10` artifact published in
Maven Central.

The local Maven resolver marker for the baseline records `central` as its
source, and the downloaded JAR matches its published SHA-1 sidecar:

```text
49fe953a69e48c321b40003a1547205d5f6e236a
```

The comparison uses japicmp `0.26.1` at `protected` access level. Synthetic
types and members are included. Annotations are evaluated. No API includes,
excludes, missing-class exceptions, or compatibility overrides are applied.
Separate classpaths use Processing Core `4.5.5` for both versions,
`cpz-utils 0.2.3` for the baseline, and `cpz-utils 0.2.4` for the candidate.

## Reproduction

The tool is downloaded to the local Maven cache only; it is not added to the
project POM:

```bash
mvn --batch-mode --no-transfer-progress dependency:get \
  -Dartifact=com.github.siom79.japicmp:japicmp:0.26.1:jar:jar-with-dependencies \
  -Dtransitive=false

mvn --batch-mode --no-transfer-progress clean package

mvn --batch-mode --no-transfer-progress dependency:build-classpath \
  -Dmdep.includeScope=compile \
  -Dmdep.outputFile=/tmp/cpz-controls-0.9.11-classpath.txt

TOOL="$HOME/.m2/repository/com/github/siom79/japicmp/japicmp/0.26.1/japicmp-0.26.1-jar-with-dependencies.jar"
OLD="$HOME/.m2/repository/io/github/cdpoloz/cpz-mvvm-processing-controls/0.9.10/cpz-mvvm-processing-controls-0.9.10.jar"
NEW="target/cpz-mvvm-processing-controls-0.9.11.jar"
NEW_CP="$(cat /tmp/cpz-controls-0.9.11-classpath.txt)"
OLD_CP="${NEW_CP/\/cpz-utils\/0.2.4\/cpz-utils-0.2.4.jar/\/cpz-utils\/0.2.3\/cpz-utils-0.2.3.jar}"

java -jar "$TOOL" \
  --old "$OLD" \
  --new "$NEW" \
  -a protected \
  --only-modified \
  --include-synthetic \
  --old-classpath "$OLD_CP" \
  --new-classpath "$NEW_CP" \
  --error-on-binary-incompatibility \
  --error-on-source-incompatibility \
  --report-only-filename \
  --xml-file /tmp/japicmp-0.9.10-to-0.9.11.xml \
  --html-file /tmp/japicmp-0.9.10-to-0.9.11.html \
  --markdown \
  > /tmp/japicmp-0.9.10-to-0.9.11.md
```

## Result

japicmp exits with status `0`:

- binary incompatibilities: 0;
- source incompatibilities: 0;
- removed public/protected types: 0;
- removed public/protected methods, constructors, or fields: 0;
- modified compatible types: 18;
- added compatible types: 2;
- added methods: 32;
- added constructors: 1;
- added interface relationships: 11;
- added fields: 0.

The two added types are already classified by the canonical allowlist:

- `FocusManagerAware`: category B extension/integration SPI;
- `DropDownCoordinator`: category D compatibility exposure.

The remaining additions are the focus attachment, panel parent-context,
manager accessor, dropdown coordination, and validation members introduced by
the structural work. `PublicApiSurfaceTest` independently verifies all 280
current types and 2,943 declared public/protected members, their A/B/C/D
classification, and the absence of category-D dependencies from A/B
signatures.

## Limits

japicmp evaluates Java class-file API, binary compatibility, source
compatibility, annotations, and serialization metadata. It does not verify
runtime behavior, resource compatibility, documentation, parameter names, or
reflection order. Those concerns remain covered by the functional tests, JAR
inspection, Javadocs, and the current-surface allowlist.

The generated Markdown, XML, and HTML reports remain temporary because they
contain machine paths and generation timestamps. This document records the
stable command, inputs, policy, and result instead.
