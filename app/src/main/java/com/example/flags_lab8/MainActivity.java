package com.example.flags_lab8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String correctImageName;
    private TextView result;
    private AssetManager assets;
    private String[] images;
    private int level = 1, wrongCount = 0;
    private Button b1, b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        result = findViewById(R.id.result);

        //get reference on assets folder & create String[] images
        assets = this.getAssets();

        //make an array of image strings
        try {
            images = assets.list("png");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //generate random values
        generateRandom();

        //defining the next button
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if level>10, show alert dialog & reset button
                if (level == 11) {
                    int correctCount = (int) (((10 - wrongCount) / 10.0) * 100);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("All done!");
                    alertDialog.setMessage(wrongCount + " Wrong, " + correctCount + "% Correct");
                    alertDialog.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //reset level
                            level = 1;
                            wrongCount = 0;
                            generateRandom();
                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                } else generateRandom();
            }
        });
    }

    private void generateRandom() {

        //put random image
        ImageView img = findViewById(R.id.imageview);
        int correctImageIndex = (int) (Math.random() * images.length);
        String imagePath = "png/" + images[correctImageIndex];
        Drawable image = null;

        try {
            InputStream inputstream = assets.open(imagePath);
            image = Drawable.createFromStream(inputstream, images[correctImageIndex]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        img.setImageDrawable(image);

        //get the correctImageName
        String name = images[correctImageIndex];
        name = name.substring(name.indexOf('-') + 1, name.indexOf('.'));
        name = name.replace('_', ' ');
        correctImageName = name;

        //ArrayList of wrong images, using random indexes from the images string array
        ArrayList<String> wrongImages = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int randomIndex = (int)(Math.random() * images.length);
            name = images[randomIndex];
            name = name.substring(name.indexOf('-') + 1, name.indexOf('.'));
            name = name.replace('_', ' ');

            //if the randomImage is already chosen or it is the correctImage, repeat loop
            if (wrongImages.contains(name) || name.equals(correctImageName)) i--;
            else wrongImages.add(name);
        }

        //get correctRandomButton
        int correctRandomButton = (int) (Math.random() * 3);

        //set the correctImage in a random button & the wrongImages normally
        switch (correctRandomButton) {
            case 0:
                b1.setText(correctImageName);
                b2.setText(wrongImages.get(0));
                b3.setText(wrongImages.get(1));
                break;
            case 1:
                b2.setText(correctImageName);
                b1.setText(wrongImages.get(0));
                b3.setText(wrongImages.get(1));
                break;
            case 2:
                b3.setText(correctImageName);
                b1.setText(wrongImages.get(0));
                b2.setText(wrongImages.get(1));
                break;
        }

        result.setText("");
        TextView question = findViewById(R.id.question);
        question.setText("Question " + level + " of 10");

        //set buttons clickable
        b1.setClickable(true);
        b2.setClickable(true);
        b3.setClickable(true);
    }

    public void checkResult(View v) {
        //prevent user from clicking another button
        b1.setClickable(false);
        b2.setClickable(false);
        b3.setClickable(false);

        //update level & check result
        level++;
        if (((Button) v).getText().toString().equals(correctImageName)) {
            result.setText("Correct!\nCountry: " + correctImageName);
        } else {
            wrongCount++;
            result.setText("Incorrect!\nCountry: " + correctImageName);
        }
    }

}