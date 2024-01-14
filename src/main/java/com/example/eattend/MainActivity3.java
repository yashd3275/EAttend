package com.example.eattend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity3 extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
    public static final String ROLLL = "ROLLL";
    public static final String CLG = "CLG";
    public static final String PROF = "PROF";
    public static final String SEM = "SEM";
    public static final String BRA = "BRA";
    public static final String SUB = "SUB";
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private int limit;
    private int Counter = 1;
    private HSSFWorkbook hssfWorkbook;
    private HSSFSheet hssfSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);




        limit = getIntent().getIntExtra("ROLLL", 0);

        TextView txtCount = findViewById(R.id.txtCount);
        Button btnPre = findViewById(R.id.btnPre);
        Button btnAbs = findViewById(R.id.btnAbs);

        if (!arePermissionsGranted()) {
            requestPermissions();
        }

        hssfWorkbook = new HSSFWorkbook();
        hssfSheet = hssfWorkbook.createSheet("EAttendStudentData");


        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("1", txtCount);
            }
        });

        btnAbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("0", txtCount);
            }
        });
    }

    private void handleButtonClick(String studentStatus, TextView txtCount) {
        if (Counter <= limit) {
            sendData(Counter, studentStatus);
            Counter++;
            txtCount.setText(String.valueOf(Counter));
        } else {
            disableButtons();
            try {
                saveExcel();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving attendance!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void   sendData(int rollNumber, String studentStatus) {
        HSSFRow hssfRow = hssfSheet.createRow(Counter - 1);


        HSSFCell rollNumberCell = hssfRow.createCell(0);
        rollNumberCell.setCellValue("Roll no. - "+rollNumber);


        HSSFCell statusCell = hssfRow.createCell(1);
        statusCell.setCellValue("Status - "+studentStatus);

        HSSFCell cl = hssfRow.createCell(2);
        cl.setCellValue("College "+CLG);

        HSSFCell br = hssfRow.createCell(3);
        br.setCellValue("Branch "+BRA);

        HSSFCell sm = hssfRow.createCell(4);
        sm.setCellValue("Sem. "+SEM);

        HSSFCell sb = hssfRow.createCell(5);
        sb.setCellValue("Subject "+SUB);

        HSSFCell pr = hssfRow.createCell(6);
        pr.setCellValue("Prof. "+PROF);

        HSSFCell dt = hssfRow.createCell(7);
        dt.setCellValue("Date "+currentDate);


        hssfSheet.setColumnWidth(0,(30*100 ));
        hssfSheet.setColumnWidth(1,(30*100));
        hssfSheet.setColumnWidth(2,(30*400 ));
        hssfSheet.setColumnWidth(3,(30*300 ));
        hssfSheet.setColumnWidth(4,(30*300 ));
        hssfSheet.setColumnWidth(5,(30*300 ));
        hssfSheet.setColumnWidth(6,(30*500 ));
        hssfSheet.setColumnWidth(7,(30*300 ));


    }

    private boolean arePermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (arePermissionsGranted()) {
                try {
                    saveExcel();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error saving attendance!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle denied permission
            }
        }
    }

    private void disableButtons() {
        Button btnPre = findViewById(R.id.btnPre);
        Button btnAbs = findViewById(R.id.btnAbs);
        btnPre.setEnabled(false);
        btnAbs.setEnabled(false);
    }

    private void saveExcel() throws IOException {
        File dir = new File(getExternalFilesDir(null), "E-Attend");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, currentDate+""+"EAttend.xls");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        hssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();
        hssfWorkbook.close();

        // Get FileProvider URI
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

        // Grant permission to the URI for Intent.FLAG_GRANT_READ_URI_PERMISSION
        grantUriPermission(getPackageName(), fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Save the file using the file provider URI
        Toast.makeText(this, "Attendance saved!", Toast.LENGTH_SHORT).show();

        // Open an Intent to share the file
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.ms-excel");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Excel File"));
    }


}

