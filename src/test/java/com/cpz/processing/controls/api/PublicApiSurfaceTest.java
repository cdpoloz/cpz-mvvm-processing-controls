package com.cpz.processing.controls.api;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.ParentContextAwareControl;
import com.cpz.processing.controls.controls.dropdown.util.DropDownCoordinator;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.util.ControlCode;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicApiSurfaceTest {
    private static final int MAX_REPORTED_DRIFT = 50;
    private static final String PROJECT_PREFIX = "com.cpz.processing.controls.";
    private static final String EXAMPLES_SEGMENT = ".examples.";
    private static final String MAIN_SEGMENT = ".main.";
    private static final Path ALLOWLIST = projectRoot().resolve("docs/public-api-signatures.txt");

    @Test
    void compiledSurfaceMatchesReviewedAllowlist() {
        ReviewedApi reviewed = ReviewedApi.load(ALLOWLIST);
        List<String> actual = PublicApiInventory.describe(distributedTypes());

        assertTrue(
                reviewed.signatures().equals(actual),
                () -> driftMessage(reviewed.signatures(), actual)
        );
        Set<String> actualTypeNames = PublicApiInventory.typeNames(actual);
        assertTrue(
                reviewed.typeCategories().keySet().equals(actualTypeNames),
                () -> "Every distributed public/protected type must have exactly one reviewed category."
                        + System.lineSeparator()
                        + driftMessage(reviewed.typeCategories().keySet().stream().sorted().toList(),
                        actualTypeNames.stream().sorted().toList())
        );
    }

    @Test
    void inventoryIsStableRegardlessOfDiscoveryOrder() {
        List<Class<?>> discovered = new ArrayList<>(distributedTypes());
        List<String> forward = PublicApiInventory.describe(discovered);

        Collections.reverse(discovered);
        List<String> reverse = PublicApiInventory.describe(discovered);

        assertEquals(forward, reverse);
        assertEquals(forward.stream().sorted().toList(), forward);
    }

    @Test
    void examplesAndLauncherAreExcludedFromDistributedSurface() {
        Set<String> rawClassNames = PublicApiInventory.classNames(productionClassesRoot());
        assertTrue(rawClassNames.stream().anyMatch(name -> name.contains(EXAMPLES_SEGMENT)));
        assertTrue(rawClassNames.stream().anyMatch(name -> name.contains(MAIN_SEGMENT)));

        Set<String> distributed = PublicApiInventory.typeNames(PublicApiInventory.describe(distributedTypes()));
        assertFalse(distributed.stream().anyMatch(name -> name.contains(EXAMPLES_SEGMENT)));
        assertFalse(distributed.stream().anyMatch(name -> name.contains(MAIN_SEGMENT)));
    }

    @Test
    void jarConfigurationExcludesExamplesAndLauncher() throws Exception {
        var document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(projectRoot().resolve("pom.xml").toFile());
        var xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xpath.evaluate(
                "//*[local-name()='plugin']"
                        + "[*[local-name()='artifactId' and text()='maven-jar-plugin']]"
                        + "//*[local-name()='exclude']/text()",
                document,
                XPathConstants.NODESET
        );
        Set<String> excludes = new TreeSet<>();
        for (int index = 0; index < nodes.getLength(); index++) {
            excludes.add(nodes.item(index).getNodeValue().trim());
        }

        assertTrue(excludes.contains("com/cpz/processing/controls/examples/**"));
        assertTrue(excludes.contains("com/cpz/processing/controls/main/**"));
    }

    @Test
    void supportedSignaturesDoNotExposeReviewCandidates() {
        ReviewedApi reviewed = ReviewedApi.load(ALLOWLIST);
        Map<String, Class<?>> types = distributedTypes().stream()
                .collect(LinkedHashMap::new, (map, type) -> map.put(type.getName(), type), Map::putAll);
        List<String> violations = new ArrayList<>();

        for (Class<?> type : types.values()) {
            Category typeCategory = reviewed.typeCategory(type.getName());
            if (typeCategory == Category.A || typeCategory == Category.B) {
                findCategoryDReferences(
                        "type " + type.getName(),
                        PublicApiInventory.typeDependencies(type),
                        reviewed,
                        violations
                );
            }
            for (Member member : PublicApiInventory.apiMembers(type)) {
                String signature = PublicApiInventory.memberSignature(member);
                Category memberCategory = reviewed.memberCategory(type.getName(), signature);
                if (memberCategory == Category.A || memberCategory == Category.B) {
                    findCategoryDReferences(
                            type.getName() + " :: " + signature,
                            PublicApiInventory.memberDependencies(member),
                            reviewed,
                            violations
                    );
                }
            }
        }

        assertEquals(List.of(), violations, () -> String.join(System.lineSeparator(), violations));
    }

    @Test
    void mandatoryInfrastructureAndReviewCandidatesAreExplicit() throws NoSuchMethodException {
        ReviewedApi reviewed = ReviewedApi.load(ALLOWLIST);

        assertEquals(Category.D, reviewed.typeCategory(DropDownCoordinator.class.getName()));
        assertEquals(
                Category.D,
                reviewed.memberCategory(
                        InputManager.class.getName(),
                        PublicApiInventory.memberSignature(InputManager.class.getMethod("getDropDownCoordinator"))
                )
        );
        assertEquals(Category.B, reviewed.typeCategory(ParentContextAwareControl.class.getName()));
        assertEquals(
                Category.B,
                reviewed.memberCategory(
                        Panel.class.getName(),
                        PublicApiInventory.memberSignature(Panel.class.getMethod("setParentOffset", float.class, float.class))
                )
        );
        assertEquals(
                Category.B,
                reviewed.memberCategory(
                        Panel.class.getName(),
                        PublicApiInventory.memberSignature(Panel.class.getMethod("clearParentOffset"))
                )
        );
        assertEquals(
                Category.B,
                reviewed.memberCategory(
                        ParentContextAwareControl.class.getName(),
                        PublicApiInventory.memberSignature(
                                ParentContextAwareControl.class.getMethod("onRemovedFromParent")
                        )
                )
        );
        assertTrue(ParentContextAwareControl.class.isAssignableFrom(Panel.class));
        assertEquals(Category.D, reviewed.typeCategory(ControlCode.class.getName()));
        assertEquals(
                Category.D,
                reviewed.memberCategory(
                        ControlCode.class.getName(),
                        PublicApiInventory.memberSignature(
                                ControlCode.class.getMethod("requireNonBlank", String.class)
                        )
                )
        );
    }

    @Test
    void driftDiagnosticNamesAddedAndRemovedSignatures() {
        String message = driftMessage(
                List.of("T|old.Type|public class old.Type"),
                List.of("T|new.Type|public class new.Type")
        );

        assertTrue(message.contains("+ T|new.Type|public class new.Type"));
        assertTrue(message.contains("- T|old.Type|public class old.Type"));
        assertTrue(message.contains("docs/public-api-signatures.txt"));
    }

    private static void findCategoryDReferences(
            String owner,
            Collection<Class<?>> dependencies,
            ReviewedApi reviewed,
            List<String> violations
    ) {
        dependencies.stream()
                .filter(dependency -> dependency.getName().startsWith(PROJECT_PREFIX))
                .filter(dependency -> reviewed.typeCategory(dependency.getName()) == Category.D)
                .map(Class::getName)
                .sorted()
                .forEach(dependency -> violations.add(
                        owner + " exposes category D type " + dependency
                ));
    }

    private static String driftMessage(List<String> expected, List<String> actual) {
        Set<String> added = new TreeSet<>(actual);
        added.removeAll(expected);
        Set<String> removed = new TreeSet<>(expected);
        removed.removeAll(actual);

        StringBuilder message = new StringBuilder(
                "Public API drift requires review in docs/public-api-signatures.txt."
        );
        if (!added.isEmpty()) {
            message.append(System.lineSeparator()).append("Unreviewed or changed signatures:");
            added.stream().limit(MAX_REPORTED_DRIFT)
                    .forEach(signature -> message.append(System.lineSeparator()).append("+ ").append(signature));
            appendOmittedCount(message, added.size());
        }
        if (!removed.isEmpty()) {
            message.append(System.lineSeparator()).append("Removed or changed reviewed signatures:");
            removed.stream().limit(MAX_REPORTED_DRIFT)
                    .forEach(signature -> message.append(System.lineSeparator()).append("- ").append(signature));
            appendOmittedCount(message, removed.size());
        }
        return message.toString();
    }

    private static void appendOmittedCount(StringBuilder message, int total) {
        if (total > MAX_REPORTED_DRIFT) {
            message.append(System.lineSeparator())
                    .append("... ")
                    .append(total - MAX_REPORTED_DRIFT)
                    .append(" more signatures omitted");
        }
    }

    private static List<Class<?>> distributedTypes() {
        return PublicApiInventory.loadDistributedTypes(productionClassesRoot());
    }

    private static Path productionClassesRoot() {
        try {
            return Path.of(Control.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("Cannot locate compiled production classes.", exception);
        }
    }

    private static Path projectRoot() {
        Path classes = productionClassesRoot();
        Path target = classes.getParent();
        if (target == null || target.getParent() == null) {
            throw new IllegalStateException("Cannot locate project root from " + classes);
        }
        return target.getParent();
    }

    private enum Category {
        A, B, C, D
    }

    private record ReviewedApi(
            List<String> signatures,
            Map<String, Category> typeCategories,
            Map<String, Category> signatureCategories
    ) {
        private static ReviewedApi load(Path path) {
            try {
                if (!Files.isRegularFile(path)) {
                    throw new IllegalStateException("Missing canonical public API allowlist: " + path);
                }
                List<String> signatures = new ArrayList<>();
                Map<String, Category> typeCategories = new LinkedHashMap<>();
                Map<String, Category> signatureCategories = new LinkedHashMap<>();
                for (String rawLine : Files.readAllLines(path)) {
                    String line = rawLine.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    String[] parts = line.split("\\|", 3);
                    if (parts.length != 3) {
                        throw new IllegalStateException("Invalid allowlist line: " + rawLine);
                    }
                    Category category;
                    try {
                        category = Category.valueOf(parts[0]);
                    } catch (IllegalArgumentException exception) {
                        throw new IllegalStateException("Invalid API category in line: " + rawLine, exception);
                    }
                    String signature = parts[1] + "|" + parts[2];
                    if (signatureCategories.put(signature, category) != null) {
                        throw new IllegalStateException("Duplicate allowlist signature: " + signature);
                    }
                    signatures.add(signature);
                    if (signature.startsWith("T|")) {
                        String typeName = signature.split("\\|", 3)[1];
                        if (typeCategories.put(typeName, category) != null) {
                            throw new IllegalStateException("Duplicate allowlist type: " + typeName);
                        }
                    }
                }
                List<String> sorted = signatures.stream().sorted().toList();
                if (!signatures.equals(sorted)) {
                    throw new IllegalStateException("Allowlist signatures must be sorted deterministically.");
                }
                return new ReviewedApi(List.copyOf(signatures), Map.copyOf(typeCategories), Map.copyOf(signatureCategories));
            } catch (IOException exception) {
                throw new IllegalStateException("Cannot read public API allowlist: " + path, exception);
            }
        }

        private Category typeCategory(String typeName) {
            Category category = this.typeCategories.get(typeName);
            if (category == null) {
                throw new IllegalStateException("Unclassified public API type: " + typeName);
            }
            return category;
        }

        private Category memberCategory(String declaringType, String memberSignature) {
            Category category = this.signatureCategories.get("M|" + declaringType + "|" + memberSignature);
            if (category == null) {
                throw new IllegalStateException(
                        "Unclassified public API member: " + declaringType + " :: " + memberSignature
                );
            }
            return category;
        }
    }

    private static final class PublicApiInventory {
        private PublicApiInventory() {
        }

        private static List<Class<?>> loadDistributedTypes(Path root) {
            List<Class<?>> result = new ArrayList<>();
            for (String name : classNames(root)) {
                if (name.contains(EXAMPLES_SEGMENT) || name.contains(MAIN_SEGMENT)) {
                    continue;
                }
                try {
                    Class<?> type = Class.forName(name, false, PublicApiSurfaceTest.class.getClassLoader());
                    if (isAccessibleType(type)) {
                        result.add(type);
                    }
                } catch (ClassNotFoundException | LinkageError exception) {
                    throw new IllegalStateException("Cannot inspect compiled production type " + name, exception);
                }
            }
            result.sort(Comparator.comparing(Class::getName));
            return List.copyOf(result);
        }

        private static Set<String> classNames(Path root) {
            try (Stream<Path> paths = Files.walk(root)) {
                Set<String> result = new TreeSet<>();
                paths.filter(path -> path.toString().endsWith(".class"))
                        .map(root::relativize)
                        .map(Path::toString)
                        .map(name -> name.replace(root.getFileSystem().getSeparator(), "."))
                        .map(name -> name.substring(0, name.length() - ".class".length()))
                        .filter(name -> name.startsWith(PROJECT_PREFIX))
                        .forEach(result::add);
                return result;
            } catch (IOException exception) {
                throw new IllegalStateException("Cannot inspect compiled production classes in " + root, exception);
            }
        }

        private static boolean isAccessibleType(Class<?> type) {
            if (type.isAnonymousClass() || type.isLocalClass() || type.isSynthetic()) {
                return false;
            }
            int modifiers = type.getModifiers();
            if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers)) {
                return false;
            }
            Class<?> enclosing = type.getEnclosingClass();
            return enclosing == null || isAccessibleType(enclosing);
        }

        private static List<String> describe(Collection<Class<?>> sourceTypes) {
            return sourceTypes.stream()
                    .sorted(Comparator.comparing(Class::getName))
                    .flatMap(type -> Stream.concat(
                            Stream.of("T|" + type.getName() + "|" + typeSignature(type)),
                            apiMembers(type).stream()
                                    .map(member -> "M|" + type.getName() + "|" + memberSignature(member))
                    ))
                    .sorted()
                    .toList();
        }

        private static Set<String> typeNames(Collection<String> signatures) {
            Set<String> names = new TreeSet<>();
            signatures.stream()
                    .filter(signature -> signature.startsWith("T|"))
                    .map(signature -> signature.split("\\|", 3)[1])
                    .forEach(names::add);
            return names;
        }

        private static List<Member> apiMembers(Class<?> type) {
            return Stream.<Member>concat(
                            Arrays.stream(type.getDeclaredConstructors()).map(Member.class::cast),
                            Stream.<Member>concat(
                                    Arrays.stream(type.getDeclaredFields()).map(Member.class::cast),
                                    Arrays.stream(type.getDeclaredMethods()).map(Member.class::cast)
                            )
                    )
                    .filter(PublicApiInventory::isApiMember)
                    .sorted(Comparator.comparing(PublicApiInventory::memberSignature))
                    .toList();
        }

        private static boolean isApiMember(Member member) {
            int modifiers = member.getModifiers();
            return Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers);
        }

        private static String typeSignature(Class<?> type) {
            StringBuilder result = new StringBuilder(typeModifiers(type));
            appendSpace(result);
            if (type.isAnnotation()) {
                result.append("@interface");
            } else if (type.isEnum()) {
                result.append("enum");
            } else if (type.isRecord()) {
                result.append("record");
            } else if (type.isInterface()) {
                result.append("interface");
            } else {
                result.append("class");
            }
            result.append(' ').append(type.getName());
            appendTypeParameters(result, type.getTypeParameters());
            Type superclass = type.getGenericSuperclass();
            if (superclass != null && superclass != Object.class && !type.isEnum() && !type.isRecord()) {
                result.append(" extends ").append(typeName(superclass));
            }
            Type[] interfaces = type.getGenericInterfaces();
            if (interfaces.length > 0) {
                result.append(type.isInterface() ? " extends " : " implements ");
                result.append(String.join(",", Arrays.stream(interfaces).map(PublicApiInventory::typeName).toList()));
            }
            if (type.isSealed()) {
                result.append(" permits ");
                result.append(String.join(",", Arrays.stream(type.getPermittedSubclasses())
                        .map(Class::getName)
                        .sorted()
                        .toList()));
            }
            return result.toString();
        }

        private static String memberSignature(Member member) {
            if (member instanceof Constructor<?> constructor) {
                StringBuilder result = new StringBuilder(executableModifiers(
                        constructor.getModifiers(),
                        constructor.isSynthetic(),
                        false,
                        false
                ));
                appendSpace(result);
                appendTypeParameters(result, constructor.getTypeParameters());
                if (constructor.getTypeParameters().length > 0) {
                    result.append(' ');
                }
                result.append(constructor.getDeclaringClass().getName());
                appendParameters(result, constructor.getGenericParameterTypes(), constructor.isVarArgs());
                appendExceptions(result, constructor.getGenericExceptionTypes());
                return result.toString();
            }
            if (member instanceof Field field) {
                StringBuilder result = new StringBuilder(fieldModifiers(field));
                appendSpace(result);
                result.append(typeName(field.getGenericType())).append(' ').append(field.getName());
                if (field.isEnumConstant()) {
                    result.append(" enum-constant");
                }
                if (field.isSynthetic()) {
                    result.append(" synthetic");
                }
                return result.toString();
            }
            Method method = (Method) member;
            StringBuilder result = new StringBuilder(executableModifiers(
                    method.getModifiers(),
                    method.isSynthetic(),
                    method.isBridge(),
                    method.isDefault()
            ));
            appendSpace(result);
            appendTypeParameters(result, method.getTypeParameters());
            if (method.getTypeParameters().length > 0) {
                result.append(' ');
            }
            result.append(typeName(method.getGenericReturnType())).append(' ').append(method.getName());
            appendParameters(result, method.getGenericParameterTypes(), method.isVarArgs());
            appendExceptions(result, method.getGenericExceptionTypes());
            if (method.getDefaultValue() != null) {
                result.append(" default=").append(annotationValue(method.getDefaultValue()));
            }
            return result.toString();
        }

        private static Set<Class<?>> typeDependencies(Class<?> type) {
            Set<Class<?>> result = new LinkedHashSet<>();
            collectType(type.getGenericSuperclass(), result);
            Arrays.stream(type.getGenericInterfaces()).forEach(value -> collectType(value, result));
            Arrays.stream(type.getTypeParameters()).forEach(value -> collectType(value, result));
            if (type.getEnclosingClass() != null) {
                result.add(type.getEnclosingClass());
            }
            return result;
        }

        private static Set<Class<?>> memberDependencies(Member member) {
            Set<Class<?>> result = new LinkedHashSet<>();
            if (member instanceof Constructor<?> constructor) {
                Arrays.stream(constructor.getGenericParameterTypes()).forEach(value -> collectType(value, result));
                Arrays.stream(constructor.getGenericExceptionTypes()).forEach(value -> collectType(value, result));
                Arrays.stream(constructor.getTypeParameters()).forEach(value -> collectType(value, result));
            } else if (member instanceof Field field) {
                collectType(field.getGenericType(), result);
            } else {
                Method method = (Method) member;
                collectType(method.getGenericReturnType(), result);
                Arrays.stream(method.getGenericParameterTypes()).forEach(value -> collectType(value, result));
                Arrays.stream(method.getGenericExceptionTypes()).forEach(value -> collectType(value, result));
                Arrays.stream(method.getTypeParameters()).forEach(value -> collectType(value, result));
            }
            return result;
        }

        private static void collectType(Type type, Set<Class<?>> target) {
            if (type == null) {
                return;
            }
            if (type instanceof Class<?> value) {
                if (value.isArray()) {
                    collectType(value.getComponentType(), target);
                } else {
                    target.add(value);
                }
            } else if (type instanceof ParameterizedType value) {
                collectType(value.getRawType(), target);
                collectType(value.getOwnerType(), target);
                Arrays.stream(value.getActualTypeArguments()).forEach(item -> collectType(item, target));
            } else if (type instanceof TypeVariable<?> value) {
                Arrays.stream(value.getBounds()).forEach(item -> collectType(item, target));
            } else if (type instanceof WildcardType value) {
                Arrays.stream(value.getLowerBounds()).forEach(item -> collectType(item, target));
                Arrays.stream(value.getUpperBounds()).forEach(item -> collectType(item, target));
            } else if (type instanceof GenericArrayType value) {
                collectType(value.getGenericComponentType(), target);
            }
        }

        private static String typeModifiers(Class<?> type) {
            int value = type.getModifiers();
            List<String> names = visibilityAndCoreModifiers(value);
            if (type.isSealed()) {
                names.add("sealed");
            }
            return String.join(" ", names);
        }

        private static String fieldModifiers(Field field) {
            int value = field.getModifiers();
            List<String> names = visibilityAndCoreModifiers(value);
            if (Modifier.isVolatile(value)) names.add("volatile");
            if (Modifier.isTransient(value)) names.add("transient");
            return String.join(" ", names);
        }

        private static String executableModifiers(
                int value,
                boolean synthetic,
                boolean bridge,
                boolean defaultMethod
        ) {
            List<String> names = visibilityAndCoreModifiers(value);
            if (Modifier.isSynchronized(value)) names.add("synchronized");
            if (Modifier.isNative(value)) names.add("native");
            if (Modifier.isStrict(value)) names.add("strictfp");
            if (defaultMethod) names.add("default");
            if (bridge) names.add("bridge");
            if (synthetic) names.add("synthetic");
            return String.join(" ", names);
        }

        private static List<String> visibilityAndCoreModifiers(int value) {
            List<String> names = new ArrayList<>();
            if (Modifier.isPublic(value)) names.add("public");
            if (Modifier.isProtected(value)) names.add("protected");
            if (Modifier.isAbstract(value)) names.add("abstract");
            if (Modifier.isStatic(value)) names.add("static");
            if (Modifier.isFinal(value)) names.add("final");
            return names;
        }

        private static void appendTypeParameters(StringBuilder target, TypeVariable<?>[] parameters) {
            if (parameters.length == 0) {
                return;
            }
            target.append('<');
            for (int index = 0; index < parameters.length; index++) {
                if (index > 0) {
                    target.append(',');
                }
                TypeVariable<?> parameter = parameters[index];
                target.append(parameter.getName());
                Type[] bounds = parameter.getBounds();
                if (!(bounds.length == 1 && bounds[0] == Object.class)) {
                    target.append(" extends ");
                    target.append(String.join("&", Arrays.stream(bounds)
                            .map(PublicApiInventory::typeName)
                            .toList()));
                }
            }
            target.append('>');
        }

        private static void appendParameters(StringBuilder target, Type[] parameters, boolean varArgs) {
            target.append('(');
            for (int index = 0; index < parameters.length; index++) {
                if (index > 0) {
                    target.append(',');
                }
                String name = typeName(parameters[index]);
                if (varArgs && index == parameters.length - 1 && name.endsWith("[]")) {
                    name = name.substring(0, name.length() - 2) + "...";
                }
                target.append(name);
            }
            target.append(')');
        }

        private static void appendExceptions(StringBuilder target, Type[] exceptions) {
            if (exceptions.length > 0) {
                target.append(" throws ");
                target.append(String.join(",", Arrays.stream(exceptions)
                        .map(PublicApiInventory::typeName)
                        .toList()));
            }
        }

        private static void appendSpace(StringBuilder target) {
            if (!target.isEmpty()) {
                target.append(' ');
            }
        }

        private static String typeName(Type type) {
            if (type instanceof Class<?> value) {
                if (value.isArray()) {
                    return typeName(value.getComponentType()) + "[]";
                }
                return value.getName();
            }
            if (type instanceof ParameterizedType value) {
                String owner = value.getOwnerType() == null ? "" : typeName(value.getOwnerType()) + ".";
                String raw = value.getRawType() instanceof Class<?> rawClass
                        ? rawClass.getSimpleName()
                        : typeName(value.getRawType());
                if (value.getOwnerType() == null) {
                    raw = typeName(value.getRawType());
                }
                return owner + raw + "<"
                        + String.join(",", Arrays.stream(value.getActualTypeArguments())
                        .map(PublicApiInventory::typeName)
                        .toList()) + ">";
            }
            if (type instanceof TypeVariable<?> value) {
                return value.getName();
            }
            if (type instanceof WildcardType value) {
                if (value.getLowerBounds().length > 0) {
                    return "? super " + typeName(value.getLowerBounds()[0]);
                }
                if (value.getUpperBounds().length == 0 || value.getUpperBounds()[0] == Object.class) {
                    return "?";
                }
                return "? extends " + typeName(value.getUpperBounds()[0]);
            }
            if (type instanceof GenericArrayType value) {
                return typeName(value.getGenericComponentType()) + "[]";
            }
            return type.getTypeName();
        }

        private static String annotationValue(Object value) {
            if (!value.getClass().isArray()) {
                return String.valueOf(value);
            }
            int length = java.lang.reflect.Array.getLength(value);
            List<String> items = new ArrayList<>(length);
            for (int index = 0; index < length; index++) {
                items.add(annotationValue(java.lang.reflect.Array.get(value, index)));
            }
            return "[" + String.join(",", items) + "]";
        }
    }
}
