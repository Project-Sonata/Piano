package com.odeyalo.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtil;
import com.puppycrawl.tools.checkstyle.utils.CheckUtil;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import com.puppycrawl.tools.checkstyle.utils.FilterUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public final class RequiredFieldAnnotationCheck extends AbstractCheck {

    private String[] packagesToExclude = new String[0];
    private final Logger logger = LoggerFactory.getLogger(RequiredFieldAnnotationCheck.class);

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
    public void visitToken(@NotNull final DetailAST ast) {
        if ( ast.getType() == TokenTypes.VARIABLE_DEF
                && ast.getParent().getType() == TokenTypes.OBJBLOCK ) {

            final String currentPackage = getCurrentPackage(ast);

            if ( ArrayUtils.contains(packagesToExclude, currentPackage) ) {
                logger.info("'{}' has been skipped because marked as excluded package", currentPackage);
                return;
            }

            final DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);

            if ( modifiers == null || !modifiers.hasChildren() ) {
                return;
            }

            DetailAST annotation = modifiers.getFirstChild();

            boolean hasAnnotation = false;

            while (annotation != null && annotation.getType() == TokenTypes.ANNOTATION) {

                DetailAST annotationName = annotation.findFirstToken(TokenTypes.IDENT);

                if ( StringUtils.equalsAny(annotationName.getText(), "NotNull", "Nullable") ) {
                    hasAnnotation = true;
                    break;
                }

                annotation = annotation.getNextSibling();
            }

            if ( !hasAnnotation ) {
                log(ast.getLineNo(), "Missing @NotNull or @Nullable annotation for field");
            }
        }
    }

    @Nullable
    private static String getCurrentPackage(@NotNull final DetailAST variableDef) {
        if ( variableDef.getParent().getParent() == null ||
                variableDef.getParent().getParent().getType() != TokenTypes.CLASS_DEF ) {
            return null;
        }

        final DetailAST classDef = variableDef.getParent().getParent();

        if ( classDef.getParent() == null || classDef.getParent().getType() != TokenTypes.COMPILATION_UNIT ) {
            return null;
        }

        final DetailAST compilationUnit = classDef.getParent();
        final DetailAST packageDef = compilationUnit.findFirstToken(TokenTypes.PACKAGE_DEF);

        final DetailAST packageNameAst = packageDef.getLastChild().getPreviousSibling();
        final FullIdent fullIdent = FullIdent.createFullIdent(packageNameAst);

        return fullIdent.getText();
    }

    public void setPackagesToExclude(final String[] packagesToExclude) {
        this.packagesToExclude = packagesToExclude;

        logger.info("{} set to be excluded", Arrays.toString(packagesToExclude));
    }
}
