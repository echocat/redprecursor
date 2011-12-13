/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is echocat redprecursor.
 *
 * The Initial Developer of the Original Code is Gregor Noczinski.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.redprecursor.meta;

import org.echocat.redprecursor.annotations.EvaluatedBy;
import org.echocat.redprecursor.utils.ClassPathResourcesIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;
import static org.echocat.redprecursor.utils.IoUtils.closeQuietly;
import static org.echocat.redprecursor.utils.NodeListIterable.*;

public class AnnotationMetaFactoryFactory {

    public static final String SERVICES_LOCATION = "META-INF/meta/org.echocat.redprecursor.annotationsMeta.xml";

    private static final String ANNOTATION_META_XSD = "annotationMeta.xsd";
    private static final AnnotationMetaFactory DEFAULT_INSTANCE = new AnnotationMetaFactoryFactory(AnnotationMetaFactoryFactory.class.getClassLoader()).createFromClassPath();

    public static AnnotationMetaFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private final ClassLoader _classLoader;
    private final DocumentBuilderFactory _documentBuilderFactory;

    public AnnotationMetaFactoryFactory(@Nonnull ClassLoader classLoader) {
        _classLoader = requireNonNull("classLoader", classLoader);
        final Schema schema = loadSchema();
        _documentBuilderFactory = createDocumentBuilderFactory(schema);
    }

    @Nonnull
    public AnnotationMetaFactory createFromClassPath() {
        final Map<Class<? extends Annotation>, AnnotationMeta<?, ?>> typeToMeta = new HashMap<Class<? extends Annotation>, AnnotationMeta<?, ?>>();
        final Iterator<URL> i = new ClassPathResourcesIterator(_classLoader, SERVICES_LOCATION);
        while (i.hasNext()) {
            final URL resourceUrl = i.next();
            final Document resourceDocument = urlToDocument(resourceUrl);
            final Element annotationsMetaElement = resourceDocument.getDocumentElement();
            for (Element annotationElement : childElementsOf(annotationsMetaElement, "annotation")) {
                final AnnotationMeta<?, ?> annotationMeta = parseAnnotation(annotationElement);
                typeToMeta.put(annotationMeta.getType(), annotationMeta);
            }
        }
        return new AnnotationMetaFactory(unmodifiableMap(typeToMeta));
    }

    @Nonnull
    public Schema getSchema() {
        return _documentBuilderFactory.getSchema();
    }

    @Nonnull
    private AnnotationMeta<?, ?> parseAnnotation(@Nonnull Element annotationElement) {
        final Class<? extends Annotation> annotationType = parseAttributeAsType(annotationElement, "type", Annotation.class);
        final AnnotationEvaluator<?, ?> annotationEvaluator = parseAnnotationChild(annotationType, annotationElement, "evaluator", EvaluatedBy.class, AnnotationEvaluator.class);
        // noinspection unchecked
        return new AnnotationMeta(annotationType, annotationEvaluator);
    }

    @Nonnull
    private <T> T parseAnnotationChild(@Nonnull Class<? extends Annotation> annotationType, @Nonnull Element annotationElement, @Nonnull String elementName, @Nonnull Class<? extends Annotation> atAnnotationDefinedWith, @Nonnull Class<T> expectedType) {
        requireNonNull("annotationType", annotationType);
        requireNonNull("annotationElement", annotationElement);
        requireNonNull("elementName", elementName);
        requireNonNull("atAnnotationDefinedWith", atAnnotationDefinedWith);
        requireNonNull("expectedType", expectedType);
        final Element element = childElementOf(annotationElement, elementName);
        final Class<T> type;
        if (element != null) {
            type = parseAttributeAsType(element, "type", expectedType);
        } else {
            type = findDefaultType(annotationType, atAnnotationDefinedWith, expectedType, elementName);
        }
        return newInstanceOf(type);
    }

    @Nonnull
    private <T> T newInstanceOf(@Nonnull Class<T> type) {
        requireNonNull("type", type);
        final Constructor<T> constructor;
        try {
            constructor = type.getConstructor();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not find a default constructor for " + type.getName() + ".", e);
        }
        if (!constructor.isAccessible()) {
            try {
                constructor.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException("Could not access the default constructor for " + type.getName() + ".", e);
            }
        }
        try {
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create an instance of " + type.getName() + ".", e);
        }
    }

