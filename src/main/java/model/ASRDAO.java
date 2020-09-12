package model;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import connections.IDao;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;


public class ASRDAO {

	private CallableStatement sp_trackresults;
	private CallableStatement sp_process_insert;
	private PreparedStatement selStateMaster;
	private PreparedStatement selDistributor;
	private PreparedStatement sel_activity;

	private PreparedStatement p_getdata;

	private PreparedStatement sel_getactivityresults; //IH_DW.DW_ODS_ACTIVITY
    private PreparedStatement sel_getresultwithreqid; // Get result with activity requisition_id




	private ASRListener listener;

	private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	private static Logger logger = lc.getLogger("ASRDAO");

	private final Set<String> nemSet = new HashSet<String>();

	private Connection con;

	private final IDao dao;


	private PreparedStatement geteastactivity;


	private List<String> month = new LinkedList<String>();

	public ASRDAO (IDao dao) {
		logger.info("Initialized");
		this.dao = dao;
	}


	public void setListener(ASRListener listener){

		this.listener = listener;
		if(listener != null){

		}
	}





	public void m_spProcessInsert(String state_abbrev)  {
		ResultSet rst = null;
		ResultSetMetaData rsmd = null;

		try
		{
			sp_process_insert.setString(1,state_abbrev);
			sp_process_insert.execute();


		} catch (SQLException se){
			se.printStackTrace();
		} finally {

		}

	}

