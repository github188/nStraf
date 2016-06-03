package cn.grgbanking.feeltm.util;


public class ImportDay {
	/*public String importData() throws Exception{
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("c:\\放假安排时间表.xls"));
		HSSFWorkbook wb =  new HSSFWorkbook(fs);
		int sheetNumber = wb.getNumberOfSheets();
		List<Holiday> holidayList = new ArrayList<Holiday>();
		for(int shNum=0;shNum<sheetNumber;shNum++){
			HSSFSheet sheet = wb.getSheetAt(shNum);
			long rowNumber = sheet.getPhysicalNumberOfRows();
			for(int rowNum=1;rowNum<=rowNumber;rowNum++){
				HSSFRow row = sheet.getRow(rowNum);
				if(row == null){
					continue;
				}
				String holidaydate = row.getCell((short)0).getStringCellValue();
				String workdate = row.getCell((short)1).getStringCellValue();
				if(!"".equals(holidaydate)){
					String[] holidays = holidaydate.split("至");
					if(holidays.length==2){
						List<String> list = DateUtil.getDateLine(holidays[0], holidays[1]);
						for(String days:list){
							Holiday holiday = new Holiday();
							Date date = DateUtil.stringToDate(days, "yyyy-MM-dd");
							holiday.setHolidaydate(date);
							holiday.setType("0");
							String isWorkDay = DateUtil.isWorkDay(days);
							holiday.setIsworkday(isWorkDay);
							holidayList.add(holiday);
						}
					}else{
						Holiday holiday = new Holiday();
						Date date = DateUtil.stringToDate(holidaydate, "yyyy-MM-dd");
						holiday.setHolidaydate(date);
						holiday.setType("0");
						String isWorkDay = DateUtil.isWorkDay(holidaydate);
						holiday.setIsworkday(isWorkDay);
						holidayList.add(holiday);
					}
				}
				if(!"".equals(workdate)){
					String[] workdates = workdate.split("、");
					for(int i=0;i<workdates.length;i++){
						Holiday holiday = new Holiday();
						Date date = DateUtil.stringToDate(workdates[i], "yyyy-MM-dd");
						holiday.setWorkdate(date);
						holiday.setType("1");
						String isWorkDay = DateUtil.isWorkDay(workdates[i]);
						holiday.setIsworkday(isWorkDay);
						holidayList.add(holiday);
					}
				}
			}
		}
		connectionOracle(holidayList);
		return "";
	}
	
	public static void main(String[] args) {
//		String s1="2014-03-09";
//		String s2="2014-04-12";
//		Date start = DateUtil.stringToDate(s1, "yyyy-MM-dd");
//		Date end = DateUtil.stringToDate(s2, "yyyy-MM-dd");
		ImportHoliday test = new ImportHoliday();
		try{
			test.importData();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void connectionOracle(List<Holiday> holidayList)throws Exception{
		Connection conn = JdbcConnect.getConnection();
		PreparedStatement pst = null;
		try {
			String sql = "";
			for(Holiday list:holidayList){
				String key = UUID.randomUUID().toString(); 
				key = key.substring(0,8)+key.substring(9,13)+key.substring(14,18)+key.substring(19,23)+key.substring(24);
				sql="insert into oa_holiday(id,holiday_date,work_date,type,isworkday) values (?,?,?,?,?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, key);
				if(list.getHolidaydate()==null){
					pst.setDate(2, null);
				}else{
					java.sql.Date holidaydate=new java.sql.Date(list.getHolidaydate().getTime());
					pst.setDate(2, holidaydate);
				}
				if(list.getWorkdate()==null){
					pst.setDate(3, null);
				}else{
					java.sql.Date workdate = new java.sql.Date(list.getWorkdate().getTime());
					pst.setDate(3, workdate);
				}
				pst.setString(4, list.getType());
				pst.setString(5, list.getIsworkday());
				pst.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
}
