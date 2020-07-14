package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidlabs.data.Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    ImageView currentWeather;
    TextView currTemp;
    TextView minimumTemp;
    TextView maximumTemp;
    TextView uvDisplay;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
//        context = this;
        currentWeather = findViewById(R.id.currentWeather);
        currTemp = findViewById(R.id.currentTemp);
        minimumTemp = findViewById(R.id.minTemp);
        maximumTemp = findViewById(R.id.maxTemp);
        uvDisplay = findViewById(R.id.uvRating);
        progress = findViewById(R.id.progress);
        ForecastQuery req = new ForecastQuery();
        req.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric", "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        public String uv;
        public String minTemp;
        public String maxTemp;
        public String currentTemp;
        public Bitmap image = null;

        public boolean fileExistence(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected String doInBackground(String... args) {

            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                URL uvUrl = new URL(args[1]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                HttpURLConnection uvConnection = (HttpURLConnection) uvUrl.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                InputStream uvResponse = uvConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                String icon = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equals("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25, 50, 75);

                            minTemp = xpp.getAttributeValue(null, "min");
                            publishProgress(25, 50, 75);

                            maxTemp = xpp.getAttributeValue(null, "max");
                            publishProgress(25, 50, 75);

                        } else if (xpp.getName().equals("weather")) {
                            icon = xpp.getAttributeValue(null, "icon");
                            publishProgress(25, 50, 75);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                URL urlForIcon = new URL("http://openweathermap.org/img/w/" + icon + ".png");
                HttpURLConnection iconConnection = (HttpURLConnection) urlForIcon.openConnection();
                iconConnection.connect();
                int responseCode = iconConnection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(iconConnection.getInputStream());
                    publishProgress(25, 50, 75);

                    FileOutputStream outputStream = openFileOutput(image + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                }

                String imageFilename = image + ".png";

                FileInputStream fis = null;
                try {
                    fis = openFileInput(image + ".png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(fis);

                Log.i("MainActivity", "looking for: " + image + ".png");
                String imageFound = null;
                if(fileExistence(imageFilename)){
                  imageFound = "Image found";}
                else imageFound = "Must download image";

                Log.i("WeatherForecast", imageFound);

                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                float uvRating = (float) uvReport.getDouble("value");
                uv = Float.toString(uvRating);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (XmlPullParserException ex) {
                ex.printStackTrace();
            } catch (
                    Exception e) {
            }
            return "Done";
        }

        public void onProgressUpdate(Integer... value) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        public void onPostExecute(String result) {
            progress.setVisibility(View.INVISIBLE);
            currentWeather.setImageBitmap(image);
            currTemp.setText("The current temperature is: " + currentTemp + ".");
            minimumTemp.setText("Minimum temperature: " + minTemp + ".");
            maximumTemp.setText("Maximum temperature: " + maxTemp + ".");
            uvDisplay.setText("Uv index: " + uv + ".");

        }


    }

}