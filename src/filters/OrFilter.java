package filters;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class OrFilter implements Filter {
    private final Filter leftChild;
    private final Filter rightChild;

    public OrFilter(Filter leftChild, Filter rightChild)   {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public boolean matches(Status status) {
        return leftChild.matches(status) || rightChild.matches(status);
    }

    @Override
    public List<String> terms() {
        List<String> terms = new ArrayList<>();
        terms.addAll(leftChild.terms());
        terms.addAll(rightChild.terms());
        return terms;
    }

    @Override
    public String toString() {
        return String.format("(%s or %s)", leftChild.toString(), rightChild.toString());
    }
}
