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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

/**
 * User: vmaksimov
 */
public class ReadExcelFile {

    public static void read(String filePath, List<FNS> fnsList) {
        try {
            InputStream input = new BufferedInputStream(
                    new FileInputStream(filePath));
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);

            for (int i = 0; i < 3; i++) {
                HSSFSheet sheet = wb.getSheetAt(i);
                if (sheet.getSheetName().startsWith(Register.FNS.foiv)) {
                    fillFNSFromExcel(fnsList, sheet);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void fillFNSFromExcel(List<FNS> fnsList, HSSFSheet sheet) {

        for (Row row : sheet) {
            if (row.getRowNum() >= 2) {

                FNS fns = null;
                Cell cell = row.getCell(1);
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
                        fns.setInns(decimalFormat.format(cell.getNumericCellValue()));
                    }else if (sheet.getSheetName().contains(Register.FNS.adapterTwoInst)) {
                        fns.setIsOgrn("on");
                        fns.setOgrns(decimalFormat.format(cell.getNumericCellValue()));
                    }
                }
                fnsList.add(fns);
            }
        }
    }

}



