/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    SimpleDictionary simpleDictionary;
    TextView wordFragment,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        wordFragment= (TextView) findViewById(R.id.ghostText);
        status= (TextView) findViewById(R.id.gameStatus);


        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary=new FastDictionary(inputStream);

        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }


        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
          //  text.setText(simpleDictionary.getGoodWordStartingWith(text.getText().toString()));

        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView label = (TextView) findViewById(R.id.gameStatus);
                // Do computer turn stuff then make it the user's turn again
                label.setText("Computer turn");
                String str = wordFragment.getText().toString();
                if (str.length() >= 4 && dictionary.isWord(str))
                    label.setText("Computer won the game!!!!");
                else {
                    String longerword = dictionary.getAnyWordStartingWith(str);
                    System.out.println("Long word::" + longerword);
                    if (longerword == null)
                        label.setText("you can't bluff the computer");
                    else {
                        System.out.println("in comp");
                        char ch = longerword.charAt(str.length());
                        wordFragment.append(Character.toString(ch));
                        userTurn = true;
                        System.out.println("ch::"+ch);
                        label.setText(USER_TURN);
                    }

                }



            }

        }, 1500);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        int unicode=event.getUnicodeChar();
        if((unicode>=65 && unicode<=90) || (unicode<=122 && unicode>=97 )){
            String letter=new String(String.valueOf(Character.toChars(unicode)));
            wordFragment.append(letter);
            System.out.println("word::"+letter);
            String str=wordFragment.getText().toString();
            computerTurn();
            if (dictionary.isWord(str)) {
                status.setText("Correct word");
            }

        }
        else
            return super.onKeyUp(keyCode, event);

        return super.onKeyUp(keyCode, event);
    }

    public void challengeComputer(View view) {
        String str=wordFragment.getText().toString();
        if(str.length()>=4 && dictionary.isWord(str))
            status.setText("You won the game");
        else {
            String output = dictionary.getAnyWordStartingWith(str);
            if (output != null) {
                wordFragment.setText(output);
                status.setText("Computer won the game!!!");
            }
             else
                status.setText("You won the game!!!");
        }
    }
}