    @Nonnull
    private <T> Class<T> findDefaultType(@Nonnull Class<?> from, @Nonnull Class<? extends Annotation> atAnnotationDefinedWith, @Nonnull Class<T> ofType, @Nonnull String tagName) {
        requireNonNull("from", from);
        requireNonNull("atAnnotationDefinedWith", atAnnotationDefinedWith);
        requireNonNull("ofType", ofType);
        requireNonNull("tagName", tagName);
        final Annotation annotation = from.getAnnotation(atAnnotationDefinedWith);
        if (annotation == null) {
            throw new IllegalArgumentException("Neither is @" + from.getName() + " annotated with @" +  atAnnotationDefinedWith.getName() + " nor is in '" + SERVICES_LOCATION + "' the tag <" + tagName + " .../> defined.");
        }
        final Method valueMethod;
        try {
            valueMethod = annotation.annotationType().getMethod("value");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("@" + atAnnotationDefinedWith.getName() + " annotated at @" + from.getName() + " has no value() method.", e);
        }
        final Object value;
        try {
            value = valueMethod.invoke(annotation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not get from @" + atAnnotationDefinedWith.getName() + ".value() annotated at @" + from.getName() + " the value.", e);
        }
        if (!(value instanceof Class)) {
            throw new IllegalArgumentException("The value of @" + atAnnotationDefinedWith.getName() + ".value() annotated at @" + from.getName() + " is not of type " + Class.class.getName() + ".");
        }
        final Class<?> type = (Class<?>) value;
        if (!ofType.isAssignableFrom(type)) {
            throw new IllegalStateException(type + " is not of expected type " + ofType.getName() + ".");
        }
        // noinspection unchecked
        return (Class<T>) type;
    }

    @Nonnull
    private <T> Class<T> parseAttributeAsType(@Nonnull Element element, @Nonnull String attributeName, @Nonnull Class<T> expectedType) {
        requireNonNull("expectedType", expectedType);
        final String attributeValue = requireNonNull("element", element).getAttribute(requireNonNull("attributeName", attributeName));
        final Class<?> type;
        try {
            type = _classLoader.loadClass(attributeValue);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("The value of <" + element.getTagName() + " " + attributeName + "=\"..\" ../> is no valid class.", e);
        }
        if (!expectedType.isAssignableFrom(type)) {
            throw new IllegalArgumentException("The value of <" + element.getTagName() + " " + attributeName + "=\"..\" ../> is no type of '" + expectedType.getName() + "'.");
        }
        // noinspection unchecked
        return (Class<T>) type;
    }

    @Nonnull
    private Document urlToDocument(@Nonnull URL url) {
        try {
            final InputStream is = requireNonNull("url", url).openStream();
            try {
                final DocumentBuilder documentBuilder = _documentBuilderFactory.newDocumentBuilder();
                documentBuilder.setErrorHandler(new ErrorHandler() {
                    @Override public void warning(SAXParseException exception) throws SAXException {}
                    @Override public void error(SAXParseException exception) throws SAXException { throw exception; }
                    @Override public void fatalError(SAXParseException exception) throws SAXException { throw exception; }
                });
                return documentBuilder.parse(is);
            } finally {
                closeQuietly(is);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + url + ".", e);
        }
    }

    @Nonnull
    private DocumentBuilderFactory createDocumentBuilderFactory(@Nonnull Schema schema) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setSchema(schema);
        return factory;
    }

    @Nonnull
    private Schema loadSchema() {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        final InputStream is = getSchemaStream();
        try {
            return schemaFactory.newSchema(new StreamSource(is));
        } catch (SAXException e) {
            throw new RuntimeException("Could not load " + ANNOTATION_META_XSD + ".", e);
        } finally {
            closeQuietly(is);
        }
    }

    @Nonnull
    private InputStream getSchemaStream() {
        final InputStream is = AnnotationMetaFactoryFactory.class.getResourceAsStream(ANNOTATION_META_XSD);
        if (is == null){
            throw new IllegalStateException("There is no 'annotationMeta.xsd' in classpath.");
        }
        return is;
    }
}
