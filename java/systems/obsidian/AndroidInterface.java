package systems.obsidian;

import android.net.Uri;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.webkit.ValueCallback;
import android.webkit.JavascriptInterface;

import systems.obsidian.HaskellActivity;
import com.gonimo.baby.R;

// Access Android features from Haskell via jsaddle:
public class AndroidInterface {

  public AndroidInterface(HaskellActivity a) {
    this.a = a;
  }

  @JavascriptInterface
  public void share(String url) {
    Intent i = new Intent(Intent.ACTION_SEND, Uri.parse(url));
    i.putExtra(Intent.EXTRA_TEXT, url);
    i.setType("text/plain");
    a.startActivity(Intent.createChooser(i, a.getString(R.string.share_using)));
  }

  @JavascriptInterface
  public void killApp() {
      a.finishAndRemoveTask();
  }

  @JavascriptInterface
  // Tell system that Gonimo should be killed.
  public void avoidKill() {
      a.makeForeground();
  }

  @JavascriptInterface
  // Tell system that Gonimo can be killed now.
  public void allowKill() {
      a.deleteForeground();
  }

  private HaskellActivity a;

}

