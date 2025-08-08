package com.odeyalo.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class RequiredFieldAnnotationCheck extends AbstractCheck {
    private Pattern[] packagesToInclude = new Pattern[0];

    private final Logger logger = LoggerFactory.getLogger(RequiredFieldAnnotationCheck.class);
    private static final String NO_PACKAGE = "";

    @Override
    public int[] getDefaultTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[]{
                TokenTypes.VARIABLE_DEF,
        };
    }

    @Override
    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    @Override
    public void visitToken(@NotNull final DetailAST variableDef) {
        if ( variableDef.getType() != TokenTypes.VARIABLE_DEF
                || variableDef.getParent().getType() != TokenTypes.OBJBLOCK ) {
            return;
        }

        final String currentPackage = getCurrentPackageFullName(variableDef);

        if ( Arrays.stream(packagesToInclude).noneMatch(pattern -> pattern.matcher(currentPackage).matches()) ) {
            logger.info("Package '{}' has been skipped because not matched any patterns", currentPackage);
            return;
        }

        logger.info("Package '{}' will be processed", currentPackage);

        final String variableType = variableDef
                .findFirstToken(TokenTypes.TYPE)
                .getFirstChild()
                .getText();

        if ( isPrimitive(variableType) ) {
            // No need to annotate primitives with NotNull/Nullable annotations
            return;
        }

        if ( !checkVariableAnnotatedWith(variableDef) ) {
            log(variableDef.getLineNo(), "Missing @NotNull or @Nullable annotation for field");
        }
    }

    public void setPackagesToInclude(@NotNull final String[] packagesToIncludeRegex) {
        this.packagesToInclude = Arrays.stream(packagesToIncludeRegex)
                .map(Pattern::compile)
                .toArray(Pattern[]::new);
    }

    private boolean checkVariableAnnotatedWith(@NotNull final DetailAST variableDef) {
        final DetailAST modifiers = variableDef.findFirstToken(TokenTypes.MODIFIERS);

        if ( modifiers == null || !modifiers.hasChildren() ) {
            return false;
        }

        final DetailAST modifier = modifiers.getFirstChild();

        return checkModifiersForAnnotation(modifier);
    }

    private boolean isPrimitive(@NotNull final String variableType) {
        return variableType.equals("boolean")
                || variableType.equals("char")
                || variableType.equals("byte")
                || variableType.equals("short")
                || variableType.equals("int")
                || variableType.equals("long")
                || variableType.equals("float")
                || variableType.equals("double");
    }

    private boolean checkModifiersForAnnotation(@NotNull final DetailAST modifiers) {

        DetailAST currModifier = modifiers;

        while (currModifier != null && currModifier.getType() == TokenTypes.ANNOTATION) {

            DetailAST annotationName = currModifier.findFirstToken(TokenTypes.IDENT);

            if ( StringUtils.equalsAny(annotationName.getText(), "NotNull", "Nullable") ) {
                return true;
            }

            currModifier = currModifier.getNextSibling();
        }

        return false;
    }

    @NotNull
    private String getCurrentPackageFullName(@NotNull final DetailAST variableDef) {
        if ( variableDef.getParent().getParent() == null ||
                variableDef.getParent().getParent().getType() != TokenTypes.CLASS_DEF ) {
            return NO_PACKAGE;
        }

        final DetailAST classDef = variableDef.getParent().getParent();

        if ( classDef.getParent() == null || classDef.getParent().getType() != TokenTypes.COMPILATION_UNIT ) {
            return NO_PACKAGE;
        }

        final DetailAST compilationUnit = classDef.getParent();
        final DetailAST packageDef = compilationUnit.findFirstToken(TokenTypes.PACKAGE_DEF);

        final DetailAST packageNameAst = packageDef.getLastChild().getPreviousSibling();
        final FullIdent fullIdent = FullIdent.createFullIdent(packageNameAst);

        return fullIdent.getText();
    }
}
