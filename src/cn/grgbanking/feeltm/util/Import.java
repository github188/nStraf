package cn.grgbanking.feeltm.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import cn.grgbanking.feeltm.config.JdbcConnect;

public class Import {
	public static Map<String, String[]> items;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Import im = new Import();
		im.importGrp("C:\\Book2.xls");
	}
	
	public boolean importGrp(String filepath){
		Import im = new Import();
		items = im.import2Map(filepath, 0, 1);
		try{
			im.updateUser();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// �����û���Ϣ
	public void updateUser() throws Exception {
		Connection conn = JdbcConnect.getConnection();
		PreparedStatement pst = null;
		try {
			for (Map.Entry<String, String[]> entry : items.entrySet()) {
				String[] contr = entry.getValue();
				String sql="update usr_hols set c_before_deferred=? where c_userid=?";
				pst = conn.prepareStatement(sql);
				String str = contr[6];
				if("".equals(str) || str==null){
					str="0";
				}
				pst.setString(1, str);
				pst.setString(2, contr[1]);
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public Map<String, String[]> import2Map(String path,int sheetIndex, int rowIndex) {
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		InputStream is = null;
		Workbook rwb = null;
		try {
			is = new FileInputStream(path);// ��ȡ
			try {
				rwb = Workbook.getWorkbook(is);
				Sheet sheet = rwb.getSheet(sheetIndex);// �uڼ�����
				for (int i = rowIndex; i < sheet.getRows(); i++) {
					List<String> strs = new ArrayList<String>();
					for (int j = 0; j < sheet.getColumns(); j++) {
						Cell cell = sheet.getCell(j, i);
						if (CellType.DATE.equals(cell.getType())) {
							DateCell dc = (DateCell) cell;
							java.util.Date date = dc.getDate();
							strs.add(new SimpleDateFormat("yyyy-MM-dd")
									.format(date));
						} else if (CellType.NUMBER.equals(cell.getType())) {
							strs.add(cell.getContents());
						} else {
							strs.add(cell.getContents().trim());
						}
					}
					map.put(String.valueOf(i), strs.toArray(new String[] {}));
				}
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				rwb.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		System.out.println("map rows:" + map.size() + "(+" + rowIndex + ")");
		return map;
	}

}
