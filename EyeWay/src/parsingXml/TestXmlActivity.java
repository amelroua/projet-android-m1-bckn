package parsingXml;

import com.example.eyeway.R;
import com.example.eyeway.R.layout;
import com.example.eyeway.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class TestXmlActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
