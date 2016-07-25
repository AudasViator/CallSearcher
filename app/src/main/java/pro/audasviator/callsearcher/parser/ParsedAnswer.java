package pro.audasviator.callsearcher.parser;


import android.support.annotation.NonNull;

public class ParsedAnswer {
    private String mTitle;
    private String mContent;
    private String mError;
    private boolean mCorrect;

    public ParsedAnswer(@NonNull String title, @NonNull String content, @NonNull String error, boolean correct) {
        mTitle = title;
        mContent = content;
        mError = error;
        mCorrect = correct;
    }

    public String getContent() {
        return mContent;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isCorrect() {
        return mCorrect;
    }

    public String getError() {
        return mError;
    }

    @Override
    public String toString() {
        if (isCorrect()) {
            return "ParsedAnswer{" +
                    "Title='" + mTitle + '\'' +
                    ", Content='" + mContent + '\'' + '}';
        } else {
            return "ParsedAnswer(Error! " + mError + ')';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParsedAnswer)) return false;

        ParsedAnswer that = (ParsedAnswer) o;

        // Студия не любит if`ы
        return mCorrect == that.mCorrect
                && (mTitle != null ? mTitle.equals(that.mTitle) : that.mTitle == null
                && (mContent != null ? mContent.equals(that.mContent) : that.mContent == null
                && (mError != null ? mError.equals(that.mError) : that.mError == null)));

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mContent != null ? mContent.hashCode() : 0);
        result = 31 * result + (mError != null ? mError.hashCode() : 0);
        result = 31 * result + (mCorrect ? 1 : 0);
        return result;
    }
}
