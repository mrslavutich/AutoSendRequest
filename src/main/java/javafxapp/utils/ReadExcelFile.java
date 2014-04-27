package javafxapp.utils;

import javafxapp.adapter.Register;
import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.fns.FNS;
import javafxapp.controller.BuilderRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: vmaksimov
 */
public class ReadExcelFile {

    public static final int CELL_STATUS = 2;
    public static final int CELL_ENTER_DATA = 1;
    private static HSSFWorkbook workbook;
    private static FileInputStream fileInputStream;

    public static List<FNS> readFNSData(String filePath) throws IOException {
        readFile(filePath);

        List<FNS> fnsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(Register.FNS.foiv)) {
               fillFNSFromExcel(sheet, fnsList);
            }
        }
        return fnsList;
    }

    private static void readFile(String filePath) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        InputStream input = new BufferedInputStream(fileInputStream);
        POIFSFileSystem fs = new POIFSFileSystem(input);
        workbook = new HSSFWorkbook(fs);
    }

    public static void writeFNSStatus(List<Adapter> adapterList, String filePath) {
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(Register.FNS.foiv)) {

                for (int r = 2; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    Cell cellData = null;
                    if (row != null) cellData = row.getCell(CELL_ENTER_DATA);
                    if (cellData != null && HSSFCell.CELL_TYPE_NUMERIC == cellData.getCellType()) {
                        setStatusInCell(adapterList, sheet, r, row);
                    }
                }

                writeOutputStream(filePath);
            }
        }
    }

    private static void setStatusInCell(List<Adapter> adapterList, HSSFSheet sheet, int r, Row row) {
        Cell cell = row.getCell(CELL_STATUS);
        for (Adapter adapter: adapterList) {
            if ((adapter.getNumReq() == r) &&
                    sheet.getSheetName().contains(adapter.getAdapterDetails().getAdapterName())) {
                if (cell == null)
                    row.createCell(CELL_STATUS).setCellValue(adapter.getResponseStatus());
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

    private static void fillFNSFromExcel(HSSFSheet sheet, List<FNS> fnsList) {

        for (Row row : sheet) {
            if (row.getRowNum() >= 2) {

                FNS fns = null;
                Cell cell = row.getCell(CELL_ENTER_DATA);
                if (cell != null && HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    try {
                        fns = BuilderRequest.fillSmevFieldsDefault();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (fns == null) break;
                    DecimalFormat decimalFormat = new DecimalFormat("#");
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

}



