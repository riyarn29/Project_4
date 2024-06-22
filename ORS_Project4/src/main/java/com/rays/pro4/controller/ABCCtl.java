package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.Abcbean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.AbcModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "ABCCtl", urlPatterns = { "/ctl/ABCCtl" })

public class ABCCtl extends BaseCtl{

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "name"));
			pass = false;

		} 
			  else if (!DataValidator.isName(request.getParameter("name"))) {
			  request.setAttribute("name", "name must contains alphabet only");
			  pass =false;
			  }
		
		if (DataValidator.isNull(request.getParameter("company"))) {
			request.setAttribute("company", PropertyReader.getValue("error.require", "name"));
			pass = false;

		} 
			  else if (!DataValidator.isName(request.getParameter("company"))) {
			  request.setAttribute("company", "company name must contains alphabet only");
			  pass =false;
			  }
		

		if (DataValidator.isNull(request.getParameter("type"))) {
			request.setAttribute("type", PropertyReader.getValue("error.require", "type"));
			pass = false;

		} 
			  else if (!DataValidator.isName(request.getParameter("type"))) {
			  request.setAttribute("type", "type must contains alphabet only");
			  pass =false;
			  }
			 

		if (DataValidator.isNull(request.getParameter("amount"))) {
			request.setAttribute("amount", PropertyReader.getValue("error.require", "amount"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("amount"))) {
			request.setAttribute("amount", "salary  must contains numbers only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "mobile"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
			request.setAttribute("mobile", "Mobile No. contain 10 Digits & Series start with 6-9");
			pass = false;
		}

		return pass;

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
		System.out.println("ABC ctl do get 1111111");
		String op = DataUtility.getString(request.getParameter("operation"));
	
		AbcModel model = new AbcModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {

			Abcbean bean;
			try {
				bean = model.findByPK(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {

				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><>**********" + id + op);

		AbcModel model = new AbcModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			Abcbean bean = (Abcbean) populateBean(request);
			try {
				if (id > 0) {

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" ABC ctl DoPost 222222");
					ServletUtility.setSuccessMessage("ABC is successfully Updated", request);

				} else {
					System.out.println(" ABC ctl DoPost 33333");
					long pk = model.add(bean);

					ServletUtility.setSuccessMessage("ABC is successfully Added", request);

					bean.setId(pk);
				}
				
			} catch (ApplicationException e) {

				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" ABCctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("ABC exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" ABC ctl D p 5555555");

			Abcbean bean = (Abcbean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" ABC ctl D Post  6666666");
				ServletUtility.redirect(ORSView.ABC_CTL, request, response);
				return;
			} catch (ApplicationException e) {

				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" ABC  ctl Do post 77777");

			ServletUtility.redirect(ORSView.ABC_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);

	}


	@Override
	protected String getView() {
		return ORSView.ABC_VIEW;
	}

}