    private static Timestamp getCurrentDate(String strDate) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MONTH,10);
        cal.set(Calendar.DAY_OF_MONTH,1);

		System.out.println(cal.getTime());


        java.util.Date today = cal.getTime();
        return new Timestamp(cal.getTimeInMillis());
    }

    // Get Results by requisition_id
    public void psel_getReqResult(String reqid){

		if(reqid.equals("")) {

				if(listener != null){

				}

			return;
		}

        ResultSet rst = null;
        ResultSetMetaData rsmd = null;
        StringBuffer sb = new StringBuffer();

        try
        {

            sel_getresultwithreqid.setString(1,reqid);



            rst = sel_getresultwithreqid.executeQuery();

            rsmd = rst.getMetaData();

            int colCount = rsmd.getColumnCount() + 1;
            int cnt = 0;

            for (int i = 1; i < colCount; i++){
                //System.out.println(i + " " + rsmd.getColumnName(i) + " " + rsmd.getColumnType(i) + " " + rsmd.getColumnTypeName(i));
            }

            /* Result Metadata
            7  - Accession_Number
            11 - Order_Test_Code
            12 - Order_Test_Name

			*/


			while (rst.next()) {

				if (nemSet.contains(rst.getString(14))) {


					sb.append(rst.getString(6) + "|");
					sb.append(rst.getString(13) + "|");
					sb.append(rst.getString(29) + "|");
					sb.append(rst.getString(14) + "|");
					sb.append(rst.getString(18));

					sb.append("\n");
					if(listener != null){

					}
					sb.setLength(0);
				}

			}



            rst.close();

        } catch (SQLException se){
            se.printStackTrace();
        } finally {
            if (rst != null) {
                try {
                    rst.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }

            }
        }
    }

    public void m_selActivityByDate(String startDate)  {
        ResultSet rst = null;
        ResultSetMetaData rsmd = null;


        try
        {

            System.out.println(getCurrentDate(""));
            sel_getactivityresults.setTimestamp(1,getCurrentDate(""));



            rst = sel_getactivityresults.executeQuery();

            rsmd = rst.getMetaData();

            int colCount = rsmd.getColumnCount() + 1;
            int cnt = 0;

            for (int i = 1; i < colCount; i++){
                //System.out.println(rsmd.getColumnName(i));
            }

            while(rst.next()){

				if(rst.getString(1).equals("28828AH")) {
					System.out.println(cnt++ + " " + rst.getString(1) + " " + rst.getString(2)  );
					psel_getReqResult(rst.getString(1));
					//System.exit(0);
				}
            }

            rst.close();

        } catch (SQLException se){
            se.printStackTrace();
        } finally {
            if (rst != null) {
                try {
                    rst.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }

            }
        }

    }

	public void m_selStateMaster(String state_abbrev, String status)  {
		ResultSet rst = null;
		ResultSetMetaData rsmd = null;

		try
		{
			selStateMaster.setString(1,state_abbrev);
			selStateMaster.setString(2,status);

			rst = selStateMaster.executeQuery();
			rsmd = rst.getMetaData();

			int colCount = rsmd.getColumnCount() + 1;

			for (int i = 1; i < colCount; i++){
				System.out.println(rsmd.getColumnName(i));
			}

			rst.close();

		} catch (SQLException se){

		} finally {
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException se) {

				}

			}
		}

	}

	public void selActivity()  {

		logger.error("OracleDAO model selActivity *************");
		ResultSet rst = null;
		ResultSetMetaData rsmd = null;

		try
		{

			rst = sel_activity.executeQuery();
			rsmd = rst.getMetaData();

			int colCount = rsmd.getColumnCount() + 1;

			for (int i = 1; i < colCount; i++){
				System.out.println(rsmd.getColumnName(i));
			}
			while(rst.next()){
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++){
					System.out.print(rst.getString(i) + " - ");
				}
				System.out.println();
			}

			rst.close();

		} catch (SQLException se){
			se.printStackTrace();
		} finally {
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}

			}
		}

	}
	
	public void getDataFromSelect(String reqid){
		ResultSet rst = null;
		ResultSetMetaData rsmd = null;

		try
		{
			p_getdata.setString(1,reqid);


		} catch (SQLException se){

		}


	}
	
	private void prepareStatements() {
		logger.warn("Preparing statements for connection: " + con);
		try {
			if(con == null){

				throw new SQLException("Connection not available");
			}

			// Get results from IH_DW.DW_ODS_Activity
            sel_getactivityresults = con.prepareStatement("select " +
                    "requisition_id " +
                    "from " +
                    "IH_DW.DW_ODS_ACTIVITY " +
                    "where " +
                    "REQUISITION_ID = 'QA0108A'");

			// Get Result with Requisition_ID
            sel_getresultwithreqid = con.prepareStatement("select * from IH_DW.RESULTS where requisition_id = ? ");

			p_getdata = con.prepareStatement("select re.accession_number, " +
					"		      re.ORDER_TEST_CODE, " +
					"                      re.ORDER_TEST_NAME, " +
					"                      re.RESULT_TEST_CODE, " +
					"                      re.RESULT_TEST_NAME, " +
					"                      re.RESULT_STATUS, " +
					"                      re.TEXTUAL_RESULT, " +
					"                      re.TEXTUAL_RESULT_FULL, " +
					"                      re.NUMERIC_RESULT, " +
					"                      re.UNIT_OF_MEASURE units, " +
					"                      re.REFERENCE_RANGE, " +
					"                      re.ABNORMAL_FLAG, " +
					"                      re.RELEASE_DATE_TIME, " +
					"                      trim(dbms_lob.substr( re.RESULT_COMMENT, 4000, 1 )) as RESULT_COMMENTS, " +
					"                      re.PERFORMING_LAB performing_lab_id, " +
					"                      re.REQUISITION_ID order_number, " +
					"                      re.LOINC_CODE, " +
					"		                  re.LOINC_NAME, " +
					"                      re.VALUE_TYPE, " +
					"                      re.LOINC_CODE, " +
					"		                  re.LOINC_NAME, " +
					"                      re.VALUE_TYPE, " +
					"                      re.MICRO_ISOLATE, " +
					"                      re.MICRO_ORGANISM_NAME, " +
					"                      re.lab_fk, " +
					"                      lo.EXTERNAL_MRN mrn, " +
					"                      lo.ordering_physician_name ordering_physician_name, " +
					"                      lo.PATIENT_TYPE, " +
					"                      lo.INITIATE_ID	patient_id, " +
					"                      lo.ALTERNATE_PATIENT_ID, " +
					"                      lo.REQUISITION_STATUS, " +
					"                      lod.REPORT_NOTES, " +
					"                      lod.SPECIMEN_RECEIVED_DATE_TIME specimen_receive_date, " +
					"                      lod.COLLECTION_DATE collection_date, " +
					"                      lod.COLLECTION_TIME collection_time, " +
					"                      lod.COLLECTION_DATE_TIME, " +
					"                      lod.DRAW_FREQUENCY draw_freq, " +
					"                      lod.RESULT_RPT_CHNG_DATE_TIME res_rprt_status_chng_dt_time, " +
					"                      lod.ORDER_DETAIL_STATUS, " +
					"                      lod.TEST_CATEGORY order_method, " +
					"                      lod.SPECIMEN_METHOD_DESC specimen_source, " +
					"                      lod.ORDER_OCCURRENCE_ID source_of_comment, " +
					"                      f.FACILITY_ID, " +
					"                      f.DISPLAY_NAME facility_name, " +
					"                      null cond_code, " +
					"                      f.ADDRESS_LINE1 facility_address1, " +
					"                      f.ADDRESS_LINE2 facility_address2, " +
					"                      f.CITY facility_city, " +
					"                      f.STATE facility_state, " +
					"                      f.ZIP facility_zip, " +
					"                      f.PHONE_NUMBER facility_phone, " +
					"                      f.EAST_WEST_FLAG, " +
					"                      f.INTERNAL_EXTERNAL_FLAG, " +
					"                      f.ACCOUNT_STATUS facility_account_status, " +
					"                      f.FACILITY_ACTIVE_FLAG, " +
					"                      f.CLINICAL_MANAGER, " +
					"                      f.FACILITY_ID acti_facility_id, " +
					"                      f.FMC_NUMBER, " +
					"                      a.CID, " +
					"                          nvl(p.lname, '') PATIENT_LAST_NAME, " +
					"                            nvl(p.fname, '') PATIENT_FIRST_NAME, " +
					"                            --p.mname PATIENT_MIDDLE_NAME, " +
					"                      ( " +
					"                        CASE " +
					"                          WHEN p.mname is null THEN null " +
					"                          WHEN upper(p.mname) = 'NULL' THEN null " +
					"                          ELSE p.mname " +
					"                        END " +
					"                      ) PATIENT_MIDDLE_NAME, " +
					"                      --p.dob date_of_birth, " +
					"                      --to_date(p.DOB, 'YYYY-MM-DD') date_of_birth, " +
					"                      ( " +
					"                        CASE " +
					"                          WHEN p.DOB is null THEN null " +
					"                          WHEN test_date(p.DOB) = 'Valid' THEN " +
					"                            ( " +
					"                              CASE " +
					"                                WHEN (EXTRACT(YEAR FROM sysdate) - to_number(SUBSTR(replace(p.DOB, '-'), 1, 4))) <= 0 THEN NULL " +
					"                                ELSE to_date(p.DOB, 'YYYY-MM-DD') " +
					"                              END " +
					"                            ) " +
					"                          ELSE NULL " +
					"                        END " +
					"                      ) date_of_birth, " +
					"                      p.sex gender, " +
					"                      p.ssn patient_ssn, " +
					"                        ( " +
					"                        CASE " +
					"                          WHEN p.DOB is null THEN 0 " +
					"                          WHEN test_date(p.DOB) = 'Valid' THEN " +
					"                            ( " +
					"                              CASE " +
					"                                WHEN (EXTRACT(YEAR FROM sysdate) - to_number(SUBSTR(replace(p.DOB, '-'), 1, 4))) <= 0 THEN 0 " +
					"                                ELSE trunc(months_between(sysdate, to_date(p.DOB, 'YYYY-MM-DD'))/12) " +
					"                              END " +
					"                            ) " +
					"                          ELSE NULL " +
					"                        END " +
					"                      ) age, " +
					"                      p.stline1 patient_account_address1, " +
					"                      p.stline2 patient_account_address2, " +
					"                      p.CITY patient_account_city, " +
					"                      p.STATE patient_account_state, " +
					"                      --'NJ' patient_account_state, " +
					"                      --'OH' patient_account_state, " +
					"                      --'IL' patient_account_state, " +
					"                      p.zipcode patient_account_zip, " +
					"                      --p.phnumber patient_home_phone, " +
					"                    ( " +
					"                       CASE " +
					"                         WHEN p.phnumber is null THEN null " +
					"                         WHEN length(p.phnumber) > 10 THEN substr(p.phnumber, ((length(p.phnumber) - 10) + 1)) " +
					"                         ELSE p.phnumber " +
					"                       END " +
					"                     ) patient_home_phone " +
					"                       " +
					"from IH_DW.RESULTS re " +
					"JOIN IH_DW.DIM_LAB_ORDER lo ON lo.requisition_id = re.requisition_id " +
					"JOIN IH_DW.DIM_LAB_ORDER_DETAILS lod ON re.LAB_ORDER_DETAILS_FK = lod.LAB_ORDER_DETAILS_PK " +
					"JOIN IH_DW.DIM_ACCOUNT a ON lo.account_fk = a.account_pk " +
					"JOIN IH_DW.DIM_FACILITY f ON a.facility_fk = f.facility_pk " +
					"JOIN STATERPT_OWNER.PatientMaster p ON p.lab_fk = re.lab_fk and lo.initiate_id = p.eid " +
					"JOIN IH_DW.DIM_LAB dl ON lo.lab_fk = dl.lab_pk " +
					"where re.requisition_id = ?");



			// Insert row into ASR_Process_Tracking table
			//Parameters: NJ(String)
			sp_process_insert = con.prepareCall("{call SP_ASR_PROCESS_TRACKING_INSERT( ?)} ");

			// Select all record from State_Master
			// Parameters: NJ(String), active(String)
			selStateMaster = con.prepareStatement("select * from STATERPT_OWNER.STATE_MASTER " +
					"where state_master_pk is not null and state_abbreviation = ? and status = ? ");

			sp_trackresults = con.prepareCall("{call SP_ASR_PROC_TRACK_RESULTS( ?, ?)} ");

			selDistributor = con.prepareStatement("select s.state, s.state_abbreviation, di.DISTRIBUTOR_ITEM_PK, d.distributor_pk, di.DISTRIBUTOR_ITEM, \n" +
					"                 di.DISTRIBUTOR_ITEM_TYPE, di.CREATED_DATE, di.CREATED_BY, di.LAST_UPDATED_DATE, di.LAST_UPDATED_BY, \n" +
					"                 di.DISTRIBUTOR_ITEMS_GROUP, di.DISTRIBUTOR_ITEM_VALUE from STATERPT_OWNER.DISTRIBUTOR_ITEMS di, STATERPT_OWNER.DISTRIBUTOR_ITEMS_MAP dim, \n" +
					"                 STATERPT_OWNER.DISTRIBUTOR d, STATERPT_OWNER.GENERATOR g, STATERPT_OWNER.STATE_MASTER s \n" +
					"           where di.distributor_item_pk is not null and dim.distributor_fk = d.distributor_pk \n" +
					"           and dim.distributor_items_group = di.distributor_items_group and d.state_fk = s.state_master_pk \n" +
					"           and d.generator_fk = g.generator_pk and g.state_fk = s.state_master_pk \n" +
					"           and s.state = ? and s.state_abbreviation = ? and di.status = ? ");


			sel_activity = con.prepareStatement("select " +
					"distinct(act.requisition_id) , f.PHYS_STATE,r.micro_isolate,r.MICRO_ORGANISM_NAME,r.result_test_name, lod.test_category,lo.requisition_status " +
					",lod.RESULT_RPT_CHNG_DATE_TIME,r.TEXTUAL_RESULT, " +
                    "                        r.TEXTUAL_RESULT_FULL, " +
                    "                        r.NUMERIC_RESULT " +
					"from " +
					"IH_DW.RESULTS r, " +
					"IH_DW.DW_ODS_ACTIVITY act, " +
					"IH_DW.DIM_LAB_ORDER lo, IH_DW.DIM_ACCOUNT a," +
					"IH_DW.DIM_LAB_ORDER_DETAILS lod, " +
					"IH_DW.DIM_FACILITY f " +
					"where " +
							"lo.requisition_id = act.requisition_id " +
					"and lo.account_fk = a.account_pk  " +

					"AND f.phys_state = 'NJ' " +
					"AND act.requisition_id = '2606GWH' " +
					//"and act.last_updated_date < '30-JUN-19 12.00.00.000000000 PM' " +
					"and r.LAB_ORDER_FK = lo.LAB_ORDER_PK  " +
					"and r.LAB_ORDER_DETAILS_FK = lod.LAB_ORDER_DETAILS_PK and lo.LAB_ORDER_PK = lod.LAB_ORDER_FK ");
					//"and lod.test_category = 'MICRO' and r.result_test_name = 'MEROPENEM'");

			//sel_Requistion = con.prepareStatement("");



		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

}
	
	
	
	
	

public void disconnect(){
	if(con != null){
		try {
			con.close();
		} catch (SQLException e) {
			
		}
		
	}
	
	dao.disconnect();
	con = null;
	
}



public void Connect() {
	
	
	
	if(con == null){
		logger.warn("Connecting SQLProdDAO..." + con); 
		
		con = (Connection) dao.getConnection();
		prepareStatements();
	}
	
}


}
