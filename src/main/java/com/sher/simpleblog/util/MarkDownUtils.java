package com.sher.simpleblog.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @Title MarkDownUtils
 * @Package com.sher.simpleblog.util
 * @Description Convert markdown to html by commonmark.java
 * @Author sher
 * @Date 2020/07/26 11:19 AM
 */
public class MarkDownUtils {

    public static String markToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public static String markToHtmlExt(String markdown) {
        Set<Extension> headingAnchor = Collections.singleton(HeadingAnchorExtension.create());
        List<Extension> table = Arrays.asList(TablesExtension.create());

        Parser parser = Parser.builder().extensions(table).build();
        Node document = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder().extensions(headingAnchor).extensions(table)
                .attributeProviderFactory(x -> {
                    return customAttributeProvider;
                })
                .build();
        return renderer.render(document);
    }

    private static final AttributeProvider customAttributeProvider = (node, s, map) -> {
        if (node instanceof Link) {
            map.put("target", "_blank");
        }
        if (node instanceof TableBlock) {
            map.put("class", "ui celled table");
        }
    };
}
