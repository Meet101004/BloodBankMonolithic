package com.example.bloodbank.utils;

import com.example.bloodbank.entity.BloodRequest;
import com.example.bloodbank.entity.BloodStock;
import com.example.bloodbank.entity.Donation;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StorageHelper {

    static DateTimeFormatter dt=DateTimeFormatter.ISO_DATE_TIME;
    public static byte[] getExcelFile(List<Donation> donationList, List<BloodRequest> bloodRequestList,List<BloodStock> bloodStockList){
        try(ByteArrayOutputStream os=new ByteArrayOutputStream();Workbook workbook = new XSSFWorkbook()){

            //FOR DONATIONS


            Sheet sheet = workbook.createSheet("Donations");
            String[] headers={"ID","DONOR_NAME","BLOOD_GROUP","QUANTITY","DONATION_DATE"};
            Row headerRow = sheet.createRow(0);
            for(int i=0;i< headers.length;i++){
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int i=1;
            for(Donation d:donationList){
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(d.getId());
                row.createCell(1).setCellValue(d.getDonorDetails().getUser().getName());
                row.createCell(2).setCellValue(d.getBloodGroup());
                row.createCell(3).setCellValue(d.getQuantity());
                row.createCell(4).setCellValue(d.getDonationDate().format(dt));
                i++;
            }

            for(i=0;i< headers.length;i++){
                sheet.autoSizeColumn(i);
            }

            //FOR BLOOD_REQUEST

            Sheet sheet2 = workbook.createSheet("Blood_Request");
            String[] headers2={"ID","HOSPITAL_NAME","BLOOD_GROUP","QUANTITY","REQUEST_DATE","STATUS"};
            Row headerRow2 = sheet2.createRow(0);
            for(i=0;i< headers2.length;i++){
                headerRow2.createCell(i).setCellValue(headers2[i]);
            }

            i=1;
            for(BloodRequest b:bloodRequestList){
                Row row = sheet2.createRow(i);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getHospital().getHospitalName());
                row.createCell(2).setCellValue(b.getBloodGroup());
                row.createCell(3).setCellValue(b.getQuantity());
                row.createCell(4).setCellValue(b.getRequestDate().format(dt));
                row.createCell(5).setCellValue(b.getStatus());
                i++;
            }
            for(i=0;i< headers2.length;i++){
                sheet2.autoSizeColumn(i);
            }

            //FOR BLOOD_REQUEST

            Sheet sheet3 = workbook.createSheet("Blood_Stock");
            String[] headers3={"ID","BLOOD_GROUP","QUANTITY","LAST_UPDATED"};
            Row headerRow3 = sheet3.createRow(0);
            for(i=0;i< headers3.length;i++){
                headerRow3.createCell(i).setCellValue(headers3[i]);
            }

            i=1;
            for(BloodStock b:bloodStockList){
                Row row = sheet3.createRow(i);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getBloodGroup());
                row.createCell(2).setCellValue(b.getAvailableUnits());
                row.createCell(3).setCellValue(b.getLastUpdated().format(dt));
                i++;
            }

            for(i=0;i< headers3.length;i++){
                sheet3.autoSizeColumn(i);
            }

            workbook.write(os);
            return os.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
