package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.jsr69.component.TemplateProviderGenerator;
import com.axellience.vuegwt.jsr69.component.VueComponentOptionsGenerator;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.directive.VueDirectiveOptionsGenerator;
import com.axellience.vuegwt.jsr69.directive.annotations.Directive;
import com.axellience.vuegwt.jsr69.style.StyleProviderGenerator;
import com.axellience.vuegwt.jsr69.style.annotations.Style;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Set;

@SupportedAnnotationTypes({
    "com.axellience.vuegwt.jsr69.component.annotations.Component",
    "com.axellience.vuegwt.jsr69.directive.annotations.Directive",
    "com.axellience.vuegwt.jsr69.style.annotations.Style"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class VueGWTProcessor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        this.processStyleAnnotations(roundEnv);
        this.processDirectiveAnnotations(roundEnv);
        this.processComponentAnnotations(roundEnv);

        // claim the annotation
        return true;
    }

    private void processComponentAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements =
            roundEnv.getElementsAnnotatedWith(Component.class);

        TemplateProviderGenerator templateGenerator = new TemplateProviderGenerator(processingEnv);
        VueComponentOptionsGenerator vueComponentOptionsGenerator =
            new VueComponentOptionsGenerator(processingEnv);

        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            vueComponentOptionsGenerator.generate(element);
            Component componentAnnotation = element.getAnnotation(Component.class);
            if (componentAnnotation.hasTemplate())
            {
                templateGenerator.generate(element);
            }
        }
    }

    private void processDirectiveAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements =
            roundEnv.getElementsAnnotatedWith(Directive.class);

        VueDirectiveOptionsGenerator vueDirectiveOptionsGenerator =
            new VueDirectiveOptionsGenerator(processingEnv);
        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            vueDirectiveOptionsGenerator.generate(element);
        }
    }

    private void processStyleAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Style.class);

        StyleProviderGenerator styleProviderGenerator = new StyleProviderGenerator(processingEnv);
        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            styleProviderGenerator.generate(element);
        }
    }
}