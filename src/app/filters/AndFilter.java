package app.filters;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class AndFilter implements Filter {
    private final Filter leftChild;
    private final Filter rightChild;

    public AndFilter(Filter leftChild, Filter rightChild)   {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    
    @Override
    public boolean matches(Status status) {
        return leftChild.matches(status) && rightChild.matches(status);
    }

    @Override
    public List<String> getTerms() {
        List<String> terms = new ArrayList<>();
        terms.addAll(leftChild.getTerms());
        terms.addAll(rightChild.getTerms());
        return terms;
    }

    @Override
    public String toString() {
        return String.format("(%s and %s)", leftChild.toString(), rightChild.toString());
    }
}