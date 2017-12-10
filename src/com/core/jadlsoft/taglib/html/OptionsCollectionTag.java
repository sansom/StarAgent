/*
 * $Id: OptionsCollectionTag.java,v 1.1 2007/04/05 03:14:20 administrator Exp $ 
 *
 * Copyright 2002-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 本标签修改自Struts的OptionsCollection标签，去掉对Select标签的依赖
 * 请在不需要与Form对应时使用，否则使用原标签
 * @author 李帮贵
 * @date 2006-09-14	
 * 
 * */
package com.core.jadlsoft.taglib.html;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.IteratorAdapter;

/**
 * Tag for creating multiple &lt;select&gt; options from a collection. The
 * collection may be part of the enclosing form, or may be independent of
 * the form. Each element of the collection must expose a 'label' and a
 * 'value', the property names of which are configurable by attributes of
 * this tag.
 * <p>
 * The collection may be an array of objects, a Collection, an Enumeration,
 * an Iterator, or a Map.
 * <p>
 * <b>NOTE</b> - This tag requires a Java2 (JDK 1.2 or later) platform.
 *
 * @version $Rev: 56513 $ $Date: 2007/04/05 03:14:20 $
 * @since Struts 1.1
 */
/**
 * @author libanggui
 *
 */
public class OptionsCollectionTag extends TagSupport {

    // ----------------------------------------------------- Instance Variables


    // ------------------------------------------------------------- Properties

    /**
     * Should the label values be filtered for HTML sensitive characters?
     */
    protected boolean filter = true;

    public boolean getFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    /**
     * The name of the bean property containing the label.
     */
    protected String label = "label";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * The name of the bean containing the values collection.
     */
    protected String name = "bean";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The name of the property to use to build the values collection.
     */
    protected String property = null;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The style associated with this tag.
     */
    private String style = null;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * The named style class associated with this tag.
     */
    private String styleClass = null;

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * The name of the bean property containing the value.
     */
    protected String value = "value";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    protected String filterProperty = null;

    public String getFilterProperty() {
        return filterProperty;
    }

    public void setFilterProperty(String filterProperty) {
        this.filterProperty = filterProperty;
    }
    
    protected String filterValue = null;

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Acquire the collection containing our options
        Object collection = TagUtils.getInstance().lookup(pageContext, name, property, null);

        if (collection == null) {
            JspException e =
                new JspException("列表值列表不存在！");
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        // Acquire an iterator over the options collection
        Iterator iter = getIterator(collection);

        StringBuffer sb = new StringBuffer();

        // Render the options
        while (iter.hasNext()) {

            Object bean = iter.next();

            if(filterProperty!=null && filterProperty.length()>0 && filterValue!=null) {
            	Object tempvalue = getBeanProperty(bean, filterProperty);
            	if(tempvalue==null) tempvalue="";
            	if(tempvalue.toString().equals(filterValue)) {
                    // Get the label for this option
                    addOptionValue(sb, bean);
            	}
            }else{
            	addOptionValue(sb, bean);
            }
        }

        TagUtils.getInstance().write(pageContext, sb.toString());

        return SKIP_BODY;
    }
    
	private void addOptionValue(StringBuffer sb, Object bean) throws JspException {
		Object beanLabel = null;
		Object beanValue = null;
		beanLabel = getBeanProperty(bean, label);
		beanValue = getBeanProperty(bean, value);

		String stringLabel = beanLabel.toString();
		String stringValue = beanValue.toString();

		// Render this option
		addOption(sb, stringLabel, stringValue, false); //selectTag.isMatched(stringValue));
	}

	private Object getBeanProperty(Object bean, String property) throws JspException {
		try {
		    Object value = PropertyUtils.getProperty(bean, property);
		    if (value == null) {
		    	value = "";
		    }
		    return value;
		} catch (IllegalAccessException e) {
		    JspException jspe =
		        new JspException("列表对象显示属性无权访问！");
		    TagUtils.getInstance().saveException(pageContext, jspe);
		    throw jspe;
		} catch (InvocationTargetException e) {
		    JspException jspe =
		        new JspException("列表对象显示属性值不匹配！");
		    TagUtils.getInstance().saveException(pageContext, jspe);
		    throw jspe;
		} catch (NoSuchMethodException e) {
		    JspException jspe =
		        new JspException("列表对象显示属性无法访问！");
		    TagUtils.getInstance().saveException(pageContext, jspe);
		    throw jspe;
		}
	}

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        filter = true;
        label = "label";
        name = "bean";
        property = null;
        style = null;
        styleClass = null;
        value = "value";
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Add an option element to the specified StringBuffer based on the
     * specified parameters.
     *<p>
     * Note that this tag specifically does not support the
     * <code>styleId</code> tag attribute, which causes the HTML
     * <code>id</code> attribute to be emitted.  This is because the HTML
     * specification states that all "id" attributes in a document have to be
     * unique.  This tag will likely generate more than one <code>option</code>
     * element element, but it cannot use the same <code>id</code> value.  It's
     * conceivable some sort of mechanism to supply an array of <code>id</code>
     * values could be devised, but that doesn't seem to be worth the trouble.
     *
     * @param sb StringBuffer accumulating our results
     * @param value Value to be returned to the server for this option
     * @param label Value to be shown to the user for this option
     * @param matched Should this value be marked as selected?
     */
    protected void addOption(StringBuffer sb, String label, String value, boolean matched) {

        sb.append("<option value=\"");
        if (filter) {
            sb.append(TagUtils.getInstance().filter(value));
        } else {
            sb.append(value);
        }
        sb.append("\"");
        if (matched) {
            sb.append(" selected=\"selected\"");
        }
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        if (styleClass != null) {
            sb.append(" class=\"");
            sb.append(styleClass);
            sb.append("\"");
        }
        
        sb.append(">");
        
        if (filter) {
            sb.append(TagUtils.getInstance().filter(label));
        } else {
            sb.append(label);
        }
        
        sb.append("</option>\r\n");

    }

    /**
     * Return an iterator for the options collection.
     *
     * @param collection Collection to be iterated over
     *
     * @exception JspException if an error occurs
     */
    protected Iterator getIterator(Object collection) throws JspException {

        if (collection.getClass().isArray()) {
            collection = Arrays.asList((Object[]) collection);
        }

        if (collection instanceof Collection) {
            return (((Collection) collection).iterator());

        } else if (collection instanceof Iterator) {
            return ((Iterator) collection);

        } else if (collection instanceof Map) {
            return (((Map) collection).entrySet().iterator());

        } else if (collection instanceof Enumeration) {
            return new IteratorAdapter((Enumeration) collection);

        } else {
            throw new JspException("对象不是合法的列表！");
        }
    }

}
