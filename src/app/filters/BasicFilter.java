package app.filters;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A basic filter that matches every tweet that contains the given word
 */
public class BasicFilter implements Filter {
    private final String word;
    private final Pattern pattern;

    public BasicFilter(String word) {
        this.word = word;
        pattern = Pattern.compile("(?i).*" + Pattern.quote(word) + ".*");
    }

    @Override
    public boolean matches(Status status) {
        String text = status.getText();
        return pattern.matcher(text).matches();
    }

    @Override
    public List<String> getTerms() {
        List<String> ans = new ArrayList<>(1);
        ans.add(word);
        return ans;
    }

    @Override
    public String toString() {
        return word;
    }

    public String getWord() {
        return word;
    }
}