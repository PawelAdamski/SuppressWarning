package pl.padamski;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddToMethod implements FindAnnotationLocation {

    static final int MAX_ENTRIES = 100;
    static Map<Path, CompilationUnit> cache = new LinkedHashMap<Path, CompilationUnit>(MAX_ENTRIES + 1, .75F, true) {
        private static final long serialVersionUID = 1L;

        public boolean removeEldestEntry(Map.Entry<Path, CompilationUnit> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    @Override
    public int findLocation(int warningLine, Path p) {
        CompilationUnit cu = getCompilationUnit(p);

        // visit and print the methods names
        MethodVisitor visitor = new MethodVisitor();
        visitor.visit(cu, warningLine);
        return visitor.getBeginLine();
    }

    protected static CompilationUnit getCompilationUnit(Path p) {
        if (cache.containsKey(p)) {
            return cache.get(p);
        }
        CompilationUnit cu = null;
        try {
            cu = JavaParser.parse(p.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        cache.put(p, cu);
        return cu;
    }

}


class MethodVisitor extends VoidVisitorAdapter<Object> {

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