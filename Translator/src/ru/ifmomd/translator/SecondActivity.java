package ru.ifmomd.translator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class SecondActivity extends Activity implements View.OnClickListener {
	final int NUMBER_OF_PICS = 10;
	int imgNumber = 0;
	Bitmap[] picsToDraw = new Bitmap[NUMBER_OF_PICS];
	ImageView image = null;
	TextView imgNumberText = null;
	String inRus = "";
	String wordToTranslate = "";
	final static String errorPictureLink = "http://files.myopera.com/ZERO555/albums/815971/thumbs/4ba64glossy-error-icon.jpg_thumb.jpg";

	public static Bitmap bitmapFromUrl(String url) throws IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return x;
	}

	private void createBitmaps() {
		String nextUrl = "";
		for (int imgNumber = 0; imgNumber < NUMBER_OF_PICS; imgNumber++) {
			try {
				nextUrl = FindPicturesURL.nextURL(wordToTranslate);
			} catch (Exception ex) {
				nextUrl = errorPictureLink;
			}
			try {
				if (nextUrl == null) {
					nextUrl = errorPictureLink;
				}
				picsToDraw[imgNumber] = bitmapFromUrl(nextUrl);
			} catch (IOException ex) {
				imgNumber--;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		wordToTranslate = intent.getStringExtra(Main.EXTRA_MESSAGE);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			inRus = ru.ifmomd.translator.Translator
					.getTranslation(wordToTranslate);
		} catch (IOException error) {
			inRus = "Произошла ошибка. Сожалеем об этом.";
		}

		createBitmaps();
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView russianWord = new TextView(this);
		russianWord.setText("Перевод слова " + wordToTranslate + " — " + inRus);
		russianWord.setTextSize(28);
		layout.addView(russianWord);

		image = new ImageView(this);
		image.setImageBitmap(picsToDraw[imgNumber]);
		image.setOnClickListener(new OnImageClickListener());
		layout.addView(image);

		imgNumberText = new TextView(this);
		imgNumberText.setText("Изображение №" + Integer.toString(imgNumber + 1)
				+ " (кликните, чтобы перейти к следующему).");
		imgNumberText.setTextSize(20);
		layout.addView(imgNumberText);

		Button back = new Button(this);
		back.setText("Назад");
		back.setTextSize(20);
		back.setOnClickListener(this);
		layout.addView(back);

		// ———————————————————————

		setContentView(layout);

	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(this, Main.class);
		startActivity(intent);

	}

	class OnImageClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			imgNumber = (imgNumber + 1) % NUMBER_OF_PICS;
			image.setImageBitmap(picsToDraw[imgNumber]);
			imgNumberText.setText("Изображение №"
					+ Integer.toString(imgNumber + 1)
					+ " (кликните, чтобы перейти к следующему).");
		}

	}

}
