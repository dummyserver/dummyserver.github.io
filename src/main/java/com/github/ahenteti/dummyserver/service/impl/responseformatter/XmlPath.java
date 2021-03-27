package com.github.ahenteti.dummyserver.service.impl.responseformatter;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.Optional;

public class XmlPath {

    public static Optional<String> read(String content, String path) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(content.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();
            return Optional.of(xPath.compile(path).evaluate(document));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
