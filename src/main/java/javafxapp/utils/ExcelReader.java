package javafxapp.utils;

import javafxapp.adapter.Register;
import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.fns.Pojo;
import javafxapp.controller.BuilderRequest;
import javafxapp.controller.ErrorController;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: vmaksimov
 */
public class ExcelReader {

    public static final String status_response = "Статус ответа";
    private static HSSFWorkbook workbook;
    private static FileInputStream fileInputStream;
    private static DecimalFormat decimalFormat = new DecimalFormat("#");
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static String lblStatus;
    private static int positionStatus;

    public static List<Pojo> readFNSData(String filePath, String nameAdapter) throws IOException {
        readFile(filePath);

        List<Pojo> fnsList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(Register.foiv.FNS.getValue() + "_" + nameAdapter)) {
               fillFNSFromExcel(sheet, fnsList);
            }
        }
        return fnsList;
    }

    public static List<javafxapp.adapter.mvd.Pojo> readMVDData(String filePath) throws IOException {
        readFile(filePath);

        List<javafxapp.adapter.mvd.Pojo> mvdList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().contains(Register.foiv.MVD.getValue() + "_" + Register.id410.getNameAdapter())) {
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

    public static void writeStatus(List<Adapter> adapterList, String filePath, String sheetName) {
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(sheetName)) {

                for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row != null) {
                        setStatusInCell(adapterList, sheet, r, row);
                    }
                }
                writeOutputStream(filePath);
            }
        }
    }


    private static void setStatusInCell(List<Adapter> adapterList, HSSFSheet sheet, int r, Row row) {
        definePositionStatus(row);
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

    private static void definePositionStatus(Row row) {
        if(row.getRowNum() < 3) {
            lblStatus = getValue(row.getCell(AdapterCells.Fns.status_short));
            findLblStatus(row, AdapterCells.Fns.status_full);
            findLblStatus(row, AdapterCells.Mvd.status);
            if (lblStatus.contains(status_response)) positionStatus = row.getRowNum();
        }
    }

    private static void findLblStatus(Row row, int position) {
        if (!lblStatus.contains(status_response)) lblStatus = getValue(row.getCell(position));
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
                try {
                    fns = (Pojo) BuilderRequest.fillSmevFieldsDefault("fns");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fns == null) break;
                Cell cellTypeDoc = row.getCell(AdapterCells.Fns.typeDoc);
                if (cellTypeDoc != null && HSSFCell.CELL_TYPE_STRING == cellTypeDoc.getCellType() && !cellTypeDoc.getStringCellValue().isEmpty()) {

                    Cell cellDocValue = row.getCell(AdapterCells.Fns.valueDoc);
                    if (cellDocValue == null || HSSFCell.CELL_TYPE_NUMERIC != cellDocValue.getCellType()) break;

                    fns.setId210fz(Register.findId210fz(sheet.getSheetName()));

                    Cell cellNomerDela = row.getCell(AdapterCells.Fns.nomerDela);
                    fns.setNomerDela(getValue(cellNomerDela));

                    if (cellTypeDoc.getStringCellValue().equals("ИНН")){
                        fns.setIsInn("on");
                        fns.setInn(decimalFormat.format(cellDocValue.getNumericCellValue()));
                        fns.setRowNum(row.getRowNum());
                        fnsList.add(fns);
                    }else if (cellTypeDoc.getStringCellValue().equals("ОГРН")) {
                        fns.setIsOgrn("on");
                        fns.setOgrn(decimalFormat.format(cellDocValue.getNumericCellValue()));
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
                        if (cell.getColumnIndex() == AdapterCells.Mvd.typeRequest) {
                            if (cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty())
                                break;
                            mvd.setTypeRequest(cell.getStringCellValue());
                        }
                        if (cell.getColumnIndex() == AdapterCells.Mvd.reason) {
                            mvd.setReason(getValue(cell));
                        }
                        if (cell.getColumnIndex() == AdapterCells.Mvd.originatorFio) mvd.setOriginatorFio(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.originatorTel) mvd.setOriginatorTel(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.originatorRegion) mvd.setOriginatorRegion(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.FirstName) mvd.setFirstName(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.FathersName) mvd.setFathersName(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.SecName) mvd.setSecName(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.DateOfBirth) {
                            if (cell.getDateCellValue() != null)
                            mvd.setDateOfBirth(simpleDateFormat.format(cell.getDateCellValue()));
                        }
                        if (cell.getColumnIndex() == AdapterCells.Mvd.SNILS) mvd.setSNILS(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.PlaceOfBirth_code) mvd.setPlaceOfBirth_code(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.PlaceOfBirth) mvd.setPlaceOfBirth(cell.getStringCellValue());
                        if (cell.getColumnIndex() == AdapterCells.Mvd.addressRegion) mvd.setAddressRegion(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.addressTypeRegistration) mvd.setAddressTypeRegistration(getCode(cell));
                        if (cell.getColumnIndex() == AdapterCells.Mvd.addressRegistrationPlace) mvd.setAddressRegistrationPlace(cell.getStringCellValue());
                    }
                }
                mvd.setRowNum(row.getRowNum());
                mvd.setId210fz(Register.findId210fz(sheet.getSheetName()));
                if (mvd.getTypeRequest() != null && !mvd.getTypeRequest().isEmpty()) mvdList.add(mvd);
            }
        }
    }

    private static String getValue(Cell cell) {
        if (cell == null) return "";
        if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()){
            return decimalFormat.format(cell.getNumericCellValue());
        }else if (HSSFCell.CELL_TYPE_STRING == cell.getCellType()){
            return (cell.getStringCellValue() == null ? "" : cell.getStringCellValue());
        }
        return "";
    }

    private static String getCode(Cell cell) {
        if (cell.getStringCellValue() != null && cell.getStringCellValue().contains("[") && cell.getStringCellValue().contains("]"))
            return cell.getStringCellValue().substring(cell.getStringCellValue().indexOf("[")+1, cell.getStringCellValue().lastIndexOf("]"));
        else
            return cell.getStringCellValue();
    }

    public static void copyFile(File from, File to) {
        try {
            Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ErrorController.showDialog("Невозможно скопировать файл для записи результирующих статусов " + e.getLocalizedMessage());
        }
    }
}



