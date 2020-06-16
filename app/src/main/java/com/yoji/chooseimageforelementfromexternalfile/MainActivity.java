package com.yoji.chooseimageforelementfromexternalfile;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView calcMainTxtView;
    private TextView calcSmallTxtView;
    private Toolbar toolbar;
    private ImageView backgroundImageView;

    private StringBuilder calcTxtStringBuilder;
    private String calcTxt;
    private String action;
    private boolean sign = false;
    private boolean secondNumFlag = false;
    private SharedPreferences sharedPreferences;
    private final String IMAGE_KEY = "image_key";
    private int imagesCreatedCounter;
    private final int IMAGE_URI = 10;
    private  final int CREATE_IMAGE_URI = 11;

    private View.OnClickListener signBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcTxt = calcMainTxtView.getText().toString();
            if (!calcTxt.equals("") && action == null) {
                sign = !sign;
                calcTxtStringBuilder.setLength(0);
                if (sign) {
                    calcTxtStringBuilder.append("-").append(calcTxt);
                } else {
                    calcTxt = calcTxt.replace("-", "");
                    calcTxtStringBuilder.append(calcTxt);
                }
                calcMainTxtView.setText(calcTxtStringBuilder);
            }
        }
    };

    private View.OnClickListener percentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcTxt = calcMainTxtView.getText().toString();
            if (!calcTxt.equals("") && action == null) {
                float calcNum = Float.parseFloat(calcTxt);
                float percentNum = calcNum / 100;
                String percentTxt = String.valueOf(percentNum);
                calcTxtStringBuilder.setLength(0);
                calcTxtStringBuilder.append(percentTxt);
                calcMainTxtView.setText(calcTxtStringBuilder);
            }
        }
    };

    private View.OnClickListener dotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcTxt = calcMainTxtView.getText().toString();
            if (!calcTxt.equals("") && !calcTxt.matches(".*\\..*")) {
                calcTxtStringBuilder.append(getString(R.string.dot));
                calcMainTxtView.setText(calcTxtStringBuilder);
            }
        }
    };

    private View.OnClickListener clearBtnOnClickListener = v -> clear();

    private View.OnClickListener numBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcTxt = calcMainTxtView.getText().toString();
            int id = v.getId();
            if (calcTxt.equals("0")) {
                calcTxtStringBuilder.deleteCharAt(0);
            }
            if (action != null) {
                calcTxtStringBuilder.setLength(0);
                calcMainTxtView.setText("");
                secondNumFlag = true;
                sign = false;
            }
            switch (id) {
                case R.id.zeroBtnId:
                    calcTxtStringBuilder.append(getString(R.string.zero));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.oneBtnId:
                    calcTxtStringBuilder.append(getString(R.string.one));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.twoBtnId:
                    calcTxtStringBuilder.append(getString(R.string.two));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.threeBtnId:
                    calcTxtStringBuilder.append(getString(R.string.three));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.fourBtnId:
                    calcTxtStringBuilder.append(getString(R.string.four));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.fiveBtnId:
                    calcTxtStringBuilder.append(getString(R.string.five));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.sixBtnId:
                    calcTxtStringBuilder.append(getString(R.string.six));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.sevenBtnId:
                    calcTxtStringBuilder.append(getString(R.string.seven));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.eightBtnId:
                    calcTxtStringBuilder.append(getString(R.string.eight));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
                case R.id.nineBtnId:
                    calcTxtStringBuilder.append(getString(R.string.nine));
                    calcMainTxtView.setText(calcTxtStringBuilder);
                    break;
            }
            action = null;
        }
    };

    private View.OnClickListener actionBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            calcTxt = calcMainTxtView.getText().toString();
            if (!calcTxt.equals("")) {
                if (secondNumFlag) {
                    calcTxt = String.valueOf(equal(calcTxt));
                    calcTxtStringBuilder.setLength(0);
                } else {
                    calcTxt = calcMainTxtView.getText().toString();
                }
                calcTxtStringBuilder.setLength(0);
                switch (id) {
                    case R.id.divBtnId:
                        action = getString(R.string.div);
                        break;
                    case R.id.mulBtnId:
                        action = getString(R.string.mul);
                        break;
                    case R.id.subBtnId:
                        action = getString(R.string.sub);
                        break;
                    case R.id.sumBtnId:
                        action = getString(R.string.sum);
                        break;
                }
                calcTxtStringBuilder.append(calcTxt).append(" ").append(action);
                calcSmallTxtView.setText(calcTxtStringBuilder);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.select_background_picture) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_URI);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_URI && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(IMAGE_KEY, Objects.requireNonNull(uri).toString());
                editor.apply();

                setBackgroundImage(uri);
            }
        }

        if (requestCode == CREATE_IMAGE_URI && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                createExternalImages(uri);
            }
        }
    }

    private View.OnClickListener equalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcTxt = calcMainTxtView.getText().toString();
            if (secondNumFlag && !calcTxt.equals("")) {
                float result = equal(calcTxt);
                clear();
                calcMainTxtView.setText(String.valueOf(result));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        String FIRST_START_FLAG = "first_start_flag";
        if (sharedPreferences.getBoolean(FIRST_START_FLAG, true)) {
            createSomeExternalImages();  //Создаёт 2 файла 1.jpg и 2.jpg из drawable ресурсов
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(FIRST_START_FLAG, false);
            editor.apply();
        }
        setBackgroundImageFromSharedPrefs();
        setSupportActionBar(toolbar);
    }

    private void init() {
        //Toolbar
        toolbar = findViewById(R.id.toolbarId);
        //ImageView
        backgroundImageView = findViewById(R.id.backgroundImageViewId);
        //TextViews
        calcMainTxtView = findViewById(R.id.calcMainTxtView);
        calcSmallTxtView = findViewById(R.id.calcSmallTxtViewId);
        //Num buttons
        Button zeroBtn = findViewById(R.id.zeroBtnId);
        Button oneBtn = findViewById(R.id.oneBtnId);
        Button twoBtn = findViewById(R.id.twoBtnId);
        Button threeBtn = findViewById(R.id.threeBtnId);
        Button fourBtn = findViewById(R.id.fourBtnId);
        Button fiveBtn = findViewById(R.id.fiveBtnId);
        Button sixBtn = findViewById(R.id.sixBtnId);
        Button sevenBtn = findViewById(R.id.sevenBtnId);
        Button eightBtn = findViewById(R.id.eightBtnId);
        Button nineBtn = findViewById(R.id.nineBtnId);
        Button dotBtn = findViewById(R.id.dotBtnId);
        //Action buttons
        Button clearBtn = findViewById(R.id.clearBtnId);
        Button signBtn = findViewById(R.id.signBtnId);
        Button percentBtn = findViewById(R.id.percentBtnId);
        Button divBtn = findViewById(R.id.divBtnId);
        Button mulBtn = findViewById(R.id.mulBtnId);
        Button subBtn = findViewById(R.id.subBtnId);
        Button sumBtn = findViewById(R.id.sumBtnId);
        Button equalBtn = findViewById(R.id.equalBtnId);

        //Num buttons On Click Listeners
        zeroBtn.setOnClickListener(numBtnOnClickListener);
        oneBtn.setOnClickListener(numBtnOnClickListener);
        twoBtn.setOnClickListener(numBtnOnClickListener);
        threeBtn.setOnClickListener(numBtnOnClickListener);
        fourBtn.setOnClickListener(numBtnOnClickListener);
        fiveBtn.setOnClickListener(numBtnOnClickListener);
        sixBtn.setOnClickListener(numBtnOnClickListener);
        sevenBtn.setOnClickListener(numBtnOnClickListener);
        eightBtn.setOnClickListener(numBtnOnClickListener);
        nineBtn.setOnClickListener(numBtnOnClickListener);
        dotBtn.setOnClickListener(dotBtnOnClickListener);
        //Action buttons On Click Listeners
        clearBtn.setOnClickListener(clearBtnOnClickListener);
        signBtn.setOnClickListener(signBtnOnClickListener);
        percentBtn.setOnClickListener(percentOnClickListener);
        divBtn.setOnClickListener(actionBtnOnClickListener);
        mulBtn.setOnClickListener(actionBtnOnClickListener);
        subBtn.setOnClickListener(actionBtnOnClickListener);
        sumBtn.setOnClickListener(actionBtnOnClickListener);
        equalBtn.setOnClickListener(equalOnClickListener);

        calcTxtStringBuilder = new StringBuilder();
        sharedPreferences = getSharedPreferences("Image", MODE_PRIVATE);
    }

    private float equal(String calcTxt) {
        float result = 0;

        String calcSmallTxt = calcSmallTxtView.getText().toString();
        String[] calcSmallTxtSplit = calcSmallTxt.split(" ");
        float firstNum = Float.parseFloat(calcSmallTxtSplit[0]);
        action = calcSmallTxtSplit[1];
        float secondNum = Float.parseFloat(calcTxt);

        switch (action) {
            case "÷":
                if (secondNum != 0) {
                    result = firstNum / secondNum;
                } else {
                    clear();
                    Toast.makeText(this, R.string.division_by_zero_message, Toast.LENGTH_SHORT).show();
                }
                break;
            case "×":
                result = firstNum * secondNum;
                break;
            case "+":
                result = firstNum + secondNum;
                break;
            case "-":
                result = firstNum - secondNum;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        return result;
    }

    private void clear() {
        calcTxtStringBuilder.setLength(0);
        calcSmallTxtView.setText("");
        calcMainTxtView.setText("");
        action = null;
        sign = false;
        secondNumFlag = false;
    }

    private void createSomeExternalImages() {
        createExternalImageStartActivity(1);
        createExternalImageStartActivity(2);
    }

    private void createExternalImageStartActivity(int id) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_TITLE, "Sandman_" + id + ".jpg");
        startActivityForResult(intent, CREATE_IMAGE_URI);
    }

    private void createExternalImages(Uri uri) {
        int resId;
        switch (imagesCreatedCounter) {
            default:
            case 0:
                resId = R.drawable.sandman_jap_1;
                imagesCreatedCounter++;
                break;
            case 1:
                resId = R.drawable.sandman_jap_2;
                break;
        }

        Bitmap image = BitmapFactory.decodeResource(getResources(), resId);

        final ContentResolver resolver = getApplicationContext().getContentResolver();

        if (readImageFromUri(uri) == null) {
            try (OutputStream stream = resolver.openOutputStream(Objects.requireNonNull(uri))) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } catch (IOException e) {
                if (uri != null) {
                    resolver.delete(Objects.requireNonNull(uri), null, null);
                }
                e.printStackTrace();
            }
        }
    }

    private void setBackgroundImageFromSharedPrefs() {
        String imagePath = sharedPreferences.getString(IMAGE_KEY, "");
        if (!imagePath.matches("")) {
            Uri imageUri = Uri.parse(imagePath);
            setBackgroundImage(imageUri);
        }
    }

    private void setBackgroundImage(Uri uri) {
        try (ParcelFileDescriptor parcelFileDescriptor =
                     getContentResolver().openFileDescriptor(Objects.requireNonNull(uri), "r")) {
            FileDescriptor fileDescriptor = Objects.requireNonNull(parcelFileDescriptor).getFileDescriptor();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            backgroundImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap readImageFromUri(Uri uri) {
        try (InputStream stream = getContentResolver().openInputStream(uri)) {
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            return null;
        }
    }
}
