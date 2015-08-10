package freemarker.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import freemarker.template.Template;

@SuppressWarnings("boxing")
public class CustomAttributeInUnboundTemplatesTest {

    @Test
    public void inFtlHeaderTest() throws IOException {
        Template t = new Template(null, "<#ftl attributes={'a': 1?int}>", new Configuration(Configuration.VERSION_2_3_23));
        t.setCustomAttribute("b", 2);
        assertEquals(1, t.getCustomAttribute("a"));
        assertEquals(2, t.getCustomAttribute("b"));
        assertEquals(ImmutableMap.of("a", 1), t.getUnboundTemplate().getCustomAttributes());
    }
    
    @Test
    public void inTemplateConfigurerTest() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        
        TemplateConfigurer tc = new TemplateConfigurer();
        tc.setCustomAttribute("a", 1);
        tc.setParentConfiguration(cfg);
        
        Template t = new Template(null, null, new StringReader(""), cfg, tc, null);
        t.setCustomAttribute("b", 2);
        assertNull(t.getCustomAttribute("a"));
        tc.configure(t);
        assertEquals(1, t.getCustomAttribute("a"));
        assertEquals(2, t.getCustomAttribute("b"));
        assertNull(t.getUnboundTemplate().getCustomAttributes());
    }

}