package com.lateensoft.pathfinder.toolkit.test.serialize;

import android.test.AndroidTestCase;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;

/**
 * @author trevsiemens
 */
public abstract class AbstractXMLAdapterTestCase extends AndroidTestCase {

    protected void assertEquals(Element expected, Element actual) {
        NodeComparator comparator = new NodeComparator();
        comparator.compare(expected, actual);
    }
}
