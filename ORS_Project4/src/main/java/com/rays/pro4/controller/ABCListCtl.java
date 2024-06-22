package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.Abcbean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.AbcModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "ABCListCtl", urlPatterns = { "/ctl/ABCListCtl" })

public class ABCListCtl extends BaseCtl{
	
	protected void preload(HttpServletRequest request) {

		AbcModel model = new AbcModel();
		
		List plist;
		try {
			plist = model.list(0, 0);
			request.setAttribute("plist",plist);
			
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		Abcbean bean = new Abcbean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setDate(DataUtility.getDate(request.getParameter("dob")));

		bean.setAmount(DataUtility.getInt(request.getParameter("amount")));
		
		bean.setType(DataUtility.getString(request.getParameter("type")));

		bean.setName(DataUtility.getString(request.getParameter("name")));
		
		bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
		
		bean.setCompany(DataUtility.getString(request.getParameter("company")));


		return bean;
	}

 	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		List list = null;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		Abcbean bean = (Abcbean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

	AbcModel model = new AbcModel();

		try {
			list = model.search(bean, pageNo, pageSize);
			System.out.println("list" + list);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			ServletUtility.setList(list, request);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			// ServletUtility.setBean(bean, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			
			ServletUtility.handleException(e, request, response);
			return;
		}
		
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));

		Abcbean bean = (Abcbean) populateBean(request);

		

		String[] ids = request.getParameterValues("ids");

		AbcModel model = new AbcModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ABC_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ABC_LIST_CTL, request, response);
			return;
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length > 0) {
				Abcbean deletebean = new Abcbean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (ApplicationException e) {
						
						ServletUtility.handleException(e, request, response);
						return;
					}

					ServletUtility.setSuccessMessage("ABC is Deleted Successfully", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {

			list = model.search(bean, pageNo, pageSize);
			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

		} catch (ApplicationException e) {
		
			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
	
	}

	
	@Override
	protected String getView() {
		return ORSView.ABC_LIST_VIEW;
	}

}



