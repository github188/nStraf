package cn.grgbanking.feeltm.util;

import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.views.jsp.TagUtils;
import org.displaytag.localization.I18nResourceProvider;
import org.displaytag.localization.LocaleResolver;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ognl.OgnlValueStack;

@SuppressWarnings("unchecked")
public class TagResources implements LocaleResolver, I18nResourceProvider,
		LocaleProvider {
	public Locale resolveLocale(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Locale oc = (Locale) session.getAttribute("locale");
		return oc;
	}

	public String getResource(String resourceKey, String defaultValue, Tag tag,
			PageContext pageContext) {
		String key = resourceKey == null ? defaultValue : resourceKey;
		String message = null;
		OgnlValueStack stack = (OgnlValueStack) TagUtils.getStack(pageContext);
		Iterator iterator = stack.getRoot().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Object o = iterator.next();
			if (!(o instanceof TextProvider))
				continue;
			TextProvider tp = (TextProvider) o;
			message = tp.getText(key, defaultValue);
			break;
		} while (true);
		return message;
	}

	public static String getResource(String resourceKey, String defaultValue,
			PageContext pageContext) {
		String key = resourceKey == null ? defaultValue : resourceKey;
		String message = null;
		OgnlValueStack stack = (OgnlValueStack) TagUtils.getStack(pageContext);
		Iterator iterator = stack.getRoot().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Object o = iterator.next();
			if (!(o instanceof TextProvider))
				continue;
			TextProvider tp = (TextProvider) o;
			message = tp.getText(key, defaultValue);
			break;
		} while (true);
		return message;
	}

	public Locale getLocale() {
		return ActionContext.getContext().getLocale();
	}

	// ------------------------------------------------------------------------------
	public TagResources() {
	}

	// ------------------------------------------------------------------------------
	public static String message(PageContext pageContext, String key)
			throws JspException {
		return getResource(key, null, pageContext);

	}

	// ------------------------------------------------------------------------------
	public static String message(PageContext pageContext, String key,
			String bundle) throws JspException {
		return getResource(key, null, pageContext);
	}

	// ------------------------------------------------------------------------------
	public static String message(PageContext pageContext, String key,
			Object arg0) throws JspException {
		return getResource(key, null, pageContext);
	}

	// ------------------------------------------------------------------------------
	public static String message(PageContext pageContext, String key,
			Object args[]) throws JspException {
		return getResource(key, null, pageContext);
	}
	// ------------------------------------------------------------------------------
}