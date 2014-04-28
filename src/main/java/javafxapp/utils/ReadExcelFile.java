package javafxapp.utils;

import javafxapp.adapter.Register;
import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.fns.Pojo;
import javafxapp.controller.BuilderRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: vmaksimov
 */
public class ReadExcelFile {

    private static HSSFWorkbook workbook;
    private static FileInputStream fileInputStream;
    private static DecimalFormat decimalFormat = new DecimalFormat("#");
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static List<Pojo> readFNSData(String filePath) throws IOException {
        readFile(filePath);

        List<Pojo> fnsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(Register.FNS.foiv)) {
               fillFNSFromExcel(sheet, fnsList);
            }
        }
        return fnsList;
    }

    public static List<javafxapp.adapter.mvd.Pojo> readMVDData(String filePath) throws IOException {
        readFile(filePath);

        List<javafxapp.adapter.mvd.Pojo> mvdList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().contains(Register.MVD.foiv + Register.MVD.adapter)) {
                fillMVDFromExcel(sheet, mvdList);
            }
        }
        return mvdList;
    }

    private static void readFile(String filePath) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        InputStream input = new BufferedInputStream(fileInputStream);
        POIFSFileSystem fs = new POIFSFileSystem(input);
        workbook = new HSSFWorkbook(fs);
    }

    public static void writeFNSStatus(List<Adapter> adapterList, String filePath, String sheetName, int positionStatus) {
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(sheetName)) {

                for (int r = 2; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row != null) {
                        setStatusInCell(adapterList, sheet, r, row, positionStatus);
                    }
                }
                writeOutputStream(filePath);
            }
        }
    }


    private static void setStatusInCell(List<Adapter> adapterList, HSSFSheet sheet, int r, Row row, int positionStatus) {
        Cell cell = row.getCell(positionStatus);
        for (Adapter adapter: adapterList) {
            if ((adapter.getNumReq() == r) &&
                    sheet.getSheetName().contains(adapter.getAdapterDetails().getAdapterName())) {
                if (cell == null)
                    row.createCell(positionStatus).setCellValue(adapter.getResponseStatus());
                else
                    cell.setCellValue(adapter.getResponseStatus());
            }
        }
    }

    private static void writeOutputStream(String filePath) {
        FileOutputStream outFile;
        try {
            fileInputStream.close();
            outFile = new FileOutputStream(filePath);
            workbook.write(outFile);
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fillFNSFromExcel(HSSFSheet sheet, List<Pojo> fnsList) {

        for (Row row : sheet) {
            if (row.getRowNum() >= 2) {

                Pojo fns = null;
                Cell cell = row.getCell(AdapterCells.Fns.enterData);
                if (cell != null && HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    try {
                        fns = (Pojo) BuilderRequest.fillSmevFieldsDefault("fns");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (fns == null) break;
                    if (sheet.getSheetName().contains(Register.FNS.adapter)) {
                        fns.setIsInn("on");
                        fns.setInn(decimalFormat.format(cell.getNumericCellValue()));
                        fns.setRowNum(row.getRowNum());
                        fnsList.add(fns);
                    }else if (sheet.getSheetName().contains(Register.FNS.adapterUL)) {
                        fns.setIsOgrn("on");
                        fns.setOgrn(decimalFormat.format(cell.getNumericCellValue()));
                        fns.setRowNum(row.getRowNum());
                        fnsList.add(fns);
                    }
                }
            }
        }
    }

    private static void fillMVDFromExcel(HSSFSheet sheet, List<javafxapp.adapter.mvd.Pojo> mvdList) {


        for (Row row : sheet) {
            if (row.getRowNum() >= 3) {

                javafxapp.adapter.mvd.Pojo mvd = new javafxapp.adapter.mvd.Pojo();
                try {
                    mvd = (javafxapp.adapter.mvd.Pojo) BuilderRequest.fillSmevFieldsDefault("mvd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (Cell cell : row) {
                    if (cell != null) {
                        if (cell.getColumnIndex() == AdapterCells.Mvd.typeRequest) mvd.setTypeRequest(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.reason) {
                            if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()){
                                mvd.setReason(decimalFormat.format(cell.getNumericCellValue()));
                            }else if (HSSFCell.CELL_TYPE_STRING == cell.getCellType()){
                                mvd.setReason(cell.getStringCellValue());
                            }
                        }
                        if (cell.getColumnIndex() == AdapterCells.Mvd.originatorFio) mvd.setOriginatorFio(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.originatorTel) mvd.setOriginatorTel(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.originatorRegion) mvd.setOriginatorRegion(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.FirstName) mvd.setFirstName(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.FathersName) mvd.setFathersName(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.SecName) mvd.setSecName(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.DateOfBirth) mvd.setDateOfBirth(simpleDateFormat.format(cell.getDateCellValue()));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.SNILS) mvd.setSNILS(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.PlaceOfBirth_code) mvd.setPlaceOfBirth_code(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.PlaceOfBirth) mvd.setPlaceOfBirth(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.addressRegion) mvd.setAddressRegion(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.addressTypeRegistration) mvd.setAddressTypeRegistration(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.addressRegistrationPlace) mvd.setAddressRegistrationPlace(cell.getStringCellValue());
                    }
                }
                mvd.setRowNum(row.getRowNum());
                mvdList.add(mvd);
                }
        }
    }

    private static String getCode(Cell cell) {
        if (cell.getStringCellValue().contains("[") && cell.getStringCellValue().contains("]"))
            return cell.getStringCellValue().substring(cell.getStringCellValue().indexOf("[")+1, cell.getStringCellValue().lastIndexOf("]"));
        else
            return cell.getStringCellValue();
    }

}



