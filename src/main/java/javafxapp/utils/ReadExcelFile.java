package javafxapp.utils;

import javafxapp.adapter.Register;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * User: vmaksimov
 */
public class ReadExcelFile {

    public static final int CELL_STATUS = 2;
    public static final int CELL_ENTER_DATA = 1;
    private static HSSFWorkbook workbook;
    private static FileInputStream fileInputStream;

    public static HashMap<String, List<FNS>> readFNSData(String filePath) throws IOException {
        HashMap<String, List<FNS>> mapFns = new HashMap<>();
        readFile(filePath);

        List<FNS> ip = new ArrayList<>();
        List<FNS> ul = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(Register.FNS.foiv)) {
               fillFNSFromExcel(sheet, ip, ul);
            }
        }
        mapFns.put(Register.FNS.adapter, ip);
        mapFns.put(Register.FNS.adapterUL, ul);
        return mapFns;
    }

    private static void readFile(String filePath) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        InputStream input = new BufferedInputStream(fileInputStream);
        POIFSFileSystem fs = new POIFSFileSystem(input);
        workbook = new HSSFWorkbook(fs);
    }

    public static void writeFNSStatus(List<String> listStatus, String type, String filePath) {
        for (int i = 0; i < 3; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().startsWith(Register.FNS.foiv) && sheet.getSheetName().contains(type)) {
                Iterator iteratorStatus = listStatus.iterator();
                for (int r = 2; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    Cell cellData = null;
                    if (row != null) cellData = row.getCell(CELL_ENTER_DATA);
                    if (cellData != null && HSSFCell.CELL_TYPE_NUMERIC == cellData.getCellType()){
                        Cell cell = row.getCell(CELL_STATUS);
                        cell.setCellValue(iteratorStatus.hasNext() ? iteratorStatus.next().toString() : "");
                    }
                }

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
        }
    }


    private static void fillFNSFromExcel(HSSFSheet sheet, List<FNS> ip, List<FNS> ul) {

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
                        ip.add(fns);
                    }else if (sheet.getSheetName().contains(Register.FNS.adapterUL)) {
                        fns.setIsOgrn("on");
                        fns.setOgrn(decimalFormat.format(cell.getNumericCellValue()));
                        ul.add(fns);
                    }
                }
            }
        }
    }

}



