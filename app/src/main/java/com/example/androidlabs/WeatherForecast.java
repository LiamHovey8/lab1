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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        ForecastQuery forecastQuery=new ForecastQuery();
        forecastQuery.execute();

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        protected String windUV;
        protected String currentTemperature;
        protected String min;
        protected String max;
        protected Bitmap bm;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar progressBar=findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView max=findViewById(R.id.max_temperature_value);
            max.setText("max temperature: "+this.max);
            TextView min=findViewById(R.id.min_temperature_value);
            min.setText("min temperature: "+this.min);
            TextView current =findViewById(R.id.current_temperature_value);
            current.setText("current temperature: "+currentTemperature);
            ImageView imageView=findViewById(R.id.current_weather_image);
            imageView.setImageBitmap(bm);
            TextView uvLevel =findViewById(R.id.uv_level);
            uvLevel.setText("current UV: "+windUV);
            ProgressBar progressBar=findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL="http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String queryUVURL="http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature")) {
                                String currentTemperature= xpp.getAttributeValue(null,"value");
                                this.currentTemperature=currentTemperature;
                                publishProgress(25);
                                String minTemperature= xpp.getAttributeValue(null,"min");
                                this.min=minTemperature;
                                publishProgress(50);
                                String maxTemperature= xpp.getAttributeValue(null,"max");
                                this.max=maxTemperature;
                                publishProgress(75);
                            }else if(tagName.equals("weather")){
                                String icon= xpp.getAttributeValue(null,"icon");
                                String urlString="http://openweathermap.org/img/w/" + icon + ".png";
                                if(!fileExistance(icon + ".png")) {
                                    Bitmap image = null;
                                    URL ImageURL = new URL(urlString);
                                    HttpURLConnection Connection = (HttpURLConnection) ImageURL.openConnection();
                                    Connection.connect();
                                    int responseCode = Connection.getResponseCode();
                                    if (responseCode == 200) {
                                        image = BitmapFactory.decodeStream(Connection.getInputStream());
                                    }
                                    FileOutputStream outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    bm=image;
                                    Log.i("image",icon+" image downloaded");
                                    outputStream.flush();
                                    outputStream.close();
                                }else{
                                    FileInputStream fis = null;
                                    try {    fis = openFileInput(icon+".png");   }
                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                                    Bitmap bm = BitmapFactory.decodeStream(fis);
                                    this.bm=bm;
                                    Log.i("image",icon+" image found locally");
                                }

                                publishProgress(100);
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
            try {       // Connect to the server:
                URL UVUrl = new URL(queryUVURL);
                HttpURLConnection urlConnection = (HttpURLConnection) UVUrl.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 5);
                StringBuilder sb = new StringBuilder();
                String line =null;
                while ((line = reader.readLine()) != null){sb.append(line + "\n");}
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                double value = jObject.getDouble("value");
                windUV=Double.toString(value);

            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch (JSONException jso)       { ret = "something went wrong with JSON";}
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }

    }
}
