package pl.padamski;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.nio.file.Path;

public class AddToClass implements FindAnnotationLocation {

    @Override
    public int findLocation(int warningLine, Path p) {
        CompilationUnit cu = AddToMethod.getCompilationUnit(p);

        // visit and print the methods names
        ClassVisitor visitor = new ClassVisitor();
        visitor.visit(cu, warningLine);
        return visitor.getBeginLine();
    }

}


class ClassVisitor extends VoidVisitorAdapter<Object> {

    private int beginLine;

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        Integer line = (Integer) arg;
        if (n.getBeginLine() <= line && line <= n.getEndLine()) {
            beginLine = n.getBeginLine();
        }

    }

    public int getBeginLine() {
        return beginLine;

    }
}