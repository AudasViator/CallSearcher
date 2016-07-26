package pro.audasviator.callsearcher.receivers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.audasviator.callsearcher.R;
import pro.audasviator.callsearcher.parser.ParsedAnswer;
import pro.audasviator.callsearcher.parser.TheBestYandexParser;

import static android.view.View.GONE;


public class CallReceiver extends AbstractCallReceiver {

    @BindView(R.id.overlay_title_text_view)
    TextView mTitleTextView;

    @BindView(R.id.overlay_content_text_view)
    TextView mContentTextView;

    @BindView(R.id.overlay_layout)
    ViewGroup mOverlayLayout;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        onCall(ctx, number, start);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        hideLayout();
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        hideLayout();
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        onCall(ctx, number, start);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        hideLayout();
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        // Можно вызвать hideLayout(), но пускай человек посмотрим, кто ему звонил и сам закроет layout
    }

    private void onCall(Context ctx, String number, Date start) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        View view = li.inflate(R.layout.caller_overlay, null);
        ButterKnife.bind(this, view);
        wm.addView(view, params);

        // Что не напишу, всегда получается ASyncTask
        // Скучно да и не модно :(
        new SearcherTask().execute(number);
    }

    @OnClick(R.id.overlay_layout)
    void hideLayout() {
        if (mOverlayLayout != null) {
            ObjectAnimator animator = ObjectAnimator
                    .ofFloat(mOverlayLayout, "alpha", 1.0f, 0.0f)
                    .setDuration(500);
            animator.addListener(new setGone());
            animator.start();
        }
    }

    private class setGone implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mOverlayLayout.setVisibility(GONE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    private class SearcherTask extends android.os.AsyncTask<String, Void, ParsedAnswer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ParsedAnswer doInBackground(String... strings) {
            if (strings[0] != null) {
                return new TheBestYandexParser().getAnswer(strings[0]);
            } else {
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ParsedAnswer parsedAnswer) {
            if (parsedAnswer != null && mTitleTextView != null && mContentTextView != null) {
                if (parsedAnswer.isCorrect()) {
                    mTitleTextView.setText(parsedAnswer.getTitle());
                    mContentTextView.setText(parsedAnswer.getContent());
                } else {
                    mTitleTextView.setText(R.string.overlay_error);
                    mContentTextView.setText(parsedAnswer.getError());
                }
            }
        }
    }
}
