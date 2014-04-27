package javafxapp.elements;


import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class IntervalTimeTextField extends TextField {

    private final Pattern timePattern;

    public IntervalTimeTextField() {
        this("00");
    }

    public IntervalTimeTextField(String time) {
        super(time);
        timePattern = Pattern.compile("\\d\\d|\\d");
        if (!validate(time)) {
            throw new IllegalArgumentException("Invalid time: " + time);
        }
    }

    @Override
    public void insertText(int index, String text) {
        StringBuilder builder = new StringBuilder(this.getText());
        builder.replace(index, index + text.length(), text);
        final String testText = builder.toString();
        if (validate(testText)) {
            this.setText(testText);
        }
        this.positionCaret(index + text.length());
    }

    @Override
    public void replaceSelection(String replacement) {
        final IndexRange selection = this.getSelection();
        if (selection.getLength()==0) {
            this.insertText(selection.getStart(), replacement);
        } else {
            this.replaceText(selection.getStart(), selection.getEnd(), replacement);
        }
    }

    @Override
    public void replaceText(IndexRange range, String text) {
        this.replaceText(range.getStart(), range.getEnd(), text);
    }

    @Override
    public void replaceText(int begin, int end, String text) {
        if (begin == end) {
            this.insertText(begin, text);
        } else {
                StringBuilder builder = new StringBuilder(this.getText());
                builder.replace(begin, end, text);
                String testText = builder.toString();
                if (validate(testText)) {
                    this.setText(testText);
                }
                this.positionCaret(end);

        }
    }

    private boolean validate(String time) {
        return timePattern.matcher(time).matches();
    }


}