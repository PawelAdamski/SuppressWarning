package pl.padamski;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.nio.file.Path;

public class AddToMethod implements FindAnnotationLocation {

    @Override
    public int findLocation(int warningLine, Path p) {
        CompilationUnit cu = null;
        try {
            cu = JavaParser.parse(p.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        // visit and print the methods names
        MethodVisitor visitor = new MethodVisitor();
        visitor.visit(cu, warningLine);
        return visitor.getBeginLine() - 1;
    }

}


class MethodVisitor extends VoidVisitorAdapter {

    private int beginLine;

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        Integer line = (Integer) arg;
        if (n.getBeginLine() <= line && line <= n.getEndLine()) {
            beginLine = n.getBeginLine();
        }

    }

    public int getBeginLine() {
        return beginLine;

    }
}