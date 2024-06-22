package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.Abcbean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class AbcModel {

	public int nextPK() throws Exception {

		String sql = "SELECT MAX(ID) FROM st_abc";
		Connection conn = null;
		int pk = 0;

		conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			pk = rs.getInt(1);
		}
		rs.close();

		return pk + 1;

	}

	public long add(Abcbean bean) throws ApplicationException, DuplicateRecordException {

		String sql = "INSERT INTO st_abc VALUES(?,?,?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getAmount());
			pstmt.setDate(4, new java.sql.Date(bean.getDate().getTime()));
			pstmt.setString(5, bean.getType());
			pstmt.setString(6, bean.getCompany());
			pstmt.setString(7, bean.getMobile());
	
			// date of birth caste by sql date

			int a = pstmt.executeUpdate();
			System.out.println(a);
			conn.commit();
			pstmt.close();

		} catch (Exception e) {

			try {
				e.printStackTrace();
				conn.rollback();

			} catch (Exception e2) {
				e2.printStackTrace();
				// application exception
				throw new ApplicationException("Exception : add rollback exceptionn" + e2.getMessage());
			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	public void delete(Abcbean bean) throws ApplicationException {

		String sql = "DELETE FROM st_abc WHERE ID=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public Abcbean findByPK(long pk) throws ApplicationException {

		String sql = "SELECT * FROM st_abc WHERE ID=?";
		Abcbean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Abcbean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAmount(rs.getInt(3));
				bean.setDate(rs.getDate(4));
				bean.setType(rs.getString(5));
			bean.setCompany(rs.getString(6));
				bean.setMobile(rs.getString(7));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();

			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	public void update(Abcbean bean) throws ApplicationException, DuplicateRecordException {

		String sql = "UPDATE st_abc SET NAME=?,AMOUNT=?, DATE=?,TYPE=?,COMPANY=?,MOBILE=? WHERE ID=?";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

		
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getAmount());
			pstmt.setDate(3, new java.sql.Date(bean.getDate().getTime()));
			pstmt.setString(4, bean.getType());
			pstmt.setString(5, bean.getCompany());
			pstmt.setString(6, bean.getMobile());
			pstmt.setLong(7, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();

			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public List search(Abcbean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(Abcbean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("SELECT * FROM st_abc where 1=1");
		if (bean != null) {
			
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND NAME like '" + bean.getName() + "%'");
			}
			
			if (bean.getCompany() != null && bean.getCompany().length() > 0) {
				sql.append(" AND COMPANY like '" + bean.getCompany() + "%'");
			}
			
			if (bean.getType() != null && bean.getType().length() > 0) {
				sql.append(" AND TYPE like '" + bean.getType() + "%'");
			}

			if (bean.getDate() != null && bean.getDate().getTime() > 0) {
				Date d = new java.sql.Date(bean.getDate().getTime());
				sql.append(" AND DATE like '" + d + "%'");
			}

			if (bean.getAmount() > 0) {
				sql.append(" AND AMOUNT = " + bean.getAmount());
			}              
			if (bean.getMobile() != null && bean.getMobile().length() > 0) {
				sql.append(" AND MOBILE like '" + bean.getMobile() + "%'");
			}

			
		
		}
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println(sql);
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new Abcbean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAmount(rs.getInt(3));
				bean.setDate(rs.getDate(4));
				bean.setType(rs.getString(5));
			bean.setCompany(rs.getString(6));
				bean.setMobile(rs.getString(7));
				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {

			throw new ApplicationException("Exception: Exception in Search User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	public List list(int pageNo, int pageSize) throws Exception {

		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_abc");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("preload........" + sql);
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Abcbean bean = new Abcbean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAmount(rs.getInt(3));
				bean.setDate(rs.getDate(4));
				bean.setType(rs.getString(5));
			bean.setCompany(rs.getString(6));
				bean.setMobile(rs.getString(7));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception in getting list of users");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

}
