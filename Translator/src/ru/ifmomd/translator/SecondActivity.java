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
	final int NUMBER_OF_PICS = 3;
	int imgNumber = 0;
	final String[] imageUrls = {"http://i3.squidoocdn.com/resize/squidoo_images/-1/lens1441580_1301342053DogMedicine.jpg",
			"http://www.dogbreedinfo.com/images/portuguesewaterdogstand.jpg",
			"http://news.ucdavis.edu/photos_images/news_mm/2012/August/dog_jaw_video.jpg",
	};
	Bitmap[] picsToDraw = new Bitmap[NUMBER_OF_PICS];
	ImageView image = null;
	TextView imgNumberText = null;
	
	public static Bitmap bitmapFromUrl(String url) throws IOException {
	    Bitmap x;

	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    connection.connect();
	    InputStream input = connection.getInputStream();

	    x = BitmapFactory.decodeStream(input);
	    return x;
	}
	
	private void createBitmaps(String[] imageUrls) {
		for (int imgNumber = 0; imgNumber < NUMBER_OF_PICS; imgNumber++) {
			try {
				picsToDraw[imgNumber] = bitmapFromUrl(imageUrls[imgNumber]);
			} catch(IOException ignore) {
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String wordToTranslate = intent.getStringExtra(Main.EXTRA_MESSAGE);
		String inRus = "";
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			inRus = ru.ifmomd.translator.Translator
					.getTranslation(wordToTranslate);
		} catch (IOException ignore) {
			inRus = "Ïðîèçîøëà îøèáêà. Ñîæàëååì îá ýòîì.";
		}
		
		createBitmaps(imageUrls);
		
		//—————————————————————Big layout definition
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView russianWord = new TextView(this);
		russianWord.setText("Ïåðåâîä ñëîâà " + wordToTranslate + " — " + inRus);
		russianWord.setTextSize(28);
		/*russianWord.setLayoutParams(new RelativeLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));*/
		layout.addView(russianWord);

		image = new ImageView(this);
		/*RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		imageParams.addRule(RelativeLayout.BELOW,
				russianWord.getId());
		image.setLayoutParams(imageParams);*/
		image.setImageBitmap(picsToDraw[imgNumber]);
		image.setOnClickListener(new OnImageClickListener());
		layout.addView(image);
		
		imgNumberText = new TextView(this);
		imgNumberText.setText("Èçîáðàæåíèå ¹" + Integer.toString(imgNumber + 1) + " (êëèêíèòå, ÷òîáû ïåðåéòè ê ñëåäóþùåìó).");
		imgNumberText.setTextSize(20);
		/*RelativeLayout.LayoutParams imgNumberTextParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imgNumberTextParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		imgNumberTextParams.addRule(RelativeLayout.BELOW,
				image.getId());
		imgNumberText.setLayoutParams(imgNumberTextParams);
		LinearLayout.LayoutParams imgNumberTextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		imgNumberTextParams.*/
		layout.addView(imgNumberText);

		Button back = new Button(this);
		back.setText("Íàçàä");
		back.setTextSize(20);
		back.setOnClickListener(this);
		/*RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		back.setLayoutParams(buttonParams);*/
		layout.addView(back);
		
		//———————————————————————

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
			imgNumberText.setText("Èçîáðàæåíèå ¹" + Integer.toString(imgNumber + 1) + " (êëèêíèòå, ÷òîáû ïåðåéòè ê ñëåäóþùåìó).");
		}
		
	}

}
