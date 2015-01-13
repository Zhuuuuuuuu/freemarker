package freemarker.cache;

import java.io.IOException;
import java.util.Locale;

import freemarker.template.Configuration;

/**
 * Used as the parameter of {@link TemplateLookupStrategy#lookup(TemplateLookupContext)}.
 * You can't create instances of this, only receive them from FreeMarker.
 * 
 * @since 2.3.22
 */
public abstract class TemplateLookupContext {
    
    private final String templateName;
    private final Locale templateLocale;
    private final Object customLookupCondition;

    /**
     * Finds the template source based on its <em>normalized</em> name; handles {@code *} steps (so called acquisition),
     * otherwise it just calls {@link TemplateLoader#findTemplateSource(String)}.
     * 
     * @param name
     *            Must be a normalized name, like {@code "foo/bar/baaz.ftl"}. A name is not normalized when, among
     *            others, it starts with {@code /}, or contains {@code .} or {@code ..} paths steps, or it uses
     *            backslash ({@code \}) instead of {@code /}. A normalized name might contains "*" steps.
     * 
     * @return The result of the lookup. Not {@code null}; check {@link TemplateLookupResult#isPositive()} to see if the
     *         lookup has found anything.
     */
    public abstract TemplateLookupResult lookupWithAcquisitionStrategy(String name) throws IOException;

    /** Default visibility to prevent extending the class from outside this package. */
    TemplateLookupContext(String templateName, Locale templateLocale, Object customLookupCondition) {
        this.templateName = templateName;
        this.templateLocale = templateLocale;
        this.customLookupCondition = customLookupCondition;
    }

    /**
     * The normalized name (path) of the template (relatively to the {@link TemplateLoader}). Not {@code null}. 
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * {@code null} if localized lookup is disabled (see {@link Configuration#getLocalizedLookup()}), otherwise the
     * locale requested.
     */
    public Locale getTemplateLocale() {
        return templateLocale;
    }

    /**
     * Return the value of the {@code customLookupCondition} parameter of
     * {@link Configuration#getTemplate(String, Locale, Object, String, boolean, boolean)}. The interpretation of this
     * value is up to the custom {@link TemplateLookupStrategy}. Usually, it's used similarly to as the default lookup
     * strategy uses {@link #getTemplateLocale()}, that is, to look for a template variation that satisfies the
     * condition, and then maybe fall back to more generic template if that's missing.
     */
    public Object getCustomLookupCondition() {
        return customLookupCondition;
    }

    /**
     * Creates a not-found lookup result that then can be used as the return value of
     * {@link TemplateLookupStrategy#lookup(TemplateLookupContext)}. (In the current implementation it just always
     * returns the same static singleton, but that might need to change in the future.)
     */
    public TemplateLookupResult createNegativeLookupResult() {
        return TemplateLookupResult.createNegativeResult();
    }
    
}